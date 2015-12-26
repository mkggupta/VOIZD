/**
 * 
 */
package com.voizd.framework.exception;

/**
 * @author Admin
 * 
 */
public interface IVZException {

	final String SYSTEM_EXCEPTION_TYPE = "S";
	final String BUSINESS_EXCEPTION_TYPE = "B";

	/**
	 * Returns the exception type.
	 * 
	 * @return
	 */
	String getExceptionType();

	/**
	 * Sets the exception type. Currently possible values are system and business
	 * 
	 * @param exceptionType
	 */
	void setExceptionType(String exceptionType);

	/**
	 * Returns the error key/code of the exception
	 * 
	 * @return
	 */
	String getErrorCode();

	/**
	 * Sets the error code of the exception.
	 * 
	 * @param errorCode
	 */
	void setErrorCode(String errorCode);
}
