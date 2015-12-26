package com.voizd.service.station.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StationVO;
import com.voizd.service.station.bo.StationBO;
import com.voizd.service.station.exception.StationServiceException;

public class StationManagerImpl implements StationManager {
	private static Logger logger = LoggerFactory.getLogger(StationManagerImpl.class);
	private StationBO stationBO;
	public Integer createStation(StationVO stationVO)throws StationServiceException{
		return stationBO.createStation(stationVO);
	} 
	public void setStationBO(StationBO stationBO) {
		this.stationBO = stationBO;
	}
	@Override
	public void deleteStation(Long stationId, Long creatorId)throws StationServiceException {
		stationBO.deleteStation(stationId,creatorId);
		
	}
	@Override
	public StationVO getStation(Long stationId) throws StationServiceException {
		return stationBO.getStation(stationId);
	}
	@Override
	public void updateStation(StationVO stationVO)throws StationServiceException {
		stationBO.updateStation(stationVO);
		
	}
	@Override
	public List<StationVO> getUserStations(Long creatorId, Byte status)throws StationServiceException {
		return stationBO.getUserStations(creatorId,status);
	}
	@Override
	public HashMap<String, Object> getStations(Long stationId, Long creatorId,Byte status,int startLimit,  int endLimit, boolean order,Map<String, Object> clientMap)
			throws StationServiceException {
		return stationBO.getStations(stationId,creatorId,status,startLimit,endLimit,order,clientMap);
	}
	
	public HashMap<String, Object> getStationDetail(Long stationId,Long creatorId,Map<String, Object> clientMap) throws StationServiceException {
		return stationBO.getStationDetail(stationId,creatorId,clientMap);
	}
	public HashMap<String, Object>  createStationShareUrl(Long stationId,Byte appId) throws StationServiceException {
		return stationBO.createStationShareUrl(stationId,appId);
	}
	public HashMap<String, Object> getIFollowList(Long stationId,Long followerId, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws StationServiceException {
		return stationBO.getIFollowList(stationId,followerId,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getMyFollowerList(Long stationId,Long creatorId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		return stationBO.getMyFollowerList(stationId,creatorId,startLimit,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getTopVoizerList(Long userId,int startLimit,int endLimit, boolean order,Map<String, Object> clientMap) throws StationServiceException {
		return stationBO.getTopVoizerList(userId,startLimit,endLimit,order,clientMap);
	}
}
