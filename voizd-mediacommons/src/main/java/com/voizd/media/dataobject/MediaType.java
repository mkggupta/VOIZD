package com.voizd.media.dataobject;


import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 24/7/12
 * Time: 12:11 PM
 */
public enum MediaType {
    AUDIO("audio", "A"), VIDEO("video", "V"), IMAGE("image", "I"), TEXT("text",
            "T"), ALL("all", "AL");

    private String value;

    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private MediaType(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public static MediaType getMediaTypeFromCode(String code) throws UnsupportedTypeException {
        MediaType requiredMediaType = null;
        for (MediaType mediaType : values()) {
            if (code.equalsIgnoreCase(mediaType.getCode())) {
                requiredMediaType = mediaType;
                break;
            }
        }
        if (requiredMediaType == null) {
            throw new UnsupportedTypeException("No media type can be found for " + code);
        }
        return requiredMediaType;
    }

    public static MediaType getMediaTypeFromValue(String type) throws UnsupportedTypeException {
        MediaType requiredMediaType = null;
        for (MediaType mediaType : values()) {
            if (type.equalsIgnoreCase(mediaType.getValue())) {
                requiredMediaType = mediaType;
                break;
            }
        }
        if (requiredMediaType == null) {
            throw new UnsupportedTypeException("No media type can be found for " + type);
        }
        return requiredMediaType;
    }
	
	 @Override
	    public String toString() {
	        return value ;
	    }
}
