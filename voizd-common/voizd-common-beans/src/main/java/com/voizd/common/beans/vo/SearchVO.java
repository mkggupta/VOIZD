package com.voizd.common.beans.vo;

public class SearchVO {
	
public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}

public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String getLanguage() {
	return language;
}
public void setLanguage(String language) {
	this.language = language;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getSearchType() {
	return searchType;
}
public void setSearchType(String searchType) {
	this.searchType = searchType;
}
public Long getUserId() {
	return userId;
}
public void setUserId(Long userId) {
	this.userId = userId;
}

private String tag;
private String location;
private String language;
private String userName;
private String searchType;
private Long userId;
private int startLimit;
private int type;
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public int getStartLimit() {
	return startLimit;
}
public void setStartLimit(int startLimit) {
	this.startLimit = startLimit;
}

public int getDirection() {
	return direction;
}
public void setDirection(int direction) {
	this.direction = direction;
}

private int pageLimit;
public int getPageLimit() {
	return pageLimit;
}
public void setPageLimit(int pageLimit) {
	this.pageLimit = pageLimit;
}

private int direction;

}
