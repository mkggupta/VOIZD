/**
 * 
 */
package com.voizd.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.authentication.exception.AuthenticationServiceFailedException;

/**
 * @author Manish
 * 
 */
public class AuthenticationInterceptor extends BaseAuthenticationInterceptor {
	Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, String> headerMap = ControllerUtils.parseHeader(request);
		Map<String, String> authMap = ControllerUtils.extractAuthMap(headerMap.get(VoizdWebConstant.voizd_AUTHETICATION_PROPERTY));
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		int loginMode = 0;

		boolean isError = false;
		try {
			logger.debug("preHandle  user authMap : "+authMap);
			String userName = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_USER_NAME_PARAM);
			String password = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_PASSWORD_PARAM);
			String authkey = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_AUTHKEY_PARAM);
			String loginModeStr = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_LOGIN_MODE_PARAM);
			String partnerUserKey = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_PARTNER_USER_KEY_PARAM);
			String appId = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_APP_ID_PARAM);
			Map<String, Object> clientParamMap = ClientHeaderUtil.extractClientParam(request);
			String currentClientVersion = (String) clientParamMap.get(ClientParamConstant.CLIENT_VERSION);
			String currentPlatform = (String) clientParamMap.get(ClientParamConstant.PLATFORM);

			if (null != loginModeStr) {
				loginMode = Integer.parseInt(loginModeStr);
			}
			if (!VoizdWebConstant.voizd_AUTHETICATION_AUTHKEY.equalsIgnoreCase(authkey)) {
				isError = true;
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.INVALID_AUTH_KEY_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.INVALID_AUTH_KEY_EXCEPTION.getErrorCode());

			} else {
				logger.debug("preHandle :userName : "+userName+" , loginMode :"+loginMode+" , partnerUserKey :"+partnerUserKey+",appId : "+appId);
				if (loginMode > 0) {
					authenticationService.authenticateUser(userName, loginMode, partnerUserKey, appId, currentClientVersion, currentPlatform, false,
							clientParamMap,null);
				} else {
					authenticationService.authenticateUser(userName, password, currentClientVersion, currentPlatform, false, clientParamMap,null);
				}
			}
		} catch (AuthenticationServiceFailedException e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.getErrorCodesEnum(e.getErrorCode()).getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, e.getErrorCode());
			logger.error("Exception in authenticating user ");
		} catch (Exception e) {
			isError = true;
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.AUTHENTICATION_SERVICE_FAILED_EXCEPTION.getErrorCode());
			logger.error("Exception in authenticating user ");
		}
		if (isError) {
			handleError(authMap, dataMap, request, response, headerMap);
			return false;
		}
		return super.preHandle(request, response, handler);
	}

}
