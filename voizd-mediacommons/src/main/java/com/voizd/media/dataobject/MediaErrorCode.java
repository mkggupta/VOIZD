package com.voizd.media.dataobject;

public enum MediaErrorCode {

    NOERROR(0, "NO ERROR"),
    ERROR_IO(1, "IO EXCEPTION"),
    ERROR_INTERNAL_SERVER_ERROR(2, "INTERVAL SERVER ERROR"),
    ERROR_INVALID_REQUEST_DATA(3, "INVALID REQUEST DATA"),
    ERROR_MAX_TRANSCODER_INSTANCE_LIMIT(4, "MEDIA TRANSCODER MAX INSTANCES ALREADY RUNNING"),
    ERROR_MAX_CONVERSION_SIZE_EXCEEDED(5, "SIZE OF CONVERTED MEDIA EXCEEDED REQUESTED SIZE"),
    UNKNOWN_ERROR(6, "UNKNOWN ERROR");

    private final int code;
    private final String message;

    private MediaErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static MediaErrorCode getMediaErrorCode(int code) {
        for (MediaErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }

}
