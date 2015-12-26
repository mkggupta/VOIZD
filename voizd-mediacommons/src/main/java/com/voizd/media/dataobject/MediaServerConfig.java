package com.voizd.media.dataobject;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: shalabh
 * Date: 26/6/12
 * Time: 3:58 PM
 */
public class MediaServerConfig {
    private String host_;
    private int port_;
    private MediaOperation[] operations_;
    private MediaCategory[] categories_;
    private MediaType[] mediaTypes_;
    private AtomicBoolean isActive_;

    public MediaServerConfig(String host, int port, MediaOperation[] operations, MediaCategory[] categories, MediaType[] mediaTypes) {
        host_ = host;
        port_ = port;
        operations_ = operations;
        categories_ = categories;
        mediaTypes_ = mediaTypes;
        isActive_ = new AtomicBoolean(true);
    }

    public String getHost() {
        return host_;
    }

    public int getPort() {
        return port_;
    }

    public MediaOperation[] getOperations() {
        return operations_;
    }

    public MediaCategory[] getCategories() {
        return categories_;
    }

    public MediaType[] getMediaTypes() {
        return mediaTypes_;
    }

    public AtomicBoolean getActive() {
        return isActive_;
    }

    public void setActive(boolean isActive) {
        isActive_.set(isActive);
    }

    @Override
    public String toString() {
        return "MediaServerConfig{" +
                "host_='" + host_ + '\'' +
                ", port_=" + port_ +
                ", operation_='" + Arrays.toString(operations_) + '\'' +
                ", category_='" + Arrays.toString(categories_) + '\'' +
                ", mediaType_='" + Arrays.toString(mediaTypes_) + '\'' +
                ", isActive_='" + isActive_ + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaServerConfig that = (MediaServerConfig) o;
        if (port_ != that.port_) return false;
        if (!host_.equals(that.host_)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = host_.hashCode();
        result = 31 * result + port_;
        return result;
    }
}
