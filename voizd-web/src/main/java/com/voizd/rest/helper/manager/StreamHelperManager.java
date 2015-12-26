package com.voizd.rest.helper.manager;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.constant.VoizdStreamParam;
import com.voizd.rest.constant.VoizdWebConstant;

public class StreamHelperManager {
	private static Logger logger = LoggerFactory.getLogger(StreamHelperManager.class);
	public static StreamVO enrichStreamVO(HashMap<String, String> requestMap){
		StreamVO  streamVO  = new StreamVO();
		if(null!=requestMap.get(VoizdStreamParam.STREAM_NAME)){
			streamVO.setStreamName(requestMap.get(VoizdStreamParam.STREAM_NAME));
		}
		if(null!=requestMap.get(VoizdStreamParam.TAG)){
			streamVO.setTag(requestMap.get(VoizdStreamParam.TAG));
		}
		if(null!=requestMap.get(VoizdStreamParam.CATEGORY)){
			streamVO.setCategory(requestMap.get(VoizdStreamParam.CATEGORY));
		}
		if(null!=requestMap.get(VoizdStreamParam.DESCRIPTION)){
			streamVO.setDescription(requestMap.get(VoizdStreamParam.DESCRIPTION));
		}
		if(null!=requestMap.get(VoizdStreamParam.USERID)){
			streamVO.setUserId(Long.parseLong(requestMap.get(VoizdStreamParam.USERID).toString()));
		}
		if(null!=requestMap.get(VoizdStreamParam.FILEIDS)){
			streamVO.setFileIds(requestMap.get(VoizdStreamParam.FILEIDS));
		}
		if(null!=requestMap.get(VoizdStreamParam.STREAM_STATE)){
			streamVO.setStreamState(Byte.parseByte(requestMap.get(VoizdStreamParam.STREAM_STATE).toString()));
		}
		streamVO.setStatus(VoizdWebConstant.DEFAULT_STREAM_STATUS);
		logger.debug("streamVO-----"+streamVO);
		return streamVO;
	}
	
	
	/*
	public static LikeVO enrichStationLikeVO(HashMap<String, String> requestMap) {
			LikeVO likeVO = new LikeVO();
			Byte status = 0 ;
			if(null!=requestMap.get(VoizdStreamParam.STATION_ID)){
				likeVO.setId(Long.parseLong(requestMap.get(VoizdStreamParam.STATION_ID)));
			}
			if(null!=requestMap.get(VoizdStationContentParam.CONTENT_ID)){
				likeVO.setId(Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID)));
			}
			likeVO.setUserId(Long.parseLong(requestMap.get(VoizdStreamParam.USERID)));
			if(null!=requestMap.get(VoizdStreamParam.LIKE)){
				status = Byte.parseByte(requestMap.get(VoizdStreamParam.LIKE).toString());
				if(status.equals(VoizdConstant.STATION_LIKE_STATUS)){
					likeVO.setLike(true);
				}else{
					likeVO.setLike(false);
				}
			}
			logger.debug("enrichStationLikeVO,"+likeVO.getId()+"-"+likeVO.getUserId()+"-"+likeVO.isLike());
		return likeVO;
	}
	
	public static StationVO enrichUpdateStationVo(HttpServletRequest httpServletRequest){
		StationVO  streamVO  = new StationVO();
		
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.STATION_ID)){
			streamVO.setStationId(Long.parseLong(httpServletRequest.getParameter(VoizdStreamParam.STATION_ID)));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.USERID)){
			streamVO.setCreatorId(Long.parseLong(httpServletRequest.getParameter(VoizdStreamParam.USERID)));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.TAG)){
			streamVO.setTag(httpServletRequest.getParameter(VoizdStreamParam.TAG));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.CATEGORY)){
			streamVO.setCategory(httpServletRequest.getParameter(VoizdStreamParam.CATEGORY));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.DESCRIPTION)){
			streamVO.setDescription(httpServletRequest.getParameter(VoizdStreamParam.DESCRIPTION));
		}
		
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.LOCATION)){
			streamVO.setLocation(httpServletRequest.getParameter(VoizdStreamParam.LOCATION));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.LANGUAGE)){
			streamVO.setLanguage(httpServletRequest.getParameter(VoizdStreamParam.LANGUAGE));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.ISADULT)){
			streamVO.setAdult((httpServletRequest.getParameter(VoizdStreamParam.ISADULT).toString().equalsIgnoreCase(VoizdWebConstant.YES)?true:false));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.FILEIDS)){
			streamVO.setFileIds(httpServletRequest.getParameter(VoizdStreamParam.FILEIDS));
		}
		if(null!=httpServletRequest.getParameter(VoizdStreamParam.STATUS)){
		 streamVO.setStatus(Byte.parseByte(httpServletRequest.getParameter(VoizdStreamParam.STATUS)));
		}
		
		return streamVO;
	}
	*/
	public static StreamVO enrichUpdateStreamVo(HashMap<String, String> requestMap){
		StreamVO  streamVO  = new StreamVO();
		if(null!=requestMap.get(VoizdStreamParam.STREAM_ID)){
			streamVO.setStreamId(Long.parseLong(requestMap.get(VoizdStreamParam.STREAM_ID)));
		}
	
		if(null!=requestMap.get(VoizdStreamParam.USERID)){
			streamVO.setUserId(Long.parseLong(requestMap.get(VoizdStreamParam.USERID)));
		}
		if(null!=requestMap.get(VoizdStreamParam.TAG)){
			streamVO.setTag(requestMap.get(VoizdStreamParam.TAG));
		}
		if(null!=requestMap.get(VoizdStreamParam.CATEGORY)){
			streamVO.setCategory(requestMap.get(VoizdStreamParam.CATEGORY));
		}
		if(null!=requestMap.get(VoizdStreamParam.DESCRIPTION)){
			streamVO.setDescription(requestMap.get(VoizdStreamParam.DESCRIPTION));
		}
		
		if(null!=requestMap.get(VoizdStreamParam.FILEIDS)){
			streamVO.setFileIds(requestMap.get(VoizdStreamParam.FILEIDS));
		}
		if(null!=requestMap.get(VoizdStreamParam.STREAM_STATE)){
			streamVO.setStreamState(Byte.parseByte(requestMap.get(VoizdStreamParam.STREAM_STATE).toString()));
		}
		if(null!=requestMap.get(VoizdStreamParam.STREAM_STATUS)){
		    streamVO.setStatus(Byte.parseByte(requestMap.get(VoizdStreamParam.STREAM_STATUS)));
		}
		
		return streamVO;
	}

}
