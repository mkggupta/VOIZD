package com.voizd.rest.controller.content;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.dto.StationDTO;
import com.voizd.common.beans.vo.VStreamVO;

import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.constant.VoizdStationParam;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.StationContentHelperManager;

import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.service.content.v1_0.ContentService;


@Controller
@RequestMapping("/api/getcontent")
public class ContentController {
	private static Logger logger = LoggerFactory.getLogger(ContentController.class);
	private ContentService contentService = null ;
	
	
	@RequestMapping(value = "/vzpb/get", method = RequestMethod.GET)
	public ModelAndView getStationContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		Long stationId=0l,userId =0l,contentId=0l;
		String jsonData = null;
		Gson gson = new Gson();
		try {
			stationId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID)):0l;
			contentId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID)):0l;
			userId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.USERID)):0l;
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getStationContent.stationId="+stationId+" contentId="+contentId+" userId="+userId);
			if(stationId>0){
				dataMap = contentService.getStationContents(stationId, contentId, userId,com.voizd.common.constant.VoizdConstant.MEDIA_ACTIVE_STATUS,VoizdWebConstant.DEFAULT_CONTENT_LIMIT, false,clientMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_CLIP_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_CLIP_SUCCESS.getSuccessCode());
			}
		} catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			if(null!=dataMap.get(VoizdConstant.STATION)){
			StationDTO stationDTO = (StationDTO) dataMap.get(VoizdConstant.STATION);
			jsonData = gson.toJson(stationDTO);
			}else{
				 jsonData = gson.toJson(dataMap);
			}
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.NO_CLIP_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.NO_CLIP_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
	
		return modelAndView;
	}
	
	@RequestMapping(value = "/getdetail", method = RequestMethod.GET)
	public ModelAndView getStationContentDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		Long stationId=0l,userId =0l,contentId=0l;
		String jsonData = null;
		Gson gson = new Gson();
		
			stationId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID)):0l;
			contentId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID)):0l;
			userId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.USERID)):0l;
		
			
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getStationContent.stationId="+stationId+" contentId="+contentId+" userId="+userId);
			if(stationId>0 && contentId>0 && userId> 0){
				dataMap = contentService.getStationContentDetail(stationId, contentId, userId,clientMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			if(null!=dataMap.get(VoizdConstant.STATION)){
			StationDTO stationDTO = (StationDTO) dataMap.get(VoizdConstant.STATION);
			jsonData = gson.toJson(stationDTO);
			}else{
				 jsonData = gson.toJson(dataMap);
			}
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpb/getvoizdetail", method = RequestMethod.POST)
	public ModelAndView getVoizDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Long stationId=0l,userId =0l,contentId=0l;
		String jsonData = null;
		Gson gson = new Gson();
			
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if(null!=requestMap && requestMap.size()>0){
				stationId =(null!=requestMap.get(VoizdStationContentParam.STATION_ID))?Long.parseLong(requestMap.get(VoizdStationContentParam.STATION_ID)):0l;
				contentId = (null!=requestMap.get(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID)):0l;
				userId =(null!=requestMap.get(VoizdStationContentParam.USERID))?Long.parseLong(requestMap.get(VoizdStationContentParam.USERID)):0l;
			}
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("getStationContent.stationId="+stationId+" contentId="+contentId+" userId="+userId);
			if(contentId>0 && userId> 0){
				dataMap = contentService.getVoizDetail(stationId, contentId, userId,clientMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
				 jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/get/more", method = RequestMethod.GET)
	public ModelAndView getStationContentMore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		Long stationId=0l,contentId=0l,userId=0l;
		int endLimit =0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		Gson gson = new Gson();
		stationId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.STATION_ID)):0l;
		contentId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID)):0l;
		userId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.USERID)):0l;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
		try {
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("ContentCreateController.stationId="+stationId+" contentId="+contentId+" userId="+userId);
			 if(stationId>0 && contentId>0){
				dataMap = contentService.getStationContents(stationId, contentId, userId,com.voizd.common.constant.VoizdConstant.MEDIA_ACTIVE_STATUS, endLimit, order,clientMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				logger.error("ContentCreateController.getStationContentMore stationId="+stationId+" contentId="+contentId+" userId="+userId);
			}
		} catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			if(null!=dataMap.get(VoizdConstant.STATION)){
			StationDTO stationDTO = (StationDTO) dataMap.get(VoizdConstant.STATION);
			jsonData = gson.toJson(stationDTO);
			}else{
				 jsonData = gson.toJson(dataMap);
			}
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
	
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpb/share", method = RequestMethod.POST)
	public ModelAndView getContentShareUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);

			if(null!=requestMap && requestMap.size()>0){
				Long stationId = 0l,contentId=0l ;
				Byte appId =0 ;
				
				try {
					if(null!=requestMap.get(VoizdStationContentParam.STATION_ID)){
						stationId=Long.parseLong(requestMap.get(VoizdStationContentParam.STATION_ID).toString());
					}
					if(null!=requestMap.get(VoizdStationContentParam.APPID)){
						appId=Byte.parseByte(requestMap.get(VoizdStationContentParam.APPID).toString());
					}
					if(null!=requestMap.get(VoizdStationContentParam.CONTENT_ID)){
						contentId=Long.parseLong(requestMap.get(VoizdStationContentParam.CONTENT_ID).toString());
					}
					Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
					dataMap= contentService.getContentShareUrl(stationId,contentId,appId,userId);
					if(null!=dataMap && dataMap.size()>0){
						dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					}else{
						dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
						dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorMessage());
						dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorCode());
					}
				} catch (ContentServiceException e) {
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorCode());
				}
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_SHARE_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);

		return modelAndView;
	}
	@RequestMapping(value = "/vzpb/{type}", method = RequestMethod.GET)
	public ModelAndView getStationContentList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		Long contentId=0l;
		int startLimit=0;
		String jsonData = null;
		Gson gson = new Gson();
		
			try {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				logger.debug("ContentCreateController.userId="+userId);
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				if(VoizdConstant.RECENT.equalsIgnoreCase(type)){
					VStreamVO  vStreamVO  = new VStreamVO();
					vStreamVO.setUserId(userId);
					vStreamVO.setStartLimit(0);
					vStreamVO.setEndLimit(VoizdWebConstant.DEFAULT_CONTENT_LIMIT);
					dataMap = contentService.getRecentVStreamContents(vStreamVO,clientMap);	
					//dataMap = contentService.getStationContentList(contentId, userId,userId,VoizdConstant.RECENT_CONTENT_STATUS,startLimit,VoizdWebConstant.DEFAULT_CONTENT_LIMIT, false,clientMap);
				}else if(VoizdConstant.POPULAR.equalsIgnoreCase(type)){
					dataMap = contentService.getStationContentList(contentId, userId,userId,VoizdConstant.POPULAR_CONTENT_STATUS,startLimit, VoizdWebConstant.DEFAULT_CONTENT_LIMIT, false,clientMap);
				}else{
					logger.debug("ContentController.not supported type "+type);
				}
			} catch (ContentServiceException e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
	

		logger.debug("ContentCreateController.dataMap="+dataMap);
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		logger.debug("ContentCreateController.jsonData = "+jsonData);
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/vzpb/{type}/more", method = RequestMethod.GET)
	public ModelAndView getStationMoreContentList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
	
		Long contentId=0l,userId=0l,visitorId=0l;
		int endLimit =0,startLimit=0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		Gson gson = new Gson();
		contentId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID)):0l;
		userId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.USERID)):0l;
		visitorId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.VISITORID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.VISITORID)):userId;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
			try {
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				if(VoizdConstant.RECENT.equalsIgnoreCase(type)){
					VStreamVO  vStreamVO  = new VStreamVO();
					vStreamVO.setUserId(userId);
					vStreamVO.setStartLimit(startLimit);
					vStreamVO.setEndLimit(endLimit);
					dataMap = contentService.getRecentVStreamContents(vStreamVO,clientMap);	
					//dataMap = contentService.getStationContentList(contentId, userId,visitorId,VoizdConstant.RECENT_CONTENT_STATUS,startLimit, endLimit, order,clientMap);
				}else if(VoizdConstant.POPULAR.equalsIgnoreCase(type)){
				
					dataMap = contentService.getStationContentList(contentId,userId,visitorId,VoizdConstant.POPULAR_CONTENT_STATUS,startLimit,endLimit, order,clientMap);
				}else{
					logger.debug("ContentController.not supported type "+type);
				}
			} catch (ContentServiceException e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
	

		
			if(null!=dataMap && dataMap.size()>0){
				jsonData = gson.toJson(dataMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
	    httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
	
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/{type}", method = RequestMethod.GET)
	public ModelAndView getMyStationContentList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		Long contentId=0l;
		int startLimit=0;
		int endLimit =10;
		String jsonData = null;
		Gson gson = new Gson();
		
			try {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				
				Long visitorId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.VISITORID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.VISITORID)):userId;
				logger.debug("ContentController.userId="+userId+" visitorId="+visitorId);
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				if(VoizdConstant.MYBOOKMARK.equalsIgnoreCase(type)){
					dataMap = contentService.getMyBookMarkContentList(userId,visitorId,VoizdConstant.CONTENT_LIKE_STATUS,startLimit,VoizdWebConstant.DEFAULT_CONTENT_LIMIT, false,clientMap);
				}else if(VoizdConstant.MY.equalsIgnoreCase(type)){
					VStreamVO vStreamVO = StationContentHelperManager.enrichVStramVO(visitorId,userId,VoizdConstant.MY,startLimit,endLimit);
					dataMap = contentService.getUserVStreamContents(vStreamVO,clientMap);	
					//dataMap = contentService.getStationContentList(contentId, userId,visitorId,VoizdConstant.MY_CONTENT_STATUS,startLimit, VoizdWebConstant.DEFAULT_CONTENT_LIMIT, false,clientMap);
				}else{
					logger.debug("ContentController.not supported type "+type);
				}
			} catch (ContentServiceException e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
	

		logger.debug("ContentCreateController.dataMap="+dataMap);
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/vzpr/{type}/more", method = RequestMethod.GET)
	public ModelAndView getMyStationMoreContentList(@PathVariable String type,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
	
		Long contentId=0l,userId=0l,visitorId=0l;
		int endLimit =0,startLimit=0;
		boolean order = false ;
		String direction = null;
		String jsonData = null;
		Gson gson = new Gson();
		contentId = (null!=httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID)):0l;
		userId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.USERID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.USERID)):0l;
		visitorId =(null!=httpServletRequest.getParameter(VoizdStationContentParam.VISITORID))?Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.VISITORID)):userId;
		endLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.ENDLIMIT));
		startLimit =Integer.parseInt(httpServletRequest.getParameter(VoizdStationParam.STARTLIMIT));
		direction =httpServletRequest.getParameter(VoizdStationParam.DIRECTION);
		if(StringUtils.isNotBlank(direction) && direction.equalsIgnoreCase(VoizdConstant.ASCENDING)){
			order = true ;
		}
		
			try {
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				if(VoizdConstant.MYBOOKMARK.equalsIgnoreCase(type)){
					dataMap = contentService.getMyBookMarkContentList(userId,visitorId,VoizdConstant.CONTENT_LIKE_STATUS,startLimit, endLimit, false,clientMap);
				}else if(VoizdConstant.MY.equalsIgnoreCase(type)){
					dataMap = contentService.getStationContentList(contentId, userId,visitorId,VoizdConstant.MY_CONTENT_STATUS,startLimit, endLimit, false,clientMap);
				}else{
					logger.debug("ContentController.not supported type "+type);
				}
			} catch (ContentServiceException e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			}
	

		
			if(null!=dataMap && dataMap.size()>0){
				jsonData = gson.toJson(dataMap);
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
				jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/content/vzpr/vstream", method = RequestMethod.POST)
	public ModelAndView getUserVStream(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		String jsonData = null;
		Gson gson = new Gson();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			VStreamVO vStreamVO = StationContentHelperManager.enrichVStramVO(requestMap,false);
			vStreamVO.setVisitorId(userId);
			dataMap = contentService.getUserVStreamContents(vStreamVO,clientMap);	
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		}catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}	
		
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			jsonData = gson.toJson(dataMap);
		}
     	logger.debug("getUserVStream.jsonData="+jsonData);
     	httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/content/vzpr/vstream/more", method = RequestMethod.GET)
	public ModelAndView getUserVStreamMore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		String jsonData = null;
		Gson gson = new Gson();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			VStreamVO vStreamVO = StationContentHelperManager.enrichVStramVO(requestMap,true);
			vStreamVO.setVisitorId(userId);
			dataMap = contentService.getUserVStreamContents(vStreamVO,clientMap);	
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
		}catch (ContentServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
		}	

		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_GET_FAILED_EXCEPTION.getErrorCode());
			jsonData = gson.toJson(dataMap);
		}
		httpServletResponse.setHeader("Content-Type", "application/json");
		modelAndView.addObject("frameResponse", httpServletResponse);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

}
