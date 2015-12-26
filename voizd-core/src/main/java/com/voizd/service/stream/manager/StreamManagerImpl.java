package com.voizd.service.stream.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.service.stream.bo.StreamBO;
import com.voizd.service.stream.exception.StreamServiceException;

public class StreamManagerImpl implements StreamManager {
	private StreamBO streamBO;

	@Override
	public HashMap<String, Object> getStream(Long streamId,Long userId, Byte streamState, int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StreamServiceException {
		return streamBO.getStream(streamId,userId,streamState,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object>  createStreamShareUrl(Long userId,Byte appId) throws StreamServiceException {
		return streamBO.createStreamShareUrl(userId,appId);
	}

	public void setStreamBO(StreamBO streamBO) {
		this.streamBO = streamBO;
	}
	
	public Long createStream(StreamVO streamVO) throws StreamServiceException {
		return streamBO.createStream(streamVO);
	}
	public StreamVO getStream(Long streamId) throws StreamServiceException {
		return null;
	}
	public void updateStream(StreamVO streamVO) throws StreamServiceException {
		streamBO.updateStream(streamVO);
	}
	public void deleteStream(Long streamId,Long userId) throws StreamServiceException {
		streamBO.deleteStream(streamId,userId);
	}
	
	
}
