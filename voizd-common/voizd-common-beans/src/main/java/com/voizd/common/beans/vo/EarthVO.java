package com.voizd.common.beans.vo;

import java.util.HashMap;

public class EarthVO {
	private HashMap<Integer, Object> gridInfo;
	public HashMap<Integer, Object> getGridInfo() {
		return gridInfo;
	}
	public void setGridInfo(HashMap<Integer, Object> gridInfo) {
		this.gridInfo = gridInfo;
	}
	private HashMap<Integer, Object> tags;
	private String tagUrl;
	private String erathUrl;
	private String audioUrl;
	private String grid;
	
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	public String getTagUrl() {
		return tagUrl;
	}
	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}
	public String getErathUrl() {
		return erathUrl;
	}
	public void setErathUrl(String erathUrl) {
		this.erathUrl = erathUrl;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public HashMap<Integer, Object> getTags() {
		return tags;
	}
	public void setTags(HashMap<Integer, Object> tags) {
		this.tags = tags;
	}

	
}
