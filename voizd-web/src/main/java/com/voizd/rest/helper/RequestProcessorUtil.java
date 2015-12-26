/**
 * 
 */
package com.voizd.rest.helper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.enumeration.DateTimeFormatEnum;
import com.voizd.common.util.DateTimeUtils;
import com.voizd.rest.constant.UserParameters;
import com.voizd.rest.constant.VoizdWebConstant;

/**
 * @author Manish
 * 
 */
public class RequestProcessorUtil {
	private static Logger logger = LoggerFactory.getLogger(RequestProcessorUtil.class);

	public static void enrichRegistrationVO(HashMap<String, String> requestMap, RegistrationVO registrationVO,Map<String, Object> clientParamMap) {
		if (null != requestMap) {
			if (null != requestMap.get(UserParameters.PASSWORD)) {
				registrationVO.setPassword(requestMap.get(UserParameters.PASSWORD));
			}
			if (null != requestMap.get(UserParameters.REGISTRATION_MODE)) {
				registrationVO.setRegistrationMode(Integer.parseInt(requestMap.get(UserParameters.REGISTRATION_MODE)));
			}
			if (null != requestMap.get(UserParameters.PARTNER_USER_KEY)) {
				registrationVO.setUserKey(requestMap.get(UserParameters.PARTNER_USER_KEY));
			}
			if (null != requestMap.get(UserParameters.PARTNER_APP_ID)) {
				registrationVO.setAppKey(requestMap.get(UserParameters.PARTNER_APP_ID));
			}
			if(null != clientParamMap.get(ClientParamConstant.CLIENT_VERSION)){
				registrationVO.setCurrentClientVersion((String) clientParamMap.get(ClientParamConstant.CLIENT_VERSION));
			}
			if(null != clientParamMap.get(ClientParamConstant.PLATFORM)){
				registrationVO.setCurrentPlatform((String) clientParamMap.get(ClientParamConstant.PLATFORM));
			}
			if(null != clientParamMap.get(ClientParamConstant.LATITUDE)){
				registrationVO.setLatitude(Float.parseFloat(clientParamMap.get(ClientParamConstant.LATITUDE).toString()));
			
			}
			if(null != clientParamMap.get(ClientParamConstant.LONGITUDE)){
				registrationVO.setLongitude(Float.parseFloat(clientParamMap.get(ClientParamConstant.LONGITUDE).toString()));
			}
			if(null !=  clientParamMap.get(ClientParamConstant.ADDREASS)){
				registrationVO.setLastLocation((String) clientParamMap.get(ClientParamConstant.ADDREASS));
			}
			if(null !=  requestMap.get(ClientParamConstant.PUSH_KEY)){
				registrationVO.setPushKey((String) requestMap.get(ClientParamConstant.PUSH_KEY));
			}
			
			enrichUserVO(requestMap, registrationVO,clientParamMap);
		}
	}

	public static void enrichUserVO(HashMap<String, String> requestMap, UserVO userVO,Map<String, Object> clientParamMap) {
		if (null != requestMap) {
			if (null != requestMap.get(UserParameters.CITY)) {
				userVO.setCity((String)requestMap.get(UserParameters.CITY));
			}else if (null!=clientParamMap && null != clientParamMap.get(UserParameters.CITY)) {
				userVO.setCity((String)clientParamMap.get(UserParameters.CITY));
			}
			if (null != requestMap.get(UserParameters.CONTACT_ADDRESS_LINE1)) {
				userVO.setContactAddressLine1(requestMap.get(UserParameters.CONTACT_ADDRESS_LINE1));
			}
			if (null != requestMap.get(UserParameters.CONTACT_ADDRESS_LINE2)) {
				userVO.setContactAddressLine2(requestMap.get(UserParameters.CONTACT_ADDRESS_LINE2));
			}
			if (null != requestMap.get(UserParameters.CONTACT_NUMBER)) {
				userVO.setContactNumber(requestMap.get(UserParameters.CONTACT_NUMBER));
			}
			logger.debug("enrichUserVO--"+requestMap.get(UserParameters.COUNTRY));
			if (null != requestMap.get(UserParameters.COUNTRY)) {
				userVO.setCountry((String)requestMap.get(UserParameters.COUNTRY));
			}else if (null!=clientParamMap && null != clientParamMap.get(UserParameters.COUNTRY)) {
				userVO.setCountry((String)clientParamMap.get(UserParameters.COUNTRY));
			}
			//logger.debug("enrichUserVO--"+clientParamMap.get(UserParameters.COUNTRY));
			if (null != requestMap.get(UserParameters.DOB)) {
				logger.debug("enrichUserVO ::DOB :: "+ requestMap.get(UserParameters.DOB));
				String dob = requestMap.get(UserParameters.DOB);
				userVO.setDob(DateTimeUtils.getDateObject(dob, DateTimeFormatEnum.WEB_DATE_FORMAT));
			}
			if (null != requestMap.get(UserParameters.FIRST_NAME)) {
				userVO.setFirstName(requestMap.get(UserParameters.FIRST_NAME));
			}
			if (null != requestMap.get(UserParameters.GENDER)) {
				userVO.setGender(Integer.parseInt(requestMap.get(UserParameters.GENDER)));
			}
			if (null != requestMap.get(UserParameters.LAST_NAME)) {
				userVO.setLastName(requestMap.get(UserParameters.LAST_NAME));
			}
			if (null != requestMap.get(UserParameters.PROFILE_PIC_FILE_ID)) {
				userVO.setProfilePicFileId(requestMap.get(UserParameters.PROFILE_PIC_FILE_ID));
			}
			if (null != requestMap.get(UserParameters.SATUTATION)) {
				userVO.setSalutation(requestMap.get(UserParameters.SATUTATION));
			}
			if (null != requestMap.get(UserParameters.SECONDARY_EMAIL_ADDRESS)) {
				userVO.setSecondaryEmailAddress(requestMap.get(UserParameters.SECONDARY_EMAIL_ADDRESS));
			}
			if (null != requestMap.get(UserParameters.STATE)) {
				userVO.setState((String)requestMap.get(UserParameters.STATE));
			}else if (null!=clientParamMap && null != clientParamMap.get(UserParameters.STATE)) {
				userVO.setState((String)clientParamMap.get(UserParameters.STATE));
			}
			if (null != requestMap.get(UserParameters.TIME_ZONE)) {
				userVO.setTimeZone(requestMap.get(UserParameters.TIME_ZONE));
			}
			if (null != requestMap.get(UserParameters.ZIPCODE)) {
				userVO.setZipcode(requestMap.get(UserParameters.ZIPCODE));
			}
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userVO.setUserName((requestMap.get(UserParameters.USER_NAME)).toLowerCase());
			}
			if (null != requestMap.get(UserParameters.USER_ID)) {
				userVO.setId(Long.parseLong(requestMap.get(UserParameters.USER_ID)));
			}
			if (null != requestMap.get(UserParameters.USER_DESC)) {
				userVO.setUserDescription(requestMap.get(UserParameters.USER_DESC));
			}
			if (null != requestMap.get(UserParameters.WEB_SITE)) {
				userVO.setWebSite(requestMap.get(UserParameters.WEB_SITE));
			}
			if (null != requestMap.get(UserParameters.USER_LANGUAGE)) {
				userVO.setLanguage(requestMap.get(UserParameters.USER_LANGUAGE));
			}else if (null!=clientParamMap && null != clientParamMap.get(ClientParamConstant.LANGUAGE)) {
				userVO.setLanguage((String)clientParamMap.get(ClientParamConstant.LANGUAGE));
			}
			if (null != requestMap.get(ClientParamConstant.LOCATION)) {
				userVO.setLocation(requestMap.get(ClientParamConstant.LOCATION).toString());
			}else if(null!=clientParamMap && null != clientParamMap.get(ClientParamConstant.LOCATION)){
				userVO.setLocation((String) clientParamMap.get(ClientParamConstant.LOCATION));
			}
		}
	}
	
	public static GlobeVO enrichGlobeVO(HashMap<String, String> requestMap) {
		GlobeVO globeVO = null;
		if (null != requestMap) {
			globeVO = new GlobeVO();
			if (null != requestMap.get(VoizdWebConstant.TAG)) {
				globeVO.setTag((String)requestMap.get(VoizdWebConstant.TAG));
			}
			if (null != requestMap.get(VoizdWebConstant.LOCATION)) {
				globeVO.setLocation((String)requestMap.get(VoizdWebConstant.LOCATION));
			}
		
		}
		return globeVO;
	}
	
	public static GlobeVO enrichGlobeVO(HttpServletRequest httpServletRequest) {
		GlobeVO globeVO = null;
		if (null != httpServletRequest) {
			globeVO = new GlobeVO();
			if (null != httpServletRequest.getParameter(VoizdWebConstant.TAG)) {
				globeVO.setTag(httpServletRequest.getParameter(VoizdWebConstant.TAG));
			}
			if (null != httpServletRequest.getParameter(VoizdWebConstant.LOCATION)) {
				globeVO.setLocation(httpServletRequest.getParameter(VoizdWebConstant.LOCATION));
			}
			if (null != httpServletRequest.getParameter(VoizdWebConstant.STARTLIMIT)) {
				globeVO.setStart(Integer.parseInt(httpServletRequest.getParameter(VoizdWebConstant.STARTLIMIT)));
			}
			if (null !=httpServletRequest.getParameter(VoizdWebConstant.ENDLIMIT)) {
				globeVO.setEnd(Integer.parseInt(httpServletRequest.getParameter(VoizdWebConstant.ENDLIMIT)));
			}
			if (null != httpServletRequest.getParameter(VoizdWebConstant.NEXT)) {
				globeVO.setDirection(Integer.parseInt(httpServletRequest.getParameter(VoizdWebConstant.NEXT)));
			}
		}
		return globeVO;
	}
	public static MapVO enrichMapVO(HashMap<String, String> requestMap) {
		MapVO mapVO = null;
		if (null != requestMap) {
			mapVO = new MapVO();
			if (null != requestMap.get(VoizdWebConstant.LONG)) {
				mapVO.setLongitude(Float.parseFloat(requestMap.get(VoizdWebConstant.LONG)));
			}
			if (null != requestMap.get(VoizdWebConstant.LAT)) {
				mapVO.setLatitude(Float.parseFloat(requestMap.get(VoizdWebConstant.LAT)));
			}
			if (null != requestMap.get(VoizdWebConstant.ZOOM)) {
				mapVO.setZoom(Integer.parseInt(requestMap.get(VoizdWebConstant.ZOOM)));
			}
			if (null != requestMap.get(VoizdWebConstant.LIMIT)) {
				mapVO.setLimit(Integer.parseInt(requestMap.get(VoizdWebConstant.LIMIT)));
			}
		}
		logger.debug("enrichMapVO---"+mapVO);
		return mapVO;
	}
}
