/**
 * 
 */
package com.voizd.framework.encryption.enumeration;

/**
 * @author Suresh
 * 
 */
public enum EncryptionAlgoEnum {
	DEFAULT(0), MD5(1);
	int type;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	EncryptionAlgoEnum(int type) {
		this.type = type;
	}

}
