package com.voizd.search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterators;
import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.dao.entities.ElasticSearchServer;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.search.SearchDAO;
import com.voizd.dao.service.ServiceFactory;
import com.voizd.search.dataobject.ElasticServerConfig;
import com.voizd.search.dataobject.SearchResult;
import com.voizd.search.dataobject.SearchResponse;
import com.voizd.search.exception.ElasticServerClientException;
import com.voizd.search.utils.Constants;
import com.voizd.search.utils.ElasticSearchUtil;
import com.voizd.search.utils.LuceneQueryBuilder;

public class VoizdSearchImpl implements VoizdSearch {
	private static Logger logger = LoggerFactory.getLogger(VoizdSearchImpl.class);
	private volatile static VoizdSearchImpl VoizdSearchImpl;
	protected HttpClient httpClient;
	private SearchDAO searchDAO;
	protected List<ElasticServerConfig> elasticServers;
	protected Iterator<ElasticServerConfig> roundRobinIterator;
	private static String POST="post";
	private static String PUT="put";
	private static String DELETE="delete";
	
	private VoizdSearchImpl() {
		searchDAO = ServiceFactory.getService(SearchDAO.class);
		elasticServers = populateElasticServers();
		initHttpClient();
		this.roundRobinIterator = Iterators.cycle(elasticServers);
	}

	public static VoizdSearchImpl getInstance(){
		if (VoizdSearchImpl == null) {
			synchronized (VoizdSearchImpl.class) {
				if (VoizdSearchImpl == null) {
					VoizdSearchImpl = new VoizdSearchImpl();
				}
			}
		}
		return VoizdSearchImpl;
	}

	protected synchronized ElasticServerConfig getNextElasticSearchServer() {
		if (roundRobinIterator.hasNext())
			return roundRobinIterator.next();
		throw new RuntimeException("No Elastic Server available to connect.");
	}

	protected SearchResponse deserializeResponse(HttpUriRequest request, HttpResponse response, String pathToResult) throws IOException {
		if (request.getMethod().equalsIgnoreCase(Constants.HEAD.getValue())) {
			if (response.getEntity() == null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					response.setEntity(new StringEntity(ElasticSearchUtil.STATUS_OK_TRUE_AND_FOUND_TRUE));
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
					response.setEntity(new StringEntity(ElasticSearchUtil.STATUS_OK_FALSE_AND_FOUND_FALSE));
				}
			}
		}
		return createNewElasticSearchResult(EntityUtils.toString(response.getEntity()), pathToResult);
	}

	protected SearchResponse createNewElasticSearchResult(String json, String pathToResult) {
		SearchResponse result = new SearchResponse();
		logger.debug("createNewElasticSearchResult() ::  json :: "+json+" ,pathToResult :: "+pathToResult );
		Map jsonMap = ElasticSearchUtil.convertJsonStringToMapObject(json);
		result.setJsonString(json);
		result.setJsonMap(jsonMap);
		result.setPathToResult(pathToResult);
	    if(json != null){
	    	result.setSucceeded(true);
	    }else{
	    	result.setSucceeded(false);
	    }
		return result;
	}

	protected boolean isOperationSucceed(Map json, String requestName) {
		try {
			if (requestName.equalsIgnoreCase(Constants.INDEX.getValue())) {
				return (Boolean) json.get(Constants.ok.getValue());
			} else if ((requestName.equalsIgnoreCase(Constants.DELETE.getValue()))) {
				return ((Boolean) json.get(Constants.ok.getValue()) && (Boolean) json.get(Constants.found.getValue()));
			} else if (requestName.equalsIgnoreCase(Constants.UPDATE.getValue())) {
				return (Boolean) json.get(Constants.ok.getValue());
			} else if (requestName.equalsIgnoreCase(Constants.GET.getValue())) {
				return (Boolean) json.get(Constants.exists.getValue());
			} else if (requestName.equalsIgnoreCase(Constants.DELETE_INDEX.getValue())) {
				return ((Boolean) json.get(Constants.ok.getValue()) && (Boolean) json.get(Constants.acknowledged.getValue()));
			}
		} catch (Exception e) {
			logger.error("Exception occurred in parsing result. " + e.getLocalizedMessage(), e);
			return false;
		}
		return true;
	}

	protected List<ElasticServerConfig> populateElasticServers() {
		List<ElasticServerConfig> elasticServerConfigs = null;
		try {
			List<ElasticSearchServer> elasticSearchServers = searchDAO.getAllLuceneSearchServers();
			logger.debug("populateElasticServers  : " + elasticSearchServers);
			elasticServerConfigs = new ArrayList<ElasticServerConfig>();
			for (ElasticSearchServer elasticSearchServer : elasticSearchServers) {
				logger.debug("populateElasticServers  : elasticSearchServer : " + elasticSearchServer.getHost());
				ElasticServerConfig elasticServerConfig = new ElasticServerConfig();
				elasticServerConfig.setHost(elasticSearchServer.getHost());
				elasticServerConfig.setPort(elasticSearchServer.getPort());
				elasticServerConfigs.add(elasticServerConfig);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Error while getting ElasticSearchServer" + e.getLocalizedMessage(), e);
		}
		return elasticServerConfigs;
	}

	protected void initHttpClient() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		httpClient = new DefaultHttpClient(cm);
	}

	protected String getElasticServiceUrl(ElasticServerConfig elasticServerConfig) {
		String baseUrl = getBaseUrl(elasticServerConfig);
		logger.debug("Elastic Search Service url: " + baseUrl);
		return baseUrl;
	}

	protected String getBaseUrl(ElasticServerConfig elasticServerConfig) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Constants.URL_PROTOCOL_PREFIX.getValue()).append(elasticServerConfig.getHost()).append(Constants.HOST_PORT_SEPARATOR.getValue())
				.append(elasticServerConfig.getPort());
		return stringBuilder.toString();
	}

	@Override
	public SearchResponse executeStation(StationSearchVO stationSearchVO, boolean refresh, String type) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResponse searchResponse = null;
		try {
			//logger.debug("executeStation :: type :: "+type+" , stationId :: "+stationSearchVO.getStationId());
			String elasticSearchUrl = ElasticSearchUtil.getStationPutUrl(elasticSearchRestUrl,type,stationSearchVO.getStationId());
			//logger.error("executeContent :: elasticSearchUrl :: "+elasticSearchUrl);
			HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(PUT, elasticSearchUrl, stationSearchVO);
			HttpResponse response = httpClient.execute(request);
			searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		} catch (Exception e) {
			logger.error("Error while positng station info "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while posting station info");
		}
		return searchResponse;
	}

	@Override
	public SearchResponse executeContent(ContentSearchVO contentSearchVO, boolean refresh, String type) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResponse searchResponse = null;
		try {
		//	logger.error("executeContent :: type :: "+type+" , contentId :: "+contentSearchVO.getContentId());
			String elasticSearchUrl = ElasticSearchUtil.getStationPutUrl(elasticSearchRestUrl,type,contentSearchVO.getContentId());
		//	logger.error("executeContent :: elasticSearchUrl :: "+elasticSearchUrl);
			HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(PUT, elasticSearchUrl, contentSearchVO);
			HttpResponse response = httpClient.execute(request);
			searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		} catch (Exception e) {
			logger.error("Error while positng content info "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while posting content info");
		}
		return searchResponse;
	}

	@Override
	public SearchResponse executeDelete(Long Id, boolean refresh, String type) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResponse searchResponse = null;
		try {
			//logger.error("executeDeleteContent :: type :: "+type+" , contentId :: "+contentId);
			String elasticSearchUrl = ElasticSearchUtil.getStationPutUrl(elasticSearchRestUrl,type,Id);
			//logger.error("executeDeleteContent :: elasticSearchUrl :: "+elasticSearchUrl);
			HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(DELETE, elasticSearchUrl,Id);
			HttpResponse response = httpClient.execute(request);
			searchResponse = deserializeResponse(request, response, ElasticSearchUtil.RESULT_PATH);
		} catch (Exception e) {
			logger.error("Error while delete content info "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while delete content info");
		}
		return searchResponse;
	}


	@Override
	public SearchResult executeStationSearch(String text,int startLimit,int pageLimit) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResult searchResult = null; ;
		try {
		String elasticSearchUrl = ElasticSearchUtil.getStationSearchUrl(elasticSearchRestUrl); 
		text = LuceneQueryBuilder.getStationQuery(text,startLimit,pageLimit);
		logger.debug("executeStationSearch :: elasticSearchUrl :: "+elasticSearchUrl+" , text ::"+text);
		HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(POST,elasticSearchUrl,text);
		HttpResponse response = httpClient.execute(request);
		SearchResponse searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		searchResult = ElasticSearchUtil.getStationSearchResult(searchResponse);
		
		} catch (Exception e) {
			logger.error("Error executeStationSearch "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while execute StationSearch");
		}
		return searchResult;
	}

	@Override
	public SearchResult executeContentSearch(String text,int startLimit,int pageLimit) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResult searchResult = null; ;
		try {
		String elasticSearchUrl = ElasticSearchUtil.getContentSearchUrl(elasticSearchRestUrl); 
		text = LuceneQueryBuilder.getContentQuery(text,startLimit,pageLimit);
		logger.debug("executeContentSearch :: elasticSearchUrl :: "+elasticSearchUrl+" , text ::"+text);
		HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(POST,elasticSearchUrl,text);
		HttpResponse response = httpClient.execute(request);
		SearchResponse searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		searchResult = ElasticSearchUtil.getContentSearchResult(searchResponse);
		} catch (Exception e) {
			logger.error("Error executeContentSearch "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while execute executeContentSearch");
		}
		return searchResult;
	}

	@Override
	public SearchResult executeAdvanceStationSearch(SearchVO searchVO, int startLimit, int pageLimit) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResult searchResult = null; ;
		try {
		String elasticSearchUrl = ElasticSearchUtil.getStationSearchUrl(elasticSearchRestUrl); 
		String text = LuceneQueryBuilder.getAdvanceStationQuery(searchVO,startLimit,pageLimit);
		logger.debug("executeAdvanceStationSearch :: elasticSearchUrl :: "+elasticSearchUrl+" , text ::"+text);
		HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(POST,elasticSearchUrl,text);
		HttpResponse response = httpClient.execute(request);
		SearchResponse searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		searchResult = ElasticSearchUtil.getStationSearchResult(searchResponse);
		
		} catch (Exception e) {
			logger.error("Error executeAdvanceStationSearch "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while execute StationSearch");
		}
		return searchResult;
	}

	@Override
	public SearchResult executeAdvanceContentSearch(SearchVO searchVO, int startLimit, int pageLimit) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResult searchResult = null; ;
		try {
		String elasticSearchUrl = ElasticSearchUtil.getContentSearchUrl(elasticSearchRestUrl); 
		String text = LuceneQueryBuilder.getAdvanceContentQuery(searchVO,startLimit,pageLimit);
		//logger.error("executeAdvanceContentSearch :: elasticSearchUrl :: "+elasticSearchUrl+" , text ::"+text);
		HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(POST,elasticSearchUrl,text);
		HttpResponse response = httpClient.execute(request);
		SearchResponse searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		searchResult = ElasticSearchUtil.getContentSearchResult(searchResponse);
		} catch (Exception e) {
			logger.error("Error executeAdvanceContentSearch "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while execute StationSearch");
		}
		return searchResult;
	}

	@Override
	public SearchResponse executeUpdateContent(ContentSearchVO contentSearchVO, boolean refresh, String type) throws ElasticServerClientException {
		// TODO Auto-generated method stub
		return null;
	}

@Override
	public SearchResult executeContentTagSearch(String text, int startLimit,
			int pageLimit) throws ElasticServerClientException {
		String elasticSearchRestUrl = getElasticServiceUrl(getNextElasticSearchServer());
		SearchResult searchResult = null; ;
		try {
		String elasticSearchUrl = ElasticSearchUtil.getContentSearchUrl(elasticSearchRestUrl); 
		text = LuceneQueryBuilder.getContentTagQuery(text,startLimit,pageLimit);
		logger.debug("executeContentSearch :: elasticSearchUrl :: "+elasticSearchUrl+" , text ::"+text);
		HttpUriRequest request = ElasticSearchUtil.constructHttpMethod(POST,elasticSearchUrl,text);
		HttpResponse response = httpClient.execute(request);
		SearchResponse searchResponse = deserializeResponse(request, response,ElasticSearchUtil.RESULT_PATH);
		searchResult = ElasticSearchUtil.getContentSearchResult(searchResponse);
		} catch (Exception e) {
			logger.error("Error executeContentSearch "+e.getLocalizedMessage(),e);
			throw new ElasticServerClientException("Error while execute executeContentSearch");
		}
		return searchResult;
	}
}
