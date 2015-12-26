package com.voizd.dao.modules.search;

import java.util.List;

import com.voizd.dao.entities.ElasticSearchServer;
import com.voizd.dao.exception.DataAccessFailedException;


public interface SearchDAO {
	public List<ElasticSearchServer> getAllLuceneSearchServers() throws DataAccessFailedException;
}
