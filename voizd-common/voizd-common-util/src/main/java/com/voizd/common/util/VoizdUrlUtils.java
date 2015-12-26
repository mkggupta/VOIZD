package com.voizd.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.voizd.common.util.resource.ResourceUtils;

public class VoizdUrlUtils {
	private static String apiServerUrl = null;
	private static String webServerUrl = null;
	private static String SEPERATOR = "/";
	private static String PARAM_SEPERATOR = "-";
	private static String STATION = "station/";
	private static String STREAM = "stream/";
	private static String CLIP = "voizclip/";
	private static String BY = "by";

	
	
	static {
		try {
			apiServerUrl =ResourceUtils.getSystemConfigProperty("api_server_url");
			webServerUrl =ResourceUtils.getSystemConfigProperty("web_server_url");
		} catch (Exception e) {
			System.err.println("voizd.properties does not have key api_server_url");
		
		}
	}
	
	public static String getServerUrl(){
		return apiServerUrl;
	}
	
	public static String getWebServerUrl(){
		return webServerUrl;
	}
	 public static final String encodedUrl(String str) {
	        String encodedStr = str;
	        try {
	            encodedStr = URLEncoder.encode(str, "UTF-8");
	        } catch (UnsupportedEncodingException ex) {
	            // ignored
	        }
	        return encodedStr;
	    }
	
	public static String getAbsoluteUrl(String url, String relativePath){
		return url+relativePath;
	}
	
	public static String getAbsoluteEncodedUrl(String url, String relativePath){
		return encodedUrl(url+relativePath);
	}
	
	public static String getStationShareUrl(String stationName,Long sId,Long creatorId,Byte appId){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(sId).append(PARAM_SEPERATOR).append(creatorId).append(PARAM_SEPERATOR).append(appId);
		StringBuilder relativePath = new StringBuilder(stationName).append(SEPERATOR).append(convertStringToHex(stringBuilder.toString())) ;
		return (getWebServerUrl()+STATION+relativePath);
	}
	
	public static String getClipShareUrl(String creatorName,String clipName,Long stationId,Long contentId,Long creatorId,Byte appId){
		StringBuilder stringBuilder = new StringBuilder();
		creatorName =creatorName.replaceAll("\\s+", PARAM_SEPERATOR); 
		clipName = clipName.replace("?", "").replace("#", "").replace("'", "").replace(".", "").replace("%", "");
		clipName =clipName.replaceAll("\\s+", PARAM_SEPERATOR);
		stringBuilder.append(stationId).append(PARAM_SEPERATOR).append(creatorId).append(PARAM_SEPERATOR).append(contentId).append(PARAM_SEPERATOR).append(appId);
		StringBuilder relativePath = new StringBuilder(clipName).append(PARAM_SEPERATOR).append(BY).append(PARAM_SEPERATOR).append(creatorName).append(SEPERATOR).append(convertStringToHex(stringBuilder.toString())) ;
		return (getWebServerUrl()+CLIP+relativePath);
	}
	
	public static String getStreamShareUrl(String userName,Long userId,Byte appId){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(appId).append(PARAM_SEPERATOR).append(userId).append(PARAM_SEPERATOR).append(appId);
		StringBuilder relativePath = new StringBuilder(userName).append(SEPERATOR).append(convertStringToHex(stringBuilder.toString())) ;
		return (getWebServerUrl()+STREAM+relativePath);
	}
	 public static String convertStringToHex(String str){
		 
		  char[] chars = str.toCharArray();
		  StringBuffer hex = new StringBuffer();
		  for(int i = 0; i < chars.length; i++){
		    hex.append(Integer.toHexString((int)chars[i]));
		  }
		  return hex.toString();
	  }

}