package com.voizd.search.dataobject;

import java.util.concurrent.atomic.AtomicBoolean;


public class ElasticServerConfig {
    private String host;
    private int port;
    private AtomicBoolean isActive;

	public ElasticServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
        isActive = new AtomicBoolean(true);
    }
	
	public ElasticServerConfig(){
		
	}

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIsActive(AtomicBoolean isActive) {
		this.isActive = isActive;
	}
   
    public AtomicBoolean getActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    @Override
    public String toString() {
        return "MediaServerConfig{" +
                "host_='" + host + '\'' +
                ", port_=" + port  + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElasticServerConfig that = (ElasticServerConfig) o;
        if (port != that.port) return false;
        if (!host.equals(that.host)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + port;
        return result;
    }

}
