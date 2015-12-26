package com.voizd.dao.modules.comment;

import com.voizd.common.beans.vo.CommentCounterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface CommentCounterDAO {
	
	public void createCommentCounter(Long commentId) throws DataUpdateFailedException;
	public void updateCommentCounter(CommentCounterVO commentCounterVO) throws DataUpdateFailedException;
	public CommentCounterVO getCommentCounter(Long commentId) throws DataAccessFailedException;

}
