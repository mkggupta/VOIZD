package com.voizd.rest.controller.amplify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdUserActionParam;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.amplify.exception.AmplifyServiceFailedException;
import com.voizd.service.amplify.v1_0.AmplifyService;
@Controller
@RequestMapping("/api/amplify")
public class AmplifyController {
	private static Logger logger = LoggerFactory.getLogger(AmplifyController.class);
	private AmplifyService amplifyService ;
	
	public void setAmplifyService(AmplifyService amplifyService) {
		this.amplifyService = amplifyService;
	}
	@RequestMapping(value = "/vzpr/amplify", method = {RequestMethod.POST})
	public ModelAndView amplifyContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap && requestMap.size() > 0) {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				Long  contentId = 0l, creatorId =0l;
				Byte status = 0;
				if (null != requestMap.get(VoizdUserActionParam.CONTENT_ID)) {
					contentId = Long.parseLong(requestMap.get(VoizdUserActionParam.CONTENT_ID).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.AMPLIFY_STATUS)) {
					status = Byte.parseByte(requestMap.get(VoizdUserActionParam.AMPLIFY_STATUS).toString());
				}
				if (null != requestMap.get(VoizdUserActionParam.CREATORID)) {
					creatorId = Long.parseLong(requestMap.get(VoizdUserActionParam.CREATORID).toString());
				}

				logger.debug("AmplifyController. status=" + status + " creatorId=" + creatorId + " contentId=" + contentId+" userId="+userId);
				if(creatorId.equals(userId)){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.SELF_AMPLIFY_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.SELF_AMPLIFY_SUCCESS.getSuccessCode());
				}else if (contentId > 0) {
					amplifyService.ampifyContent(contentId,creatorId,status, userId);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.AMPLIFY_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.AMPLIFY_SUCCESS.getSuccessCode());
				}
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (AmplifyServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while tapContent service "+e.getLocalizedMessage(),e);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFY_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Error while tapContent service "+e.getLocalizedMessage(),e);
		}

		jsonData = gson.toJson(dataMap);

		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserActionController.tap jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/amplifier", method = RequestMethod.GET)
	public ModelAndView getAmplifierList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l,contentId=0l;;
		String jsonData = null;
		int startLimit=0;
		if(logger.isDebugEnabled()){
			logger.debug("AmplifyController.getTopVoizerList"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("AmplifyController.getTopVoizerList"+requestParamMap);
		}
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			contentId = Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID));
			if(userId>0 && contentId>0){
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				logger.debug("getAmplifierList.contentId="+contentId+", userId="+userId);
				dataMap = amplifyService.getAmplifierList(contentId,userId,startLimit,VoizdWebConstant.DEFAULT_AMPLIFIER_LIMIT,false,clientMap);
			}
			logger.debug("getTopVoizerList.dataMap="+dataMap);
		} catch (AmplifyServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.AMPLIFIER_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.AMPLIFIER_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
		
	
	@RequestMapping(value = "/vzpb/amplifier/more", method = RequestMethod.GET)
	public ModelAndView getMoreAmplifierList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l;
		Long contentId =0l;
		int endLimit =0;
		int startLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationContentParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationContentParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationContentParam.DIRECTION);
		contentId = Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID));
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
		logger.debug("getMoreAmplifierList., userId="+userId+",endLimit="+endLimit+" ,direction="+direction);
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getTopVoizerList., userId="+userId);
			dataMap = amplifyService.getAmplifierList(contentId,userId,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			logger.debug("getTopVoizerList.dataMap="+dataMap);
		} catch (AmplifyServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.AMPLIFIER_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.AMPLIFIER_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	

}
