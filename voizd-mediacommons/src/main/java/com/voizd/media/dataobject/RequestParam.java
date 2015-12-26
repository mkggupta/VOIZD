package com.voizd.media.dataobject;

/**
 * User: shalabh Date: 23/7/12 Time: 2:39 PM
 */
public enum RequestParam {
	WIDTH("width", "w", true), HEIGHT("height", "h", true), BITRATE("bitr",
			"b", true), CODEC("codec", "c", true), MEDIATYPE("mtype", "mt",
			false), MEDIACATEGORY("mcategory", "mc", false), ORIGINAL_FILE_ID(
			"originalFileId", "ofid", false), REQUESTED_EXT("requestedExt",
			"rext", false),START_SEC("startsec", "s", true), DURATION_SEC("durationsec", "d", true), FILE_ID1("fileId1", "fid1", false), FILE_ID2(
			"fileId2", "fid2", false), RESPONSE_TYPE("rtype", "rtype", false), MAX_CONVERT_SIZE("maxsize", "maxsize", false), OWNERKEY("ownerkey", "ownerkey", false);

	private String name;

	private String code;

	private boolean cachable;

	private RequestParam(String name, String code, boolean cachable) {
		this.name = name;
		this.code = code;
		this.cachable = cachable;
	}
	
	

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public boolean isCachable() {
		return cachable;
	}
}
