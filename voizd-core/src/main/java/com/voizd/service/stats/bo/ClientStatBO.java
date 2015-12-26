package com.voizd.service.stats.bo;

import java.util.Map;

import com.voizd.service.stats.exception.ClientStatServiceException;

public interface ClientStatBO {
	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException ;
}
