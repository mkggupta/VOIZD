package com.voizd.service.authentication.manager;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.TagCloudVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.enumeration.UserStatusEnum;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.mongo.service.MongoServiceImpl;
import com.voizd.service.authentication.bo.AuthenticationBO;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.search.exception.SearchServiceException;
import com.voizd.service.search.helper.SearchUtils;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.manager.UserManager;
import com.voizd.service.station.bo.StationBO;
import com.voizd.service.station.helper.StationUtils;
import com.voizd.service.station.exception.StationServiceException;

public class AuthenticationManagerImpl implements AuthenticationManager {
	Logger logger = LoggerFactory.getLogger(AuthenticationManagerImpl.class);
	private AuthenticationBO authenticationBO;
	private UserManager userManager;
	private ContentDAO contentDAO ;
    private StationBO stationBO;
	public void setStationBO(StationBO stationBO) {
		this.stationBO = stationBO;
	}
	public void setSearchBO(SearchBO searchBO) {
		this.searchBO = searchBO;
	}

	private SearchBO searchBO;
	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}

	public StationDAO getStationDAO() {
		return stationDAO;
	}

	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}

	private StationDAO stationDAO;

	/**
	 * @return the authenticationBO
	 */
	public AuthenticationBO getAuthenticationBO() {
		return authenticationBO;
	}

	/**
	 * @param authenticationBO
	 *            the authenticationBO to set
	 */
	public void setAuthenticationBO(AuthenticationBO authenticationBO) {
		this.authenticationBO = authenticationBO;
	}

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
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#authenticateUser(java.lang.String, java.lang.String)
	 */
	public AuthenticationDetailsVO authenticateUser(String userName, String password, String currentClientVersion, String currentPlatform,
			boolean updateLoginStats, Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		AuthenticationDetailsVO authenticationDetailsVO = authenticationBO.authenticateUser(userName, password, currentClientVersion, currentPlatform,
				updateLoginStats,clientMap,pushKey);
		try {
			UserVO userVO = userManager.getUserProfile(authenticationDetailsVO.getId(), clientMap);
			if (null != userVO) {
				authenticationDetailsVO.setFirstName(userVO.getFirstName());
				authenticationDetailsVO.setLastName(userVO.getLastName());
				authenticationDetailsVO.setProfilePicUrl(userVO.getProfilePicUrl());
			}
		} catch (UserServiceFailedException e) {
			logger.error("Exception in fetching user details user status  for userId " + authenticationDetailsVO.getId() + " error " + e.getMessage(), e);
		}
		return authenticationDetailsVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#authenticateUser(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationDetailsVO authenticateUser(String userName, int loginMode, String partnerUserKey, String appId, String currentClientVersion,
			String currentPlatform, boolean updateLoginStats, Map<String, Object> clientMap,String pushKey) throws AuthenticationServiceFailedException {
		AuthenticationDetailsVO authenticationDetailsVO = authenticationBO.authenticateUser(userName, loginMode, partnerUserKey, appId, currentClientVersion,
				currentPlatform, updateLoginStats,clientMap,pushKey);
		try {
			UserVO userVO = userManager.getUserProfile(authenticationDetailsVO.getId(), clientMap);
			if (null != userVO) {
				authenticationDetailsVO.setFirstName(userVO.getFirstName());
				authenticationDetailsVO.setLastName(userVO.getLastName());
				authenticationDetailsVO.setProfilePicUrl(userVO.getProfilePicUrl());
			}
		} catch (UserServiceFailedException e) {
			logger.error("Exception in fetching user details user status  for userId " + authenticationDetailsVO.getId() + " error " + e.getMessage(), e);
		}
		return authenticationDetailsVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#logoutUser(java.lang.String)
	 */
	@Override
	public void logoutUser(String userName) throws AuthenticationServiceFailedException {
		authenticationBO.updateUserLoginStatus(userName, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(String userName, String password, String oldPassword) throws AuthenticationServiceFailedException {
		/*
		 * try { UserAuth userAuth = authenticationDAO.getUserAuthDetails(userName); if(authenticationDetailsVO == null){ throw new
		 * UserServiceFailedException(ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION); }else{ String dbPassword=authenticationDetailsVO.get } } catch
		 * (UserServiceFailedException e) { logger.error("Exception in changePassword  userName " +userName+" ,verificationCode error "+ e.getMessage(), e);
		 * throw new AuthenticationServiceFailedException(e.getErrorCode(), e); }
		 */
		authenticationBO.changePassword(userName, password, oldPassword);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#verifyEmailAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public void verifyEmailAddress(long id, String emailAddress, String verificationCode) throws AuthenticationServiceFailedException {
		long userId = authenticationBO.verifyEmailAddress(id, emailAddress, verificationCode);
		try {
			logger.debug("verifyEmailAddress in  userId "+userId);
			userManager.updateUserStatus(userId, UserStatusEnum.ACTIVE.getStatus());
		} catch (UserServiceFailedException e) {
			logger.error("Exception in updating user status  for userId " + userId + " verification id " + id + " emailAddress " + emailAddress
					+ " verificationCode " + verificationCode + " error " + e.getMessage(), e);
			throw new AuthenticationServiceFailedException(e.getErrorCode(), e);
		}
	     	UserVO userVO = null;
				try {
					userVO = userManager.getUserProfile(userId);
				} catch (UserServiceFailedException e1) {
					logger.error("Exception in getUserProfile for userId "+userId);
					throw new AuthenticationServiceFailedException(e1.getErrorCode(), e1);
				}

	try {
			StationVO stationVO = StationUtils.getDefaultStationVO(userVO);
			stationBO.createStation(stationVO);
		} catch (StationServiceException e) {
			logger.error("Error while creating default station : Please check logs " + e.getLocalizedMessage(), e);
		}


		 StationVO stationVO = null;
			try {
				stationVO = stationDAO.getUserStationInfo(userId,VoizdConstant.ACTIVE_STATION_STATUS);
			} catch (DataAccessFailedException e) {
				logger.error("Exception in getUserStationList user station  for userId "+userId);
			}	
			/* try {
				 logger.error("Userid :: "+userId+ " , stationVO.getStationId() :: "+stationVO.getStationId());
				stationDAO.updateStation(stationVO.getStationId(),userId,VoizdConstant.ACTIVE_STATION_STATUS);
			} catch (DataUpdateFailedException e) {
				logger.error("Exception in updating user station status for userId "+userId);
			}
              */
			
				
		try {
			TagCloudVO tagCloudVO = null;
			List<ContentVO> contentVOList = contentDAO.getContentList(stationVO.getStationId(), VoizdConstant.CONTENT_ACTIVE_STATUS, 100);
			for (ContentVO contentVO : contentVOList) {
				try {
					ContentSearchVO contentSearchVO = SearchUtils.transformContentSearchVO(contentVO, userVO);
					if (contentSearchVO != null) {
						searchBO.indexContentMedia(contentSearchVO, false, VoizdConstant.CONTENT);
					} else {
						logger.error("contentSearchVO is null .Please contentVo");
					}
				} catch (SearchServiceException e) {
					logger.error("Error while insert station data in search :" + e.getLocalizedMessage(), e);
				}
				try {
					if (contentVO.getContentId() != null && contentVO.getContentId()> 0) {
						
						MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
						 tagCloudVO = new TagCloudVO();
						tagCloudVO.setContentId(contentVO.getContentId());
						tagCloudVO.setCreatorId(contentVO.getCreatorId());
						tagCloudVO.setCreatedDate(DateTimeUtils.getGMTDate());
						tagCloudVO.setStationId(contentVO.getStationId());
						tagCloudVO.setLatitude(contentVO.getLatitude());
						tagCloudVO.setLongitude(contentVO.getLongitude());
						tagCloudVO.setTagCloud(contentVO.getTagCloud());
						tagCloudVO.setCountry(contentVO.getCountry());
						tagCloudVO.setLanguage(contentVO.getLanguage());
					    mongoServiceImpl.insertTagDetail(tagCloudVO);
					}
				} catch (Exception e) {
					logger.error("Exception while insert tag detail : " + e.getLocalizedMessage(), e);
				}
			}
		} catch (DataAccessFailedException e) {
			logger.error("Exception in get user content status for userId " + userId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#resetPassword(java.lang.String)
	 */
	@Override
	public void resetPassword(String userName) throws AuthenticationServiceFailedException {
		authenticationBO.resetPassword(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#changePassword(long, long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(long userId, long forgotPasswordId, String verificationCode, String userName, String password)
			throws AuthenticationServiceFailedException {
		authenticationBO.verifyForgetPasswordCode(forgotPasswordId, userId, verificationCode);
		authenticationBO.changePassword(userId, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.authentication.manager.AuthenticationManager#initiateEmailVerification(java.lang.String)
	 */
	@Override
	public void initiateEmailVerification(String userName) throws AuthenticationServiceFailedException {
		authenticationBO.initiateEmailVerification(userName);
	}

}
