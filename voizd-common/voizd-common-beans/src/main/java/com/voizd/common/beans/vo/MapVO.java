package com.voizd.common.beans.vo;

public class MapVO {

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public long getRang() {
		return rang;
	}
	public void setRang(long rang) {
		this.rang = rang;
	}
	
	  public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	  public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	  private float latitude;
	  private float longitude;
	  private String country;
	  private long rang;
	  private int zoom;
	  private String tag;
	  private int limit; 
}
