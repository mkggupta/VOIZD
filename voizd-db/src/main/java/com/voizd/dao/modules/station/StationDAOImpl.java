/**
 * 
 */
package com.voizd.dao.modules.station;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.StationContants;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.dao.entities.Station;
/**
 * @author arvind
 *
 */
public class StationDAOImpl extends AbstractDBManager implements StationDAO {
	private static Logger logger = LoggerFactory.getLogger(StationDAOImpl.class);
	

	@Override
	public Long createStation(StationVO station) throws DataUpdateFailedException {
		try {
		return (Long) sqlMapClient_.insert(StationContants.INSERT_STATION_INFO,station);
		}catch(SQLException e){
			logger.error("Exception in createStation : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public Boolean stationAlreadyExists(String stationName, Long creatorId)throws DataAccessFailedException {
		  boolean stationAlreadyExists = false;
		  try{
		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationName", stationName);
			params.put("creatorId", creatorId);
			Integer count = (Integer)sqlMapClient_.queryForObject(StationContants.GET_STATION_ALREADY_EXT_FOR_CREATOR_ID, params);
			logger.debug(" count="+count);
			if (count != null) {
				stationAlreadyExists = count > 0 ? true : false;
			} 
			
		    }catch(SQLException e){
				logger.error("Exception in stationAlreadyExists : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
		    logger.debug(" stationAlreadyExists="+stationAlreadyExists);
			return stationAlreadyExists;
	}

	@Override
	public StationVO getStationInfo(Long stationId) throws DataAccessFailedException {
		try {
			return (StationVO) sqlMapClient_.queryForObject(StationContants.GET_STATION_INFO,stationId);
		} catch (SQLException e) {
			logger.error("Exception in getStationInfo : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	public StationVO getActiveStationInfo(Long stationId) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status", VoizdConstant.STATION_ACTIVE_STATUS);
			return (StationVO) sqlMapClient_.queryForObject(StationContants.GET_ACTIVE_STATION_INFO,params);
		} catch (SQLException e) {
			logger.error("Exception in getActiveStationInfo : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	public void updateStation(Station station) throws DataUpdateFailedException{
		try {
			  sqlMapClient_.update(StationContants.UPDATE_STATION_INFO,station);
		} catch (SQLException e) {
			logger.error("Exception in updateStation : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StationVO> getRecentStationList(int startLimit,int endLimit)throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", VoizdConstant.STATION_ACTIVE_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<StationVO>) sqlMapClientSlave_.queryForList(StationContants.GET_RECENT_STATION_LIST,params);
		} catch (SQLException e) {
			logger.error("Exception in getRecentStationList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<StationVO> getMyStationList(Long creatorId,int startLimit,int endLimit) throws DataAccessFailedException{
		try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("creatorId", creatorId);
				params.put("status", VoizdConstant.STATION_DELETED_STATUS);
				params.put("startLimit", startLimit);
				params.put("endLimit", endLimit);
			return (List<StationVO>) sqlMapClientSlave_.queryForList(StationContants.GET_MY_STATION_LIST,params);
		} catch (SQLException e) {
			logger.error("Exception in getMyStationList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	

	
	@SuppressWarnings("unchecked")
	public List<StationVO> getUserStationList(Long creatorId, Byte status) throws DataAccessFailedException{
	try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", status);
			params.put("creatorId", creatorId);
		 return (List<StationVO>) sqlMapClient_.queryForList(StationContants.GET_USER_STATIONS,params);
	} catch (SQLException e) {
		logger.error("Exception in getUserStationList : " + e.getMessage());
		throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
	}
	}

    @Override
	public void updateStationStatus(Long stationId, byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status",status);
			sqlMapClient_.update(StationContants.UPDATE_STATION_STATUS,params);
		} catch (SQLException e) {
			logger.error("Exception in updateStationStatus : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	 }
    
    public StationVO getStationDetail(Long stationId,Long creatorId) throws DataAccessFailedException{
    	try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("creatorId", creatorId);
			params.put("status", VoizdConstant.STATION_DELETED_STATUS);
			logger.debug("getStationDetail :stationId " + stationId+" creatorId "+ creatorId+" "+VoizdConstant.STATION_DELETED_STATUS);
		   return (StationVO) sqlMapClient_.queryForObject(StationContants.GET_STATION_DETAIL_BY_USERID,params);
		} catch (SQLException e) {
			logger.error("Exception in getUserStationList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
    }

	@Override
	public void updateStation(Long stationId, Long userId, Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("userid",userId);
			params.put("status",status);
			sqlMapClient_.update(StationContants.UPDATE_STATION_STATUS,params);
		} catch (SQLException e) {
			logger.error("Exception in updateStationStatus : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public StationVO getUserStationInfo(Long creatorId, Byte status) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creatorId", creatorId);
			params.put("status",status);
			return (StationVO) sqlMapClient_.queryForObject(StationContants.GET_USER_STATION_BY_USERID,params);
		} catch (SQLException e) {
			logger.error("Exception in getActiveStationInfo : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
}
