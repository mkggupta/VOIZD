package com.voizd.service.location.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class LocationServiceException extends VZBusinessException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LocationServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		
	}

	public LocationServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		
	}

	public LocationServiceException(String errorCode) {
		super(errorCode);
		
	}
	public LocationServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		
	}



}
