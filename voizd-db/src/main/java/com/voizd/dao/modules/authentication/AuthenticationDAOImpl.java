/**
 * 
 */
package com.voizd.dao.modules.authentication;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.voizd.dao.constants.UserConstants;
import com.voizd.dao.entities.ForgetPasswordVerification;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Manish
 * 
 */
public class AuthenticationDAOImpl extends AbstractDBManager implements AuthenticationDAO {
	Logger logger = LoggerFactory.getLogger(AuthenticationDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#getUserAuthDetailsList(java.lang.String)
	 */
	public UserAuth getUserAuthDetails(String userName) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_USER_NAME, userName);
			return (UserAuth) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_AUTH_DETAILS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#getUserThirdPartyAuthDetails(long)
	 */
	@Override
	public UserThirdPartyAuth getUserThirdPartyAuthDetails(long id, int thirdPartyId) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_ID, thirdPartyId);
			return (UserThirdPartyAuth) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_THIRD_PARTY_AUTH_DETAILS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.user.UserDAO#saveUserAuthDetails(com.voizd.dao.entities.UserAuth)
	 */
	public void saveUserThirdPartyAuthDetails(UserThirdPartyAuth userThirdPartyAuth) throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(UserConstants.QUERY_INSERT_USER_THIRD_PARTY_AUTH_DETAILS, userThirdPartyAuth);
		} catch (NestedSQLException e) {
			Throwable t = e;
			while (t.getCause() != null) {
				t = t.getCause();
			}
			if (t instanceof MySQLIntegrityConstraintViolationException) {
				logger.error("User already present in database username : " + userThirdPartyAuth.getId() + " : registration mode : "
						+ userThirdPartyAuth.getThirdPartyId() + " " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_UNIQUE_CONSTRAINT_VOILATION_EXCEPTION, e);
			} else {
				logger.error("Exception in storing user authentication details in database for the user : " + userThirdPartyAuth.getId() + " error  : "
						+ e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}

		} catch (SQLException e) {
			logger.error("Exception in storing user authentication details in database for the user : " + userThirdPartyAuth.getId() + " error  : "
					+ e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#updateUserOnlineStatus(java.lang.String, boolean)
	 */
	@Override
	public int updateUserLoginStatus(String userName, int status) throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_USER_NAME, userName);
			parameterMap.put(UserConstants.COLUMN_LOGIN_STATUS, status);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_LOGIN_STATUS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's login status : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#updateUserLoginParams(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public int updateUserLoginParams(String userName, int status, String currentClientVersion, String currentPlatform,String pushKey) throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_USER_NAME, userName);
			parameterMap.put(UserConstants.COLUMN_LOGIN_STATUS, status);
			parameterMap.put(UserConstants.COLUMN_CURRENT_CLIENT_VERSION, currentClientVersion);
			parameterMap.put(UserConstants.COLUMN_CURRENT_PLATFORM, currentPlatform);
			parameterMap.put(UserConstants.PUSH_KEY, pushKey);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_LOGIN_PARAMS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's login status : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@Override
	public int updateUserLoginParams(long id, int status, String currentClientVersion, String currentPlatform,String pushKey) throws DataUpdateFailedException{
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			parameterMap.put(UserConstants.COLUMN_LOGIN_STATUS, status);
			parameterMap.put(UserConstants.COLUMN_CURRENT_CLIENT_VERSION, currentClientVersion);
			parameterMap.put(UserConstants.COLUMN_CURRENT_PLATFORM, currentPlatform);
			parameterMap.put(UserConstants.PUSH_KEY, pushKey);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_LOGIN_PARAMS_BY_ID, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's login status : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#updatePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public int updatePassword(String userName, String password) throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_USER_NAME, userName);
			parameterMap.put(UserConstants.COLUMN_PASSWORD, password);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_PASSWORD, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's password for the user : " + userName + " error : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#updatePassword(long, java.lang.String)
	 */
	@Override
	public int updatePassword(long userId, String password) throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, userId);
			parameterMap.put(UserConstants.COLUMN_PASSWORD, password);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_PASSWORD_BY_ID, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's password for the userId : " + userId + " error : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#getUserEmailVerification(long)
	 */
	@Override
	public UserEmailVerification getUserEmailVerification(long id) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			return (UserEmailVerification) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_EMAIL_VERIFICATION_DETAILS_BY_ID, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting email verification details from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#getForgetPasswordVerification(long)
	 */
	@Override
	public ForgetPasswordVerification getForgetPasswordVerification(long id) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			return (ForgetPasswordVerification) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_FORGOT_PASSWORD_VERIFICATION_DETAILS_BY_ID,
					parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting forgot password verification details from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#updateForgetPasswordVerification(long, int)
	 */
	@Override
	public void updateForgetPasswordVerification(long id, int status) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			parameterMap.put(UserConstants.COLUMN_STATUS, status);
			sqlMapClient_.update(UserConstants.QUERY_UPDATE_FORGOT_PASSWORD_VERIFICATION_DETAILS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting forgot password verification details from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.authentication.AuthenticationDAO#saveForgetPasswordVerification(com.voizd.dao.entities.ForgetPasswordVerification)
	 */
	@Override
	public long saveForgetPasswordVerification(ForgetPasswordVerification forgetPasswordVerification) throws DataUpdateFailedException {
		try {
			return (Long) sqlMapClient_.insert(UserConstants.QUERY_INSERT_FORGET_PASSWORD_VERIFICATION_DETAILS, forgetPasswordVerification);
		} catch (SQLException e) {
			logger.error("Exception in storing forget password verification details in database for the user : " + forgetPasswordVerification.getUserId()
					+ " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public UserThirdPartyAuth getUserThirdPartyAuthDetails(String userKey, int thirdPartyId) throws DataAccessFailedException{
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_USER_KEY, userKey);
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_ID, thirdPartyId);
			return (UserThirdPartyAuth) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_AUTH_DETAILS_BY_USER_KEY, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public UserThirdPartyAuth getUserThirdPartyAuthDetails(String userKey,String appKey, int thirdPartyId) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_USER_KEY, userKey);
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_APP_KEY, appKey);
			parameterMap.put(UserConstants.COLUMN_THIRD_PARTY_ID, thirdPartyId);
			return (UserThirdPartyAuth) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_AUTH_DETAILS_BY_ALL, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public UserAuth getUserAuthDetailsById(long id)throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			return (UserAuth) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_AUTH_DETAILS_BY_ID, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	

}
