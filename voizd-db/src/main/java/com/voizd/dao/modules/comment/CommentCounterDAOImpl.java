package com.voizd.dao.modules.comment;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.CommentCounterVO;
import com.voizd.dao.constants.CommentCounterContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class CommentCounterDAOImpl extends AbstractDBManager implements CommentCounterDAO {
	private static Logger logger = LoggerFactory.getLogger(CommentCounterDAOImpl.class);
	@Override
	public void createCommentCounter(Long commentId)throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(CommentCounterContants.INSERT_COMMENT_COUNTER,commentId);
		} catch (SQLException e) {
			logger.error("Exception in createCommentCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public CommentCounterVO getCommentCounter(Long commentId)throws DataAccessFailedException {
		try {
			return (CommentCounterVO) sqlMapClient_.queryForObject(CommentCounterContants.GET_COMMENT_COUNTER,commentId);
		} catch (SQLException e) {
			logger.error("Exception in getCommentCounter : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateCommentCounter(CommentCounterVO commentCounterVO)throws DataUpdateFailedException {
		try {
			sqlMapClient_.update(CommentCounterContants.UPDATE_COMMENT_COUNTER,commentCounterVO);
		} catch (SQLException e) {
			logger.error("Exception in updateCommentCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

}
