package com.voizd.service.stream.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.StreamDTO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.StreamCounterVO;
import com.voizd.common.beans.vo.StreamMediaVO;
import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStreamParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.stream.UserStreamCounterDAO;
import com.voizd.dao.modules.stream.UserStreamDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.service.content.v1_0.ContentService;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.station.v1_0.StationService;
import com.voizd.service.stream.exception.StreamServiceException;
import com.voizd.service.stream.helper.StreamUtils;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;

public class StreamBOImpl implements StreamBO {
	private static Logger logger = LoggerFactory.getLogger(StreamBOImpl.class);
	private StationService  stationService;
	private ContentService contentService ;
	private UserService userService ;
	private UserStreamDAO userStreamDAO;
	private UserStreamCounterDAO  userStreamCounterDAO;
	private  MediaDAO mediaDAO ;
	private MediaService  mediaService;
	@Override
	public HashMap<String, Object> getStream(Long streamId,Long userId, Byte streamState, int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StreamServiceException {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<StreamDTO> streamDTOList =new ArrayList<StreamDTO>();
		String streamType= VoizdConstant.MYSTREAM ;
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
		Long preStreamId= 0l;
		Long nextStreamId= 0l;
		int resultSize =0 ;
		 String imageResolution = null;
		 List<StreamVO> streamList = null ;
		 
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		
		try {
			//true is asc order
				if(order && streamId>0){
					streamList = userStreamDAO.getMyStreamList(userId,startLimit,endLimit+1);
					 hasNext = true ;
				}else if(streamId>0){
				//desc order	
					streamList = userStreamDAO.getMyStreamList(userId,startLimit,endLimit+1);
					 hasPre = true ;
				}else{
					//first hit
					streamList = userStreamDAO.getMyStreamList(userId,startLimit,endLimit+1);
				 isFirstRequest = true;
				}
				logger.debug("getStream streamList"+streamList);
				if(null!= streamList){
					if(endLimit<streamList.size()){
						if(order){
							streamList.remove(0);
							 hasPre = true ;
						}else{
							streamList.remove(streamList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = streamList.size();
				}
				 int cnt =0 ;
				 for(StreamVO streamVO : streamList){
					 StreamDTO streamDTO = new StreamDTO();

					 if(order && cnt==0){
						 preStreamId= streamVO.getStreamId();
					 }
					 streamId =streamVO.getStreamId();
					 streamDTO.setStId(streamId);
					 streamDTO.setStName(streamVO.getStreamName());
					 streamDTO.setStDate(streamVO.getCreatedDate());
					 streamDTO.setCId(streamVO.getUserId());
					 streamDTO.setStTag(streamVO.getTag());
					 streamDTO.setStState(streamVO.getStreamState());
					 streamDTO.setStState(streamVO.getStatus());
					 StreamMediaVO streamMediaVO = mediaDAO.getStreamMediaByFileId(streamVO.getFileId());
					 if(null!=streamMediaVO){
						 mediaService.convertMedia(streamMediaVO.getMediaType(),streamMediaVO.getFileId(),streamMediaVO.getExt(),imageResolution);
						 streamDTO.setStImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(streamMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(streamMediaVO.getFileId(),streamMediaVO.getExt(),imageResolution),false)));
					 }else{
						 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(streamVO.getFileId());
						 if(null!=defaultMediaVO){
						    mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
						    streamDTO.setStImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
						 }
					 }
					
					 streamDTO.setStDesc(streamVO.getDescription());
					 try {
						 UserVO userVO = userService.getUserProfile(streamVO.getUserId());
						 if(null!= userVO){
							 streamDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 }
						 StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(streamId);
						if(null!=streamCounterVO){
							streamDTO.setStLikes(streamCounterVO.getLikes());
							streamDTO.setStContCnt(streamCounterVO.getNumberOfContent());
							streamDTO.setStView(streamCounterVO.getView());
							streamDTO.setStComments(streamCounterVO.getComments());
							streamDTO.setStShare(streamCounterVO.getShare());
						}
						if(streamVO.isHasContent()){
							StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MYTAPPED_STATION_URL);
							relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStreamParam.STREAM_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(streamVO.getStreamId());
							if(userId>0){
								relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							}
							streamDTO.setStDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						}
						/*if(streamVO.getUserId()!=userId){	
							if(userId>0){
								
								Byte sLikeStatus  = stationLikeDAO.getUserStationLike(stationId, userId);
								if(null!=sLikeStatus){
									 if(VoizdConstant.STATION_UNLIKE_STATUS.equals(sLikeStatus)){
										 streamDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
									 }else{
										 streamDTO.setLikeValue(VoizdConstant.STATION_UNLIKE_STATUS);
									 }
								}else{
									streamDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
								}
							}else{
								
								streamDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
							}
							
							streamDTO.setLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_LIKE_URL));
						}*/
						
					} catch (DataAccessFailedException e) {
						logger.error(" getStations.stationCounterVO exception for stationid="+streamVO.getStreamId()+
						"reason is "+e.getLocalizedMessage(),e);
						throw new StreamServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
					}catch (UserServiceFailedException e) {
						logger.error(" getStations.getUserProfile exception for stationid="+streamVO.getStreamId()+" for user "+streamVO.getUserId()+
								"reason is "+e.getLocalizedMessage(),e);
						throw new StreamServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
					}
					
					streamDTOList.add(streamDTO);
					cnt++;
					if(cnt==streamDTOList.size()){
						nextStreamId= streamVO.getStreamId();
					}
					 
				 }
				 if(isFirstRequest && streamList!=null && streamList.size()<=0){
					 resultMap.put("status",  "success");
					 resultMap.put("message", "You have not created any stream till now.");
				 }
		} catch (DataAccessFailedException e) {
			throw new StreamServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
		}
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MYSTREAM_MORE_URL);
			
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			resultMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MYSTREAM_MORE_URL);
			
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStreamParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			resultMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		resultMap.put(streamType, streamDTOList);
		return resultMap;
	}
	public HashMap<String, Object>  createStreamShareUrl(Long userId,Byte appId) throws StreamServiceException {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String userName =null;
		 UserVO userVO;
		try {
			userVO = userService.getUserProfile(userId);
			 if(null!= userVO){
				 userName= UserUtils.displayName(userVO.getFirstName(), userVO.getLastName());
			 }
			 dataMap.put(VoizdConstant.MESSAGE,userName+" has shared his personal collection ");
			 dataMap.put(VoizdConstant.SHARE_URL, VoizdUrlUtils.getStreamShareUrl(userName, userId, appId));
		} catch (UserServiceFailedException e) {
			throw new StreamServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		
		return dataMap;
	}
	
	public Long createStream(StreamVO streamVO) throws StreamServiceException {
		Long responseCode = -1l ;
	
				try {
					Boolean isExist = userStreamDAO.streamAlreadyExists(streamVO.getStreamName(),streamVO.getUserId());
					logger.debug("createStream.isExist="+isExist);
					if(isExist){
						return responseCode;
					}else{
						 try {
							 String fileIdArr[] = null;
							 if(null==streamVO.getFileIds()){
								 streamVO.setFileId(mediaDAO.getDefaultMedia(VoizdConstant.DEFAULT_FILE_NAME));
							 }else{
								 	fileIdArr =streamVO.getFileIds().split("\\s*,\\s*");
								   if(fileIdArr.length>0){
									   streamVO.setFileId(fileIdArr[0]);	
									}	
							 }
							Long streamId = userStreamDAO.createStream(streamVO);
							logger.debug("streamVO.streamId="+streamId);
							 if(null!=fileIdArr && fileIdArr.length>0){
									
									int ordering = 1 ;
									for (String fileId:fileIdArr){
										try {
											MediaVO mediaVO = mediaService.getMediaInfo(fileId);
											if(null!=mediaVO){
												StreamMediaVO streamMediaVO = new StreamMediaVO();
												streamMediaVO.setStreamId(streamId);
												streamMediaVO.setMediaType(mediaVO.getMediaType());
												streamMediaVO.setExt(mediaVO.getMimeType());
												streamMediaVO.setFileId(fileId);
												streamMediaVO.setOrdering(ordering);
												streamMediaVO.setSize(mediaVO.getSize());
												streamMediaVO.setStatus(VoizdConstant.MEDIA_ACTIVE_STATUS);
												mediaDAO.createStreamMedia(streamMediaVO);
												ordering++;
											}
										} catch (MediaServiceFailedException e) {
											throw new StreamServiceException(ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION);
										}
									}
							}	
							 userStreamCounterDAO.createStreamCounter(streamId);
							responseCode = streamId;
						} catch (DataUpdateFailedException e) {
							throw new StreamServiceException(ErrorCodesEnum.STREAM_UPDATE_SERVICE_FAILED_EXCEPTION);
						}
					}
				} catch (DataAccessFailedException e) {
					throw new StreamServiceException(ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION);
				}

		return responseCode;
	}
	public StreamVO getStream(Long streamId) throws StreamServiceException {
		return null;
	}
	public void updateStream(StreamVO streamVO) throws StreamServiceException {
	      try{

	        StreamVO oldStreamVO = userStreamDAO.getStreamInfoByUserId(streamVO.getUserId(),streamVO.getStreamId());
			
			if(oldStreamVO==null){
				throw new StreamServiceException(ErrorCodesEnum.STREAM_UPDATE_DATA_NOT_FOUND);	
			}
			StreamVO updatedStreamVO = StreamUtils.transformStream(streamVO, oldStreamVO);
			List<String>  fileIdsList = null ;
			if(streamVO.getFileIds() != null){
				fileIdsList = StreamUtils.getListOfFileId(streamVO.getFileIds());
				   if(fileIdsList.size()>0){
					   updatedStreamVO.setFileId(fileIdsList.get(0));	
					}	
				 updatedStreamVO.setFileIds(streamVO.getFileIds());
			}else{
				updatedStreamVO.setFileId(oldStreamVO.getFileId());
			}
			userStreamDAO.updateStream(updatedStreamVO);
			
			if(null!=fileIdsList && fileIdsList.size()>0){
					mediaDAO.updateStreamMedia(streamVO.getStreamId());
					int ordering = 1 ;
					for (String fileId : fileIdsList) {
						try {
							MediaVO mediaVO = mediaService.getMediaInfo(fileId);
							if(null!=mediaVO){
								StreamMediaVO streamMediaVO = StreamUtils.transformStationMedia(mediaVO, streamVO.getStreamId(), ordering);
								mediaDAO.createStreamMedia(streamMediaVO);
								ordering++;
							}
						} catch (MediaServiceFailedException e) {
							throw new StreamServiceException(ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION);
						}
					}
			}	
			
	        }catch (DataUpdateFailedException e) {
				throw new StreamServiceException(ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION);
			}catch (DataAccessFailedException e) {
				throw new StreamServiceException(ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION);
			}catch(StreamServiceException e){
	        	logger.error("Station update fail "+e.getLocalizedMessage(),e);
	        	throw new StreamServiceException(ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION);
	        }
	}
	
	
	public void deleteStream(Long streamId,Long userId) throws StreamServiceException {
		try{
			userStreamDAO.updateStreamStatus(userId,streamId,VoizdConstant.DELETED_STREAM_STATUS,false);
			logger.debug("Station update ");
		} catch (DataUpdateFailedException e) {
			throw new StreamServiceException(ErrorCodesEnum.STREAM_DELETE_FAILED_EXCEPTION);
		}
	}
	
	public void setStationService(StationService stationService) {
		this.stationService = stationService;
	}
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setUserStreamDAO(UserStreamDAO userStreamDAO) {
		this.userStreamDAO = userStreamDAO;
	}
	public void setUserStreamCounterDAO(UserStreamCounterDAO userStreamCounterDAO) {
		this.userStreamCounterDAO = userStreamCounterDAO;
	}
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	

}
