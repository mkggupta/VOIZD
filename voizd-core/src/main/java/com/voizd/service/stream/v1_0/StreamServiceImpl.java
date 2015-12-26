package com.voizd.service.stream.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.service.stream.exception.StreamServiceException;
import com.voizd.service.stream.manager.StreamManager;

public class StreamServiceImpl implements StreamService {
	private StreamManager streamManager ;
	
	
	@Override
	public HashMap<String, Object> getStream(Long streamId,Long userId, Byte streamState, int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StreamServiceException {
		return streamManager.getStream(streamId,userId,streamState,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object>  createStreamShareUrl(Long userId,Byte appId) throws StreamServiceException {
		return streamManager.createStreamShareUrl(userId,appId);
	}

	public void setStreamManager(StreamManager streamManager) {
		this.streamManager = streamManager;
	}
	

	public Long createStream(StreamVO streamVO) throws StreamServiceException {
		return streamManager.createStream(streamVO);
	}
	public StreamVO getStream(Long streamId) throws StreamServiceException {
		return null;
	}
	public void updateStream(StreamVO streamVO) throws StreamServiceException {
		streamManager.updateStream(streamVO);
	}
	public void deleteStream(Long streamId,Long userId) throws StreamServiceException {
		streamManager.deleteStream(streamId,userId);
	}
	
}
