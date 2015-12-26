package com.voizd.dao.modules.station;

import java.util.List;

import com.voizd.common.beans.vo.StationFollowerVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface StationSubDAO {
	
	public Byte getStationTap(Long stationId,Long streamId,Long follwerId,Long follweeId) throws DataAccessFailedException;
	public void saveStationTap(Long stationId,Long streamId,Long follwerId,Long follweeId) throws DataUpdateFailedException;
	public void updateStationTap(Long stationId,Long streamId, Long follwerId,Byte status,Long follweeId)throws DataUpdateFailedException;
	public List<StationFollowerVO> getTappedStationList(Long follwerId,int startLimit,int endLimit) throws DataAccessFailedException;
	public List<StationFollowerVO> getStationFollowerList(Long stationId,Long follweeId,int startLimit,int endLimit) throws DataAccessFailedException;
	public Byte getStationTap(Long stationId,Long follwerId) throws DataAccessFailedException;
	
	
}
