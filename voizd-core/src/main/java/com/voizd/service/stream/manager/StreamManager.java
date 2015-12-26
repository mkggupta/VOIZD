package com.voizd.service.stream.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.service.stream.exception.StreamServiceException;

public interface StreamManager {
	public HashMap<String, Object> getStream(Long streamId,Long userId, Byte streamState, int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StreamServiceException ;
	public HashMap<String, Object>  createStreamShareUrl(Long userId,Byte appId) throws StreamServiceException ;
	public Long createStream(StreamVO streamVO) throws StreamServiceException ;
	public StreamVO getStream(Long streamId) throws StreamServiceException ;
	public void updateStream(StreamVO streamVO) throws StreamServiceException ;
	public void deleteStream(Long streamId,Long userId) throws StreamServiceException ;
}
