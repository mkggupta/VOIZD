/**
 * 
 */
package com.voizd.common.beans.vo;

import java.util.Date;

/**
 * @author Manish
 * 
 */
public class UserVO {
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
	private String primaryEmailAddress;
	private float longitude;
	private float latitude; 
	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}

	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfileUpdateUrl() {
		return profileUpdateUrl;
	}

	public void setProfileUpdateUrl(String profileUpdateUrl) {
		this.profileUpdateUrl = profileUpdateUrl;
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

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the profilePicFileExt
	 */
	public String getProfilePicFileExt() {
		return profilePicFileExt;
	}

	/**
	 * @param profilePicFileExt
	 *            the profilePicFileExt to set
	 */
	public void setProfilePicFileExt(String profilePicFileExt) {
		this.profilePicFileExt = profilePicFileExt;
	}

	/**
	 * @return the profilePicUrl
	 */
	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	/**
	 * @param profilePicUrl
	 *            the profilePicUrl to set
	 */
	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the profilePicFileId
	 */
	public String getProfilePicFileId() {
		return profilePicFileId;
	}

	/**
	 * @param profilePicFileId
	 *            the profilePicFileId to set
	 */
	public void setProfilePicFileId(String profilePicFileId) {
		this.profilePicFileId = profilePicFileId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the salutation
	 */
	public String getSalutation() {
		return salutation;
	}

	/**
	 * @param salutation
	 *            the salutation to set
	 */
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * @param contactNumber
	 *            the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * @return the contactAddressLine1
	 */
	public String getContactAddressLine1() {
		return contactAddressLine1;
	}

	/**
	 * @param contactAddressLine1
	 *            the contactAddressLine1 to set
	 */
	public void setContactAddressLine1(String contactAddressLine1) {
		this.contactAddressLine1 = contactAddressLine1;
	}

	/**
	 * @return the contactAddressLine2
	 */
	public String getContactAddressLine2() {
		return contactAddressLine2;
	}

	/**
	 * @param contactAddressLine2
	 *            the contactAddressLine2 to set
	 */
	public void setContactAddressLine2(String contactAddressLine2) {
		this.contactAddressLine2 = contactAddressLine2;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode
	 *            the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the secondaryEmailAddress
	 */
	public String getSecondaryEmailAddress() {
		return secondaryEmailAddress;
	}

	/**
	 * @param secondaryEmailAddress
	 *            the secondaryEmailAddress to set
	 */
	public void setSecondaryEmailAddress(String secondaryEmailAddress) {
		this.secondaryEmailAddress = secondaryEmailAddress;
	}
}
