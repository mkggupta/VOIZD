package com.voizd.media.dataobject.request.thumb;

import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 8/8/12
 * Time: 12:34 PM
 */
public class CreateThumbMediaRequest extends BaseMediaRequest {
    public CreateThumbMediaRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(MediaOperation.CREATETHUMB, MediaType.IMAGE, fileId);
        addMediaAttributes(RequestParam.ORIGINAL_FILE_ID, fileId);
        addMediaAttributes(RequestParam.REQUESTED_EXT, mimeType.getExt());
    }

    public CreateThumbMediaRequest(String fileId, MimeType mimeType, Integer width, Integer height) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(width, height);
    }

    public CreateThumbMediaRequest(String fileId, MimeType mimeType, String dimensions) throws UnsupportedTypeException {
        this(fileId, mimeType);
        String[] dimensionFields = dimensions.split(MediaConstants.IMAGE_WIDTH_HEIGHT_SEPARATOR);
        addMediaAttributes(Integer.parseInt(dimensionFields[MediaConstants.IMAGE_WIDTH_INDEX]),
                Integer.parseInt(dimensionFields[MediaConstants.IMAGE_HEIGHT_INDEX]));
    }

    private void addMediaAttributes(Integer width, Integer height) {
        addMediaAttributes(RequestParam.WIDTH, width);
        addMediaAttributes(RequestParam.HEIGHT, height);
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
