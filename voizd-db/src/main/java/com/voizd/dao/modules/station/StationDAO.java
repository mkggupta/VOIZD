/**
 * 
 */
package com.voizd.dao.modules.station;

import java.util.List;

import com.voizd.common.beans.vo.StationVO;
import com.voizd.dao.entities.Station;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author arvind
 *
 */
public interface StationDAO {
	public Long createStation(StationVO stationVO) throws DataUpdateFailedException;
	public Boolean stationAlreadyExists(String stationName,Long creatorId) throws DataAccessFailedException;
	public StationVO getStationInfo(Long stationId) throws DataAccessFailedException;	
	public void updateStation(Station station) throws DataUpdateFailedException;
	public List<StationVO> getRecentStationList(int startLimit,int endLimit) throws DataAccessFailedException;
	public List<StationVO> getMyStationList(Long creatorId,int startLimit,int endLimit) throws DataAccessFailedException;
	public List<StationVO> getUserStationList(Long creatorId, Byte status) throws DataAccessFailedException;
	public StationVO getActiveStationInfo(Long stationId) throws DataAccessFailedException;	
	public void updateStationStatus(Long stationId,byte status) throws DataUpdateFailedException;
	public StationVO getStationDetail(Long stationId,Long creatorId) throws DataAccessFailedException;	
	public void updateStation(Long stationId,Long userId,Byte status) throws DataUpdateFailedException;
	public StationVO getUserStationInfo(Long creatorId,Byte status) throws DataAccessFailedException;	

	//public List<StationVO> getTrendingStationList(int endLimit) throws DataAccessFailedException;
	
}
