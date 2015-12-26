package com.voizd.common.beans.vo;

import java.util.Date;

public class StreamLikeVO {

	
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

	private Long streamId;
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

	public Long getStreamId() {
		return streamId;
	}

	public void setStreamId(Long streamId) {
		this.streamId = streamId;
	}
}
