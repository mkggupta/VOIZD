package com.voizd.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.media.ImageSize;

public class ClientHeaderUtil {
	 private static Logger logger = LoggerFactory.getLogger(ClientHeaderUtil.class);
	
	public static Map<String,Object>  extractClientParam(HttpServletRequest request){
		
			Map<String,Object> paramMap = new HashMap<String, Object>(); 
			String clientParam = request.getHeader(ClientParamConstant.CLIENT_PARAM);
			logger.debug("param--"+clientParam);
			//clientParam= "PLATFORM##Andriod::CLIENT_VERSION##asssd::IMEI##3114445140536492369::LATITUDE##0.0::LONGITUDE##0.0::ACCURACY##0.0::ALTITUDE##0.0::BEARING##0.0::SPEED##0.0::TIME##0::BOARD##montblanc::BRAND##SEMC::CPU_ABI##armeabi-v7a::DEVICE##ST25i::DISPLAY##6.0.B.3.188::FINGERPRINT##SEMC/ST25i_1261-3755/ST25i:2.3.7/6.0.B.3.188/Q7P_zw:user/release-keys::HOST##BuildHost::ID##6.0.B.3.188::MANUFACTURER##Sony Ericsson::MODEL##ST25i::PRODUCT##ST25i_1261-3755::TAGS##release-keys::TIME##19 Feb 2012 06:36:37 GMT::TYPE##user::USER##BuildUser::VERSION.CODENAME##REL::VERSION.INCREMENTAL##Q7P_zw::VERSION.RELEASE##2.3.7::VERSION.SDK##10::VERSION.SDK_INT##10::USER_AGENT##Mozilla/5.0 (Linux; U; Android 2.3.7; en-in; ST25i Build/6.0.B.3.188) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1::DENSITY_DPI##240.0::DENSITY##1.5::SCALED_DENSITY##1.5::XDPI##283.53488::YDPI##281.70908::WIDTH_PIXELS##480::HEIGHT_PIXELS##854::ZOOM_SUPPORTED##Not Supported::SMOOTH_ZOOM_SUPPORTED##Not Supported::MAX_ZOOM##100::video##3gp,mp4::seviceprovider##IND  airtel::operatorSimName##airtel::isoCountryCode##in";
			String height =null;
			String width =null;
			if(StringUtils.isNotBlank(clientParam)){
				logger.debug("param--------------------");
				String[] strArray = clientParam.split(ClientParamConstant.MAPSEPERATOR);
				for (int loop = 0; loop < strArray.length; loop++) {
					String[] strKeyValueArray = strArray[loop].split(ClientParamConstant.SEPERATOR);
					//logger.debug(strKeyValueArray[0]+"----"+strKeyValueArray[1]);
					if(ClientParamConstant.MODEL.equalsIgnoreCase(strKeyValueArray[0])||ClientParamConstant.PHMODEL.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.MODEL, strKeyValueArray[1]);	
					}else if(ClientParamConstant.OPERATOR.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.OPERATOR, strKeyValueArray[1]);
					}else if(ClientParamConstant.COUNTRYCODE.equalsIgnoreCase(strKeyValueArray[0])){
							paramMap.put(ClientParamConstant.COUNTRY, strKeyValueArray[1]);
					}else if(ClientParamConstant.PLATFORM.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.PLATFORM, strKeyValueArray[1]);
					}else if(ClientParamConstant.LATITUDE.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.LATITUDE, strKeyValueArray[1]);
					}else if(ClientParamConstant.LONGITUDE.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.LONGITUDE, strKeyValueArray[1]);
					}else if(ClientParamConstant.ADDREASS.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.ADDREASS, strKeyValueArray[1]);
					//	String address ="New Delhi$null$India$null$Delhi$West Delhi$B 1/257$Janakpuri";
						//address="Beroucha Kachhar$212216$India$null$Uttar Pradesh$Kaushambi$Rd$null";
						String[] addressArray =strKeyValueArray[1].split(ClientParamConstant.ADDRESSSEPERATOR);
						
						for (int addLoop = 0; addLoop < addressArray.length; addLoop++) {
							if(addLoop==ClientParamConstant.STATE_POSITION){
								paramMap.put(ClientParamConstant.STATE, addressArray[addLoop]);
							}
							if(addLoop==ClientParamConstant.CITY_POSITION){
								paramMap.put(ClientParamConstant.CITY, addressArray[addLoop]);	
							}
							if(addLoop==ClientParamConstant.LOCATION_POSITION){
								paramMap.put(ClientParamConstant.LOCATION, addressArray[addLoop]);
							}
							if(paramMap.get(ClientParamConstant.COUNTRY)==null){
								if(addLoop==ClientParamConstant.COUNTRY_POSITION){
									paramMap.put(ClientParamConstant.COUNTRY, addressArray[addLoop]);
								}
							}
						}
						
					}else if(ClientParamConstant.LANGUAGE.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.LANGUAGE, strKeyValueArray[1]);
					}
					else if(ClientParamConstant.CLIENT_VERSION.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.CLIENT_VERSION, strKeyValueArray[1]);
					}else if(ClientParamConstant.MANUFACTURER.equalsIgnoreCase(strKeyValueArray[0])|| ClientParamConstant.PHMODEL.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.MOBILEVENDOR, strKeyValueArray[1]);
					}else if(ClientParamConstant.IMEI.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.IMEI, (null==strKeyValueArray[1])?"0":strKeyValueArray[1]);
					}else if(ClientParamConstant.IMAGESIZE.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.IMAGESIZE, (null==strKeyValueArray[1])?ImageSize.O_130x130.getNewValue():strKeyValueArray[1]);
					}else if(ClientParamConstant.THUMBSIZE.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.THUMBSIZE, (null==strKeyValueArray[1])?ImageSize.O_130x130.getNewValue():strKeyValueArray[1]);
					}else if(ClientParamConstant.HEIGHT_PIXELS.equalsIgnoreCase(strKeyValueArray[0])){
						
						if(null!=strKeyValueArray[1]){
							height = strKeyValueArray[1];
						}
						logger.debug("ClientParamConstant--height-----------------"+height);
					}else if(ClientParamConstant.WIDTH_PIXELS.equalsIgnoreCase(strKeyValueArray[0])){
						if(null!=strKeyValueArray[1]){
							width = strKeyValueArray[1];
						}
						logger.debug("ClientParamConstant--weight-----------------"+width);
					}else if(ClientParamConstant.SCREENRESOLUTION.equalsIgnoreCase(strKeyValueArray[0])){
						paramMap.put(ClientParamConstant.SCREENRESOLUTION,strKeyValueArray[1]);
						logger.debug("ClientParamConstant--SCREENRESOLUTION-----------------"+width);
					}
				}
				if(StringUtils.isNotBlank(height)&& StringUtils.isNotBlank(width)){
					paramMap.put(ClientParamConstant.SCREENRESOLUTION,width+"x"+height);
				}
			}else{
				logger.debug("ClientParam is blank--------------"+clientParam);
			}
			String clientIp = request.getHeader(ClientParamConstant.CLIENT_IP);
			logger.debug("ClientParam is clientIp--------------"+clientIp);
			if(StringUtils.isBlank(clientIp)){
				clientIp = request.getRemoteAddr();
				logger.debug("ClientParam is clientIp-2-------------"+clientIp);
			}
			if(StringUtils.isNotBlank(clientIp)){
				paramMap.put(ClientParamConstant.CLIENTIP, clientIp);
			}
			logger.debug("ClientParamConstant-paramMap="+paramMap);
			return paramMap;
			
	}
	


}
