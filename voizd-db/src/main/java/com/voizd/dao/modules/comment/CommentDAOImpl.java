package com.voizd.dao.modules.comment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.CommentConstants;
import com.voizd.dao.constants.ContentConstants;
import com.voizd.dao.entities.CommentInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class CommentDAOImpl extends AbstractDBManager implements CommentDAO {
	private static Logger logger = LoggerFactory.getLogger(CommentDAOImpl.class);
	@Override
	public Long saveCommentInfo(CommentInfo commentInfo)throws DataUpdateFailedException {
		try {
			return (Long) sqlMapClient_.insert(CommentConstants.INSERT_COMMENT_INFO,commentInfo);
			}catch(SQLException e){
				logger.error("Exception in saveCommentInfo : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommentInfo> getCommentListByContentId(Long contentId,int sLimit,int eLimit)
			throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("sLimit", sLimit);
			params.put("eLimit", eLimit);
			return (List<CommentInfo>) sqlMapClientSlave_.queryForList(CommentConstants.GET_COMMENT_INFO_LIST_BY_CONTENT_ID,params);
		} catch (SQLException e) {
			logger.error("Exception in getCommentListByContentId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public CommentInfo getCommentInfoByContentId(Long contentId)
			throws DataAccessFailedException {
		try {
			return (CommentInfo) sqlMapClientSlave_.queryForObject(CommentConstants.GET_COMMENT_INFO_BY_CONTENT_ID,contentId);
		} catch (SQLException e) {
			logger.error("Exception in getCommentInfoByContentId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	
	@Override
	public CommentInfo getCommentInfoByCommentId(Long commentId)throws DataAccessFailedException {
		try {
			return (CommentInfo) sqlMapClientSlave_.queryForObject(CommentConstants.GET_COMMENT_INFO_BY_COMMENT_ID,commentId);
		} catch (SQLException e) {
			logger.error("Exception in getCommentInfoByCommentId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public int updateCommentStatusByCommentId(Long commentId, Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("commentId", commentId);
			params.put("userId", userId);
			params.put("status",status);
			logger.debug("Exception in updateCommentStatusByCommentId : "+ commentId+" "+userId);
			return sqlMapClient_.update(CommentConstants.UPDATE_COMMENT_STATUS_BY_COMMENT_ID, params);
		} catch (SQLException e) {
			logger.error("Exception in updateCommentStatusByCommentId : ", e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
