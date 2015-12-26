/**
 * 
 */
package com.voizd.service.media.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author arvind
 *
 */
public class MediaServiceFailedException extends  VZBusinessException {

	public MediaServiceFailedException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	
	public MediaServiceFailedException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	public MediaServiceFailedException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	public MediaServiceFailedException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

}
