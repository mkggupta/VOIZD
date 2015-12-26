package com.voizd.service.advt.v1_0;

import java.util.Map;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.service.advt.exception.AdvtServiceException;



public interface AdvtService {
	
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap) throws AdvtServiceException;

}
