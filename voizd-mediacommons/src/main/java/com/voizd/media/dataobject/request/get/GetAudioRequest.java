package com.voizd.media.dataobject.request.get;


import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 23/7/12
 * Time: 3:53 PM
 */
public class GetAudioRequest extends GetMediaRequest {
    public GetAudioRequest(String fileId, MimeType mimeType, Integer bitRate, String codec) throws UnsupportedTypeException {
        this(fileId, mimeType);
        addMediaAttributes(RequestParam.BITRATE, bitRate);
        addMediaAttributes(RequestParam.CODEC, codec);
    }

    public GetAudioRequest(String fileId, MimeType mimeType) throws UnsupportedTypeException {
        super(fileId, mimeType);
    }

    public Integer getBitRate() {
        return (Integer) mediaAttributesMap_.get(RequestParam.BITRATE);
    }

    public String getCodec() {
        return (String) mediaAttributesMap_.get(RequestParam.CODEC);
    }
}
