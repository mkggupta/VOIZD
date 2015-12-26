/**
 * 
 */
package com.voizd.framework.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Suresh
 * 
 */
public class MD5Encrypter extends BaseEncrypter {
	Logger logger = LoggerFactory.getLogger(MD5Encrypter.class);

	public String encrypt(String value) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(value.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			 for (int i = 0; i < messageDigest.length; i++) {
	                String h = Integer.toHexString(0xFF & messageDigest[i]);
	                while (h.length() < 2)
	                    h = "0" + h;
	                hexString.append(h);
	            }
			
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception in md5 encryption ", e);
			return value;
		}

	}
	
	
}
