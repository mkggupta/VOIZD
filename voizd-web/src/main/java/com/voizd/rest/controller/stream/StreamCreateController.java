package com.voizd.rest.controller.stream;

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
import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.StreamHelperManager;
import com.voizd.service.stream.exception.StreamServiceException;
import com.voizd.service.stream.v1_0.StreamService;
@Controller
@RequestMapping("/api/stream/vzpr")
public class StreamCreateController {
	private static Logger logger = LoggerFactory.getLogger(StreamCreateController.class);
	private StreamService streamService = null ;
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createStream(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Long streamId = 0l ;
		if(logger.isDebugEnabled()){
			logger.debug("StreamCreateController.createStream"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StreamCreateController.requestParamMap"+requestParamMap);
		}
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0 ){
			try {
				StreamVO  streamVO  = StreamHelperManager.enrichStreamVO(requestMap);
				streamId = streamService.createStream(streamVO);
				logger.debug("StreamCreateController.streamId="+streamId);
				if(streamId>0){
					dataMap.put(VoizdWebConstant.STREAM_ID, streamId);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE,SuccessCodesEnum.STREAM_CREATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.STREAM_CREATE_SUCCESS.getSuccessCode());
				}else if(streamId<0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.STREAM_CREATE_ALREADY_EXIST.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.STREAM_CREATE_ALREADY_EXIST.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (StreamServiceException e) {
				logger.error("createStation()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				logger.error("createStream()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_CREATION_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("StreamCreateController.jsonData="+jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		try {
			StreamVO  streamVO= StreamHelperManager.enrichUpdateStreamVo(requestMap);
			if(null != streamVO){
				logger.debug("updateStation ::::::::: con getStreamid ::: "+streamVO.getStreamId());
				try {
					streamService.updateStream(streamVO);	
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE,SuccessCodesEnum.STREAM_UPDATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.STREAM_UPDATE_SUCCESS.getSuccessCode());
				
				} catch (StreamServiceException e) {
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					if (ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
						dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorMessage());
						dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorCode());
						logger.debug("updateStation :: "+e.getLocalizedMessage(),e);
					}else if((ErrorCodesEnum.STREAM_UPDATE_DATA_NOT_FOUND.getErrorCode().equalsIgnoreCase(e.getErrorCode()))){
						dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STREAM_UPDATE_DATA_NOT_FOUND.getErrorMessage());
						dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_UPDATE_DATA_NOT_FOUND.getErrorCode());
						logger.debug("updateStation :: "+e.getLocalizedMessage(),e);
					}
					
				}
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STREAM_UPDATION_FAILED_EXCEPTION.getErrorCode());
		}
        
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView deleteStreamContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		if(null!=requestMap && requestMap.size()>0 ){
			
			try {
				StreamVO  streamVO = StreamHelperManager.enrichUpdateStreamVo(requestMap);
				if(null!=streamVO){
					logger.debug("streamVO--"+streamVO);
					streamService.deleteStream(streamVO.getStreamId(), streamVO.getUserId());
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.CLIP_DELETE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CLIP_DELETE_SUCCESS.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}
			}catch(StreamServiceException e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				if(ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}else if(ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorMessage());
					dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorCode());
				}else{
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}
			}catch(Exception e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CONTENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				
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
	
	
	public void setStreamService(StreamService streamService) {
		this.streamService = streamService;
	}
	



}
