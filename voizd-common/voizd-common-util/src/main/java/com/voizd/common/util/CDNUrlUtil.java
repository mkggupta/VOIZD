package com.voizd.common.util;

import com.voizd.common.util.resource.ResourceUtils;



public class CDNUrlUtil {
	private static String imageUrl = null;
	private static String audioUrl = null;
	private static String videoUrl = null;
	private static String SEPERATOR = "/";
	private static final String DOT=".";
	private static final String GET="get/";
	static {
		try {
			imageUrl =ResourceUtils.getSystemConfigProperty("cdn_image_url");
			audioUrl =ResourceUtils.getSystemConfigProperty("cdn_audio_url");
			videoUrl =ResourceUtils.getSystemConfigProperty("cdn_video_url");
			
		} catch (Exception e) {
			System.err.println("voizd.properties does not have key cdn_image_url,cdn_audio_url,cdn_video_url");
		
		}
	}
	
	public static String getCdnUrl(String urlType){
		
		if("image".equalsIgnoreCase(urlType)){
			return imageUrl;
		}else if("audio".equalsIgnoreCase(urlType)){
			return audioUrl;
		}else if("video".equalsIgnoreCase(urlType)){
			return videoUrl;
		}else{
			return null ;
		}

	}
	
	public static String getCdnContentUrl(String mediaType,String fileId, String ext,boolean isStream ){
		StringBuilder contentUrl = new StringBuilder();
		if("image".equalsIgnoreCase(mediaType)){
			contentUrl.append(imageUrl).append(GET).append(fileId).append(DOT).append(ext);
		}else if("audio".equalsIgnoreCase(mediaType)){
			if(isStream){
				contentUrl.append(audioUrl).append(GET).append(fileId).append(DOT).append(ext);
			}else{
				contentUrl.append(audioUrl).append(GET).append(fileId).append(DOT).append(ext);
			}
		}else if("video".equalsIgnoreCase(mediaType)){
			if(isStream){
				contentUrl.append(videoUrl).append(GET).append(fileId).append(DOT).append(ext);
			}else{
				contentUrl.append(videoUrl).append(GET).append(fileId).append(DOT).append(ext);
			}
		}
		return contentUrl.toString();

	}
	
	
	public static String getCdnContentUrl(String mediaType, String url,boolean isStream ){
		StringBuilder contentUrl = new StringBuilder();
		if("image".equalsIgnoreCase(mediaType)){
			contentUrl.append(imageUrl).append(url);
		}else if("audio".equalsIgnoreCase(mediaType)){
			if(isStream){
				contentUrl.append(audioUrl).append(url);
			}else{
				contentUrl.append(audioUrl).append(url);
			}
		}else if("video".equalsIgnoreCase(mediaType)){
			if(isStream){
				contentUrl.append(videoUrl).append(url);
			}else{
				contentUrl.append(videoUrl).append(url);
			}
		}
		return contentUrl.toString();

	}

}
