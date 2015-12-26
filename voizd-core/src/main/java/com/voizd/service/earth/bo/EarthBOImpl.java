package com.voizd.service.earth.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.EarthVO;
import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.entities.MapInfo;
import com.voizd.dao.entities.MediaVoice;
import com.voizd.dao.entities.TagMediaInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.earth.EarthDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.dao.mongo.service.MongoServiceImpl;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.service.earth.exception.EarthServiceException;
import com.voizd.service.media.v1_0.MediaService;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.user.bo.UserBO;

public class EarthBOImpl implements EarthBO {
	Logger logger = LoggerFactory.getLogger(EarthBOImpl.class);
	private EarthDAO earthDAO;
	private MediaDAO mediaDAO;
	private MediaService  mediaService;
	private SearchBO searchBO;
	private UserBO userBO;
	
    public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setSearchBO(SearchBO searchBO) {
		this.searchBO = searchBO;
	}

	public void setEarthDAO(EarthDAO earthDAO) {
		this.earthDAO = earthDAO;
	}

	@Override
	public HashMap<String, Object> getEarthView(String country, int limit) throws EarthServiceException {
		HashMap<String, Object> erathMap = new HashMap<String, Object>();
		HashMap<String, Object> mapOne = getView(VoizdConstant.INDIA, limit,VoizdConstant.INDIA_CODE);
		HashMap<String, Object> mapTwo = getView(VoizdConstant.UNITED_STATES, limit,VoizdConstant.US_CODE);
		erathMap.put(VoizdConstant.GLOBE_PART_ONE, mapOne);
		erathMap.put(VoizdConstant.GLOBE_PART_TWO, mapTwo);
		return erathMap;
	}

	private HashMap<String, Object> getView(String country, int limit, String type) {
		HashMap<String, Object> erathMap = new HashMap<String, Object>();
		try {
			EarthVO earthVO = null;
			logger.debug("getView : conuntry ::"+country+" , limit :: "+limit);
			List<EarthInfo> earthInfoList = earthDAO.getEarthInfo(country, limit);
			List<EarthVO> tagList = new ArrayList<EarthVO>();
			int id = 1;
			for (EarthInfo erathInfo : earthInfoList) {
				earthVO = new EarthVO();
				HashMap<Integer, Object> gridMap = new HashMap<Integer, Object>();
				gridMap.put(id, erathInfo.getCountry());
				earthVO.setGridInfo(gridMap);
				HashMap<Integer, Object> tagMap = new HashMap<Integer, Object>();
				tagMap.put(1, erathInfo.getTagName());
				earthVO.setTags(tagMap);
				tagList.add(earthVO);
				id++;
			}
		
			MediaVoice mediaVoice = null;
			if(VoizdConstant.INDIA_CODE.equalsIgnoreCase(type)){
				mediaVoice = mediaDAO.getMediaVoice(1); // is number will be dynamic 
			}else{
				mediaVoice = mediaDAO.getMediaVoice(2);
			}
			logger.debug("getView :: mediaVoice :::: "+mediaVoice);
			
			String audioUrl = VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVoice.getMediaType(),MediaUtil.getMediaContentUrl(mediaVoice.getFileId(),mediaVoice.getExt(),""),false));
			erathMap.put(VoizdConstant.TAG_URL,VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.TAG_URL));
			erathMap.put(VoizdConstant.LOCATION_URL,VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.LOCATION_URL));
			erathMap.put(VoizdConstant.AUDIO_URL,audioUrl);
			erathMap.put(VoizdConstant.GRID,VoizdConstant.GRID_COUNT);
			erathMap.put(VoizdConstant.GRID_HOME, tagList);
			erathMap.put(VoizdConstant.MAP_VIEW, type);
		} catch (DataAccessFailedException e) {
			logger.error("DataAccessFailedException in get view " + e.getLocalizedMessage(), e);
		} catch (Exception e) {
			logger.error("Exception in get view " + e.getLocalizedMessage(), e);
		}
		return erathMap;
	}

	@Override
	public HashMap<String, Object> getLocationView(GlobeVO globeVO) throws EarthServiceException {
		HashMap<String, Object> earthMap = new HashMap<String, Object>();
		try {
			logger.debug("getLocationView : conuntry ::"+globeVO.getLocation());
			int startLimit=0;
			int endLimit=10;
			logger.debug("getLocationView : start ::"+globeVO.getStart() +" , end :: "+globeVO.getEnd() );
			if(globeVO.getDirection() == 1){
				startLimit = globeVO.getStart();
				endLimit = globeVO.getEnd();
			}
			
			List<EarthInfo> locationTagList = earthDAO.getLocationTag(globeVO.getLocation(),startLimit,endLimit+1);
			boolean hasNext = false ;
			if(locationTagList != null){
				if(endLimit<locationTagList.size()){
					hasNext=true;
					locationTagList.remove(locationTagList.size()-1);
				}
				
			}
			endLimit = locationTagList.size();
		
			if(null!=locationTagList && locationTagList.size()>0){
				List<EarthInfo> finalLocationTagList = new ArrayList<EarthInfo>();
				for (EarthInfo earthInfo : locationTagList) {
					EarthInfo earthInfoVO  = new EarthInfo();
					List<String> audioUrlList = new ArrayList<String>();
					List<TagMediaInfo> tagMediaInfo = new ArrayList<TagMediaInfo>();
					String fileIdArr[] = earthInfo.getFileIds().split("\\s*,\\s*");
					String audioUrl = null;
					for(String fileId:fileIdArr){
						audioUrl = VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.AUDIO,MediaUtil.getMediaContentUrl(fileId,VoizdConstant.MP3,null),true));
						MediaVO mediaVO = mediaService.getMediaInfo(fileId);
						if(null!=mediaVO){
							TagMediaInfo  tagMedia = new TagMediaInfo();
							tagMedia.setAudioUrl(audioUrl);
							tagMedia.setDuration(DateTimeUtils.getTimeInSecond(mediaVO.getDuration()));
							tagMediaInfo.add(tagMedia);
						}
						audioUrlList.add(audioUrl);
					}
					earthInfoVO.setTagId(earthInfo.getTagId());
					earthInfoVO.setTagName(earthInfo.getTagName());
					earthInfoVO.setAudioUrl(audioUrlList);
					earthInfoVO.setTagMediaInfo(tagMediaInfo);
					earthInfoVO.setContCnt(earthInfo.getContCnt());
					finalLocationTagList.add(earthInfoVO);
				}
				
			 earthMap.put("tags",finalLocationTagList);
			 
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.LOCATION_URL_MORE);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.LOCATION).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(globeVO.getLocation());
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.NEXT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(VoizdConstant.HAS_NEXT);
				earthMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			 earthMap.put(VoizdConstant.TAG_URL, VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.TAG_URL));
		} catch (DataAccessFailedException e) {
			logger.error("DataAccessFailedException in  getLocationView " + e.getLocalizedMessage(), e);
		}catch (Exception e) {
			logger.error("Exception in getLocationView " + e.getLocalizedMessage(), e);
		}
		return earthMap;
	}

	@Override
	public HashMap<String, Object> getTagView(GlobeVO globeVO,Map<String, Object> clientMap) throws EarthServiceException {
		HashMap<String, Object> erathMap = new HashMap<String, Object>();
		try{
			SearchVO searchVO = new SearchVO();
			searchVO.setTag(globeVO.getTag());
			searchVO.setUserId(globeVO.getUserId());
			erathMap = searchBO.getContentSearch(searchVO,0,10,clientMap);
		}catch(Exception e){
			logger.error("Exception in getTagView " + e.getLocalizedMessage(), e);
		}
		return erathMap;
	}
	
	
	public HashMap<String, Object> getVTags(int startLimit, int endLimit,boolean order) throws EarthServiceException{

		HashMap<String, Object> earthMap = new HashMap<String, Object>();
		try {
			boolean hasNext = false ;
			boolean hasPre = false ;
			int resultSize =0 ;

			List<EarthInfo> locationTagList = earthDAO.getTrendingTag(startLimit,endLimit+1);
			//true is asc order
			if(order){
				 hasNext = true ;
			}else{
				if(startLimit>0){
					//desc order	
					 hasPre = true ;
				}
			}
			if(locationTagList != null){
				if(endLimit<locationTagList.size()){
					if(order){
						locationTagList.remove(0);
						 hasPre = true ;
					}else{
						locationTagList.remove(locationTagList.size()-1);
						hasNext=true;
					}
				}
				
			}
			endLimit = locationTagList.size();
			resultSize = locationTagList.size();
			if(null!=locationTagList && locationTagList.size()>0){
				List<EarthInfo> finalLocationTagList = new ArrayList<EarthInfo>();
				for (EarthInfo earthInfo : locationTagList) {
					EarthInfo earthInfoVO  = new EarthInfo();
					List<String> audioUrlList = new ArrayList<String>();
					List<TagMediaInfo> tagMediaInfo = new ArrayList<TagMediaInfo>();
					String fileIdArr[] = earthInfo.getFileIds().split("\\s*,\\s*");
					String audioUrl = null;
					for(String fileId:fileIdArr){
						audioUrl = VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.AUDIO,MediaUtil.getMediaContentUrl(fileId,VoizdConstant.MP3,null),true));
						MediaVO mediaVO = mediaService.getMediaInfo(fileId);
						if(null!=mediaVO){
							TagMediaInfo  tagMedia = new TagMediaInfo();
							tagMedia.setAudioUrl(audioUrl);
							tagMedia.setDuration(DateTimeUtils.getTimeInSecond(mediaVO.getDuration()));
							tagMediaInfo.add(tagMedia);
						}
						audioUrlList.add(audioUrl);
					}
					earthInfoVO.setTagId(earthInfo.getTagId());
					earthInfoVO.setTagName(earthInfo.getTagName());
					earthInfoVO.setAudioUrl(audioUrlList);
					earthInfoVO.setContCnt(earthInfo.getContCnt());
					earthInfoVO.setTagMediaInfo(tagMediaInfo);
					finalLocationTagList.add(earthInfoVO);
				}
				earthMap.put("tags",finalLocationTagList);
			 
			}
			
			if(hasNext){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.V_TAG_URL_MORE);
				relativeUrl.append(VoizdRelativeUrls.QUERY_PARAM).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit+resultSize);
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				earthMap.put("next", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			if(hasPre){
				StringBuilder relativeUrl= new StringBuilder(VoizdRelativeUrls.V_TAG_URL_MORE);
				if(resultSize<endLimit){
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(0);
				}else{
					relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.STARTLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(startLimit-resultSize);
				}
				relativeUrl.append(VoizdRelativeUrls.PARAM_SEPARATOR).append(VoizdStationContentParam.ENDLIMIT).append(VoizdRelativeUrls.VALUE_SEPARATOR).append(endLimit);
				
				earthMap.put("pre", VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), relativeUrl.toString()));
			}
			 earthMap.put(VoizdConstant.TAG_URL, VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.TAG_URL));
		} catch (DataAccessFailedException e) {
			logger.error("DataAccessFailedException in  getVTags " + e.getLocalizedMessage(), e);
			throw new EarthServiceException(ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION);
		}catch (Exception e) {
			logger.error("Exception in getVTags " + e.getLocalizedMessage(), e);
			throw new EarthServiceException(ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION);
		}
		return earthMap;
	
		
	}

	@Override
	public HashMap<String, Object> getMapView(MapVO mapVO,Map<String, Object> clientMap) throws EarthServiceException {
		HashMap<String, Object> earthMap = new HashMap<String, Object>();
		try {
			MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
			//ArrayList<MapInfo> mapInfoList = mongoServiceImpl.getTagList(mapVO.getCountry(), mapVO.getLimit());
			//ArrayList<MapInfo> mapInfoList = mongoServiceImpl.getTagList(mapVO.getCountry(),20);
			ArrayList<MapInfo> mapInfoList = mongoServiceImpl.getMapTagList(mapVO,10);
			String platform = VoizdConstant.DEFAULT_PLATFORM;
			if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
				 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
			 }
			MediaVoice mediaVoice = mediaDAO.getMediaVoice(1); // is number will be dynamic 
			logger.debug("getView :: mapVO.getCountry() :::: "+mapVO.getCountry()+" IMAGESIZE="+clientMap.get(ClientParamConstant.IMAGESIZE)+"THUMBSIZE= "+clientMap.get(ClientParamConstant.THUMBSIZE));
			String imageResolution = null;
			  if (clientMap != null && null !=clientMap.get(ClientParamConstant.THUMBSIZE)) {
			      imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.THUMBSIZE).toString());
			  } else {
			      imageResolution = MediaUtil.getImageSize(null);
			  }
			  ArrayList<MapInfo> tagInfoList =  new ArrayList<MapInfo>();
			  for(MapInfo mapInfo : mapInfoList){
				UserVO userVO = userBO.getUserProfile(mapInfo.getCreatorId());
				if (null != userVO.getProfilePicFileId() && null != userVO.getProfilePicFileExt()) {
					 mediaService.convertMedia(VoizdConstant.IMAGE,userVO.getProfilePicFileId(),VoizdConstant.IMAGE_JPG,imageResolution);
					mapInfo.setProfilePicUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.IMAGE,
							MediaUtil.getMediaContentUrl(userVO.getProfilePicFileId(), userVO.getProfilePicFileExt(), imageResolution), false)));
					tagInfoList.add(mapInfo);
				}
				List<ContentMediaVO> contentMediaVOList = mediaDAO.getContentMedia(mapInfo.getContentId());
				for(ContentMediaVO contentMediaVO:contentMediaVOList){
					if("audio".equalsIgnoreCase(contentMediaVO.getMediaType())){
						if(VoizdConstant.ANDROID_PLATFORM.equalsIgnoreCase(platform)&& (VoizdConstant.MP3.equalsIgnoreCase(contentMediaVO.getExt())||VoizdConstant.AMR.equalsIgnoreCase(contentMediaVO.getExt())) ){
							mapInfo.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}else{
							 contentMediaVO.setExt(VoizdConstant.MP3);
							 mediaService.convertMedia(contentMediaVO.getMediaType(),contentMediaVO.getFileId(),contentMediaVO.getExt(),null);
							 mapInfo.setCAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(contentMediaVO.getMediaType(),MediaUtil.getMediaContentUrl(contentMediaVO.getFileId(),contentMediaVO.getExt(),null),true)));
						}
						logger.debug("getView :: mapVO.getCAudUrl() :::: "+mapInfo.getCAudUrl());
						if(StringUtils.isNotBlank(contentMediaVO.getDuration())){
							mapInfo.setCDuration(DateTimeUtils.getTimeInSecond(contentMediaVO.getDuration()));
						}
					}
				}
				
			  }
			
		String audioUrl = VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVoice.getMediaType(),MediaUtil.getMediaContentUrl(mediaVoice.getFileId(),mediaVoice.getExt(),""),false));
		earthMap.put(VoizdConstant.TAG_URL,VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(),VoizdRelativeUrls.TAG_URL));
		earthMap.put(VoizdConstant.AUDIO_URL,audioUrl);
		earthMap.put("tagList",tagInfoList);
		earthMap.put(VoizdConstant.MAP_VIEW,"MAP");
		}catch (Exception e) {
			logger.error("Exception in get view " + e.getLocalizedMessage(), e);
		}
		return earthMap;
	}
}
