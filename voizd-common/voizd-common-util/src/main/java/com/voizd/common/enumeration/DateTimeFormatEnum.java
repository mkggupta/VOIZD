/**
 * 
 */
package com.voizd.common.enumeration;

/**
 * @author Manish
 * 
 */
public enum DateTimeFormatEnum {

	WEB_DATE_FORMAT("dd/mm/yyyy");

	String format;

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	DateTimeFormatEnum(String format) {
		this.format = format;
	}
}
