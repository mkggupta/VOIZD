package com.voizd.rest.controller.content;

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
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.StationContentHelperManager;
import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.service.content.v1_0.ContentService;

@Controller
@RequestMapping("/api/content/vzpr")
public class ContentCreateController {
	private static Logger logger = LoggerFactory.getLogger(ContentCreateController.class);
	private ContentService contentService = null ;
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createStationContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0){	
			try {
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				ContentVO  contentVO  = StationContentHelperManager.enrichStationContentVo(requestMap,clientMap);
				dataMap = contentService.createStationContent(contentVO);
				logger.debug("dataMap "+dataMap);
				if(null!=dataMap && dataMap.size()>0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.CLIP_CREATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CLIP_CREATE_SUCCESS.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (ContentServiceException e) {
				logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
					logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateStationContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		try {
				ContentVO  contentVO  = StationContentHelperManager.enrichUpdateStationContentVo(requestMap);
			    contentService.updateStationContent(contentVO.getTag(),contentVO.getFileIds(),contentVO.getContentId(),contentVO.getCreatorId());
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.CLIP_UPDATE_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CLIP_UPDATE_SUCCESS.getSuccessCode());
		}catch(ContentServiceException e){
			logger.error("ContentCreateController.updateStationContent() "+e.getLocalizedMessage() ,e);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			if(ErrorCodesEnum.STATION_CONTENT_UPDATE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_UPDATION_FAILED_EXCEPTION.getErrorMessage());
			}else{
				dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_CONTENT_UPDATION_FAILED_EXCEPTION.getErrorMessage());
			}
			dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.STATION_CONTENT_UPDATION_FAILED_EXCEPTION.getErrorCode());
		}catch(Exception e){
			logger.error("ContentCreateController.updateStationContent() "+e.getLocalizedMessage() ,e);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_CONTENT_UPDATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.STATION_CONTENT_UPDATION_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);	
		return modelAndView;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView deleteStationContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		if(null!=requestMap && requestMap.size()>0 ){
			
			try {
					ContentVO  contentVO  = StationContentHelperManager.enrichUpdateStationContentVo(requestMap);
					contentService.deleteStationContent(contentVO.getStationId(),contentVO.getContentId(),contentVO.getCreatorId());
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.CLIP_DELETE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CLIP_DELETE_SUCCESS.getSuccessCode());

			}catch(ContentServiceException e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				if(ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}else if(ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorCode());
				}else{
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}
			}catch(Exception e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);	
		return modelAndView;
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public ModelAndView postAdvtContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		int responseCode = 0 ;
		
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0){	
			try {
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				ContentVO  contentVO  = StationContentHelperManager.enrichStationContentVo(requestMap,clientMap);
				dataMap = contentService.createStationContent(contentVO);
				if(responseCode==VoizdWebConstant.SUCCESS_CODE){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.CLIP_CREATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CLIP_CREATE_SUCCESS.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (ContentServiceException e) {
				logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
					logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CONTENT_CREATION_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}

}
