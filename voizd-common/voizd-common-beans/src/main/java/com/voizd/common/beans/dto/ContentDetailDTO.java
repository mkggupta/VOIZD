package com.voizd.common.beans.dto;

import java.util.List;

public class ContentDetailDTO extends ContentDTO{
	
	 List<CommentInfoDTO> comment;
	 List<AmplifierDTO> amplify;
	 List<FollowerDTO> follower;
	 
	public List<FollowerDTO> getFollower() {
		return follower;
	}

	public void setFollower(List<FollowerDTO> follower) {
		this.follower = follower;
	}

	public List<AmplifierDTO> getAmplify() {
		return amplify;
	}

	public void setAmplify(List<AmplifierDTO> amplify) {
		this.amplify = amplify;
	}

	public List<CommentInfoDTO> getComment() {
		return comment;
	}

	public void setComment(List<CommentInfoDTO> comment) {
		this.comment = comment;
	}

}
