package com.voizd.rest.helper.manager;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.constant.VoizdStationParam;

public class SearchHelperManager {
	private static Logger logger = LoggerFactory.getLogger(SearchHelperManager.class);

	public static String enrichSaerch(HashMap<String, String> requestMap) {
		String tag = null;
		if (null != requestMap.get(VoizdStationParam.TAG)) {
			tag = requestMap.get(VoizdStationParam.TAG);
		}
		logger.debug("enrichSaerch :: tag :: " + tag);
		return tag;
	}
	
	public static SearchVO enrichSearchVO(HashMap<String, String> requestMap,Long userId) {
		SearchVO searchVO = new SearchVO();
		if (null != requestMap.get(VoizdStationParam.TAG)) {
			searchVO.setTag(requestMap.get(VoizdStationParam.TAG));
		}
		if (null != requestMap.get(VoizdStationParam.LANGUAGE)) {
			searchVO.setLanguage(requestMap.get(VoizdStationParam.LANGUAGE));
		}
		if (null != requestMap.get(VoizdStationParam.LOCATION)) {
			searchVO.setLocation(requestMap.get(VoizdStationParam.LOCATION));
		}
		if (null != requestMap.get(VoizdStationParam.USER_NAME)) {
			searchVO.setUserName(requestMap.get(VoizdStationParam.USER_NAME));
		}
		if (null != requestMap.get(VoizdStationParam.SEARCH_TYPE)) {
			searchVO.setSearchType(requestMap.get(VoizdStationParam.SEARCH_TYPE));
		}
		if (null != requestMap.get(VoizdStationParam.TYPE)) {
			searchVO.setType(Integer.parseInt(requestMap.get(VoizdStationParam.TYPE)));
		}
		if(null != userId){
			searchVO.setUserId(userId);
		}
		return searchVO;
	}
	
	public static SearchVO enrichSearchVO(HttpServletRequest httpServletRequest,Long userId) {
		SearchVO searchVO = new SearchVO();
		if (null != httpServletRequest.getParameter(VoizdStationParam.TAG)) {
			searchVO.setTag(httpServletRequest.getParameter(VoizdStationParam.TAG));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT)) {
			searchVO.setStartLimit(Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT)));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT)) {
			searchVO.setPageLimit(Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT)));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.DIRECTION)) {
			searchVO.setDirection(Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.DIRECTION)));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.SEARCH_TYPE)) {
			searchVO.setSearchType(httpServletRequest.getParameter(VoizdStationParam.SEARCH_TYPE));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.LANGUAGE)) {
			searchVO.setLanguage(httpServletRequest.getParameter(VoizdStationParam.LANGUAGE));
		}
		if (null != httpServletRequest.getParameter(VoizdStationParam.LOCATION)) {
			searchVO.setLocation(httpServletRequest.getParameter(VoizdStationParam.LOCATION));
		}
		if(null != userId){
			searchVO.setUserId(userId);
		}
		return searchVO;
	}
}
