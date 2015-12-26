package com.voizd.rest.helper.manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.VStreamVO;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdStationContentParam;

public class StationContentHelperManager {
	private static Logger logger = LoggerFactory.getLogger(StationContentHelperManager.class);
	public static ContentVO enrichStationContentVo(HashMap<String, String> requestMap, Map<String, Object> clientMap){
		ContentVO  contentVO  = new ContentVO();
		
		if(null!=requestMap.get(VoizdStationContentParam.STATION_ID)){
			contentVO.setStationId(Long.parseLong(requestMap.get(VoizdStationContentParam.STATION_ID).toString()));
		}
		if(null!=requestMap.get(VoizdStationContentParam.TAG)){
			contentVO.setTag(requestMap.get(VoizdStationContentParam.TAG));
		}
		
		if(null!=requestMap.get(VoizdStationContentParam.TITLE)){
			contentVO.setTitle(requestMap.get(VoizdStationContentParam.TITLE));
		}
		if(null!=requestMap.get(VoizdStationContentParam.USERID)){
			contentVO.setCreatorId(Long.parseLong(requestMap.get(VoizdStationContentParam.USERID).toString()));
		}
		if(null!=requestMap.get(VoizdStationContentParam.CATEGORY)){
			contentVO.setCategory(requestMap.get(VoizdStationContentParam.CATEGORY));
		}
		
		if(null!=requestMap.get(VoizdStationContentParam.FILEIDS)){
			contentVO.setFileIds(requestMap.get(VoizdStationContentParam.FILEIDS));
		}
		if(null!=requestMap.get(VoizdStationContentParam.APPIDS)){
			contentVO.setAppIds(requestMap.get(VoizdStationContentParam.APPIDS));
		}
		 if(null!=requestMap.get(VoizdStationContentParam.WEBLINK)){
			 contentVO.setWeblink(requestMap.get(VoizdStationContentParam.WEBLINK));
		 }
		
		 if(null!=clientMap.get(ClientParamConstant.LONGITUDE)){
			 contentVO.setLongitude(Float.parseFloat(clientMap.get(ClientParamConstant.LONGITUDE).toString()));
		 }
		 if(null!=clientMap.get(ClientParamConstant.LATITUDE)){
			 contentVO.setLatitude(Float.parseFloat(clientMap.get(ClientParamConstant.LATITUDE).toString()));
		 }
		 if(null!=clientMap.get(ClientParamConstant.COUNTRY)){
			 contentVO.setCountry(clientMap.get(ClientParamConstant.COUNTRY).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.LANGUAGE)){
			 contentVO.setLanguage(clientMap.get(ClientParamConstant.LANGUAGE).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.STATE)){
			 contentVO.setState(clientMap.get(ClientParamConstant.STATE).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.CITY)){
			 contentVO.setCity(clientMap.get(ClientParamConstant.CITY).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.LOCATION)){
			 contentVO.setLocation(clientMap.get(ClientParamConstant.LOCATION).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.ADDREASS)){
			 contentVO.setAddress(clientMap.get(ClientParamConstant.ADDREASS).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.REGISTRATION)){
			 contentVO.setRegistration(clientMap.get(ClientParamConstant.REGISTRATION).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.CLIENTIP)){
			 contentVO.setMobileIp(clientMap.get(ClientParamConstant.CLIENTIP).toString());
		 }
		
		contentVO.setStatus(VoizdWebConstant.DEFAULT_CONTENT_STATUS);
		logger.debug("contentVO----"+contentVO);
		return contentVO;
	}
	
	public static ContentVO enrichUpdateStationContentVo(HashMap<String, String> requestMap){
		ContentVO  contentVO  = new ContentVO();
		
		if(null!=requestMap.get(VoizdStationContentParam.STATION_ID)){
			contentVO.setStationId(Long.parseLong(requestMap.get(VoizdStationContentParam.STATION_ID).toString()));
		}
		if(null!=requestMap.get(VoizdStationContentParam.TAG)){
			contentVO.setTag(requestMap.get(VoizdStationContentParam.TAG));
		}
		if(null!=requestMap.get(VoizdStationContentParam.CONTENT_ID)){
			contentVO.setContentId(Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID).toString()));
		}
		if(null!=requestMap.get(VoizdStationContentParam.USERID)){
			contentVO.setCreatorId(Long.parseLong(requestMap.get(VoizdStationContentParam.USERID).toString()));
		}		
		if(null!=requestMap.get(VoizdStationContentParam.FILEIDS)){
			contentVO.setFileIds(requestMap.get(VoizdStationContentParam.FILEIDS));
		}
		contentVO.setStatus(VoizdWebConstant.DEFAULT_CONTENT_STATUS);
		logger.debug("enrichUpdateStationContentVo,contentVO----"+contentVO);
		return contentVO;
	}
	public static VStreamVO enrichVStramVO(HashMap<String, String> requestMap,boolean hasNext){
		VStreamVO  vStreamVO  = new VStreamVO();
		
		if(null!=requestMap.get(VoizdStationContentParam.USERID)){
			vStreamVO.setUserId((Long.parseLong(requestMap.get(VoizdStationContentParam.USERID))));
		}	
		
		if(null!=requestMap.get(VoizdStationContentParam.TYPE)){
			vStreamVO.setType((requestMap.get(VoizdStationContentParam.TYPE)));
		}
	
		if(null!=requestMap.get(VoizdStationContentParam.STARTLIMIT)){
			vStreamVO.setStartLimit((Integer.parseInt(requestMap.get(VoizdStationContentParam.STARTLIMIT))));
		}
		if(null!=requestMap.get(VoizdStationContentParam.ENDLIMIT)){
			vStreamVO.setEndLimit((Integer.parseInt(requestMap.get(VoizdStationContentParam.STARTLIMIT))));
		}
		vStreamVO.setHasNext(hasNext);
		logger.debug("enrichVStramVO,vStreamVO----"+vStreamVO);
		logger.debug("enrichVStramVO,userid----"+vStreamVO.getUserId());
		logger.debug("enrichVStramVO,type----"+vStreamVO.getType());
		return vStreamVO;
	}
	
	public static VStreamVO enrichVStramVO(Long userId,Long visitorId,String type,int startLimit,int endLimit){
		VStreamVO  vStreamVO  = new VStreamVO();
	    vStreamVO.setUserId(userId);
	    vStreamVO.setVisitorId(visitorId);
		vStreamVO.setType(type);
	    vStreamVO.setStartLimit(startLimit);
		vStreamVO.setEndLimit(endLimit);
		return vStreamVO;
	}

}
