package com.voizd.service.stream.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class StreamServiceException extends VZBusinessException{

	public StreamServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	
	public StreamServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	public StreamServiceException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	public StreamServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
