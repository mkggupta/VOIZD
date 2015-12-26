package com.voizd.service.comment.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.CommentVO;
import com.voizd.service.comment.bo.CommentBO;
import com.voizd.service.comment.exception.CommentServiceException;

public class CommentManagerImpl implements CommentManager {
	private CommentBO  commentBO ;
	public void setCommentBO(CommentBO commentBO) {
		this.commentBO = commentBO;
	}
	public HashMap<String, Object> createComment(CommentVO commentVO) throws CommentServiceException{
		return commentBO.createComment(commentVO);
	}
	@Override
	public HashMap<String, Object> getCommentList(Long contentId, int start,
			int end,Map<String, Object> clientMap,Long userId) throws CommentServiceException { 
		return commentBO.getCommentList(contentId, start, end,clientMap,userId);
	}
	@Override
	public void deleteComment(Long commentId, Long userId)
			throws CommentServiceException {
		 commentBO.deleteComment(commentId,userId);
		
	}
	
}
