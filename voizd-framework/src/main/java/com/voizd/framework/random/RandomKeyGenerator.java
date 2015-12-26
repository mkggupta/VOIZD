/**
 * 
 */
package com.voizd.framework.random;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Suresh
 * 
 */
public class RandomKeyGenerator {

	public static String generateRandomAlphanumericKey(int length) {
		return RandomStringUtils.random(length, true, true);
	}
}
