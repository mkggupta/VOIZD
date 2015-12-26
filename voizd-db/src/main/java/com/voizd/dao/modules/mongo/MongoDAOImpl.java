package com.voizd.dao.modules.mongo;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.MongoServer;
import com.voizd.dao.constants.MongoConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class MongoDAOImpl extends AbstractDBManager implements MongoDAO{
	Logger logger = LoggerFactory.getLogger(MongoDAOImpl.class);
	@SuppressWarnings("unchecked")
	@Override
	public List<MongoServer> getMongoServers() throws DataAccessFailedException {
		try {
			return sqlMapClient_.queryForList(MongoConstants.GET_MONOG_SERVER_INFO);
		} catch (SQLException e) {
			logger.error("Exception in getMongoServers: " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}


}
