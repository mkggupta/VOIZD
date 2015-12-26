package com.voizd.media.dataobject.request.cut;

import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MediaOperation;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 7/8/12
 * Time: 6:09 PM
 */
public class CutGetMediaRequest extends BaseMediaRequest {
    protected String originalFileId_;
    protected MimeType mimeType_;

    public CutGetMediaRequest(String fileId, MimeType mimeType, Integer bitRate, String codec, Integer startTime, Integer duration) throws UnsupportedTypeException {
        this(fileId, mimeType, startTime, duration);
        addMediaAttributes(RequestParam.BITRATE, bitRate);
        addMediaAttributes(RequestParam.CODEC, codec);
    }

    public CutGetMediaRequest(String fileId, MimeType mimeType, Integer startTime, Integer duration) throws UnsupportedTypeException {
        super(MediaOperation.CUTGET, fileId);
        originalFileId_ = fileId;
        mimeType_ = mimeType;
        addMediaAttributes(RequestParam.START_SEC, startTime);
        addMediaAttributes(RequestParam.DURATION_SEC, duration);
    }

    public String getOriginalFileId() {
        return originalFileId_;
    }

    public MimeType getMimeType() {
        return mimeType_;
    }

    public Integer getStartTime() {
        return (Integer) mediaAttributesMap_.get(RequestParam.START_SEC);
    }

    public Integer getDuration() {
        return (Integer) mediaAttributesMap_.get(RequestParam.DURATION_SEC);
    }

    public Integer getBitRate() {
        return (Integer) mediaAttributesMap_.get(RequestParam.CODEC);
    }

    public String getCodec() {
        return (String) mediaAttributesMap_.get(RequestParam.CODEC);
    }

    @Override
    protected String getUrlPath() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaOperation_.getUrlSuffix()).append(MediaConstants.URL_PATH_SEPARATOR)
                .append(originalFileId_).append(MediaConstants.URL_EXTENSION_SEPARATOR).append(mimeType_.getExt());
        return stringBuilder.toString();
    }
}
