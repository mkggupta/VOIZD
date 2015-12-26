package com.voizd.rest.controller.advt;

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
import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.advt.exception.AdvtServiceException;
import com.voizd.service.advt.v1_0.AdvtService;

@Controller
@RequestMapping("/api/advt")
public class AdvtController {
	
	private static Logger logger = LoggerFactory.getLogger(AdvtController.class);
	private AdvtService advtService ;
	
	public void setAdvtService(AdvtService advtService) {
		this.advtService = advtService;
	}

	@RequestMapping(value = "/getMic", method = RequestMethod.GET)
	public ModelAndView getMicAdvt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			
			try {
				AdvtMicInfo advtMicInfo = advtService.getMicAdvt(clientMap) ;
				dataMap.put(VoizdWebConstant.ADVT, advtMicInfo);
				if(null!=dataMap && dataMap.size()>0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.ADVT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.ADVT_SERVICE_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (AdvtServiceException e) {
				logger.error("getMicAdvt()"+e.getLocalizedMessage(),e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.ADVT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.ADVT_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
	

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}
	
	

}
