/**
 * 
 */
package com.voizd.appstats.core.service.request.manager;

import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.common.beans.vo.RequestLogVO;

/**
 * @author Suresh
 * 
 */
public interface RequestLogManager {

	void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException;
}
