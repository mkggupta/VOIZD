/**
 * 
 */
package com.voizd.common.enumeration;

/**
 * @author Manish
 * 
 */
public enum LoginStatusEnum {
	OFFLINE(0), ONLINE(1);
	int status;

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

	LoginStatusEnum(int status) {
		this.status = status;
	}
}
