package com.voizd.service.station.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.FollowerDTO;
import com.voizd.common.beans.dto.StationDTO;
import com.voizd.common.beans.dto.VoizerDTO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationFollowerVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StationSearchVO;
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
import com.voizd.dao.entities.Station;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationLikeDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.user.UserCounterDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.search.helper.SearchUtils;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.helper.StationUtils;
import com.voizd.service.station.v1_0.StationServiceImpl;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.helper.UserUtils;
import com.voizd.service.user.v1_0.UserService;

public class StationBOImpl implements StationBO {
	private static Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
	
	private StationDAO  stationDAO;
	private StationCounterDAO stationCounterDAO ;
	private  MediaDAO mediaDAO ;
	private StationSubDAO  stationSubDAO ;
	private MediaService  mediaService;
	private UserService userService ;
	private StationLikeDAO stationLikeDAO;
	private SearchBO searchBO;
	private UserCounterDAO userCounterDAO ;

	public void setUserCounterDAO(UserCounterDAO userCounterDAO) {
		this.userCounterDAO = userCounterDAO;
	}
	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setStationLikeDAO(StationLikeDAO stationLikeDAO) {
		this.stationLikeDAO = stationLikeDAO;
	}
	public void setSearchBO(SearchBO searchBO) {
		this.searchBO = searchBO;
	}
	
	public Integer createStation(StationVO stationVO) throws StationServiceException {
		Integer responseCode = 0 ;
		Integer ALREADY_EXIST_CODE =-1;
				try {
					Boolean isExist = stationDAO.stationAlreadyExists(stationVO.getStationName(),stationVO.getCreatorId());
					logger.debug("StationBOImpl.isExist="+isExist);
					if(isExist){
						return ALREADY_EXIST_CODE;
					}else{
						 try {
							 String fileIdArr[] = null;
							 if(null==stationVO.getFileIds()){
								 stationVO.setFileId(mediaDAO.getDefaultMedia(VoizdConstant.DEFAULT_FILE_NAME));
							 }else{
								 	fileIdArr =stationVO.getFileIds().split("\\s*,\\s*");
								   if(fileIdArr.length>0){
											stationVO.setFileId(fileIdArr[0]);
											
									}	
							 }
							Long stationId = stationDAO.createStation(stationVO);
							logger.debug("StationBOImpl.stationId="+stationId);
							 if(null!=fileIdArr && fileIdArr.length>0){
									
									int ordering = 1 ;
									for (String fileId:fileIdArr){
										try {
											MediaVO mediaVO = mediaService.getMediaInfo(fileId);
											if(null!=mediaVO){
												StationMediaVO stationMediaVO = new StationMediaVO();
												stationMediaVO.setStationId(stationId);
												stationMediaVO.setMediaType(mediaVO.getMediaType());
												stationMediaVO.setExt(mediaVO.getMimeType());
												stationMediaVO.setFileId(fileId);
												stationMediaVO.setOrdering(ordering);
												stationMediaVO.setSize(mediaVO.getSize());
												stationMediaVO.setStatus(VoizdConstant.MEDIA_ACTIVE_STATUS);
												mediaDAO.createStationMedia(stationMediaVO);
												ordering++;
											}
										} catch (MediaServiceFailedException e) {
											throw new StationServiceException(ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION);
										}
									}
							}	
							stationCounterDAO.createStationCounter(stationId);
							responseCode = stationId.intValue();
						} catch (DataUpdateFailedException e) {
							throw new StationServiceException(ErrorCodesEnum.STATION_UPDATE_SERVICE_FAILED_EXCEPTION);
						}
					}
				} catch (DataAccessFailedException e) {
					throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
				}

		return responseCode;
	}
	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}
	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}
	@Override
	public void deleteStation(Long stationId, Long creatorId)throws StationServiceException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public StationVO getStation(Long stationId) throws StationServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	public HashMap<String, Object> getStationDetail(Long stationId,Long creatorId,Map<String, Object> clientMap) throws StationServiceException{
		
		HashMap<String, Object> stationMap = new HashMap<String, Object>();
		String stationType= VoizdConstant.STATION ;
		List<StationDTO> stationDtoList =new ArrayList<StationDTO>();
		try {
			logger.debug(" getStationDetail =="+stationId+" --- "+ creatorId);
			StationVO stationVO =stationDAO.getStationDetail(stationId, creatorId);
			if(null!=stationVO){
				 String imageResolution = null;
				 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
					 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
				 }else{
					 imageResolution = MediaUtil.getImageSize(null);
				 }
				
				 StationDTO stationDTO = new StationDTO();
				 stationId =stationVO.getStationId();
				 stationDTO.setSId(stationVO.getStationId());
				 stationDTO.setSName(stationVO.getStationName());
				 stationDTO.setSDate(stationVO.getCreatedDate());
				 stationDTO.setCId(stationVO.getCreatorId());
				 stationDTO.setTag(stationVO.getTag());
				 stationDTO.setDesc(stationVO.getDescription());
				 stationDTO.setLanguage(stationVO.getLanguage());
				 stationDTO.setLocation(stationVO.getLocation());
				 if(stationVO.isAdult()){
				    stationDTO.setAdult(VoizdConstant.ADULT_STATUS);
				 }else{
					 stationDTO.setAdult(VoizdConstant.NOT_ADULT_STATUS);
				 }
				 StringBuilder  sImgUrl = new StringBuilder();
				 List<StationMediaVO> stationMediaVOList = mediaDAO.getStationMediaList(stationId,VoizdConstant.IMAGE);
				
				 if(null!=stationMediaVOList && stationMediaVOList.size()>0){
					 for(StationMediaVO stationMediaVO:stationMediaVOList){
						 mediaService.convertMedia(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
						 sImgUrl.append(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)).append(VoizdRelativeUrls.CONTENT_URL_SEPARATOR);
					 }
					 
				 }else{
					 StationMediaVO stationMediaVO = mediaDAO.getStationMediaByFileId(stationVO.getFileId());
					 if(null!=stationMediaVO){
						 sImgUrl.append(CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false));
						
					 }else{
						 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(stationVO.getFileId());
						 if(null!=defaultMediaVO){
							    mediaService.convertMedia(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution);
							    sImgUrl.append(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),defaultMediaVO.getExt(),imageResolution),false));
						  }
					 }
				 } 
				 if(StringUtils.isNotBlank(sImgUrl.toString())){
					 stationDTO.setSImgUrl(VoizdUrlUtils.encodedUrl(sImgUrl.substring(0, sImgUrl.length()-1)));
				 }
				  stationDTO.setSUpdateUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.STATION_UPDATE_URL));
				  stationDtoList.add(stationDTO);
				 stationMap.put(stationType, stationDtoList);
			}
		}catch (DataAccessFailedException e) {
			throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
		}
		return stationMap;
	}
	@Override
	public void updateStation(StationVO updateStationVO) throws StationServiceException {
	        try{

			StationVO oldStationInfo = stationDAO.getStationInfo(updateStationVO.getStationId());
			
			if(oldStationInfo==null){
				throw new StationServiceException(ErrorCodesEnum.STATION_SERVICE_UPDATE_DATA_NOT_FOUND);	
			}
			long newUserId =updateStationVO.getCreatorId();
			long oldUserId =oldStationInfo.getCreatorId();
			if(newUserId != oldUserId){
				throw new StationServiceException(ErrorCodesEnum.STATION_CONTENT_INVALID_USER);
			}
			
			Station station = StationUtils.transformStation(updateStationVO, oldStationInfo);
			if(station==null){
				throw new StationServiceException(ErrorCodesEnum.STATION_SERVICE_UPDATE_DATA_NOT_FOUND);	
			}
			logger.debug("station--"+station.getFileId());
	        stationDAO.updateStation(station);
	        
	        StationVO stationInfo = stationDAO.getStationInfo(updateStationVO.getStationId());
	        if(stationInfo != null){
	            StationSearchVO stationSearchVO = SearchUtils.transformStationSearchVO(stationInfo, userService.getUserProfile(oldStationInfo.getCreatorId()));
	            searchBO.indexUpdateStationMedia(stationSearchVO, false, VoizdConstant.STATION);
	        }else{
	        	logger.error("Update search station value is null .Please check ");	
	        }
	        
	        if(updateStationVO.getFileIds() != null && (station.getFileIdList() != null && station.getFileIdList().size()>0) ){
	        	logger.debug("Station updateStation ::fileid list :: "+updateStationVO.getFileIds());
	        	mediaDAO.updateStationMedia(updateStationVO.getStationId());
	        	List<String> fileIds = StationUtils.getListOfFileId(updateStationVO.getFileIds());
	        	int order=1;
				for (String fileId : fileIds) {
					MediaVO mediaVO = mediaService.getMediaInfo(fileId);
					mediaDAO.createStationMedia(StationUtils.transformStationMedia(mediaVO, updateStationVO.getStationId(),order));
					order++;
				}
	        }
	        }catch(Exception e){
	        	logger.error("Station update fail "+e.getLocalizedMessage(),e);
	        	throw new StationServiceException(ErrorCodesEnum.STATION_UPDATE_SERVICE_FAILED_EXCEPTION);
	        }
	}
	@Override
	public List<StationVO> getUserStations(Long creatorId, Byte status)throws StationServiceException {
		 try {
			return stationDAO.getUserStationList(creatorId, status);
		} catch (DataAccessFailedException e) {
			throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
		}
	}
	@Override
	public HashMap<String, Object> getStations(Long stationId, Long userId,Byte status,int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws StationServiceException {
		
		List<StationDTO> stationDtoList =new ArrayList<StationDTO>();
		List<StationVO> stationList = null;
		HashMap<String, Object> stationMap = new HashMap<String, Object>();
		logger.debug(" getStations.status ="+status);
		String stationType= VoizdConstant.RECENT ;
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
		Long preStationId= 0l;
		Long nextStationId= 0l;
		int resultSize =0 ;
		 String imageResolution = null;
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		
		
		if(VoizdConstant.RECENT_STATION_STATUS.equals(status)){
			try {
				//true is asc order
				if(order && stationId>0){
					 stationList = stationDAO.getRecentStationList(startLimit,endLimit+1);
					 hasNext = true ;
				}else if(stationId>0){
				//desc order	
					 stationList = stationDAO.getRecentStationList(startLimit,endLimit+1);
					 hasPre = true ;
				}else{
					//first hit
				 stationList = stationDAO.getRecentStationList(startLimit,endLimit+1);
				 isFirstRequest = true;
				}
				logger.debug("RECENT_STATION_STATUS getStations"+stationList);
				if(null!= stationList){
					if(endLimit<stationList.size()){
						if(order){
							stationList.remove(0);
							 hasPre = true ;
						}else{
							stationList.remove(stationList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = stationList.size();
				}
				 int cnt =0 ;
				 for(StationVO stationVO:stationList){
					 StationDTO stationDTO = new StationDTO();
					 if(order && cnt==0){
						 preStationId= stationVO.getStationId();
					 }
					 stationId =stationVO.getStationId();
					 stationDTO.setSId(stationVO.getStationId());
					 stationDTO.setSName(stationVO.getStationName());
					 stationDTO.setSDate(stationVO.getCreatedDate());
					 stationDTO.setCId(stationVO.getCreatorId());
					 stationDTO.setLanguage(stationVO.getLanguage());
					 stationDTO.setLocation(stationVO.getLocation());
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
					 try {
						 UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
						 if(null!= userVO){
							 stationDTO.setCName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 }
						StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationVO.getStationId());
						if(null!=stationCounterVO){
							stationDTO.setLikes(stationCounterVO.getLikes());
							stationDTO.setDislikes(stationCounterVO.getDislikes());
							stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
							stationDTO.setView(stationCounterVO.getView());
							stationDTO.setSComments(stationCounterVO.getComments());
							stationDTO.setFollower(stationCounterVO.getFollower());
							stationDTO.setShare(stationCounterVO.getShare());
						}
						if(stationVO.isHasContent()){
							StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.STATION_CONTENT_DETAIL_URL);
							relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getStationId());
							if(userId>0){
								relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
							}
							stationDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						}
						if(stationVO.getCreatorId()!=userId){	
							if(userId>0){
								
								 Byte subStatus = stationSubDAO.getStationTap(stationId,userId);
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
						}
						
					} catch (DataAccessFailedException e) {
						logger.error(" getStations.stationCounterVO exception for stationid="+stationVO.getStationId()+
						"reason is "+e.getLocalizedMessage(),e);
						throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
					}catch (UserServiceFailedException e) {
						logger.error(" getStations.getUserProfile exception for stationid="+stationVO.getStationId()+" for user "+stationVO.getCreatorId()+
								"reason is "+e.getLocalizedMessage(),e);
						throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
					}
					
					stationDtoList.add(stationDTO);
					cnt++;
					if(cnt==stationDtoList.size()){
						nextStationId= stationVO.getStationId();
					}
					
				 }
			} catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.RECENT_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				stationMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.RECENT_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				stationMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
		
		}else if(VoizdConstant.POPULAR_STATION_STATUS.equals(status)){
			try {
				List<StationCounterVO> stationCounterVOList= null;
				//true is asc order
				if(order && stationId>0){
					stationCounterVOList = stationCounterDAO.getStationCounterList(startLimit,endLimit+1);
					// stationCounterVOList = stationCounterDAO.getStationCounterListAsc(stationId,endLimit+1);
					 hasNext = true ;
				}else if(stationId>0){
				//desc order	
					stationCounterVOList = stationCounterDAO.getStationCounterList(startLimit,endLimit+1);
					 //stationCounterVOList = stationCounterDAO.getStationCounterListDesc(stationId,endLimit+1);
					 hasPre = true ;
				}else{
					//first hit
					stationCounterVOList = stationCounterDAO.getStationCounterList(startLimit,endLimit+1);
				  isFirstRequest = true;
				}
				if(null!= stationCounterVOList){
					if(endLimit<stationCounterVOList.size()){
						if(order){
							stationCounterVOList.remove(0);
							 hasPre = true ;
						}else{
							stationCounterVOList.remove(stationCounterVOList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = stationCounterVOList.size() ;
				}
				 int cnt =0 ;
				
				for(StationCounterVO stationCounterVO:stationCounterVOList){
					StationVO stationVO = stationDAO.getActiveStationInfo(stationCounterVO.getStationId());
					if(null!=stationVO){
						 StationDTO stationDTO = new StationDTO();
						 stationDTO.setSId(stationVO.getStationId());
						 if(order && cnt==0){
							 preStationId= stationVO.getStationId();
						 }
						 stationId =stationVO.getStationId();
						 stationDTO.setSName(stationVO.getStationName());
						 stationDTO.setSDate(stationVO.getCreatedDate());
						 stationDTO.setCId(stationVO.getCreatorId());
						 stationDTO.setLanguage(stationVO.getLanguage());
						 stationDTO.setLocation(stationVO.getLocation());
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
					
						 stationDTO.setLikes(stationCounterVO.getLikes());
						 stationDTO.setDislikes(stationCounterVO.getDislikes());
						 stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
						 stationDTO.setView(stationCounterVO.getView());
						 stationDTO.setSComments(stationCounterVO.getComments());
						 stationDTO.setFollower(stationCounterVO.getFollower());	
						 stationDTO.setShare(stationCounterVO.getShare());
						 if(stationVO.isHasContent()){
								StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.STATION_CONTENT_DETAIL_URL);
								relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getStationId());
								if(userId>0){
									relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
								}
								stationDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						 }
						if(stationVO.getCreatorId()!=userId){	
							
							if(userId>0){
								
								 Byte subStatus = stationSubDAO.getStationTap(stationId,userId);
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
						}
						
						 stationDtoList.add(stationDTO);	
						 cnt++;
						 if(cnt==stationDtoList.size()){
							nextStationId= stationVO.getStationId();
						}
					}
				}
			} catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.POPULAR_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				stationMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.POPULAR_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				stationMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
			
			stationType = VoizdConstant.POPULAR;
		}else if(VoizdConstant.MY_TAP_STATION_STATUS.equals(status)){

			if(null!=userId && userId>0){
				try {
					List<StationFollowerVO> stationFollowerVOList= null;
					Long id = stationId ;
					//true is asc order
					if(order && id>0){
						stationFollowerVOList = stationSubDAO.getTappedStationList(userId,startLimit,endLimit+1);
						//stationFollowerVOList = stationSubDAO.getTappedStationListAsc(id,userId,endLimit+1);
						 hasNext = true ;
					}else if(id>0){
					//desc order
						stationFollowerVOList = stationSubDAO.getTappedStationList(userId,startLimit,endLimit+1);
						//stationFollowerVOList = stationSubDAO.getTappedStationListDesc(id,userId,endLimit+1);
						 hasPre = true ;
					}else{
						//first hit
						stationFollowerVOList = stationSubDAO.getTappedStationList(userId,startLimit,endLimit+1);
					  isFirstRequest = true;
					}
					if(null!= stationFollowerVOList){
						if(endLimit<stationFollowerVOList.size()){
							if(order){
								stationFollowerVOList.remove(0);
								 hasPre = true ;
							}else{
								stationFollowerVOList.remove(stationFollowerVOList.size()-1);
							}
							hasNext = true ;
						}
						resultSize = stationFollowerVOList.size();
					}
					 int cnt =0 ;
					 stationMap.put(VoizdConstant.STREAM_SHARE_URL_KEY, VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STREAM_SHARE_URL));
					logger.debug("MY_TAP_STATION_STATUS stationFollowerVOList="+stationFollowerVOList+" followerid="+userId);
					 for(StationFollowerVO stationFollowerVO:stationFollowerVOList){
						 if(order && cnt==0){
							 preStationId= stationFollowerVO.getId();
						 }
						 StationVO stationVO = stationDAO.getStationInfo(stationFollowerVO.getStationId());
						  stationId = stationFollowerVO.getStationId();
							if(null!=stationVO){
							 StationDTO stationDTO = new StationDTO();
							 stationDTO.setSId(stationVO.getStationId());
							 stationDTO.setSName(stationVO.getStationName());
							 stationDTO.setSDate(stationVO.getCreatedDate());
							 stationDTO.setCId(stationVO.getCreatorId());
							 stationDTO.setStatus(stationVO.getStatus());
							 stationDTO.setLanguage(stationVO.getLanguage());
							 stationDTO.setLocation(stationVO.getLocation());
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
							 try {
								StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationVO.getStationId());
								if(null!=stationCounterVO){
									stationDTO.setLikes(stationCounterVO.getLikes());
									stationDTO.setDislikes(stationCounterVO.getDislikes());
									stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
									stationDTO.setView(stationCounterVO.getView());
									stationDTO.setSComments(stationCounterVO.getComments());
									stationDTO.setFollower(stationCounterVO.getFollower());
									stationDTO.setShare(stationCounterVO.getShare());
								}
								 if(stationVO.isHasContent()){
										StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.STATION_CONTENT_DETAIL_URL);
										relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getStationId());
										if(userId>0){
											relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
										}
										stationDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
								 }
								
								 Byte subStatus = stationSubDAO.getStationTap(stationVO.getStationId(),userId);
								 if(null!=subStatus){
									if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
										stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
									}else{
										stationDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
									}
								 }else{
									 stationDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS); 
								 }
								Byte sLikeStatus  = stationLikeDAO.getUserStationLike(stationVO.getStationId(), userId);
								if(null!=sLikeStatus){
									 if(VoizdConstant.STATION_UNLIKE_STATUS.equals(sLikeStatus)){
										 stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
									 }else{
										 stationDTO.setLikeValue(VoizdConstant.STATION_UNLIKE_STATUS);
									 }
								}else{
									stationDTO.setLikeValue(VoizdConstant.STATION_LIKE_STATUS);
								}
								stationDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
								stationDTO.setLikeUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_LIKE_URL));
							} catch (DataAccessFailedException e) {
								logger.error(" getStations.stationCounterVO exception for stationid="+stationId+
								"reason is "+e.getLocalizedMessage(),e);
							}
							stationDtoList.add(stationDTO);
						 }else{
						 logger.info("not get sId ="+stationId); 
						 }
							 cnt++;
							 if(cnt==stationDtoList.size()){
								nextStationId= stationFollowerVO.getId();
							}
					 }
					 if(isFirstRequest && stationFollowerVOList!=null && stationFollowerVOList.size()<=0){
						 stationMap.put("status",  "success");
						 stationMap.put("message", "You have not tapped any station till now");
					 }
				} catch (DataAccessFailedException e) {
					throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
				}catch (UserServiceFailedException e) {
					throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
				}
				
				
			}else{
				logger.info("not get followerid ="+userId);
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MYTAPPED_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				stationMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MYTAPPED_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preStationId);
				if(userId>0){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				stationMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			
			stationType = VoizdConstant.MYTAPPED;	
		}else if(VoizdConstant.MY_STATION_STATUS.equals(status)){
			if(null!=userId && userId >0){
				try {
					//true is asc order
					if(order && stationId>0){
						 stationList = stationDAO.getMyStationList(userId,startLimit,endLimit+1);
						// stationList = stationDAO.getMyStationListAsc(stationId,userId,endLimit+1);
						 hasNext = true ;
					}else if(stationId>0){
					//desc order	
						 stationList = stationDAO.getMyStationList(userId,startLimit,endLimit+1);
						 //stationList = stationDAO.getMyStationListDesc(stationId,userId,endLimit+1);
						 hasPre = true ;
					}else{
						//first hit
					 stationList = stationDAO.getMyStationList(userId,startLimit,endLimit+1);
					 isFirstRequest = true;
					}
					logger.debug("RECENT_STATION_STATUS getStations"+stationList);
					if(null!= stationList){
						if(endLimit<stationList.size()){
							if(order){
								stationList.remove(0);
								 hasPre = true ;
							}else{
								stationList.remove(stationList.size()-1);
							}
							hasNext = true ;
						}
						resultSize = stationList.size() ;
					}
					 int cnt =0 ;
					
					logger.debug("MY_STATION_STATUS="+stationList);
					 for(StationVO stationVO:stationList){
						 StationDTO stationDTO = new StationDTO();
						 stationDTO.setSId(stationVO.getStationId());
						 if(order && cnt==0){
							 preStationId= stationVO.getStationId();
						 }
						 stationDTO.setSName(stationVO.getStationName());
						 stationDTO.setSDate(stationVO.getCreatedDate());
						 stationDTO.setCId(stationVO.getCreatorId());
						 stationDTO.setStatus(stationVO.getStatus());
						 stationDTO.setLanguage(stationVO.getLanguage());
						 stationDTO.setLocation(stationVO.getLocation());
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
						 try {
							StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationVO.getStationId());
							if(null!=stationCounterVO){
								stationDTO.setLikes(stationCounterVO.getLikes());
								stationDTO.setDislikes(stationCounterVO.getDislikes());
								stationDTO.setContCnt(stationCounterVO.getNumberOfContent());
								stationDTO.setView(stationCounterVO.getView());
								stationDTO.setSComments(stationCounterVO.getComments());
								stationDTO.setFollower(stationCounterVO.getFollower());
								stationDTO.setShare(stationCounterVO.getShare());
							}
							 if(stationVO.isHasContent()){
									StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.STATION_CONTENT_DETAIL_URL);
									relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getStationId());
									if(userId>0){
										relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
									}
									stationDTO.setCUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
							 }	
							 logger.debug(stationVO.getCreatorId()+" userId ="+userId);
							 if(stationVO.getCreatorId().equals(userId)){
									StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.STATION_DETAIL_URL);
									relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getStationId());
									relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
									stationDTO.setSDtlUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
									 logger.debug(stationDTO.getSDtlUrl()+"");
							 }
							
						} catch (DataAccessFailedException e) {
							logger.error(" getStations.stationCounterVO exception for stationid="+stationVO.getStationId()+
							"reason is "+e.getLocalizedMessage(),e);
						}
						stationDtoList.add(stationDTO);
						 cnt++;
						 if(cnt==stationDtoList.size()){
							nextStationId=stationVO.getStationId();
						}
					 }
					 if(isFirstRequest && stationList!=null && stationList.size()<=0){
						 stationMap.put(VoizdConstant.STATUS, VoizdConstant.SUCCESS);
						 stationMap.put(VoizdConstant.MESSAGE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessMessage());
					 }
				} catch (DataAccessFailedException e) {
					throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
				}catch (UserServiceFailedException e) {
					throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
				}
				
				
			}else{
				logger.error("not get creatorId ="+userId);
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MY_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(nextStationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				stationMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.MY_STATION_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(preStationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				stationMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			
			stationType = VoizdConstant.MY ;
		}else{
			throw new StationServiceException(ErrorCodesEnum.STATION_BUSINESS_RULES_FAILED_EXCEPTION);
		}
		
		stationMap.put(stationType, stationDtoList);
		
		 logger.debug(" getStations.stationDtoList ="+stationDtoList);
		return stationMap;
	}
	
	public HashMap<String, Object> createStationShareUrl(Long stationId,Byte appId) throws StationServiceException{
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
			try {
				StationVO stationVO = stationDAO.getActiveStationInfo(stationId);
				if(null!=stationVO){
					String thumbUrl =null;
					String userName =null;
					 StationMediaVO stationMediaVO = mediaDAO.getStationMediaByFileId(stationVO.getFileId());
					 if(null!=stationMediaVO){
						  thumbUrl=CDNUrlUtil.getCdnContentUrl(stationMediaVO.getMediaType(),stationMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,false);
					 }else{
						 DefaultMediaVO defaultMediaVO = mediaDAO.getDefaultMediaVO(stationVO.getFileId());
						 if(null!=defaultMediaVO){
							thumbUrl=CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),defaultMediaVO.getFileId(),defaultMediaVO.getExt(),false);
						 }
					 }
					
					 UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
					 if(null!= userVO){
						 userName= UserUtils.displayName(userVO.getFirstName(), userVO.getLastName());
					 }
					 StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(stationVO.getStationId());
					 if(null!=stationCounterVO){
						stationCounterVO.setShare(stationCounterVO.getShare()+1);
						 stationCounterDAO.updateStationCounter(stationCounterVO);
					 }
					
					dataMap.put(VoizdConstant.MESSAGE,"Listening "+userName+"'s station "+stationVO.getStationName());
					dataMap.put(VoizdConstant.THUMB_URL,thumbUrl);
					dataMap.put(VoizdConstant.SHARE_URL, VoizdUrlUtils.getStationShareUrl(stationVO.getStationName(),stationId,stationVO.getCreatorId(),appId));			
				}
			}catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}catch (DataUpdateFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_UPDATE_SERVICE_FAILED_EXCEPTION);
			}
			return dataMap;
	}
	
	@Override
	public HashMap<String, Object> getIFollowList(Long stationId,Long followerId, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {

		List<FollowerDTO> followerDtoList =new ArrayList<FollowerDTO>();
		List<StationFollowerVO> stationFollowerList = null;
		HashMap<String, Object> followerMap = new HashMap<String, Object>();
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
		//Long preStationId= 0l;
		//Long nextStationId= 0l;
		int resultSize =0 ;
		 String imageResolution = null;
		 String followType = VoizdConstant.IFOLLOW ;
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		 
		try {
				//true is asc order
				if(order){
					 stationFollowerList = stationSubDAO.getTappedStationList(followerId,startLimit,endLimit+1);
					 hasNext = true ;
				}else{
					if(startLimit==0){
						//first hit
						 isFirstRequest = true;
					}else{
						//desc order	
						 hasPre = true ;
					}
					
				 stationFollowerList = stationSubDAO.getTappedStationList(followerId,startLimit,endLimit+1);
				
				}
				logger.debug("getFollowerList stationFollowerList"+stationFollowerList);
				if(null!= stationFollowerList){
					if(endLimit<stationFollowerList.size()){
						if(order){
							stationFollowerList.remove(0);
							 hasPre = true ;
						}else{
							stationFollowerList.remove(stationFollowerList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = stationFollowerList.size();
				}
				 int cnt =0 ;
				 for(StationFollowerVO stationFollowerVO:stationFollowerList){
					 FollowerDTO stationFollowerDTO = new FollowerDTO();
					 logger.debug("getFollowerList getFolloweeId="+stationFollowerVO.getFolloweeId());
					 stationFollowerDTO.setFollowerId(stationFollowerVO.getFolloweeId());
					 UserVO userVO = userService.getUserProfile(stationFollowerVO.getFolloweeId());
					 logger.debug("getFollowerList userVO="+userVO +" "+userVO.getStatus());
					 if(null!= userVO){
						 stationFollowerDTO.setFollowerName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
						 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(followerId);
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationFollowerVO.getFolloweeId());
						 stationFollowerDTO.setFollowerProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						 if (null != userVO.getProfilePicFileId()) {
							 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
							 if(null!=mediaVO){
								 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 stationFollowerDTO.setFollowerImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
									 stationFollowerDTO.setFollowerImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
						 }
						 stationFollowerDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
						 stationFollowerDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
						 if(StringUtils.isNotBlank(userVO.getLanguage())){
							 stationFollowerDTO.setLanguage(userVO.getLanguage());
						 }
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
							 stationFollowerDTO.setAddress(address.toString());
						 }
						 List<StationVO> stationVOList = stationDAO.getMyStationList(stationFollowerVO.getFolloweeId(),VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
						 if(null!=stationVOList && stationVOList.size()>0 ){
								Long defaultStationId = stationVOList.get(0).getStationId();
								StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(defaultStationId);
								if(null!=stationCounterVO){
									Long contentCount = 0l;
									if(stationCounterVO.getNumberOfContent()>0){
										contentCount =stationCounterVO.getNumberOfContent();
									}
									UserCounterVO userCounterVO = userCounterDAO.getUserCounter(stationFollowerVO.getFolloweeId());
									logger.debug("userCounterVO--"+userCounterVO +userCounterVO.getAmplified());
									if(null!=userCounterVO && userCounterVO.getAmplified()>0){
										contentCount +=userCounterVO.getAmplified();
									}
									logger.debug("contentCount--"+contentCount);
									stationFollowerDTO.setContCnt(contentCount);	
									stationFollowerDTO.setFollowerCnt(stationCounterVO.getFollower());
								}
						 }
						 followerDtoList.add(stationFollowerDTO);
					 }
				
						cnt++;
						
					}
			} catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			} catch (MediaServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_FOLLOWER_MORE_URL);
					relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(followerId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				followerMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.USER_FOLLOWER_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(followerId);
				if(resultSize<endLimit){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);	
				}else{
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				followerMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(null!=followerDtoList && followerDtoList.size()>0){
				followerMap.put(followType, followerDtoList);
			}
		 
		return followerMap;
	}
	
	
	@Override
	public HashMap<String, Object> getMyFollowerList(Long stationId,Long creatorId, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {

		List<FollowerDTO> followerDtoList =new ArrayList<FollowerDTO>();
		List<StationFollowerVO> stationFollowerList = null;
		HashMap<String, Object> followerMap = new HashMap<String, Object>();
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
	//	Long preStationId= 0l;
	//	Long nextStationId= 0l;
		int resultSize =0 ;
		 String imageResolution = null;
		 String followType = VoizdConstant.MYFOLLOWER ;
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		 
		try {
				//true is asc order
				if(order){
					 stationFollowerList = stationSubDAO.getStationFollowerList(stationId,creatorId,startLimit,endLimit+1);
					 hasNext = true ;
				}else{
					if(startLimit==0){
						//first hit
						 isFirstRequest = true;
					}else{
						//desc order	
						 hasPre = true ;
					}
					
					 stationFollowerList = stationSubDAO.getStationFollowerList(stationId,creatorId,startLimit,endLimit+1);
				
				}
				logger.debug("getFollowerList stationFollowerList"+stationFollowerList+" order="+order);
				if(null!= stationFollowerList){
					if(endLimit<stationFollowerList.size()){
						if(order){
							stationFollowerList.remove(0);
							 hasPre = true ;
						}else{
							stationFollowerList.remove(stationFollowerList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = stationFollowerList.size();
				}
				 int cnt =0 ;
				 for(StationFollowerVO stationFollowerVO:stationFollowerList){
					 FollowerDTO stationFollowerDTO = new FollowerDTO();
					 logger.debug("getFollowerList getFolloweeId="+stationFollowerVO.getFolloweeId());
					 stationFollowerDTO.setFollowerId(stationFollowerVO.getFollowerId());
					 UserVO userVO = userService.getUserProfile(stationFollowerVO.getFollowerId());
					 logger.debug("getFollowerList userVO="+userVO +" "+userVO.getStatus());
					 if(null!= userVO){
						 stationFollowerDTO.setFollowerName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
						 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(creatorId);
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationFollowerVO.getFollowerId());
						 stationFollowerDTO.setFollowerProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						 if (null != userVO.getProfilePicFileId()) {
							 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
							 if(null!=mediaVO){
								 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 stationFollowerDTO.setFollowerImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
									 stationFollowerDTO.setFollowerImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
						 }
						 
						 stationFollowerDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
						 stationFollowerDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
						 if(StringUtils.isNotBlank(userVO.getLanguage())){
							 stationFollowerDTO.setLanguage(userVO.getLanguage());
						 }
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
							 stationFollowerDTO.setAddress(address.toString());
						 }
						 List<StationVO> stationVOList = stationDAO.getMyStationList(stationFollowerVO.getFollowerId(),VoizdConstant.STATION_START_LIMIT,VoizdConstant.STATION_END_LIMIT);
						 if(null!=stationVOList && stationVOList.size()>0 ){
								Long defaultStationId = stationVOList.get(0).getStationId();
								StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(defaultStationId);
								if(null!=stationCounterVO){
									Long contentCount = 0l;
									if(stationCounterVO.getNumberOfContent()>0){
										contentCount =stationCounterVO.getNumberOfContent();
									}
									UserCounterVO userCounterVO = userCounterDAO.getUserCounter(stationFollowerVO.getFollowerId());
									logger.debug("userCounterVO--"+userCounterVO +userCounterVO.getAmplified());
									if(null!=userCounterVO && userCounterVO.getAmplified()>0){
										contentCount +=userCounterVO.getAmplified();
									}
									logger.debug("contentCount--"+contentCount);
									
									stationFollowerDTO.setContCnt(contentCount);
									stationFollowerDTO.setFollowerCnt(stationCounterVO.getFollower());
								}
								 logger.debug("getFollowerList getCreatorId="+stationVOList.get(0).getCreatorId()+",creatorId="+creatorId+" defaultStationId="+defaultStationId);
								if(stationVOList.get(0).getCreatorId()!=creatorId){	
									if(creatorId>0){
										
										 Byte subStatus = stationSubDAO.getStationTap(defaultStationId,creatorId);
										 if(null!=subStatus){
											if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
												stationFollowerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
											}else{
												stationFollowerDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
											}
										 }else{
											 stationFollowerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS); 
										 }
										
									}else{
										stationFollowerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
										
									}
									logger.debug("getFollowerList getTapValue="+stationFollowerDTO.getTapValue());
									stationFollowerDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
								}
						 }
						 followerDtoList.add(stationFollowerDTO);
					 }
						cnt++;
						
					}
			} catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			} catch (MediaServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_STATION_MORE_FOLLOWER_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(creatorId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				followerMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.MY_STATION_MORE_FOLLOWER_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.STATION_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationId);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.FOLLOWERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(creatorId);
				if(resultSize<endLimit){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
				}else{
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				followerMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
			if(null!=followerDtoList && followerDtoList.size()>0){
				followerMap.put(followType, followerDtoList);
			}
		 
		return followerMap;
	}
	
	@Override
	public HashMap<String, Object> getTopVoizerList(Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		List<VoizerDTO> voizerDtoList =new ArrayList<VoizerDTO>();
		List<StationCounterVO> stationCounterVOList = null;
		HashMap<String, Object> voizerMap = new HashMap<String, Object>();
		boolean hasNext = false ;
		boolean hasPre = false ;
		boolean isFirstRequest = false ;
		int resultSize =0 ;
		 String imageResolution = null;
		 String topVoizer = VoizdConstant.TOPVOIZER ;
		 if(clientMap!=null && null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
			 imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
		 }else{
			 imageResolution = MediaUtil.getImageSize(null);
		 }
		try {
				//true is asc order
				if(order){
					 stationCounterVOList = stationCounterDAO.getStationCounterList(startLimit,endLimit+1);
					 hasNext = true ;
				}else{
					if(startLimit==0){
						//first hit
						 isFirstRequest = true;
					}else{
						//desc order	
						 hasPre = true ;
					}
					 stationCounterVOList = stationCounterDAO.getStationCounterList(startLimit,endLimit+1);
				}
				logger.debug("getTopVoizerList stationCounterVOList"+stationCounterVOList+" order="+order);
				if(null!= stationCounterVOList){
					if(endLimit<stationCounterVOList.size()){
						if(order){
							stationCounterVOList.remove(0);
							 hasPre = true ;
						}else{
							stationCounterVOList.remove(stationCounterVOList.size()-1);
						}
						hasNext = true ;
					}
					resultSize = stationCounterVOList.size();
				}
				 int cnt =0 ;
				 for(StationCounterVO stationCounterVO:stationCounterVOList){
					 VoizerDTO voizerDTO = new VoizerDTO();
					 StationVO stationVO = stationDAO.getActiveStationInfo(stationCounterVO.getStationId());
					if(null!=stationVO){
						voizerDTO.setVoizerId(stationVO.getCreatorId());
						UserVO userVO = userService.getUserProfile(stationVO.getCreatorId());
					 logger.debug("getFollowerList userVO="+userVO +" "+userVO.getStatus());
					 if(null!= userVO){
						 voizerDTO.setVoizerName(UserUtils.displayName(userVO.getFirstName(), userVO.getLastName()));
						 StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.USER_PUBLIC_PROFILE_URL);
						 relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdConstant.VISITOR_ID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(userId);
						 relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.USERID).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(stationVO.getCreatorId());
						 voizerDTO.setProfileUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),relativeUrl.toString()));
						 if (null != userVO.getProfilePicFileId()) {
							 MediaVO mediaVO = mediaService.getMediaInfo(userVO.getProfilePicFileId());
							 if(null!=mediaVO){
								 mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
								 voizerDTO.setImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
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
									 voizerDTO.setImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(defaultMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(defaultMediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
						 }
						 voizerDTO.setLatitude(userVO.getLatitude());
						 voizerDTO.setLongitude(userVO.getLongitude());
						 voizerDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
						 voizerDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
						 if(StringUtils.isNotBlank(userVO.getLanguage())){
							 voizerDTO.setLanguage(userVO.getLanguage());
						 }
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
							 voizerDTO.setAddress(address.toString());
						 }
						 	Long contentCount = 0l;
							if(stationCounterVO.getNumberOfContent()>0){
								contentCount =stationCounterVO.getNumberOfContent();
							}
							UserCounterVO userCounterVO = userCounterDAO.getUserCounter(stationVO.getCreatorId());
							logger.debug("userCounterVO--"+userCounterVO +userCounterVO.getAmplified());
							if(null!=userCounterVO && userCounterVO.getAmplified()>0){
								contentCount +=userCounterVO.getAmplified();
							}
							logger.debug("contentCount--"+contentCount);
						 voizerDTO.setContCnt(contentCount);
						 voizerDTO.setFollowerCnt(stationCounterVO.getFollower());

								 logger.debug("getFollowerList getCreatorId="+stationVO.getCreatorId()+",userId="+userId);
								if(stationVO.getCreatorId()!=userId){	
									if(userId>0){
										
										 Byte subStatus = stationSubDAO.getStationTap(stationVO.getStationId(),userId);
										 if(null!=subStatus){
											if(VoizdConstant.STATION_UNFOLLOW_STATUS.equals(subStatus)){
												voizerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
											}else{
												voizerDTO.setTapValue(VoizdConstant.STATION_UNFOLLOW_STATUS);
											}
										 }else{
											 voizerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS); 
										 }
									}else{
										voizerDTO.setTapValue(VoizdConstant.STATION_FOLLOW_STATUS);
										
									}
									logger.debug("getFollowerList getTapValue="+voizerDTO.getTapValue());
									voizerDTO.setTapUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.STATION_TAP_URL));
								}
						 voizerDtoList.add(voizerDTO);
					 }
						cnt++;	
				 }		
			 }
			} catch (DataAccessFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			} catch (MediaServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
			}catch (UserServiceFailedException e) {
				throw new StationServiceException(ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION);
			}
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.TOP_VOIZER_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.DESCENDING);
				voizerMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder( VoizdRelativeUrls.TOP_VOIZER_MORE_URL);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				if(resultSize<endLimit){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
				}else{
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationParam.DIRECTION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.ASCENDING);
				voizerMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
				
			}
			if(null!=voizerDtoList && voizerDtoList.size()>0){
				voizerMap.put(topVoizer, voizerDtoList);
			}
		 
		return voizerMap;
	}
	
	
	@Override
	public List<StationVO> getMyStationList(Long creatorId, int startLimit,int endLimit)  throws StationServiceException {
		try {
			return stationDAO.getMyStationList(creatorId,startLimit,endLimit);
		} catch (DataAccessFailedException e) {
			throw new StationServiceException(ErrorCodesEnum.STATION_GET_SERVICE_FAILED_EXCEPTION);
		}
	}
	@Override
	public StationCounterVO getStationCounter(Long stationId)throws StationServiceException {
		try {
			return stationCounterDAO.getStationCounter(stationId);
		} catch (DataAccessFailedException e) {
			throw new StationServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);
		}
	}
	
	public UserCounterVO getUserCounter(Long userId) throws StationServiceException{
		try {
			return userCounterDAO.getUserCounter(userId);
		} catch (DataAccessFailedException e) {
			throw new StationServiceException(ErrorCodesEnum.USER_COUNTER_SERVICE_FAILED_EXCEPTION);
		}
	}

}
