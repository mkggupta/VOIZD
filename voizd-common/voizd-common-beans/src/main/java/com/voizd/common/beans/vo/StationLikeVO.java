package com.voizd.common.beans.vo;

import java.util.Date;

public class StationLikeVO {

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	private Long stationId;
	private Long userId;
	private Byte likeValue;
    private Date createdDate;
	private Date modifiedDate;
	public Byte getLikeValue() {
		return likeValue;
	}

	public void setLikeValue(Byte likeValue) {
		this.likeValue = likeValue;
	}
}
