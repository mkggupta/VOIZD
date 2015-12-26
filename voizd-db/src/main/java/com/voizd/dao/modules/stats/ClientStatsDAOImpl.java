package com.voizd.dao.modules.stats;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StatsVO;
import com.voizd.dao.constants.StatsConstants;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class ClientStatsDAOImpl extends AbstractDBManager implements ClientStatsDAO {
	Logger logger = LoggerFactory.getLogger(ClientStatsDAOImpl.class);
	@Override
	public void saveListenStats(StatsVO statsVO)throws DataUpdateFailedException {
		try {
			statsSqlMapClient_.insert(StatsConstants.INSERT_LISTEN_STATS_INFO, statsVO);
		} catch (SQLException e) {
			logger.error("Exception in storing stats details in database  : " + statsVO + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public void saveShareStats(StatsVO statsVO)throws DataUpdateFailedException {
		try {
			statsSqlMapClient_.insert(StatsConstants.INSERT_SHARE_STATS_INFO, statsVO);
		} catch (SQLException e) {
			logger.error("Exception in storing stats details in database  : " + statsVO + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}
	
	@Override
	public void saveCommentListenStats(StatsVO statsVO)throws DataUpdateFailedException {
		try {
			statsSqlMapClient_.insert(StatsConstants.INSERT_COMMENT_LISTEN_STATS_INFO, statsVO);
		} catch (SQLException e) {
			logger.error("Exception in storing stats saveCommentListenStats details in database  : " + statsVO + " error  : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

}
