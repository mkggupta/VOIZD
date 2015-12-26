/**
 * 
 */
package com.voizd.appstats.core.service.request.v1_0;

import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.common.beans.vo.RequestLogVO;

/**
 * @author Suresh
 * 
 */
public interface RequestLogService {

	void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException;
}
