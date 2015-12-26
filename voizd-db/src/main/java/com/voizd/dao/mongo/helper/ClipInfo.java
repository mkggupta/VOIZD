package com.voizd.dao.mongo.helper;

import java.util.Date;

public class ClipInfo {

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	private Long contentId;
	private Long creatorId;
	private Date date;
	private int status;
	public boolean isAmplify() {
		return isAmplify;
	}

	public void setAmplify(boolean isAmplify) {
		this.isAmplify = isAmplify;
	}
	private boolean isAmplify;
	private Long amplifierId;
	public Long getAmplifierId() {
		return amplifierId;
	}

	public void setAmplifierId(Long amplifierId) {
		this.amplifierId = amplifierId;
	}
}
