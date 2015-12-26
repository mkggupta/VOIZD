/**
 * 
 */
package com.voizd.common.beans.vo;

import java.io.Serializable;
import java.util.Date;


public class AmplifyInfoVO implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getAmplifyId() {
		return amplifyId;
	}
	public void setAmplifyId(Long amplifyId) {
		this.amplifyId = amplifyId;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	 public Long getAmplifierId() {
			return amplifierId;
	}
	public void setAmplifierId(Long amplifierId) {
			this.amplifierId = amplifierId;
	}
	public Date getAmplifyDate() {
		return amplifyDate;
	}
	public void setAmplifyDate(Date amplifyDate) {
		this.amplifyDate = amplifyDate;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	 private Long contentId;
	 private Long amplifyId;
	 private Long creatorId;
	 private Byte status;
	 private Long amplifierId;
	 private Long userId;
	 private Date amplifyDate;
	 private Date createdDate;
	
	
	

}
