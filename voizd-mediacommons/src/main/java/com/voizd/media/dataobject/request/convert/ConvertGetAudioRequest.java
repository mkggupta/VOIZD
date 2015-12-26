package com.voizd.media.dataobject.request.convert;


import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 23/7/12
 * Time: 3:53 PM
 */
public class ConvertGetAudioRequest extends ConvertGetMediaRequest {
    public ConvertGetAudioRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(fileId, mimeType);
    }

    public ConvertGetAudioRequest(String fileId, MimeType mimeType, Integer maxSize) throws UnsupportedTypeException {
        super(fileId, mimeType, maxSize);
    }

    public ConvertGetAudioRequest(String fileId, MimeType mimeType, Integer bitRate, String codec) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(RequestParam.BITRATE, bitRate);
        addMediaAttributes(RequestParam.CODEC, codec);
    }

    public ConvertGetAudioRequest(String fileId, MimeType mimeType, Integer bitRate, String codec, Integer maxSize) throws UnsupportedTypeException {
        this(fileId, mimeType, maxSize);
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
