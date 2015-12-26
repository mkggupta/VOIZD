package com.voizd.service.tagcloud.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class TagCloudServiceException extends VZBusinessException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TagCloudServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		
	}

	public TagCloudServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		
	}

	public TagCloudServiceException(String errorCode) {
		super(errorCode);
		
	}
	public TagCloudServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		
	}



}
