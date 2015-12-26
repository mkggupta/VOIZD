package com.voizd.media.dataobject.request.get;


import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 23/7/12
 * Time: 3:36 PM
 */
public class GetImageRequest extends GetMediaRequest {
    public GetImageRequest(String fileId, MimeType mimeType, String dimensions) throws UnsupportedTypeException {
        this(fileId, mimeType);
        String[] dimensionFields = dimensions.split(MediaConstants.IMAGE_WIDTH_HEIGHT_SEPARATOR);
        addMediaAttributes(Integer.parseInt(dimensionFields[MediaConstants.IMAGE_WIDTH_INDEX]),
                Integer.parseInt(dimensionFields[MediaConstants.IMAGE_HEIGHT_INDEX]));
    }

    public GetImageRequest(String fileId, MimeType mimeType, Integer width, Integer height) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(width, height);
    }

    public GetImageRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(fileId, mimeType);
    }


    private void addMediaAttributes(Integer width, Integer height) {
        addMediaAttributes(RequestParam.WIDTH, width);
        addMediaAttributes(RequestParam.HEIGHT, height);
    }

    public Integer getWidth() {
        return (Integer) mediaAttributesMap_.get(RequestParam.WIDTH);
    }

    public Integer getHeight() {
        return (Integer) mediaAttributesMap_.get(RequestParam.HEIGHT);
    }
}
