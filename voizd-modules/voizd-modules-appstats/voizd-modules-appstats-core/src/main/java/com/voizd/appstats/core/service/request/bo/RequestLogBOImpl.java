/**
 * 
 */
package com.voizd.appstats.core.service.request.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.appstats.core.service.request.util.RequestLogUtil;
import com.voizd.common.beans.vo.RequestLogVO;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.stats.RequestLogDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Suresh
 * 
 */
public class RequestLogBOImpl implements RequestLogBO {
	Logger logger = LoggerFactory.getLogger(RequestLogBOImpl.class);
	private RequestLogDAO requestLogDAO;

	/**
	 * @return the requestLogDAO
	 */
	public RequestLogDAO getRequestLogDAO() {
		return requestLogDAO;
	}

	/**
	 * @param requestLogDAO
	 *            the requestLogDAO to set
	 */
	public void setRequestLogDAO(RequestLogDAO requestLogDAO) {
		this.requestLogDAO = requestLogDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voizd.appstats.core.service.request.bo.RequestLogBO#logRequest()
	 */
	@Override
	public void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException {
		try {
			requestLogDAO.saveRequestLog(RequestLogUtil.transformRequestLogVOtoRequestLog(requestLogVO));
		} catch (DataUpdateFailedException e) {
			logger.error("Exception in logging request details : " + requestLogVO + " error  : " + e.getMessage());
			throw new RequestLogServiceFailedException(ErrorCodesEnum.REQUEST_LOG_SERVICE_FAILED_EXCEPTION);
		}
	}

}
