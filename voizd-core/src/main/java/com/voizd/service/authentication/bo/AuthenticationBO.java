/**
 * 
 */
package com.voizd.service.authentication.bo;

import java.util.Map;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;

/**
 * @author Manish
 * 
 */
public interface AuthenticationBO {
	AuthenticationDetailsVO getUserAuthDetailsList(String userName) throws AuthenticationServiceFailedException;

	AuthenticationDetailsVO authenticateUser(String userName, String password, String currentClientVersion, String currentPlatform,
			boolean updateLoginStats,Map<String, Object> clientMap,String pushKey)
			throws AuthenticationServiceFailedException;

	AuthenticationDetailsVO authenticateUser(String userName, int loginMode, String partnerUserKey, String appId, String currentClientVersion,
			String currentPlatform, boolean updateLoginStats,Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException;

	void updateUserLoginStatus(String userName, int status) throws AuthenticationServiceFailedException;

	void changePassword(String userName, String password,String oldPassword) throws AuthenticationServiceFailedException;

	long verifyEmailAddress(long id, String emailAddress, String verificationCode) throws AuthenticationServiceFailedException;

	void resetPassword(String userName) throws AuthenticationServiceFailedException;

	long verifyForgetPasswordCode(long id, long userId, String verificationCode) throws AuthenticationServiceFailedException;

	void changePassword(long userId, String password) throws AuthenticationServiceFailedException;

	void initiateEmailVerification(String userName) throws AuthenticationServiceFailedException;
	
	long getUserId(String partnerUserKey, int appId) throws AuthenticationServiceFailedException;
}
