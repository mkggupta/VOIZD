package com.voizd.common.beans.vo;

public class PushMessageVO {
  public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
private Long id;
  private Long userId;
  private String notifType;
  private String deviceKey;
  private String message;
  private int status;
  
}
