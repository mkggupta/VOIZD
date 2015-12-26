package com.voizd.rest.helper.manager;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.LikeVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.rest.constant.VoizdWebConstant;

public class StationHelperManager {
	private static Logger logger = LoggerFactory.getLogger(StationHelperManager.class);
	public static StationVO enrichStationVo(HashMap<String, String> requestMap){
		StationVO  stationVO  = new StationVO();
		
		if(null!=requestMap.get(VoizdStationParam.STATION_NAME)){
			stationVO.setStationName(requestMap.get(VoizdStationParam.STATION_NAME));
		}
		if(null!=requestMap.get(VoizdStationParam.TAG)){
			stationVO.setTag(requestMap.get(VoizdStationParam.TAG));
		}
		if(null!=requestMap.get(VoizdStationParam.CATEGORY)){
			stationVO.setCategory(requestMap.get(VoizdStationParam.CATEGORY));
		}
		if(null!=requestMap.get(VoizdStationParam.DESCRIPTION)){
			stationVO.setDescription(requestMap.get(VoizdStationParam.DESCRIPTION));
		}
		if(null!=requestMap.get(VoizdStationParam.USERID)){
			stationVO.setCreatorId(Long.parseLong(requestMap.get(VoizdStationParam.USERID).toString()));
		}
		if(null!=requestMap.get(VoizdStationParam.LOCATION)){
			stationVO.setLocation(requestMap.get(VoizdStationParam.LOCATION));
		}
		if(null!=requestMap.get(VoizdStationParam.LANGUAGE)){
			stationVO.setLanguage(requestMap.get(VoizdStationParam.LANGUAGE));
		}
		if(null!=requestMap.get(VoizdStationParam.ISADULT)){
			stationVO.setAdult((requestMap.get(VoizdStationParam.ISADULT).toString().equalsIgnoreCase(VoizdWebConstant.YES)?true:false));
		}
		if(null!=requestMap.get(VoizdStationParam.FILEIDS)){
			stationVO.setFileIds(requestMap.get(VoizdStationParam.FILEIDS));
		}
		
		
		stationVO.setStatus(VoizdWebConstant.DEFAULT_STATION_STATUS);
		
		return stationVO;
	}
	
	
	
	public static LikeVO enrichStationLikeVO(HashMap<String, String> requestMap) {
			LikeVO likeVO = new LikeVO();
			Byte status = 0 ;
			if(null!=requestMap.get(VoizdStationParam.STATION_ID)){
				likeVO.setId(Long.parseLong(requestMap.get(VoizdStationParam.STATION_ID)));
			}
			if(null!=requestMap.get(VoizdStationContentParam.CONTENT_ID)){
				likeVO.setId(Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID)));
			}
			likeVO.setUserId(Long.parseLong(requestMap.get(VoizdStationParam.USERID)));
			if(null!=requestMap.get(VoizdStationParam.LIKE)){
				status = Byte.parseByte(requestMap.get(VoizdStationParam.LIKE).toString());
				if(status.equals(VoizdConstant.STATION_LIKE_STATUS)){
					likeVO.setLike(true);
				}else{
					likeVO.setLike(false);
				}
			}
			logger.debug("enrichStationLikeVO,"+likeVO.getId()+"-"+likeVO.getUserId()+"-"+likeVO.isLike());
		return likeVO;
	}
	
	/*public static StationVO enrichUpdateStationVo(HttpServletRequest httpServletRequest){
		StationVO  stationVO  = new StationVO();
		
		if(null!=httpServletRequest.getParameter(VoizdStationParam.STATION_ID)){
			stationVO.setStationId(Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.USERID)){
			stationVO.setCreatorId(Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.TAG)){
			stationVO.setTag(httpServletRequest.getParameter(VoizdStationParam.TAG));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.CATEGORY)){
			stationVO.setCategory(httpServletRequest.getParameter(VoizdStationParam.CATEGORY));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.DESCRIPTION)){
			stationVO.setDescription(httpServletRequest.getParameter(VoizdStationParam.DESCRIPTION));
		}
		
		if(null!=httpServletRequest.getParameter(VoizdStationParam.LOCATION)){
			stationVO.setLocation(httpServletRequest.getParameter(VoizdStationParam.LOCATION));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.LANGUAGE)){
			stationVO.setLanguage(httpServletRequest.getParameter(VoizdStationParam.LANGUAGE));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.ISADULT)){
			stationVO.setAdult((httpServletRequest.getParameter(VoizdStationParam.ISADULT).toString().equalsIgnoreCase(VoizdWebConstant.YES)?true:false));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.FILEIDS)){
			stationVO.setFileIds(httpServletRequest.getParameter(VoizdStationParam.FILEIDS));
		}
		if(null!=httpServletRequest.getParameter(VoizdStationParam.STATUS)){
		 stationVO.setStatus(Byte.parseByte(httpServletRequest.getParameter(VoizdStationParam.STATUS)));
		}
		
		return stationVO;
	}*/
	
	public static StationVO enrichUpdateStationVo(HashMap<String, String> requestMap){
		StationVO  stationVO  = new StationVO();
		
		if(null!=requestMap.get(VoizdStationParam.STATION_ID)){
			stationVO.setStationId(Long.parseLong(requestMap.get(VoizdStationParam.STATION_ID)));
			
		}
	
		if(null!=requestMap.get(VoizdStationParam.USERID)){
			stationVO.setCreatorId(Long.parseLong(requestMap.get(VoizdStationParam.USERID)));
		}
		if(null!=requestMap.get(VoizdStationParam.TAG)){
			stationVO.setTag(requestMap.get(VoizdStationParam.TAG));
		}
		if(null!=requestMap.get(VoizdStationParam.CATEGORY)){
			stationVO.setCategory(requestMap.get(VoizdStationParam.CATEGORY));
		}
		if(null!=requestMap.get(VoizdStationParam.DESCRIPTION)){
			stationVO.setDescription(requestMap.get(VoizdStationParam.DESCRIPTION));
		}
		
		if(null!=requestMap.get(VoizdStationParam.LOCATION)){
			stationVO.setLocation(requestMap.get(VoizdStationParam.LOCATION));
		}
		if(null!=requestMap.get(VoizdStationParam.LANGUAGE)){
			stationVO.setLanguage(requestMap.get(VoizdStationParam.LANGUAGE));
		}
		if(null!=requestMap.get(VoizdStationParam.ISADULT)){
			stationVO.setAdult((requestMap.get(VoizdStationParam.ISADULT).toString().equalsIgnoreCase(VoizdWebConstant.YES)?true:false));
		}
		if(null!=requestMap.get(VoizdStationParam.FILEIDS)){
			stationVO.setFileIds(requestMap.get(VoizdStationParam.FILEIDS));
		}
		if(null!=requestMap.get(VoizdStationParam.STATUS)){
		    stationVO.setStatus(Byte.parseByte(requestMap.get(VoizdStationParam.STATUS)));
		}
		
		return stationVO;
	}

}
