package com.voizd.rest.controller.stream;

import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.constant.VoizdStreamParam;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.stream.exception.StreamServiceException;
import com.voizd.service.stream.v1_0.StreamService;
@Controller
@RequestMapping(value = "/api/getstream")
public class StreamController {
	private static Logger logger = LoggerFactory.getLogger(StreamController.class);
	private StreamService streamService = null ;
	@RequestMapping(value = "/vzpr/{type}")
	public ModelAndView getMyStreamList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l,streamId=0l;
		String jsonData = null;
		int startLimit=0;
	
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getMyStreamList., userId="+userId);
			 if(VoizdConstant.MYSTREAM.equalsIgnoreCase(type)){
				dataMap = streamService.getStream(streamId,userId,VoizdConstant.ALL_STREAM_STATUS,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			}else{
				logger.debug("getMyStreamList.not supported type "+type);
			} 
			
		} catch (StreamServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap){
			jsonData = gson.toJson(dataMap);
		}else{
			if(null==dataMap || dataMap.size()==0){
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STREAM_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.NO_STREAM_SUCCESS.getSuccessCode());
			}
		  jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("getStationList.jsonData="+jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/vzpr/{type}/more")
	public ModelAndView getMyMoreStreamList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l,streamId=0l;
		String jsonData = null;
		int startLimit=0;
		int endLimit =0;
		String direction = null;
		boolean order = false ;
	
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getMyMoreStreamList., userId="+userId);
			userId =(null!=httpServletRequest.getParameter(VoizdStreamParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStreamParam.USERID)):0l;
			endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStreamParam.ENDLIMIT));
			startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStreamParam.STARTLIMIT));
			direction =httpServletRequest.getParameter(VoizdStreamParam.DIRECTION);
			streamId  =Long.parseLong(httpServletRequest.getParameter(VoizdStreamParam.STREAM_ID).toString());
			if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
				order = true ;
			}
			 if(VoizdConstant.MYSTREAM.equalsIgnoreCase(type)){
				dataMap = streamService.getStream(streamId,userId,VoizdConstant.ALL_STREAM_STATUS,startLimit,endLimit,order,clientMap);
			}else{
				logger.debug("getMyMoreStreamList.not supported type "+type);
			} 
			
		} catch (StreamServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.STREAM_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap){
			jsonData = gson.toJson(dataMap);
		}else{
			if(null==dataMap || dataMap.size()==0){
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STREAM_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.NO_STREAM_SUCCESS.getSuccessCode());
			}
		  jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("getMyMoreStreamList.jsonData="+jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/vzpr/share")
	public ModelAndView getStremShareUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0){
			Long userId = 0l ;
			Byte appId =0 ;
			try {
				if(null!=requestMap.get(VoizdStationParam.USERID)){
					userId=Long.parseLong(requestMap.get(VoizdStationParam.USERID).toString());
				}
				if(null!=requestMap.get(VoizdStationParam.APPID)){
					appId=Byte.parseByte(requestMap.get(VoizdStationParam.APPID).toString());
				}
				dataMap= streamService.createStreamShareUrl(userId, appId);
			}catch (StreamServiceException e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_SHARE_FAILED_EXCEPTION.getErrorMessage());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_SHARE_FAILED_EXCEPTION.getErrorMessage());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("get.share="+jsonData);
		return modelAndView;
	}
		
	public void setStreamService(StreamService streamService) {
		this.streamService = streamService;
	}
	
	

}
