package com.voizd.service.comment.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.CommentVO;
import com.voizd.service.comment.exception.CommentServiceException;

public interface CommentService {
	public HashMap<String, Object> createComment(CommentVO commentVO) throws CommentServiceException ;
	public HashMap<String, Object> getCommentList(Long contentId,int start,int end,Map<String, Object> clientMap,Long userId) throws CommentServiceException ;
	public void deleteComment(Long commentId,Long userId) throws CommentServiceException ;
}
