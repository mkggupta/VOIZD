/**
 * 
 */
package com.voizd.framework.mail;

import com.voizd.framework.exception.VZSystemException;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Suresh
 * 
 */
public class MailServiceFailedException extends VZSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8245453540290474764L;

	/**
	 * @param errorCodeEnum
	 * @param e
	 */
	public MailServiceFailedException(ErrorCodesEnum errorCodeEnum, Exception e) {
		super(errorCodeEnum, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCodeEnum
	 */
	public MailServiceFailedException(ErrorCodesEnum errorCodeEnum) {
		super(errorCodeEnum);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param e
	 */
	public MailServiceFailedException(String errorCode, Exception e) {
		super(errorCode, e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 */
	public MailServiceFailedException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
