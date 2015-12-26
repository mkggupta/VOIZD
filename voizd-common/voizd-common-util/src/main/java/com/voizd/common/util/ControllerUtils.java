package com.voizd.common.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.constant.VoizdConstant;

public class ControllerUtils {
	private static Logger logger = LoggerFactory.getLogger(ControllerUtils.class);
	private static final String TYPE = "type";
	private static final String CAT = "cat";
	private static final String DATA = "data";
	private static final String USER = "user";
	private static final String STATION = "station";
	private static final String CONTENT = "content";
	private static final String STREAM = "stream";
	private static final String USER_NAME = "usrName";
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getRequestMap(HttpServletRequest httpServletRequest) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		Enumeration<String> e = httpServletRequest.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			String value = httpServletRequest.getParameter(key);
			if (StringUtils.isNotBlank(value)) {
				requestMap.put(key, value.trim());
			}
		}
		logger.debug("getRequestMap=" + requestMap);
		return requestMap;
	}

	public static HashMap<String, String> getRequestMapFromMultipart(HttpServletRequest httpServletRequest) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
		if (isMultipart) {

			ServletFileUpload upload = new ServletFileUpload();

			try {
				FileItemIterator iter = upload.getItemIterator(httpServletRequest);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String fieldName = item.getFieldName();
					logger.debug("fieldName: {}", fieldName);
					InputStream stream = item.openStream();
					String fieldValue = null;
					if (item.isFormField()) {
						fieldValue = Streams.asString(stream);
						logger.debug("fieldValue: {}", fieldValue);
						if(USER_NAME.equalsIgnoreCase(fieldName)){
							requestMap.put(fieldName, fieldValue.trim());
						}else if (StringUtils.isNotBlank(fieldValue)) {
							requestMap.put(fieldName, fieldValue.trim());
						}

					} else {
						logger.error(" field " + fieldName + " with  name " + item.getName() + " detected.");
					}
					stream.close();
				}
			} catch (Exception e) {
				/*
				 * logger.error(e.getClass().getName() + " exception occurred while parsing data for submission. Reason: " + e.getLocalizedMessage(), e);
				 */
			}

		} else {
			requestMap = getRequestMap(httpServletRequest);
		}
		httpServletRequest.setAttribute(VoizdConstant.voizd_REQ_PARAMS_MAP, requestMap);
		logger.debug("getRequestMap=" + requestMap);
		return requestMap;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseHeader(HttpServletRequest httpServletRequest) {
		Map<String, String> requestParamMap = new HashMap<String, String>();
		Enumeration<Object> headerNames = httpServletRequest.getHeaderNames();
		logger.debug("headerNames=" + headerNames);
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			requestParamMap.put(headerName, httpServletRequest.getHeader(headerName));
		}
		logger.debug("parseHeaderRequestMap=" + requestParamMap);
		return requestParamMap;
	}

	public static Long extractUserIdFromHeader(HttpServletRequest httpServletRequest) {
		Long userId = (httpServletRequest.getHeader(VoizdConstant.USERID.toLowerCase()) != null) ? Long.parseLong(httpServletRequest.getHeader(
				VoizdConstant.USERID.toLowerCase()).toString()) : 0l;
		logger.debug("extractUserIdFromHeader userId =" + userId);
		if (userId == null || userId == 0) {
			userId = (httpServletRequest.getParameter(VoizdConstant.USERID.toLowerCase()) != null) ? Long.parseLong(httpServletRequest.getParameter(
					VoizdConstant.USERID.toLowerCase()).toString()) : 0l;
			logger.debug("extractUserIdFromHeader final userId =" + userId);
		}
		
		return userId;
	}

	public static String extractIMEIfromClientProperty(String clientProperty) {
		String imeai = null;
		if (null != clientProperty) {
			String clientPropertyArr[] = clientProperty.split("::");
			for (String string : clientPropertyArr) {
				if (string.startsWith("IMEI##")) {
					return string.split("##")[1];
				}
			}
		}
		return imeai;
	}

	public static Map<String, String> extractAuthMap(String authParams) {
		Map<String, String> authMap = new HashMap<String, String>();
		if (null != authParams) {
			String[] authParamsArr = authParams.split(VoizdConstant.voizd_CLIENT_PARAM_SEPERATOR);
			String[] paramsArr;
			for (String params : authParamsArr) {
				paramsArr = params.split(VoizdConstant.voizd_CLIENT_KEY_SEPERATOR);
				if (paramsArr.length == 2) {
					authMap.put(paramsArr[0], paramsArr[1]);
				}
			}
		}
		return authMap;
	}

	public static String extractClientVersionfromClientProperty(String clientProperty) {
		if (null != clientProperty) {
			String clientPropertyArr[] = clientProperty.split("::");
			for (String string : clientPropertyArr) {
				if (string.startsWith("CLIENT_VERSION##")) {
					return string.split("##")[1];
				}
			}
		}
		return null;
	}

	public static String extractPlatformfromClientProperty(String clientProperty) {
		if (null != clientProperty) {
			String clientPropertyArr[] = clientProperty.split("::");
			for (String string : clientPropertyArr) {
				if (string.startsWith("PLATFORM##")) {
					return string.split("##")[1];
				}
			}
		}
		return null;
	}
	
	public static HashMap<String, Object> getMediaRequestMapFromMultipart(HttpServletRequest httpServletRequest) {
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
		if (isMultipart) {
			ServletFileUpload upload = new ServletFileUpload();
			try {
				FileItemIterator iter = upload.getItemIterator(httpServletRequest);

				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String fieldName = item.getFieldName();
					InputStream stream = item.openStream();

					logger.debug("fieldName :: " + fieldName);
					
					if (TYPE.equalsIgnoreCase(fieldName)) {
						String fileType = Streams.asString(stream);
						requestMap.put(fieldName, fileType.trim());
					} else if (CAT.equalsIgnoreCase(fieldName)) {
						String category = Streams.asString(stream);
						requestMap.put(fieldName, category.trim());
					} else if (DATA.equalsIgnoreCase(fieldName)) {
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						Streams.copy(stream, byteArrayOutputStream, true);
						byte mediaData[] = byteArrayOutputStream.toByteArray();
						requestMap.put(fieldName, mediaData);
					}
					stream.close();
				}
			} catch (Exception e) {
				logger.error(e.getClass().getName() + " exception occurred while parsing data for submission. Reason: " + e.getLocalizedMessage());
			}
		}
		return requestMap;
	}
	
	public static String getMediaCategory(String catInfo) {
		String category=null;
		if(USER.equalsIgnoreCase(catInfo)){
			category = VoizdCategory.USER.getCategory();
		}else if(STATION.equalsIgnoreCase(catInfo)){
			category = VoizdCategory.STATION.getCategory();
		}else if(CONTENT.equalsIgnoreCase(catInfo)){
			category = VoizdCategory.CONTENT.getCategory();
		}else if(STREAM.equalsIgnoreCase(catInfo)){
			category = VoizdCategory.STREAM.getCategory();
		}else{
			category = VoizdCategory.DEFAULT.getCategory();
		}
		return category;
	}
	
	public static String extractUserNameFromHeader(HttpServletRequest httpServletRequest) {
		String userName = null;
		if (httpServletRequest.getHeader(VoizdConstant.USER_NAME) != null) {
			userName = httpServletRequest.getHeader(VoizdConstant.USER_NAME);
		}
		return userName;
	}
}
