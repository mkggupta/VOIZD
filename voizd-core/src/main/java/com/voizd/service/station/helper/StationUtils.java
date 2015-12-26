package com.voizd.service.station.helper;

import java.util.ArrayList;
import java.util.List;

import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.entities.Station;

public class StationUtils {

	public static Station transformStation(StationVO newStationVO,StationVO oldStationVO){		
		Station station = null;
		if(newStationVO != null){
			station = new Station();
			
			station.setStationId(oldStationVO.getStationId());
			
			if(newStationVO.getTag()!=null){
				station.setTag(newStationVO.getTag());
			}else{
				station.setTag(oldStationVO.getTag());
			}
			if(newStationVO.getLanguage()!=null){
				station.setLanguage(newStationVO.getLanguage());
			}else{
				station.setLanguage(oldStationVO.getLanguage());	
			}
			if(newStationVO.getLocation()!=null){
				station.setLocation(newStationVO.getLocation());
			}else{
				station.setLocation(oldStationVO.getLocation());
			}
			if(newStationVO.getDescription()!=null){
				station.setDescription(newStationVO.getDescription());
			}else{
			    station.setDescription(oldStationVO.getDescription());	
			}
			
			if(newStationVO.getFileIds() != null){
				station.setFileIdList(getListOfFileId(newStationVO.getFileIds()));
				station.setFileId(getListOfFileId(newStationVO.getFileIds()).get(0));
			}else{
				station.setFileId(oldStationVO.getFileId());
			}
			
		}
		return station;
	}
	
	
	public static StationMediaVO transformStationMedia(MediaVO mediaVO,Long stationId,int order){		
		StationMediaVO stationMediaVO =null;
	    if(mediaVO != null){
	    	stationMediaVO = new StationMediaVO();
	    	stationMediaVO.setFileId(mediaVO.getFileId());
	    	stationMediaVO.setMediaType(mediaVO.getMediaType());
	    	stationMediaVO.setExt(mediaVO.getMimeType());
	    	stationMediaVO.setStatus((byte)1);
	    	stationMediaVO.setSize(mediaVO.getSize());
	    	stationMediaVO.setStationId(stationId);
	    	stationMediaVO.setOrdering(order);
	    }
		return stationMediaVO;
	}
	
	public static ContentMediaVO transformContentMedia(MediaVO mediaVO,Long contentId,int order){		
		ContentMediaVO contentMediaVO =null;
	    if(mediaVO != null){
	    	contentMediaVO = new ContentMediaVO();
	    	contentMediaVO.setFileId(mediaVO.getFileId());
	    	contentMediaVO.setMediaType(mediaVO.getMediaType());
	    	contentMediaVO.setExt(mediaVO.getMimeType());
	    	contentMediaVO.setStatus((byte)1);
	    	contentMediaVO.setSize(mediaVO.getSize());
	    	contentMediaVO.setContentId(contentId);
	    	contentMediaVO.setOrdering(order);
	    }
		return contentMediaVO;
	}
	
	public static List<String> getListOfFileId(String fileIds){		
		String files[] = fileIds.split("\\s*,\\s*");
		ArrayList<String> fileList= null;
		if(files != null && files.length >0){
			fileList = new ArrayList<String>();
		for(String fileId : files){
			fileList.add(fileId);
		  }
		}
		return fileList;
	}
	
	public static StationVO getDefaultStationVO(UserVO userVO){		
		StationVO stationVO = null;
		if(userVO.getId() >0){
			stationVO = new StationVO();
			stationVO.setStationName(VoizdConstant.DEFAULT_STATION_NAME);
			//if(userVO.getStatus()>0){
				stationVO.setStatus(VoizdConstant.ACTIVE_STATION_STATUS);
			//}else{
			//    stationVO.setStatus(VoizdConstant.DEFAULT_STATION_STATUS);
		//	}
			stationVO.setLanguage(userVO.getLanguage());
			stationVO.setCreatorId(userVO.getId());
			stationVO.setDescription(VoizdConstant.DEFAULT_STATION_DESCRIPTION);
			stationVO.setFileId(userVO.getProfilePicFileId());
			stationVO.setLocation(userVO.getLocation());
		}
		return stationVO;
	}
	
}
