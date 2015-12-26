package com.voizd.media.dataobject.file;


import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaType;
import com.voizd.media.dataobject.MimeType;

/**
 * User: shalabh
 * Date: 6/7/12
 * Time: 12:03 PM
 */
public class MediaFile {
    private Integer appId_;
    private Integer version_;
    private Integer repoId_;
    private MediaCategory mediaCategory_;
    private MediaType mediaType_;
    private MimeType mimeType_;


    public MediaFile(Integer appId, Integer version, Integer repoId, MediaCategory mediaCategory, MediaType mediaType, MimeType mimeType) {
        appId_ = appId;
        version_ = version;
        repoId_ = repoId;
        mediaCategory_ = mediaCategory;
        mediaType_ = mediaType;
        mimeType_ = mimeType;
    }

    public Integer getAppId() {
        return appId_;
    }

    public Integer getVersion() {
        return version_;
    }

    public Integer getRepoId() {
        return repoId_;
    }

    public MediaCategory getMediaCategory() {
        return mediaCategory_;
    }

    public MediaType getMediaType() {
        return mediaType_;
    }


    public MimeType getMimeType() {
        return mimeType_;
    }
}
