package com.voizd.service.advt.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class AdvtServiceException extends VZBusinessException {
	public AdvtServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	
	public AdvtServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	public AdvtServiceException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	public AdvtServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
