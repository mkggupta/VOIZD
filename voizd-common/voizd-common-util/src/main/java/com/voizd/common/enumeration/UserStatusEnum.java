/**
 * 
 */
package com.voizd.common.enumeration;

/**
 * @author Manish
 * 
 */
public enum UserStatusEnum {
	INACTIVE(0), ACTIVE(1), BLOCKED(2);
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

	UserStatusEnum(int status) {
		this.status = status;
	}
}
