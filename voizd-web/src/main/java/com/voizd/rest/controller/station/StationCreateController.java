package com.voizd.rest.controller.station;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.StationHelperManager;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.v1_0.StationService;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/api/station/vzpr")
public class StationCreateController {
	private static Logger logger = LoggerFactory.getLogger(StationCreateController.class);
	private StationService stationService = null ;
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Integer stationId = 0 ;
		if(logger.isDebugEnabled()){
			logger.debug("StationCreateController.createStation"+httpServletRequest);
			Map<String,String> requestParamMap = ControllerUtils.parseHeader(httpServletRequest);
			logger.debug("StationCreateController.requestParamMap"+requestParamMap);
		}
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0 ){
			try {
				StationVO  stationVO = StationHelperManager.enrichStationVo(requestMap);
				stationId = stationService.createStation(stationVO);
				logger.debug("StationCreateController.stationId="+stationId);
				if(stationId>0){
					dataMap.put(VoizdWebConstant.STATION_ID, stationId);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE,SuccessCodesEnum.STATION_CREATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.STATION_CREATE_SUCCESS.getSuccessCode());
				}else if(stationId<0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.STATION_CREATE_ALREADY_EXIST.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.STATION_CREATE_ALREADY_EXIST.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (StationServiceException e) {
				logger.error("createStation()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
				logger.error("createStation()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_CREATION_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("StationCreateController.jsonData="+jsonData);
		return modelAndView;
	}
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		try {
			StationVO stationVO = StationHelperManager.enrichUpdateStationVo(requestMap);
			if(null != stationVO){
				logger.debug("enrichUpdateStationVo ::::::::: con getStationId ::: "+stationVO.getStationId());
				try {
					stationService.updateStation(stationVO);	
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE,SuccessCodesEnum.STATION_UPDATE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE,SuccessCodesEnum.STATION_UPDATE_SUCCESS.getSuccessCode());
				
				} catch (StationServiceException e) {
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					if (ErrorCodesEnum.STATION_SERVICE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
						dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
						dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
						logger.debug("updateStation :: "+e.getLocalizedMessage(),e);
					}else if((ErrorCodesEnum.STATION_SERVICE_UPDATE_DATA_NOT_FOUND.getErrorCode().equalsIgnoreCase(e.getErrorCode()))){
						dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorMessage());
						dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorCode());
						logger.debug("updateStation :: "+e.getLocalizedMessage(),e);
					}
					
				}
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorCode());
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATION_UPDATION_FAILED_EXCEPTION.getErrorCode());
		}
        
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	

	public void setStationService(StationService stationService) {
		this.stationService = stationService;
	}


}
