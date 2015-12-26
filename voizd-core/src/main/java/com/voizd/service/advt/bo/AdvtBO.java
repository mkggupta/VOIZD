package com.voizd.service.advt.bo;

import java.util.Map;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.service.advt.exception.AdvtServiceException;

public interface AdvtBO {
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap) throws AdvtServiceException;

}
