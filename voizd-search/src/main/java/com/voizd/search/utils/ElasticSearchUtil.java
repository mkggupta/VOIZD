package com.voizd.search.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.search.dataobject.SearchResponse;
import com.voizd.search.dataobject.SearchResult;

public class ElasticSearchUtil {
	private static Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
	public static String STATUS_OK_TRUE_AND_FOUND_TRUE = "{\"ok\" : true, \"found\" : true}";
	public static String STATUS_OK_FALSE_AND_FOUND_FALSE = "{\"ok\" : false, \"found\" : false}";
	public static String RESULT_PATH = "hits/hits/_source";
	
	public static HttpUriRequest constructHttpMethod(String methodName, String url, Object data) throws UnsupportedEncodingException {
		logger.debug("constructHttpMethod() :: methodName :: " + methodName + " , url :: " + url);

		if (Constants.POST.getValue().equalsIgnoreCase(methodName)) {
			HttpPost httpPost = new HttpPost(url);
			if (data != null) {
				httpPost.setEntity(new StringEntity(createJsonStringEntity(data), Constants.UTF8.getValue()));
			}
			return httpPost;
		} else if (Constants.PUT.getValue().equalsIgnoreCase(methodName)) {
			HttpPut httpPut = new HttpPut(url);
			if (data != null) {
				//logger.error("constructHttpMethod() :: createJsonStringEntity :: " + createJsonStringEntity(data));
				httpPut.setEntity(new StringEntity(createJsonStringEntity(data), Constants.UTF8.getValue()));
			}
			return httpPut;
		} else if (Constants.DELETE.getValue().equalsIgnoreCase(methodName)) {
			return new HttpDelete(url);
		} else if (Constants.GET.getValue().equalsIgnoreCase(methodName)) {
			HttpGetWithEntity httpGet = new HttpGetWithEntity(url);
			if (data != null) {
				httpGet.setEntity(new StringEntity(createJsonStringEntity(data), Constants.UTF8.getValue()));
			}
			return httpGet;
		} else {
			return null;
		}
	}
	
	private static String createJsonStringEntity(Object data) {
		if (data instanceof String) {
			if (isJson(data.toString())) {
				//logger.error("createJsonStringEntity() :: DATA :: "+data.toString());
				return data.toString();
			} else {
			//	logger.error("createJsonStringEntity() :: DATA else:: "+data.toString());
				return new Gson().toJson(data);
			}
		} else {
			logger.debug("createJsonStringEntity() :: DATA outer else:: "+data.toString());
			return new Gson().toJson(data);
		}
	}

	public static boolean isJson(String data) {
		try {
			//logger.error("isJson() :: data :: "+data);
			JsonElement result = new JsonParser().parse(data);
			return !result.equals(JsonNull.INSTANCE);
		} catch (JsonSyntaxException e) {
			String[] bulkRequest = data.split("\n");
			return bulkRequest.length >= 1;
		}
	}
	
	public static Map convertJsonStringToMapObject(String jsonTxt) {
		try {
			return GsonContextLoader.getGsonContext().fromJson(jsonTxt,
					Map.class);
		} catch (Exception e) {
			logger.error("An exception occurred while converting json string to map object .. jsonText="
					+ jsonTxt);
		}
		return new HashMap();
	}
	
	public static String getStationPutUrl(String baseUrl,String type,Long Id) {
		StringBuilder stringBuilder = new StringBuilder();
		String prefix = null;
		//logger.error("getStationPutUrl() :: type :: "+type);
		if("station".equalsIgnoreCase(type)){
			prefix =Constants.STATAION_ID_PREFIX.getValue();
			stringBuilder.append(baseUrl+"/").append(type).append(Constants.ST.getValue()).append(prefix+Id);
		}else if("content".equalsIgnoreCase(type)){
			prefix =Constants.CONTENT_ID_PREFIX.getValue();	
			stringBuilder.append(baseUrl+"/").append(type).append(Constants.CT.getValue()).append(prefix+Id);
		}
		
		return stringBuilder.toString();
	}

	public static String getStationSearchUrl(String baseUrl) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseUrl).append(Constants.STATION.getValue()).append(Constants.ST.getValue()).append(Constants.SEARCH.getValue());
		return stringBuilder.toString();
	}
	
	public static String getContentSearchUrl(String baseUrl) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseUrl).append(Constants.CONTENT.getValue()).append(Constants.CT.getValue()).append(Constants.SEARCH.getValue());
		return stringBuilder.toString();
	}
	
	public static SearchResult getStationSearchResult(SearchResponse searchResponse) {
		SearchResult searchResult = new SearchResult() ;
		List<StationSearchVO>  listStationSearchVO = searchResponse.getSourceAsObjectList(StationSearchVO.class);
		//logger.error("getStationSearchResult() :: listStationSearchVO :: "+listStationSearchVO);
		searchResult.setStationSearchVOList(listStationSearchVO);
		//logger.error("getStationSearchResult() :: getTotalCount :: "+searchResponse.getTotalCount().intValue());
		searchResult.setSize(searchResponse.getTotalCount().intValue());
		return searchResult;
	}
	
	public static SearchResult  getContentSearchResult(SearchResponse searchResponse) {
		SearchResult searchResult = new SearchResult() ;
		List<ContentSearchVO>  stationSearchVOList = searchResponse.getSourceAsObjectList(ContentSearchVO.class);
		logger.debug("getContentSearchResult ::  "+stationSearchVOList);
		searchResult.setContentSearchVOList(stationSearchVOList);
		searchResult.setSize(searchResponse.getTotalCount().intValue());
		return searchResult;
	}
	
}
