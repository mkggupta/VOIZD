package com.voizd.interceptor;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.voizd.appstats.core.service.request.v1_0.RequestLogService;
import com.voizd.common.beans.vo.RequestLogVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.util.ControllerUtils;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.authentication.v1_0.AuthenticationService;

public abstract class BaseAuthenticationInterceptor extends HandlerInterceptorAdapter {
	protected AuthenticationService authenticationService;
	protected RequestLogService requestLogService;
	Logger logger = LoggerFactory.getLogger(BaseAuthenticationInterceptor.class);

	/**
	 * @return the requestLogService
	 */
	public RequestLogService getRequestLogService() {
		return requestLogService;
	}

	/**
	 * @param requestLogService
	 *            the requestLogService to set
	 */
	public void setRequestLogService(RequestLogService requestLogService) {
		this.requestLogService = requestLogService;
	}

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

	protected void logAuthFailureRequest(Map<String, String> headerMap, HttpServletRequest request, String responseString, String requestType, String imei) {
		try {

			@SuppressWarnings("unchecked")
			HashMap<String, String> requestMap = (HashMap<String, String>) request.getAttribute(VoizdConstant.voizd_REQ_PARAMS_MAP);
			Long userId = ControllerUtils.extractUserIdFromHeader(request);
			if (null == userId) {
				userId = -1l;
			}
			String ip = request.getRemoteAddr();
			int status = 0;

			requestLogService.logRequest(new RequestLogVO(converMapToString(headerMap), request.getRequestURI() + "?" + converMapToString(requestMap),
					responseString, requestType, userId, imei, new Date(), ip, status));
		} catch (Exception e) {
			logger.error("Exception in logging request details ", e);
		}
	}

	protected String converMapToString(Map<String, String> valueMap) {
		String returnString = "";
		if (null != valueMap) {
			Set<String> keySet = valueMap.keySet();
			for (String key : keySet) {
				returnString += key + "#~" + valueMap.get(key) + "#;#";
			}
		}
		return returnString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	protected void handleError(Map<String, String> authMap, HashMap<String, Object> dataMap, HttpServletRequest request, HttpServletResponse response,
			Map<String, String> headerMap) throws IOException {
		String requestType = headerMap.get(VoizdWebConstant.REQUEST_TYPE);
		String imei = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_IMEI_PARAM);
		if (null == requestType) {
			requestType = "-1";
		}
		dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		dataMap.put(VoizdWebConstant.REQUEST_TYPE, requestType);
		Gson gson = new Gson();
		String jsonData = null;
		jsonData = gson.toJson(dataMap);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().print(jsonData);
		response.setHeader("Content-Type", "application/json");
		logAuthFailureRequest(headerMap, request, jsonData, requestType, imei);
	}
}
