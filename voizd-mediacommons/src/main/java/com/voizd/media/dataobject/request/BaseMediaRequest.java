package com.voizd.media.dataobject.request;

import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;
import com.voizd.media.utils.file.MediaFileFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shalabh
 * Date: 24/7/12
 * Time: 12:00 PM
 */
public abstract class BaseMediaRequest {
    protected MediaOperation mediaOperation_;
    protected MediaType mediaType_;
    protected MediaCategory mediaCategory_;
    protected Map<RequestParam, Object> mediaAttributesMap_;

    public BaseMediaRequest(MediaOperation mediaOperation, MediaType mediaType, MediaCategory mediaCategory) {
        init(mediaOperation, mediaType, mediaCategory);
    }

    public BaseMediaRequest(MediaOperation mediaOperation, String fileId) throws UnsupportedTypeException {
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        init(mediaOperation, mediaFile.getMediaType(), mediaFile.getMediaCategory());
    }

    public BaseMediaRequest(MediaOperation mediaOperation, MediaType mediaType, String fileId) throws UnsupportedTypeException {
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        init(mediaOperation, mediaType, mediaFile.getMediaCategory());
    }

    public BaseMediaRequest(MediaOperation mediaOperation, MediaCategory category, String fileId) throws UnsupportedTypeException {
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        init(mediaOperation, mediaFile.getMediaType(), category);
    }

    private void init(MediaOperation mediaOperation, MediaType mediaType, MediaCategory mediaCategory) {
        mediaOperation_ = mediaOperation;
        mediaType_ = mediaType;
        mediaCategory_ = mediaCategory;
        mediaAttributesMap_ = new HashMap<RequestParam, Object>();
    }

    public MediaOperation getMediaOperation() {
        return mediaOperation_;
    }

    public MediaType getMediaType() {
        return mediaType_;
    }

    public MediaCategory getMediaCategory() {
        return mediaCategory_;
    }

    public Map<RequestParam, Object> getMediaAttributesMap() {
        return mediaAttributesMap_;
    }

    protected String getQueryString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<RequestParam, Object> entry : mediaAttributesMap_.entrySet()) {
            String paramName = entry.getKey().getName();
            String paramValue = entry.getValue().toString();
            stringBuilder.append(paramName).append(MediaConstants.INTRA_PARAMETER_SEPARATOR)
                    .append(paramValue).append(MediaConstants.INTER_PARAMETER_SEPARATOR);
        }
        String queryString = stringBuilder.toString();
        return (queryString.length() > 0) ? queryString.substring(0, queryString.length() - 1) : queryString;
    }

    protected abstract String getUrlPath();

    public String getCacheKeySuffix() {
        StringBuilder stringBuilder = new StringBuilder();
        List<RequestParam> requestParamValues = mediaOperation_.getRequestParamList();
        for (RequestParam requestParam : requestParamValues) {
            if (requestParam.isCachable()) {
                Object value = mediaAttributesMap_.get(requestParam);
                if (value != null) {
                    stringBuilder.append("_");
                    stringBuilder.append(value);
                }
            }
        }
        return stringBuilder.toString();
    }

    public String getServiceUrl(String baseUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        String urlPath = getUrlPath();
        String queryString = getQueryString();
        stringBuilder.append(baseUrl).append(urlPath);
        if (queryString.length() > 0) {
            stringBuilder.append(MediaConstants.QUERY_PARAMETER_SEPARATOR).append(queryString);
        }
        return stringBuilder.toString();
    }

    protected void addMediaAttributes(RequestParam param, Object paramValue) {
        if (paramValue != null) {
            mediaAttributesMap_.put(param, paramValue);
        }
    }
}
