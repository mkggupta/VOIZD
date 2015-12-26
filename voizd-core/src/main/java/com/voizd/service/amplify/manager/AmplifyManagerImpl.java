package com.voizd.service.amplify.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.service.amplify.bo.AmplifyActionBO;
import com.voizd.service.amplify.exception.AmplifyServiceFailedException;

public class AmplifyManagerImpl implements AmplifyManager {

	private AmplifyActionBO  amplifyActionBO;
	public void setAmplifyActionBO(AmplifyActionBO amplifyActionBO) {
		this.amplifyActionBO = amplifyActionBO;
	}

	@Override
	public void ampifyContent(Long contentId, Long creatorId, Byte status,Long userId)throws AmplifyServiceFailedException{
		amplifyActionBO.ampifyContent(contentId,creatorId,status,userId);
	}
	
	public HashMap<String, Object> getAmplifierList(Long contentId,Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws AmplifyServiceFailedException  {
		return amplifyActionBO.getAmplifierList(contentId, userId, startLimit, endLimit, order, clientMap);
	}

	
		
	
}
