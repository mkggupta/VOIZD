package com.voizd.dao.modules.user;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.dao.constants.UserCounterContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class UserCounterDAOImpl extends AbstractDBManager implements UserCounterDAO {
	private static Logger logger = LoggerFactory.getLogger(UserCounterDAOImpl.class);
	public void createUserCounter(Long userId) throws DataUpdateFailedException{
		try {
			sqlMapClient_.insert(UserCounterContants.INSERT_USER_COUNTER,userId);
		} catch (SQLException e) {
			logger.error("Exception in createUserCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	public void updateUserCounter(UserCounterVO userCounterVO) throws DataUpdateFailedException{
		try {
			sqlMapClient_.update(UserCounterContants.UPDATE_USER_COUNTER,userCounterVO);
		} catch (SQLException e) {
			logger.error("Exception in updateUserCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@Override
	public UserCounterVO getUserCounter(Long userId)throws DataAccessFailedException {
		try {
			return (UserCounterVO) sqlMapClient_.queryForObject(UserCounterContants.GET_USER_COUNTER,userId);
		} catch (SQLException e) {
			logger.error("Exception in getUserCounter : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
}
