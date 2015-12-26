package com.voizd.service.content.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;
import com.voizd.common.beans.dto.AmplifierDTO;
import com.voizd.common.beans.dto.CommentDTO;
import com.voizd.common.beans.dto.CommentInfoDTO;
import com.voizd.common.beans.dto.ContentDTO;
import com.voizd.common.beans.dto.ContentDetailDTO;
import com.voizd.common.beans.dto.FollowerDTO;
import com.voizd.common.beans.dto.StationDTO;
import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentFollowerVO;
import com.voizd.common.beans.vo.ContentLikeVO;
import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.IpToLocationVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.ShareVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.TagCloudVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.beans.vo.VStreamVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.common.util.LocationUtil;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.entities.UserInfo;
import com.voizd.dao.entities.UserPushInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.content.ContentLikeDAO;
import com.voizd.dao.modules.content.ContentSubDAO;
import com.voizd.dao.modules.country.CountryDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationLikeDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.user.UserDAO;
import com.voizd.dao.mongo.service.MongoServiceImpl;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.service.amplify.bo.AmplifyActionBO;
import com.voizd.service.amplify.exception.AmplifyServiceFailedException;
import com.voizd.service.comment.bo.CommentBO;
import com.voizd.service.comment.exception.CommentServiceException;
import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.service.content.helper.ContentUtils;
import com.voizd.service.location.bo.LocationBO;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.search.exception.SearchServiceException;
import com.voizd.service.search.helper.SearchUtils;
import com.voizd.service.station.helper.StationUtils;
import com.voizd.service.tagcloud.bo.TagCloudBO;
import com.voizd.service.tagcloud.exception.TagCloudServiceException;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;
import com.voizd.util.PushMessageUtil;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ContentBOImpl implements ContentBO {
	private static Logger logger = LoggerFactory.getLogger(ContentBOImpl.class);
	private static String TAP="tap";
	private static String DELETE="delete";
	private StationDAO  stationDAO ;
	private ContentDAO contentDAO ;
	private MediaDAO mediaDAO ;
	private StationCounterDAO stationCounterDAO;
	private MediaService  mediaService;
	private ContentCounterDAO contentCounterDAO ;
	private UserService userService ;
	private ContentSubDAO  contentSubDAO;
	private ContentLikeDAO contentLikeDAO;
	private StationSubDAO  stationSubDAO ;
	private StationLikeDAO stationLikeDAO;
	private SearchBO searchBO;
	private CountryDAO countryDAO;
	private UserDAO userDAO;
    private JmsSender jmsSender;
    private TagCloudBO tagCloudBO;
    private LocationBO locationBO;
	private AmplifyDAO amplifyDAO;
	private AmplifyActionBO amplifyActionBO;
	private CommentBO commentBO;
	
	public void setCommentBO(CommentBO commentBO) {
		this.commentBO = commentBO;
	}
	public void setAmplifyActionBO(AmplifyActionBO amplifyActionBO) {
		this.amplifyActionBO = amplifyActionBO;
	}
	public void setAmplifyDAO(AmplifyDAO amplifyDAO) {
		this.amplifyDAO = amplifyDAO;
	}
	public void setLocationBO(LocationBO locationBO) {
		this.locationBO = locationBO;
	}
	public void setTagCloudBO(TagCloudBO tagCloudBO) {
		this.tagCloudBO = tagCloudBO;
	}
	public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}


	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}


	@Override
	public HashMap<String, Object> createStationContent(ContentVO contentVO) throws ContentServiceException {
	//	Integer responseCode = 0 ;
		HashMap<String, Object> stationContentMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(contentVO.getFileIds())){
			try {
				UserVO userVO = userService.getUserProfile(contentVO.getCreatorId());
				if(null==contentVO.getStationId()|| contentVO.getStationId()==0){
					 List<StationVO> stationVOList = stationDAO.getMyStationList(contentVO.getCreatorId(),VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
					if(null!=stationVOList && stationVOList.size()>0 ){
					  contentVO.setStationId(stationVOList.get(0).getStationId());
					  int status = userDAO.getUserStatusById(userVO.getId());
					  IpToLocationVO ipToLocationVO = null ;
					  logger.debug("createStationContent="+contentVO.getLatitude()+contentVO.getLatitude()+contentVO.getCountry()+contentVO.getMobileIp());
					  if(StringUtils.isNotBlank(contentVO.getMobileIp())&& (contentVO.getLatitude()==0.0 || contentVO.getLatitude()==0.0 || null==contentVO.getCountry())  ){
						   ipToLocationVO = locationBO.getIpToLocation(LocationUtil.getFileLocation(), contentVO.getMobileIp());
						  logger.debug("ipToLocationVO=="+ipToLocationVO +ipToLocationVO.getCountry());
						  if(null!=ipToLocationVO){
							  if(contentVO.getLatitude()==0.0 && ipToLocationVO.getLatitude()!=0.0 ){
								  contentVO.setLatitude(ipToLocationVO.getLatitude());
							  }
							  if(contentVO.getLongitude()==0.0 && ipToLocationVO.getLongitude()!=0.0 ){
								  contentVO.setLongitude(ipToLocationVO.getLongitude());
							  }
							 /* if(StringUtils.isBlank(contentVO.getCountry()) && StringUtils.isNotBlank(ipToLocationVO.getCountry())){
								  contentVO.setCountry(ipToLocationVO.getCountry());
							  }*/
						  }
					  }
					 if(null!=contentVO.getCountry()){
						 contentVO.setCountry(countryDAO.getUserCountry(contentVO.getCountry()));
					 }else {
						 if(null!=ipToLocationVO){
							 if(StringUtils.isBlank(contentVO.getCountry()) && StringUtils.isNotBlank(ipToLocationVO.getCountry())){
								  contentVO.setCountry(ipToLocationVO.getCountry());
							  }else if(null!=userVO.getCountry()){
									 contentVO.setCountry(userVO.getCountry());
								 }else{
									 contentVO.setCountry(VoizdConstant.DEFAULT_COUNTRY);
								 }
						 }else{
							 contentVO.setCountry(VoizdConstant.DEFAULT_COUNTRY);
						 }
					 }
					
					 if (null!=userVO.getLanguage() && userVO.getLanguage().trim().length()>0){
						 contentVO.setLanguage(userVO.getLanguage());
					 }else {
						 if(null!=contentVO.getLanguage() && contentVO.getLanguage().trim().length()>0){
							 contentVO.setLanguage(countryDAO.getUserLanguage(contentVO.getLanguage()));
						 }else{
							 contentVO.setLanguage(VoizdConstant.DEFAULT_LANGUAGE);
						 }
					 }
					 if(StringUtils.isBlank(contentVO.getState())){
						 contentVO.setState(VoizdConstant.DEFAULT_STATE);
						}
					if(StringUtils.isBlank(contentVO.getCity())){
							contentVO.setCity(VoizdConstant.DEFAULT_CITY);
					}
					 
					 if(null!=contentVO.getTitle()){
							try {
								 contentVO.setTagCloud(tagCloudBO.createTagCloud(contentVO,status));
							} catch (TagCloudServiceException e) {
								logger.error("Error while insert tag data  :" + e.getLocalizedMessage(), e);
							}
					 }
				     Long contentId = contentDAO.createContent(contentVO);
				     if(null!=stationVOList.get(0).getCreatedDate()){
						 contentVO.setCreatedDate(stationVOList.get(0).getCreatedDate());
					 }
				     logger.debug("contentSearchVO is  : "+contentId+" , contentId.longValue() : "+contentId.longValue());
				  
				   
				     logger.debug("userVO.getStatus is "+userVO.getStatus()+" , status :: "+status);
				 if(contentId != null && contentId.longValue()>0 && (status == 1)) {
					try {	
						logger.debug("getTitle  is ::  "+contentVO.getTitle());
						ContentSearchVO contentSearchVO = SearchUtils.transformContentSearchVO(contentVO,userVO);
						logger.debug("contentSearchVO  :: "+contentSearchVO +"---"+contentVO.getFileIds());
						if(contentSearchVO != null){
							searchBO.indexContentMedia(contentSearchVO, false, VoizdConstant.CONTENT);
						}else{
							logger.error("contentSearchVO is null .Please contentVo");
						}
					} catch (SearchServiceException e) {
						logger.error("Error while insert station data in search :" + e.getLocalizedMessage(), e);
					}
					
							try {
								if (contentId != null && contentId.longValue() > 0) {
									
									MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
									TagCloudVO tagCloudVO = new TagCloudVO();
									tagCloudVO.setContentId(contentId);
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
				 if(contentId != null && contentId.longValue()>0){
					 TapClipVO  tapClipVO = ContentUtils.transformTapClipVO(contentVO,userVO,status);
					 sendJmsMessage(tapClipVO,TAP);
				 }
				
				String fileIdArr[] = contentVO.getFileIds().split("\\s*,\\s*");
				 int ordering = 1 ;
					for (String fileId:fileIdArr){
						try {
							MediaVO mediaVO = mediaService.getMediaInfo(fileId);
							if(null!=mediaVO){
								logger.debug("mediaVO.getDuration()--"+mediaVO.getDuration()+" size"+mediaVO.getSize());
								ContentMediaVO contentMediaVO = new ContentMediaVO();
								contentMediaVO.setContentId(contentId);
								contentMediaVO.setFileId(fileId);
								contentMediaVO.setMediaType(mediaVO.getMediaType());
								contentMediaVO.setExt(mediaVO.getMimeType());
								contentMediaVO.setOrdering(ordering);
								contentMediaVO.setDuration(mediaVO.getDuration());
								contentMediaVO.setSize(mediaVO.getSize());
								contentMediaVO.setStatus(VoizdConstant.MEDIA_ACTIVE_STATUS);
								mediaDAO.createContentMedia(contentMediaVO);
								ordering++;
							    if(VoizdConstant.AUDIO.equalsIgnoreCase(mediaVO.getMediaType()) && !VoizdConstant.MP3.equalsIgnoreCase(mediaVO.getMimeType())){
									contentMediaVO.setExt(VoizdConstant.MP3);
									 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							   }
							}
						} catch (MediaServiceFailedException e) {
							throw new ContentServiceException(ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION);
						}
					}
				
				/*StationVO stationVO = stationDAO.getStationInfo(contentVO.getStationId());
		
				logger.debug("stationVO.getStatus()--"+stationVO.getStatus());
				 //insert search station info	
				if (stationVO.getStatus() == VoizdConstant.DEFAULT_STATION_STATUS) {
					try {
						stationVO.setStatus(VoizdConstant.ACTIVE_STATION_STATUS);
						StationSearchVO stationSearchVO = SearchUtils.transformStationSearchVO(stationVO,userVO);
						searchBO.indexStationMedia(stationSearchVO, false, VoizdConstant.STATION);
					} catch (SearchServiceException e) {
						logger.error("Error while insert station data in search :" + e.getLocalizedMessage(), e);
					}
				}
				
				if(stationVO.getStatus() !=VoizdConstant.DELETE_STATION_STATUS){
				  stationDAO.updateStationStatus(contentVO.getStationId(),VoizdConstant.ACTIVE_STATION_STATUS);
				}*/
				
				// responseCode = 1 ;
				
				StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(contentVO.getStationId());
				if(null!=stationCounterVO){
					stationCounterVO.setNumberOfContent(stationCounterVO.getNumberOfContent()+1);
					stationCounterDAO.updateStationCounter(stationCounterVO);
				}
				
				contentCounterDAO.createContentCounter(contentId);
				
			}
					if(StringUtils.isNotBlank(contentVO.getAppIds())){
						 List<ShareVO> shareVOList = new ArrayList<ShareVO>();
						 
						String appIdArr[] = contentVO.getAppIds().split("\\s*,\\s*");	
						for (String appId:appIdArr){
							ShareVO  shareVO = getContentShareUrl(contentVO.getStationId(),contentVO.getContentId(),appId);
							shareVOList.add(shareVO);
						}
						if(shareVOList.size()>0)
						stationContentMap.put("share", shareVOList);
					logger.debug("shareVOList---"+shareVOList);
					}else{
						stationContentMap.put("status", "success");
					}
		 }	
			} catch (DataUpdateFailedException e) {
				logger.error("Error while createStationContent update :" + e.getLocalizedMessage(), e);
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_SERVICE_FAILED_EXCEPTION);
			}catch (DataAccessFailedException e) {
				logger.error("Error while createStationContent get:" + e.getLocalizedMessage(), e);
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
			}catch (Exception e) {
				logger.error("Error while createStationContent :" + e.getLocalizedMessage(), e);
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
			}	
		}else{
			throw new ContentServiceException(ErrorCodesEnum.INVALID_REQUEST_EXCEPTION);
		}
		
		return stationContentMap;
	}
	
	
	public HashMap<String, Object> getStationContents(Long stationId,Long contentId,Long userId, Byte status, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		HashMap<String, Object> stationContentMap = new HashMap<String, Object>();
		List<ContentDTO> stationContentDtoList =new ArrayList<ContentDTO>();
		boolean hasNext = false ;
		boolean hasPre = false ;
		//boolean isFirstRequest = false ;
		Long preContentId= 0l;
		Long nextContentId= 0l;
		List<ContentVO>  contentVOList=null;
		try {
			StationVO stationVO = stationDAO.getActiveStationInfo(stationId);
			if(null!=stationVO){
				 String imageResolution = null;
				// String thumbResolution = null;
				 String platform = VoizdConstant.DEFAULT_PLATFORM;
				 if(clientMap!=null){
					 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					 }else{
						 imageResolution = MediaUtil.getImageSize(null);
					 }
					/* if(null!=clientMap.get(ClientParamConstant.THUMBSIZE)){
						   thumbResolution = MediaUtil.getThumbImageSize(clientMap.get(ClientParamConstant.THUMBSIZE).toString());
				    }else{
				    	thumbResolution = MediaUtil.getThumbImageSize(null);
					 }*/
					 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
						 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
					 }
				 }else{
					 imageResolution = MediaUtil.getImageSize(null);
					 //thumbResolution = MediaUtil.getThumbImageSize(null);
				 }
				 
				 StationDTO stationDTO = new StationDTO();
				 stationDTO.setSId(stationVO.getStationId());
				 stationDTO.setSName(stationVO.getStationName());
				 stationDTO.setSDate(stationVO.getCreatedDate());
				 stationDTO.setCId(stationVO.getCreatorId());
				 StationMediaVO stationMediaVO = mediaDAO.getStationMediaByFileId(stationVO.getFileId());
				 if(null!=stationMediaVO){
					 mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
					 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
				 }else{
					 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(stationVO.getFileId());
					 if(null!=defaultMediaVO){
						 mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
						 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
					 }
				 }
				 if(stationVO.isAdult()){
					    stationDTO.setAdult(VoizdConstant.ADULT_STATUS);
				  }else{
						 stationDTO.setAdult(VoizdConstant.NOT_ADULT_STATUS);
				}
				 stationDTO.setDesc(stationVO.getDescription());
			
					UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
					 if(null!= userVO){
						 stationDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
					 }
					 
					StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationId);
					if(null!=stationCounterVO){
							stationDTO.setLikes(stationCounterVO.getLikes());
							stationDTO.setDislikes(stationCounterVO.getDislikes());
							stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
							stationDTO.setView(stationCounterVO.getView());
							stationDTO.setSComments(stationCounterVO.getComments());
							stationDTO.setFollower(stationCounterVO.getFollower());
							stationDTO.setShare(stationCounterVO.getShare());
					}
					if(stationVO.getCreatorId()!=userId){	
						if(userId>0){
							Byte subStatus = stationSubDAO.getStationTap(stationId,userId);
							logger.debug("getStationContents_subStatus--"+subStatus+" stationId="+stationId+" userId="+userId);
							if(null!=subStatus){
								if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
									stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
								}else{
									stationDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
								}
							}else{
								stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
							}
							Byte sLikeStatus  = stationLikeDAO.getUserStationLike(stationId, userId);
							if(null!=sLikeStatus){
								 if(VoizdConstant.STATION_UNLIKE_STATUS.equals(sLikeStatus)){
									 stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
								 }else{
									 stationDTO.setLikeValue(VoizdConstant.STATION_UNLIKE_STATUS);
								 }
							}else{
								stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
							}
						}else{
							stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
							stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
						}
						stationDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
						stationDTO.setLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_LIKE_URL));
						stationDTO.setSShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_SHARE_URL));
					}
			
					//true is asc order
					if(order && contentId>0){
						contentVOList = contentDAO.getContentListAsc(stationId,contentId,status,endLimit+1);
						 hasNext = true ;
					}else if(contentId>0){
					//desc order	
						contentVOList = contentDAO.getContentListDesc(stationId,contentId,status,endLimit+1);
						 hasPre = true ;
					}else{
						//first hit
						 contentVOList = contentDAO.getContentList(stationId, status, endLimit+1);
					    // isFirstRequest = true;
					}
					logger.debug("RECENT_Content_STATUS contentList="+contentVOList+" order="+order);
					if(null!= contentVOList && endLimit<contentVOList.size()){
						if(order){
							 contentVOList.remove(0);
							// hasPre = true ;
						}else{
							contentVOList.remove(contentVOList.size()-1);
						}
						hasNext = true ;
					}
				int cnt =0 ;
			 for(ContentVO contentVO:contentVOList){
				 if(hasPre && cnt==0){
					 preContentId= contentVO.getContentId();
				 } 
				ContentDTO contentDTO = new ContentDTO();
				contentDTO.setCId(contentVO.getCreatorId());
				contentDTO.setCTitle(contentVO.getTitle());
				contentDTO.setCTag(contentVO.getTag());
				contentDTO.setCntId(contentVO.getContentId());
				List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
				StringBuilder  cImgUrl = new StringBuilder();
			
				int loopCnt =0 ;
				
				 if(null!=stationMediaVO){
					 mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
					 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
				 }else{
					 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(stationVO.getFileId());
					 if(null!=defaultMediaVO){
						 mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
						 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
					 }
				 }
				
				for(ContentMediaVO contentMediaVO:contentMediaVOList){
					if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
						if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}else{
							 contentMediaVO.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}
						
						if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
							contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
						}
					}else{
						 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						 cImgUrl.append(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)).append(VoizdRelativeUrls.CONTENT_URL_SEPARATOR);
						
						if(loopCnt==0){
							// mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),thumbResolution);
						     contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
						}
						loopCnt++;
					}
				}
				if(StringUtils.isNotBlank(cImgUrl.toString())){
					contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(cImgUrl.substring(0, cImgUrl.length()-1)));
				}else{
					 stationMediaVO = mediaDAO.getStationMedia(stationId);
					 if(null!=stationMediaVO){
						 mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						// mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),stationMediaVO.getExt(),thumbResolution);
						
						 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
						 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
					 }else{
						 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FILE_NAME);
						 if(null!=defaultMediaVO){
							 mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
							// mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),thumbResolution);
							 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
							 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
							
						 }
					 }
				}
				ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
				logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
				if(null!=contentCounterVO){
					contentDTO.setCLikes(contentCounterVO.getLikes());
					contentDTO.setCFollower(contentCounterVO.getFollower());
					contentDTO.setCDislikes(contentCounterVO.getDislikes());
					contentDTO.setCShare(contentCounterVO.getShare());
					contentDTO.setComments(contentCounterVO.getComments());
					contentDTO.setCView(contentCounterVO.getView());
				}
				if(contentVO.getCreatorId()!=userId){
					if(userId>0){
						Byte subStatus = contentSubDAO.getContentTap(contentVO.getContentId(),userId);
						if(VoizdConstant.CONTENT_UNFOLLOW_STATUS.equals(subStatus)){
							contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
						}else{
							contentDTO.setCTapValue(VoizdConstant.CONTENT_UNFOLLOW_STATUS);
						}
						Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
						
						 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						 }else{
							 contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						 }
					
						
					}else{
						contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
						contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
					}
					contentDTO.setCTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_TAP_URL));
					contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
					contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
				}
				
				stationContentDtoList.add(contentDTO);
				cnt++;
				if(cnt==contentVOList.size()){
					nextContentId= contentVO.getContentId();
				}
			}
			
			 stationDTO.setContent(stationContentDtoList);
			
				logger.debug("getStationContents preContentId="+preContentId+" nextContentId="+nextContentId+"  "+hasNext+" "+hasPre);
				if(hasNext){
					StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.CONTENT_DETAIL_MORE_URL);
					relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextContentId);
					if(userId>0){
						relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
					}
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
					stationDTO.setCNext(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
					
				}
				if(hasPre){
					StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.CONTENT_DETAIL_MORE_URL);
					relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preContentId);
					if(userId>0){
						relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
					}
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
					stationDTO.setCPre(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
					
				}
				 stationContentMap.put("station", stationDTO);
		}else{
			logger.info("Station no active sID="+stationId);
		}
		} catch (DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		} catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		logger.debug("getStationContents stationContentMap="+preContentId+" nextContentId="+nextContentId+"  "+hasNext+" "+hasPre);
		
		
		return stationContentMap;
	}
	
	
	
	public HashMap<String, Object> getStationContentList(Long contentId,Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		
			if(VoizdConstant.RECENT_CONTENT_STATUS.equals(status)){
				contentMap = getStationRecentContentList(contentId,userId,status,startLimit,endLimit,order,clientMap);
			}else if(VoizdConstant.POPULAR_CONTENT_STATUS.equals(status)){
				contentMap = getStationPopularContentList(contentId,userId,status,startLimit,endLimit,order,clientMap);
			}else if(VoizdConstant.MY_TAP_CONTENT_STATUS.equals(status)){
				contentMap = getMyTappedContentList(contentId,userId,status,startLimit,endLimit,order,clientMap);
			}else if(VoizdConstant.MY_CONTENT_STATUS.equals(status)){
				logger.debug("getStationContentList userId="+userId+" visitorId="+visitorId);
				if(visitorId.equals(userId)){
					logger.debug("getStationContentList if=");
					contentMap = getMyStationContentList(contentId,visitorId,status,startLimit,endLimit,order,clientMap);
				}else{
					logger.debug("getStationContentList else");
					contentMap = getMyStationContentList(contentId,visitorId,status,startLimit,endLimit,order,clientMap);
				}
			}else{
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
			}
		
		
		return contentMap;
	}

	public HashMap<String, Object> getStationRecentContentList(Long contentId,Long userId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.RECENT ;
		boolean hasNext = false ;
		boolean hasPre = false ;
		Long preContentId= 0l;
		Long nextContentId= 0l;
		List<ContentVO> contentVOList = null ;
		int resultSize =0 ;
		try {
			 String imageResolution = null;
			// String thumbResolution = null;
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
				/* if(null!=clientMap.get(ClientParamConstant.THUMBSIZE)){
						thumbResolution = MediaUtil.getThumbImageSize(clientMap.get(ClientParamConstant.THUMBSIZE).toString());
				   }else{
					   thumbResolution = MediaUtil.getThumbImageSize(null);
					}*/
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
				 //thumbResolution = MediaUtil.getThumbImageSize(null);
			 }
			//true is asc order
			if(order && contentId>0){
				contentVOList = contentDAO.getRecentContentList(startLimit,endLimit+1);
				//contentVOList = contentDAO.getRecentContentListAsc(contentId,endLimit+1);
				 hasNext = true ;
			}else if(contentId>0){
			//desc order	
				contentVOList = contentDAO.getRecentContentList(startLimit,endLimit+1);
				//contentVOList = contentDAO.getRecentContentListDesc(contentId,endLimit+1);
				 hasPre = true ;
			}else{
				//first hit
				contentVOList = contentDAO.getRecentContentList(startLimit,endLimit+1);
			  
			}
			logger.debug("RECENT_Content_STATUS contentList="+contentVOList+" thumbResolution="+imageResolution);
			if(null!= contentVOList){
				if(endLimit<contentVOList.size()){
					if(order){
						 contentVOList.remove(0);
						 hasPre = true ;
					}else{
						contentVOList.remove(contentVOList.size()-1);
					}
					hasNext = true ;
				}
				resultSize= contentVOList.size();
			}
			 int cnt =0 ;
			 for(ContentVO contentVO:contentVOList){
				 if(hasPre && cnt==0){
					 preContentId= contentVO.getContentId();
				 } 
			    StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
				if(null!=stationVO){
				 
					ContentDTO contentDTO = new ContentDTO();
					contentDTO.setSId(contentVO.getStationId());
					contentDTO.setSName(stationVO.getStationName());
					contentDTO.setCId(contentVO.getCreatorId());
					contentDTO.setCTitle(contentVO.getTitle());
					contentDTO.setCTag(contentVO.getTag());
					contentDTO.setCntId(contentVO.getContentId());
					contentDTO.setLatitude(contentVO.getLatitude());
					contentDTO.setLongitude(contentVO.getLongitude());
					contentDTO.setWeblink(contentVO.getWeblink());
					UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
					 if(null!= userVO){
						 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
					 }
					 
					List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
					 if(stationVO.isAdult()){
						 contentDTO.setAdult(VoizdConstant.ADULT_STATUS);
					  }else{
						  contentDTO.setAdult(VoizdConstant.NOT_ADULT_STATUS);
					}
					StringBuilder  cImgUrl = new StringBuilder();
				
					int loopCnt =0 ;
					for(ContentMediaVO contentMediaVO:contentMediaVOList){
						if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
							logger.debug("RECENT_Content_STATUS audio="+contentMediaVO.getMediaType()+" platform="+platform +"ext =="+contentMediaVO.getExt());
							if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
								contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
							}else{
								 contentMediaVO.setExt(VoizdConstant.MP3);
								 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
								contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
							}
							
						 if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
								contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
						 }
						}else{
							if(loopCnt==0){
							  mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
							  //mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),thumbResolution);
							  cImgUrl.append(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)).append(VoizdRelativeUrls.CONTENT_URL_SEPARATOR);
							  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							}
							
							loopCnt++;
						}
					}
					if(StringUtils.isNotBlank(cImgUrl.toString())){
						contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(cImgUrl.substring(0, cImgUrl.length()-1)));
					}else{
						StationMediaVO stationMediaVO = mediaDAO.getStationMedia(contentVO.getStationId());
						 if(null!=stationMediaVO){
							  mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
							  //mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),stationMediaVO.getExt(),thumbResolution);
							  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
						 }else{
							 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FILE_NAME);
							 if(null!=defaultMediaVO){
							  mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
							  //mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),thumbResolution);
							  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
							  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
							
						     }
						 }
					}
					ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
					logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
					if(null!=contentCounterVO){
						contentDTO.setCLikes(contentCounterVO.getLikes());
						contentDTO.setCFollower(contentCounterVO.getFollower());
						contentDTO.setCDislikes(contentCounterVO.getDislikes());
						contentDTO.setCShare(contentCounterVO.getShare());
						contentDTO.setComments(contentCounterVO.getComments());
						contentDTO.setCView(contentCounterVO.getView());
					}
					if(contentVO.getCreatorId()!=userId){
					
						if(userId>0){
							Byte subStatus = contentSubDAO.getContentTap(contentVO.getContentId(),userId);
							if(null!=subStatus){
							if(VoizdConstant.CONTENT_UNFOLLOW_STATUS.equals(subStatus)){
									contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
								}else{
									contentDTO.setCTapValue(VoizdConstant.CONTENT_UNFOLLOW_STATUS);
								}
							}else{
								contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
							}
							Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
							if(null!=likeStatus){
								 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
										contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
								 }else{
									 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
								 }
							}else{
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							}
							
						}else{
							contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
							contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						}
						contentDTO.setCTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_TAP_URL));
						contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
						contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
					}
					
					contentDtoList.add(contentDTO);
					
					}
					cnt++;
					if(cnt==contentVOList.size()){
						nextContentId= contentVO.getContentId();
					}
				}
			 
		} catch (DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		} catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		
		logger.debug("preContentId="+preContentId+" nextContentId="+nextContentId);
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.RECENT_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextContentId);
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+ resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.RECENT_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preContentId);
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}
	
		contentMap.put(contentType, contentDtoList);
		
		 logger.debug(" getStations.contentMap ="+contentMap);
		return contentMap;
	}
	
	public HashMap<String, Object> getMyStationContentList(Long contentId,Long userId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.MY ;
		boolean hasNext = false ;
		boolean hasPre = false ;
	
		//Long preContentId= 0l;
		//Long nextContentId= 0l;
		List<ContentVO> contentVOList = null ;
		int resultSize =0 ;
		try {
			 String imageResolution = null;
			
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
			
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
				
			 }
			//true is asc order
			if(order){
				contentVOList = contentDAO.getMyContentList(userId,startLimit,endLimit+1);
			
				 hasNext = true ;
			}else{
				//desc order	
				if(startLimit>0){
					 hasPre = true ;
				}
				//first hit
				contentVOList = contentDAO.getMyContentList(userId,startLimit,endLimit+1);
			
			}
			logger.debug("MY contentList="+contentVOList);
			if(null!= contentVOList){
				if(endLimit<contentVOList.size()){
					if(order){
						contentVOList.remove(0);
						 hasPre = true ;
					}else{
						contentVOList.remove(contentVOList.size()-1);
						hasNext = true ;
					}
				}
				resultSize = contentVOList.size();
			}
			
			 for(ContentVO contentVO:contentVOList){
				
			    StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
			    logger.debug("stationVO -"+stationVO+ ""+userId+""+contentVO.getCreatorId());
			    if(null==stationVO && contentVO.getCreatorId().equals(userId)){
			    	stationVO = stationDAO.getUserStationInfo(userId, VoizdConstant.DEFAULT_STATION_STATUS);
			    }
				if(null!=stationVO){
				 
					ContentDTO contentDTO = new ContentDTO();
					contentDTO.setCId(contentVO.getCreatorId());
					contentDTO.setCTitle(contentVO.getTitle());
					if(StringUtils.isNotBlank(contentVO.getTag())){
						contentDTO.setCTag(contentVO.getTag());
					}
					contentDTO.setWeblink(contentVO.getWeblink());
					contentDTO.setCntId(contentVO.getContentId());
					contentDTO.setCDate(DateTimeUtils.format(contentVO.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
					//contentDTO.setCDate(contentVO.getCreatedDate());
					UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
					 if(null!= userVO){
						 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						// contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL));
						 if (null != userVO.getProfilePicFileId()) {
							 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
							 if(null!=mediaVO){
								 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
									 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
						 }
					 }
				
					List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
				
					for(ContentMediaVO contentMediaVO:contentMediaVOList){
						if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
							if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
								contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
							}else{
								 contentMediaVO.setExt(VoizdConstant.MP3);
								 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
								contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
							}
							if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
								contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
							}
						}else{
							logger.error("No content found for contentid="+contentVO.getContentId());
						}
					}
				
					ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
					logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
					if(null!=contentCounterVO){
						contentDTO.setCLikes(contentCounterVO.getLikes());
						contentDTO.setCShare(contentCounterVO.getShare());
						contentDTO.setComments(contentCounterVO.getComments());
						contentDTO.setCView(contentCounterVO.getView());
					}
					logger.debug("contentVO.getCreatorId()="+contentVO.getCreatorId()+" userId="+userId);
					contentDTO.setCDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.VOIZD_CONTENT_URL));
					/*if(contentVO.getCreatorId().equals(userId)){
							StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.CONTENT_URL);
							relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getContentId());
							relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getStationId());
							relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							contentDTO.setCDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
							logger.debug(contentDTO.getCDtlUrl());
							contentDTO.setCDeleteUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_DELETE_URL));	
					}*/
			
					if(userId>0){
						
						Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
						if(null!=likeStatus){
							 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
									contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							 }else{
								 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
							 }
						}else{
							contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						}
						
					}else{
						contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
					}
					
						contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
					    contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
						contentDtoList.add(contentDTO);
						
					}
				
					
				}
			 
		} catch (MediaServiceFailedException | DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
			
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_CONTENT_MORE_URL);
		
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+ resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			if(resultSize<endLimit){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
			}else{
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}
		contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
		contentMap.put(contentType, contentDtoList);
		
		 logger.debug(" getStations.contentDtoList ="+contentDtoList);
		return contentMap;
	}
	
	public HashMap<String, Object> getStationPopularContentList(Long contentId,Long userId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.POPULAR ;
		boolean hasNext = false ;
		boolean hasPre = false ;
		Long preContentId= 0l;
		Long nextContentId= 0l;
		int resultSize =0 ;
		try {
			 String imageResolution = null;
			// String thumbResolution = null;
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
				/* if(null!=clientMap.get(ClientParamConstant.THUMBSIZE)){
						thumbResolution = MediaUtil.getThumbImageSize(clientMap.get(ClientParamConstant.THUMBSIZE).toString());
				   }else{
					   thumbResolution = MediaUtil.getThumbImageSize(null);
					}*/
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
				// thumbResolution = MediaUtil.getThumbImageSize(null);
			 }
		List<ContentCounterVO> contentCounterVOList= null;
		//true is asc order
		if(order && contentId>0){
			contentCounterVOList = contentCounterDAO.getContentCounterList(startLimit,endLimit+1);
			//contentCounterVOList = contentCounterDAO.getContentCounterListAsc(contentId,endLimit+1);
			 hasNext = true ;
		}else if(contentId>0){
		//desc order
			contentCounterVOList = contentCounterDAO.getContentCounterList(startLimit,endLimit+1);
			//contentCounterVOList = contentCounterDAO.getContentCounterListDesc(contentId,endLimit+1);
			 hasPre = true ;
		}else{
			//first hit
			contentCounterVOList = contentCounterDAO.getContentCounterList(startLimit,endLimit+1);
		  
		}
		if(null!= contentCounterVOList ){
			
			if(endLimit<contentCounterVOList.size()){
				if(order){
					contentCounterVOList.remove(0);
					 //hasPre = true ;
				}else{
					contentCounterVOList.remove(contentCounterVOList.size()-1);
					hasNext = true ;
				}
				
			}
			resultSize = contentCounterVOList.size() ;
		}
		logger.debug("getStationPopularContentList----"+startLimit+" "+endLimit);
		 int cnt =0 ;
		for(ContentCounterVO contentCounterVO :contentCounterVOList){
			 if(hasPre && cnt==0){
				 preContentId= contentCounterVO.getContentId();
			 } 
			ContentVO contentVO = contentDAO.getContentById(contentCounterVO.getContentId(),VoizdConstant.CONTENT_ACTIVE_STATUS);
			if(null!=contentVO){
			  StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
			 if(null!=stationVO){ 
				ContentDTO contentDTO = new ContentDTO();
				contentDTO.setSId(contentVO.getStationId());
				contentDTO.setSName(stationVO.getStationName());
				contentDTO.setCId(contentVO.getCreatorId());
				contentDTO.setCTitle(contentVO.getTitle());
				contentDTO.setCTag(contentVO.getTag());
				contentDTO.setCntId(contentVO.getContentId());
				contentDTO.setWeblink(contentVO.getWeblink());
				 if(stationVO.isAdult()){
					 contentDTO.setAdult(VoizdConstant.ADULT_STATUS);
				  }else{
					  contentDTO.setAdult(VoizdConstant.NOT_ADULT_STATUS);
				}
				 UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
				 if(null!= userVO){
					 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
				 }
				List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
				StringBuilder  cImgUrl = new StringBuilder();
			
				int loopCnt =0 ;
				for(ContentMediaVO contentMediaVO:contentMediaVOList){
					if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
						if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}else{
							 contentMediaVO.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}
						if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
							contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
						}
					}else{
						 
						if(loopCnt==0){ 
							mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						  // mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,thumbResolution);
						  cImgUrl.append(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)).append(VoizdRelativeUrls.CONTENT_URL_SEPARATOR);
						  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
						  }
						loopCnt++;
					}
				}
				if(StringUtils.isNotBlank(cImgUrl.toString())){
					contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(cImgUrl.substring(0, cImgUrl.length()-1)));
				}else{
					StationMediaVO stationMediaVO = mediaDAO.getStationMedia(contentVO.getStationId());
					 if(null!=stationMediaVO){
						  mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						//  mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,thumbResolution);
						  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
						  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
					 }else{
						 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FILE_NAME);
						 if(null!=defaultMediaVO){
						  mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
						//  mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),thumbResolution);
						  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
						  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
						
					     }
					 }
				}
				
					contentDTO.setCLikes(contentCounterVO.getLikes());
					contentDTO.setCFollower(contentCounterVO.getFollower());
					contentDTO.setCDislikes(contentCounterVO.getDislikes());
					contentDTO.setCShare(contentCounterVO.getShare());
					contentDTO.setComments(contentCounterVO.getComments());
					contentDTO.setCView(contentCounterVO.getView());
					if(contentVO.getCreatorId()!=userId){
						if(userId>0){
							Byte subStatus = contentSubDAO.getContentTap(contentVO.getContentId(),userId);
							if(null!=subStatus){
							if(VoizdConstant.CONTENT_UNFOLLOW_STATUS.equals(subStatus)){
									contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
								}else{
									contentDTO.setCTapValue(VoizdConstant.CONTENT_UNFOLLOW_STATUS);
								}
							}else{
								contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
							}
							Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
							if(null!=likeStatus){
								 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
										contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
								 }else{
									 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
								 }
							}else{
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							}
							
						}else{
							contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
							contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
						}
						contentDTO.setCTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_TAP_URL));
						contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
						contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
					}
			
				contentDtoList.add(contentDTO);
				
				
			}	
			}
			cnt++;
			if(cnt==contentCounterVOList.size()){
				nextContentId=contentCounterVO.getContentId();
			}
			
		}
		}catch (DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		 logger.debug(" getStations.nextContentId ="+nextContentId+" preContentId="+preContentId);
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.POPULAR_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextContentId);
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+ resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.POPULAR_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preContentId);
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}
		contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
		contentMap.put(contentType, contentDtoList);
		return contentMap;
	}
	

	public HashMap<String, Object> getMyTappedContentList(Long contentId,Long userId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.MYTAPPED ;
		boolean hasNext = false ;
		boolean hasPre = false ;
	
		Long preContentId= 0l;
		Long nextContentId= 0l;
		int resultSize =0 ;
		if(null!=userId && userId>0){
			
		try {
			 String imageResolution = null;
			// String thumbResolution = null;
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
				/* if(null!=clientMap.get(ClientParamConstant.THUMBSIZE)){
						thumbResolution = MediaUtil.getThumbImageSize(clientMap.get(ClientParamConstant.THUMBSIZE).toString());
				   }else{
					   thumbResolution = MediaUtil.getThumbImageSize(null);
					}*/
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
				// thumbResolution = MediaUtil.getThumbImageSize(null);
			 }
			contentMap.put(VoizdConstant.STREAM_SHARE_URL_KEY, VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STREAM_SHARE_URL));
			List<ContentFollowerVO> contentFollowerVOList= null;
			Long id = contentId ;
			//true is asc order
			if(order && id>0){
				contentFollowerVOList = contentSubDAO.getTappedContentList(userId,startLimit,endLimit+1);
				//contentFollowerVOList = contentSubDAO.getTappedContentListAsc(id,userId,endLimit+1);
				 hasNext = true ;
			}else if(id>0){
			//desc order	
				contentFollowerVOList = contentSubDAO.getTappedContentList(userId,startLimit,endLimit+1);
				//contentFollowerVOList = contentSubDAO.getTappedContentListDesc(id,userId,endLimit+1);
				 hasPre = true ;
			}else{
				//first hit
				contentFollowerVOList = contentSubDAO.getTappedContentList(userId,startLimit,endLimit+1);
			 
			}
			if(null!= contentFollowerVOList ){
				if(endLimit<contentFollowerVOList.size()){
					if(order){
						contentFollowerVOList.remove(0);
						 hasPre = true ;
					}else{
						contentFollowerVOList.remove(contentFollowerVOList.size()-1);
						hasNext = true ;
					}
				}
				
				resultSize = contentFollowerVOList.size();
			}
			 int cnt =0 ;
			 for(ContentFollowerVO contentFollowerVO:contentFollowerVOList){
				 if(hasPre && cnt==0){
					 preContentId= contentFollowerVO.getId();
				 }

					ContentVO contentVO = contentDAO.getContentById(contentFollowerVO.getContentId(),VoizdConstant.CONTENT_ACTIVE_STATUS);
					if(null!=contentVO){
					  StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
					 if(null!=stationVO){ 
						ContentDTO contentDTO = new ContentDTO();
						contentDTO.setSId(contentVO.getStationId());
						contentDTO.setSName(stationVO.getStationName());
						contentDTO.setCId(contentVO.getCreatorId());
						contentDTO.setCTitle(contentVO.getTitle());
						contentDTO.setCTag(contentVO.getTag());
						contentDTO.setCntId(contentVO.getContentId());
						contentDTO.setWeblink(contentVO.getWeblink());
						UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
						 if(null!= userVO){
							 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 }
						 if(stationVO.isAdult()){
							 contentDTO.setAdult(VoizdConstant.ADULT_STATUS);
						  }else{
							  contentDTO.setAdult(VoizdConstant.NOT_ADULT_STATUS);
						}
						List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
						StringBuilder  cImgUrl = new StringBuilder();
						
						int loopCnt =0 ;
						for(ContentMediaVO contentMediaVO:contentMediaVOList){
							if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
								if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}else{
									 contentMediaVO.setExt(VoizdConstant.MP3);
									 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}
								if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
									contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
								}
							}else{
								 
								if(loopCnt==0){
									mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
									  // mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,thumbResolution);
									  cImgUrl.append(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)).append(VoizdRelativeUrls.CONTENT_URL_SEPARATOR);
									  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									
								}
								loopCnt++;
							}
						}
						if(StringUtils.isNotBlank(cImgUrl.toString())){
							contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(cImgUrl.substring(0, cImgUrl.length()-1)));
						}else{
							StationMediaVO stationMediaVO = mediaDAO.getStationMedia(contentVO.getStationId());
							 if(null!=stationMediaVO){
								  mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								  //mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,thumbResolution);
								  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							 }else{
								 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FILE_NAME);
								 if(null!=defaultMediaVO){
								  mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
								 // mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),thumbResolution);
								  contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
								  contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false)));
								
							     }
							 }
						}
						ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
						logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
						if(null!=contentCounterVO){
							contentDTO.setCLikes(contentCounterVO.getLikes());
							contentDTO.setCFollower(contentCounterVO.getFollower());
							contentDTO.setCDislikes(contentCounterVO.getDislikes());
							contentDTO.setCShare(contentCounterVO.getShare());
							contentDTO.setComments(contentCounterVO.getComments());
							contentDTO.setCView(contentCounterVO.getView());
						}
						if(contentVO.getCreatorId()!=userId){
							if(userId>0){
								Byte subStatus = contentSubDAO.getContentTap(contentVO.getContentId(),userId);
								if(null!=subStatus){
								if(VoizdConstant.CONTENT_UNFOLLOW_STATUS.equals(subStatus)){
										contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
									}else{
										contentDTO.setCTapValue(VoizdConstant.CONTENT_UNFOLLOW_STATUS);
									}
								}else{
									contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
								}
								Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
								if(null!=likeStatus){
									 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
											contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
									 }else{
										 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
									 }
								}else{
									contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
								}
								
							}else{
								contentDTO.setCTapValue(VoizdConstant.CONTENT_FOLLOW_STATUS);
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							}
							contentDTO.setCTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_TAP_URL));
							contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
							contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
						}
						contentDtoList.add(contentDTO);
						
						
					}	
					}
					cnt++;
					if(cnt==contentFollowerVOList.size()){
						nextContentId=contentFollowerVO.getId();
					}
			 }
			
		}catch (DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MYTAPPED_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextContentId);
			if(userId>0){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MYTAPPED_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preContentId);
			if(userId>0){
			 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}
			contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
			contentMap.put(contentType, contentDtoList);
		}
		
		return contentMap;
	}

	@Override
	public void updateStationContent(String tag,String fileIds,Long contentId,Long userId) throws ContentServiceException {
		try{
			ContentVO contentVO =null;
			try {
			
				contentVO = contentDAO.getContentByContentId(contentId);
				if(contentVO==null){
					throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION);
				}
				if(contentVO.getCreatorId().longValue() != userId.longValue()){
				
					throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_INVALID_USER);
				}
			} catch (DataAccessFailedException e) {
				logger.error("Exception while "+e.getLocalizedMessage(),e);
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION);
			}
			if(tag != null){
				contentDAO.updateContentTag(tag,contentId);	
				contentVO.setTag(tag);
				ContentSearchVO contentSearchVO = SearchUtils.transformContentSearchVO(contentVO,userService.getUserProfile(contentVO.getCreatorId()));
				searchBO.indexUpdateContentMedia(contentSearchVO,false,VoizdConstant.CONTENT);
			}
			if(fileIds != null ){
				mediaDAO.updateContentMedia(contentId);
				List<String> files = StationUtils.getListOfFileId(fileIds);
				  int order=1;
					for (String fileId : files) {
						try {
							MediaVO mediaVO = mediaService.getMediaInfo(fileId);
							mediaDAO.createContentMedia(StationUtils.transformContentMedia(mediaVO,contentId,order));
							order++;
						} catch (Exception e) {
							logger.error("Exception while getMediaInfo "+e.getLocalizedMessage(),e);
							throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION);
						}
					
					}
			}
		
		} catch (DataUpdateFailedException e) {
			logger.error("Exception updateStationContent() while "+e.getLocalizedMessage(),e);
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION);
		}catch (Exception e) {
			logger.error("Exception updateStationContent() while "+e.getLocalizedMessage(),e);
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION);
		}
	}


	@Override
	public void deleteStationContent(Long stationId,Long contentId,Long userId) throws ContentServiceException {
		try{
			logger.debug("deleteStationContent() ::  stationId :: "+stationId);
			if(null==stationId|| stationId==0){
				 List<StationVO> stationVOList = stationDAO.getMyStationList(userId,VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
				if(null!=stationVOList && stationVOList.size()>0 ){
				 stationId = stationVOList.get(0).getStationId();
				 logger.debug("deleteStationContent()1 ::  stationId :: "+stationId);
				 logger.debug("userId :: "+userId+" stationId :: "+stationVOList.get(0).getCreatorId());
				if(userId.longValue() != stationVOList.get(0).getCreatorId().longValue() ){
					  throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_INVALID_USER);
				  }
				}
			}
			logger.debug("deleteStationContent()2 ::  stationId :: "+stationId);
			if(null==stationId|| stationId==0){
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION);
			}
				int status = contentDAO.updateContentStatusByContentId(contentId,stationId,userId,VoizdConstant.CONTENT_DELETED_STATUS);
				logger.debug("deleteStationContent() ::  status :: "+status);
				if(status==1){
					TapClipVO  tapClipVO = new TapClipVO();
					tapClipVO.setStationId(stationId);
					tapClipVO.setContentId(contentId);
					tapClipVO.setCreatorId(userId);
					tapClipVO.setStatus(VoizdConstant.CONTENT_DELETED_STATUS);
				    sendJmsMessage(tapClipVO,DELETE);
				    
					StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationId);
					Long contentCount = stationCounterVO.getNumberOfContent();
					logger.debug("deleteStationContent() ::  contentCount :: "+contentCount);
				   if(contentCount.longValue() >0) {
						stationCounterVO.setNumberOfContent(contentCount -1);
						stationCounterDAO.updateStationCounter(stationCounterVO);	
						/*if((contentCount-1) == 0){
							stationDAO.updateStationStatus(stationId,VoizdConstant.STATION_DEACTIVE_STATUS);
							try {
							searchBO.indexDeleteStationMedia(stationId, false, VoizdConstant.STATION);
							} catch (SearchServiceException e) {
								logger.error("Error while indexDeleteContentMedia "+e.getLocalizedMessage(),e);
							}
						}*/
					}

					try {
						searchBO.indexDeleteContentMedia(contentId, false,VoizdConstant.CONTENT);
					} catch (SearchServiceException e) {
						logger.error("Error while indexDeleteContentMedia "+e.getLocalizedMessage(),e);
					}
				}else{
					logger.info("deleteStationContent() ::  contentId is already deleted :: "+contentId);
				}
			
		}  catch (DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);
		}catch (DataUpdateFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION);
		}
	}
	public HashMap<String, Object> getStationContentDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
		
		String contentType= VoizdConstant.CONTENT ;
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		List<ContentDTO> stationContentDtoList =new ArrayList<ContentDTO>();
		try {
				ContentDTO contentDTO = getContentDTO(stationId,contentId,userId,clientMap);
				logger.debug("contentDTO="+contentDTO+" contentDTO="+contentDTO.getCTitle());
				stationContentDtoList.add(contentDTO);
				contentMap.put(contentType,stationContentDtoList);
			
		} catch (ContentServiceException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
		}
		logger.debug("contentMap="+contentMap);
		return contentMap;
	}
	
	public ContentDTO getContentDTO(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
			ContentDTO contentDTO = new ContentDTO();
		try {
			logger.debug("contentId="+contentId+" stationId="+stationId);
			//ContentVO contentVO = contentDAO.getContent(contentId,stationId);
			ContentVO contentVO = contentDAO.getContentById(contentId,VoizdConstant.CONTENT_ACTIVE_STATUS);
			logger.debug("contentVO="+contentVO);
			if(null!=contentVO){
				 String imageResolution = null;
				
				 if(clientMap!=null){
					 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
						  imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						  imageResolution = MediaUtil.getImageSize(null);
					}	
				 }else{
					 imageResolution = MediaUtil.getImageSize(null);
				 }
				
				contentDTO.setSId(contentVO.getStationId());
				contentDTO.setCId(contentVO.getCreatorId());
				contentDTO.setCTitle(contentVO.getTitle());
				contentDTO.setCTag(contentVO.getTag());
				contentDTO.setCntId(contentVO.getContentId());
				contentDTO.setWeblink(contentVO.getWeblink());
				contentDTO.setCDate(DateTimeUtils.format(contentVO.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
				List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMediaList(contentVO.getContentId(),VoizdConstant.AUDIO);
				for(ContentMediaVO contentMediaVO:contentMediaVOList){
					if(VoizdConstant.AUDIO.equalsIgnoreCase(contentMediaVO.getMediaType())){
						/*if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}else{*/
							 contentMediaVO.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						//}
						if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
							contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
						}
					}else{
						logger.error("There is no audio attach with getContentId="+contentVO.getContentId());
					}
				}
				
				 UserVO userVO = userService.getUserProfile(contentVO.getCreatorId());
				 if(null!= userVO){
					
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
					 contentDTO.setAddress(userVO.getAddress());
					 contentDTO.setLanguage(userVO.getLanguage());
					 contentDTO.setUserDescription(userVO.getUserDescription());
					 contentDTO.setWebSite(userVO.getWebSite());
					 contentDTO.setLocation(userVO.getLocation());
					 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
					
					 if (null != userVO.getProfilePicFileId()) {
						 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
						 if(null!=mediaVO){
							 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
							 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
								 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
							}
					 }
				 }
				 	ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
					logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
					if(null!=contentCounterVO){
						contentDTO.setCLikes(contentCounterVO.getLikes());
						contentDTO.setCShare(contentCounterVO.getShare());
						contentDTO.setComments(contentCounterVO.getComments());
						contentDTO.setCView(contentCounterVO.getView());
						contentDTO.setCAmplify(contentCounterVO.getAmplify());
						if(contentCounterVO.getAmplify()>0){
							 StringBuilder amplifierUrl= new StringBuilder(VoizdRelativeUrls.AMPLIFIER_URL);
							 amplifierUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getContentId());
							 contentDTO.setAmplifierUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),amplifierUrl.toString()));
						}
					}
					List<AmplifyInfoVO> amplifierCounterVOList = null;
					amplifierCounterVOList = amplifyDAO.getAmplifierList(contentVO.getContentId(),0,1);
					 for(AmplifyInfoVO amplifyInfoVO:amplifierCounterVOList){
						 contentDTO.setAmplifierId(amplifyInfoVO.getAmplifierId());
							userVO = userService.getUserProfile(amplifyInfoVO.getAmplifierId());
							if (null != userVO) {
								contentDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
								/*if (amplifyInfoVO.getAmplifierId().equals(userId)) {
									 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
									 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
								}else{*/
									 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
									 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
									 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(amplifyInfoVO.getAmplifierId());
									 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
								//}
							}
					 }
					/*if(contentVO.getAmplifierId() != null && contentVO.getAmplifierId()>0){
						contentDTO.setAmplifierId(contentVO.getAmplifierId());
						userVO = userService.getUserProfile(tapClipVO.getAmplifierId());
						if (null != userVO) {
							contentDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
							if (tapClipVO.getAmplifierId().equals(userId)) {
								 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
								 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
							}else{
							 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
							 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getAmplifierId());
							 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
							}
						}
						logger.debug("contentVO.tapClipVO.getAmplifierId()()=" + tapClipVO.getAmplifierId());
					}
					*/
				
			}
			
		} catch (MediaServiceFailedException | UserServiceFailedException |DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
		}
		logger.debug("contentDTO="+contentDTO);
		return contentDTO;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getVoizDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException{
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		ContentDetailDTO contentDetailDTO = new ContentDetailDTO();
		HashMap<String, Object> returnMap = null;
		 List<CommentInfoDTO> commentList= new ArrayList<CommentInfoDTO>();
		 List<AmplifierDTO> amplifyList = new ArrayList<AmplifierDTO>();
		 List<FollowerDTO> followerList= new ArrayList<FollowerDTO>();
		
		try {
				ContentDTO contentDTO = getContentDTO(stationId,contentId,userId,clientMap);
				
				try {
					BeanUtils.copyProperties(contentDetailDTO,contentDTO);
					logger.debug(contentDTO.getCAmplify()+"contentDTO.getComments()="+contentDTO.getComments()+"--"+contentDTO.getCLikes());
					if(contentDTO.getCAmplify()>0){
						AmplifierDTO amplifierDTO = new AmplifierDTO();
						BeanUtils.copyProperties(amplifierDTO,contentDTO);
						try {
							returnMap = amplifyActionBO.getAmplifierList(contentId, userId, 0, 1,false,clientMap);
							logger.debug("amplifyActionBOreturnMap()="+returnMap);
							if(null!=returnMap && returnMap.get(VoizdConstant.AMPLIFIER)!=null){
								amplifyList = (List<AmplifierDTO>) returnMap.get(VoizdConstant.AMPLIFIER);
								logger.debug("amplifyList()="+amplifyList);
								contentDetailDTO.setAmplify(amplifyList);
							}
						} catch (AmplifyServiceFailedException e) {
							e.printStackTrace();
						}
						/*amplifyList.add(amplifierDTO);
						contentDetailDTO.setAmplify(amplifyList);*/
					}
					if(contentDTO.getComments()>0){
						CommentDTO commentDTO = new CommentDTO();
						BeanUtils.copyProperties(commentDTO,contentDTO);
						try {
							returnMap = commentBO.getCommentList(contentId,0, 1, clientMap,userId);
							logger.debug("CommentDTO.returnMap()="+returnMap);
							if(null!=returnMap && returnMap.get("CommentList")!=null){
								commentDTO = (CommentDTO) returnMap.get("CommentList");
								logger.debug("commentList()="+commentDTO.getComment());
								List<CommentInfoDTO> commentInfoDTOList = commentDTO.getComment();
								logger.debug("commentInfoDTOList()="+commentInfoDTOList);
								logger.debug("CommentInfoDTO()="+commentInfoDTOList.get(0));
								commentList.add(commentInfoDTOList.get(0));
								contentDetailDTO.setComment(commentList);
							}
						} catch (CommentServiceException e) {
							
							e.printStackTrace();
						}
						//commentList.add(commentDTO);
						//contentDetailDTO.setComment(commentList);
					}
					/*if(contentDTO.getCFollower()>0){
						FollowerDTO followerDTO = new FollowerDTO();
						BeanUtils.copyProperties(followerDTO,contentDTO);
						followerList.add(followerDTO);
						contentDetailDTO.setFollower(followerList);
					}*/
					logger.debug("contentDTO="+contentDTO);
				}catch (IllegalAccessException e) {
					logger.error("Exception in transforming contentDTO error  : " + e.getMessage());
				} catch (InvocationTargetException e) {
					logger.error("Exception in transforming contentDTO error  : " + e.getMessage());
				}
				
		} catch (ContentServiceException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
		}
		logger.debug("contentDetailDTO="+contentDetailDTO);
		dataMap.put("VoizDetail",contentDetailDTO);
		
		return dataMap;
	}
	
	public HashMap<String, Object> getContentShareUrl(Long stationId,Long contentId,Byte appId,Long userId)throws ContentServiceException {

		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
			try {
				ContentVO contentVO = contentDAO.getContentById(contentId,VoizdConstant.CONTENT_ACTIVE_STATUS);
				if(null!=contentVO){
					String thumbUrl =null;
					String userName =null;
					logger.debug("getContentShareUrl 1:: contentId :: "+contentVO.getContentId()+" , CreatorId :: "+contentVO.getCreatorId()+" userId ="+userId);
				/*	List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMediaList(contentVO.getContentId(),VoizdConstant.IMAGE);
					
					if(null!=contentMediaVOList && contentMediaVOList.size()>0){
						ContentMediaVO contentMediaVO = contentMediaVOList.get(0);
						 thumbUrl=CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,false);
						 
					}else{
						StationMediaVO stationMediaVO = mediaDAO.getStationMedia(contentVO.getStationId());
						 if(null!=stationMediaVO){
							 thumbUrl=CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,false);
							 
						 }else{
							DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FILE_NAME);
							 if(null!=defaultMediaVO){
								 thumbUrl=CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),false);
							 }
						 }
					}
					*/
					 UserVO userVO = userService.getUserProfile(contentVO.getCreatorId());
					 if(null!= userVO){
						 userName= UserUtils.displayName(userVO.getFirstName(), userVO.getLastName());
						 DefaultMediaVO defaultMediaVO = null;
						 defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_SHARE);
						 if(null!=defaultMediaVO){
							 thumbUrl=CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),false);
						}else{
							 if (null != userVO.getProfilePicFileId()) {
								 	MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
									 thumbUrl=CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,false); 
								}
						}
					 }
					 StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
					 if(null!=stationVO){
						 dataMap.put(VoizdConstant.SHARE_URL, VoizdUrlUtils.getClipShareUrl(userName,contentVO.getTitle(),contentVO.getStationId(),contentId,stationVO.getCreatorId(),appId));	
						 if(VoizdConstant.TWITTER_APPID.equals(appId)){
							 dataMap.put(VoizdConstant.MESSAGE,"Listening to '"+contentVO.getTitle()+"' #VOIZD By "+userName);
						 }else{
							 dataMap.put(VoizdConstant.MESSAGE,"Listening to '"+contentVO.getTitle()+"' VOIZD By "+userName);
						 }
						 ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
						 if(null!=contentCounterVO){
							 contentCounterVO.setShare(contentCounterVO.getShare()+1);
							 contentCounterDAO.updateContentCounter(contentCounterVO);
							 	logger.debug("getContentShareUrl :: contentId :: "+contentVO.getContentId()+" , CreatorId :: "+contentVO.getCreatorId()+" userId ="+userId);
							 	if(!contentVO.getCreatorId().equals(userId)){
									UserPushInfo pushInfo = userDAO.getUserPushInfo(contentVO.getCreatorId());
									if(pushInfo != null && "yes".equalsIgnoreCase(pushInfo.getSendNotif())){
										logger.debug("getContentShareUrl :: userId :: "+userId);
										UserInfo userInfo = userDAO.getUserProfile(userId);
										String message = PushMessageUtil.getPushMessage(contentVO.getTitle(),"shared",userInfo.getFirstName(),userInfo.getLastName());
										sendJmsPushMessage(userId,pushInfo.getDeviceKey(),message,pushInfo.getNotifType());
									}
								}else{
							 		logger.debug("getContentShareUrl ::, CreatorId :: "+contentVO.getCreatorId()+" userId="+userId);
							 	}
							 
						 }

					 }
					
					dataMap.put(VoizdConstant.THUMB_URL,thumbUrl);
								
				}
			}catch (MediaServiceFailedException | UserServiceFailedException | DataUpdateFailedException |DataAccessFailedException e) {
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
			}
			return dataMap;
	
		
	}
	
	public ShareVO getContentShareUrl(Long stationId,Long contentId,String appId)throws ContentServiceException {

		ShareVO shareVO = new ShareVO();
		
			try {
				ContentVO contentVO = contentDAO.getContentById(contentId,VoizdConstant.CONTENT_ACTIVE_STATUS);
				if(null!=contentVO){
					String thumbUrl =null;
					String userName =null;
					
					 UserVO userVO = userService.getUserProfile(contentVO.getCreatorId());
					 if(null!= userVO){
						 userName= UserUtils.displayName(userVO.getFirstName(), userVO.getLastName());
						 if (null != userVO.getProfilePicFileId()) {
							 	MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
								 thumbUrl=CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,false);
								 
							}else{
								DefaultMediaVO defaultMediaVO = null;
								if (userVO.getGender() == 1) {
									defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
								} else {
									defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
								}
								if(null!=defaultMediaVO){
										 thumbUrl=CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),false);
									
								 }
							}
					 }
					 List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
					 
					 StationVO stationVO = stationDAO.getActiveStationInfo(contentVO.getStationId());
					 if(null!=stationVO){
						 
						 shareVO.setShareurl(VoizdUrlUtils.getClipShareUrl(userName,contentVO.getTitle(),contentVO.getStationId(),contentId,stationVO.getCreatorId(),Byte.parseByte(appId)));	
						 shareVO.setMessage("Listening to "+contentVO.getTitle()+" VOIZD By "+userName);
						 ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
						 if(null!=contentCounterVO){
							 contentCounterVO.setShare(contentCounterVO.getShare()+1);
							 contentCounterDAO.updateContentCounter(contentCounterVO);
						 }

					 }
					
					 shareVO.setThumburl(thumbUrl);
					 shareVO.setAppId(Byte.parseByte(appId));
								
				}
			}catch (MediaServiceFailedException | UserServiceFailedException | DataUpdateFailedException |DataAccessFailedException e) {
				throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
			}
			return shareVO;
	
		
	}
	
	public HashMap<String, Object> getMyBookMarkContentList(Long userId,Long visitorId,  Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {

		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.MYBOOKMARK ;
		boolean hasNext = false ;
		boolean hasPre = false ;
	
		Long preContentId= 0l;
		Long nextContentId= 0l;
		int resultSize =0 ;
		if(null!=userId && userId>0){
			
		try {
			 String imageResolution = null;
		
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
			 }
		
			List<ContentLikeVO> ContentLikeVOList= null;
			//true is asc order
			if(order ){
				ContentLikeVOList = contentLikeDAO.getLikedContentList(userId,startLimit,endLimit+1);
				 hasNext = true ;
			}else{
				if(startLimit>0){
					//desc order	
					 hasPre = true ;
				}
				ContentLikeVOList = contentLikeDAO.getLikedContentList(userId,startLimit,endLimit+1);
			}
			if(null!= ContentLikeVOList ){
				if(endLimit<ContentLikeVOList.size()){
					if(order){
						ContentLikeVOList.remove(0);
						 hasPre = true ;
					}else{
						ContentLikeVOList.remove(ContentLikeVOList.size()-1);
						hasNext = true ;
					}
				}
				
				resultSize = ContentLikeVOList.size();
			}
			 int cnt =0 ;
			 for(ContentLikeVO ContentLikeVO:ContentLikeVOList){
			
					ContentVO contentVO = contentDAO.getContentById(ContentLikeVO.getContentId(),VoizdConstant.CONTENT_ACTIVE_STATUS);
					if(null!=contentVO){
					  StationVO stationVO = stationDAO.getStationDetail(contentVO.getStationId(),contentVO.getCreatorId());
					 if(null!=stationVO){ 
						ContentDTO contentDTO = new ContentDTO();
						//contentDTO.setSId(contentVO.getStationId());
						//contentDTO.setSName(stationVO.getStationName());
						contentDTO.setCId(contentVO.getCreatorId());
						contentDTO.setCTitle(contentVO.getTitle());
						contentDTO.setCTag(contentVO.getTag());
						contentDTO.setCDate(DateTimeUtils.format(contentVO.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
						//contentDTO.setCDate(contentVO.getCreatedDate());
						contentDTO.setWeblink(contentVO.getWeblink());
						contentDTO.setCntId(contentVO.getContentId());
						UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
						 if(null!= userVO){
							 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
							 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getCreatorId());
							 contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
							 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
							 if (null != userVO.getProfilePicFileId()) {
								 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
								 if(null!=mediaVO){
									 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
									 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
										 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
										 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									}
							 }
						 }
						
						List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(contentVO.getContentId());
						for(ContentMediaVO contentMediaVO:contentMediaVOList){
							if(VoizdConstant.AUDIO.equalsIgnoreCase(contentMediaVO.getMediaType())){
								if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}else{
									 contentMediaVO.setExt(VoizdConstant.MP3);
									 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}
								if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
									contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
								}
							}else{
								logger.error("There is no audio attach with getContentId="+contentVO.getContentId());
							}
						}
						
						ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentVO.getContentId());
						logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentVO.getContentId());
						if(null!=contentCounterVO){
							contentDTO.setCLikes(contentCounterVO.getLikes());
							contentDTO.setCShare(contentCounterVO.getShare());
							contentDTO.setComments(contentCounterVO.getComments());
							contentDTO.setCView(contentCounterVO.getView());
							contentDTO.setCAmplify(contentCounterVO.getAmplify());
							if(contentCounterVO.getAmplify()>0){
								 StringBuilder amplifierUrl= new StringBuilder(VoizdRelativeUrls.AMPLIFIER_URL);
								 amplifierUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getContentId());
								 contentDTO.setAmplifierUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),amplifierUrl.toString()));
							}
							if(contentCounterVO.getComments()>0){
								 StringBuilder commentUrl= new StringBuilder(VoizdRelativeUrls.COMMENT_URL);
								 commentUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentVO.getContentId());
								 contentDTO.setCommentUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),commentUrl.toString()));
								 logger.debug("contentCounterVO.contentDTO=" + commentUrl);
							}
							
						}
					
						contentDTO.setCommentPostUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.COMMENT_POST_URL));
						contentDTO.setCDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.VOIZD_CONTENT_URL));
						contentDTO.setAmpSts(VoizdConstant.ACTIVE_AMPLIFY_STATUS);
						contentDTO.setAmplifyUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.AMPLIFY_URL));
						
							if(userId>0){
								
								Byte likeStatus =contentLikeDAO.getUserContentLike(contentVO.getContentId(), userId);
								if(null!=likeStatus){
									 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
											contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
									 }else{
										 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
									 }
								}else{
									contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
								}
								
							}else{
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							}
							
							contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
							contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
					
						contentDtoList.add(contentDTO);
					}	
					}
					cnt++;
				
			 }
			
		}catch (MediaServiceFailedException | DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_BOOKMARK_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_BOOKMARK_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			if(resultSize<endLimit){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
			}else{
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}
		contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
		contentMap.put(contentType, contentDtoList);
		}
		
		return contentMap;
	
	}
	
	private void sendJmsMessage( TapClipVO  tapClipVO,String command){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("content", tapClipVO);
			msgMap.put("command",command);
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:tapvoizd");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}
	
	public StationDAO getStationDAO() {
		return stationDAO;
	}

	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}

	public ContentDAO getContentDAO() {
		return contentDAO;
	}

	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}

	public MediaDAO getMediaDAO() {
		return mediaDAO;
	}

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}


	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setContentSubDAO(ContentSubDAO contentSubDAO) {
		this.contentSubDAO = contentSubDAO;
	}


	public void setContentLikeDAO(ContentLikeDAO contentLikeDAO) {
		this.contentLikeDAO = contentLikeDAO;
	}


	public StationLikeDAO getStationLikeDAO() {
		return stationLikeDAO;
	}


	public void setStationLikeDAO(StationLikeDAO stationLikeDAO) {
		this.stationLikeDAO = stationLikeDAO;
	}


	public StationSubDAO getStationSubDAO() {
		return stationSubDAO;
	}

	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}
	public void setSearchBO(SearchBO searchBO) {
		this.searchBO = searchBO;
	}

	@Override
	public HashMap<String, Object> getUserVStreamContents(VStreamVO vStreamVO, Map<String, Object> clientMap) throws ContentServiceException {
		List<ContentDTO> contentDtoList = new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		try {
			MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
			List<TapClipVO> tapClipVOList = null;
			Long userId = vStreamVO.getUserId();
			Long visitorId = vStreamVO.getVisitorId();
			boolean hasNext = false;
			int startLimit = 0;
			int endLimit = 10;
			int resultSize = 0;
			logger.debug("getUserVStreamContents() ::ALL :: " + vStreamVO.getType() + " , userid :: " + vStreamVO.getUserId()+ "visitorId="+vStreamVO.getVisitorId()+" more ::"+vStreamVO.isHasNext());	
			int count = 0;
			boolean isDeleteNeeded = false;
			if (VoizdConstant.ALL.equalsIgnoreCase(vStreamVO.getType())) {	
				if(vStreamVO.isHasNext()){
				startLimit =vStreamVO.getStartLimit();
				}else{
				startLimit=10;
				}
				HashMap<String, Object> dataMap = mongoServiceImpl.getUserVStream(userId, vStreamVO.getType(), startLimit);
				tapClipVOList = (List<TapClipVO>) dataMap.get(VoizdConstant.CONTENT);
				count = (int) dataMap.get(VoizdConstant.COUNT);

			} else if (VoizdConstant.MY.equalsIgnoreCase(vStreamVO.getType())) {
				if(vStreamVO.isHasNext()){
					startLimit =vStreamVO.getStartLimit();
					}else{
					startLimit=10;
				 }
				HashMap<String, Object> dataMap = mongoServiceImpl.getMyVStream(userId, vStreamVO.getType(), startLimit);
				tapClipVOList = (List<TapClipVO>) dataMap.get(VoizdConstant.CONTENT);
				count = (int) dataMap.get(VoizdConstant.COUNT);
				if(count>0){
					isDeleteNeeded = true;
				}

			}else if (VoizdConstant.IFOLLOW.equalsIgnoreCase(vStreamVO.getType())) {
				if(vStreamVO.isHasNext()){
					startLimit =vStreamVO.getStartLimit();
					}else{
					startLimit=10;
				 }
				HashMap<String, Object> dataMap = mongoServiceImpl.getIFollowVStream(userId, startLimit);
				tapClipVOList = (List<TapClipVO>) dataMap.get(VoizdConstant.CONTENT);
				count = (int) dataMap.get(VoizdConstant.COUNT);

			}
			logger.debug("getUserVSreamtContents() ::tapClipVOList :: " + tapClipVOList);

			if (tapClipVOList != null && tapClipVOList.size() > 0) {
				resultSize = tapClipVOList.size();
				if (VoizdConstant.MY.equalsIgnoreCase(vStreamVO.getType())) {
					if (count > resultSize + startLimit-10) {
						hasNext = true;
					}
				}else if (VoizdConstant.ALL.equalsIgnoreCase(vStreamVO.getType())){
					if (count > resultSize + startLimit-10) {
						hasNext = true;
					}
				}else if (VoizdConstant.IFOLLOW.equalsIgnoreCase(vStreamVO.getType())){
					if (count > resultSize + startLimit-10) {
						hasNext = true;
					}
				}
				getContentList(contentDtoList, tapClipVOList, clientMap, userId,visitorId,isDeleteNeeded);
				logger.debug("getUserVSreamtContents() ::resultSize :: " + resultSize + " , count :: " + count + " ,startLimit :: " + startLimit);
			}
			if (hasNext) {
				StringBuilder relativeUrl = new StringBuilder(VoizdRelativeUrls.VSTREAM_CONTENT_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR)
						.append(userId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR)
						.append(startLimit + resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR)
						.append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.TYPE).append(VoizdRelativeUrls.VALUE_SEPARATOR)
						.append(vStreamVO.getType());
				contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(null!=contentDtoList && contentDtoList.size()>0){
				if (VoizdConstant.MY.equalsIgnoreCase(vStreamVO.getType())){
						contentMap.put(VoizdConstant.MY, contentDtoList);
					}else{
					  contentMap.put(VoizdConstant.CONTENT_MAP, contentDtoList);
					}
			}else{
				contentMap.put(VoizdConstant.MESSAGE, SuccessCodesEnum.NO_CLIP_SUCCESS.getSuccessMessage());
			}
			logger.debug(" getUser.vstream =" + contentDtoList);
		} catch(MongoException e){
			logger.error("MongoException while get vstream content " + e.getLocalizedMessage(), e);
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_SERVICE_FAILED_EXCEPTION);
		}catch (Exception e) {
			logger.error("Exception while get vstream content " + e.getLocalizedMessage(), e);
		}
		contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
		return contentMap;
	}
	
	private void getContentList(List<ContentDTO> contentDtoList, List<TapClipVO> tapClipVOList, Map<String, Object> clientMap, Long userId,Long visitorId,boolean isDeleteNeeded)
			throws DataAccessFailedException, UserServiceFailedException, MediaServiceFailedException {
		String imageResolution = null;
		String platform = VoizdConstant.DEFAULT_PLATFORM;
		if (clientMap != null) {
			if (null != clientMap.get(ClientParamConstant.IMAGESIZE)) {
				imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
			} else {
				imageResolution = MediaUtil.getImageSize(null);
			}
			if (null != clientMap.get(ClientParamConstant.PLATFORM)) {
				platform = clientMap.get(ClientParamConstant.PLATFORM).toString();
			}
		} else {
			imageResolution = MediaUtil.getImageSize(null);
		}
		Byte ampSts = VoizdConstant.ACTIVE_AMPLIFY_STATUS;
		for (TapClipVO tapClipVO : tapClipVOList) {
			if(tapClipVO.isAmplify()){
				 ampSts = VoizdConstant.INACTIVE_AMPLIFY_STATUS;
			}
			ContentDTO contentDTO = new ContentDTO();
			contentDTO.setCId(tapClipVO.getCreatorId());
			contentDTO.setCTitle(tapClipVO.getTitle());
			contentDTO.setCTag(tapClipVO.getTag());
			contentDTO.setCntId(tapClipVO.getContentId());
			contentDTO.setWeblink(tapClipVO.getWeblink());
			contentDTO.setCDate(DateTimeUtils.format(tapClipVO.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			logger.debug("getUserVSreamtContents() :: CreatorId :: " + tapClipVO.getCreatorId());
			UserVO userVO = userService.getUserProfile(tapClipVO.getCreatorId());
			if (null != userVO) {
				contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
				if (tapClipVO.getCreatorId().equals(visitorId)) {
					 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
					 contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
				}else{
				 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
				 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getCreatorId());
				 contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
				}
				if (null != userVO.getProfilePicFileId()) {
					MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
					if (null != mediaVO) {
						mediaService.convertMedia(mediaVO.getMediaType(), mediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
						contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(mediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution), false)));
						contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(mediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution), false)));
					}
				} else {
					DefaultMediaVO defaultMediaVO = null;
					if (userVO.getGender() == 1) {
						defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_MALE);
					} else {
						defaultMediaVO = mediaDAO.getDefaultMediaVOByName(VoizdConstant.DEFAULT_FEMALE);
					}
					if (null != defaultMediaVO) {
						mediaService.convertMedia(defaultMediaVO.getMediaType(), defaultMediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
						contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution), false)));
						contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution), false)));
					}
				}
			}
			List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(tapClipVO.getContentId());

			for (ContentMediaVO contentMediaVO : contentMediaVOList) {
				if ("audio".equalsIgnoreCase(contentMediaVO.getMediaType())) {
					if (VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)
							&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt()) || VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt()))) {
						contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(), contentMediaVO.getExt(), null), true)));
					} else {
						
						mediaService.convertMedia(contentMediaVO.getMediaType(), contentMediaVO.getFileId(), VoizdConstant.MP3, null);
						contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),
								MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),VoizdConstant.MP3, null), true)));
					}
					if (StringUtils.isNotBlank(contentMediaVO.getDuration())) {
						contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
					}
				} else {
					logger.error("No content found for contentid=" + tapClipVO.getContentId());
				}
			}

			ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(tapClipVO.getContentId());
			logger.debug("contentCounterVO=" + contentCounterVO + " contentId=" + tapClipVO.getContentId());
			if (null != contentCounterVO) {
				contentDTO.setCLikes(contentCounterVO.getLikes());
				contentDTO.setCShare(contentCounterVO.getShare());
				contentDTO.setComments(contentCounterVO.getComments());
				contentDTO.setCView(contentCounterVO.getView());
				contentDTO.setCAmplify(contentCounterVO.getAmplify());
				if(contentCounterVO.getAmplify()>0){
					 StringBuilder amplifierUrl= new StringBuilder(VoizdRelativeUrls.AMPLIFIER_URL);
					 amplifierUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getContentId());
					 contentDTO.setAmplifierUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),amplifierUrl.toString()));
				}
				if(contentCounterVO.getComments()>0){
					 StringBuilder commentUrl= new StringBuilder(VoizdRelativeUrls.COMMENT_URL);
					 commentUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getContentId());
					 contentDTO.setCommentUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),commentUrl.toString()));
					 logger.debug("getContentList=" + commentUrl);
				}
				
			}
		
			contentDTO.setCommentPostUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.COMMENT_POST_URL));
			contentDTO.setCDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.VOIZD_CONTENT_URL));
			if(tapClipVO.getAmplifierId() != null && tapClipVO.getAmplifierId()>0){
				contentDTO.setAmplifierId(tapClipVO.getAmplifierId());
				userVO = userService.getUserProfile(tapClipVO.getAmplifierId());
				if (null != userVO) {
					contentDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
					if (tapClipVO.getAmplifierId().equals(visitorId)) {
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
						 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}else{
					 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
					 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
					 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getAmplifierId());
					 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					}
				}
				logger.debug("contentVO.tapClipVO.getAmplifierId()()=" + tapClipVO.getAmplifierId());
			}
		
			contentDTO.setAmpSts(ampSts);
			contentDTO.setAmplifyUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.AMPLIFY_URL));
			
			logger.debug("contentVO.getCreatorId()=" + tapClipVO.getCreatorId() + " userId=" + userId+tapClipVO.isAmplify());
			
			if(userId.equals(visitorId) && isDeleteNeeded){
				logger.debug(" true contentVO.getCreatorId()=userId="+userId+" visitorId="+visitorId);
				if (tapClipVO.getCreatorId().equals(visitorId)) {
					contentDTO.setCDeleteUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_DELETE_URL));
				}/*else if(tapClipVO.getAmplifierId() != null && tapClipVO.getAmplifierId().equals(visitorId)){
					contentDTO.setCDeleteUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_DELETE_URL));
				}*/
			}else{
				logger.debug("contentVO.getCreatorId()=userId="+userId+" visitorId="+visitorId);
			}
			if (visitorId > 0) {

				Byte likeStatus = contentLikeDAO.getUserContentLike(tapClipVO.getContentId(), visitorId);
				if (null != likeStatus) {
					if (VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)) {
						contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
					} else {
						contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
					}
				} else {
					contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
				}

			} else {
				contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
			}
			contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
			contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
			contentDtoList.add(contentDTO);
		}
	}
	
	//Voizd Live feed
	@Override
	public HashMap<String, Object> getRecentVStreamContents(VStreamVO vStreamVO, Map<String, Object> clientMap) throws ContentServiceException {
		List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		String contentType= VoizdConstant.RECENT ;
		boolean hasNext = false ;
		boolean hasPre = false ;
	
		Long preContentId= 0l;
		Long nextContentId= 0l;
		int resultSize =0 ;
		boolean order = false ;
		Long userId =vStreamVO.getUserId();
		int startLimit = vStreamVO.getStartLimit();
		int endLimit = vStreamVO.getEndLimit();
		if(null!=userId && userId>0){
			
		try {
			 String imageResolution = null;
		
			 String platform = VoizdConstant.DEFAULT_PLATFORM;
			 if(clientMap!=null){
				 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
					}else{
						imageResolution = MediaUtil.getImageSize(null);
					}
				 if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
					 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
				 }
			 }else{
				 imageResolution = MediaUtil.getImageSize(null);
			 }
		
			List<TapClipVO> contentTapClipVOList= null;
			//false is desc order
				contentTapClipVOList = contentDAO.getRecentContentListDesc(VoizdConstant.CONTENT_ACTIVE_STATUS,startLimit,endLimit+1);
				 hasNext = true ;
		
			if(null!= contentTapClipVOList ){
				if(endLimit<contentTapClipVOList.size()){
					if(order){
						contentTapClipVOList.remove(0);
						 hasPre = true ;
					}else{
						contentTapClipVOList.remove(contentTapClipVOList.size()-1);
						hasNext = true ;
					}
				}
				
				resultSize = contentTapClipVOList.size();
			}
			 int cnt =0 ;
			 for(TapClipVO tapClipVO:contentTapClipVOList){
						ContentDTO contentDTO = new ContentDTO();
						contentDTO.setLatitude(tapClipVO.getLatitude());
						contentDTO.setLongitude(tapClipVO.getLongitude());
						contentDTO.setCId(tapClipVO.getCreatorId());
						contentDTO.setCTitle(tapClipVO.getTitle());
						contentDTO.setCTag(tapClipVO.getTag());
						
						logger.debug("tapClipVO.getCreatedDate()"+tapClipVO.getCreatedDate());
					   contentDTO.setCDate(DateTimeUtils.format(tapClipVO.getCreatedDate(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
					   contentDTO.setWeblink(tapClipVO.getWeblink());
						contentDTO.setCntId(tapClipVO.getContentId());
						UserVO userVO = userService.getUserProfile(tapClipVO.getCreatorId());
						 if(null!= userVO){
							 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
							 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getCreatorId());
							 contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
							 contentDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
							 logger.debug("tapClipVO.getCreatorId()="+tapClipVO.getCreatorId()+"userVO.getLatitude()="+userVO.getLatitude());
							// contentDTO.setLatitude(userVO.getLatitude());
							// contentDTO.setLongitude(userVO.getLongitude());
							 if (null != userVO.getProfilePicFileId()) {
								 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
								 if(null!=mediaVO){
									 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
									 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
										 contentDTO.setCImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
										 contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
									}
							 }
						 }
						
						List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(tapClipVO.getContentId());
						for(ContentMediaVO contentMediaVO:contentMediaVOList){
							if(VoizdConstant.AUDIO.equalsIgnoreCase(contentMediaVO.getMediaType())){
								if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}else{
									 contentMediaVO.setExt(VoizdConstant.MP3);
									 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
								}
								if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
									contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
								}
							}else{
								logger.error("There is no audio attach with getContentId="+tapClipVO.getContentId());
							}
						}
						
						ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(tapClipVO.getContentId());
						logger.debug("contentCounterVO="+contentCounterVO+" contentId="+tapClipVO.getContentId());
						if(null!=contentCounterVO){
							contentDTO.setCLikes(contentCounterVO.getLikes());
							contentDTO.setCShare(contentCounterVO.getShare());
							contentDTO.setComments(contentCounterVO.getComments());
							contentDTO.setCView(contentCounterVO.getView());
							contentDTO.setCAmplify(contentCounterVO.getAmplify());
							if(contentCounterVO.getAmplify()>0){
								 StringBuilder amplifierUrl= new StringBuilder(VoizdRelativeUrls.AMPLIFIER_URL);
								 amplifierUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getContentId());
								 contentDTO.setAmplifierUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),amplifierUrl.toString()));
								 
								 List<AmplifyInfoVO> amplifierCounterVOList = null;
									amplifierCounterVOList = amplifyDAO.getAmplifierList(tapClipVO.getContentId(),0,1);
									 for(AmplifyInfoVO amplifyInfoVO:amplifierCounterVOList){
										 contentDTO.setAmplifierId(amplifyInfoVO.getAmplifierId());
											userVO = userService.getUserProfile(amplifyInfoVO.getAmplifierId());
											if (null != userVO) {
												contentDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
												/*if (amplifyInfoVO.getAmplifierId().equals(userId)) {
													 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
													 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
												}else{*/
													 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
													 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
													 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(amplifyInfoVO.getAmplifierId());
													 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
												//}
											}
									 }
								 
								 
									logger.debug("contentVO.getRecentVStreamContents=" + tapClipVO.getAmplifierId());
							}
							
							if(contentCounterVO.getComments()>0){
								 StringBuilder commentUrl= new StringBuilder(VoizdRelativeUrls.COMMENT_URL);
								 commentUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(tapClipVO.getContentId());
								 contentDTO.setCommentUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),commentUrl.toString()));
								 logger.debug("contentVO.contentDTO=" + commentUrl);
							}
							
						}
					
						contentDTO.setCommentPostUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.COMMENT_POST_URL));
						contentDTO.setCDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.VOIZD_CONTENT_URL));
						contentDTO.setAmpSts(VoizdConstant.ACTIVE_AMPLIFY_STATUS);
						contentDTO.setAmplifyUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.AMPLIFY_URL));
						
							if(userId>0){
								
								Byte likeStatus =contentLikeDAO.getUserContentLike(tapClipVO.getContentId(), userId);
								if(null!=likeStatus){
									 if(VoizdConstant.CONTENT_UNLIKE_STATUS.equals(likeStatus)){
											contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
									 }else{
										 contentDTO.setCLikeValue(VoizdConstant.CONTENT_UNLIKE_STATUS);
									 }
								}else{
									contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
								}
								
							}else{
								contentDTO.setCLikeValue(VoizdConstant.CONTENT_LIKE_STATUS);
							}
							
							contentDTO.setCLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_LIKE_URL));
							contentDTO.setCShareUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.CONTENT_SHARE_URL));
					
						contentDtoList.add(contentDTO);
				
					
					cnt++;
				
			 }
			
		}catch (MediaServiceFailedException | DataAccessFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}catch (UserServiceFailedException e) {
			throw new ContentServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
		}
		
		if(hasNext){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.RECENT_CONTENT_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
			contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
		}
		/*if(hasPre){
			StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_BOOKMARK_MORE_URL);
			relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
			if(resultSize<endLimit){
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
			}else{
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
			}
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
			relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
			contentMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			
		}*/
		contentMap.put("search", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.SEARCH_HASH_URL));
		contentMap.put(contentType, contentDtoList);
		}
		
		return contentMap;
	
	
	}
	
	private void sendJmsPushMessage(Long userId,String pushKey,String message,String platform){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("deviceKey",pushKey);
			msgMap.put("pushMessage",message);
			msgMap.put("userId", userId);
			msgMap.put("platform", platform);
			msgMap.put("share","yes");
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:push");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}
	
}
