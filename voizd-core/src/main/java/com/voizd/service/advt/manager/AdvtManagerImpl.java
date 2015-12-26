package com.voizd.service.advt.manager;

import java.util.Map;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.service.advt.bo.AdvtBO;
import com.voizd.service.advt.exception.AdvtServiceException;

public class AdvtManagerImpl implements AdvtManager {
	private AdvtBO advtBO ;

	public void setAdvtBO(AdvtBO advtBO) {
		this.advtBO = advtBO;
	}

	@Override
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap)throws AdvtServiceException {
		 return advtBO.getMicAdvt(clientMap);
	}

}
