/**
 * 
 */
package com.voizd.framework.exception;

import java.util.HashMap;

/**
 * @author Admin
 * 
 */
public class ExceptionUtil {

	HashMap<String, String> errorCodeMap = new HashMap<String, String>();

	/**
	 * This will return the configured error message for an error code based on the language code.
	 * 
	 * @param errorCode
	 * @param langCode
	 * @return
	 * @throws NotImplementedException
	 */
	// TODO NEED TO IMPLEMENT
	public String getErrorMessageByErrorCode(String errorCode, String langCode) throws NotImplementedException {
		throw new NotImplementedException();
	}
}
