package com.voizd.dao.modules.comment;

import java.util.List;

import com.voizd.dao.entities.CommentInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface CommentDAO {
	Long saveCommentInfo(CommentInfo commentInfo) throws DataUpdateFailedException;
	List<CommentInfo> getCommentListByContentId(Long contentId,int sLimit,int eLimit) throws DataAccessFailedException;
	CommentInfo getCommentInfoByContentId(Long commentId) throws DataAccessFailedException;
	int updateCommentStatusByCommentId(Long commentId,Long userId,Byte status) throws DataUpdateFailedException;
	CommentInfo getCommentInfoByCommentId(Long commentId) throws DataAccessFailedException;
}
