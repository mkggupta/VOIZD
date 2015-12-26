package com.voizd.dao.modules.stats;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.UserConstants;
import com.voizd.dao.entities.RequestLog;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class RequestLogDAOImpl extends AbstractDBManager implements RequestLogDAO {
	Logger logger = LoggerFactory.getLogger(RequestLogDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.dao.modules.stats.RequestLogDAO#saveRequestLog(com.voizd.dao.entities.RequestLog)
	 */
	@Override
	public void saveRequestLog(RequestLog requestLog) throws DataUpdateFailedException {
		try {
			statsSqlMapClient_.insert(UserConstants.QUERY_INSERT_REQUEST_LOG, requestLog);
		} catch (SQLException e) {
			logger.error("Exception in storing stats details in database  : " + requestLog + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
