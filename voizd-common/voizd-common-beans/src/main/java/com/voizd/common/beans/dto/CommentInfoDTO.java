package com.voizd.common.beans.dto;

public class CommentInfoDTO {
	
	public Long getCmtId() {
		return cmtId;
	}
	public void setCmtId(Long cmtId) {
		this.cmtId = cmtId;
	}
	public Long getCmtCntId() {
		return cmtCntId;
	}
	public void setCmtCntId(Long cmtCntId) {
		this.cmtCntId = cmtCntId;
	}
	public String getCmtDate() {
		return cmtDate;
	}
	public void setCmtDate(String cmtDate) {
		this.cmtDate = cmtDate;
	}
	public Long getCmtView() {
		return cmtView;
	}
	public void setCmtView(Long cmtView) {
		this.cmtView = cmtView;
	}
	public Long getCmtLikes() {
		return cmtLikes;
	}
	public void setCmtLikes(Long cmtLikes) {
		this.cmtLikes = cmtLikes;
	}
	public Long getCmtShare() {
		return cmtShare;
	}
	public void setCmtShare(Long cmtShare) {
		this.cmtShare = cmtShare;
	}
	public String getCmtTitle() {
		return cmtTitle;
	}
	public void setCmtTitle(String cmtTitle) {
		this.cmtTitle = cmtTitle;
	}
	public String getCommenterName() {
		return commenterName;
	}
	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}
	public Long getCmtDuration() {
		return cmtDuration;
	}
	public void setCmtDuration(Long cmtDuration) {
		this.cmtDuration = cmtDuration;
	}
	public String getCmtCntUrl() {
		return cmtCntUrl;
	}
	public void setCmtCntUrl(String cmtCntUrl) {
		this.cmtCntUrl = cmtCntUrl;
	}
	public String getCmtprofileUrl() {
		return cmtprofileUrl;
	}
	public void setCmtprofileUrl(String cmtprofileUrl) {
		this.cmtprofileUrl = cmtprofileUrl;
	}
	private Long cmtId;
	private Long cmtCntId;
	private String cmtDate;
	private Long cmtView;
	private Long cmtLikes;
	private Long cmtShare;
	private String cmtTitle;
	private String commenterName;
	private Long cmtDuration;
	private String cmtCntUrl;
	private String cmtprofileUrl;
	private String commenterProImgUrl;
	public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	private String weblink;
	
	public String getCommenterProImgUrl() {
		return commenterProImgUrl;
	}
	public void setCommenterProImgUrl(String commenterProImgUrl) {
		this.commenterProImgUrl = commenterProImgUrl;
	}
}
