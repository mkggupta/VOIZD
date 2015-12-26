/**
 * 
 */
package com.voizd.framework.encryption;

import com.voizd.framework.encryption.enumeration.EncryptionAlgoEnum;

/**
 * @author Suresh
 * 
 */
public class EncryptionFactory {

	public static BaseEncrypter getEncrypter(EncryptionAlgoEnum encryptionAlgoEnum) {
		if (EncryptionAlgoEnum.MD5.getType() == encryptionAlgoEnum.getType()) {
			return new MD5Encrypter();
		} else
			return new BaseEncrypter();
	}
}
