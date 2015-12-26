package com.voizd.rest.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.rest.constant.UserParameters;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.RequestProcessorUtil;
import com.voizd.service.user.exception.UserServiceFailedException;
import com.voizd.service.user.exception.UserServiceValidationFailedException;
import com.voizd.service.user.v1_0.UserService;

/**
 * @author Manish
 * 
 */

@Controller
@RequestMapping("/api/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/vzpb/register", method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		UserVO userVO = null;
		RegistrationVO registerationVO = new RegistrationVO();
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			Map<String, Object> clientParamMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.debug("register : requestMap :: "+requestMap);
			RequestProcessorUtil.enrichRegistrationVO(requestMap, registerationVO,clientParamMap);
			userVO = userService.registerUser(registerationVO, clientParamMap);

		} catch (UserServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);

			if (ErrorCodesEnum.USER_ALREADY_EXIST.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_ALREADY_EXIST.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_ALREADY_EXIST.getErrorMessage());
				logger.info("User is already present in the system :::::::" + registerationVO.getUserName());
			}else if (ErrorCodesEnum.USER_DOB_MISSING.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_DOB_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_DOB_MISSING.getErrorMessage());
				logger.info("User dob is missing :::::::" + registerationVO.getUserName());
			}else if (ErrorCodesEnum.USER_DOB_INCORRECT.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_DOB_INCORRECT.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_DOB_INCORRECT.getErrorMessage());
				logger.info("User dob is incorrect :::::::" + registerationVO.getUserName());
			}else {
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				logger.error("Unexpected error in registering user ::::" + registerationVO.getUserName(), e);
			}

		} catch (UserServiceValidationFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.CODE, e.getErrorCode());
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.getErrorCodesEnum(e.getErrorCode()).getErrorMessage());
			logger.info("User service validation failed excepted :::::::");

		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			logger.error("Unexpected error in registering user ::::" + registerationVO.getUserName(), e);
		}
		if (null != userVO) {
			jsonData = gson.toJson(userVO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpr/getprofile", method = RequestMethod.GET)
	public ModelAndView getPrivateProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		Gson gson = new Gson();
		String jsonData = null;
		UserDTO userDTO = null;
		Long userId = 0l;
		try {
			userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			userDTO = userService.getUserPrivateProfile(userId, clientMap);
			logger.info("userDTO :::::::" + userDTO);
		} catch (UserServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.info("User not present in the system :::::::" + userId);
			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in fetching user details for the user ::::" + userId, e);
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in fetching user details for the user ::::" + userId, e);
		}
		if (null != userDTO) {
			jsonData = gson.toJson(userDTO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserController.jsonData=" + jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpb/getprofile", method = RequestMethod.GET)
	public ModelAndView getPublicProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		Gson gson = new Gson();
		String jsonData = null;
		UserDTO userDTO = null;
		Long userId = 0l;
		Long visitorId=0l;
		try {
			
			visitorId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			if(null!=httpServletRequest.getParameter(UserParameters.USERID)){
				userId = Long.parseLong(httpServletRequest.getParameter(UserParameters.USERID));
			}
			if(visitorId==0){
				userId = Long.parseLong(httpServletRequest.getParameter(UserParameters.VISITOR_ID));
			}
			if(userId==0){
				HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
				if(null != requestMap.get(UserParameters.VISITOR_ID)){
					visitorId = Long.parseLong(requestMap.get(UserParameters.VISITOR_ID));	
				}
				if(null != requestMap.get(UserParameters.USERID)){
					userId = Long.parseLong(requestMap.get(UserParameters.USERID));	
				}
			}
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			
			logger.debug("UserController.getPublicProfile=" + userId+" visitorId="+visitorId);
			userDTO = userService.getUserPublicProfile(userId,visitorId, clientMap);

		} catch (UserServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.info("User not present in the system :::::::" + userId);
			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in fetching user details for the user ::::" + userId, e);
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in fetching user details for the user ::::" + userId, e);
		}
		if (null != userDTO) {
			jsonData = gson.toJson(userDTO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/friendprofile", method = RequestMethod.POST)
	public ModelAndView getFriendProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		Gson gson = new Gson();
		String jsonData = null;
		UserDTO userDTO = null;
		String friendId = null;
		Long visitorId=0l;
		int thirdPartyId = 0;
		try {
			
			visitorId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			logger.debug("getFriendProfile.visitorId=" + visitorId);
			if(visitorId>0){
				HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
				if(null != requestMap.get(UserParameters.FRIEND_ID)){
					friendId = requestMap.get(UserParameters.FRIEND_ID).toString();	
				}
				if(null != requestMap.get(UserParameters.PARTNER_APP_ID)){
					thirdPartyId = Integer.parseInt(requestMap.get(UserParameters.PARTNER_APP_ID));	
				}
				
			}
			logger.debug("getFriendProfile.friendId=" + friendId+" thirdPartyId="+thirdPartyId);
			if(StringUtils.isNotBlank(friendId)){
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				userDTO = userService.getFriendPublicProfile(friendId,thirdPartyId,visitorId, clientMap);
			}

		} catch (UserServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.info("User not present in the system :::::::" + friendId);
			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in fetching user details for the user ::::" + friendId, e);
			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in fetching user details for the user ::::" + friendId, e);
		}
		if (null != userDTO) {
			jsonData = gson.toJson(userDTO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserController.jsonData=" + jsonData);
		return modelAndView;
	}

	
	@RequestMapping(value = "/vzpr/updateprofile", method = RequestMethod.POST)
	public ModelAndView updateProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		long start = System.currentTimeMillis();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		UserVO userVO = null;
		UserVO returnUserVO = null;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			userVO = new UserVO();
			RequestProcessorUtil.enrichUserVO(requestMap, userVO,null);
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			logger.info("Time taken updateProfile.enrichUserVO: " + (System.currentTimeMillis() - start)
					+ " msec");
			if(userId==userVO.getId()){
				returnUserVO = userService.updateUser(userVO,clientMap);
			}
			logger.info("Time taken updateProfile.returnUserVO: " + (System.currentTimeMillis() - start)
					+ " msec");
		} catch (UserServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);

			if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.info("User not present in the system :::::::" + userVO.getUserName());

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in updating user ::::" + userVO.getUserName(), e);

			}
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in registering user ::::" + userVO.getUserName(), e);

		}
		if (null != returnUserVO) {
			jsonData = gson.toJson(returnUserVO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("UserController.jsonData=" + jsonData);
		logger.info("Time taken updateProfile: " + (System.currentTimeMillis() - start)
				+ " msec");
		return modelAndView;
	}
}
