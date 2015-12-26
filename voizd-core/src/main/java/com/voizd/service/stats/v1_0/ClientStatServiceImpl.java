package com.voizd.service.stats.v1_0;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.service.stats.exception.ClientStatServiceException;
import com.voizd.service.stats.manager.ClientStatManager;

public class ClientStatServiceImpl implements ClientStatService {
	private ClientStatManager clientStatManager ;
	private static Logger logger = LoggerFactory.getLogger(ClientStatServiceImpl.class);
	
	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException  {
		 clientStatManager.saveAppStats(statsMap);
	}

	public void setClientStatManager(ClientStatManager clientStatManager) {
		this.clientStatManager = clientStatManager;
	}

	
	
}
