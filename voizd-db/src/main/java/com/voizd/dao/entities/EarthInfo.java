package com.voizd.dao.entities;

import java.util.Date;
import java.util.List;

import com.voizd.common.beans.dto.ContentDTO;

public class EarthInfo {
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	public String getFileIds() {
		return fileIds;
	}
	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}
	public List<String> getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(List<String> audioUrl) {
		this.audioUrl = audioUrl;
	}
	public Long getContCnt() {
		return contCnt;
	}
	public void setContCnt(Long contCnt) {
		this.contCnt = contCnt;
	}
	public List<TagMediaInfo> getTagMediaInfo() {
		return tagMediaInfo;
	}
	public void setTagMediaInfo(List<TagMediaInfo> tagMediaInfo) {
		this.tagMediaInfo = tagMediaInfo;
	}

	private Long tagId;
	private String tagName;
	private String city;
	private String state;
	private String country;
	private String language;
	private Long counter; 
	private Date createdDate;
	private Date modifiedDate;
	private String fileIds;
	private List<String> audioUrl;
	private List<TagMediaInfo> tagMediaInfo;
	private Long contCnt;
	
	
}
