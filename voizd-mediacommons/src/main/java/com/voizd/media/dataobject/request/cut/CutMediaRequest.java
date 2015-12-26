package com.voizd.media.dataobject.request.cut;

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
public class CutMediaRequest extends BaseMediaRequest {
    public CutMediaRequest(String fileId, MimeType mimeType, Integer bitRate, String codec, Integer startTime, Integer duration) throws UnsupportedTypeException {
        this(fileId, mimeType, startTime, duration);
        addMediaAttributes(RequestParam.BITRATE, bitRate);
        addMediaAttributes(RequestParam.CODEC, codec);
    }

    public CutMediaRequest(String fileId, MimeType mimeType, Integer startTime, Integer duration) throws UnsupportedTypeException {
        super(MediaOperation.CUT, fileId);
        addMediaAttributes(RequestParam.ORIGINAL_FILE_ID, fileId);
        addMediaAttributes(RequestParam.REQUESTED_EXT, mimeType.getExt());
        addMediaAttributes(RequestParam.START_SEC, startTime);
        addMediaAttributes(RequestParam.DURATION_SEC, duration);
    }

    public String getOriginalFileId() {
        return (String) mediaAttributesMap_.get(RequestParam.ORIGINAL_FILE_ID);
    }

    public MimeType getMimeType() throws UnsupportedTypeException {
        String extension = (String) mediaAttributesMap_.get(RequestParam.REQUESTED_EXT);
        return MimeType.getMimeTypeForExtension(extension);
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
        return mediaOperation_.getUrlSuffix();
    }
}
