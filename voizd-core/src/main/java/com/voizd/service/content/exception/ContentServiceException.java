package com.voizd.service.content.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class ContentServiceException extends VZBusinessException {

	
	public ContentServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	
	public ContentServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	public ContentServiceException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	public ContentServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
