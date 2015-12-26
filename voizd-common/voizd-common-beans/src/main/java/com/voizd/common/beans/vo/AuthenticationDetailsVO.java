/**
 * 
 */
package com.voizd.common.beans.vo;

import java.util.Date;

/**
 * @author Manish
 * 
 */
public class AuthenticationDetailsVO {
	private long id;
	private String userName;
	private int status;
	private Date createdDate;
	private Date modifiedDate;
	private Date lastLoginTime;
	private int registerationMode;
	private int lastLoginMode;
	private String firstName;
	private String lastName;
	private String profilePicUrl;

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
	 * @return the registerationMode
	 */
	public int getRegisterationMode() {
		return registerationMode;
	}

	/**
	 * @param registerationMode
	 *            the registerationMode to set
	 */
	public void setRegisterationMode(int registerationMode) {
		this.registerationMode = registerationMode;
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
