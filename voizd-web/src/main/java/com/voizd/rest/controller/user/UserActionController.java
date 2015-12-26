package com.voizd.rest.controller.user;



import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.constant.VoizdStreamParam;
import com.voizd.common.constant.VoizdUserActionParam;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.user.action.exception.UserActionServiceFailedException;
import com.voizd.service.user.action.v1_0.UserActionService;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
@Controller
public class UserActionController {
	private static Logger logger = LoggerFactory.getLogger(UserActionController.class);
	private UserActionService userActionService ;
	
	@RequestMapping(value = "/api/station/vzpr/tap", method = {RequestMethod.POST})
	public ModelAndView tapStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;

		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap && requestMap.size() > 0) {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				Long stationId = 0l, follwerId = 0l,streamId =0l,creatorId=0l;
				Byte status = 0;

				if (null != requestMap.get(VoizdUserActionParam.STATION_ID)) {
					stationId = Long.parseLong(requestMap.get(VoizdUserActionParam.STATION_ID).toString());
				}

				if (null != requestMap.get(VoizdUserActionParam.FOLLOWERID)) {
					follwerId = Long.parseLong(requestMap.get(VoizdUserActionParam.FOLLOWERID).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.TAP_STATUS)) {
					status = Byte.parseByte(requestMap.get(VoizdUserActionParam.TAP_STATUS).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.CREATORID)) {
					creatorId = Long.parseLong(requestMap.get(VoizdUserActionParam.CREATORID).toString());
				}
				 
				logger.error("UserActionController.stationId=" + stationId + " follwerId=" + follwerId + " status=" + status +" creatorId="+creatorId);
				/*if(userId.equals(follwerId)){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.TAP_SELF_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.TAP_SELF_SUCCESS.getSuccessCode());
				}else*/ if (creatorId > 0 && follwerId>0) {
					if(creatorId.equals(follwerId) ){
						dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
						dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.TAP_SELF_SUCCESS.getSuccessMessage());
						dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.TAP_SELF_SUCCESS.getSuccessCode());
					}else{
						userActionService.tapStation(creatorId, follwerId, status);
						dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
						dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.TAP_SUCCESS.getSuccessMessage());
						dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.TAP_SUCCESS.getSuccessCode());
					}
				}else if (stationId > 0 && follwerId>0) {
					userActionService.tapStation(stationId,streamId, follwerId, status);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.TAP_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.TAP_SUCCESS.getSuccessCode());
				}
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
		}catch (UserActionServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			if (ErrorCodesEnum.USER_ACCOUNT_INACTIVE.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_ACCOUNT_INACTIVE.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_ACCOUNT_INACTIVE.getErrorCode());
			}else{
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			logger.error("Error while tapStation service "+e.getLocalizedMessage(),e);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while tapStation service "+e.getLocalizedMessage(),e);
		}

		jsonData = gson.toJson(dataMap);

		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserActionController.jsonData=" + jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/api/content/vzpr/tap", method = {RequestMethod.POST})
	public ModelAndView tapContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap && requestMap.size() > 0) {

				Long follwerId = 0l, contentId = 0l, streamId =0l;;
				Byte status = 0;

				if (null != requestMap.get(VoizdUserActionParam.CONTENT_ID)) {
					contentId = Long.parseLong(requestMap.get(VoizdUserActionParam.CONTENT_ID).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.FOLLOWERID)) {
					follwerId = Long.parseLong(requestMap.get(VoizdUserActionParam.FOLLOWERID).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.TAP_STATUS)) {
					status = Byte.parseByte(requestMap.get(VoizdUserActionParam.TAP_STATUS).toString());
				}

				logger.debug("UserActionController. follwerId=" + follwerId + " status=" + status + " contentId=" + contentId);
				if (contentId > 0) {
					userActionService.tapContent(contentId,streamId,follwerId, status);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.TAP_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.TAP_SUCCESS.getSuccessCode());
				}
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (UserActionServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while tapContent service "+e.getLocalizedMessage(),e);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_TAP_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while tapContent service "+e.getLocalizedMessage(),e);
		}

		jsonData = gson.toJson(dataMap);

		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserActionController.tap jsonData=" + jsonData);
		return modelAndView;
	}
	

	@RequestMapping(value = "/api/like/vzpr/{type}", method = {RequestMethod.POST})
	public ModelAndView stationLike(@PathVariable String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		logger.debug("UserActionController stationLike type :: " + type);
		Long stationId = 0l, userId = 0l, contentId = 0l, streamId=0l ;
		Byte status = 0;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap && requestMap.size() > 0) {
				if (null != requestMap.get(VoizdStationParam.STATION_ID)) {
					stationId = Long.parseLong(requestMap.get(VoizdStationParam.STATION_ID));
				}
				if (null != requestMap.get(VoizdStationContentParam.CONTENT_ID)) {
					contentId = Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID));
				}
				if (null != requestMap.get(VoizdStationParam.USERID)) {
					userId = Long.parseLong(requestMap.get(VoizdStationParam.USERID));
				}
				if (null != requestMap.get(VoizdStationParam.LIKE)) {
					status = Byte.parseByte(requestMap.get(VoizdStationParam.LIKE).toString());
				}
				if (null != requestMap.get(VoizdStreamParam.STREAM_ID)) {
					streamId = Long.parseLong(requestMap.get(VoizdStreamParam.STREAM_ID));
				}
				 if (VoizdConstant.CONTENT.equalsIgnoreCase(type) && contentId > 0 && userId > 0) {
					userActionService.likeOperation(contentId, userId, status, type);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessCode());
				} else if (VoizdConstant.STATION.equalsIgnoreCase(type) && stationId > 0 && userId > 0) {
					userActionService.likeOperation(stationId, userId, status, type);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessCode());
				}else if (VoizdConstant.STREAM.equalsIgnoreCase(type) && streamId > 0 && userId > 0) {
					userActionService.likeOperation(streamId, userId, status, type);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.LIKE_SUCCESS.getSuccessCode());
				}else {
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorCode());
				}
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}

		} catch (UserActionServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while stationLike service "+e.getLocalizedMessage(),e);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_LIKE_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while stationLike service "+e.getLocalizedMessage(),e);
		}

		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserActionController stationLike action :: jsonData =" + jsonData);
		return modelAndView;
	}
	
	public void setUserActionService(UserActionService userActionService) {
		this.userActionService = userActionService;
	}

}
