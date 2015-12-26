package com.voizd.service.user.action.bo;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentLikeVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationLikeVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.StreamCounterVO;
import com.voizd.common.beans.vo.StreamLikeVO;
import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.content.ContentLikeDAO;
import com.voizd.dao.modules.content.ContentSubDAO;
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
import com.voizd.service.stream.exception.StreamServiceException;
import com.voizd.service.stream.helper.StreamUtils;
import com.voizd.service.stream.v1_0.StreamService;
import com.voizd.service.user.action.exception.UserActionServiceFailedException;
import com.voizd.util.JmsUtil;
import com.voizd.util.PushMessageUtil;

public class UserActionBOImpl implements UserActionBO {
	private static Logger logger = LoggerFactory.getLogger(UserActionBOImpl.class);
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
	private JmsSender jmsSender;
	private JmsUtil jmsUtil;
	

	public void setJmsUtil(JmsUtil jmsUtil) {
		this.jmsUtil = jmsUtil;
	}




	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}


	
	
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
	
	
	public void tapStation(Long stationId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException{
		
	}
	
	public void tapStation(Long stationId,Long streamId,Long follwerId,Long follweeId,Byte status) throws UserActionServiceFailedException{
		
		try {
			
			if(follwerId<=0 || stationId<=0){
				logger.error("tapStation stationId"+stationId +" follwerId is not found "+follwerId);
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_DATA_NOT_FOUND_EXCEPTION);
			}
			
			StationVO stationVO  = stationDAO.getActiveStationInfo(stationId) ;
			logger.debug("stationVO---"+stationVO);
			if(null!=stationVO){
				if(stationVO.getCreatorId()!=follwerId){
					/*StreamVO streamVO = null ;
					if(streamId==0){
						List<StreamVO> streamVOList = userStreamDAO.getUserStreamList(follwerId);
						if(null!=streamVOList && streamVOList.size()>0){
							streamVO = streamVOList.get(0);
						}
					}else{
						streamVO = userStreamDAO.getStreamInfoByUserId(follwerId, streamId);
					}
					logger.debug("streamVO---"+streamVO);
					if(null==streamVO){
						try {
							streamVO=StreamUtils.getDefaultStreamVO(follwerId);
							streamId = streamService.createStream(streamVO);
							streamVO.setStreamId(streamId);
						} catch (StreamServiceException e) {
							logger.error("could not create default stream "+e.getLocalizedMessage(), e);
							throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
						}
					}else{
						streamId = streamVO.getStreamId();
						logger.debug("streamId-----"+streamId);
					}*/
					Byte subStatus = stationSubDAO.getStationTap(stationId,streamId,follwerId,follweeId);
					logger.debug("subStatus---"+subStatus);
					if(null==subStatus){
						stationSubDAO.saveStationTap(stationId, streamId,follwerId,follweeId);
						StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationId);
						if(null!=stationCounterVO){
							stationCounterVO.setFollower(stationCounterVO.getFollower()+1);
							stationCounterDAO.updateStationCounter(stationCounterVO);
							UserCounterVO userCounterVO = userCounterDAO.getUserCounter(follwerId);
							if(null!=userCounterVO){
								userCounterVO.setFollow(userCounterVO.getFollow()+1);
								userCounterDAO.updateUserCounter(userCounterVO);
							}else{
									logger.error("no counter for user for userId="+follwerId);
							}
							
						}else{
							logger.error("no counter for tapStation for stationId="+stationId);
						}
						/*StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(streamId);
						if(null!=streamCounterVO){
							streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()+1);
							userStreamCounterDAO.updateStreamCounter(streamCounterVO);
							if(streamCounterVO.getNumberOfContent()==1 && streamVO.getStatus()==VoizdConstant.DEACTIVE_STREAM_STATUS){
								userStreamDAO.updateStreamStatus(follwerId, streamId, VoizdConstant.ACTIVE_STREAM_STATUS, true);
							}
						}else{
							logger.error("no counter for stream for streamId="+streamId);
						}*/
						
					}else{
						if(subStatus.equals(status)){
							//do nothing
						}else{
							sendJmsMessage(stationId,follwerId,follweeId);
							logger.debug("stationCounterDAO.stationId="+stationId+" follwerId="+follwerId+" status="+status);
							stationSubDAO.updateStationTap(stationId,streamId, follwerId, status,follweeId);
							logger.debug("stationCounterDAO.updateStationTap=");
							StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationId);
							if(null!=stationCounterVO){
								UserCounterVO userCounterVO = userCounterDAO.getUserCounter(follwerId);
								if(VoizdConstant.STATION_FOLLOW_STATUS.equals(status)){
									stationCounterVO.setFollower(stationCounterVO.getFollower()+1);
									userCounterVO.setFollow(userCounterVO.getFollow()+1);
								}else{
									stationCounterVO.setFollower(stationCounterVO.getFollower()-1);
									userCounterVO.setFollow(userCounterVO.getFollow()-1);
								}
								if(stationCounterVO.getFollower()>=0){
									stationCounterDAO.updateStationCounter(stationCounterVO);
									userCounterDAO.updateUserCounter(userCounterVO);
								}
							}else{
								logger.error("no counter for tapStation for stationId="+stationId);
							}
							
						/*	StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(streamId);
							if(null!=streamCounterVO){
								if(VoizdConstant.STATION_FOLLOW_STATUS.equals(status)){
									streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()+1);
								}else{
									streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()-1);
								}
								if(streamCounterVO.getNumberOfContent()>=0){
									userStreamCounterDAO.updateStreamCounter(streamCounterVO);
								}
								if(streamCounterVO.getNumberOfContent()==0){
									userStreamDAO.updateStreamStatus(follwerId, streamId, VoizdConstant.DEACTIVE_STREAM_STATUS, false);
								}
							}else{
								logger.error("no counter for stream for streamId="+streamId);
							}*/
							
							
						}
					}
				}else{
					logger.info("you are trying to follow your station stationId="+stationId+" follwerId="+follwerId);
				}
			}else{
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
			}
		} catch (DataUpdateFailedException e) {
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_UPDATE_SERVICE_FAILED_EXCEPTION);
		} catch (DataAccessFailedException e) {
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
		}
		
	}
	
	
   public void tapContent(Long contentId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException{
		
		try {
			if(follwerId<=0){
				logger.error("tapContent contentId"+contentId +" follwerId is not found "+follwerId);
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_DATA_NOT_FOUND_EXCEPTION);
			}
			ContentVO contentVO  = contentDAO.getContentById(contentId,VoizdConstant.CONTENT_ACTIVE_STATUS) ;
			logger.debug("contentVO---"+contentVO);
			if(null!=contentVO){
				if(contentVO.getCreatorId()!=follwerId){
					StreamVO streamVO = null ;
					if(streamId==0){
						List<StreamVO> streamVOList = userStreamDAO.getUserStreamList(follwerId);
						if(null!=streamVOList && streamVOList.size()>0){
							streamVO = streamVOList.get(0);
						}
					}else{
						streamVO = userStreamDAO.getStreamInfoByUserId(follwerId, streamId);
					}
					logger.debug("tapContentstreamVO---"+streamVO);
					if(null==streamVO){
						try {
							streamId = streamService.createStream(StreamUtils.getDefaultStreamVO(follwerId));
						} catch (StreamServiceException e) {
							logger.error("could not create default stream "+e.getLocalizedMessage(), e);
							throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
						}
					}else{
						streamId = streamVO.getStreamId();
						logger.debug("tapContentstreamId-----"+streamId);
					}
					Byte subStatus = contentSubDAO.getContentTap(contentId,streamId,follwerId);
					logger.debug("tapContent subStatus---"+subStatus);
					if(null==subStatus){
						contentSubDAO.saveContentTap(contentId,streamId, follwerId);
						ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentId);
						if(null!=contentCounterVO){
							contentCounterVO.setFollower(contentCounterVO.getFollower()+1);
							contentCounterDAO.updateContentCounter(contentCounterVO);
						}else{
							logger.error("no counter for tapStation for contentId="+contentId);
						}
						StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(streamId);
						if(null!=streamCounterVO){
							streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()+1);
							userStreamCounterDAO.updateStreamCounter(streamCounterVO);
							if(streamCounterVO.getNumberOfContent()==1 && streamVO.getStatus()==VoizdConstant.DEACTIVE_STREAM_STATUS){
								userStreamDAO.updateStreamStatus(follwerId, streamId, VoizdConstant.ACTIVE_STREAM_STATUS, true);
							}
						}else{
							logger.error("no counter for stream for streamId="+streamId);
						}
						
						
					}else{
						if(subStatus.equals(status)){
							//do nothing
						}else{
							logger.debug("stationCounterDAO.contentId="+contentId+" follwerId="+follwerId+" status="+status);
							contentSubDAO.updateContentTap(contentId,streamId, follwerId, status);
							logger.debug("stationCounterDAO.updateStationTap=");
							ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentId);
							if(null!=contentCounterVO){
								if(VoizdConstant.CONTENT_FOLLOW_STATUS.equals(status)){
									contentCounterVO.setFollower(contentCounterVO.getFollower()+1);
								}else{
									contentCounterVO.setFollower(contentCounterVO.getFollower()-1);
								}
								if(contentCounterVO.getFollower()>=0){
									contentCounterDAO.updateContentCounter(contentCounterVO);
								}
							}else{
								logger.error("no counter for tapStation for contentId="+contentId);
							}
							
							StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(streamId);
							if(null!=streamCounterVO){
								if(VoizdConstant.CONTENT_FOLLOW_STATUS.equals(status)){
									streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()+1);
								}else{
									streamCounterVO.setNumberOfContent(streamCounterVO.getNumberOfContent()-1);
								}
								if(streamCounterVO.getNumberOfContent()>=0){
									userStreamCounterDAO.updateStreamCounter(streamCounterVO);
								}
								if(streamCounterVO.getNumberOfContent()==0){
									userStreamDAO.updateStreamStatus(follwerId, streamId, VoizdConstant.DEACTIVE_STREAM_STATUS, false);
								}
							}else{
								logger.error("no counter for stream for streamId="+streamId);
							}
							
						}
					}
				}else{
					logger.info("you are trying to follow your content contentId="+contentId+" follwerId="+follwerId);
				}
			}else{
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
			}
		} catch (DataUpdateFailedException e) {
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_UPDATE_SERVICE_FAILED_EXCEPTION);
		} catch (DataAccessFailedException e) {
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_GET_SERVICE_FAILED_EXCEPTION);
		}
		
	}

	@Override
	public void userLikeOperation(Long id, Long userId, Byte status,String type) throws UserActionServiceFailedException {
		
		if(userId<=0 || id<=0 || StringUtils.isBlank(type)){
			logger.error("userLikeOperation id = "+id +" userId = "+userId+" type="+type);
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_DATA_NOT_FOUND_EXCEPTION);
		}
		 if(VoizdConstant.CONTENT.equalsIgnoreCase(type)){
		  try{
			  ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(id);
			  ContentLikeVO contentLikeVO = contentLikeDAO.userContentLikeStatus(id, userId);
			  if(contentCounterVO != null){
			  long likeValue = contentCounterVO.getLikes();
			  if(contentLikeVO != null){
				  contentLikeDAO.updateContentLikeStatus(id, userId, status);
			  }else{
				  contentLikeDAO.insertContentLike(id, userId, status);
			  }
			 if (VoizdConstant.CONTENT_LIKE_STATUS.equals(status) && likeValue >= 0) {
					contentCounterVO.setLikes(likeValue+1);
					contentCounterDAO.updateContentCounter(contentCounterVO);
					UserCounterVO userCounterVO = userCounterDAO.getUserCounter(userId);
					if(null!=userCounterVO){
						userCounterVO.setLikes(userCounterVO.getLikes()+1);
						userCounterDAO.updateUserCounter(userCounterVO);
					}else{
							logger.error("no counter for user for userId="+userId);
					}
					ContentVO contentVO = contentDAO.getContentByContentId(id);
					logger.debug("userLikeOperation :: content :: "+id+" , CreatorId :: "+contentVO.getCreatorId());
				 	if(!contentVO.getCreatorId().equals(userId)){
						UserPushInfo pushInfo = userDAO.getUserPushInfo(contentVO.getCreatorId());
						if(pushInfo != null && "yes".equalsIgnoreCase(pushInfo.getSendNotif())){
							logger.debug("userLikeOperation :: userId :: "+userId);
							UserInfo userInfo = userDAO.getUserProfile(userId);
							String message = PushMessageUtil.getPushMessage(contentVO.getTitle(),"favorited",userInfo.getFirstName(),userInfo.getLastName());
							sendJmsPushMessage(userId,pushInfo.getDeviceKey(),message,pushInfo.getNotifType());
						}
					}else{
				 		logger.debug("ampifyContent ::, CreatorId :: "+contentVO.getCreatorId()+" userId="+userId);
				 	}
			 } else if(likeValue > 0 && VoizdConstant.CONTENT_UNLIKE_STATUS.equals(status)){
				 contentCounterVO.setLikes(likeValue-1);
				 contentCounterDAO.updateContentCounter(contentCounterVO); 
					UserCounterVO userCounterVO = userCounterDAO.getUserCounter(userId);
					if(null!=userCounterVO){
						userCounterVO.setLikes(userCounterVO.getLikes()-1);
						userCounterDAO.updateUserCounter(userCounterVO);
					}else{
							logger.error("no counter for user for userId="+userId);
					}
			 }else{
				 logger.error("userLikeOperation not doing any thing :: type " + type + ", like :: " + status+" contentid="+id);
			 }
			 logger.debug("Sending push jsm message start ="+userId);
			/* UserPushInfo pushInfo = userDAO.getUserPushInfo(214l);
				if(pushInfo != null && "yes".equalsIgnoreCase(pushInfo.getSendNotif())){
					logger.debug("userLikeOperation :: userId :: "+userId);
					UserInfo userInfo = userDAO.getUserProfile(214l);
					String message = PushMessageUtil.getlikePushMessage(userInfo.getFirstName(),userInfo.getLastName());
					sendJmsPushMessage(userId,pushInfo.getDeviceKey(),message,pushInfo.getNotifType());
				}*/
			//sendJmsPushMessage(null,"1851660bb1c86c5da6dd9fe85e4ca223dd0969459727de7e394a22613576656e","Hello , This is test message. ");
			// sendJmsPushMessage(null,"be449c17c3520edfb9bced018bbeb688c1f3a2b4f6739f97754b1aa0cd4c6c31","Hello , This is test message. ");
			 logger.debug("Sending push jsm message end ="+userId);
			}else{
				 logger.error("userLikeOperation :: Please check content counter table no value  "+" contentid="+id);
			}
		  }catch (Exception e) {
				logger.error("userLikeOperation "+e.getLocalizedMessage(),e);
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
			}
	  }else if (VoizdConstant.STATION.equalsIgnoreCase(type)) {
			try {
				StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(id);
				StationLikeVO stationLikeVO = stationLikeDAO.userStationLikeStatus(id, userId);
				if(stationCounterVO != null){
				long likeValue = stationCounterVO.getLikes();
				if (stationLikeVO != null) {
					stationLikeDAO.updateStationLikeStatus(id, userId, status);
				} else {
					stationLikeDAO.insertStationLike(id, userId, status);
				}
				if (VoizdConstant.STATION_LIKE_STATUS.equals(status) && likeValue >= 0) {
					stationCounterVO.setLikes(likeValue + 1);
					stationCounterDAO.updateStationCounter(stationCounterVO);
				} else if (likeValue > 0 && VoizdConstant.STATION_UNLIKE_STATUS.equals(status)) {
					stationCounterVO.setLikes(likeValue - 1);
					stationCounterDAO.updateStationCounter(stationCounterVO);
				} else {
					logger.error("userLikeOperation not doing any thing :: type " + type + ", like :: " + status+" stationid="+id);
				}
			 }else{
			 logger.error("userLikeOperation  station counter found null please :: type " + type + ", like :: " + status+" stationid="+id);
			 }
			} catch (Exception e) {
				logger.error("userLikeOperation " + e.getLocalizedMessage(), e);
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
			}
		}else if (VoizdConstant.STREAM.equalsIgnoreCase(type)) {
			try {
				StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(id);

				if(streamCounterVO != null){
					StreamLikeVO streamLikeVO = userStreamLikeDAO.userStreamLikeStatus(id, userId);
					long likeValue = streamCounterVO.getLikes();
					if (streamLikeVO != null) {
						userStreamLikeDAO.updateStreamLikeStatus(id, userId, status);
					} else {
						userStreamLikeDAO.insertStreamLike(id, userId, status);
					}
					if (VoizdConstant.STREAM_LIKE_STATUS.equals(status) && likeValue >= 0) {
						streamCounterVO.setLikes(likeValue + 1);
						userStreamCounterDAO.updateStreamCounter(streamCounterVO);
					} else if (likeValue > 0 && VoizdConstant.STREAM_UNLIKE_STATUS.equals(status)) {
						streamCounterVO.setLikes(likeValue - 1);
						userStreamCounterDAO.updateStreamCounter(streamCounterVO);
					} else {
						logger.error("userLikeOperation not doing any thing :: type " + type + ", like :: " + status+" streamid="+id);
					}
			 }else{
			 logger.error("userLikeOperation  station counter found null please :: type " + type + ", like :: " + status+" streamid="+id);
			 }
			} catch (Exception e) {
				logger.error("userLikeOperation " + e.getLocalizedMessage(), e);
				throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
			}
		}
	}
	
	public void tapStation(Long creatorId, Long follwerId, Byte status) throws UserActionServiceFailedException{
		 try {
				if(creatorId<=0 || follwerId<=0 ){
					logger.debug("tapContent creatorId = "+creatorId +" follwerId = "+follwerId);
					throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_DATA_NOT_FOUND_EXCEPTION);
				}
				if(creatorId==follwerId ){
					logger.info("you are trying to follow to self follwerId="+follwerId);
					throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
				}
				int userStatus = userDAO.getUserStatusById(follwerId);
				
				if(VoizdConstant.ACTIVE_USER_STATUS!=userStatus){
					throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACCOUNT_INACTIVE);
				}
				
			List<StationVO> stationVOList = stationDAO.getMyStationList(creatorId,VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
			logger.info("stationVOList="+stationVOList);
			if(null!=stationVOList && stationVOList.size()>0 ){
					Long stationId = stationVOList.get(0).getStationId();
					tapStation(stationId,0l,follwerId,creatorId,status);
			}
		
		} catch (Exception e) {
			logger.error("tapContent " + e.getLocalizedMessage(), e);
			throw new UserActionServiceFailedException(ErrorCodesEnum.USER_ACTION_SERVICE_FAILED_EXCEPTION);
		}
	}
	
	

	private void sendJmsMessage(Long stationId,Long creatorId,Long follweeId){
		try {
			TapClipVO tapClipVO= new TapClipVO();
			tapClipVO.setStationId(stationId);
			tapClipVO.setCreatorId(creatorId);
			tapClipVO.setFollweeId(follweeId);
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put(VoizdConstant.CLIP_KEY, tapClipVO);
			msgMap.put(VoizdConstant.COMMAND_KEY,"uptap");
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:tapvoizd");
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
			msgMap.put("like", 1);
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:push");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}
	
}
