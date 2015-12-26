/**
 * 
 */
package com.voizd.dao.modules.authentication;

import com.voizd.dao.entities.ForgetPasswordVerification;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author Manish
 * 
 */
public interface AuthenticationDAO {

	UserAuth getUserAuthDetails(String userName) throws DataAccessFailedException;

	UserThirdPartyAuth getUserThirdPartyAuthDetails(long id, int thirdPartyId) throws DataAccessFailedException;

	void saveUserThirdPartyAuthDetails(UserThirdPartyAuth userThirdPartyAuth) throws DataUpdateFailedException;

	int updateUserLoginStatus(String userName, int status) throws DataUpdateFailedException;

	int updatePassword(String userName, String password) throws DataUpdateFailedException;

	int updatePassword(long userId, String password) throws DataUpdateFailedException;

	UserEmailVerification getUserEmailVerification(long id) throws DataAccessFailedException;

	long saveForgetPasswordVerification(ForgetPasswordVerification forgetPasswordVerification) throws DataUpdateFailedException;

	ForgetPasswordVerification getForgetPasswordVerification(long id) throws DataAccessFailedException;

	void updateForgetPasswordVerification(long id, int status) throws DataAccessFailedException;

	int updateUserLoginParams(String userName, int status, String currentClientVersion, String currentPlatform,String pushKey) throws DataUpdateFailedException;
	
	int updateUserLoginParams(long id, int status, String currentClientVersion, String currentPlatform,String pushKey) throws DataUpdateFailedException;
	
	UserThirdPartyAuth getUserThirdPartyAuthDetails(String userKey, int thirdPartyId) throws DataAccessFailedException;
	
	UserThirdPartyAuth getUserThirdPartyAuthDetails(String userKey,String appKey, int thirdPartyId) throws DataAccessFailedException;
	
	UserAuth getUserAuthDetailsById(long id) throws DataAccessFailedException;

}
