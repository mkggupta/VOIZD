/**
 * 
 */
package com.voizd.service.user.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.dto.VoizerDTO;
import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;
import com.voizd.service.authentication.bo.AuthenticationBO;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.station.bo.StationBO;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.helper.StationUtils;
import com.voizd.service.user.bo.UserBO;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserManagerHelper;
import com.voizd.service.user.helper.UserUtils;


/**
 * @author Manish
 * 
 */
public class UserManagerImpl implements UserManager {

	private UserBO userBO;
	private MediaService mediaService;
	private  MediaDAO mediaDAO ;
	private StationBO stationBO;
	private StationSubDAO  stationSubDAO ;
	private AuthenticationBO authenticationBO;
	private JmsSender jmsSender;
	
	public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}

	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}

	public void setStationBO(StationBO stationBO) {
		this.stationBO = stationBO;
	}
	
	public void setAuthenticationBO(AuthenticationBO authenticationBO) {
		this.authenticationBO = authenticationBO;
	}


	Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

	/**
	 * @return the userBO
	 */
	public UserBO getUserBO() {
		return userBO;
	}

	/**
	 * @param userBO
	 *            the userBO to set
	 */
	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	/**
	 * @return the mediaService
	 */
	public MediaService getMediaService() {
		return mediaService;
	}

	/**
	 * @param mediaService
	 *            the mediaService to set
	 */
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.manager.UserManager#getUserProfile(java.lang.String)
	 */
	public UserVO getUserProfile(long userId, Map<String, Object> clientMap) throws UserServiceFailedException {
		UserVO userVO = userBO.getUserProfile(userId);
		String imageResolution = null;
		if (clientMap != null && null != clientMap.get(ClientParamConstant.IMAGESIZE)) {
			imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		} else {
			imageResolution = MediaUtil.getImageSize(null);
		}
		if (null != userVO.getProfilePicFileId()) {
			mediaService.convertMedia(VoizdConstant.IMAGE, userVO.getProfilePicFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
			userVO.setProfilePicFileExt( VoizdConstant.IMAGE_JPG);
		}else{
			
			 try {
				 DefaultMediaVO defaultMediaVO = null;
					if (userVO.getGender() == 1) {
						defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
					} else {
						defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
					}
				 if(null!=defaultMediaVO){
					    mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
					    userVO.setProfilePicFileId(defaultMediaVO.getFileId());
					    userVO.setProfilePicFileExt(defaultMediaVO.getExt());
				  }
			} catch (DataAccessFailedException e) {
				logger.error("Exception in getting profile information for the user : " + userId + " so ignoring extention information " + " error  : "
						+ e.getMessage());
			}
	
		 }
		UserManagerHelper.enrichUser(userVO, imageResolution);
		return userVO;
	}

	public UserVO getUserProfile(long userId) throws UserServiceFailedException {
		UserVO userVO = userBO.getUserProfile(userId);
		return userVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.manager.UserManager#registerUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO registerUser(RegistrationVO registerationVO, Map<String, Object> clientMap) throws UserServiceFailedException {
		
		String imageResolution = null;
		if (clientMap != null && null != clientMap.get(ClientParamConstant.IMAGESIZE)) {
			imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		} else {
			imageResolution = MediaUtil.getImageSize(null);
		}
		if (null != registerationVO.getProfilePicFileId()) {
			
			MediaVO mediaVO;
			try {
				mediaVO = mediaService.getMediaInfo(registerationVO.getProfilePicFileId());
				if (null != mediaVO) {
					registerationVO.setProfilePicFileExt(VoizdConstant.IMAGE_JPG);
				}
				mediaService.convertMedia(VoizdConstant.IMAGE, registerationVO.getProfilePicFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
				UserManagerHelper.enrichUser(registerationVO, imageResolution);
			} catch (MediaServiceFailedException e) {
				// SUPRESSING ERROR AND GOING AHEAD WITH REGISTRATION
				logger.error("Exception in getting media information for the user : " + registerationVO + " so ignoring extention information " + " error  : "
						+ e.getMessage());
			}
		} else {
			try {
				DefaultMediaVO defaultMediaVO = null;
				if (registerationVO.getGender() == 1) {
					defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
				} else {
					defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
				}

				if (defaultMediaVO != null && defaultMediaVO.getFileId() != null && defaultMediaVO.getFileId().trim().length() > 0) {
					registerationVO.setProfilePicFileId(defaultMediaVO.getFileId());
					registerationVO.setProfilePicFileExt(defaultMediaVO.getExt());
				
					mediaService.convertMedia(VoizdConstant.IMAGE, registerationVO.getProfilePicFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
					UserManagerHelper.enrichUser(registerationVO, imageResolution);
				}
			} catch (DataAccessFailedException e) {
				// SUPRESSING ERROR AND GOING AHEAD WITH REGISTRATION
				logger.error("Exception in getting media information for the user : " + registerationVO + " so ignoring extention information " + " error  : "
						+ e.getMessage());
			}
		}
		UserVO userVO = userBO.registerUser(registerationVO);
		userVO.setStatus(1);
		if (userVO != null && userVO.getId() > 0) {
			try {
				userBO.createUserCounter(userVO.getId());
			} catch (UserServiceFailedException e1) {
				logger.error("Exception in getting media information for the user : " + userVO.getId() + " so ignoring extention information " + " error  : "
						+ e1.getMessage());
			}
			
		/*try {
				StationVO stationVO = StationUtils.getDefaultStationVO(userVO);
				stationBO.createStation(stationVO);
			} catch (StationServiceException e) {
				logger.error("Error while creating default station : Please check logs " + e.getLocalizedMessage(), e);
			} */

		}
		return userVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.manager.UserManager#updateUser(com.voizd.common.beans.vo.UserVO)
	 */
	public UserVO updateUser(UserVO userVO,Map<String, Object> clientMap) throws UserServiceFailedException {
		String imageResolution = null;
		long start = System.currentTimeMillis();
		if (clientMap != null && null != clientMap.get(ClientParamConstant.IMAGESIZE)) {
			imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		} else {
			imageResolution = MediaUtil.getImageSize(null);
		}
		if (null != userVO.getProfilePicFileId()) {
			
				String fileIdArr[] = userVO.getProfilePicFileId().split("\\s*,\\s*");
				for (String fileId:fileIdArr){
					userVO.setProfilePicFileId(fileId);
				}
			
			MediaVO mediaVO;
			try {
				mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
				if (null != mediaVO) {
					userVO.setProfilePicFileExt(mediaVO.getMimeType());
				}
			} catch (MediaServiceFailedException e) {
				// SUPRESSING ERROR AND GOING AHEAD WITH REGISTRATION
				logger.error("Exception in getting media information for the user : " + userVO + " so ignoring extention information " + " error  : "
						+ e.getMessage());
			}
			logger.info("Time taken updateUser.mediaVO: " + (System.currentTimeMillis() - start)
					+ " msec");
			sendJmsMessage(userVO.getId(),userVO.getProfilePicFileId());
			logger.info("Time taken updateUser.sendJmsMessage: " + (System.currentTimeMillis() - start)
					+ " msec");
		}
	
		userVO = userBO.updateUser(userVO);
		logger.debug("updateUser--"+userVO.getProfilePicFileId());
		logger.info("Time taken updateUser.userVO: " + (System.currentTimeMillis() - start)
				+ " msec");
		if (null != userVO.getProfilePicFileId()) {
			mediaService.convertMedia(VoizdConstant.IMAGE, userVO.getProfilePicFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
			userVO.setProfilePicFileExt( VoizdConstant.IMAGE_JPG);
			UserManagerHelper.enrichUser(userVO, imageResolution);
		}
		logger.debug("updateUser-userVO-"+userVO);
		logger.info("Time taken updateUser.enrichUser: " + (System.currentTimeMillis() - start)
				+ " msec");
		return userVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.manager.UserManager#getUserAuthDetails(java.lang.String)
	 */
	public AuthenticationDetailsVO getUserAuthDetails(String userName) throws UserServiceFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.service.user.manager.UserManager#updateUserStatus(long, java.lang.String)
	 */
	@Override
	public void updateUserStatus(long userId, int userStatus) throws UserServiceFailedException {
		userBO.updateUserStatus(userId, userStatus);
	}

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	@Override
	public UserDTO getUserPublicProfile(long userId, long visitorId, Map<String, Object> clientMap) throws UserServiceFailedException {

		UserDTO userDTO = new UserDTO();
		UserVO userVO = getUserProfile(userId,clientMap);
		 StringBuilder address = new StringBuilder();
		 if(StringUtils.isNotBlank(userVO.getCity())){
			 address.append(userVO.getCity()).append(VoizdConstant.ADRESS_SEPERATOR);
		 }
		 if(StringUtils.isNotBlank(userVO.getState())){
			 address.append(userVO.getState()).append(VoizdConstant.ADRESS_SEPERATOR);
		 }
		 if(StringUtils.isNotBlank(userVO.getCountry())){
			 address.append(userVO.getCountry());
		 }
		 if(StringUtils.isNotBlank(address.toString())){
			 userVO.setAddress(address.toString());
		 }
		userDTO = UserUtils.transformUserVOToUserDTO(userVO);
		try {
			userDTO.setPrimaryEmailAddress("");
			List<StationVO>  stationVO = stationBO.getMyStationList(userId, VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
			if(null!=stationVO && stationVO.size()>0 ){
				Long stationId= stationVO.get(0).getStationId();
				StationCounterVO stationCounterVO = stationBO.getStationCounter(stationId);
				if(null!=stationCounterVO){
					Long contentCount = 0l;
					if(stationCounterVO.getNumberOfContent()>0){
						contentCount =stationCounterVO.getNumberOfContent();
					}
					UserCounterVO userCounterVO = stationBO.getUserCounter(userId);
					logger.debug("userCounterVO-getUserPublicProfile-"+userCounterVO+userCounterVO.getAmplified());
					if(null!=userCounterVO && userCounterVO.getAmplified()>0){
						contentCount +=userCounterVO.getAmplified();
					}
					
					if(contentCount>0){
						userDTO.setContCnt(contentCount);
						StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MY_CONTENT_URL);
						if(visitorId>0){
							relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.VISITORID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
						}
						userDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}
					if(stationCounterVO.getFollower()>0){
						userDTO.setFollowerCnt(stationCounterVO.getFollower());
						StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_STATION_FOLLOWER_URL);
						relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
						relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.CREATORID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.get(0).getCreatorId());
						userDTO.setFollowerUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}
					//UserCounterVO userCounterVO = stationBO.getUserCounter(userId);
					if(null!=userCounterVO){
						userDTO.setIFollowCnt(userCounterVO.getFollow());
						StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_FOLLOW_URL);
						relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
						userDTO.setIFollowUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}
						/*	userDTO.setBookMarkCnt(userCounterVO.getLikes());
						if(userId==visitorId){
							userDTO.setBookMarkUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.USER_BOOKMARK_URL));
						}
					}*/
					if(stationVO.get(0).getCreatorId()!=visitorId){	
						if(visitorId>0){
							
							 Byte subStatus = stationSubDAO.getStationTap(stationId,visitorId);
							 if(null!=subStatus){
								if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
									userDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
								}else{
									userDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
								}
							 }else{
								 userDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS); 
							 }
							
						}else{
							userDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
							
						}
						logger.debug("getFollowerList getTapValue="+userDTO.getTapValue());
						userDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
					}
				}
				
			}
		} catch (DataAccessFailedException | StationServiceException e) {
			logger.error("Exception in getUserPublicProfile information for the user : " + userId + " visitorId "+visitorId + " error  : "
					+ e.getMessage());
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		return userDTO;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDTO getUserPrivateProfile(long userId,Map<String, Object> clientMap) throws UserServiceFailedException {
		UserDTO userDTO = new UserDTO();
		UserVO userVO = getUserProfile(userId,clientMap);
		 StringBuilder address = new StringBuilder();
		 if(StringUtils.isNotBlank(userVO.getCity())){
			 address.append(userVO.getCity()).append(VoizdConstant.ADRESS_SEPERATOR);
		 }
		 if(StringUtils.isNotBlank(userVO.getState())){
			 address.append(userVO.getState()).append(VoizdConstant.ADRESS_SEPERATOR);
		 }
		 if(StringUtils.isNotBlank(userVO.getCountry())){
			 address.append(userVO.getCountry());
		 }
		 if(StringUtils.isNotBlank(address.toString())){
			 userVO.setAddress(address.toString());
		 }
		userVO.setProfileUpdateUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.USER_UPDATE_PROFILE_URL));
		userDTO = UserUtils.transformUserVOToUserDTO(userVO);
		userDTO.setPrimaryEmailAddress(userVO.getPrimaryEmailAddress());
		try {
			List<StationVO>  stationVO = stationBO.getMyStationList(userId, VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
			if(null!=stationVO && stationVO.size()>0 ){
				Long stationId= stationVO.get(0).getStationId();
				StationCounterVO stationCounterVO = stationBO.getStationCounter(stationId);
				if(null!=stationCounterVO){
					Long contentCount = 0l;
					if(stationCounterVO.getNumberOfContent()>0){
						contentCount =stationCounterVO.getNumberOfContent();
					}
					UserCounterVO userCounterVO = stationBO.getUserCounter(userId);
					logger.debug("userCounterVO--"+userCounterVO +userCounterVO.getAmplified());
					if(null!=userCounterVO && userCounterVO.getAmplified()>0){
						contentCount +=userCounterVO.getAmplified();
					}
					logger.debug("contentCount--"+contentCount);
					if(contentCount>0){
						userDTO.setContCnt(contentCount);
						userDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.MY_CONTENT_URL));
					}
					if(stationCounterVO.getFollower()>0){
						userDTO.setFollowerCnt(stationCounterVO.getFollower());
						StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_STATION_FOLLOWER_URL);
						relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
						relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.CREATORID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.get(0).getCreatorId());
						userDTO.setFollowerUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}
				
					if(null!=userCounterVO){
						userDTO.setIFollowCnt(userCounterVO.getFollow());
						StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_FOLLOW_URL);
						relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
						userDTO.setIFollowUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						userDTO.setBookMarkCnt(userCounterVO.getLikes());
						userDTO.setBookMarkUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.USER_BOOKMARK_URL));
					}
				}
			/*	HashMap<String, Object>  topVoizerMap= stationBO.getTopVoizerList(userId,0,10,false,clientMap);
				if(null!=topVoizerMap && topVoizerMap.size()>0){
					List<VoizerDTO> voizerDtoList  = (List<VoizerDTO>) topVoizerMap.get(VoizdConstant.TOPVOIZER);
					userDTO.setTopVoizer(voizerDtoList);
				}*/
				
			}
		} catch (StationServiceException e) {
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		return userDTO;
	}
	
	@Override
	public UserDTO getFriendPublicProfile(String friendId,int thirdPartyId,long visitorId,Map<String, Object> clientMap) throws UserServiceFailedException{
		UserDTO userDTO = new UserDTO();
		try {
			long userId = authenticationBO.getUserId(friendId, thirdPartyId);
			logger.debug("getFriendPublicProfile.userId=" + userId);
			if(userId>0){
				userDTO = getUserPublicProfile(userId,visitorId,clientMap);
			}
		} catch (AuthenticationServiceFailedException e) {
			throw new UserServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		return userDTO;
	}
	
	private void sendJmsMessage(Long creatorId,String fileId){
		try {
			TapClipVO tapClipVO= new TapClipVO();
			tapClipVO.setCreatorId(creatorId);
			tapClipVO.setUserFileId(fileId);
			
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("content", tapClipVO);
			msgMap.put("command","updateprofile");
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:tapvoizd");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void updateUserPushStatus(long userId, String pushStatus)
			throws UserServiceFailedException {
		userBO.updateUserPushStatus(userId, pushStatus);
	}

}
