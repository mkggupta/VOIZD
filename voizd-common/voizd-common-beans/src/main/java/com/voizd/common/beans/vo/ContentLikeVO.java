package com.voizd.common.beans.vo;

import java.util.Date;

public class ContentLikeVO {
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
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
	public Byte getLikeValue() {
		return likeValue;
	}
	public void setLikeValue(Byte likeValue) {
		this.likeValue = likeValue;
	}
	private Long contentId;
	private Long userId;
	private Byte likeValue;
    private Date createdDate;
	private Date modifiedDate;
	
}
