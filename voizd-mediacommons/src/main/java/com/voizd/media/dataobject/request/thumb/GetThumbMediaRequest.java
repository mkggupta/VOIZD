package com.voizd.media.dataobject.request.thumb;

import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 8/8/12
 * Time: 12:34 PM
 */
public class GetThumbMediaRequest extends BaseMediaRequest {
    protected String fileId_;
    protected MimeType mimeType_;

    public GetThumbMediaRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(MediaOperation.GETTHUMBNAIL, MediaType.IMAGE, fileId);
        fileId_ = fileId;
        mimeType_ = mimeType;
    }

    public GetThumbMediaRequest(String fileId, MimeType mimeType, Integer width, Integer height) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(width, height);
    }

    public GetThumbMediaRequest(String fileId, MimeType mimeType, String dimensions) throws UnsupportedTypeException {
        this(fileId, mimeType);
        String[] dimensionFields = dimensions.split(MediaConstants.IMAGE_WIDTH_HEIGHT_SEPARATOR);
        addMediaAttributes(Integer.parseInt(dimensionFields[MediaConstants.IMAGE_WIDTH_INDEX]),
                Integer.parseInt(dimensionFields[MediaConstants.IMAGE_HEIGHT_INDEX]));
    }

    private void addMediaAttributes(Integer width, Integer height) {
        addMediaAttributes(RequestParam.WIDTH, width);
        addMediaAttributes(RequestParam.HEIGHT, height);
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
