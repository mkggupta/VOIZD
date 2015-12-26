/**
 * 
 */
package com.voizd.rest.controller.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.encryption.EncryptionFactory;
import com.voizd.framework.encryption.enumeration.EncryptionAlgoEnum;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.UserParameters;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;
import com.voizd.service.authentication.v1_0.AuthenticationService;

/**
 * @author Manish
 * 
 */
@Controller
@RequestMapping("/api/authentication")
public class AuthenticationController {
	private AuthenticationService authenticationService;

	/**
	 * @return the authenticationService
	 */
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	/**
	 * @param authenticationService
	 *            the authenticationService to set
	 */
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ModelAndView authenticate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		AuthenticationDetailsVO authenticationDetailsVO = null;
		String userName = null;
		String password = null;
		boolean isError = false;
		int loginMode = 0;
		String partnerUserKey = null;
		String appId = null;
		String pushKey=null;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			String currentClientVersion = (String) clientMap.get(ClientParamConstant.CLIENT_VERSION);
			String currentPlatform = (String) clientMap.get(ClientParamConstant.PLATFORM);
			logger.debug("authenticate requestMap="+requestMap);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);
				pushKey = requestMap.get(UserParameters.PUSH_KEY);
			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}
			if (null != requestMap.get(UserParameters.LOGIN_MODE)) {
				loginMode = Integer.parseInt(requestMap.get(UserParameters.LOGIN_MODE));
			}
			if (loginMode > 0) {

				if (null != requestMap.get(UserParameters.PARTNER_USER_KEY)) {
					partnerUserKey = requestMap.get(UserParameters.PARTNER_USER_KEY);
				} else {
					isError = true;
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.PARTNER_USER_KEY_MISSING.getErrorCode());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.PARTNER_USER_KEY_MISSING.getErrorMessage());
				}
				if (null != requestMap.get(UserParameters.PARTNER_APP_ID)) {
					appId = requestMap.get(UserParameters.PARTNER_APP_ID);
				} else {
					isError = true;
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.APP_ID_MISSING.getErrorCode());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.APP_ID_MISSING.getErrorMessage());
				}

				if (!isError) {
					authenticationDetailsVO = authenticationService.authenticateUser(userName, loginMode, partnerUserKey, appId, currentClientVersion,
							currentPlatform, true,clientMap,pushKey);
				}
			} else {
				if (null != requestMap.get(UserParameters.PASSWORD)) {
					password = requestMap.get(UserParameters.PASSWORD);
				} else {
					isError = true;
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.PASSWORD_MISSING.getErrorCode());
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.PASSWORD_MISSING.getErrorMessage());
				}

				if (!isError) {
					authenticationDetailsVO = authenticationService.authenticateUser(userName, password, currentClientVersion, currentPlatform,
							true, clientMap,pushKey);
				}
			}

		} catch (AuthenticationServiceFailedException e) {

			isError = true;

			if (ErrorCodesEnum.USER_STATUS_INACTIVE.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_STATUS_INACTIVE.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_STATUS_INACTIVE.getErrorCode());
				logger.info("Exception in authentication : user is inactive : username : " + userName);

			} else if (ErrorCodesEnum.USER_IS_BLOCKED.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_IS_BLOCKED.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_IS_BLOCKED.getErrorCode());
				logger.info("Exception in authentication : user is blocked : username : " + userName);

			} else if (ErrorCodesEnum.AUTHENTICATION_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_PASSWORD_WRONG.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_PASSWORD_WRONG.getErrorCode());
				logger.info("Exception in authentication : Authentication failed : username : " + userName);

			} else if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				if (loginMode > 0) {
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
					logger.info("Exception in authentication : Invalid username : username : " + userName);
				}else{
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_EXIST_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_EXIST_EXCEPTION.getErrorCode());
					logger.info("Exception in authentication : Invalid username : username : " + userName +"--"+loginMode);
				}

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in authentication for username ::::" + userName, e);

			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in authentication for username ::::" + userName, e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (null != authenticationDetailsVO) {
			jsonData = gson.toJson(authenticationDetailsVO);
		} else {
			if (dataMap.size() == 0) {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpr/logout", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		String userName = null;
		boolean isError = false;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);
				authenticationService.logoutUser(userName);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}

		} catch (AuthenticationServiceFailedException e) {
			isError = true;

			if (ErrorCodesEnum.ONLINE_STATUS_CHANGE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.ONLINE_STATUS_CHANGE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.ONLINE_STATUS_CHANGE_FAILED_EXCEPTION.getErrorCode());
				logger.info("Exception in logout : error in changing user online status : username : " + userName);

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in logout for username ::::" + userName, e);

			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in logout for username ::::" + userName, e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
		jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpr/changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		String userName = null;
		String password = null;
		String oldPassword = null;
		boolean isError = false;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}

			if (null != requestMap.get(UserParameters.PASSWORD)) {
				password = requestMap.get(UserParameters.PASSWORD);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.PASSWORD_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.PASSWORD_MISSING.getErrorMessage());
			}

			if (null != requestMap.get(UserParameters.OLD_PASSWORD)) {
				oldPassword = requestMap.get(UserParameters.OLD_PASSWORD);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.OLD_PASSWORD_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.OLD_PASSWORD_MISSING.getErrorMessage());
			}

			if (!isError) {
				authenticationService.changePassword(userName, password, oldPassword);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
			}

		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			if (ErrorCodesEnum.OLD_PASSWORD_IN_CORRECT.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.OLD_PASSWORD_IN_CORRECT.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.OLD_PASSWORD_IN_CORRECT.getErrorCode());
				logger.error("Unexpected error in change password for username ::::" + userName + " password: " + password + " , oldPassword " + oldPassword, e);

			} else if (ErrorCodesEnum.OLD_PASSWORD_MISSING.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.OLD_PASSWORD_MISSING.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.OLD_PASSWORD_MISSING.getErrorCode());
				logger.error("Unexpected error in change password for username ::::" + userName + " password: " + password + " , oldPassword " + oldPassword, e);

			} else if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in change password for username ::::" + userName + " password: " + password + " , oldPassword " + oldPassword, e);

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in change password for username ::::" + userName + " password: " + password + " , oldPassword " + oldPassword, e);
			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in  change password for username ::::" + userName, e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
		jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		String userName = null;
		String password = null;
		long forgotPasswordId = 0;
		long userId = 0;
		String verificationCode = null;
		boolean isError = false;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}

			if (null != requestMap.get(UserParameters.PASSWORD)) {
				password = requestMap.get(UserParameters.PASSWORD);
				password = EncryptionFactory.getEncrypter(EncryptionAlgoEnum.MD5).encrypt(password);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.PASSWORD_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.PASSWORD_MISSING.getErrorMessage());
			}
			if (null != requestMap.get(UserParameters.FORGOT_PASSWORD_ID)) {
				forgotPasswordId = Long.parseLong(requestMap.get(UserParameters.FORGOT_PASSWORD_ID));

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.FORGOT_PASSWORD_ID_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.FORGOT_PASSWORD_ID_MISSING.getErrorMessage());
			}
			if (null != requestMap.get(UserParameters.USER_ID)) {
				userId = Long.parseLong(requestMap.get(UserParameters.USER_ID));

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERID_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERID_MISSING.getErrorMessage());
			}
			if (null != requestMap.get(UserParameters.VERIFCIATION_CODE)) {
				verificationCode = requestMap.get(UserParameters.VERIFCIATION_CODE);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.VERIFICTION_CODE_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.VERIFICTION_CODE_MISSING.getErrorMessage());
			}
			if (!isError) {
				authenticationService.changePassword(userId, forgotPasswordId, verificationCode, userName, password);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
			}

		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			if (ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorCode());
				logger.info("Link expired. Exception in change password for username ::::" + userName + " password: " + password, e);

			} else if (ErrorCodesEnum.INVALID_PASSWORD_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.INVALID_PASSWORD_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.INVALID_PASSWORD_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorCode());
				logger.info("Link expired. Exception in change password for username ::::" + userName + " password: " + password, e);

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Unexpected error in change password for username ::::" + userName + " password: " + password, e);
			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Unexpected error in  change password for username ::::" + userName, e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}
		jsonData = gson.toJson(dataMap);

		if (isError) {
			modelAndView.setViewName(VoizdWebConstant.PASSWORD_CHANGE_FAILURE_VIEW_NAME);
		} else {
			modelAndView.setViewName(VoizdWebConstant.PASSWORD_CHANGE_SUCCESS_VIEW_NAME);
		}
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/verifyEmail/{id}/{emailId}/{code}", method = RequestMethod.GET)
	public ModelAndView verifyEmailAddress(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@PathVariable("id") long verificationId, @PathVariable("emailId") String emailId, @PathVariable("code") String code) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		boolean isError = false;
		try {
			authenticationService.verifyEmailAddress(verificationId, emailId, code);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);

		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			if (ErrorCodesEnum.INVALID_EMAIL_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.INVALID_EMAIL_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.INVALID_EMAIL_VERIFICATION_CREDENTIALS_EXCEPTION.getErrorCode());
				logger.info("Exception in email verification : error in verifying user details : emailId : " + emailId + " id " + verificationId);

			} else if (ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.LINK_EXPIRED_EXCEPTION.getErrorCode());
				logger.info("Link expired. Exception in email verification : error in verifying user details : emailId : " + emailId + " id " + verificationId);

			}

			else {

				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Exception in email verification : error in verifying user details : emailId : " + emailId + " id " + verificationId, e);
			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Exception in email verification : error in verifying user details : emailId : " + emailId + " id " + verificationId, e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			isError = true;
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}

		jsonData = gson.toJson(dataMap);
		if (isError) {
			modelAndView.setViewName(VoizdWebConstant.EMAIL_VERIFICATION_FAILURE_VIEW_NAME);
		} else {
			modelAndView.setViewName(VoizdWebConstant.EMAIL_VERIFICATION_SUCCESS_VIEW_NAME);
		}
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/resetPasswordRequest", method = RequestMethod.POST)
	public ModelAndView resetPasswordRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		String userName = null;
		boolean isError = false;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}
			authenticationService.resetPassword(userName);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
			dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.FORGET_PASSWORD_SUCCESS.getSuccessMessage());
			dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.FORGET_PASSWORD_SUCCESS.getSuccessCode());
		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			 if (ErrorCodesEnum.USER_IS_BLOCKED.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_IS_BLOCKED.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_IS_BLOCKED.getErrorCode());
				logger.info("Exception in authentication : user is blocked : username : " + userName);

			}else if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				
			}else{
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
			logger.error("Exception in request for change password request : userName : " + userName + " error " + e.getMessage(), e);

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Exception in request for change password request : userName : " + userName + " error " + e.getMessage(), e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			isError = true;
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}

		jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/initiateEmailVerification", method = RequestMethod.POST)
	public ModelAndView initiateEmailVerification(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		String userName = null;
		boolean isError = false;
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			if (null != requestMap.get(UserParameters.USER_NAME)) {
				userName = requestMap.get(UserParameters.USER_NAME);

			} else {
				isError = true;
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USERNAME_MISSING.getErrorCode());
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USERNAME_MISSING.getErrorMessage());
			}
			authenticationService.initiateEmailVerification(userName);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);

		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			if (ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())) {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.USER_NOT_FOUND_EXCEPTION.getErrorCode());
				logger.info("Exception in initiateEmailVerification : Invalid username : username : " + userName);

			} else {
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
				logger.error("Exception in initiateEmailVerification request : userName : " + userName + " error " + e.getMessage(), e);
			}

		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Exception in initiateEmailVerification request : userName : " + userName + " error " + e.getMessage(), e);
		}

		if (isError) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		}

		if (dataMap.size() == 0) {
			isError = true;
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}

		jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		logger.debug("AuthenticationController.jsonData=" + jsonData);
		return modelAndView;
	}
}
