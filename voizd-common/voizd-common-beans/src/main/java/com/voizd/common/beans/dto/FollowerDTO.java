package com.voizd.common.beans.dto;


public class FollowerDTO {
	 public Long getFollowerId() {
		return followerId;
	}
	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}
	public String getFollowerName() {
		return followerName;
	}
	public void setFollowerName(String followerName) {
		this.followerName = followerName;
	}
	public String getFollowerImgUrl() {
		return followerImgUrl;
	}
	public void setFollowerImgUrl(String followerImgUrl) {
		this.followerImgUrl = followerImgUrl;
	}
	public String getFollowerProfileUrl() {
		return followerProfileUrl;
	}
	public void setFollowerProfileUrl(String followerProfileUrl) {
		this.followerProfileUrl = followerProfileUrl;
	}
	public String getFollowerNextUrl() {
		return followerNextUrl;
	}
	public void setFollowerNextUrl(String followerNextUrl) {
		this.followerNextUrl = followerNextUrl;
	}
	public String getFollowerPreUrl() {
		return followerPreUrl;
	}
	public void setFollowerPreUrl(String followerPreUrl) {
		this.followerPreUrl = followerPreUrl;
	}
	public String getTapUrl() {
		return tapUrl;
	}
	public void setTapUrl(String tapUrl) {
		this.tapUrl = tapUrl;
	}
	public Byte getTapValue() {
		return tapValue;
	}
	public void setTapValue(Byte tapValue) {
		this.tapValue = tapValue;
	}
	
	 public Long getContCnt() {
			return contCnt;
		}
	public void setContCnt(Long contCnt) {
			this.contCnt = contCnt;
		}
	 public String getLanguage() {
			return language;
		}
	 public void setLanguage(String language) {
			this.language = language;
		}
	 public String getAddress() {
			return address;
		}
	 public void setAddress(String address) {
			this.address = address;
		}
	 public Long getFollowerCnt() {
			return followerCnt;
		}
		public void setFollowerCnt(Long followerCnt) {
			this.followerCnt = followerCnt;
		}
	
	 private Long 	followerId;
	 private String followerName;
	 private String followerImgUrl;
	 private String followerProfileUrl;
	 private String followerNextUrl;
	 private String followerPreUrl;
	 private String tapUrl;
	 private Byte tapValue;
	 private Long contCnt;
	 private String language;
	 private String address;
	 private Long followerCnt;
	
	
	
	
}
