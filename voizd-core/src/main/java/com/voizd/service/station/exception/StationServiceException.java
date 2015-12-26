package com.voizd.service.station.exception;

import com.voizd.framework.exception.VZBusinessException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class StationServiceException extends VZBusinessException{

	public StationServiceException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	
	public StationServiceException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	public StationServiceException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	public StationServiceException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
