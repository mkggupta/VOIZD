package com.voizd.media.dataobject;

import com.voizd.media.dataobject.exception.UnsupportedTypeException;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: shalabh
 * Date: 26/7/12
 * Time: 11:54 AM
 */
public enum MediaOperation {
    POST(1, "post", "/post") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.OWNERKEY);
            return params;
        }
    },
    MERGE(2, "merge", "/merge") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.FILE_ID1);
            params.add(RequestParam.FILE_ID2);
            params.add(RequestParam.OWNERKEY);
            return params;
        }
    },
    GET(3, "get", "/get") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },   
    GETTHUMBNAIL(4, "getthumb", "/getthumb") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },
    CONVERT(5, "convert", "/convert") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            return params;
        }
    },
    CONVERTGET(6, "convertget", "/convertget") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.MAX_CONVERT_SIZE);
            return params;
        }
    },
    CUT(7, "cut", "/cut") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },
    CUTGET(8, "cutget", "/cutget") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },
    CREATETHUMB(9, "createthumb", "/createthumb") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },
    CREATETHUMBGET(10, "createthumbget", "/createthumbget") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.ORIGINAL_FILE_ID);
            params.add(RequestParam.REQUESTED_EXT);
            params.add(RequestParam.WIDTH);
            params.add(RequestParam.HEIGHT);
            params.add(RequestParam.BITRATE);
            params.add(RequestParam.CODEC);
            params.add(RequestParam.START_SEC);
            params.add(RequestParam.DURATION_SEC);
            return params;
        }
    },
    ALL(11, "", ""),
    CHECK(12, "check", "/check") {
        @Override
        public List<RequestParam> getRequestParamList() {
            List<RequestParam> params = new ArrayList<RequestParam>();
            params.add(RequestParam.MEDIACATEGORY);
            params.add(RequestParam.MEDIATYPE);
            params.add(RequestParam.OWNERKEY);
            return params;
        }
    };

    private int id_;
    private String value_;
    private String urlSuffix_;

    private MediaOperation(int id, String value, String urlSuffix) {
        id_ = id;
        value_ = value;
        urlSuffix_ = urlSuffix;
    }

    public int getId() {
        return id_;
    }

    public String getValue() {
        return value_;
    }

    public String getUrlSuffix() {
        return urlSuffix_;
    }

    public List<RequestParam> getRequestParamList() {
        List<RequestParam> params = new ArrayList<RequestParam>();
        return params;
    }

    public static MediaOperation getMediaOperation(String value) throws UnsupportedTypeException {
        MediaOperation requiredMediaOperation = null;
        if (StringUtils.hasText(value)) {
            for (MediaOperation mediaOperation : values()) {
                if (value.equals(mediaOperation.value_)) {
                    requiredMediaOperation = mediaOperation;
                    break;
                }
            }
        }
        if (requiredMediaOperation == null) {
            throw new UnsupportedTypeException("No media operation can be found for " + value);
        }
        return requiredMediaOperation;
    }

    public static MediaOperation getMediaOperation(int id) throws UnsupportedTypeException {
        MediaOperation requiredMediaOperation = null;
        for (MediaOperation mediaOperation : values()) {
            if (mediaOperation.id_ == id) {
                requiredMediaOperation = mediaOperation;
                break;
            }
        }
        if (requiredMediaOperation == null) {
            throw new UnsupportedTypeException("No media operation can be found for " + id);
        }
        return requiredMediaOperation;
    }
}
