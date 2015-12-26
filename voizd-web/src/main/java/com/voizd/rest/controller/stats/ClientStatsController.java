package com.voizd.rest.controller.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.voizd.common.beans.vo.StatsVO;
import com.voizd.common.constant.ClientStatsConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.stats.exception.ClientStatServiceException;
import com.voizd.service.stats.v1_0.ClientStatService;


@Controller
@RequestMapping("/api/stats")
public class ClientStatsController {
	private static Logger logger = LoggerFactory.getLogger(ClientStatsController.class);
	private ClientStatService clientStatService = null ;
	@RequestMapping(value = "/vzpb/save")
	public ModelAndView saveClientStats(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long userId =0l;
		String jsonData = null;
	
		
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			logger.debug("saveClientStats., userId="+userId);
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			logger.debug(" requestMap=          "+requestMap);
			if(null!=requestMap && requestMap.size()>0 ){
				logger.debug("saveClientStats., requestMap="+requestMap.get(VoizdConstant.CLIENT_STATS_KEY_HEADER));
				
					 String response = requestMap.get(VoizdConstant.CLIENT_STATS_KEY_HEADER);
					 JsonParser JSONValue = new JsonParser();
					 JsonElement jsonElement=JSONValue.parse(response);
					 JsonObject obj2 = jsonElement.getAsJsonObject();
				
					  	if(null!=obj2){
					  		Map<String, Object> resultMap = new HashMap<String, Object>();
					  		List<StatsVO> statsVOList = new ArrayList<StatsVO>();
					  		JsonArray array ;
					  	if(null!=obj2.get(ClientStatsConstant.PLAY)){
					  		array= obj2.get(ClientStatsConstant.PLAY).getAsJsonArray();
					  		if(null!=array){
								for (int i=0; i<array.size(); i++){
									  StatsVO statsVO =gson.fromJson(array.get(i),StatsVO.class);
									  logger.debug("saveClientStats.paly statsVO ="+statsVO.getCreatorId());
									  if(null!=statsVO.getCreatorId()){
										  statsVO.setUserId(userId);
										  statsVOList.add(statsVO);
									  }
								  }
					  		}

							 logger.debug("saveClientStats.statsVOList= "+statsVOList);
							if(null!=statsVOList && statsVOList.size()>0){
								resultMap.put(ClientStatsConstant.PLAY, statsVOList);
								statsVOList = new ArrayList<StatsVO>();
							}
					  	}
							if(null!=obj2.get(ClientStatsConstant.SHARE)){
								array = obj2.get(ClientStatsConstant.SHARE).getAsJsonArray();
								if(null!=array){
									for (int i=0; i<array.size(); i++){
										  StatsVO statsVO =gson.fromJson(array.get(i),StatsVO.class);
										  logger.debug("saveClientStats.sharstatsVO="+statsVO.getCreatorId());
										  statsVO.setUserId(userId);
										  statsVOList.add(statsVO);
									  }
								}
								 logger.debug("saveClientStats.share sVOList= "+statsVOList);
								if(null!=statsVOList && statsVOList.size()>0){
									resultMap.put(ClientStatsConstant.SHARE, statsVOList);
									statsVOList = new ArrayList<StatsVO>();
								}
							}
							logger.debug("saveClientStats.share obj2.get(ClientStatsConstant.COMMENT_PLAY)= "+obj2.get(ClientStatsConstant.COMMENT_PLAY));
							if(null!=obj2.get(ClientStatsConstant.COMMENT_PLAY)){
								array = obj2.get(ClientStatsConstant.COMMENT_PLAY).getAsJsonArray();
								if(null!=array){
									for (int i=0; i<array.size(); i++){
										  StatsVO statsVO =gson.fromJson(array.get(i),StatsVO.class);
										  logger.debug("saveClientStats.COMMENT_PLAY"+statsVO.getCmtId());
										  statsVO.setUserId(userId);
										  statsVOList.add(statsVO);
									  }
								}
								if(null!=statsVOList && statsVOList.size()>0){
									resultMap.put(ClientStatsConstant.COMMENT_PLAY, statsVOList);
									statsVOList = new ArrayList<StatsVO>();
								}
							}
							 logger.debug("saveClientStats.resultMap= "+resultMap);
					  		if(null!=resultMap && resultMap.size()>0){
								 clientStatService.saveAppStats(resultMap);
								 logger.debug("saveClientStats.done= ");
							 }
					  	}else{
					  		logger.error("saveClientStats., client is not sending any data response="+response);
					  	}
				
					 
				
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.STATS_DATA_MISSING.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.STATS_DATA_MISSING.getErrorCode());
			}
			
			
		} catch (ClientStatServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.CLIENT_STATS_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.CLIENT_STATS_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.CLIENT_STATS_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.CLIENT_STATS_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
	
		 if(dataMap.size()==0){
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.STATS_SAVE_SUCCESS.getSuccessMessage());
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.STATS_SAVE_SUCCESS.getSuccessCode());
		  }
		 jsonData = gson.toJson(dataMap);
		
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("saveClientStats.jsonData="+jsonData);
		return modelAndView;
	}
	
	


	public void setClientStatService(ClientStatService clientStatService) {
		this.clientStatService = clientStatService;
	}
	
	

}
