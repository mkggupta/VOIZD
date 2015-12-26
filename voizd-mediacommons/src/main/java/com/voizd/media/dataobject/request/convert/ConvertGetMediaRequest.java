package com.voizd.media.dataobject.request.convert;


import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MediaOperation;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 25/7/12
 * Time: 1:13 PM
 */
public abstract class ConvertGetMediaRequest extends BaseMediaRequest {
    private String originalFileId_;
    private MimeType mimeType_;

    protected ConvertGetMediaRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(MediaOperation.CONVERTGET, fileId);
        originalFileId_ = fileId;
        mimeType_ = mimeType;
    }

    protected ConvertGetMediaRequest(String fileId, MimeType mimeType, Integer maxSize) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(RequestParam.MAX_CONVERT_SIZE, maxSize);
    }

    public Integer getMaxSize() {
        return (Integer) mediaAttributesMap_.get(RequestParam.MAX_CONVERT_SIZE);
    }

    @Override
    protected String getUrlPath() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaOperation_.getUrlSuffix()).append(MediaConstants.URL_PATH_SEPARATOR)
                .append(originalFileId_).append(MediaConstants.URL_EXTENSION_SEPARATOR).append(mimeType_.getExt());
        return stringBuilder.toString();
    }
}
