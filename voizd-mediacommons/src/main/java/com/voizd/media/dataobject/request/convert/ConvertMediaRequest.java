package com.voizd.media.dataobject.request.convert;

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
public abstract class ConvertMediaRequest extends BaseMediaRequest {
    protected ConvertMediaRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(MediaOperation.CONVERT, fileId);
        addMediaAttributes(RequestParam.ORIGINAL_FILE_ID, fileId);
        addMediaAttributes(RequestParam.REQUESTED_EXT, mimeType.getExt());
    }

    public String getOriginalFileId() {
        return (String) mediaAttributesMap_.get(RequestParam.ORIGINAL_FILE_ID);
    }

    public MimeType getMimeType() throws UnsupportedTypeException {
        String extension = (String) mediaAttributesMap_.get(RequestParam.REQUESTED_EXT);
        return MimeType.getMimeTypeForExtension(extension);
    }

    @Override
    protected String getUrlPath() {
        return mediaOperation_.getUrlSuffix();
    }
}
