/**
 * 
 */
package com.voizd.common.beans.vo;

/**
 * @author Manish
 * 
 */
public class RegistrationVO extends UserVO {
	private String password;
	private int registrationMode;
	private String userKey;
	private String appKey;
	private String currentClientVersion;
	private String currentPlatform;
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
	 * @return the userKey
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * @param userKey
	 *            the userKey to set
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * @return the appKey
	 */
	public String getAppKey() {
		return appKey;
	}

	/**
	 * @param appKey
	 *            the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
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

}
