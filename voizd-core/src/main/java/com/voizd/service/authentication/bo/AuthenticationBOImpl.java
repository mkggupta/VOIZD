/**
 * 
 */
package com.voizd.service.authentication.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.parser.node.SetPropertyExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.enumeration.LoginStatusEnum;
import com.voizd.common.enumeration.UserStatusEnum;
import com.voizd.dao.entities.ForgetPasswordVerification;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserEmailVerification;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.entities.UserThirdPartyAuth;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.authentication.AuthenticationDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.country.CountryDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.user.UserDAO;
import com.voizd.framework.encryption.EncryptionFactory;
import com.voizd.framework.encryption.enumeration.EncryptionAlgoEnum;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.mail.MailServiceFailedException;
import com.voizd.framework.random.RandomKeyGenerator;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;
import com.voizd.service.content.bo.ContentBO;
import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.service.tagcloud.bo.TagCloudBO;
import com.voizd.service.tagcloud.exception.TagCloudServiceException;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.util.email.EmailUtil;
import com.voizd.common.constant.VoizdConstant;

/**
 * @author Manish
 * 
 */
public class AuthenticationBOImpl implements AuthenticationBO {

	Logger logger = LoggerFactory.getLogger(AuthenticationBOImpl.class);

	private UserDAO userDAO;
	
	private CountryDAO countryDAO;

	private AuthenticationDAO authenticationDAO;
	
	private StationDAO  stationDAO ;
	
	private ContentDAO  contentDAO ;
	
	private TagCloudBO tagCloudBO;
	 
	private MediaDAO mediaDAO ;
	
	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}
	
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setTagCloudBO(TagCloudBO tagCloudBO) {
			this.tagCloudBO = tagCloudBO;
	}

	
	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @return the authenticationDAO
	 */
	public AuthenticationDAO getAuthenticationDAO() {
		return authenticationDAO;
	}

	/**
	 * @param authenticationDAO
	 *            the authenticationDAO to set
	 */
	public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
		this.authenticationDAO = authenticationDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#getUserAuthDetailsList(java.lang.String)
	 */
	public AuthenticationDetailsVO getUserAuthDetailsList(String userName) throws AuthenticationServiceFailedException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#authenticateUser(java.lang.String, java.lang.String)
	 */
	public AuthenticationDetailsVO authenticateUser(String userName, String password, String currentClientVersion, String currentPlatform,
			boolean updateLoginStats,Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		AuthenticationDetailsVO authenticationDetailsVO;
		try {
			logger.debug("authenticateUser :userName :"+userName +" ,updateLoginStats :"+updateLoginStats);
			
			UserAuth userAuth = authenticationDAO.getUserAuthDetails(userName);

			if (null != userAuth) {
				String storedPassword = userAuth.getPassword();

				// CHECK STATUS
			/*	if (UserStatusEnum.INACTIVE.getStatus() == userAuth.getStatus()) {
					logger.error("Exception in authentication user : " + userName + " user is inactive ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_STATUS_INACTIVE);

				} else*/ if (UserStatusEnum.BLOCKED.getStatus() == userAuth.getStatus()) {

					logger.error("Exception in authentication user : " + userName + " user is blocked ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_IS_BLOCKED);

				} else {

					String encryptedPassword = EncryptionFactory.getEncrypter(EncryptionAlgoEnum.DEFAULT).encrypt(password);
					if (encryptedPassword.equalsIgnoreCase(storedPassword)) {
						authenticationDetailsVO = UserUtils.transformUserAuthToAuthenticationDetailsVO(userAuth);
						UserVO userVO = getUserVO(clientMap);
						if (updateLoginStats) {
							updateLoginParams(userAuth.getId(), currentClientVersion, currentPlatform,pushKey);
							updateUserInfo(userAuth.getId(),userVO);
							updatePushUserInfo(userAuth.getId(),"no",pushKey,currentPlatform);
						}
					} else {
						logger.error("Authentication failed for user : " + userName);
						throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_FAILED_EXCEPTION);
					}
				}

			} else {
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Exception in authentication user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
		return authenticationDetailsVO;
	}

	private void updateLoginParams(String userName, String currentClientVersion, String currentPlatform,String pushKey) {
		try {
			authenticationDAO.updateUserLoginParams(userName, LoginStatusEnum.ONLINE.getStatus(), currentClientVersion, currentPlatform,pushKey);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating login params for userName : " + userName + " currentClientVersion " + currentClientVersion
					+ " currentPlatform " + currentPlatform + " error " + e, e);
		}
	}
	
	private void updateLoginParams(Long id, String currentClientVersion, String currentPlatform,String pushKey) {
		try {
			authenticationDAO.updateUserLoginParams(id, LoginStatusEnum.ONLINE.getStatus(), currentClientVersion, currentPlatform,pushKey);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating login params for id : " + id + " currentClientVersion " + currentClientVersion
					+ " currentPlatform " + currentPlatform + " error " + e, e);
		}
	}

	private void updateUserInfo(long userId,UserVO userVO) {
		UserVO dbUserVO=null;
		try {
			UserInfo user = userDAO.getUserProfile(userId);
			dbUserVO = UserUtils.transformUserInfoToUserVO(user);
		    UserUtils.updateDBUserVO(dbUserVO, userVO);
			UserInfo userInfo = UserUtils.transformUserVOToUserInfo(dbUserVO);
			userDAO.updateUserInfo(userInfo);
		} catch (Exception e) {
			logger.error("Exception in updating user details in database for the user : " + userVO + " error  : " + e.getMessage());
		}
	}
	
	private void updatePushUserInfo(long userId,String pushStatus,String pushKey,String currentPlatform) {
		try {	
			if(StringUtils.isBlank(pushKey)){
				logger.error("updatePushUserInfo :pushKey= " + pushKey + " userId  : " +userId);
				return;
			}
			UserPushInfo userPushInfo = userDAO.getUserPushInfo(userId);
			if(userPushInfo != null && userPushInfo.getUserId() !=null){
				logger.debug("Update user push info userid "+userId+" , pushKey :: "+pushKey);
				userDAO.updateUserPushStatus(userId, pushStatus, pushKey, currentPlatform);	
			}else{
				UserPushInfo pushInfo = new UserPushInfo();
				pushInfo.setUserId(userId);
				pushInfo.setDeviceKey(pushKey);
				pushInfo.setNotifType(currentPlatform);
				pushInfo.setSendNotif(pushStatus);
				logger.debug("saveUserPushInfo userid "+userId+" , pushKey :: "+pushKey);
				userDAO.saveUserPushInfo(pushInfo);
			}
			
		} catch (Exception e) {
			logger.error("Exception in updating "+e.getLocalizedMessage(),e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#authenticateUser(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationDetailsVO authenticateUser(String userName, int loginMode, String partnerUserKey, String appId, String currentClientVersion,
			String currentPlatform, boolean updateLoginStats,Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		AuthenticationDetailsVO authenticationDetailsVO;
		try {
			UserThirdPartyAuth userThirdPartyAuth = null ;
			logger.debug("authenticateUser :userName : " + userName +" ,loginMode : "+loginMode+" ,partnerUserKey : "+partnerUserKey+" ,appId : "+appId+" , updateLoginStats :"+updateLoginStats);
			UserAuth userAuth = authenticationDAO.getUserAuthDetails(userName);
			if (null == userAuth){
				logger.debug("Authentication   : partnerUserKey" + partnerUserKey+ "appId ="+appId+" loginMode ="+loginMode);
				userThirdPartyAuth = authenticationDAO.getUserThirdPartyAuthDetails(partnerUserKey,appId,loginMode);
				if(null!=userThirdPartyAuth){
					userAuth = authenticationDAO.getUserAuthDetailsById(userThirdPartyAuth.getId());
				}
			}

			if (null != userAuth) {

				// CHECK STATUS
				/*if (UserStatusEnum.INACTIVE.getStatus() == userAuth.getStatus()) {
					logger.error("Exception in authentication user : " + userName + " user is inactive ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_STATUS_INACTIVE);

				} else*/ if (UserStatusEnum.BLOCKED.getStatus() == userAuth.getStatus()) {

					logger.error("Exception in authentication user : " + userName + " user is blocked ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_IS_BLOCKED);

				} else {
					if(userThirdPartyAuth==null){
						userThirdPartyAuth = authenticationDAO.getUserThirdPartyAuthDetails(userAuth.getId(), loginMode);
					}
					
					if (null != userThirdPartyAuth) {
						logger.debug("Authentication :: userThirdPartyAuth :: " + userThirdPartyAuth.getUserKey()+" , userThirdPartyAuth :: "+userThirdPartyAuth.getAppKey());
						if (userThirdPartyAuth.getUserKey().equalsIgnoreCase(partnerUserKey) && userThirdPartyAuth.getAppKey().equalsIgnoreCase(appId)) {
							authenticationDetailsVO = UserUtils.transformUserAuthToAuthenticationDetailsVO(userAuth);
							UserVO userVO = getUserVO(clientMap);
							if (updateLoginStats) {
									updateLoginParams(userAuth.getId(), currentClientVersion, currentPlatform,pushKey);
									updateUserInfo(userAuth.getId(),userVO);
									updatePushUserInfo(userAuth.getId(),"no",pushKey,currentPlatform);
							}
						} else {
							logger.error("Authentication failed for user : " + userName);
							throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_FAILED_EXCEPTION);
						}

					} else {
						// TODO SAVE USER DATA IN THE DB FOR THE REGISTERATION MODE
						authenticationDetailsVO = UserUtils.transformUserAuthToAuthenticationDetailsVO(userAuth);
						userThirdPartyAuth = new UserThirdPartyAuth(userAuth.getId(), loginMode, partnerUserKey, appId);
						authenticationDAO.saveUserThirdPartyAuthDetails(userThirdPartyAuth);
					}
				}

			} else {
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			
			}
		} catch (DataAccessFailedException | DataUpdateFailedException e) {
			logger.error("Exception in authentication user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
		return authenticationDetailsVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#logoutUser(java.lang.String)
	 */
	@Override
	public void updateUserLoginStatus(String userName, int status) throws AuthenticationServiceFailedException {
		try {
			int updateCount = authenticationDAO.updateUserLoginStatus(userName, status);
			logger.info("status update count : " + updateCount);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in changing user online status for user : " + userName + " status " + status);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION);
		}
		try{
			UserAuth userAuth = userDAO.getUserAuthDetails(userName);
			logger.info("update push status userid : " + userAuth.getId());
			userDAO.updateUserPushStatus(userAuth.getId(),"yes");
		}catch (Exception e) {
			logger.error("Exception in updateUserPushStatus "+userName,e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(String userName, String password,String oldPassword) throws AuthenticationServiceFailedException {
		try {
			UserAuth userAuth;
			try {
				userAuth = authenticationDAO.getUserAuthDetails(userName);
			} catch (DataAccessFailedException e) {
				logger.error("Exception in getUserAuthDetails : " + userName + " password " + password);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION);
			}
			if(userAuth == null){
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
			if(oldPassword == null){
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.OLD_PASSWORD_MISSING);
			}
			if(oldPassword.equalsIgnoreCase(userAuth.getPassword())){
				int updateCount = authenticationDAO.updatePassword(userName, password);
				logger.info("status update count : " + updateCount);
			}else{
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.OLD_PASSWORD_IN_CORRECT);
			}
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating user password for user : " + userName + " password " + password);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#changePassword(long, java.lang.String)
	 */
	@Override
	public void changePassword(long userId, String password) throws AuthenticationServiceFailedException {
		try {
			int updateCount = authenticationDAO.updatePassword(userId, password);
			logger.info("status update count : " + updateCount);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in updating user password for userId : " + userId + " password " + password);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#verifyEmailAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public long verifyEmailAddress(long id, String emailAddress, String verificationCode) throws AuthenticationServiceFailedException {
		try {
			UserEmailVerification userEmailVerification = authenticationDAO.getUserEmailVerification(id);
			if (null != userEmailVerification && null != userEmailVerification.getExpiryDate() && userEmailVerification.getExpiryDate().before(new Date())) {
				logger.info("Link expired. Email verification failed for id " + id + " emailAddress " + emailAddress + " verificationCode " + verificationCode);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.LINK_EXPIRED_EXCEPTION);
			}
			if (null != userEmailVerification && emailAddress.equalsIgnoreCase(userEmailVerification.getEmailId())
					&& verificationCode.equalsIgnoreCase(userEmailVerification.getVerificationCode())) {
				
				try {
					List<StationVO> stationVOList = stationDAO.getMyStationList(userEmailVerification.getUserId(),VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
						if(null!=stationVOList && stationVOList.size()>0 ){
							List<ContentVO>  contentVOList = contentDAO.getContentList(stationVOList.get(0).getStationId(), VoizdConstant.CONTENT_ACTIVE_STATUS, 100);
							
							for(ContentVO contentVO :contentVOList ){
								List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
								for(ContentMediaVO contentMediaVO:contentMediaVOList){
									if(VoizdConstant.AUDIO.equalsIgnoreCase(contentMediaVO.getMediaType())){
										contentVO.setFileIds(contentMediaVO.getFileId());
									}
								}
								try {
									tagCloudBO.createTagCloud(contentVO,1);
								} catch (TagCloudServiceException e) {
									logger.error("Error in creating tag cloud "+e.getLocalizedMessage() ,e );
									e.printStackTrace();
								}
							}
							
						}
				} catch (DataAccessFailedException e) {
					e.printStackTrace();
				}
				return userEmailVerification.getUserId();
			} else {
				logger.info("Email verification failed for id " + id + " emailAddress " + emailAddress + " verificationCode " + verificationCode);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.INVALID_EMAIL_VERIFICATION_CREDENTIALS_EXCEPTION);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Email verification failed for id " + id + " emailAddress " + emailAddress + " verificationCode " + verificationCode);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#verifyForgetPasswordCode(long, long, java.lang.String)
	 */
	@Override
	public long verifyForgetPasswordCode(long id, long userId, String verificationCode) throws AuthenticationServiceFailedException {
		try {
			ForgetPasswordVerification forgetPasswordVerification = authenticationDAO.getForgetPasswordVerification(id);
			if (null != forgetPasswordVerification.getExpiryDate() && forgetPasswordVerification.getExpiryDate().before(new Date())) {
				logger.info("Link expired. Forgot password verification failed for id " + id + " userId " + userId + " verificationCode " + verificationCode);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.LINK_EXPIRED_EXCEPTION);
			}
			if (forgetPasswordVerification.getStatus() > 0) {
				logger.info("Link already used. Forgot password verification failed for id " + id + " userId " + userId + " verificationCode "
						+ verificationCode);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.LINK_EXPIRED_EXCEPTION);
			}
			if (null != forgetPasswordVerification && (userId == forgetPasswordVerification.getUserId())
					&& verificationCode.equalsIgnoreCase(forgetPasswordVerification.getVerificationCode())) {
				updateForgetPasswordVerification(id, 1);
				return forgetPasswordVerification.getUserId();
			} else {
				logger.info("Forgot password verification failed for id " + id + " userId " + userId + " verificationCode " + verificationCode);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.INVALID_PASSWORD_VERIFICATION_CREDENTIALS_EXCEPTION);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Forgot password verification failed for id " + id + " userId " + userId + " verificationCode " + verificationCode, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
	}

	private void updateForgetPasswordVerification(long id, int status) {
		try {
			authenticationDAO.updateForgetPasswordVerification(id, status);
		} catch (DataAccessFailedException e) {
			logger.error("Exception in updating password verification link  id : " + id + " status " + status + " error " + e, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#resetPassword(java.lang.String)
	 */
	@Override
	public void resetPassword(String userName) throws AuthenticationServiceFailedException {
		try {
			UserAuth userAuth = authenticationDAO.getUserAuthDetails(userName);

			if (null != userAuth) {

				// CHECK STATUS
				/*if (UserStatusEnum.INACTIVE.getStatus() == userAuth.getStatus()) {
					logger.error("Exception in changing user password : " + userName + " user is inactive ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_STATUS_INACTIVE);

				} else */if (UserStatusEnum.BLOCKED.getStatus() == userAuth.getStatus()) {

					logger.error("Exception  in changing user password : " + userName + " user is blocked ");
					throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_IS_BLOCKED);

				} else {
					UserInfo user = userDAO.getUserProfile(userAuth.getId());
					// SEND EMAIL
					String verificaitonCode = RandomKeyGenerator.generateRandomAlphanumericKey(90);
					ForgetPasswordVerification ForgetPasswordVerification = new ForgetPasswordVerification(userAuth.getId(), verificaitonCode, new Date(), null);
					long forgetPasswordId = authenticationDAO.saveForgetPasswordVerification(ForgetPasswordVerification);
					try {

						EmailUtil.sendResetPasswordEmail(user.getFirstName(), user.getLastName(), forgetPasswordId, userAuth.getId(), userAuth.getUserName(),
								verificaitonCode);

					} catch (MailServiceFailedException e) {
						logger.error("Exception in sending change password email. Trying again ", e);
						EmailUtil.sendResetPasswordEmail(user.getFirstName(), user.getLastName(), forgetPasswordId, userAuth.getId(), userAuth.getUserName(),
								verificaitonCode);
					}
				}
			} else {
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Exception in resetPassword user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in resetPassword user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		} catch (MailServiceFailedException e) {
			logger.error("Exception in sending reset password mail user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.bo.AuthenticationBO#initiateEmailVerification(java.lang.String)
	 */
	@Override
	public void initiateEmailVerification(String userName) throws AuthenticationServiceFailedException {
		UserAuth userAuth;
		try {
			userAuth = authenticationDAO.getUserAuthDetails(userName);
			if (null == userAuth) {
				logger.error("User not found : " + userName);
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
			UserInfo user = userDAO.getUserProfile(userAuth.getId());
			if (null == user) {
				logger.error("User not found userAuth.getId() : " + userAuth.getId());
				throw new AuthenticationServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION);
			}
			String verificaitonCode = RandomKeyGenerator.generateRandomAlphanumericKey(90);
			UserEmailVerification userEmailVerification = new UserEmailVerification(userAuth.getId(), userAuth.getUserName(), verificaitonCode, 1, new Date(),
					null);
			long emailVerificationId = userDAO.saveUserEmailVerification(userEmailVerification);
			// SEND EMAIL
			try {
				EmailUtil.sendEmailVerificationEmail(user.getFirstName(), user.getLastName(), emailVerificationId, userAuth.getUserName(), verificaitonCode);
			} catch (MailServiceFailedException e) {
				logger.error("Exception in sending verification email. Trying again ", e);
				EmailUtil.sendEmailVerificationEmail(user.getFirstName(), user.getLastName(), emailVerificationId, userAuth.getUserName(), verificaitonCode);
			}
		} catch (DataAccessFailedException e) {
			logger.error("Exception in authentication user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in authentication user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		} catch (MailServiceFailedException e) {
			logger.error("Exception in authentication user : " + userName + " error " + e, e);
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
	}
	private UserVO getUserVO(Map<String, Object> clientMap) {
		 UserVO userVO = null;
		  if(clientMap != null){
			  userVO = new UserVO();	
			if(null !=  clientMap.get(ClientParamConstant.ADDREASS)){
				userVO.setLocation((String) clientMap.get(ClientParamConstant.ADDREASS));
			}
			String language = null;;
			try {
				language = countryDAO.getUserLanguage((String) clientMap.get(ClientParamConstant.LANGUAGE));
			} catch (DataAccessFailedException e) {
				logger.error("Exception in authentication getUserLanguage  : " +e.getLocalizedMessage(), e);
			}
			if(null != clientMap.get(ClientParamConstant.LANGUAGE)){
				userVO.setLanguage(language);
			}
		  }
		return userVO;
	}

	@Override
	public long getUserId(String partnerUserKey, int thirdPartyId)throws AuthenticationServiceFailedException {
		long userId = 0l;
		logger.debug("getUserId  :partnerUserKey=" +partnerUserKey+" thirdPartyId="+thirdPartyId);
		try {
			UserThirdPartyAuth userThirdPartyAuth = authenticationDAO.getUserThirdPartyAuthDetails(partnerUserKey, thirdPartyId);
			if(null!=userThirdPartyAuth){
				userId= userThirdPartyAuth.getId();
			}
		} catch (DataAccessFailedException e) {
			throw new AuthenticationServiceFailedException(ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION, e);
		}
		logger.debug("getUserId  :userId=" +userId);
		return userId;
	}
}
