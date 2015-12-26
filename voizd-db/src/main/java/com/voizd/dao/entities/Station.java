package com.voizd.dao.entities;

import java.util.List;

public class Station {
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public List<String> getFileIdList() {
		return fileIdList;
	}
	public void setFileIdList(List<String> fileIdList) {
		this.fileIdList = fileIdList;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	private Long stationId;
	private String tag;
	private String description;
	private String location;
	private String language;
	private String fileId;
	private List<String> fileIdList;
	
}
