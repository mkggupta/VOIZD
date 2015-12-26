/**
 * 
 */
package com.voizd.dao.entities;

import java.util.Date;

/**
 * @author Manish
 * 
 */
public class UserAuth {
	private long id;
	private String userName;
	private String password;
	private int status;
	private Date createdDate;
	private Date modifiedDate;
	private Date lastLoginTime;
	private int registrationMode;
	private int lastLoginMode;
	private String currentClientVersion;
	private String currentPlatform;
	private int loginStatus;
	private float longitude;
	private float latitude; 
    private String lastLocation;
    public String getPushKey() {
		return pushKey;
	}

	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}

	private String pushKey;
    
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

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	/**
	 * @return the loginStatus
	 */
	public int getLoginStatus() {
		return loginStatus;
	}

	/**
	 * @param loginStatus
	 *            the loginStatus to set
	 */
	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * @return the currentClientVersion
	 */
	public String getCurrentClientVersion() {
		return currentClientVersion;
	}

	/**
	 * @param currentClientVersion
	 *            the currentClientVersion to set
	 */
	public void setCurrentClientVersion(String currentClientVersion) {
		this.currentClientVersion = currentClientVersion;
	}

	/**
	 * @return the currentPlatform
	 */
	public String getCurrentPlatform() {
		return currentPlatform;
	}

	/**
	 * @param currentPlatform
	 *            the currentPlatform to set
	 */
	public void setCurrentPlatform(String currentPlatform) {
		this.currentPlatform = currentPlatform;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the lastLoginTime
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the registrationMode
	 */
	public int getRegistrationMode() {
		return registrationMode;
	}

	/**
	 * @param registrationMode
	 *            the registrationMode to set
	 */
	public void setRegistrationMode(int registrationMode) {
		this.registrationMode = registrationMode;
	}

	/**
	 * @return the lastLoginMode
	 */
	public int getLastLoginMode() {
		return lastLoginMode;
	}

	/**
	 * @param lastLoginMode
	 *            the lastLoginMode to set
	 */
	public void setLastLoginMode(int lastLoginMode) {
		this.lastLoginMode = lastLoginMode;
	}

}
