package com.voizd.service.user.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.enumeration.UserStatusEnum;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.authentication.AuthenticationDAO;
import com.voizd.dao.modules.country.CountryDAO;
import com.voizd.dao.modules.user.UserCounterDAO;
import com.voizd.dao.modules.user.UserDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.mail.MailServiceFailedException;
import com.voizd.framework.random.RandomKeyGenerator;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.util.email.EmailUtil;
import com.voizd.service.authentication.manager.AuthenticationManager;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.helper.StationUtils;
import com.voizd.service.station.bo.StationBO;

public class UserBOImpl implements UserBO {
	Logger logger = LoggerFactory.getLogger(UserBOImpl.class);

	private UserDAO userDAO;
	
	private UserCounterDAO  userCounterDAO;
	
	private AuthenticationDAO authenticationDAO;
	
	private CountryDAO countryDAO;

	private AuthenticationManager authenticationManager;

	private StationBO stationBO;
	
	public void setStationBO(StationBO stationBO) {
		this.stationBO = stationBO;
	}

	/**
	 * @param authenticationManager
	 *            the authenticationManager to set
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
		this.authenticationDAO = authenticationDAO;
	}

	public void setUserCounterDAO(UserCounterDAO userCounterDAO) {
		this.userCounterDAO = userCounterDAO;
	}

	
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.bo.UserBO#getUserProfile(java.lang.String)
	 */
	public UserVO getUserProfile(long userId) throws UserServiceFailedException {
		try {
			UserInfo user = userDAO.getUserProfile(userId);
			if (null == user || user.getId() == 0) {
				logger.info("User not found userId: " + userId);
				throw new UserServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
			UserVO userVO = UserUtils.transformUserInfoToUserVO(user);
			userVO.setPrimaryEmailAddress(user.getPrimaryEmailAddress());
			UserAuth userAuth = authenticationDAO.getUserAuthDetailsById(userId);
			if(null!=userAuth){
				userVO.setStatus(userAuth.getStatus());
				userVO.setLongitude(userAuth.getLongitude());
				userVO.setLatitude(userAuth.getLatitude());
			}
			
			return userVO;
		} catch (DataAccessFailedException e) {
			logger.error("Exception in geting user Profile from database : " + e.getMessage());
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.bo.UserBO#registerUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO registerUser(RegistrationVO registrationVO) throws UserServiceFailedException {
		UserVO userVO = new UserVO();
		try {
			UserAuth userAuth = UserUtils.transformRegisterationVOToUserAuth(registrationVO, true);
			registrationVO.setId(userDAO.saveUserAuthDetails(userAuth));

			UserInfo userInfo = UserUtils.transformUserVOToUserInfo(registrationVO);
			try {
				logger.debug("registerUser  :country :: "+userInfo.getCountry());
				logger.debug("registerUser  : language :: "+userInfo.getLanguage());
				if(userInfo.getCountry() != null && userInfo.getCountry().trim().length()>0){
					String country = countryDAO.getUserCountry(userInfo.getCountry());
					if(StringUtils.isNotBlank(country)){
						userInfo.setCountry(countryDAO.getUserCountry(userInfo.getCountry()));
					}
					logger.debug("registerUser "+country+ "country :: "+userInfo.getCountry());
				}else{
					userInfo.setCountry(VoizdConstant.DEFAULT_COUNTRY);
				}
				if(userInfo.getLanguage() != null && userInfo.getLanguage().trim().length()>0){
					userInfo.setLanguage(countryDAO.getUserLanguage(userInfo.getLanguage()));
				}else{
					userInfo.setLanguage(VoizdConstant.DEFAULT_LANGUAGE);
				}
				userInfo.setPrimaryEmailAddress(registrationVO.getUserName());
			} catch (DataAccessFailedException e1) {
				logger.error("Exception in while user country or language for username : "+userAuth.getUserName() );
			}
			
			userDAO.saveUserInfo(userInfo);
			if(null!=registrationVO && registrationVO.getPushKey()!=null){
				try{
				UserPushInfo userPushInfo = new UserPushInfo();
				userPushInfo.setUserId(userInfo.getId());
				userPushInfo.setDeviceKey(registrationVO.getPushKey());
				userPushInfo.setNotifType(registrationVO.getCurrentPlatform());
				userPushInfo.setSendNotif("no");
				userDAO.saveUserPushInfo(userPushInfo);
				}catch (Exception e) {
					logger.error("Exception  while saveUserPushInfo  "+e.getLocalizedMessage(),e);
				}
			}
			logger.debug("registrationVO.getRegistrationMode() : "+registrationVO.getRegistrationMode() );
			if (registrationVO.getRegistrationMode() > 0) {
				UserThirdPartyAuth userThirdPartyAuth = new UserThirdPartyAuth(userAuth.getId(), registrationVO.getRegistrationMode(),
						registrationVO.getUserKey(), registrationVO.getAppKey());
				userDAO.saveUserThirdPartyAuthDetails(userThirdPartyAuth);
				userVO.setStatus(UserStatusEnum.ACTIVE.getStatus());
				try {
					UserInfo user = userDAO.getUserProfile(userAuth.getId());
		        	userVO.setId(userAuth.getId());
			        userVO.setLanguage(user.getLanguage());
			        userVO.setProfilePicFileId(user.getProfilePicFileId());
			        userVO.setLocation(user.getLocation());

					StationVO stationVO = StationUtils.getDefaultStationVO(userVO);
					stationBO.createStation(stationVO);
				} catch (StationServiceException e) {
					logger.error("Error while creating default station : Please check logs " + e.getLocalizedMessage(), e);
				}
			} else {
				userVO.setStatus(UserStatusEnum.INACTIVE.getStatus());
				String verificaitonCode = RandomKeyGenerator.generateRandomAlphanumericKey(90);
				UserEmailVerification userEmailVerification = new UserEmailVerification(userAuth.getId(), userAuth.getUserName(), verificaitonCode, 1,
						new Date(), null);
				long emailVerificationId = userDAO.saveUserEmailVerification(userEmailVerification);
				// SEND EMAIL
				try {
					logger.error("emailVerificationId :: "+emailVerificationId+" ,emailid : "+ userAuth.getUserName()+" ,verificaitonCode :: "+verificaitonCode);
					authenticationManager.verifyEmailAddress(emailVerificationId, userAuth.getUserName(), verificaitonCode);
					//EmailUtil.sendEmailVerificationEmail(registrationVO.getFirstName(), registrationVO.getLastName(), emailVerificationId,
					///		userAuth.getUserName(), verificaitonCode);
				} catch (Exception e) {
					logger.error("Exception in sending verification email. Trying again ", e);
					//EmailUtil.sendEmailVerificationEmail(registrationVO.getFirstName(), registrationVO.getLastName(), emailVerificationId,
					//		userAuth.getUserName(), verificaitonCode);
				}
			}

		} catch (DataUpdateFailedException e) {
			if (ErrorCodesEnum.DATABASE_UNIQUE_CONSTRAINT_VOILATION_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				logger.error("User already present in system:  username : " + registrationVO.getUserName() + " : registeration mode : "
						+ registrationVO.getRegistrationMode() + " " + e.getMessage());
				throw new UserServiceFailedException(ErrorCodesEnum.USER_ALREADY_EXIST);
			}
			logger.error("Exception in storing user details in database for the user : " + registrationVO + " error  : " + e.getMessage());
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);

		} catch (Exception e) {
			// INTENTIONALLY IGNORED AFTER SECOND TRY
			logger.error("Exception in sending verification email. Going ahead with the registraion. Case need to be handled separately " + e, e);
		}
		try {
			BeanUtils.copyProperties(userVO, registrationVO);
		} catch (IllegalAccessException e) {
			logger.error("Exception in transforming registerationVO error  : " + e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("Exception in transforming registerationVO error  : " + e.getMessage());
		}
		logger.debug("userVO in storing ---"+userVO.getStatus());
		if (registrationVO.getRegistrationMode() > 0) {
			userVO.setStatus(UserStatusEnum.ACTIVE.getStatus());
			logger.debug("usereeeVO in storing ---"+userVO.getStatus());
		}
		return userVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.bo.UserBO#updateUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO updateUser(UserVO userVO) throws UserServiceFailedException {
		UserVO dbUserVO = getUserProfile(userVO.getId());
		//userVO = getUserCountryOrLanguage(userVO);
		UserUtils.updateDBUserVO(dbUserVO, userVO);
		UserInfo userInfo = UserUtils.transformUserVOToUserInfo(dbUserVO);
		try {
			userDAO.updateUserInfo(userInfo);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating user details in database for the user : " + userVO + " error  : " + e.getMessage());
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		return dbUserVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.bo.UserBO#updateUserStatus(java.lang.String, java.lang.String)
	 */
	public void updateUserStatus(long userId, int userStatus) throws UserServiceFailedException {
		try {
			userDAO.updateUserStatus(userId, userStatus);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating user status in database for the user : " + userId + " error  : " + e.getMessage());
			throw new UserServiceFailedException(ErrorCodesEnum.USER_STATUS_UPDATE_FAILED_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.bo.UserBO#getUserAuthDetails(java.lang.String, int)
	 */
	public AuthenticationDetailsVO getUserAuthDetails(String userName) throws UserServiceFailedException {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	private UserVO getUserCountryOrLanguage(UserVO userVO){
		if(userVO != null && userVO.getCountry() != null){
			try {
				userVO.setCountry(countryDAO.getUserCountry(userVO.getCountry()));
			} catch (DataAccessFailedException e) {
				logger.error("Exception in updating user in getUserCountry: " + userVO.getId() + " error  : " + e.getMessage());
			}
		}
		if(userVO != null && userVO.getLanguage() != null){
			try {
				userVO.setCountry(countryDAO.getUserCountry(userVO.getCountry()));
			} catch (DataAccessFailedException e) {
				logger.error("Exception in updating user in getUserCountry: " + userVO.getId() + " error  : " + e.getMessage());
			}
		}
		return userVO;
	}*/
	
	public void createUserCounter(Long userId) throws UserServiceFailedException{
		try {
			userCounterDAO.createUserCounter(userId);
		} catch (DataUpdateFailedException e) {
			throw new UserServiceFailedException(ErrorCodesEnum.USER_COUNTER_SERVICE_FAILED_EXCEPTION);
		}
	}

	@Override
	public void updateUserPushStatus(long userId, String pushStatus)
			throws UserServiceFailedException {
		try {
			userDAO.updateUserPushStatus(userId, pushStatus);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updateUserPushStatus"+e.getLocalizedMessage(),e);
			throw new UserServiceFailedException(ErrorCodesEnum.USER_PUSH_UPDATE_EXCEPTION);
		}
	}

}
