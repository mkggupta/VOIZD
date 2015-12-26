package com.voizd.dao.modules.station;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StationLikeVO;
import com.voizd.dao.constants.StationLikeConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class StationLikeDAOImlp extends AbstractDBManager implements StationLikeDAO {
	private static Logger logger = LoggerFactory.getLogger(StationLikeDAOImlp.class);

	@Override
	public void insertStationLike(Long stationId, Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.insert(StationLikeConstants.INSERT_STATION_LIKE_INFO, params);
		} catch (Exception e) {
			logger.error("Exception in insertStationLike : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}

	}

	@Override
	public Byte getUserStationLike(Long stationId, Long userId) throws DataAccessFailedException {
	
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("userId", userId);
			return (Byte) sqlMapClient_.queryForObject(StationLikeConstants.GET_USER_STATION_LIKE_EXISTS, params);
			
		} catch (Exception e) {
			logger.error("Exception in isUserStationLikeExits : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public void updateStationLikeStatus(Long stationId, Long userId, Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.update(StationLikeConstants.UPDATE_STATION_LIKE_STATUS, params);
		} catch (Exception e) {
			logger.error("Exception in updateStationLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public StationLikeVO userStationLikeStatus(Long stationId, Long userId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("userId", userId);
			return  (StationLikeVO) sqlMapClient_.queryForObject(StationLikeConstants.USER_STATION_STATUS_VALUE, params);
		} catch (Exception e) {
			logger.error("Exception in userStationLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	
	}

}
