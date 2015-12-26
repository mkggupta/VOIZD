/**
 * 
 */
package com.voizd.framework.exception;

import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Admin
 * 
 */
public class VZBusinessException extends VZException {

	/**
	 * @param errorCode
	 * @param e
	 */
	public VZBusinessException(String errorCode, Exception e) {
		super(errorCode, e);
		super.setExceptionType(BUSINESS_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCode
	 */
	public VZBusinessException(String errorCode) {
		super(errorCode);
		super.setExceptionType(BUSINESS_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCodeEnum
	 * @param e
	 */
	public VZBusinessException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		super.setExceptionType(BUSINESS_EXCEPTION_TYPE);
	}

	/**
	 * @param errorCodeEnum
	 */
	public VZBusinessException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1146757635940220423L;

}
