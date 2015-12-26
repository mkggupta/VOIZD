package com.voizd.dao.modules.stream;

import java.util.List;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface UserStreamDAO {
	public Long createStream(StreamVO streamVO) throws DataUpdateFailedException;
	public Boolean streamAlreadyExists(String streamName,Long userId) throws DataAccessFailedException;
	public StreamVO getStreamInfo(Long streamId) throws DataAccessFailedException;	
	public void updateStream(StreamVO streamVO) throws DataUpdateFailedException;
	public List<StreamVO> getMyStreamList(Long userId,int startLimit,int endLimit) throws DataAccessFailedException;
	public List<StreamVO> getUserStreamList(Long userId) throws DataAccessFailedException;
	public void updateStreamStatus(Long userId,Long streamId,Byte status,Boolean hasContent) throws DataUpdateFailedException;
	public StreamVO getStreamInfoByUserId(Long userId,Long streamId) throws DataAccessFailedException;
}

