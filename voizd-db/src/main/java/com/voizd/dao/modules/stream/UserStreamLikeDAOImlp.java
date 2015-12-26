package com.voizd.dao.modules.stream;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StreamLikeVO;
import com.voizd.dao.constants.StreamLikeConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class UserStreamLikeDAOImlp extends AbstractDBManager implements UserStreamLikeDAO {
	private static Logger logger = LoggerFactory.getLogger(UserStreamLikeDAOImlp.class);

	@Override
	public void insertStreamLike(Long streamId, Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamId", streamId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.insert(StreamLikeConstants.INSERT_USER_STREAM_LIKE_INFO, params);
		} catch (Exception e) {
			logger.error("Exception in insertStreamLike : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}

	}

	@Override
	public Byte getUserStreamLike(Long streamId, Long userId) throws DataAccessFailedException {
	
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamId", streamId);
			params.put("userId", userId);
			return (Byte) sqlMapClient_.queryForObject(StreamLikeConstants.GET_USER_STREAM_LIKE_EXISTS, params);
			
		} catch (Exception e) {
			logger.error("Exception in isUserStreamLikeExits : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public void updateStreamLikeStatus(Long streamId, Long userId, Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamId", streamId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.update(StreamLikeConstants.UPDATE_USER_STREAM_LIKE_STATUS, params);
		} catch (Exception e) {
			logger.error("Exception in updateStreamLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public StreamLikeVO userStreamLikeStatus(Long streamId, Long userId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamId", streamId);
			params.put("userId", userId);
			return  (StreamLikeVO) sqlMapClient_.queryForObject(StreamLikeConstants.USER_STREAM_STATUS_VALUE, params);
		} catch (Exception e) {
			logger.error("Exception in userStreamLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	
	}

}
