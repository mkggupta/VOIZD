package com.voizd.search.utils;

public enum Constants {
	
	URL_PROTOCOL_PREFIX("http://"),
	HEAD("HEAD"),
	voizd("/voizd"),
	ST("/st"),
	CT("/ct"),
	COUNT("count"),
	ORDERBY("orderby"),
	INDEX("INDEX"),
	BULK("BULK"),
	STATAION_ID_PREFIX("/ST_"),
	CONTENT_ID_PREFIX("/CT_"),
	ok("ok"),	
	found("found"),	
	exists("exists"),
	DELETE_INDEX("DELETE_INDEX"),
	acknowledged("acknowledged"),
	POST("POST"),
	GET("GET"),
	PUT("PUT"),
	DELETE("DELETE"),
	UPDATE("UPDATE"),
	UTF8("UTF-8"),
	SEARCH("/_search"),
	STATION("/station"),
	CONTENT("/content"),
	HOST_PORT_SEPARATOR(":");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	private Constants(String value){
		this.value = value;
	}
}
