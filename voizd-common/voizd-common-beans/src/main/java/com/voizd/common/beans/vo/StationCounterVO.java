package com.voizd.common.beans.vo;

public class StationCounterVO {
	 private Long stationId;
	 private Long view;
	 private Long likes;
	 private Long dislikes;
	 private Long follower;
	 private Long comments;
	 private Long numberOfContent;
	 private Long share;
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getView() {
		return view;
	}
	public void setView(Long view) {
		this.view = view;
	}
	
	public Long getFollower() {
		return follower;
	}
	public void setFollower(Long follower) {
		this.follower = follower;
	}
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	public Long getNumberOfContent() {
		return numberOfContent;
	}
	public void setNumberOfContent(Long numberOfContent) {
		this.numberOfContent = numberOfContent;
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
	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
}
