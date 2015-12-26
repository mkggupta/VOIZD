package com.voizd.media.client;


import com.voizd.common.beans.vo.MediaServerVO;
import com.voizd.common.util.http.HttpUtil;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.service.ServiceFactory;
import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.exception.MediaClientException;
import com.voizd.media.dataobject.exception.MediaServiceException;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;
import com.voizd.media.dataobject.request.BaseMediaRequest;
import com.voizd.media.dataobject.request.check.GetFileCheckRequest;
import com.voizd.media.dataobject.request.convert.ConvertAudioRequest;
import com.voizd.media.dataobject.request.convert.ConvertGetMediaRequest;
import com.voizd.media.dataobject.request.convert.ConvertMediaRequest;
import com.voizd.media.dataobject.request.cut.CutGetMediaRequest;
import com.voizd.media.dataobject.request.cut.CutMediaRequest;
import com.voizd.media.dataobject.request.get.GetMediaRequest;
import com.voizd.media.dataobject.request.merge.MergeMediaRequest;
import com.voizd.media.dataobject.request.post.PostMediaRequest;
import com.voizd.media.dataobject.request.thumb.CreateGetThumbMediaRequest;
import com.voizd.media.dataobject.request.thumb.CreateThumbMediaRequest;
import com.voizd.media.dataobject.request.thumb.GetThumbMediaRequest;
import com.voizd.media.utils.file.MediaFileFactory;
import com.voizd.media.utils.gson.GsonContextLoader;
import com.voizd.media.utils.jmx.MBeanUtils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.voizd.media.dataobject.MediaConstants.*;

/**
 * User: shalabh
 * Date: 26/6/12
 * Time: 2:16 PM
 */
public class MediaServerClientImpl implements MediaServerClient, MediaServerClientImplMBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServerClientImpl.class);

    private volatile static MediaServerClientImpl mediaServerClient_;

    private MediaDAO mediaDAO;
 

	private HttpClient httpClient_;
    private MediaServerConfig[] mediaServers_;
    private InactiveServerTracker inactiveServerTracker_;
    private Map<MediaServerConfig, String> mediaServerPingUrlMap_;
    private Map<MediaServerKey, RoundRobinIterator> roundRobinServerMap_;
    private MediaServiceStatistics statistics_;

    private MediaServerClientImpl() throws SQLException, MediaClientException {
    	mediaDAO = ServiceFactory.getService(MediaDAO.class);
        statistics_ = new MediaServiceStatistics();
        initMediaServers();
        initHttpClient();
        initRoundRobinScheduler(mediaServers_);
        scheduleInactiveServerTracker();
        registerMBean();
    }

    private void registerMBean() {
        try {
            MBeanUtils.registerMBean(MBEAN_SERVICE_NAME, MBEAN_MEDIA_VALUE, MEDIA_SERVER_CLIENT_MBEAN_NAME, this);
        } catch (Exception e) {
            LOGGER.error("Error while registering MBean for media server client", e);
        }
    }

    private void initHttpClient() {
        int maxConnectionsPerHost = HTTP_CLIENT_MAX_CONNECTIONS_PER_HOST;
        int maxTotalConnections = HTTP_CLIENT_MAX_TOTAL_CONNECTIONS;
        LOGGER.info("Initializing http client with maxConnectionsPerHost: " + maxConnectionsPerHost + "::maxTotalConnections: " + maxTotalConnections);
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
        httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
        httpConnectionManagerParams.setMaxTotalConnections(maxTotalConnections);
        httpConnectionManager.setParams(httpConnectionManagerParams);
        httpClient_ = new HttpClient(httpConnectionManager);
    }

    public static MediaServerClient getInstance() throws SQLException, MediaClientException {
        if (mediaServerClient_ == null) {
            synchronized (MediaServerClientImpl.class) {
                if (mediaServerClient_ == null) {
                    mediaServerClient_ = new MediaServerClientImpl();
                }
            }
        }
        return mediaServerClient_;
    }

    private void initMediaServers() throws SQLException, MediaClientException {
        mediaServers_ = populateMediaServers();
        mediaServerPingUrlMap_ = new HashMap<MediaServerConfig, String>();
        for (MediaServerConfig mediaServerConfig : mediaServers_) {
            String pingUrl = getPingUrl(mediaServerConfig);
            mediaServerPingUrlMap_.put(mediaServerConfig, pingUrl);
        }
    }

    private MediaServerConfig[] populateMediaServers() throws SQLException, MediaClientException {
    	List<MediaServerVO> mediaServerDataObjects = null;
        		try {
        			mediaServerDataObjects = mediaDAO.getAllMediaServers();
				} catch (DataAccessFailedException e) {
					 LOGGER.info("Get Media Servers list data base call fail: "+e.getLocalizedMessage(),e);
				}
        Set<MediaServerConfig> mediaServerConfigs = new HashSet<MediaServerConfig>();
        for (MediaServerVO mediaServerDataObject : mediaServerDataObjects) {
            String[] supportedOperations = mediaServerDataObject.getOperation().split(",");
            MediaOperation[] mediaOperations = populateMediaOperations(supportedOperations);
            String[] supportedCategories = mediaServerDataObject.getCategory().split(",");
            MediaCategory[] mediaCategories = populateMediaCategories(supportedCategories);
            String[] supportedMediaTypes = mediaServerDataObject.getMediaType().split(",");
            com.voizd.media.dataobject.MediaType[] mediaTypes = populateMediaTypes(supportedMediaTypes);
            MediaServerConfig mediaServerConfig = new MediaServerConfig(mediaServerDataObject.getHost(), mediaServerDataObject.getPort(), mediaOperations, mediaCategories, mediaTypes);
            mediaServerConfigs.add(mediaServerConfig);
        }
        return mediaServerConfigs.toArray(new MediaServerConfig[mediaServerConfigs.size()]);
    }

    private com.voizd.media.dataobject.MediaType[] populateMediaTypes(String[] supportedMediaTypes) throws MediaClientException {
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        for (String supportedMediaType : supportedMediaTypes) {
            try {
                MediaType mediaType = MediaType.getMediaTypeFromCode(supportedMediaType);
                mediaTypes.add(mediaType);
            } catch (Exception e) {
                // throw new MediaClientException("Invalid media type " + supportedMediaType, e);
            	LOGGER.error("Invalid media type " + supportedMediaType, e);
            }
        }
        return mediaTypes.toArray(new MediaType[mediaTypes.size()]);
    }

    private MediaCategory[] populateMediaCategories(String[] supportedCategories) throws MediaClientException {
        List<MediaCategory> mediaCategories = new ArrayList<MediaCategory>();
        for (String supportedCategory : supportedCategories) {
            try {
                MediaCategory mediaCategory = MediaCategory.getMediaCategoryForCode(supportedCategory);
                mediaCategories.add(mediaCategory);
            } catch (Exception e) {
               // throw new MediaClientException("Invalid media category " + supportedCategory, e);
            	LOGGER.error("Invalid media category " + supportedCategory, e);
            }
        }
        return mediaCategories.toArray(new MediaCategory[mediaCategories.size()]);
    }

    private MediaOperation[] populateMediaOperations(String[] supportedOperations) throws MediaClientException {
        List<MediaOperation> mediaOperations = new ArrayList<MediaOperation>();
        for (String supportedOperation : supportedOperations) {
            try {
                MediaOperation mediaOperation = MediaOperation.getMediaOperation(Integer.parseInt(supportedOperation));
                mediaOperations.add(mediaOperation);
            } catch (Exception e) {
                //throw new MediaClientException("Invalid operation " + supportedOperation, e);
            	LOGGER.error("Invalid operation " + supportedOperation, e);
            }
        }
        return mediaOperations.toArray(new MediaOperation[mediaOperations.size()]);
    }

    private void initRoundRobinScheduler(MediaServerConfig[] mediaServers) {
        roundRobinServerMap_ = new HashMap<MediaServerKey, RoundRobinIterator>();
        Map<MediaServerKey, Set<MediaServerConfig>> mediaServerMap = new HashMap<MediaServerKey, Set<MediaServerConfig>>();
        for (MediaServerConfig mediaServerConfig : mediaServers) {
            MediaOperation[] mediaOperations = getOperationsForServer(mediaServerConfig);
            MediaCategory[] mediaCategories = getCategoriesForServer(mediaServerConfig);
            MediaType[] mediaTypes = getTypesForServer(mediaServerConfig);
            for (MediaOperation mediaOperation : mediaOperations) {
                for (MediaCategory mediaCategory : mediaCategories) {
                    for (MediaType mediaType : mediaTypes) {
                        MediaServerKey mediaServerKey = new MediaServerKey(mediaOperation, mediaCategory, mediaType);
                        Set<MediaServerConfig> mediaServerConfigs = mediaServerMap.get(mediaServerKey);
                        if (mediaServerConfigs == null) {
                            mediaServerConfigs = new HashSet<MediaServerConfig>();
                            mediaServerMap.put(mediaServerKey, mediaServerConfigs);
                        }
                        mediaServerConfigs.add(mediaServerConfig);
                    }
                }
            }
        }
        for (Map.Entry<MediaServerKey, Set<MediaServerConfig>> entry : mediaServerMap.entrySet()) {
            MediaServerKey mediaServerKey = entry.getKey();
            Set<MediaServerConfig> mediaServerConfigs = entry.getValue();
            roundRobinServerMap_.put(mediaServerKey, new RoundRobinIterator(mediaServerConfigs.toArray(new MediaServerConfig[mediaServerConfigs.size()])));
        }
    }

    private MediaType[] getTypesForServer(MediaServerConfig mediaServerConfig) {
        Set<MediaType> mediaTypes = new HashSet<MediaType>();
        for (MediaType mediaType : mediaServerConfig.getMediaTypes()) {
            if (mediaType.equals(MediaType.ALL)) {
                mediaTypes.addAll(Arrays.asList(getAllMediaTypes()));
            } else {
                mediaTypes.add(mediaType);
            }
        }
        return mediaTypes.toArray(new MediaType[mediaTypes.size()]);
    }

    private MediaCategory[] getCategoriesForServer(MediaServerConfig mediaServerConfig) {
        Set<MediaCategory> mediaCategories = new HashSet<MediaCategory>();
        for (MediaCategory mediaCategory : mediaServerConfig.getCategories()) {
            if (mediaCategory.equals(MediaCategory.ALL)) {
                mediaCategories.addAll(Arrays.asList(getAllCategories()));
            } else {
                mediaCategories.add(mediaCategory);
            }
        }
        return mediaCategories.toArray(new MediaCategory[mediaCategories.size()]);
    }

    private MediaOperation[] getOperationsForServer(MediaServerConfig mediaServerConfig) {
        Set<MediaOperation> mediaOperations = new HashSet<MediaOperation>();
        for (MediaOperation mediaOperation : mediaServerConfig.getOperations()) {
            if (mediaOperation.equals(MediaOperation.ALL)) {
                mediaOperations.addAll(Arrays.asList(getAllOperations()));
            } else {
                mediaOperations.add(mediaOperation);
            }
        }
        return mediaOperations.toArray(new MediaOperation[mediaOperations.size()]);
    }

    private MediaOperation[] getAllOperations() {
        List<MediaOperation> mediaOperations = new ArrayList<MediaOperation>();
        for (MediaOperation mediaOperation : MediaOperation.values()) {
            if (!mediaOperation.equals(MediaOperation.ALL)) {
                mediaOperations.add(mediaOperation);
            }
        }
        return mediaOperations.toArray(new MediaOperation[mediaOperations.size()]);
    }

    private MediaCategory[] getAllCategories() {
        List<MediaCategory> mediaCategories = new ArrayList<MediaCategory>();
        for (MediaCategory mediaCategory : MediaCategory.values()) {
            if (!mediaCategory.equals(MediaCategory.ALL)) {
                mediaCategories.add(mediaCategory);
            }
        }
        return mediaCategories.toArray(new MediaCategory[mediaCategories.size()]);
    }

    private MediaType[] getAllMediaTypes() {
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        for (MediaType mediaType : MediaType.values()) {
            if (!mediaType.equals(MediaType.ALL)) {
                mediaTypes.add(mediaType);
            }
        }
        return mediaTypes.toArray(new MediaType[mediaTypes.size()]);
    }

    private void scheduleInactiveServerTracker() {
        inactiveServerTracker_ = new InactiveServerTracker();
        Thread thread = new Thread(inactiveServerTracker_, "MediaServer-InactiveServerTracker");
        thread.setDaemon(true);
        thread.start();
    }

    private MediaServerConfig getNextMediaServer(MediaOperation mediaOperation, MediaCategory mediaCategory, MediaType mediaType) {
        MediaServerKey mediaServerKey = new MediaServerKey(mediaOperation, mediaCategory, mediaType);
        RoundRobinIterator roundRobinIterator = roundRobinServerMap_.get(mediaServerKey);
        if (roundRobinIterator != null) {
            MediaServerConfig mediaServerConfig = roundRobinIterator.hasNext() ? roundRobinIterator.next() : null;
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Next active media server: " + mediaServerConfig);
            }*/
            return mediaServerConfig;
        } else {
            LOGGER.warn("No media server found for " + mediaServerKey);
        }
        return null;
    }

    private String getBaseUrl(MediaServerConfig mediaServerConfig) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_PROTOCOL_PREFIX).append(mediaServerConfig.getHost()).append(HOST_PORT_SEPARATOR)
                .append(mediaServerConfig.getPort()).append(MEDIA_SERVICE_NAME);
        return stringBuilder.toString();
    }

    private String getPingUrl(MediaServerConfig mediaServerConfig) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBaseUrl(mediaServerConfig)).append(PING_URL_SUFFIX);
        return stringBuilder.toString();
    }

    private String getMediaServiceUrl(MediaServerConfig mediaServerConfig, BaseMediaRequest baseMediaRequest) {
        String baseUrl = getBaseUrl(mediaServerConfig);
        String mediaServiceUrl = baseMediaRequest.getServiceUrl(baseUrl);
       /* if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Media Service url: " + mediaServiceUrl);
        }*/
        return mediaServiceUrl;
    }
    
    private String getMediaServicebaseUrl(MediaServerConfig mediaServerConfig) {
        return  getBaseUrl(mediaServerConfig);
    }

    private void addToActiveServers(MediaServerConfig mediaServerConfig) {
     /*   if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding media server to active servers list: " + mediaServerConfig);
        }*/
        mediaServerConfig.setActive(true);
        inactiveServerTracker_.removeFromInactiveServers(mediaServerConfig);
    }

    private void addToInactiveServers(MediaServerConfig mediaServerConfig) {
     /*   if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding media server to inactive servers list: " + mediaServerConfig);
        }*/
        mediaServerConfig.setActive(false);
        inactiveServerTracker_.addToInactiveServers(mediaServerConfig);
    }

    public MediaResponse postMedia(PostMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        return getPostMediaResponse(mediaRequest, mediaRequest.getData());
    }

    public byte[] getMedia(GetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received get media request");
            }*/
            return executeGetRequest(mediaRequest);
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public String convertMedia(ConvertMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received convert media request");
            }*/
            byte[] response = executeGetRequest(mediaRequest);
            String fileId = (response != null) ? new String(response) : null;
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media converted successfully...Generated file id: " + fileId);
            }*/
            return fileId;
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public byte[] convertAndGetMedia(ConvertGetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
            /*if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received convert and get media request");
            }*/
            return executeGetRequest(mediaRequest);
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public MediaResponse mergeMedia(MergeMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        return getPostMediaResponse(mediaRequest, mediaRequest.getSecondMediaData());
    }

    public String cutMedia(CutMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received cut media request");
            }*/
            byte[] response = executeGetRequest(mediaRequest);
            String fileId = (response != null) ? new String(response) : null;
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media cut successfully...Generated file id: " + fileId);
            }*/
            return fileId;
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public byte[] cutAndGetMedia(CutGetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received cut and get media request");
            }*/
            return executeGetRequest(mediaRequest);
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public byte[] createAndGetThumbnail(CreateGetThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received create and get thumbnail request");
            }*/
            return executeGetRequest(mediaRequest);
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public String createThumbnail(CreateThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received create thumbnail request");
            }*/
            byte[] response = executeGetRequest(mediaRequest);
            String fileId = (response != null) ? new String(response) : null;
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Thumbnail created successfully...Generated file id: " + fileId);
            }*/
            return fileId;
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    public byte[] getThumbnail(GetThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received get thumbnail request");
            }*/
            return executeGetRequest(mediaRequest);
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }
    
    public boolean fileExist(GetFileCheckRequest mediaRequest) throws MediaServiceException, MediaClientException{
    	byte[] response = executeCheckRequest(mediaRequest);  	 
    	if (response == null) {
    		return false;
    	}
    	try{
    		return Boolean.valueOf(new String(response));
    	} catch(Exception e){
    		LOGGER.error("Error while parsing response " + response + "; Error: " + e.getMessage(), e);

    	}
    	return false;
    }
    
    private byte[] executeCheckRequest(BaseMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        MediaServerConfig mediaServerConfig = getNextMediaServer(mediaRequest.getMediaOperation(), mediaRequest.getMediaCategory(), mediaRequest.getMediaType());
        if (mediaServerConfig != null) {
            String mediaServiceUrl = getMediaServiceUrl(mediaServerConfig, mediaRequest);
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media server " + mediaServerConfig + " found for " + mediaRequest.getMediaOperation() + "; " + mediaRequest.getMediaCategory() + "; " + mediaRequest.getMediaType());
                LOGGER.debug("Media service url: " + mediaServiceUrl);
            }*/
            if (mediaServiceUrl != null) {
                try {
                    return executeGetRequest(mediaServiceUrl);
                } catch (IOException e) {
                    LOGGER.warn("Error while executing request for url " + mediaServiceUrl + "...Sending request to another media server", e);
                    addToInactiveServers(mediaServerConfig);
                    return executeCheckRequest(mediaRequest);
                } catch (MediaServiceException e) {
                    LOGGER.error("Error while executing request for url " + mediaServiceUrl + "; Error: " + e.getErrorCode(), e);
                    throw e;
                }
            } else {
                throw new MediaClientException("Invalid media server url");
            }
        } else {
            throw new MediaClientException("No active media server found for operation: " + mediaRequest.getMediaOperation().getValue() +
                    "; category: " + mediaRequest.getMediaCategory().getValue() + "; type: " + mediaRequest.getMediaType().getValue());
        }
    }

    private void updateStatistics(MediaOperation mediaOperation, long startTime) {
        statistics_.incrementTotalRequests(mediaOperation);
        statistics_.updateResponseTime(mediaOperation, System.currentTimeMillis() - startTime);
    }

    private MediaResponse getPostMediaResponse(BaseMediaRequest mediaRequest, byte[] data) throws MediaServiceException, MediaClientException {
        long startTime = System.currentTimeMillis();
        try {
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received " + mediaRequest.getMediaOperation().getValue() + " media request");
            }*/
            byte[] response = executePostRequest(mediaRequest, data);
            if (response == null) {
                return null;
            }
            String jsonResponse = new String(response);
           /* if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Json response for media " + mediaRequest.getMediaOperation().getValue() + ": " + jsonResponse);
            }*/
            MediaResponse mediaResponse = GsonContextLoader.getGsonContext().fromJson(jsonResponse, MediaResponse.class);
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media " + mediaRequest.getMediaOperation().getValue() + " successful...Generated file id: " + mediaResponse.getFileId());
            }*/
            return mediaResponse;
        } finally {
            updateStatistics(mediaRequest.getMediaOperation(), startTime);
        }
    }

    private byte[] executeGetRequest(BaseMediaRequest mediaRequest) throws MediaServiceException, MediaClientException {
        MediaServerConfig mediaServerConfig = getNextMediaServer(mediaRequest.getMediaOperation(), mediaRequest.getMediaCategory(), mediaRequest.getMediaType());
        if (mediaServerConfig != null) {
            String mediaServiceUrl = getMediaServiceUrl(mediaServerConfig, mediaRequest);
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media server " + mediaServerConfig + " found for " + mediaRequest.getMediaOperation() + "; " + mediaRequest.getMediaCategory() + "; " + mediaRequest.getMediaType());
                LOGGER.debug("Media service url: " + mediaServiceUrl);
            }*/
            if (mediaServiceUrl != null) {
                try {
                    return executeGetRequest(mediaServiceUrl);
                } catch (IOException e) {
                    LOGGER.warn("Error while executing request for url " + mediaServiceUrl + "...Sending request to another media server", e);
                    statistics_.incrementIOExceptions();
                    addToInactiveServers(mediaServerConfig);
                    return executeGetRequest(mediaRequest);
                } catch (MediaServiceException e) {
                    LOGGER.error("Error while executing request for url " + mediaServiceUrl + "; Error: " + e.getErrorCode(), e);
                    statistics_.incrementMediaServerErrorsForCode(e.getErrorCode().getCode());
                    throw e;
                }
            } else {
                throw new MediaClientException("Invalid media server url");
            }
        } else {
            throw new MediaClientException("No active media server found for operation: " + mediaRequest.getMediaOperation().getValue() +
                    "; category: " + mediaRequest.getMediaCategory().getValue() + "; type: " + mediaRequest.getMediaType().getValue());
        }
    }

    private byte[] executeGetRequest(String url) throws IOException, MediaServiceException {
        GetMethod getMethod = new GetMethod(url);
        return executeHttpMethod(url, getMethod);
    }

    private byte[] executePostRequest(BaseMediaRequest mediaRequest, byte[] data) throws MediaServiceException, MediaClientException {
        MediaServerConfig mediaServerConfig = getNextMediaServer(mediaRequest.getMediaOperation(), mediaRequest.getMediaCategory(), mediaRequest.getMediaType());
        if (mediaServerConfig != null) {
            String mediaServiceUrl = getMediaServiceUrl(mediaServerConfig, mediaRequest);
          /*  if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Media server " + mediaServerConfig + " found for " + mediaRequest.getMediaOperation() + "; " + mediaRequest.getMediaCategory() + "; " + mediaRequest.getMediaType());
                LOGGER.debug("Media service url: " + mediaServiceUrl);
            }*/
            if ((mediaServiceUrl != null) && (mediaServiceUrl.length() > 0)) {
                try {
                    return executePostRequest(mediaServiceUrl, data);
                } catch (IOException e) {
                    LOGGER.warn("Error while executing request for url " + mediaServiceUrl + "...Sending request to another media server", e);
                    statistics_.incrementIOExceptions();
                    addToInactiveServers(mediaServerConfig);
                    return executePostRequest(mediaRequest, data);
                } catch (MediaServiceException e) {
                    LOGGER.error("Error while executing request for url " + mediaServiceUrl);
                    statistics_.incrementMediaServerErrorsForCode(e.getErrorCode().getCode());
                    throw e;
                }
            } else {
                throw new MediaClientException("Invalid media server url");
            }
        } else {
            throw new MediaClientException("No active media server found for operation: " + mediaRequest.getMediaOperation().getValue() +
                    "; category: " + mediaRequest.getMediaCategory().getValue() + "; type: " + mediaRequest.getMediaType().getValue());
        }
    }

    private byte[] executePostRequest(String url, byte[] data) throws IOException, MediaServiceException {
        PostMethod postMethod = new PostMethod(url);
        if ((data != null) && (data.length > 0)) {
            RequestEntity requestEntity = new ByteArrayRequestEntity(data, APPLIICATION_OCTET_STREAM_CONTENT_TYPE);
            postMethod.setRequestEntity(requestEntity);
        }
        return executeHttpMethod(url, postMethod);
    }

    private byte[] executeHttpMethod(String url, HttpMethodBase httpMethodBase) throws IOException, MediaServiceException {
        try {
            int responseCode = httpClient_.executeMethod(httpMethodBase);
            if (responseCode != HttpStatus.SC_OK) {
                LOGGER.warn("Http request failed for url " + url + "::Response code: " + responseCode);
                MediaErrorCode errorCode = MediaErrorCode.UNKNOWN_ERROR;
                Header errorResponseHeader = httpMethodBase.getResponseHeader(ERROR_CODE_RESPONSE_HEADER_NAME);
                if (errorResponseHeader != null) {
                    errorCode = MediaErrorCode.getMediaErrorCode(Integer.parseInt(errorResponseHeader.getValue()));
                }
                throw new MediaServiceException("Error while executing request for url " + url, errorCode);
            }
            return httpMethodBase.getResponseBody();
        } finally {
            httpMethodBase.releaseConnection();
        }
    }
    
    private String executeMethod(String url) throws IOException, MediaServiceException {
        GetMethod getMethod = new GetMethod(url);
        return executeMethod(url, getMethod);
    }
    
    private String executeMethod(String url, HttpMethodBase httpMethodBase) throws IOException, MediaServiceException {
        try {
            int responseCode = httpClient_.executeMethod(httpMethodBase);
            if (responseCode != HttpStatus.SC_OK) {
                LOGGER.warn("Http request failed for url " + url + "::Response code: " + responseCode);
                MediaErrorCode errorCode = MediaErrorCode.UNKNOWN_ERROR;
                Header errorResponseHeader = httpMethodBase.getResponseHeader(ERROR_CODE_RESPONSE_HEADER_NAME);
                if (errorResponseHeader != null) {
                    errorCode = MediaErrorCode.getMediaErrorCode(Integer.parseInt(errorResponseHeader.getValue()));
                }
                throw new MediaServiceException("Error while executing request for url " + url, errorCode);
            }
            return httpMethodBase.getResponseBodyAsString();
        } finally {
            httpMethodBase.releaseConnection();
        }
    }

    //reason for using String array is that MXBeans are not supported in jdk1.5
    public String[] getAllMediaServers() {
        Set<String> mediaServers = new HashSet<String>();
        for (MediaServerConfig mediaServerConfig : mediaServers_) {
            mediaServers.add(mediaServerConfig.toString());
        }
        return mediaServers.toArray(new String[mediaServers.size()]);
    }

    public String getGetRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.GET);
    }

    public String getPostRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.POST);
    }

    public String getConvertRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CONVERT);
    }

    public String getConvertGetRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CONVERTGET);
    }

    public String getMergeRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.MERGE);
    }

    public String getCutRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CUT);
    }

    public String getCutGetRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CUTGET);
    }

    public String getCreateGetThumbnailRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CREATETHUMBGET);
    }

    public String getCreateThumbnailRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.CREATETHUMB);
    }

    public String getGetThumbnailRequestsStats() {
        return statistics_.getMediaRequestStats(MediaOperation.GETTHUMBNAIL);
    }

    public int getTotalIOExceptions() {
        return statistics_.totalIOExceptions_.get();
    }

    public int getTotalMediaServerErrors() {
        return statistics_.getTotalMediaServerErrors();
    }

    //reason for using String array is that MXBeans are not supported in jdk1.5
    public String[] getInactiveMediaServers() {
        Set<MediaServerConfig> mediaServerConfigs = inactiveServerTracker_.inactiveMediaServers_.keySet();
        Set<String> inactiveMediaServers = new HashSet<String>();
        for (MediaServerConfig mediaServerConfig : mediaServerConfigs) {
            inactiveMediaServers.add(mediaServerConfig.toString());
        }
        return inactiveMediaServers.toArray(new String[inactiveMediaServers.size()]);
    }

    public int getIOErrors() {
        return statistics_.mediaServerErrorCountMap_.get(MediaErrorCode.ERROR_IO.getCode()).get();
    }

    public int getInternalServerErrors() {
        return statistics_.mediaServerErrorCountMap_.get(MediaErrorCode.ERROR_INTERNAL_SERVER_ERROR.getCode()).get();
    }

    public int getInvalidRequestErrors() {
        return statistics_.mediaServerErrorCountMap_.get(MediaErrorCode.ERROR_INVALID_REQUEST_DATA.getCode()).get();
    }

    public int getFFMpegOverloadErrors() {
        return statistics_.mediaServerErrorCountMap_.get(MediaErrorCode.ERROR_MAX_TRANSCODER_INSTANCE_LIMIT.getCode()).get();
    }

    public int getUnknownMediaServerErrors() {
        return statistics_.mediaServerErrorCountMap_.get(MediaErrorCode.UNKNOWN_ERROR.getCode()).get();
    }

    private class RoundRobinIterator implements Iterator<MediaServerConfig> {
        private ArrayList<MediaServerConfig> mediaServerConfigs_;
        private AtomicInteger index_;

        private RoundRobinIterator(MediaServerConfig[] mediaServerConfigs) {
            mediaServerConfigs_ = new ArrayList<MediaServerConfig>(Arrays.asList(mediaServerConfigs));
            Random random = new Random();
            index_ = new AtomicInteger(random.nextInt(mediaServerConfigs_.size()));
        }

        public boolean hasNext() {
            return mediaServerConfigs_.size() > 0;
        }

        public MediaServerConfig next() {
            MediaServerConfig mediaServerConfig = null;
            int count = 0;
            while (((mediaServerConfig == null) || (!mediaServerConfig.getActive().get())) && (count++ < mediaServerConfigs_.size())) {
                mediaServerConfig = mediaServerConfigs_.get(index_.getAndIncrement() % mediaServerConfigs_.size());
            }
            return (mediaServerConfig != null) ?
                    (mediaServerConfig.getActive().get() ? mediaServerConfig : null)
                    : null;
        }

        public void remove() {
            //Not supported
        }
    }

    private class InactiveServerTracker implements Runnable {
        private Map<MediaServerConfig, MediaServerConfig> inactiveMediaServers_;

        private InactiveServerTracker() {
            inactiveMediaServers_ = new ConcurrentHashMap<MediaServerConfig, MediaServerConfig>();
        }

        private void addToInactiveServers(MediaServerConfig mediaServerConfig) {
            inactiveMediaServers_.put(mediaServerConfig, mediaServerConfig);
        }

        private void removeFromInactiveServers(MediaServerConfig mediaServerConfig) {
            inactiveMediaServers_.remove(mediaServerConfig);
        }

        public void run() {
            while (true) {
                try {
                    for (MediaServerConfig mediaServerConfig : inactiveMediaServers_.keySet()) {
                        try {
                            String pingUrl = mediaServerPingUrlMap_.get(mediaServerConfig);
                            LOGGER.info("Checking ping url " + pingUrl + " for media server " + mediaServerConfig);
                            URL url = new URL(pingUrl);
                            HttpUtil.accessURL(url, DEFAULT_SOCKET_TIMEOUT);
                            addToActiveServers(mediaServerConfig);
                        } catch (Exception e) {
                            LOGGER.error("Error while tracking heartbeat for " + mediaServerConfig, e);
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.error("Error while running inactive server tracker", ex);
                } finally {
                    try {
                        Thread.sleep(INACTIVE_SERVER_TRACKER_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOGGER.error("Inactive Server tracker thread interrupted", e);
                    }
                }
            }
        }
    }

    private class MediaServerKey {
        private MediaOperation mediaOperation_;
        private MediaCategory mediaCategory_;
        private MediaType mediaType_;

        private MediaServerKey(MediaOperation mediaOperation, MediaCategory mediaCategory, MediaType mediaType) {
            mediaOperation_ = mediaOperation;
            mediaCategory_ = mediaCategory;
            mediaType_ = mediaType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MediaServerKey that = (MediaServerKey) o;

            if (mediaCategory_ != that.mediaCategory_) return false;
            if (mediaOperation_ != that.mediaOperation_) return false;
            if (mediaType_ != that.mediaType_) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mediaOperation_.hashCode();
            result = 31 * result + mediaCategory_.hashCode();
            result = 31 * result + mediaType_.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "MediaServerKey{" +
                    "mediaOperation_=" + mediaOperation_ +
                    ", mediaCategory_=" + mediaCategory_ +
                    ", mediaType_=" + mediaType_ +
                    '}';
        }
    }

    private class MediaServiceStatistics {
        private Map<MediaOperation, MediaRequestStatistics> mediaRequestStatisticsMap_;
        private AtomicInteger totalIOExceptions_;
        private Map<Integer, AtomicInteger> mediaServerErrorCountMap_;

        private MediaServiceStatistics() {
            initMediaRequestStatistics();
            totalIOExceptions_ = new AtomicInteger(0);
            initMediaServerErrorStats();
        }

        private void initMediaRequestStatistics() {
            mediaRequestStatisticsMap_ = new HashMap<MediaOperation, MediaRequestStatistics>();
            for (MediaOperation mediaOperation : MediaOperation.values()) {
                if (!mediaOperation.equals(MediaOperation.ALL)) {
                    mediaRequestStatisticsMap_.put(mediaOperation, new MediaRequestStatistics());
                }
            }
        }

        private void initMediaServerErrorStats() {
            mediaServerErrorCountMap_ = new HashMap<Integer, AtomicInteger>();
            for (MediaErrorCode errorCode : MediaErrorCode.values()) {
                if (!errorCode.equals(MediaErrorCode.NOERROR)) {
                    mediaServerErrorCountMap_.put(errorCode.getCode(), new AtomicInteger(0));
                }
            }
        }

        private void incrementTotalRequests(MediaOperation mediaOperation) {
            MediaRequestStatistics mediaRequestStatistics = mediaRequestStatisticsMap_.get(mediaOperation);
            mediaRequestStatistics.incrementTotalRequests();
        }

        private void updateResponseTime(MediaOperation mediaOperation, long responseTime) {
            MediaRequestStatistics mediaRequestStatistics = mediaRequestStatisticsMap_.get(mediaOperation);
            mediaRequestStatistics.updateResponseTime(responseTime);
        }

        private void incrementIOExceptions() {
            totalIOExceptions_.incrementAndGet();
        }

        private void incrementMediaServerErrorsForCode(int code) {
            mediaServerErrorCountMap_.get(code).incrementAndGet();
        }

        private String getMediaRequestStats(MediaOperation mediaOperation) {
            MediaRequestStatistics mediaRequestStatistics = mediaRequestStatisticsMap_.get(mediaOperation);
            return mediaRequestStatistics.toString();
        }

        private int getTotalMediaServerErrors() {
            int totalErrors = 0;
            for (AtomicInteger errorCount : mediaServerErrorCountMap_.values()) {
                totalErrors += errorCount.get();
            }
            return totalErrors;
        }
    }

    private class MediaRequestStatistics {
        private AtomicInteger totalRequests_;
        private AtomicLong totalResponseTime_;
        private long minResponseTime_;
        private long maxResponseTime_;

        private MediaRequestStatistics() {
            totalRequests_ = new AtomicInteger(0);
            totalResponseTime_ = new AtomicLong(0);
            minResponseTime_ = 0;
            maxResponseTime_ = 0;
        }

        private void incrementTotalRequests() {
            totalRequests_.incrementAndGet();
        }

        private void updateResponseTime(long responseTime) {
            totalResponseTime_.addAndGet(responseTime);
            minResponseTime_ = ((minResponseTime_ == 0) || (responseTime < minResponseTime_)) ? responseTime : minResponseTime_;
            maxResponseTime_ = (responseTime > maxResponseTime_) ? responseTime : maxResponseTime_;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            float averageResponseTime = (totalRequests_.get() > 0) ? (float) totalResponseTime_.get() / (float) totalRequests_.get() : 0;
            return stringBuilder.append("Total Requests = ").append(totalRequests_.get())
                    .append("\nTotal Response Time = ").append(totalResponseTime_.get())
                    .append("\nAverage Response Time = ").append(averageResponseTime)
                    .append("\nMin Response Time = ").append(minResponseTime_)
                    .append("\nMax Response Time = ").append(maxResponseTime_)
                    .toString();
        }
    }

	@Override
	public String getMediaDuration(String fileId) throws MediaServiceException, MediaClientException {
		   String duration = null;
		 try {
			  MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
			  MimeType mimeType =mediaFile.getMimeType();
			  ConvertMediaRequest request = new ConvertAudioRequest(fileId, mimeType);
			  MediaServerConfig mediaServerConfig = getNextMediaServer(request.getMediaOperation(), request.getMediaCategory(), request.getMediaType());
			  String baseUrl = getMediaServicebaseUrl(mediaServerConfig);
			 // LOGGER.error("getMediaDuration :: baseUrl "+baseUrl);
			  baseUrl= baseUrl+"/duration/"+fileId;
			 /// LOGGER.error("getMediaDuration :: baseUrl final :: "+baseUrl);
			  return duration =  executeMethod(baseUrl);
		} catch (Exception e) {
			  LOGGER.error("getMediaDuration :: error while getting media duration "+e.getLocalizedMessage(),e);
		}
		return duration;
	}
}
