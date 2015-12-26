/**
 * 
 */
package com.voizd.service.user.v1_0;

import java.util.Map;

import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.exception.UserServiceValidationFailedException;
import com.voizd.service.user.manager.UserManager;
import com.voizd.service.user.validation.UserServiceValidator;

/**
 * @author Manish
 * 
 */
public class UserServiceImpl implements UserService {

	private UserManager userManager;

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager
	 *            the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.v1_0.UserService#registerUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO registerUser(RegistrationVO registerationVO, Map<String, Object> clientMap) throws UserServiceFailedException,
			UserServiceValidationFailedException {
		UserServiceValidator.validateRegistrationRequest(registerationVO);
		return userManager.registerUser(registerationVO, clientMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.v1_0.UserService#updateUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO updateUser(UserVO userVO,Map<String, Object> clientMap) throws UserServiceFailedException {
		return userManager.updateUser(userVO,clientMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.v1_0.UserService#updateUserStatus(java.lang.String, java.lang.String)
	 */
	public void updateUserStatus(long userId, int userStatus) throws UserServiceFailedException {
		userManager.updateUserStatus(userId, userStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.v1_0.UserService#getUserProfile()
	 */
	public UserVO getUserProfile(long userId, Map<String, Object> clientMap) throws UserServiceFailedException {
		return userManager.getUserProfile(userId, clientMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.v1_0.UserService#getUserProfile(long)
	 */
	@Override
	public UserVO getUserProfile(long userId) throws UserServiceFailedException {
		return userManager.getUserProfile(userId);
	}

	@Override
	public UserDTO getUserPublicProfile(long userId, long visitorId, Map<String, Object> clientMap) throws UserServiceFailedException {
		return userManager.getUserPublicProfile(userId,visitorId,clientMap);
	}

	@Override
	public UserDTO getUserPrivateProfile(long userId,Map<String, Object> clientMap) throws UserServiceFailedException {
		return userManager.getUserPrivateProfile(userId, clientMap);
	}
	@Override
	public UserDTO getFriendPublicProfile(String friendId,int thirdPartyId,long visitorId,Map<String, Object> clientMap) throws UserServiceFailedException{
		return userManager.getFriendPublicProfile(friendId,thirdPartyId,visitorId,clientMap);
	}

	@Override
	public void updateUserPushStatus(long userId, String pushStatus)
			throws UserServiceFailedException {
		userManager.updateUserPushStatus(userId, pushStatus);
	}

	
}
