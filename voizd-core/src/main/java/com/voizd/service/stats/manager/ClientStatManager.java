package com.voizd.service.stats.manager;

import java.util.Map;

import com.voizd.service.stats.exception.ClientStatServiceException;

public interface ClientStatManager {
	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException ;
}
