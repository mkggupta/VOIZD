package com.voizd.service.stats.v1_0;
import java.util.Map;

import com.voizd.service.stats.exception.ClientStatServiceException;

public interface ClientStatService {
	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException ;

}
