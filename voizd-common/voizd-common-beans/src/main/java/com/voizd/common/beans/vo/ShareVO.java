package com.voizd.common.beans.vo;

public class ShareVO {

	private String shareurl;
	public String getShareurl() {
		return shareurl;
	}
	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}
	public String getThumburl() {
		return thumburl;
	}
	public void setThumburl(String thumburl) {
		this.thumburl = thumburl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Byte getAppId() {
		return appId;
	}
	public void setAppId(Byte appId) {
		this.appId = appId;
	}
	private String thumburl;
	private String message;
	private Byte appId;
}
