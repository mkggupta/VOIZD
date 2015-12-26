package com.voizd.rest.controller.push;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.util.ControllerUtils;
import com.voizd.rest.constant.UserParameters;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.user.v1_0.UserService;

@Controller
@RequestMapping("/api/push/vzpb")
public class PushController {
private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	private static Logger logger = LoggerFactory.getLogger(PushController.class);
	
	@RequestMapping(value = "/status", method = RequestMethod.POST)
	public ModelAndView updateUserPushStatus(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if(null!=requestMap && requestMap.size()>0){
				if(null!=requestMap.get(UserParameters.UID) && null!=requestMap.get(UserParameters.PUSH_STATUS) ){
					Long userId = Long.parseLong(requestMap.get(UserParameters.UID).toString());
					String pushStatus = requestMap.get(UserParameters.PUSH_STATUS);
					userService.updateUserPushStatus(userId, pushStatus);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.SUCCESS_CODE);
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					logger.error("Exception while updateUserPushStatus= "+requestMap);
				}
			}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				logger.error("Exception while updateUserPushStatus "+requestMap);
			}
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("Exception while updateUserPushStatus "+e.getLocalizedMessage(),e);
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("PushController :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;

	}
}
