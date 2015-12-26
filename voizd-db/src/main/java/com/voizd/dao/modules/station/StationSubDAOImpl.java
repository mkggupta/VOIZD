package com.voizd.dao.modules.station;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StationFollowerVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.StationSubContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class StationSubDAOImpl extends AbstractDBManager implements StationSubDAO {
	private static Logger logger = LoggerFactory.getLogger(StationSubDAOImpl.class);
	@Override
	public Byte getStationTap(Long stationId, Long streamId,Long follwerId,Long follweeId)throws DataAccessFailedException {
		
		  try{
		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("follwerId", follwerId);
			params.put("streamId", streamId);
			params.put("follweeId", follweeId);
			return (Byte)sqlMapClient_.queryForObject(StationSubContants.GET_STATION_FOLLOWER_INFO_BY_STREAM, params);
		  }catch(SQLException e){
				logger.error("Exception in getStationTap : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
	}
	@Override
	public Byte getStationTap(Long stationId, Long follwerId)throws DataAccessFailedException {
		
		  try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("follwerId", follwerId);
			return (Byte)sqlMapClient_.queryForObject(StationSubContants.GET_STATION_FOLLOWER_INFO, params);
		  }catch(SQLException e){
				logger.error("Exception in getStationTap : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
	}

	@Override
	public void saveStationTap(Long stationId,Long streamId, Long follwerId,Long follweeId)throws DataUpdateFailedException {
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("stationId", stationId);
				params.put("follwerId", follwerId);
				params.put("streamId", streamId);
				params.put("follweeId", follweeId);
				sqlMapClient_.insert(StationSubContants.INSERT_STATION_FOLLOWER_INFO, params);
			  }catch(SQLException e){
					logger.error("Exception in saveStationTap : " + e.getMessage());
					throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public void updateStationTap(Long stationId,Long streamId, Long follwerId,Byte status,Long follweeId)throws DataUpdateFailedException {
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("stationId", stationId);
				params.put("follwerId", follwerId);
				params.put("streamId", streamId);
				params.put("status", status);
				params.put("follweeId", follweeId);
				sqlMapClient_.update(StationSubContants.UPDATE_STATION_FOLLOWER_INFO, params);
			  }catch(SQLException e){
					logger.error("Exception in saveStationTap : " + e.getMessage());
					throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
		
	}
	
	@SuppressWarnings("unchecked")
	public List<StationFollowerVO> getTappedStationList(Long follwerId,int startLimit,int endLimit) throws DataAccessFailedException{
		
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("follwerId", follwerId);
				params.put("status", VoizdConstant.STATION_FOLLOW_STATUS);
				params.put("startLimit", startLimit);
				params.put("endLimit", endLimit);
				return (List<StationFollowerVO>)sqlMapClient_.queryForList(StationSubContants.GET_STATION_FOLLOWED_BY_ME, params);
			  }catch(SQLException e){
					logger.error("Exception in getTappedStationList : " + e.getMessage());
					throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
		
	}
	
	@SuppressWarnings("unchecked")
	public List<StationFollowerVO> getStationFollowerList(Long stationId,Long follweeId,int startLimit,int endLimit) throws DataAccessFailedException{

		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
			  	params.put("stationId", stationId);
				params.put("follweeId", follweeId);
				params.put("status", VoizdConstant.STATION_FOLLOW_STATUS);
				params.put("startLimit", startLimit);
				params.put("endLimit", endLimit);			
				return (List<StationFollowerVO>)sqlMapClient_.queryForList(StationSubContants.GET_MY_STATION_FOLLOWER, params);
			  }catch(SQLException e){
					logger.error("Exception in getTappedStationList : " + e.getMessage(),e);
					throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
	}
	
	

}
