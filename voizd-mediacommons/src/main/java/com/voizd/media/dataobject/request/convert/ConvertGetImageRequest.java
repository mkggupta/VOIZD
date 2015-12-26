package com.voizd.media.dataobject.request.convert;


import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 23/7/12
 * Time: 3:36 PM
 */
public class ConvertGetImageRequest extends ConvertGetMediaRequest {
    public ConvertGetImageRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(fileId, mimeType);
    }

    public ConvertGetImageRequest(String fileId, MimeType mimeType, Integer maxSize) throws UnsupportedTypeException {
        super(fileId, mimeType, maxSize);
    }

    public ConvertGetImageRequest(String fileId, MimeType mimeType, String dimensions) throws UnsupportedTypeException {
        this(fileId, mimeType);
        String[] dimensionFields = dimensions.split(MediaConstants.IMAGE_WIDTH_HEIGHT_SEPARATOR);
        addMediaAttributes(Integer.parseInt(dimensionFields[MediaConstants.IMAGE_WIDTH_INDEX]),
                Integer.parseInt(dimensionFields[MediaConstants.IMAGE_HEIGHT_INDEX]));
    }

    public ConvertGetImageRequest(String fileId, MimeType mimeType, String dimensions, Integer maxSize) throws UnsupportedTypeException {
        this(fileId, mimeType, maxSize);
        String[] dimensionFields = dimensions.split(MediaConstants.IMAGE_WIDTH_HEIGHT_SEPARATOR);
        addMediaAttributes(Integer.parseInt(dimensionFields[MediaConstants.IMAGE_WIDTH_INDEX]),
                Integer.parseInt(dimensionFields[MediaConstants.IMAGE_HEIGHT_INDEX]));
    }

    public ConvertGetImageRequest(String fileId, MimeType mimeType, Integer width, Integer height) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(width, height);
    }

    public ConvertGetImageRequest(String fileId, MimeType mimeType, Integer width, Integer height, Integer maxSize) throws UnsupportedTypeException {
        this(fileId, mimeType, maxSize);
        addMediaAttributes(width, height);
    }

    private void addMediaAttributes(Integer width, Integer height) {
        addMediaAttributes(RequestParam.WIDTH, width);
        addMediaAttributes(RequestParam.HEIGHT, height);
    }
}
