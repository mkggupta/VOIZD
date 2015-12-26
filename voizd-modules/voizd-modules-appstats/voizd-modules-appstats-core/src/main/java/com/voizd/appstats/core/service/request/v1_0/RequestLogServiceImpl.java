/**
 * 
 */
package com.voizd.appstats.core.service.request.v1_0;

import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.appstats.core.service.request.manager.RequestLogManager;
import com.voizd.common.beans.vo.RequestLogVO;

/**
 * @author Suresh
 * 
 */
public class RequestLogServiceImpl implements RequestLogService {

	public RequestLogManager requestLogManager;

	/**
	 * @return the requestLogManager
	 */
	public RequestLogManager getRequestLogManager() {
		return requestLogManager;
	}

	/**
	 * @param requestLogManager
	 *            the requestLogManager to set
	 */
	public void setRequestLogManager(RequestLogManager requestLogManager) {
		this.requestLogManager = requestLogManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.appstats.core.service.request.v1_0.RequestLogService#logRequest()
	 */
	@Override
	public void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException {
		requestLogManager.logRequest(requestLogVO);
	}

}
