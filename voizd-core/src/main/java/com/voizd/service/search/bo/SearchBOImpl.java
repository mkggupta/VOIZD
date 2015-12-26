package com.voizd.service.search.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.ContentDTO;
import com.voizd.common.beans.dto.StationDTO;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentLikeDAO;
import com.voizd.dao.modules.content.ContentSubDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationLikeDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.search.dataobject.SearchResult;
import com.voizd.search.exception.ElasticServerClientException;
import com.voizd.search.service.VoizdSearch;
import com.voizd.search.service.VoizdSearchImpl;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.search.exception.SearchServiceException;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;


public class SearchBOImpl implements SearchBO{
	private static Logger logger = LoggerFactory.getLogger(SearchBOImpl.class);
	final String DEFAULT_FILE_NAME ="station";
	final String ADVANCE ="ad";
	private  MediaDAO mediaDAO ;
	private UserService userService ;
	private StationLikeDAO stationLikeDAO;
	private StationCounterDAO stationCounterDAO ;
	private StationSubDAO  stationSubDAO ;
	private StationDAO  stationDAO ;
	private ContentCounterDAO contentCounterDAO ;
	private ContentSubDAO  contentSubDAO;
	private ContentLikeDAO contentLikeDAO;
	private MediaService  mediaService;
	private AmplifyDAO amplifyDAO;
	
	public void setAmplifyDAO(AmplifyDAO amplifyDAO) {
		this.amplifyDAO = amplifyDAO;
	}
	
	@Override
	public void indexStationMedia(StationSearchVO stationSearchVO, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			voizdSearch.executeStation(stationSearchVO, refresh, type);
		} catch (ElasticServerClientException e) {
			logger.error("Error indexStationMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
		}
	}

	@Override
	public void indexContentMedia(ContentSearchVO contentSearchVO, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			voizdSearch.executeContent(contentSearchVO, refresh, type);
		} catch (ElasticServerClientException e) {
			logger.error("Error indexContentMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
		}
	}

	@Override
	public void indexUpdateStationMedia(StationSearchVO stationSearchVO, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			voizdSearch.executeStation(stationSearchVO, refresh, type);
		} catch (ElasticServerClientException e) {
			logger.error("Error indexStationMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
		}
	}

	@Override
	public void indexUpdateContentMedia(ContentSearchVO contentSearchVO, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			voizdSearch.executeContent(contentSearchVO, refresh, type);
		} catch (ElasticServerClientException e) {
			logger.error("Error indexContentMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
		}	
	}

	@Override
	public void indexDeleteContentMedia(Long contentId, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
		voizdSearch.executeDelete(contentId, refresh, type);
		}catch(ElasticServerClientException e){
			logger.error("Error indexDeleteContentMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);	
		}
	}

	@Override
	public void indexDeleteStationMedia(Long stationId, boolean refresh, String type) throws SearchServiceException {
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
		voizdSearch.executeDelete(stationId, refresh, type);
		}catch(ElasticServerClientException e){
			logger.error("Error indexDeleteContentMedia() : "+e.getLocalizedMessage(),e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);	
		}
	}


	@Override
	public HashMap<String, Object> getStationSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException {
		List<StationDTO> stationDtoList = new ArrayList<StationDTO>();
		HashMap<String, Object> stationMap = new HashMap<String, Object>();
		try {
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			boolean hasNext = false ;
		
			SearchResult searchResult =null;
			if(ADVANCE.equalsIgnoreCase(searchVO.getSearchType())){
				searchResult = voizdSearch.executeAdvanceStationSearch(searchVO, startLimit, pageLimit);
			}else{
				searchResult = voizdSearch.executeStationSearch(searchVO.getTag(),startLimit,pageLimit);
			}
			List<StationSearchVO> stationSearchVOList = searchResult.getStationSearchVOList();
			int totalSize =searchResult.getSize();
			if(totalSize - startLimit >pageLimit){
				hasNext=true;
			}
			
			 String screenResolution = null;
			 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
				 screenResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
			 }else{
				 screenResolution = MediaUtil.getImageSize(null);
			 }
			//List<StationVO> stationVOList = SearchUtils.getStationSearchResponse(stationSearchVOList);
			
			if (stationSearchVOList != null && stationSearchVOList.size() > 0) {
				for (StationSearchVO stationSearchVO : stationSearchVOList) {
					StationDTO stationDTO = new StationDTO();

					stationDTO.setSId(stationSearchVO.getStationId());
					stationDTO.setSName(stationSearchVO.getStationName());
					//stationDTO.setSDate(StationSearchVO.getCreatedDate());
					stationDTO.setCId(stationSearchVO.getCreatorId());
					try {
						StationMediaVO stationMediaVO = mediaDAO.getStationMediaByFileId(stationSearchVO.getFileId());
						 if(null!=stationMediaVO){
							 mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,screenResolution);
							 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,screenResolution),false)));
						 }else{
							 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(stationSearchVO.getFileId());
							 if(null!=defaultMediaVO){
							    mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),screenResolution);
							    stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),screenResolution),false)));
							 }
						 }
						stationDTO.setDesc(stationSearchVO.getDescription());

						Long userId = (searchVO.getUserId()!=null)?searchVO.getUserId():0l;
						
						UserVO userVO = userService.getUserProfile(stationSearchVO.getCreatorId());
						if (null != userVO) {
							stationDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						}
						StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationSearchVO.getStationId());
						if (null != stationCounterVO) {
							stationDTO.setLikes(stationCounterVO.getLikes());
							stationDTO.setDislikes(stationCounterVO.getDislikes());
							stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
							stationDTO.setView(stationCounterVO.getView());
							stationDTO.setSComments(stationCounterVO.getComments());
							stationDTO.setFollower(stationCounterVO.getFollower());
							stationDTO.setShare(stationCounterVO.getShare());
						}
						//logger.debug("---stationSearchVO.isHasContent()---"+stationSearchVO.getHasContent());
						if (stationSearchVO.getHasContent()>0) {
							StringBuilder relativeUrl = new StringBuilder(VoizdRelativeUrls.STATION_CONTENT_DETAIL_URL);
							relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR)
									.append(stationSearchVO.getStationId());
							if (userId > 0) {
								relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID)
										.append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							}
							stationDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
						}

						if (stationSearchVO.getCreatorId() != userId) {
							if (userId > 0) {

								Byte subStatus = stationSubDAO.getStationTap(stationSearchVO.getStationId(), userId);
								if (null != subStatus) {
									if (VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)) {
										stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
									} else {
										stationDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
									}
								} else {
									stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
								}
								Byte sLikeStatus = stationLikeDAO.getUserStationLike(stationSearchVO.getStationId(), userId);
								if (null != sLikeStatus) {
									if (VoizdConstant.STATION_UNLIKE_STATUS.equals(sLikeStatus)) {
										stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
									} else {
										stationDTO.setLikeValue(VoizdConstant.STATION_UNLIKE_STATUS);
									}
								} else {
									stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
								}
							} else {
								stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
								stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
							}
							stationDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
							stationDTO.setLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_LIKE_URL));
						}

					} catch (DataAccessFailedException e) {
						logger.error(
								" getStations.stationCounterVO exception for stationid=" + stationSearchVO.getStationId() + "reason is " + e.getLocalizedMessage(), e);
						throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
					} catch (UserServiceFailedException e) {
						logger.error(
								" getStations.getUserProfile exception for stationid=" + stationSearchVO.getStationId() + " for user " + stationSearchVO.getCreatorId()
										+ "reason is " + e.getLocalizedMessage(), e);
						throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
					}
					stationDtoList.add(stationDTO);
					
				}
				if(hasNext){
					StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.SEARCH_STATION_MORE_URL);
					relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM);
					if(searchVO.getTag() != null){
						relativeUrl.append(VoizdStationContentParam.TAG).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getTag());
					}
					if(ADVANCE.equalsIgnoreCase(searchVO.getSearchType())){
						if(searchVO.getLocation() != null){
					     relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.LOCATION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getLocation());
						}
						if(searchVO.getLanguage() != null){
						     relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.LANGUAGE).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getLanguage());
							}
						if(searchVO.getUserName() != null){
						     relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USER_NAME).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getUserName());
							}
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.SEARCH_TYPE).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdStationContentParam.ADVANCE);
					}
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(pageLimit+startLimit);
			        relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(pageLimit);
			        relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
					stationMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				}
			} else {

			}
			//STATION_MAP
			stationMap.put(VoizdConstant.STATION_MAP,stationDtoList);
			stationMap.put(VoizdConstant.STATION_RESULT_SIZE,totalSize);
		} catch (ElasticServerClientException e) {
			logger.error("Error getStationSearch() : " + e.getLocalizedMessage(), e);
			throw new SearchServiceException(ErrorCodesEnum.SEVERE_ERROR);
		}
		return stationMap;
	}

	@Override
	public HashMap<String, Object> getContentSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException {
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		try {
			List<ContentDTO> contentDtoList =new ArrayList<ContentDTO>();
			boolean hasNext = false ;
		
			VoizdSearch voizdSearch = VoizdSearchImpl.getInstance();
			SearchResult searchResult = null;
			String searchType = searchVO.getSearchType();
			if(ADVANCE.equalsIgnoreCase(searchType)){
				searchResult = voizdSearch.executeAdvanceContentSearch(searchVO, startLimit, pageLimit);
			}else if (searchVO.getType() == 1){
				searchResult = voizdSearch.executeContentSearch(searchVO.getTag(),startLimit,pageLimit);	
				//searchResult = voizdSearch.executeContentTagSearch(searchVO.getTag(),startLimit,pageLimit);
			}else{
				searchResult = voizdSearch.executeContentTagSearch(searchVO.getTag(),startLimit,pageLimit);
			}
			List<ContentSearchVO> contentSearchVOList = searchResult.getContentSearchVOList();
			int totalSize = searchResult.getSize();
			if(totalSize - startLimit >pageLimit){
				hasNext = true;
			}
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
			logger.debug("getContentSearch() :: totalSize :: " + totalSize+", text :: "+searchVO.getTag() +", startLimit :: "+startLimit+",pageLimit "+pageLimit);
			
			logger.debug("getContentSearch() ::contentSearchVOList :: "+contentSearchVOList);
			 
			 for(ContentSearchVO contentSearchVO:contentSearchVOList){
				   try {
					StationVO stationVO = stationDAO.getActiveStationInfo(contentSearchVO.getStationId());
					if(stationVO != null){
						 
						ContentDTO contentDTO = new ContentDTO();
						contentDTO.setCId(contentSearchVO.getCreatorId());
						contentDTO.setCTitle(contentSearchVO.getTitle());
						contentDTO.setCTag(contentSearchVO.getTag());
						contentDTO.setCntId(contentSearchVO.getContentId());
						contentDTO.setCName(UserUtils.displayName(contentSearchVO.getFirstName(),contentSearchVO.getLastName()));
						//contentDTO.setCDate(DateTimeUtils.format(DateTimeUtils.convertToDate(contentSearchVO.getCreatedDate()),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
						//contentDTO.setCDate(DateTimeUtils.convertToDate(contentSearchVO.getCreatedDate()));
						contentDTO.setCDate(contentSearchVO.getCreatedDate());
						//String fileId = contentSearchVO.getUserFileid();
						contentDTO.setWeblink(contentSearchVO.getWeblink());
						UserVO user = userService.getUserProfile(contentSearchVO.getCreatorId());
						String fileId = user.getProfilePicFileId();

						if(fileId != null){
							mediaService.convertMedia(VoizdConstant.IMAGE,fileId,VoizdConstant.IMAGE_JPG,imageResolution);
							contentDTO.setCThumbUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.IMAGE,MediaUtil.getMediaContentUrl(fileId,"jpg",imageResolution),false)));
						}
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
						 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getUserId());
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getCreatorId());
						 contentDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
					
						 String contentFileid = contentSearchVO.getFileId();
						 logger.debug("contentCounterVO= contentFileid   ::::::::::::::::"+contentFileid +"VISITOR_ID="+searchVO.getUserId()+" stationVO.getCreatorId()="+stationVO.getCreatorId());
						if (contentFileid != null && contentFileid.trim().length()>0) {
							MediaVO mediaVO = mediaService.getMediaInfo(contentSearchVO.getFileId());
							if (mediaVO != null) {
								if (VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)
										&& (VoizdConstant.MP3.equalsIgnoreCase(mediaVO.getMimeType()) || VoizdConstant.AMR.equalsIgnoreCase(mediaVO
												.getMimeType()))) {
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),
											MediaUtil.getMediaContentUrl(mediaVO.getFileId(), mediaVO.getMimeType(), null), true)));
								} else {
									mediaService.convertMedia(mediaVO.getMediaType(), mediaVO.getFileId(), VoizdConstant.MP3, null);
									contentDTO.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),
											MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.MP3, null), true)));
								}
								if (StringUtils.isNotBlank(mediaVO.getDuration())) {
									contentDTO.setCDuration(DateTimeUtils.getTimeInSecond(mediaVO.getDuration()));
								}
							}
						}

						ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(contentSearchVO.getContentId());
						logger.debug("contentCounterVO="+contentCounterVO+" contentId="+contentSearchVO.getContentId());
						if(null!=contentCounterVO){
							contentDTO.setCLikes(contentCounterVO.getLikes());
							contentDTO.setCShare(contentCounterVO.getShare());
							contentDTO.setComments(contentCounterVO.getComments());
							contentDTO.setCView(contentCounterVO.getView());
							contentDTO.setCAmplify(contentCounterVO.getAmplify());
							if(contentCounterVO.getAmplify()>0){
								 StringBuilder amplifierUrl= new StringBuilder(VoizdRelativeUrls.AMPLIFIER_URL);
								 amplifierUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentSearchVO.getContentId());
								 contentDTO.setAmplifierUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),amplifierUrl.toString()));
								 List<AmplifyInfoVO> amplifierCounterVOList = null;
									amplifierCounterVOList = amplifyDAO.getAmplifierList(contentSearchVO.getContentId(),0,1);
									 for(AmplifyInfoVO amplifyInfoVO:amplifierCounterVOList){
										 contentDTO.setAmplifierId(amplifyInfoVO.getAmplifierId());
										 UserVO	userVO = userService.getUserProfile(amplifyInfoVO.getAmplifierId());
											if (null != userVO) {
												contentDTO.setAmplifierName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
												/*if (amplifyInfoVO.getAmplifierId().equals(userId)) {
													 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PRIVATE_PROFILE_URL);
													 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
												}else{*/
													 relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
													 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getUserId());
													 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(amplifyInfoVO.getAmplifierId());
													 contentDTO.setAmplifierProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
												//}
											}
									 }
								 
							}
							if(contentCounterVO.getComments()>0){
								 StringBuilder commentUrl= new StringBuilder(VoizdRelativeUrls.COMMENT_URL);
								 commentUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.CONTENT_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(contentSearchVO.getContentId());
								 contentDTO.setCommentUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),commentUrl.toString()));
								 logger.debug("contentVO.contentDTO=" + commentUrl);
							}
							
						}
					
						contentDTO.setCommentPostUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.COMMENT_POST_URL));
					
						contentDTO.setAmpSts(VoizdConstant.ACTIVE_AMPLIFY_STATUS);
						contentDTO.setAmplifyUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.AMPLIFY_URL));
						Long userId = searchVO.getUserId();
						logger.debug("userId in search="+userId+" searchVO="+searchVO.getTag());
							if(userId>0){
							
								Byte likeStatus =contentLikeDAO.getUserContentLike(contentSearchVO.getContentId(), userId);
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
						
				} catch (Exception e) {
					logger.error("Error while content search info : "+e.getLocalizedMessage(),e);
				}
				   
			 }
			if (hasNext && StringUtils.isNotBlank(searchVO.getTag())) {
				StringBuilder relativeUrl = new StringBuilder(VoizdRelativeUrls.SEARCH_CONTENT_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.TAG).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(searchVO.getTag());
		        relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(pageLimit+startLimit);
		        relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(pageLimit);
		        relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				contentMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
				if(null!=contentDtoList && contentDtoList.size()>0){
					 contentMap.put(VoizdConstant.CONTENT_MAP, contentDtoList);
					 contentMap.put(VoizdConstant.CONTENT_RESULT_SIZE,totalSize);
				}else{
					contentMap.put(VoizdConstant.MESSAGE, SuccessCodesEnum.SEARCH_SUCCESS.getSuccessMessage());
				}
			}catch(ElasticServerClientException e){
				logger.error("Error getContentSearch() : "+e.getLocalizedMessage(),e);
				throw new SearchServiceException(ErrorCodesEnum.SEARCH_SERVICE_FAILED_EXCEPTION);	
			}
		return contentMap;
	}

	
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setStationLikeDAO(StationLikeDAO stationLikeDAO) {
		this.stationLikeDAO = stationLikeDAO;
	}

	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}
	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}
	
	public StationDAO getStationDAO() {
		return stationDAO;
	}

	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}

	public ContentCounterDAO getContentCounterDAO() {
		return contentCounterDAO;
	}

	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}

	public ContentSubDAO getContentSubDAO() {
		return contentSubDAO;
	}

	public void setContentSubDAO(ContentSubDAO contentSubDAO) {
		this.contentSubDAO = contentSubDAO;
	}

	public ContentLikeDAO getContentLikeDAO() {
		return contentLikeDAO;
	}

	public void setContentLikeDAO(ContentLikeDAO contentLikeDAO) {
		this.contentLikeDAO = contentLikeDAO;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
}
