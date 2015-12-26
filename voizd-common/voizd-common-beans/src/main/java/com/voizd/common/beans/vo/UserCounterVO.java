package com.voizd.common.beans.vo;

public class UserCounterVO {
	 public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Long getFollow() {
		return follow;
	}
	public void setFollow(Long follow) {
		this.follow = follow;
	}
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
	public Long getAmplified() {
		return amplified;
	}
	public void setAmplified(Long amplified) {
		this.amplified = amplified;
	}
	 private Long userId;
	 private Long view;
	 private Long likes;
	 private Long follow;
	 private Long comments;
	 private Long share;
	 private Long amplified;

}
