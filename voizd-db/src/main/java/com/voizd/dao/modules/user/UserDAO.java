/**
 * 
 */
package com.voizd.dao.modules.user;

import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author Manish
 * 
 */
public interface UserDAO {

	UserInfo getUserProfile(long id) throws DataAccessFailedException;

	void saveUserInfo(UserInfo userInfo) throws DataUpdateFailedException;

	Long saveUserAuthDetails(UserAuth userAuth) throws DataUpdateFailedException;

	UserAuth getUserAuthDetails(String userName) throws DataAccessFailedException;

	int getUserStatusById(long id) throws DataAccessFailedException;
	
	void updateUserInfo(UserInfo userInfo) throws DataUpdateFailedException;

	void saveUserThirdPartyAuthDetails(UserThirdPartyAuth userThirdPartyAuth) throws DataUpdateFailedException;

	long saveUserEmailVerification(UserEmailVerification userEmailVerification) throws DataUpdateFailedException;

	long updateUserStatus(long userId, int userStatus) throws DataUpdateFailedException;
	
	void updateUserPushStatus(long userId, String pushStatus) throws DataUpdateFailedException;
	
	void updateUserPushStatus(long userId, String pushStatus,String pushKey,String currentPlatform) throws DataUpdateFailedException;
	
	void saveUserPushInfo(UserPushInfo userPushInfo) throws DataUpdateFailedException;
	
	UserPushInfo getUserPushInfo(long id) throws DataAccessFailedException;
}
