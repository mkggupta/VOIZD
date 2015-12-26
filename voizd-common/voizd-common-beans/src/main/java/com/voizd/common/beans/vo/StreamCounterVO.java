package com.voizd.common.beans.vo;

public class StreamCounterVO {
	 private Long streamId;
	 private Long view;
	 private Long likes;
	
	 private Long comments;
	 private Long numberOfContent;
	 private Long share;
	
	public Long getView() {
		return view;
	}
	public void setView(Long view) {
		this.view = view;
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

	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
	public Long getStreamId() {
		return streamId;
	}
	public void setStreamId(Long streamId) {
		this.streamId = streamId;
	}
}
