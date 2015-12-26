package com.voizd.service.stats.manager;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.service.stats.bo.ClientStatBO;
import com.voizd.service.stats.exception.ClientStatServiceException;

public class ClientStatManagerImpl implements ClientStatManager {
	private static Logger logger = LoggerFactory.getLogger(ClientStatManagerImpl.class);
	private ClientStatBO clientStatBO;

	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException {
		clientStatBO.saveAppStats(statsMap);
	}

	public void setClientStatBO(ClientStatBO clientStatBO) {
		this.clientStatBO = clientStatBO;
	}

	
	
}
