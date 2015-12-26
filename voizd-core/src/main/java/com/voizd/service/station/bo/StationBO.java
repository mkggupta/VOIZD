package com.voizd.service.station.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.service.station.exception.StationServiceException;

public interface StationBO {
	
	public Integer createStation(StationVO stationVO)throws StationServiceException;
	public StationVO getStation(Long stationId) throws StationServiceException ;
	public void updateStation(StationVO stationVO) throws StationServiceException ;
	public void deleteStation(Long stationId,Long creatorId) throws StationServiceException ;
	public List<StationVO> getUserStations(Long creatorId, Byte status) throws StationServiceException ;
	public HashMap<String, Object> getStations(Long stationId,Long creatorId, Byte status,int startLimit, int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException ;
	public HashMap<String, Object> getStationDetail(Long stationId,Long creatorId,Map<String, Object> clientMap) throws StationServiceException;
	public HashMap<String, Object>  createStationShareUrl(Long stationId,Byte appId) throws StationServiceException ;
	public HashMap<String, Object> getIFollowList(Long stationId,Long followerId, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException ;
	public List<StationVO> getMyStationList(Long creatorId,int startLimit,int endLimit)throws StationServiceException;
	public StationCounterVO getStationCounter(Long stationId) throws StationServiceException;
	public UserCounterVO getUserCounter(Long userId) throws StationServiceException;
	public HashMap<String, Object> getMyFollowerList(Long stationId,Long creatorId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException ;
	public HashMap<String, Object> getTopVoizerList(Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException ;
}
