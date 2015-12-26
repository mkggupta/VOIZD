/**
 * 
 */
package com.voizd.service.user.manager;

import java.util.Map;

import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.service.user.exception.UserServiceFailedException;


/**
 * @author Manish
 * 
 */
public interface UserManager {

	UserVO getUserProfile(long userId, Map<String, Object> clientMap) throws UserServiceFailedException;
	
	UserDTO getUserPrivateProfile(long userId, Map<String, Object> clientMap) throws UserServiceFailedException;

	UserVO getUserProfile(long userId) throws UserServiceFailedException;
	
	UserDTO getUserPublicProfile(long userId,long visitorId, Map<String, Object> clientMap) throws UserServiceFailedException;

	UserVO registerUser(RegistrationVO registerationVO, Map<String, Object> clientMap) throws UserServiceFailedException;

	UserVO updateUser(UserVO userVO,Map<String, Object> clientMap) throws UserServiceFailedException;

	void updateUserStatus(long userId, int userStatus) throws UserServiceFailedException;

	AuthenticationDetailsVO getUserAuthDetails(String userName) throws UserServiceFailedException;
	
	UserDTO getFriendPublicProfile(String friendId,int thirdPartyId,long visitorId,Map<String, Object> clientMap) throws UserServiceFailedException;
	
	void updateUserPushStatus(long userId, String pushStatus) throws UserServiceFailedException;

}
