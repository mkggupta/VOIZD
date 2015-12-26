/**
 * 
 */
package com.voizd.service.authentication.exception;

import com.voizd.framework.exception.VZSystemException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Manish
 *
 */
public class AuthenticationServiceFailedException extends VZSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6635394944116362028L;

	/**
	 * @param errorCode
	 * @param e
	 */
	public AuthenticationServiceFailedException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 */
	public AuthenticationServiceFailedException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCodeEnum
	 * @param e
	 */
	public AuthenticationServiceFailedException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCodeEnum
	 */
	public AuthenticationServiceFailedException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

}
