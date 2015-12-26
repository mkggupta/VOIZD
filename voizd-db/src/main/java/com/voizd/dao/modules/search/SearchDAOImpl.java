package com.voizd.dao.modules.search;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.SearchConstants;
import com.voizd.dao.entities.ElasticSearchServer;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class SearchDAOImpl extends AbstractDBManager implements SearchDAO{
	Logger logger = LoggerFactory.getLogger(SearchDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<ElasticSearchServer> getAllLuceneSearchServers() throws DataAccessFailedException {
		try {
			String status="active";
			return sqlMapClient_.queryForList(SearchConstants.GET_SEARCH_SERVER_INFO,status);
		} catch (SQLException e) {
			logger.error("Exception in getAllLuceneSearchServers: " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
