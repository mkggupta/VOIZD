/**
 * 
 */
package com.voizd.dao.entities;

import java.util.Date;

/**
 * @author Manish
 * 
 */
public class ForgetPasswordVerification {

	private long id;
	private long userId;
	private String verificationCode;
	private Date createdDate;
	private Date expiryDate;
	private int status;

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
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the verificationCode
	 */
	public String getVerificationCode() {
		return verificationCode;
	}

	/**
	 * @param verificationCode
	 *            the verificationCode to set
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param userId
	 * @param verificationCode
	 * @param createdDate
	 * @param expiryDate
	 */
	public ForgetPasswordVerification(long userId, String verificationCode, Date createdDate, Date expiryDate) {
		super();
		this.userId = userId;
		this.verificationCode = verificationCode;
		this.createdDate = createdDate;
		this.expiryDate = expiryDate;
	}

	/**
	 * 
	 */
	public ForgetPasswordVerification() {
		super();
	}

}
