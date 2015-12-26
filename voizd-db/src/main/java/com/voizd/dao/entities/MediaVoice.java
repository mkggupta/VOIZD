package com.voizd.dao.entities;

import java.util.Date;

public class MediaVoice {
	 public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getMediaType() {
		return MediaType;
	}
	public void setMediaType(String mediaType) {
		MediaType = mediaType;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public int getGlobe() {
		return globe;
	}
	public void setGlobe(int globe) {
		this.globe = globe;
	}
	private String fileId;
	 private String MediaType;
	 private String ext;
	 private long size;
	 private String duration; 
	 private int status;
	 private Date createddate;
	 private int globe;

}
