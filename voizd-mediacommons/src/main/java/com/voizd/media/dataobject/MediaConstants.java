package com.voizd.media.dataobject;

/**
 * User: shalabh
 * Date: 24/7/12
 * Time: 1:52 PM
 */
public abstract class MediaConstants {
    public static final String URL_PROTOCOL_PREFIX = "http://";
    public static final String HOST_PORT_SEPARATOR = ":";
    public static final String MEDIA_SERVICE_NAME = "/rtMediaServer";

    public static final String QUERY_PARAMETER_SEPARATOR = "?";
    public static final String INTER_PARAMETER_SEPARATOR = "&";
    public static final String INTRA_PARAMETER_SEPARATOR = "=";
    public static final String URL_PATH_SEPARATOR = "/";
    public static final String URL_EXTENSION_SEPARATOR = ".";

    public static final String IMAGE_WIDTH_HEIGHT_SEPARATOR = "x";
    public static final int IMAGE_WIDTH_INDEX = 0;
    public static final int IMAGE_HEIGHT_INDEX = 1;

    public static final long INACTIVE_SERVER_TRACKER_SLEEP_TIME = 300000;

    public static final String ERROR_CODE_RESPONSE_HEADER_NAME = "code";
    public static final String ERROR_MESSAGE_RESPONSE_HEADER_NAME = "message";

    public static final String JSON_RESPONSE_TYPE = "json";

    public static final String PING_URL_SUFFIX = "/ping.htm";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;

    public static final String APPLIICATION_OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";

    public static final int HTTP_CLIENT_MAX_CONNECTIONS_PER_HOST = 5;
    public static final int HTTP_CLIENT_MAX_TOTAL_CONNECTIONS = 50;

    public static final String MBEAN_ROCKETALK_DOMAIN_NAME = "com.rocketalk";
    public static final String MBEAN_MODULE_KEY = "module";
    public static final String MBEAN_SUBMODULE_KEY = "submodule";
    public static final String MBEAN_DOMAIN_KEY = "domain";

    public static final String MBEAN_SERVICE_NAME = "service";
    public static final String MBEAN_MEDIA_VALUE = "media";
    public static final String MEDIA_SERVER_CLIENT_MBEAN_NAME = "MediaServerClient";
    public static final String MEDIA_SERVER_RUNTIME_PERF_MBEAN_NAME = "MediaServerRuntimePerf-";
    public static final String MEDIA_SERVER_CACHE_MBEAN_NAME = "MediaServerCache";
    public static final String MEDIA_SERVER_MANAGER_MBEAN_NAME = "MediaServerManager";
}
