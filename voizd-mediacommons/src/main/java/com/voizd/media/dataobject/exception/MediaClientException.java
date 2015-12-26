package com.voizd.media.dataobject.exception;

/**
 * User: shalabh
 * Date: 7/9/12
 * Time: 3:35 PM
 */
public class MediaClientException extends Exception {
    public MediaClientException(String message) {
        super(message);
    }

    public MediaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
