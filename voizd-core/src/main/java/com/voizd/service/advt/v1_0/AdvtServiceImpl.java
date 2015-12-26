package com.voizd.service.advt.v1_0;

import java.util.Map;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.service.advt.exception.AdvtServiceException;
import com.voizd.service.advt.manager.AdvtManager;

public class AdvtServiceImpl implements AdvtService {
	private AdvtManager advtManager;
	public void setAdvtManager(AdvtManager advtManager) {
		this.advtManager = advtManager;
	}
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap) throws AdvtServiceException{
		return advtManager.getMicAdvt(clientMap);
	}

}
