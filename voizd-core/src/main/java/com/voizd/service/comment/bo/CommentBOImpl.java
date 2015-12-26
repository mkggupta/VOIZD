package com.voizd.service.comment.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.CommentDTO;
import com.voizd.common.beans.dto.CommentInfoDTO;
import com.voizd.common.beans.dto.ContentDTO;
import com.voizd.common.beans.vo.CommentCounterVO;
import com.voizd.common.beans.vo.CommentVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.IpToLocationVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.ShareVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.entities.CommentInfo;
import com.voizd.dao.entities.CommentMediaInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.comment.CommentCounterDAO;
import com.voizd.dao.modules.comment.CommentDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.content.ContentLikeDAO;
import com.voizd.dao.modules.content.ContentSubDAO;
import com.voizd.dao.modules.country.CountryDAO;
import com.voizd.dao.modules.media.CommentMediaDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationLikeDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.user.UserDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;
import com.voizd.service.comment.exception.CommentServiceException;
import com.voizd.service.comment.helper.CommentUtils;
import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.service.location.bo.LocationBO;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.tagcloud.bo.TagCloudBO;

import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;
import com.voizd.util.PushMessageUtil;

public class CommentBOImpl implements CommentBO {
	private static Logger logger = LoggerFactory.getLogger(CommentBOImpl.class);
	private static String COMMENT="comment";
	private static String DELETE="delete";
	
	private ContentDAO contentDAO ;
	private CommentDAO commentDAO ;
	private CommentMediaDAO commentMediaDAO ;
	private MediaService  mediaService;
	private UserService userService ;
	private CountryDAO countryDAO;
    private JmsSender jmsSender;
	private ContentLikeDAO contentLikeDAO;
	
    public void setContentLikeDAO(ContentLikeDAO contentLikeDAO) {
		this.contentLikeDAO = contentLikeDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private UserDAO userDAO;
	private MediaDAO mediaDAO ;
	private CommentCounterDAO commentCounterDAO ;
	private ContentCounterDAO contentCounterDAO ;
	
	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}
	public void setCommentCounterDAO(CommentCounterDAO commentCounterDAO) {
		this.commentCounterDAO = commentCounterDAO;
	}
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}
	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}
	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}
	public void setCommentMediaDAO(CommentMediaDAO commentMediaDAO) {
		this.commentMediaDAO = commentMediaDAO;
	}
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}
	public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}
	
	


	public HashMap<String, Object> createComment(CommentVO commentVO) throws CommentServiceException{HashMap<String, Object> commentMap = new HashMap<String, Object>();
	if(StringUtils.isNotBlank(commentVO.getFileIds())){
		try {
			UserVO userVO = userService.getUserProfile(commentVO.getCommenterId());
			if(null!=commentVO.getContentId()|| commentVO.getContentId()>0){
				ContentVO contentVO =  contentDAO.getContentById(commentVO.getContentId(),VoizdConstant.CONTENT_ACTIVE_STATUS);
				if(null!=contentVO){
				 IpToLocationVO ipToLocationVO = null ;
				 if(null!=commentVO.getCountry()){
					 commentVO.setCountry(countryDAO.getUserCountry(commentVO.getCountry()));
				 }else {
					 if(null!=ipToLocationVO){
						 if(StringUtils.isBlank(commentVO.getCountry()) && StringUtils.isNotBlank(ipToLocationVO.getCountry())){
							 commentVO.setCountry(ipToLocationVO.getCountry());
						  }else if(null!=userVO.getCountry()){
							  commentVO.setCountry(userVO.getCountry());
							 }else{
								 commentVO.setCountry(VoizdConstant.DEFAULT_COUNTRY);
							 }
					 }else{
						 commentVO.setCountry(VoizdConstant.DEFAULT_COUNTRY);
					 }
				 }
				
				 if (null!=userVO.getLanguage() && userVO.getLanguage().trim().length()>0){
					 commentVO.setLanguage(userVO.getLanguage());
				 }else {
					 if(null!=commentVO.getLanguage() && commentVO.getLanguage().trim().length()>0){
						 commentVO.setLanguage(countryDAO.getUserLanguage(commentVO.getLanguage()));
					 }else{
						 commentVO.setLanguage(VoizdConstant.DEFAULT_LANGUAGE);
					 }
				 }
				 if(StringUtils.isBlank(commentVO.getState())){
					 commentVO.setState(VoizdConstant.DEFAULT_STATE);
					}
				if(StringUtils.isBlank(commentVO.getCity())){
					commentVO.setCity(VoizdConstant.DEFAULT_CITY);
				}
				 
				/* if(null!=commentVO.getCommentText()){
						try {
							commentVO.setTagCloud(tagCloudBO.createTagCloud(contentVO,status));
						} catch (TagCloudServiceException e) {
							logger.error("Error while insert tag data  :" + e.getLocalizedMessage(), e);
						}
				 }*/
				CommentInfo commentInfo = CommentUtils.transformCommentInfo(commentVO, userVO, contentVO.getCreatorId());
				Long commentId = commentDAO.saveCommentInfo(commentInfo);
				logger.debug("mediaVO.getFileIds()--"+commentVO.getFileIds());
				String fileIdArr[] = commentVO.getFileIds().split("\\s*,\\s*");
				 int ordering = 1 ;
					for (String fileId:fileIdArr){
						try {
							MediaVO mediaVO = mediaService.getMediaInfo(fileId);
							if(null!=mediaVO){
								logger.debug("mediaVO.getDuration()--"+mediaVO.getDuration()+" size"+mediaVO.getSize());
								CommentMediaInfo commentMediaInfo = new CommentMediaInfo();
								commentMediaInfo.setCommentId(commentId);
								commentMediaInfo.setFileId(fileId);
								commentMediaInfo.setMediaType(mediaVO.getMediaType());
								commentMediaInfo.setExt(mediaVO.getMimeType());
								commentMediaInfo.setDuration(mediaVO.getDuration());
								commentMediaInfo.setSize((int)mediaVO.getSize());
								commentMediaDAO.createCommentMedia(commentMediaInfo);
								ordering++;
							    if(VoizdConstant.AUDIO.equalsIgnoreCase(mediaVO.getMediaType()) && !VoizdConstant.MP3.equalsIgnoreCase(mediaVO.getMimeType())){
							    	commentMediaInfo.setExt(VoizdConstant.MP3);
									 mediaService.convertMedia(commentMediaInfo.getMediaType(),commentMediaInfo.getFileId(),commentMediaInfo.getExt(),null);
							   }
							}
						} catch (MediaServiceFailedException e) {
							throw new ContentServiceException(ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION);
						}
					}
					
					ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(commentVO.getContentId());
					if(null!=contentCounterVO){
						contentCounterVO.setComments(contentCounterVO.getComments()+1);
						contentCounterDAO.updateContentCounter(contentCounterVO);
						commentCounterDAO.createCommentCounter(commentId);
					}
					try{
						if(!contentVO.getCreatorId().equals(commentVO.getCommenterId())){
							  UserPushInfo pushInfo = userDAO.getUserPushInfo(contentVO.getCreatorId());
							if(pushInfo != null && "yes".equalsIgnoreCase(pushInfo.getSendNotif())){
						      String message = PushMessageUtil.getPushMessage("comment","",userVO.getFirstName(),userVO.getLastName());
						      sendJmsPushMessage(contentVO.getCreatorId(),pushInfo.getDeviceKey(),message,pushInfo.getNotifType());
							}
						}
					}catch (Exception e) {
						logger.error("Error you are  trying to comment of deleted comments contentid:" + commentVO.getContentId());
					}
					/*if(StringUtils.isNotBlank(contentVO.getAppIds())){
						 List<ShareVO> shareVOList = new ArrayList<ShareVO>();
						 
						String appIdArr[] = contentVO.getAppIds().split("\\s*,\\s*");	
						for (String appId:appIdArr){
							ShareVO  shareVO = getContentShareUrl(contentVO.getStationId(),contentVO.getContentId(),appId);
							shareVOList.add(shareVO);
						}
						if(shareVOList.size()>0)
							commentMap.put("share", shareVOList);
					logger.debug(" commentMap shareVOList---"+shareVOList);
					}else{*/
						commentMap.put("status", "success");
					//}
				}else{
					logger.error("Error you are  trying to comment of deleted comments contentid:" + commentVO.getContentId());
					throw new CommentServiceException(ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION);
				}
				
			}else{
				logger.error("Error while createComment in commentVO:" + commentVO);
			}
		} catch (DataUpdateFailedException e) {
			logger.error("Error while createComment update :" + e.getLocalizedMessage(), e);
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION);
		}catch (DataAccessFailedException e) {
			logger.error("Error while createComment get:" + e.getLocalizedMessage(), e);
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION);
		}catch (Exception e) {
			logger.error("Error while createComment :" + e.getLocalizedMessage(), e);
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION);
		}	
		
	}else{
		throw new CommentServiceException(ErrorCodesEnum.INVALID_REQUEST_EXCEPTION);
	}
	return commentMap;
	
	}
	@Override
	public HashMap<String, Object> getCommentList(Long contentId, int start,int end,Map<String, Object> clientMap,Long userId) throws CommentServiceException {
		HashMap<String, Object> commentMap = new HashMap<String, Object>();
		CommentDTO commentDTO = new CommentDTO();
		List<CommentInfoDTO> commentInfoDTOList =new ArrayList<CommentInfoDTO>();
		boolean hasNext = false ;
		try {
			logger.error("Error while getCommentList :contentId : " +contentId+" , start : "+start +" ,end :  "+end );
			ContentVO contentVO =  contentDAO.getContentById(contentId,VoizdConstant.CONTENT_ACTIVE_STATUS);
			commentDTO.setcTitle(contentVO.getTitle());
		
		
			
			List<CommentInfo>  commentInfoList = commentDAO.getCommentListByContentId(contentId, start, end+1);	
			 String imageResolution = null;
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(commentInfoList != null && commentInfoList.size()>10){
				 hasNext = true; 
				 commentInfoList.remove(commentInfoList.size()-1);
			 }
			if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					  imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
				}else{
					  imageResolution = MediaUtil.getImageSize(null);
				}	
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
			 }
			 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
				 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
			 }
			 
			 List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
				for(ContentMediaVO contentMediaVO:contentMediaVOList){
					if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
						logger.debug("RECENT_Content_STATUS audio="+contentMediaVO.getMediaType()+" platform="+platform +"ext =="+contentMediaVO.getExt());
						if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
							commentDTO.setcAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}else{
							 contentMediaVO.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							 commentDTO.setcAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}
						
					 if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
						 commentDTO.setcDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
					 }
				 }
				}
				ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
				commentDTO.setcView(contentCounterVO.getView());
				commentDTO.setWeblink(contentVO.getWeblink());
				if(contentVO.getCreatorId()!=userId){
					
					if(userId>0){
						
						Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
						if(null!=likeStatus){
							 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
								 commentDTO.setcLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							 }else{
								 commentDTO.setcLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
							 }
						}else{
							commentDTO.setcLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						}
						
					}else{
						
						commentDTO.setcLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
					}
					
					commentDTO.setcLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
					commentDTO.setcShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
					commentDTO.setAmplifyUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.AMPLIFY_URL));
				}
				
				UserVO userVO = userService.getUserProfile(contentVO.getCreatorId());
				
				 if(null!= userVO){
					 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
					 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
					 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getCreatorId());
					 commentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					 commentDTO.setcName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
					 if (null != userVO.getProfilePicFileId()) {
						 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
						 if(null!=mediaVO){
							 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
							 commentDTO.setcImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							 //commentDTO.setcThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
								 commentDTO.setcImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								// commentDTO.setcThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							}
					 }
				 }
					
			for(CommentInfo commentInfo : commentInfoList) {
				
				CommentInfoDTO commentInfoDTO = new CommentInfoDTO();
				commentInfoDTO.setCmtId(commentInfo.getCommentId());
				commentInfoDTO.setCmtCntId(commentInfo.getContentId());
				//commentInfoDTO.setCmtDate(commentInfo.getCreatedDate().toGMTString());
				commentInfoDTO.setCmtDate(DateTimeUtils.format(commentInfo.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
				commentInfoDTO.setCmtTitle(commentInfo.getCommentText());
			
				CommentMediaInfo commentMediaInfo = commentMediaDAO.getCommentMedia(commentInfo.getCommentId());
				CommentCounterVO commentCounterVO = commentCounterDAO.getCommentCounter(commentInfo.getCommentId());
				if(commentCounterVO != null){
				commentInfoDTO.setCmtView(commentCounterVO.getView());
				}
				commentInfoDTO.setWeblink(commentInfo.getWeblink());
				logger.error(" getCommentList :commentMediaInfo : " +commentMediaInfo+" , comment id "+commentInfo.getCommentId() );
					if(commentMediaInfo != null && "audio".equalsIgnoreCase(commentMediaInfo.getMediaType())){
						if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(commentMediaInfo.getExt())||VoizdConstant.AMR.equalsIgnoreCase(commentMediaInfo.getExt())) ){
							commentInfoDTO.setCmtCntUrl((VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(commentMediaInfo.getMediaType(),MediaUtil.getMediaContentUrl(commentMediaInfo.getFileId(),commentMediaInfo.getExt(),null),true))));
						}else{
							commentMediaInfo.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(commentMediaInfo.getMediaType(),commentMediaInfo.getFileId(),commentMediaInfo.getExt(),null);
							 commentInfoDTO.setCmtCntUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(commentMediaInfo.getMediaType(),MediaUtil.getMediaContentUrl(commentMediaInfo.getFileId(),commentMediaInfo.getExt(),null),true)));
						}
						
						if(StringUtils.isNotBlank(commentMediaInfo.getDuration())){
							commentInfoDTO.setCmtDuration(DateTimeUtils.getTimeInSecond(commentMediaInfo.getDuration()));
						}
					}
				
				 UserVO commenterVO = userService.getUserProfile(commentInfo.getCommenterUserId());
				 if(commenterVO != null){
					 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
					 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
					 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(commentInfo.getCommenterUserId());
					 commentInfoDTO.setCmtprofileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					 commentInfoDTO.setCommenterName(UserUtils.displayName(commenterVO.getFirstName(), commenterVO.getLastName()));
				 }
				 if (null != commenterVO.getProfilePicFileId()) {
					 MediaVO mediaVO = mediaService.getMediaInfo(commenterVO.getProfilePicFileId());
					 if(null!=mediaVO){
						 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						 commentInfoDTO.setCommenterProImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
					 }else{
							DefaultMediaVO defaultMediaVO = null;
							if (commenterVO.getGender() == 1) {
								defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
							} else {
								defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
							}
							if(null!=defaultMediaVO){
								 mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 commentInfoDTO.setCommenterProImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),
										 MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							}
					 }
				 }		
				commentInfoDTOList.add(commentInfoDTO);
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.COMMENT_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(start+end);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(end+10);
				commentDTO.setNext(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
			
		}catch (Exception e) {
			logger.error("Error while getCommentList :" + e.getLocalizedMessage(), e);
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION);
		}
		commentDTO.setComment(commentInfoDTOList);
		//commentDTO.setcName(cName);
		commentMap.put("CommentList", commentDTO);	
		return commentMap;	
	}
	
	@Override
	public void deleteComment(Long commentId,Long userId) throws CommentServiceException {
		try{
			logger.debug("deleteComment() ::  commentId :: "+commentId+" userId="+userId);
		if(null!=commentId && null !=userId){
			CommentInfo commentInfo = commentDAO.getCommentInfoByCommentId(commentId);
			int status = commentDAO.updateCommentStatusByCommentId(commentId, userId, VoizdConstant.COMMENT_DELETED_STATUS);
			logger.debug("deleteComment() ::  status :: "+status);
			if(status==1 && null!=commentInfo){
					ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(commentInfo.getContentId());
					if(null!=contentCounterVO){
						if(contentCounterVO.getComments()>0){
							contentCounterVO.setComments(contentCounterVO.getComments()-1);
							contentCounterDAO.updateContentCounter(contentCounterVO);
						}				
				}	
			}
		}else{
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION);
		}
		
		}  catch (DataAccessFailedException e) {
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION);
		}catch (DataUpdateFailedException e) {
			throw new CommentServiceException(ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION);
		}
	}
	
	private void sendJmsPushMessage(Long userId,String pushKey,String message,String platform){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("deviceKey",pushKey);
			msgMap.put("pushMessage",message);
			msgMap.put("userId", userId);
			msgMap.put("platform", platform);
			msgMap.put("comment","yes");
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:push");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}
	
	
}
