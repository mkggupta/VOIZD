package com.voizd.dao.modules.stream;

import java.util.List;

import com.voizd.common.beans.vo.StreamCounterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface UserStreamCounterDAO {
	public void createStreamCounter(Long streamId) throws DataUpdateFailedException;
	public void updateStreamCounter(StreamCounterVO streamCounterVO) throws DataUpdateFailedException;
	public StreamCounterVO getStreamCounter(Long streamId) throws DataAccessFailedException;
	public List<StreamCounterVO> getStreamCounterList(int startLimit,int endLimit) throws DataAccessFailedException;
}
