package com.voizd.common.beans.vo;



import java.io.Serializable;
import java.util.Date;

public class TapClipVO implements Serializable {
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
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getFileIds() {
		return fileIds;
	}
	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Byte getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Byte userStatus) {
		this.userStatus = userStatus;
	}
	public boolean isRegistration() {
		return isRegistration;
	}
	public void setRegistration(boolean isRegistration) {
		this.isRegistration = isRegistration;
	}
	
	public Long getFollweeId() {
		return follweeId;
	}
	public void setFollweeId(Long follweeId) {
		this.follweeId = follweeId;
	}
	
	public Long getAmplifierId() {
		return amplifierId;
	}
	public void setAmplifierId(Long amplifierId) {
		this.amplifierId = amplifierId;
	}
	public boolean isAmplify() {
		return isAmplify;
	}
	public void setAmplify(boolean isAmplify) {
		this.isAmplify = isAmplify;
	}
	public String getUserFileId() {
		return userFileId;
	}
	public void setUserFileId(String userFileId) {
		this.userFileId = userFileId;
	}
	public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	private Long contentId;
	private Long stationId;
	private String tag;
	private String category;
	private String title;
	private Long creatorId;
	private Byte status;
	private String fileIds;
	private boolean isPrivate;
	private Long adminId;
	private Date createdDate;
	private String contentType;
	private Date modifiedDate;
	private float latitude;
	private float longitude;
	private String language;
	private String country;
	private String state;
	private String city;
	private String location;
	private String address;
	private String firstName;
	private String lastName;
	private String username;
	private Byte userStatus;
	private String userFileId;
	private Long follweeId;
	private Long amplifierId;
	private boolean isRegistration;
	private boolean isAmplify;
	private String weblink;
	
}
