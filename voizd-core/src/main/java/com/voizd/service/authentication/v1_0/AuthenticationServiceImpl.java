/**
 * 
 */
package com.voizd.service.authentication.v1_0;

import java.util.Map;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;
import com.voizd.service.authentication.manager.AuthenticationManager;

/**
 * @author Manish
 * 
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	private AuthenticationManager authenticationManager;

	/**
	 * @return the authenticationManager
	 */
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * @param authenticationManager
	 *            the authenticationManager to set
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#authenticateUser(java.lang.String, java.lang.String)
	 */
	public AuthenticationDetailsVO authenticateUser(String userName, String password, String currentClientVersion, String currentPlatform,
			boolean updateLoginStats, Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		return authenticationManager.authenticateUser(userName, password, currentClientVersion, currentPlatform, updateLoginStats,clientMap,pushKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#authenticateUser(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationDetailsVO authenticateUser(String userName, int loginMode, String partnerUserKey, String appId, String currentClientVersion,
			String currentPlatform, boolean updateLoginStats, Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		return authenticationManager.authenticateUser(userName, loginMode, partnerUserKey, appId, currentClientVersion, currentPlatform, updateLoginStats,
				clientMap,pushKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#logoutUser(java.lang.String)
	 */
	@Override
	public void logoutUser(String userName) throws AuthenticationServiceFailedException {
		authenticationManager.logoutUser(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(String userName, String password, String oldPassword) throws AuthenticationServiceFailedException {
		authenticationManager.changePassword(userName, password, oldPassword);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#resetPassword(java.lang.String)
	 */
	public void resetPassword(String userName) throws AuthenticationServiceFailedException {
		authenticationManager.resetPassword(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#verifyEmailAddress(java.lang.String, java.lang.String)
	 */
	public void verifyEmailAddress(long id, String emailAddress, String verificationCode) throws AuthenticationServiceFailedException {
		authenticationManager.verifyEmailAddress(id, emailAddress, verificationCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#changePassword(long, long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(long userId, long forgotPasswordId, String verificationCode, String userName, String password)
			throws AuthenticationServiceFailedException {
		authenticationManager.changePassword(userId, forgotPasswordId, verificationCode, userName, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.v1_0.AuthenticationService#initiateEmailVerification(java.lang.String)
	 */
	@Override
	public void initiateEmailVerification(String userName) throws AuthenticationServiceFailedException {
		authenticationManager.initiateEmailVerification(userName);
	}

}
