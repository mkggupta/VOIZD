/**
 * 
 */
package com.voizd.framework.exception;

import com.voizd.framework.exception.util.ErrorCodesEnum;


/**
 * @author Admin
 * 
 */
public class VZSystemException extends VZException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4197422745681241940L;

	/**
	 * @param errorCode
	 * @param e
	 */
	public VZSystemException(String errorCode, Exception e) {
		super(errorCode, e);
		super.setExceptionType(SYSTEM_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCode
	 */
	public VZSystemException(String errorCode) {
		super(errorCode);
		super.setExceptionType(SYSTEM_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCodeEnum
	 * @param e
	 */
	public VZSystemException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		super.setExceptionType(SYSTEM_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCodeEnum
	 */
	public VZSystemException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
	}
}
