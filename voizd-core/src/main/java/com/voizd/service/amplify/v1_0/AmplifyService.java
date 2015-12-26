package com.voizd.service.amplify.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.service.amplify.exception.AmplifyServiceFailedException;

public interface AmplifyService {
	public void ampifyContent(Long contentId, Long creatorId,Byte status,Long userId)throws AmplifyServiceFailedException;
	public HashMap<String, Object> getAmplifierList(Long contentId,Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws AmplifyServiceFailedException ;
}
