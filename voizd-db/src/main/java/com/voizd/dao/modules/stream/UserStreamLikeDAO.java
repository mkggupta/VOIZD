package com.voizd.dao.modules.stream;

import com.voizd.common.beans.vo.StreamLikeVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface UserStreamLikeDAO {
	public void insertStreamLike(Long streamId, Long userId,Byte status) throws DataUpdateFailedException;
	public void updateStreamLikeStatus(Long streamId, Long userId,Byte status) throws DataUpdateFailedException;
	public Byte getUserStreamLike(Long streamId, Long userId) throws DataAccessFailedException;
	public StreamLikeVO userStreamLikeStatus(Long streamId, Long userId) throws DataAccessFailedException;
}
