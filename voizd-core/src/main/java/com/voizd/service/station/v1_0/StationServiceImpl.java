package com.voizd.service.station.v1_0;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voizd.common.beans.vo.StationVO;
import com.voizd.service.station.exception.StationServiceException;
import com.voizd.service.station.manager.StationManager;

public class StationServiceImpl implements com.voizd.service.station.v1_0.StationService {
	private StationManager stationManager ;
	
	public Integer createStation(StationVO stationVO) throws StationServiceException {
		return stationManager.createStation(stationVO);
	}
	public void setStationManager(StationManager stationManager) {
		this.stationManager = stationManager;
	}
	@Override
	public void deleteStation(Long stationId, Long creatorId)throws StationServiceException {
		stationManager.deleteStation(stationId, creatorId);
		
	}
	@Override
	public StationVO getStation(Long stationId) throws StationServiceException {
	
		return stationManager.getStation(stationId);
	}
	@Override
	public void updateStation(StationVO stationVO)throws StationServiceException {
		stationManager.updateStation(stationVO);
		
	}
	@Override
	public List<StationVO> getUserStations(Long creatorId, Byte status)throws StationServiceException {
		
		return stationManager.getUserStations(creatorId,status);
	}
	@Override
	public  HashMap<String, Object> getStations(Long stationId, Long creatorId,Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)
			throws StationServiceException {
		return stationManager.getStations(stationId,creatorId,status,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getStationDetail(Long stationId,Long creatorId,Map<String, Object> clientMap) throws StationServiceException {
		return stationManager.getStationDetail(stationId,creatorId,clientMap);
	}
	public HashMap<String, Object>  createStationShareUrl(Long stationId,Byte appId) throws StationServiceException {
		return stationManager.createStationShareUrl(stationId, appId);
	}
	public HashMap<String, Object> getIFollowList(Long stationId,Long followerId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		return stationManager.getIFollowList(stationId,followerId,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getMyFollowerList(Long stationId,Long creatorId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		return stationManager.getMyFollowerList(stationId,creatorId,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getTopVoizerList(Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		return stationManager.getTopVoizerList(userId,startLimit,endLimit,order,clientMap);
	}
	
}
