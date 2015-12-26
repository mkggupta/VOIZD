package com.voizd.dao.modules.mongo;

import java.util.List;

import com.voizd.common.beans.vo.MongoServer;
import com.voizd.dao.exception.DataAccessFailedException;

public interface MongoDAO {
	public List<MongoServer> getMongoServers() throws DataAccessFailedException;
}
