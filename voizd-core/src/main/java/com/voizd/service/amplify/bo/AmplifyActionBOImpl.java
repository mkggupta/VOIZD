package com.voizd.service.amplify.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.AmplifierDTO;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.content.ContentLikeDAO;
import com.voizd.dao.modules.content.ContentSubDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationLikeDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.stream.UserStreamCounterDAO;
import com.voizd.dao.modules.stream.UserStreamDAO;
import com.voizd.dao.modules.stream.UserStreamLikeDAO;
import com.voizd.dao.modules.user.UserCounterDAO;
import com.voizd.dao.modules.user.UserDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;
import com.voizd.service.amplify.exception.AmplifyServiceFailedException;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.stream.v1_0.StreamService;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;
import com.voizd.util.PushMessageUtil;

public class AmplifyActionBOImpl implements AmplifyActionBO {
	private static Logger logger = LoggerFactory.getLogger(AmplifyActionBOImpl.class);
	private StationSubDAO stationSubDAO ;
	private StationCounterDAO stationCounterDAO ;
	private StationDAO  stationDAO;
	private StationLikeDAO stationLikeDAO;
	private ContentSubDAO contentSubDAO;
	private ContentDAO  contentDAO;
	private ContentLikeDAO contentLikeDAO;
	private ContentCounterDAO contentCounterDAO ;
	private UserStreamDAO userStreamDAO ;
	private StreamService streamService ;
	private UserStreamCounterDAO  userStreamCounterDAO;
	private UserStreamLikeDAO userStreamLikeDAO ;
	private UserCounterDAO  userCounterDAO;
	private UserDAO userDAO ;
	private AmplifyDAO amplifyDAO ;
	private UserService userService ;
	private MediaDAO mediaDAO ;
	private MediaService  mediaService;
	
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAmplifyDAO(AmplifyDAO amplifyDAO) {
		this.amplifyDAO = amplifyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}


	private JmsSender jmsSender;
	
    public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}

	public void setUserCounterDAO(UserCounterDAO userCounterDAO) {
		this.userCounterDAO = userCounterDAO;
	}

	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}


	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}


	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}


	public void setStationLikeDAO(StationLikeDAO stationLikeDAO) {
		this.stationLikeDAO = stationLikeDAO;
	}


	public void setContentSubDAO(ContentSubDAO contentSubDAO) {
		this.contentSubDAO = contentSubDAO;
	}


	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}


	public void setContentLikeDAO(ContentLikeDAO contentLikeDAO) {
		this.contentLikeDAO = contentLikeDAO;
	}


	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}
	
	public void setStreamService(StreamService streamService) {
		this.streamService = streamService;
	}


	public void setUserStreamDAO(UserStreamDAO userStreamDAO) {
		this.userStreamDAO = userStreamDAO;
	}


	public void setUserStreamCounterDAO(UserStreamCounterDAO userStreamCounterDAO) {
		this.userStreamCounterDAO = userStreamCounterDAO;
	}


	public void setUserStreamLikeDAO(UserStreamLikeDAO userStreamLikeDAO) {
		this.userStreamLikeDAO = userStreamLikeDAO;
	}
	

	
	public void ampifyContent(Long contentId, Long creatorId, Byte status,Long userId)throws AmplifyServiceFailedException{
		 try {
			 if(creatorId<=0 || userId<=0 || contentId<=0 ){
					logger.debug("ampifyContent creatorId = "+creatorId +" userId = "+userId);
					throw new AmplifyServiceFailedException(ErrorCodesEnum.USER_ACTION_DATA_NOT_FOUND_EXCEPTION);
				}
			 	boolean counterUpdate = false ;
				ContentVO contentVO = contentDAO.getContentById(contentId, VoizdConstant.CONTENT_ACTIVE_STATUS);
				logger.debug(""+contentVO+"  status= "+status);
				if(null!=contentVO){
					 AmplifyInfoVO amplifyInfoVO =amplifyDAO.getAmplifyInfo(contentId,userId);
					 if(amplifyInfoVO==null){
						 logger.debug("createAmplifyInfo"+status);
						 if(VoizdConstant.ACTIVE_AMPLIFY_STATUS.equals(status)){
							 amplifyInfoVO = new AmplifyInfoVO();
							 amplifyInfoVO.setContentId(contentId);
							 amplifyInfoVO.setCreatorId(creatorId);
							 amplifyInfoVO.setStatus(VoizdConstant.ACTIVE_AMPLIFY_STATUS);
							 amplifyInfoVO.setAmplifierId(userId);
							 amplifyDAO.createAmplifyInfo(amplifyInfoVO);
							 amplifyInfoVO.setUserId(userId);
							 counterUpdate = true ;
						 }else{
							 logger.debug("else createAmplifyInfo status="+status);
						 }
					 }else{
						 	if(!status.equals(amplifyInfoVO.getStatus())){
							 	 amplifyInfoVO.setStatus(status);
								 amplifyDAO.updateAmplifyInfo(amplifyInfoVO);
								 logger.debug("updateAmplifyInfo"+amplifyInfoVO +"contentId="+contentId+" userId="+userId);
								 counterUpdate = true ;
						 	}else{
								 logger.debug("else updateAmplifyInfo status="+status);
							 }
					 }
					 	if(counterUpdate){
							ContentCounterVO contentCounterVO =contentCounterDAO.getContentCounter(contentId);
							if(null!=contentCounterVO){
								if(VoizdConstant.ACTIVE_AMPLIFY_STATUS==status){
									contentCounterVO.setAmplify(contentCounterVO.getAmplify()+1);
								}else{
									if(contentCounterVO.getAmplify()>0){
										contentCounterVO.setAmplify(contentCounterVO.getAmplify()-1);
									}else{
										counterUpdate= false ;
									}
								}
								contentCounterDAO.updateContentCounter(contentCounterVO);
							}else{
								logger.error("no counter for amplyfy for contentId="+contentId);
							}
							
							UserCounterVO userCounterVO = userCounterDAO.getUserCounter(userId);
							if(null!=userCounterVO){
								if(VoizdConstant.ACTIVE_AMPLIFY_STATUS==status){
									userCounterVO.setAmplified(userCounterVO.getAmplified()+1);
								}else{
									if(userCounterVO.getAmplified()>0){
										userCounterVO.setAmplified(userCounterVO.getAmplified()-1);
									}else{
										logger.debug("no amplified counter for user to decrement for userId="+userId);
									}
								}
								userCounterDAO.updateUserCounter(userCounterVO);
							}else{
									logger.error("no setAmplified counter for user for userId="+userId);
							}
							
							if(counterUpdate){
								 amplifyInfoVO.setCreatedDate(contentVO.getCreatedDate());
								 if(VoizdConstant.ACTIVE_AMPLIFY_STATUS==status){
									 sendJmsMessage(VoizdConstant.AMPLIFY_COMMAND,amplifyInfoVO);
									 	logger.debug("ampifyContent :: contentId :: "+contentVO.getContentId()+" , CreatorId :: "+contentVO.getCreatorId()+" userId="+userId);
									 	if(!contentVO.getCreatorId().equals(userId)){
											UserPushInfo pushInfo = userDAO.getUserPushInfo(contentVO.getCreatorId());
											if(pushInfo != null && "yes".equalsIgnoreCase(pushInfo.getSendNotif())){
												logger.debug("ampifyContent :: userId :: "+userId);
												UserInfo userInfo = userDAO.getUserProfile(userId);
												String message = PushMessageUtil.getPushMessage(contentVO.getTitle(),"amplified",userInfo.getFirstName(),userInfo.getLastName());
												sendJmsPushMessage(userId,pushInfo.getDeviceKey(),message,pushInfo.getNotifType());
											}
									 	}else{
									 		logger.debug("ampifyContent ::, CreatorId :: "+contentVO.getCreatorId()+" userId="+userId);
									 	}
								 }else{
									 sendJmsMessage(VoizdConstant.DEAMPLIFY_COMMAND,amplifyInfoVO); 
								 }
								 logger.debug("sendJmsMessage--");
							}else{
								 logger.debug("not avesendJmsMessage--");
							}
					 	}else{
					 		 logger.debug(" counterUpdate--"+counterUpdate);
					 	}
					
				}else{
					throw new AmplifyServiceFailedException(ErrorCodesEnum.STATION_CONTENT_DELETE_EXCEPTION);
				}
		 }catch (Exception e) {
				logger.error("ampifyContent " + e.getLocalizedMessage(), e);
				throw new AmplifyServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
		}
	}
	
	public HashMap<String, Object> getAmplifierList(Long contentId,Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws AmplifyServiceFailedException  {
		List<AmplifierDTO> amplifierDtoList =new ArrayList<AmplifierDTO>();
		List<AmplifyInfoVO> amplifierCounterVOList = null;
		HashMap<String, Object> amplifierMap = new HashMap<String, Object>();
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
		int resultSize =0 ;
		 String imageResolution = null;
		 String amplifier = VoizdConstant.AMPLIFIER ;
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		try {
				//true is asc order
				if(order){
					amplifierCounterVOList = amplifyDAO.getAmplifierList(contentId,startLimit,endLimit+1);
					 hasNext = true ;
				}else{
					if(startLimit==0){
						//first hit
						 isFirstRequest = true;
					}else{
						//desc order	
						 hasPre = true ;
					}
					amplifierCounterVOList =amplifyDAO.getAmplifierList(contentId,startLimit,endLimit+1);
				}
				logger.debug("getamplifierList amplifierCounterVOList"+amplifierCounterVOList+" order="+order);
				if(null!= amplifierCounterVOList){
					if(endLimit<amplifierCounterVOList.size()){
						if(order){
							amplifierCounterVOList.remove(0);
							 hasPre = true ;
						}else{
							amplifierCounterVOList.remove(amplifierCounterVOList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = amplifierCounterVOList.size();
				}
				 int cnt =0 ;
				 for(AmplifyInfoVO amplifyInfoVO:amplifierCounterVOList){
					 AmplifierDTO amplifierDTO = new AmplifierDTO();
				
						amplifierDTO.setAmplifierId(amplifyInfoVO.getAmplifierId());
						amplifierDTO.setAmplifyDate(amplifyInfoVO.getAmplifyDate());
						UserVO userVO = userService.getUserProfile(amplifyInfoVO.getAmplifierId());
					 logger.debug("getFollowerList userVO="+userVO +" "+userVO.getStatus());
					 if(null!= userVO){
						 amplifierDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
						 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(amplifyInfoVO.getAmplifierId());
						 amplifierDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						 if (null != userVO.getProfilePicFileId()) {
							 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
							 if(null!=mediaVO){
								 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 amplifierDTO.setImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							 }
						 }else{
								DefaultMediaVO defaultMediaVO = null;
								if (userVO.getGender() == 1) {
									defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
								} else {
									defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
								}
								if(null!=defaultMediaVO){
									 mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
									 amplifierDTO.setImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
						 }
						 
						 amplifierDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
						 amplifierDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
						 if(StringUtils.isNotBlank(userVO.getLanguage())){
							 amplifierDTO.setLanguage(userVO.getLanguage());
						 }
						 logger.debug("getFollowerList userVO=userVO.getLanguage()"+userVO.getLanguage());
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
							 amplifierDTO.setAddress(address.toString());
						 }

						 StationVO stationVO = stationDAO.getUserStationInfo(amplifyInfoVO.getAmplifierId(),VoizdConstant.STATION_ACTIVE_STATUS);
						if(null!=stationVO){
							StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationVO.getStationId());
							if(null!=stationCounterVO){
								Long contentCount = 0l;
								if(stationCounterVO.getNumberOfContent()>0){
									contentCount =stationCounterVO.getNumberOfContent();
								}
								UserCounterVO userCounterVO = userCounterDAO.getUserCounter(userId);
								logger.debug("userCounterVO--"+userCounterVO +userCounterVO.getAmplified());
								if(null!=userCounterVO && userCounterVO.getAmplified()>0){
									contentCount +=userCounterVO.getAmplified();
								}
								logger.debug("contentCount--"+contentCount);
								 amplifierDTO.setContCnt(contentCount);
								 amplifierDTO.setFollowerCnt(stationCounterVO.getFollower());
							}
							 logger.debug("getFollowerList getCreatorId="+stationVO.getCreatorId()+",userId="+userId);
							  if(stationVO.getCreatorId()!=userId){	
									if(userId>0){
										 Byte subStatus = stationSubDAO.getStationTap(stationVO.getStationId(),userId);
										 if(null!=subStatus){
											if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
												amplifierDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
											}else{
												amplifierDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
											}
										 }else{
											 amplifierDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS); 
										 }
									}else{
										amplifierDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
										
									}
									logger.debug("getFollowerList getTapValue="+amplifierDTO.getTapValue());
									amplifierDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
							}
						}
								amplifierDtoList.add(amplifierDTO);
					 }
						cnt++;	
					
			 }
			} catch (DataAccessFailedException e) {
				logger.error(e.getLocalizedMessage(),e);
				throw new AmplifyServiceFailedException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			} catch (MediaServiceFailedException e) {
				logger.error(e.getLocalizedMessage(),e);
				throw new AmplifyServiceFailedException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				logger.error(e.getLocalizedMessage(),e);
				throw new AmplifyServiceFailedException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.AMPLIFIER_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				amplifierMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.AMPLIFIER_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentId);
				if(resultSize<endLimit){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
				}else{
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				amplifierMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
			if(null!=amplifierDtoList && amplifierDtoList.size()>0){
				amplifierMap.put(amplifier, amplifierDtoList);
			}
		 
		return amplifierMap;
	}
	

	
	private void sendJmsMessage(String command,AmplifyInfoVO amplifyInfoVO){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put(VoizdConstant.AMPLIFY_KEY, amplifyInfoVO);
			msgMap.put(VoizdConstant.COMMAND_KEY,command);
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:amplifyvoizd");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}

	private void sendJmsPushMessage(Long userId,String pushKey,String message,String platform){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("deviceKey",pushKey);
			msgMap.put("pushMessage",message);
			msgMap.put("userId", userId);
			msgMap.put("platform", platform);
			msgMap.put("amplify","yes");
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:push");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}

}
