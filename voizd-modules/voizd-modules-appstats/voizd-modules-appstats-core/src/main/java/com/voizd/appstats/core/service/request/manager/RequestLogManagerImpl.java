/**
 * 
 */
package com.voizd.appstats.core.service.request.manager;

import com.voizd.appstats.core.service.request.bo.RequestLogBO;
import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.common.beans.vo.RequestLogVO;

/**
 * @author Suresh
 * 
 */
public class RequestLogManagerImpl implements RequestLogManager {

	private RequestLogBO requestLogBO;

	/**
	 * @return the requestLogBO
	 */
	public RequestLogBO getRequestLogBO() {
		return requestLogBO;
	}

	/**
	 * @param requestLogBO
	 *            the requestLogBO to set
	 */
	public void setRequestLogBO(RequestLogBO requestLogBO) {
		this.requestLogBO = requestLogBO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.appstats.core.service.request.manager.RequestLogManager#logRequest()
	 */
	@Override
	public void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException {
		requestLogBO.logRequest(requestLogVO);
	}

}
