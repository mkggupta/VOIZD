package com.voizd.common.beans.dto;

import java.util.Date;
import java.util.List;


public class StationDTO {
	 private Long sId;
	 private String sName;
	 private Date sDate;
	 private Long view;
	 private Long likes;
	 private Long dislikes;
	 private Long follower;
	 private Long sComments;
	 private Long contCnt;
	 private String desc;
	 private String sImgUrl;
	 private Long cId;
	 private String cName;
	 private String cUrl;
	 private String tapUrl;
	 private Byte tapValue;
	 private String likeUrl;
	 private Byte likeValue;
	 private Long share;
	 List<ContentDTO> content;
	 private String sDtlUrl;
	 private String tag;
	 private String location;
	 private String language;
	 private Byte status;
	 private String sUpdateUrl;
	 private Byte adult;
	 private String sShareUrl;
	 private String cNext;
	 private String cPre;
	public Long getSId() {
		return sId;
	}
	public void setSId(Long id) {
		sId = id;
	}
	public String getSName() {
		return sName;
	}
	public void setSName(String name) {
		sName = name;
	}
	
	public Long getView() {
		return view;
	}
	public void setView(Long view) {
		this.view = view;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getDislikes() {
		return dislikes;
	}
	public void setDislikes(Long dislikes) {
		this.dislikes = dislikes;
	}
	public Long getFollower() {
		return follower;
	}
	public void setFollower(Long follower) {
		this.follower = follower;
	}
	
	public Long getContCnt() {
		return contCnt;
	}
	public void setContCnt(Long contCnt) {
		this.contCnt = contCnt;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSImgUrl() {
		return sImgUrl;
	}
	public void setSImgUrl(String imgUrl) {
		sImgUrl = imgUrl;
	}
	public Long getCId() {
		return cId;
	}
	public void setCId(Long id) {
		cId = id;
	}
	public String getCName() {
		return cName;
	}
	public void setCName(String name) {
		cName = name;
	}
	public String getCUrl() {
		return cUrl;
	}
	public void setCUrl(String url) {
		cUrl = url;
	}
	public String getTapUrl() {
		return tapUrl;
	}
	public void setTapUrl(String tapUrl) {
		this.tapUrl = tapUrl;
	}
	public Byte getTapValue() {
		return tapValue;
	}
	public void setTapValue(Byte tapValue) {
		this.tapValue = tapValue;
	}
	public List<ContentDTO> getContent() {
		return content;
	}
	public void setContent(List<ContentDTO> content) {
		this.content = content;
	}
	public Date getSDate() {
		return sDate;
	}
	public void setSDate(Date date) {
		sDate = date;
	}
	public Long getSComments() {
		return sComments;
	}
	public void setSComments(Long comments) {
		sComments = comments;
	}
	public String getLikeUrl() {
		return likeUrl;
	}
	public void setLikeUrl(String likeUrl) {
		this.likeUrl = likeUrl;
	}
	public Byte getLikeValue() {
		return likeValue;
	}
	public void setLikeValue(Byte likeValue) {
		this.likeValue = likeValue;
	}
	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
	public String getSDtlUrl() {
		return sDtlUrl;
	}
	public void setSDtlUrl(String dtlUrl) {
		sDtlUrl = dtlUrl;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getSUpdateUrl() {
		return sUpdateUrl;
	}
	public void setSUpdateUrl(String updateUrl) {
		sUpdateUrl = updateUrl;
	}
	
	public Byte getAdult() {
		return adult;
	}
	public void setAdult(Byte adult) {
		this.adult = adult;
	}
	public String getSShareUrl() {
		return sShareUrl;
	}
	public void setSShareUrl(String shareUrl) {
		sShareUrl = shareUrl;
	}
	public String getCNext() {
		return cNext;
	}
	public void setCNext(String next) {
		cNext = next;
	}
	public String getCPre() {
		return cPre;
	}
	public void setCPre(String pre) {
		cPre = pre;
	}
	


	

}
