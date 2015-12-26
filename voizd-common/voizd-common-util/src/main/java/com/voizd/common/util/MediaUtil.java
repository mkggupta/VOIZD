package com.voizd.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.media.ImageSize;



public class MediaUtil {
	private static Logger logger = LoggerFactory.getLogger(MediaUtil.class);
	private static final Pattern HEIGHT_WIDTH_PAT = Pattern.compile(".*\\d+[xX]\\d+.*");
	public static final Pattern DIMENTION_PAT = Pattern.compile("(\\d+)[xX](\\d+)");

	//private static final String GIF="gif";
	//private static final  String ORIGINAL = "original";
	//private static final  String DEFAULT = "default";
	private static final String BASE="get/";
	//private static final String PREVIEW_BASE="/createthumbget/";
	//private static final String EXT="jpg";
	private static final String HIGHT="?height=";
	private static final String WIDTH="&width=";
	private static final String DOT=".";
	//private static final String ROOT="get/";
	private static final String IMAGE_WIDTH_HEIGHT_SEPARATOR = "x";
	private static final int IMAGE_WIDTH_INDEX = 0;
	private static final int IMAGE_HEIGHT_INDEX = 1;
	private static final int IMAGE_MIN_LENGTH = 50;
	private static final int IMAGE_MAX_LENGTH = 300;
	
	
	public static String getImageSize(String screenResolution){
		
		logger.debug("screenResolution[0]: " +screenResolution);
		if(StringUtils.isBlank(screenResolution)){
			screenResolution =ImageSize.O_130x130.getNewValue();
		}
		screenResolution = getImageDimension(screenResolution);
		
		return screenResolution;
	}
	
	public static String getThumbImageSize(String screenResolution){
		if(StringUtils.isBlank(screenResolution)){
			screenResolution =ImageSize.O_130x130.getNewValue();
		}
		screenResolution = getImageDimension(screenResolution);
		return screenResolution;
	}
	
	public static String getMediaContentUrl(String fileId, String ext,String screenResolution ){
		
		StringBuilder mediaUrl = new StringBuilder();
		mediaUrl.append(BASE).append(fileId).append(DOT).append(ext);
		if(StringUtils.isNotBlank(screenResolution)){
			String[] dimensionFields = screenResolution.split(IMAGE_WIDTH_HEIGHT_SEPARATOR);
			if(dimensionFields != null && dimensionFields.length == 2){
				mediaUrl.append(HIGHT).append(dimensionFields[IMAGE_HEIGHT_INDEX]).
				append(WIDTH).append(dimensionFields[IMAGE_WIDTH_INDEX]);	
			}
		}
		//logger.debug(" mediaUrl : "+mediaUrl);
		return mediaUrl.toString();
	}
	
	
	public static String getImageDimension(String inputDimension) {
		Matcher m = DIMENTION_PAT.matcher(inputDimension);
		if (m.matches()) {
			long width = Long.parseLong(m.group(1));
			long height = Long.parseLong(m.group(2));
			return new StringBuilder().append(getNormalizedDimension(width, IMAGE_MIN_LENGTH, IMAGE_MAX_LENGTH)).append("x").append(getNormalizedDimension(height, IMAGE_MIN_LENGTH, IMAGE_MAX_LENGTH)).toString();
		} else {
			return ImageSize.O_130x130.getNewValue();
		}
	}
	
	public static long getNormalizedDimension(long length, long minLength, long maxLength) {
		long normalizedLength = ((length + 25) / IMAGE_MIN_LENGTH) * IMAGE_MIN_LENGTH;
		if (normalizedLength < minLength) {
			return minLength;
		}
		if (normalizedLength > maxLength) {
			return maxLength;
		}
		return normalizedLength;
	}

}
