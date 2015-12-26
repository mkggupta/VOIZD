package com.voizd.service.comment.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.CommentVO;
import com.voizd.service.comment.exception.CommentServiceException;
import com.voizd.service.comment.manager.CommentManager;

public class CommentServiceImpl implements CommentService {
	private CommentManager commentManager;
	
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	@Override
	public HashMap<String, Object> createComment(CommentVO commentVO) throws CommentServiceException{
		return commentManager.createComment(commentVO);
	}

	@Override
	public HashMap<String, Object> getCommentList(Long contentId, int start,
			int end,Map<String, Object> clientMap,Long userId) throws CommentServiceException {
		return commentManager.getCommentList(contentId, start, end,clientMap,userId);
	}

	@Override
	public void deleteComment(Long commentId,Long userId) throws CommentServiceException {
		 commentManager.deleteComment(commentId,userId);
	}
	
}
