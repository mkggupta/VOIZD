package com.voizd.appstats.core.service.request.bo;

import com.voizd.appstats.core.service.request.exception.RequestLogServiceFailedException;
import com.voizd.common.beans.vo.RequestLogVO;

public interface RequestLogBO {

	void logRequest(RequestLogVO requestLogVO) throws RequestLogServiceFailedException;
}
