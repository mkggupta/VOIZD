package com.voizd.common.beans.vo;

public class StatsVO {
	 private Long creatorId;
	 private Long cntId;
	 private Long stationId;
	 private Long userId;
	 private String op;
	 private Long duration;
	 private String appId;
	 private Long stId;
	 private Long cmtId;
	
	public Long getCmtId() {
		return cmtId;
	}
	public void setCmtId(Long cmtId) {
		this.cmtId = cmtId;
	}
	public Long getCntId() {
		return cntId;
	}
	public void setCntId(Long cntId) {
		this.cntId = cntId;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Long getStId() {
		return stId;
	}
	public void setStId(Long stId) {
		this.stId = stId;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	
	
}
