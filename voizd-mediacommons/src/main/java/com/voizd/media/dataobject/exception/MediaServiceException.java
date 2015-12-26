package com.voizd.media.dataobject.exception;

import com.voizd.media.dataobject.MediaErrorCode;

/**
 * User: shalabh
 * Date: 27/7/12
 * Time: 4:41 PM
 */
public class MediaServiceException extends Exception {
    private MediaErrorCode errorCode_;

    public MediaServiceException(String message, MediaErrorCode errorCode) {
        super(message);
        errorCode_ = errorCode;
    }

    public MediaErrorCode getErrorCode() {
        return errorCode_;
    }
}
