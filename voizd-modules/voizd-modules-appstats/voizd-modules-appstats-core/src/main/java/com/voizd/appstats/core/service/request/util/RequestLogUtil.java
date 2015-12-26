/**
 * 
 */
package com.voizd.appstats.core.service.request.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.RequestLogVO;
import com.voizd.dao.entities.RequestLog;

/**
 * @author Suresh
 * 
 */
public class RequestLogUtil {
	static Logger logger = LoggerFactory.getLogger(RequestLogUtil.class);

	public static RequestLog transformRequestLogVOtoRequestLog(RequestLogVO requestLogVO) {
		RequestLog requestLog = new RequestLog();
		if (null != requestLogVO) {
			try {
				BeanUtils.copyProperties(requestLog, requestLogVO);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying requestLogVO " + requestLogVO + " to requestLog", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying requestLogVO " + requestLogVO + " to requestLog", e);
			}
		}
		return requestLog;
	}
}
