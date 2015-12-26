package com.voizd.dao.entities;

import java.util.Date;

public class UserPushInfo {
	

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getNotifType() {
		return notifType;
	}
	public void setNotifType(String notifType) {
		this.notifType = notifType;
	}
	public String getDeviceKey() {
		return deviceKey;
	}
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}
	public String getSendNotif() {
		return sendNotif;
	}
	public void setSendNotif(String sendNotif) {
		this.sendNotif = sendNotif;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	private Long userId;
	private String notifType;
	private String deviceKey;
	private String sendNotif;
	private Date modifiedDate;
	private Date createdDate;

}
