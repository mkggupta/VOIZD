package com.voizd.dao.modules.station;

import com.voizd.common.beans.vo.StationLikeVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface StationLikeDAO {
	public void insertStationLike(Long stationId, Long userId,Byte status) throws DataUpdateFailedException;
	public void updateStationLikeStatus(Long stationId, Long userId,Byte status) throws DataUpdateFailedException;
	public Byte getUserStationLike(Long stationId, Long userId) throws DataAccessFailedException;
	public StationLikeVO userStationLikeStatus(Long stationId, Long userId) throws DataAccessFailedException;
}
