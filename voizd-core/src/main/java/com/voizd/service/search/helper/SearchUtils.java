package com.voizd.service.search.helper;

import java.util.ArrayList;
import java.util.List;

import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.util.DateTimeUtils;

public class SearchUtils {
	
	public static StationSearchVO transformStationSearchVO(StationVO stationVO,UserVO userVO){		
		StationSearchVO stationSearchVO = null;
		if(stationVO != null){
			stationSearchVO = new StationSearchVO();
			stationSearchVO.setStationId(stationVO.getStationId());
			stationSearchVO.setStationName(stationVO.getStationName());
			stationSearchVO.setStatus(stationVO.getStatus());
			stationSearchVO.setCategory(stationVO.getCategory());
			stationSearchVO.setDescription(stationVO.getDescription());
			stationSearchVO.setTag(stationVO.getTag());
			stationSearchVO.setLanguage(stationVO.getLanguage());
			stationSearchVO.setLocation(stationVO.getLocation());
			stationSearchVO.setCreatorId(stationVO.getCreatorId());
			stationSearchVO.setIsAdult(stationVO.isAdult() ?  1 : 0 );
			stationSearchVO.setFirstName(userVO.getFirstName());
			stationSearchVO.setLastName(userVO.getLastName());
			stationSearchVO.setFileId(stationVO.getFileId());
			stationSearchVO.setHasContent(stationVO.isHasContent() ?  1 : 0);
			}
		return stationSearchVO;
	}
	
	public static ContentSearchVO transformContentSearchVO(ContentVO contentVO,UserVO userVO){		
		ContentSearchVO contentSearchVO = null;
		if(contentVO != null){
			contentSearchVO = new ContentSearchVO();
			contentSearchVO.setStationId(contentVO.getStationId());
			contentSearchVO.setContentId(contentVO.getContentId());
			contentSearchVO.setStatus(contentVO.getStatus());
			String title=contentVO.getTitle();
			String tag = contentVO.getTag();
			String tagCloud = contentVO.getTagCloud();
			if(tagCloud != null){
				contentSearchVO.setTag(tagCloud);
				//contentSearchVO.setTitle(title);
			}/*else{
				 contentSearchVO.setTag(title);
				 contentSearchVO.setTitle(title);
			}*/
			if(tag != null){
				 contentSearchVO.setSearch(title+" "+tag);
			}else{
				contentSearchVO.setSearch(title);
			}
			contentSearchVO.setTitle(title);
			contentSearchVO.setCreatorId(contentVO.getCreatorId());
			contentSearchVO.setCity(contentVO.getCity());
			contentSearchVO.setCountry(contentVO.getCountry());
			contentSearchVO.setLanguage(contentVO.getLanguage());
			contentSearchVO.setLatitude(contentVO.getLatitude());
			contentSearchVO.setLongitude(contentVO.getLongitude());
			contentSearchVO.setLocation(contentVO.getLocation());
			contentSearchVO.setUserFileid(userVO.getProfilePicFileId());
			contentSearchVO.setFirstName(userVO.getFirstName());
			contentSearchVO.setLastName(userVO.getLastName());
			contentSearchVO.setCreatedDate(DateTimeUtils.getGmtTime());
			contentSearchVO.setFileId(contentVO.getFileIds());
			contentSearchVO.setWeblink(contentVO.getWeblink());
		}
		return contentSearchVO;
	}


	public static ContentSearchVO transformContentSearchVOByProfile(ContentVO contentVO ,UserVO userVO,UserVO dbUserVO){		
		ContentSearchVO contentSearchVO = null;
		if(userVO != null){
			contentSearchVO = new ContentSearchVO();
			if(userVO.getFirstName() !=null){
				contentSearchVO.setFirstName(userVO.getFirstName());
			}else{
				contentSearchVO.setFirstName(dbUserVO.getFirstName());
			}
			if(userVO.getLastName() !=null){
				contentSearchVO.setLastName(userVO.getLastName());
			}else{
				contentSearchVO.setLastName(userVO.getLastName());
			}
			contentSearchVO.setStationId(contentVO.getStationId());
			contentSearchVO.setContentId(contentVO.getContentId());
			contentSearchVO.setStatus(contentVO.getStatus());
			contentSearchVO.setDescription(contentVO.getTitle());
			contentSearchVO.setTag(contentVO.getTag());
			contentSearchVO.setCreatorId(contentVO.getCreatorId());
		}
		return contentSearchVO;
	}
	public static StationSearchVO transformStationSearchVOByProfile(StationVO stationVO ,UserVO userVO,UserVO dbUserVO){		
		StationSearchVO stationSearchVO = null;
		if(userVO != null){
			stationSearchVO = new StationSearchVO();
			if(userVO.getFirstName() !=null){
				stationSearchVO.setFirstName(userVO.getFirstName());
			}else{
				stationSearchVO.setFirstName(dbUserVO.getFirstName());
			}
			if(userVO.getLastName() !=null){
				stationSearchVO.setLastName(userVO.getLastName());
			}else{
				stationSearchVO.setLastName(userVO.getLastName());
			}
			stationSearchVO.setStationId(stationVO.getStationId());
			stationSearchVO.setStationName(stationVO.getStationName());
			stationSearchVO.setStatus(stationVO.getStatus());
			stationSearchVO.setCategory(stationVO.getCategory());
			stationSearchVO.setDescription(stationVO.getDescription());
			stationSearchVO.setTag(stationVO.getTag());
			stationSearchVO.setLanguage(stationVO.getLanguage());
			stationSearchVO.setLocation(stationVO.getLocation());
			stationSearchVO.setCreatorId(stationVO.getCreatorId());
			stationSearchVO.setIsAdult(stationVO.isAdult() ?  1 : 0 );
			stationSearchVO.setFileId(stationVO.getFileId());
		}
		return stationSearchVO;
	}
	
	public static List<StationVO> getStationSearchResponse(List<StationSearchVO> stationSearchVOList) {
		List<StationVO> listStationVO = new ArrayList<StationVO>();
		for (StationSearchVO stationSearchVO : stationSearchVOList) {
			listStationVO.add(getStationVO(stationSearchVO));
		}
		return listStationVO;
	}
	
	public static List<ContentVO> getContentSearchResponse(List<ContentSearchVO> contentSearchVOList) {
		List<ContentVO> listStationVO = new ArrayList<ContentVO>();
		for (ContentSearchVO contentSearchVO : contentSearchVOList) {
			listStationVO.add(getContentVO(contentSearchVO));
		}
		return listStationVO;
	}

	private static ContentVO getContentVO(ContentSearchVO contentSearchVO){
		ContentVO contentVO = new ContentVO();
		contentVO.setContentId(contentSearchVO.getContentId());
		contentVO.setTitle(contentSearchVO.getDescription());
		contentVO.setTag(contentSearchVO.getTag());
		contentVO.setStationId(contentSearchVO.getStationId());
		contentVO.setStatus(contentSearchVO.getStatus());
		//contentVO.setCreatedDate(contentSearchVO.getCreatedDate().);
		return contentVO;
	}
	private static StationVO getStationVO(StationSearchVO stationSearchVO){
		StationVO stationVO = new StationVO();
		stationVO.setCreatorId(stationSearchVO.getCreatorId());
		stationVO.setStationId(stationSearchVO.getStationId());
		stationVO.setStationName(stationSearchVO.getStationName());
		stationVO.setStatus(stationSearchVO.getStatus());
		stationVO.setAdult(stationSearchVO.getIsAdult() == 1  ? true : false);
		stationVO.setDescription(stationSearchVO.getDescription());
		stationVO.setLanguage(stationSearchVO.getLanguage());
		stationVO.setLocation(stationSearchVO.getLocation());
		stationVO.setTag(stationSearchVO.getTag());
		stationVO.setFileId(stationSearchVO.getFileId());
		//stationVO.setCreatedDate(stationSearchVO.getCreatedDate());
		return stationVO;
	}
}
