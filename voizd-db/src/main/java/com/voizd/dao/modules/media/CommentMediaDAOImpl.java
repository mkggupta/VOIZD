package com.voizd.dao.modules.media;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.CommentMediaConstants;
import com.voizd.dao.entities.CommentMediaInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class CommentMediaDAOImpl extends AbstractDBManager implements CommentMediaDAO{
	private static Logger logger = LoggerFactory.getLogger(CommentMediaDAOImpl.class);
	@Override
	public void createCommentMedia(CommentMediaInfo commentMediaInfo)
			throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(CommentMediaConstants.INSERT_COMMENT_MEDIA_INFO,commentMediaInfo);
			}catch(SQLException e){
				logger.error("Exception in createCommentMedia : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@Override
	public CommentMediaInfo getCommentMedia(Long commentId)
			throws DataAccessFailedException {
		try {
			return (CommentMediaInfo) sqlMapClient_.queryForObject(CommentMediaConstants.GET_COMMENT_MEDIA_BY_COMMENT_ID,commentId);
		} catch (SQLException e) {
			logger.error("Exception in getCommentMedia : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
