/**
 * 
 */
package com.voizd.framework.exception;

import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * This is an exception which is thrown when a unimplemented method/operation is called. Such methods will be implemented in the future releases.
 * 
 * @author Admin
 * 
 */
public class NotImplementedException extends VZSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6754322344879085413L;

	public NotImplementedException() {
		super(ErrorCodesEnum.OPERATION_NOT_IMPLEMENTED_EXCEPTION.getErrorCode());
	}
}
