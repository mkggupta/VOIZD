package com.voizd.common.util;

import com.voizd.common.util.resource.ResourceUtils;

public class LocationUtil {
	private static String fileLocation = null;
	
	public static String getFileLocation(){
		try {
			fileLocation =ResourceUtils.getSystemConfigProperty("ip_to_location_db_path");
		} catch (Exception e) {
			System.err.println("voizd.properties does not have key ip_to_location_db_path");
		}
		
		return fileLocation ;
	}
}
