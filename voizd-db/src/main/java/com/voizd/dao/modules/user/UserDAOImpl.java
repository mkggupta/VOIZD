/**
 * 
 */
package com.voizd.dao.modules.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.voizd.dao.constants.UserConstants;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Manish
 * 
 */
public class UserDAOImpl extends AbstractDBManager implements UserDAO {
	Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.user.UserDAO#getUserProfile()
	 */
	public UserInfo getUserProfile(long id) throws DataAccessFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, id);
			return (UserInfo) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_INFO, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.user.UserDAO#getUserAuthDetails(java.lang.String, int)
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
	 * @see com.voizd.dao.modules.user.UserDAO#createUser(com.voizd.dao.entities.UserInfo)
	 */
	public void saveUserInfo(UserInfo userInfo) throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(UserConstants.QUERY_INSERT_USER_INFO, userInfo);
		} catch (SQLException e) {
			logger.error("Exception in storing user details in database for the user : " + userInfo + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.user.UserDAO#createUser(com.voizd.dao.entities.UserInfo)
	 */
	public void updateUserInfo(UserInfo userInfo) throws DataUpdateFailedException {
		try {
			sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_INFO, userInfo);
		} catch (SQLException e) {
			logger.error("Exception in storing user details in database for the user : " + userInfo + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.user.UserDAO#saveUserAuthDetails(com.voizd.dao.entities.UserAuth)
	 */
	public Long saveUserAuthDetails(UserAuth userAuth) throws DataUpdateFailedException {
		try {
			return (Long) sqlMapClient_.insert(UserConstants.QUERY_INSERT_USER_AUTH_DETAILS, userAuth);
		} catch (NestedSQLException e) {
			Throwable t = e;
			while (t.getCause() != null) {
				t = t.getCause();
			}
			if (t instanceof MySQLIntegrityConstraintViolationException) {
				logger.error("User already present in database username : " + userAuth.getUserName() + " : registration mode : "
						+ userAuth.getRegistrationMode() + " " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_UNIQUE_CONSTRAINT_VOILATION_EXCEPTION, e);
			} else {
				logger.error("Exception in storing user authentication details in database for the user : " + userAuth + " error  : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}

		} catch (SQLException e) {
			logger.error("Exception in storing user authentication details in database for the user : " + userAuth + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
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
	 * @see com.voizd.dao.modules.user.UserDAO#saveUserEmailVerification(com.voizd.dao.entities.UserEmailVerification)
	 */
	@Override
	public long saveUserEmailVerification(UserEmailVerification userEmailVerification) throws DataUpdateFailedException {
		try {
			return (Long) sqlMapClient_.insert(UserConstants.QUERY_INSERT_EMAIL_VERIFICATION_DETAILS, userEmailVerification);
		} catch (SQLException e) {
			logger.error("Exception in storing email verification details in database for the user : " + userEmailVerification.getUserId() + " error  : "
					+ e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.user.UserDAO#updateUserStatus(long, int)
	 */
	@Override
	public long updateUserStatus(long userId, int userStatus) throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.COLUMN_ID, userId);
			parameterMap.put(UserConstants.COLUMN_USER_STATUS, userStatus);
			return sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_STATUS, parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating user's password for the user : " + userId + " userStatus " + userStatus + " error : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public int getUserStatusById(long id) throws DataAccessFailedException {
		try {
			return (Integer) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_STATUS_BY_ID, id);
		} catch (SQLException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateUserPushStatus(long userId, String userStatus)
			throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.USER_ID, userId);
			parameterMap.put(UserConstants.SEND_NOTIFICATION, userStatus);
			sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_PUSH_MESSAGE_STATUS,parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating push status : "+e.getLocalizedMessage(),e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void saveUserPushInfo(UserPushInfo userPushInfo)
			throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(UserConstants.QUERY_INSERT_USER_PUSH_INFO,userPushInfo);
		} catch (SQLException e) {
			logger.error("Exception in updating push status : "+ e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateUserPushStatus(long userId, String pushStatus,
			String pushKey, String currentPlatform)
			throws DataUpdateFailedException {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put(UserConstants.USER_ID, userId);
			parameterMap.put(UserConstants.SEND_NOTIFICATION, pushStatus);
			parameterMap.put(UserConstants.PUSH_KEY, pushKey);
			parameterMap.put(UserConstants.PLATFORM, currentPlatform);
			sqlMapClient_.update(UserConstants.QUERY_UPDATE_USER_PUSH_STATUS_WITH_PUSH_KEY,parameterMap);
		} catch (SQLException e) {
			logger.error("Exception in updating push status : "+e.getLocalizedMessage(),e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public UserPushInfo getUserPushInfo(long userId)
			throws DataAccessFailedException {
		try {
			return  (UserPushInfo) sqlMapClientSlave_.queryForObject(UserConstants.QUERY_GET_USER_PUSH_INFO, userId);
		} catch (SQLException e) {
			logger.error("Exception in geting user getUserPushInfo from database : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	

	
}
