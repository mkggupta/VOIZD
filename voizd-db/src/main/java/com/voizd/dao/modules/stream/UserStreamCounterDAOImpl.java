package com.voizd.dao.modules.stream;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StreamCounterVO;
import com.voizd.dao.constants.UserStreamCounterContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class UserStreamCounterDAOImpl extends AbstractDBManager implements UserStreamCounterDAO {
	private static Logger logger = LoggerFactory.getLogger(UserStreamCounterDAOImpl.class);

	@Override
	public void createStreamCounter(Long streamId)throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(UserStreamCounterContants.INSERT_USER_STREAM_COUNTER,streamId);
		} catch (SQLException e) {
			logger.error("Exception in createStreamCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public StreamCounterVO getStreamCounter(Long streamId)throws DataAccessFailedException {
		try {
			return (StreamCounterVO) sqlMapClient_.queryForObject(UserStreamCounterContants.GET_USER_STREAM_COUNTER,streamId);
		} catch (SQLException e) {
			logger.error("Exception in getStreamCounter : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public List<StreamCounterVO> getStreamCounterList(int startLimit,
			int endLimit) throws DataAccessFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStreamCounter(StreamCounterVO streamCounterVO)throws DataUpdateFailedException {
		try {
			sqlMapClient_.update(UserStreamCounterContants.UPDATE_USER_STREAM_COUNTER,streamCounterVO);
		} catch (SQLException e) {
			logger.error("Exception in updateStreamCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}


}
