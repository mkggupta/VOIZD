package com.voizd.common.beans.dto;

import java.util.Date;
import java.util.List;

public class UserDTO {
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getContactAddressLine1() {
		return contactAddressLine1;
	}
	public void setContactAddressLine1(String contactAddressLine1) {
		this.contactAddressLine1 = contactAddressLine1;
	}
	public String getContactAddressLine2() {
		return contactAddressLine2;
	}
	public void setContactAddressLine2(String contactAddressLine2) {
		this.contactAddressLine2 = contactAddressLine2;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSecondaryEmailAddress() {
		return secondaryEmailAddress;
	}
	public void setSecondaryEmailAddress(String secondaryEmailAddress) {
		this.secondaryEmailAddress = secondaryEmailAddress;
	}
	public String getProfilePicFileId() {
		return profilePicFileId;
	}
	public void setProfilePicFileId(String profilePicFileId) {
		this.profilePicFileId = profilePicFileId;
	}
	public String getProfilePicFileExt() {
		return profilePicFileExt;
	}
	public void setProfilePicFileExt(String profilePicFileExt) {
		this.profilePicFileExt = profilePicFileExt;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getProfilePicUrl() {
		return profilePicUrl;
	}
	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getProfileUpdateUrl() {
		return profileUpdateUrl;
	}
	public void setProfileUpdateUrl(String profileUpdateUrl) {
		this.profileUpdateUrl = profileUpdateUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getCUrl() {
		return cUrl;
	}
	public void setCUrl(String url) {
		cUrl = url;
	}
	public Long getBookMarkCnt() {
		return bookMarkCnt;
	}
	public void setBookMarkCnt(Long bookMarkCnt) {
		this.bookMarkCnt = bookMarkCnt;
	}
	public String getBookMarkUrl() {
		return bookMarkUrl;
	}
	public void setBookMarkUrl(String bookMarkUrl) {
		this.bookMarkUrl = bookMarkUrl;
	}
	public Long getAmplifiedCnt() {
		return amplifiedCnt;
	}
	public void setAmplifiedCnt(Long amplifiedCnt) {
		this.amplifiedCnt = amplifiedCnt;
	}
	public String getAmplifiedUrl() {
		return amplifiedUrl;
	}
	public void setAmplifiedUrl(String amplifiedUrl) {
		this.amplifiedUrl = amplifiedUrl;
	}
	public Long getIFollowCnt() {
		return iFollowCnt;
	}
	public void setIFollowCnt(Long followCnt) {
		iFollowCnt = followCnt;
	}
	public String getIFollowUrl() {
		return iFollowUrl;
	}
	public void setIFollowUrl(String followUrl) {
		iFollowUrl = followUrl;
	}
	public Long getFollowerCnt() {
		return followerCnt;
	}
	public void setFollowerCnt(Long followerCnt) {
		this.followerCnt = followerCnt;
	}
	public String getFollowerUrl() {
		return followerUrl;
	}
	public void setFollowerUrl(String followerUrl) {
		this.followerUrl = followerUrl;
	}
	
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}
	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}
	public List<VoizerDTO> getTopVoizer() {
		return topVoizer;
	}
	public void setTopVoizer(List<VoizerDTO> topVoizer) {
		this.topVoizer = topVoizer;
	}
	private long id;
	private String firstName;
	private String userName;
	private String lastName;
	private String salutation;
	private Date dob;
	private int gender;
	private String contactNumber;
	private String contactAddressLine1;
	private String contactAddressLine2;
	private String zipcode;
	private String city;
	private String state;
	private String country;
	private String secondaryEmailAddress;
	private String primaryEmailAddress;
	private String profilePicFileId;
	private String profilePicFileExt;
	private String timeZone;
	private String profilePicUrl;
	private int status;
	private String language;
	private String location;
	private String userDescription;
	private String webSite;
	private String profileUpdateUrl;
	private String address;
	private String tapUrl;
	private Byte tapValue;
	private Long followerCnt;
	private String followerUrl;
	private Long contCnt;
	private String cUrl;
	private Long bookMarkCnt;
	private String bookMarkUrl;
	private Long amplifiedCnt;
	private String amplifiedUrl;
	private Long iFollowCnt;
	private String iFollowUrl;
	private List<VoizerDTO> topVoizer;
	
}
