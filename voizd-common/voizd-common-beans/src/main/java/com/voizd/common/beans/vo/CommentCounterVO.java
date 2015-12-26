/**
 * 
 */
package com.voizd.common.beans.vo;

/**
 * @author manish
 *
 */
public class CommentCounterVO {
	 public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
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
	public Long getAmplify() {
		return amplify;
	}
	public void setAmplify(Long amplify) {
		this.amplify = amplify;
	}
	 public Long getCommentId() {
			return commentId;
	}
	public void setCommentId(Long commentId) {
			this.commentId = commentId;
	}
	 
	 private Long commentId;
	 private Long contentId;
	 private Long view;
	 private Long likes;
	 private Long dislikes;
	 private Long follower;
	 private Long comments;
	 private Long share; 
	 private Long amplify;
}
