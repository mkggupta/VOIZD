package com.voizd.media.dataobject.request.convert;


import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 23/7/12
 * Time: 3:53 PM
 */
public class ConvertGetVideoRequest extends ConvertGetMediaRequest {
    public ConvertGetVideoRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(fileId, mimeType);
    }

    public ConvertGetVideoRequest(String fileId, MimeType mimeType, Integer maxSize) throws UnsupportedTypeException {
        super(fileId, mimeType, maxSize);
    }

    public ConvertGetVideoRequest(String fileId, MimeType mimeType, Integer bitRate, String codec) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(bitRate, codec);
    }

    public ConvertGetVideoRequest(String fileId, MimeType mimeType, Integer bitRate, String codec, Integer maxSize) throws UnsupportedTypeException {
        this(fileId, mimeType, maxSize);
        addMediaAttributes(bitRate, codec);
    }

    private void addMediaAttributes(Integer bitRate, String codec) {
        addMediaAttributes(RequestParam.BITRATE, bitRate);
        addMediaAttributes(RequestParam.CODEC, codec);
    }

    public Integer getBitRate() {
        return (Integer) mediaAttributesMap_.get(RequestParam.BITRATE);
    }

    public String getCodec() {
        return (String) mediaAttributesMap_.get(RequestParam.CODEC);
    }
}
