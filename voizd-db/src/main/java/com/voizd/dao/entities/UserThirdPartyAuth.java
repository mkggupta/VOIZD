/**
 * 
 */
package com.voizd.dao.entities;

/**
 * @author Manish
 * 
 */
public class UserThirdPartyAuth {
	private long id;
	private int thirdPartyId;
	private String userKey;
	private String appKey;

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
	 * @return the thirdPartyId
	 */
	public int getThirdPartyId() {
		return thirdPartyId;
	}

	/**
	 * @param thirdPartyId
	 *            the thirdPartyId to set
	 */
	public void setThirdPartyId(int thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
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
	 * @param id
	 * @param thirdPartyId
	 * @param userKey
	 * @param appKey
	 */
	public UserThirdPartyAuth(long id, int thirdPartyId, String userKey, String appKey) {
		super();
		this.id = id;
		this.thirdPartyId = thirdPartyId;
		this.userKey = userKey;
		this.appKey = appKey;
	}

	/**
	 * 
	 */
	public UserThirdPartyAuth() {
		super();
		// TODO Auto-generated constructor stub
	}

}
