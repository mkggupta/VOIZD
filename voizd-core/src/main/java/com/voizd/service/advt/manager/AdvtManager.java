package com.voizd.service.advt.manager;

import java.util.Map;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.service.advt.exception.AdvtServiceException;

public interface AdvtManager {
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap) throws AdvtServiceException;

}
