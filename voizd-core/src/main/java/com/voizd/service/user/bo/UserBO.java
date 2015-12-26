/**
 * 
 */
package com.voizd.service.user.bo;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.service.user.exception.UserServiceFailedException;

/**
 * @author Manish
 * 
 */
public interface UserBO {

	UserVO getUserProfile(long userId) throws UserServiceFailedException;

	UserVO registerUser(RegistrationVO registerationVO) throws UserServiceFailedException;

	UserVO updateUser(UserVO userVO) throws UserServiceFailedException;

	void updateUserStatus(long userId, int userStatus) throws UserServiceFailedException;

	AuthenticationDetailsVO getUserAuthDetails(String userName) throws UserServiceFailedException;
	
	void createUserCounter(Long userId) throws UserServiceFailedException;
	
	void updateUserPushStatus(long userId, String pushStatus) throws UserServiceFailedException;

}
