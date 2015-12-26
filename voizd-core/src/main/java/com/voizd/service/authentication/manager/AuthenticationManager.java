/**
 * 
 */
package com.voizd.service.authentication.manager;

import java.util.Map;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;

/**
 * @author Manish
 * 
 */
public interface AuthenticationManager {

	AuthenticationDetailsVO authenticateUser(String userName, String password, String currentClientVersion, String currentPlatform, boolean updateLoginStats,
			Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException;

	AuthenticationDetailsVO authenticateUser(String userName, int loginMode, String partnerUserKey, String appId, String currentClientVersion,
			String currentPlatform, boolean updateLoginStats, Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException;

	void logoutUser(String userName) throws AuthenticationServiceFailedException;

	void changePassword(String userName, String password, String oldPassword) throws AuthenticationServiceFailedException;

	void verifyEmailAddress(long id, String emailAddress, String verificationCode) throws AuthenticationServiceFailedException;

	void resetPassword(String userName) throws AuthenticationServiceFailedException;

	void changePassword(long userId, long forgotPasswordId, String verificationCode, String userName, String password)
			throws AuthenticationServiceFailedException;

	void initiateEmailVerification(String userName) throws AuthenticationServiceFailedException;
}
