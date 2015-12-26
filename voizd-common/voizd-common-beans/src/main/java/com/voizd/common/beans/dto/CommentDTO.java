package com.voizd.common.beans.dto;

import java.util.List;

public class CommentDTO {
	public Long getcId() {
		return cId;
	}
	public void setcId(Long cId) {
		this.cId = cId;
	}
	public Long getCntId() {
		return cntId;
	}
	public void setCntId(Long cntId) {
		this.cntId = cntId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getcUrl() {
		return cUrl;
	}
	public void setcUrl(String cUrl) {
		this.cUrl = cUrl;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public Long getcDuration() {
		return cDuration;
	}
	public void setcDuration(Long cDuration) {
		this.cDuration = cDuration;
	}
	public String getcTitle() {
		return cTitle;
	}
	public void setcTitle(String cTitle) {
		this.cTitle = cTitle;
	}
	public String getcDate() {
		return cDate;
	}
	public void setcDate(String cDate) {
		this.cDate = cDate;
	}
	public Long getcView() {
		return cView;
	}
	public void setcView(Long cView) {
		this.cView = cView;
	}
	public Long getcLikes() {
		return cLikes;
	}
	public void setcLikes(Long cLikes) {
		this.cLikes = cLikes;
	}
	public Long getcDislikes() {
		return cDislikes;
	}
	public void setcDislikes(Long cDislikes) {
		this.cDislikes = cDislikes;
	}
	public Long getcFollower() {
		return cFollower;
	}
	public void setcFollower(Long cFollower) {
		this.cFollower = cFollower;
	}
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	public Long getcShare() {
		return cShare;
	}
	public void setcShare(Long cShare) {
		this.cShare = cShare;
	}
	public String getcImgUrl() {
		return cImgUrl;
	}
	public void setcImgUrl(String cImgUrl) {
		this.cImgUrl = cImgUrl;
	}
	public List<CommentInfoDTO> getComment() {
		return comment;
	}
	public void setComment(List<CommentInfoDTO> comment) {
		this.comment = comment;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	
	private String next;
	private Long cId;
	private Long cntId;
	private String cName;
	private String cUrl;
	private String profileUrl;
	private Long cDuration;
	private String cTitle;
	private String cDate;
	private Long cView;
	private Long cLikes;
	private Long cDislikes;
	private Long cFollower;
	private Long comments;
	private Long cShare;
	private String cImgUrl;
	private String cAudUrl;
	public String getcAudUrl() {
		return cAudUrl;
	}
	public void setcAudUrl(String cAudUrl) {
		this.cAudUrl = cAudUrl;
	}
	 public Byte getcTapValue() {
		return cTapValue;
	}
	public void setcTapValue(Byte cTapValue) {
		this.cTapValue = cTapValue;
	}
	public String getcLikeUrl() {
		return cLikeUrl;
	}
	public void setcLikeUrl(String cLikeUrl) {
		this.cLikeUrl = cLikeUrl;
	}
	public Byte getcLikeValue() {
		return cLikeValue;
	}
	public void setcLikeValue(Byte cLikeValue) {
		this.cLikeValue = cLikeValue;
	}

	private Byte cTapValue;
	 private String cLikeUrl;
	 private Byte cLikeValue;
	 public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}

	private String weblink;
	 public String getAmplifyUrl() {
		return amplifyUrl;
	}
	public void setAmplifyUrl(String amplifyUrl) {
		this.amplifyUrl = amplifyUrl;
	}

	private String amplifyUrl;
	 public String getcShareUrl() {
		return cShareUrl;
	}
	public void setcShareUrl(String cShareUrl) {
		this.cShareUrl = cShareUrl;
	}

	private String cShareUrl;
	List<CommentInfoDTO> comment;
	
	
}
