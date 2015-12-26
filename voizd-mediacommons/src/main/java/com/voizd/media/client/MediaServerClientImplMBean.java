package com.voizd.media.client;

/**
 * User: shalabh
 * Date: 31/7/12
 * Time: 1:11 PM
 */
public interface MediaServerClientImplMBean {
    public String[] getAllMediaServers();

    public String getGetRequestsStats();

    public String getPostRequestsStats();

    public String getConvertRequestsStats();

    public String getConvertGetRequestsStats();

    public String getMergeRequestsStats();

    public String getCutRequestsStats();

    public String getCutGetRequestsStats();

    public String getCreateGetThumbnailRequestsStats();

    public String getCreateThumbnailRequestsStats();

    public String getGetThumbnailRequestsStats();

    public int getTotalIOExceptions();

    public int getTotalMediaServerErrors();

    public String[] getInactiveMediaServers();

    public int getIOErrors();

    public int getInternalServerErrors();

    public int getInvalidRequestErrors();

    public int getFFMpegOverloadErrors();

    public int getUnknownMediaServerErrors();
}
