package com.voizd.media.dataobject.request.get;

import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MediaOperation;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 25/7/12
 * Time: 1:13 PM
 */
public abstract class GetMediaRequest extends BaseMediaRequest {
    protected String fileId_;
    protected MimeType mimeType_;

    protected GetMediaRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(MediaOperation.GET, fileId);
        fileId_ = fileId;
        mimeType_ = mimeType;
    }

    public String getFileId() {
        return fileId_;
    }

    public MimeType getMimeType() {
        return mimeType_;
    }

    @Override
    protected String getUrlPath() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaOperation_.getUrlSuffix()).append(MediaConstants.URL_PATH_SEPARATOR)
                .append(fileId_).append(MediaConstants.URL_EXTENSION_SEPARATOR).append(mimeType_.getExt());
        return stringBuilder.toString();
    }
}
