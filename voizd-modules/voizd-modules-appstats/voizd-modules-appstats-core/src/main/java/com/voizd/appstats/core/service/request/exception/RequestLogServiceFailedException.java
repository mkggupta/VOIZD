/**
 * 
 */
package com.voizd.appstats.core.service.request.exception;

import com.voizd.framework.exception.VZSystemException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Suresh
 * 
 */
public class RequestLogServiceFailedException extends VZSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5269607778193404754L;

	/**
	 * @param errorCodeEnum
	 * @param e
	 */
	public RequestLogServiceFailedException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCodeEnum
	 */
	public RequestLogServiceFailedException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param e
	 */
	public RequestLogServiceFailedException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 */
	public RequestLogServiceFailedException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
