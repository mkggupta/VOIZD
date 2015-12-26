package com.voizd.rest.controller.earth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.RequestProcessorUtil;
import com.voizd.service.earth.exception.EarthServiceException;
import com.voizd.service.earth.v1_0.EarthService;


@Controller
@RequestMapping("/api/globe/vzpb")
public class EarthController {
	private static Logger logger = LoggerFactory.getLogger(EarthController.class);
	private EarthService earthService;
	private JmsSender jmsSender;
	
	public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}

	public void setEarthService(EarthService earthService) {
		this.earthService = earthService;
	}
	@RequestMapping(value = "/earth", method = RequestMethod.GET)
	public ModelAndView getEarth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String country="india";
		try{
			dataMap = earthService.getEarthView(country,VoizdWebConstant.DEFAULT_GRID_TAG_COUNT);
		}catch(Exception e){
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getEarth :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/map", method = RequestMethod.POST)
	public ModelAndView getMap(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try{
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
		MapVO mapVO = RequestProcessorUtil.enrichMapVO(requestMap);
		dataMap = earthService.getMapView(mapVO, clientMap);
		}catch(Exception e){
			logger.error("Exception while getMap " + e.getLocalizedMessage(), e);
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getMap :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/location", method = RequestMethod.POST)
	public ModelAndView getEarthLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		try{
			GlobeVO globeVO = RequestProcessorUtil.enrichGlobeVO(requestMap);
			dataMap = earthService.getLocationView(globeVO);
		}catch(Exception e){
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getEarthLocation :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/location/more", method = RequestMethod.GET)
	public ModelAndView getEarthLocationMore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		//HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		try{
			GlobeVO globeVO = RequestProcessorUtil.enrichGlobeVO(httpServletRequest);
			dataMap = earthService.getLocationView(globeVO);
		}catch(Exception e){
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getEarthLocation :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/vtag", method = RequestMethod.GET)
	public ModelAndView getVTag(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		int startLimit=0;
		String jsonData = null;
		Gson gson = new Gson();
		try{
			dataMap = earthService.getVTags(startLimit,VoizdWebConstant.DEFAULT_VTAG_LIMIT,false);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		}catch(EarthServiceException e){
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
		}catch(Exception e){
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		logger.debug("getVTag :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/vtag/more", method = RequestMethod.GET)
	public ModelAndView getVTagMore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String jsonData = null;
		Gson gson = new Gson();
		try{
			boolean order = false ;
			//String direction = null;
			int endLimit =0,startLimit=0;
			startLimit= Integer.parseInt(httpServletRequest.getParameter(VoizdWebConstant.STARTLIMIT));
			endLimit= Integer.parseInt(httpServletRequest.getParameter(VoizdWebConstant.ENDLIMIT));
			/*
			direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
			if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
				order = true ;
			}*/
			
			dataMap = earthService.getVTags(startLimit,endLimit,order);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		}catch(EarthServiceException e){
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
		}catch(Exception e){
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.EARTH_SERVICE_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		logger.debug("getVTagMore :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/tags", method = RequestMethod.POST)
	public ModelAndView getEarthTag(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		GlobeVO globeVO = RequestProcessorUtil.enrichGlobeVO(requestMap);
		try{
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			globeVO.setUserId(userId);
			dataMap = earthService.getTagView(globeVO,clientMap);
		}catch(Exception e){
			logger.error("Exception while getEarthTag " + e.getLocalizedMessage(), e);	
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getEarthTag :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	/*private void sendJmsMessage(){
		try {
			Message message = new Message();
			message.setDestination("a:tapvoizd");
			message.setUserId(1l);
			message.setText("Testing message hello word");
			
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:tapvoizd");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(message, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}*/
/*	private void sendJmsMessage(ContentVO contentVO,boolean isRegistarion){
		try {
			Message message = new Message();
			message.setDestination("a:tapvoizd");
			message.setUserId(contentVO.getCreatorId());
			message.setContentId(contentVO.getContentId());
			message.setStationId(contentVO.getStationId());
			message.setText("User posting content");
			message.setUserStatus(status);
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("content", contentVO);
			msgMap.put("isreg", isRegistarion);
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader("a:tapvoizd");
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}*/
}
