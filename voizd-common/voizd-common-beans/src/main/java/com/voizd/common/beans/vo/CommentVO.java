/**
 * 
 */
package com.voizd.common.beans.vo;


/**
 * @author manish
 *
 */
public class CommentVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public Long getCommenterId() {
		return commenterId;
	}
	public void setCommenterId(Long commenterId) {
		this.commenterId = commenterId;
	}
	public String getFileIds() {
		return fileIds;
	}
	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
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
	public String getAppIds() {
		return appIds;
	}
	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTagCloud() {
		return tagCloud;
	}
	public void setTagCloud(String tagCloud) {
		this.tagCloud = tagCloud;
	}
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	
	private String country;
	private String state;
	private String city;
	private String location;
	private String address;
	private String tagCloud;
	private String commentText;
	private Long commenterId;
	private String fileIds;
	private float latitude;
	private float longitude;
	private String appIds;
	private String language;
	private Long contentId;
	private Long commentId;
	private String weblink;

	
}
