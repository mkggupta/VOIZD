/**
 * 
 */
package com.voizd.interceptor;

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

import com.voizd.appstats.core.service.request.v1_0.RequestLogService;
import com.voizd.common.beans.vo.RequestLogVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.util.ControllerUtils;
import com.voizd.rest.constant.VoizdWebConstant;

/**
 * @author Manish
 * 
 */
public class RequestLoggerInterceptor extends HandlerInterceptorAdapter {
	Logger logger = LoggerFactory.getLogger(RequestLoggerInterceptor.class);
	private RequestLogService requestLogService;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		try {
			Map<String, String> headerMap = ControllerUtils.parseHeader(request);
			String requestType = headerMap.get(VoizdWebConstant.REQUEST_TYPE);
			if (null == requestType) {
				requestType = "-1";
			}
			HashMap<String, String> requestMap = (HashMap<String, String>) request.getAttribute(VoizdConstant.voizd_REQ_PARAMS_MAP);
			String responseData = modelAndView.getModel().get(VoizdWebConstant.RESPONSE).toString();
			String imei = ControllerUtils.extractIMEIfromClientProperty(headerMap.get(VoizdConstant.voizd_CLIENT_PROPERTY));
			Long userId = ControllerUtils.extractUserIdFromHeader(request);
			if (null == userId) {
				userId = -1l;
			}
			modelAndView.addObject(VoizdWebConstant.REQUEST_TYPE, requestType);
			String ip = request.getRemoteAddr();
			int status = 1;
			if (responseData.contains("\"status\":\"error\"")) {
				status = 0;
			}
			if (responseData.startsWith("{")) {
				responseData = responseData.replaceFirst("\\{", "{\"requesttype\":\"" + requestType + "\",");
				modelAndView.addObject(VoizdWebConstant.RESPONSE, responseData);
			}

			requestLogService.logRequest(new RequestLogVO(converMapToString(headerMap), request.getRequestURI() + "?" + converMapToString(requestMap),
					responseData, requestType, userId, imei, new Date(), ip, status));
		} catch (Exception e) {
			logger.error("Exception in logging request details ", e);
		}
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

	private String converMapToString(Map<String, String> valueMap) {
		String returnString = "";
		if (null != valueMap) {
			Set<String> keySet = valueMap.keySet();
			for (String key : keySet) {
				returnString += key + "#~" + valueMap.get(key) + "#;#";
			}
		}
		return returnString;
	}

}
