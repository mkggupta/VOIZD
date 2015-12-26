package com.voizd.rest.controller.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.v1_0.StationService;
@Controller
@RequestMapping("/api/getstation")
public class StationController {
	private static Logger logger = LoggerFactory.getLogger(StationController.class);
	private StationService stationService = null ;

	@RequestMapping(value = "/vzpb/{type}", method = RequestMethod.GET)
	public ModelAndView getStationList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l;
		String jsonData = null;
		int startLimit=0;
		if(logger.isDebugEnabled()){
			logger.debug("StationController.getRecentStation"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationController.getRecentStation"+requestParamMap);
		}
		
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			logger.debug("getRecentStation.stationId="+stationId+", userId="+userId);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			if(VoizdConstant.RECENT.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.RECENT_STATION_STATUS,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			}else if(VoizdConstant.POPULAR.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.POPULAR_STATION_STATUS,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			}else{
				logger.debug("StationController.not supported type "+type);
			} 
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
			
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	

	@RequestMapping(value = "/vzpb/{type}/more", method = RequestMethod.GET)
	public ModelAndView getMoreStationList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l;
		int endLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		int startLimit=0;
		stationId =Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)); 
		userId =(null!=httpServletRequest.getParameter(VoizdStationParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)):0l;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getMoreRecentStation.stationId="+stationId+", userId="+userId+",startLimit="+startLimit+",endLimit="+endLimit+" ,direction="+direction);
			if(VoizdConstant.RECENT.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.RECENT_STATION_STATUS,startLimit,endLimit,order,clientMap);
			}else if(VoizdConstant.POPULAR.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.POPULAR_STATION_STATUS,startLimit,endLimit,order,clientMap);
			}else{
				logger.debug("StationController.not supported type "+type);
			} 
			
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0 ){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());

		  jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);	
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/{type}", method = RequestMethod.GET)
	public ModelAndView getMyStationList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l;
		int startLimit=0;
		String jsonData = null;
		if(logger.isDebugEnabled()){
			logger.debug("StationController.getRecentStation"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationController.getRecentStation"+requestParamMap);
		}
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			logger.debug("getRecentStation.stationId="+stationId+", userId="+userId);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			if(VoizdConstant.MYTAPPED.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.MY_TAP_STATION_STATUS,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			}else if(VoizdConstant.MY.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.MY_STATION_STATUS,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			}else{
				logger.debug("StationController.not supported type "+type);
			} 
			
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	

	@RequestMapping(value = "/vzpr/{type}/more", method = RequestMethod.GET)
	public ModelAndView getMoreMyStationList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l;
		int endLimit =0;
		int startLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		stationId =Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)); 
		userId =(null!=httpServletRequest.getParameter(VoizdStationParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)):0l;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		logger.debug("getMoreRecentStation.stationId="+stationId+", userId="+userId+",endLimit="+endLimit+" ,direction="+direction);
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			if(VoizdConstant.MYTAPPED.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.MY_TAP_STATION_STATUS,startLimit,endLimit,order,clientMap);
			}else if(VoizdConstant.MY.equalsIgnoreCase(type)){
				dataMap = stationService.getStations(stationId,userId,VoizdConstant.MY_STATION_STATUS,startLimit,endLimit,order,clientMap);
			}else{
				logger.debug("StationController.not supported type "+type);
			} 
			
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
		  jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
	
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/user", method = RequestMethod.GET)
	public ModelAndView getUserStationList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l;
		String jsonData = null;
		logger.debug("getRecentStation. userId="+userId);
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			List<StationVO> stationVOList = stationService.getUserStations(userId, VoizdConstant.STATION_DELETED_STATUS);
			if(null!=stationVOList && stationVOList.size()>0){
				dataMap.put(VoizdWebConstant.STATION, stationVOList);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
			}
		}catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("getMoreStationList.more jsonData="+jsonData);
		return modelAndView;
	}
	

	@RequestMapping(value = "/vzpr/get", method = RequestMethod.GET)
	public ModelAndView getStationDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l,stationId=0l;
		String jsonData = null;
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			stationId = Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID));
			userId =(null!=httpServletRequest.getParameter(VoizdStationParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)):0l;
			dataMap = stationService.getStationDetail(stationId,userId,clientMap);
			logger.debug("getStationDetails.more jsonData="+jsonData);
		}catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	

	@RequestMapping(value = "/vzpr/share", method = RequestMethod.POST)
	public ModelAndView getStationShareUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0){
			Long stationId = 0l ;
			Byte appId =0 ;
			
			try {
				if(null!=requestMap.get(VoizdStationParam.STATION_ID)){
					stationId=Long.parseLong(requestMap.get(VoizdStationParam.STATION_ID).toString());
				}
				if(null!=requestMap.get(VoizdStationParam.APPID)){
					appId=Byte.parseByte(requestMap.get(VoizdStationParam.APPID).toString());
				}
				dataMap= stationService.createStationShareUrl(stationId, appId);
				if(null!=dataMap && dataMap.size()>0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (StationServiceException e) {
				logger.error("getStationShareUrl()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_SHARE_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/ifollow", method = RequestMethod.GET)
	public ModelAndView getIFollowList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l,followerId=0l;
		String jsonData = null;
		int startLimit=0;
		if(logger.isDebugEnabled()){
			logger.debug("StationController.getRecentStation"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationController.getRecentStation"+requestParamMap);
		}
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			followerId =(null!=httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID)):userId;
			logger.debug("getStationFollowerList.stationId="+stationId+", userId="+userId+" followerId="+followerId);
			dataMap = stationService.getIFollowList(stationId,followerId,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			logger.debug("getStationFollowerList.dataMap="+dataMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);	
		return modelAndView;
	}
		
	
	@RequestMapping(value = "/vzpr/ifollow/more", method = RequestMethod.GET)
	public ModelAndView getMoreIFollowList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l,followerId=0l;
		int endLimit =0;
		int startLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		stationId =Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)); 
		userId =(null!=httpServletRequest.getParameter(VoizdStationParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)):0l;
		followerId =(null!=httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID)):0l;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
		logger.debug("getMoreRecentStation.stationId="+stationId+", userId="+userId+",endLimit="+endLimit+" ,direction="+direction+"followerId="+followerId);
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			
			dataMap = stationService.getIFollowList(stationId,followerId,startLimit,endLimit,false,clientMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessCode());
		  jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
	
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/myfollower", method = RequestMethod.GET)
	public ModelAndView getMyStationFollowerList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l,creatorId=0l;
		String jsonData = null;
		int startLimit=0;
		if(logger.isDebugEnabled()){
			logger.debug("StationController.getRecentStation"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationController.getRecentStation"+requestParamMap);
		}
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			stationId =(null!=httpServletRequest.getParameter(VoizdStationParam.STATION_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)):0l;
			creatorId =(null!=httpServletRequest.getParameter(VoizdStationParam.CREATORID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.CREATORID)):userId;
			logger.debug("getStationFollowerList.stationId="+stationId+", userId="+userId+" creatorId="+creatorId);
			dataMap = stationService.getMyFollowerList(stationId,creatorId,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			logger.debug("getStationFollowerList.dataMap="+dataMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
		
	
	@RequestMapping(value = "/vzpr/myfollower/more", method = RequestMethod.GET)
	public ModelAndView getMoreMyStationFollowerList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l,followerId=0l;
		int endLimit =0;
		int startLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		stationId =Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.STATION_ID)); 
		userId =(null!=httpServletRequest.getParameter(VoizdStationParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.USERID)):0l;
		followerId =(null!=httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationParam.FOLLOWERID)):0l;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
		logger.debug("getMoreRecentStation.stationId="+stationId+", userId="+userId+",endLimit="+endLimit+" ,direction="+direction);
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			dataMap = stationService.getMyFollowerList(stationId,followerId,startLimit,endLimit,false,clientMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_FOLLOWER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_TAPPED_STATION_SUCCESS.getSuccessCode());
		  jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpb/topvoizer", method = RequestMethod.GET)
	public ModelAndView getTopVoizerList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long stationId=0l,userId =0l;
		String jsonData = null;
		int startLimit=0;
		if(logger.isDebugEnabled()){
			logger.debug("StationController.getTopVoizerList"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationController.getTopVoizerList"+requestParamMap);
		}
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getTopVoizerList.stationId="+stationId+", userId="+userId);
			dataMap = stationService.getTopVoizerList(userId,startLimit,VoizdWebConstant.DEFAULT_STATION_LIMIT,false,clientMap);
			logger.debug("getTopVoizerList.dataMap="+dataMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_VOIZER_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_VOIZER_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
		
	
	@RequestMapping(value = "/vzpb/topvoizer/more", method = RequestMethod.GET)
	public ModelAndView getMoreTopVoizerList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l;
		int endLimit =0;
		int startLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
		logger.debug("getMoreRecentStation., userId="+userId+",endLimit="+endLimit+" ,direction="+direction);
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getTopVoizerList., userId="+userId);
			dataMap = stationService.getTopVoizerList(userId,startLimit,endLimit,false,clientMap);
			logger.debug("getTopVoizerList.dataMap="+dataMap);
		} catch (StationServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.TOP_VOIZER_GET_FAILED_EXCEPTION.getErrorCode());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_VOIZER_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_VOIZER_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	public void setStationService(StationService stationService) {
		this.stationService = stationService;
	}


}
