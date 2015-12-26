/**
 * 
 */
package com.voizd.dao.modules.stationcounter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.StationCounterContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.common.beans.vo.StationCounterVO;
/**
 * @author arvind
 *
 */
public class StationCounterDAOImpl extends AbstractDBManager implements StationCounterDAO {
	private static Logger logger = LoggerFactory.getLogger(StationCounterDAOImpl.class);
	
	@Override
	public void createStationCounter(Long stationId) throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(StationCounterContants.INSERT_STATION_COUNTER,stationId);
		} catch (SQLException e) {
			logger.error("Exception in createStationCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	public void updateStationCounter(StationCounterVO stationCounterVO) throws DataUpdateFailedException{
		try {
			sqlMapClient_.update(StationCounterContants.UPDATE_STATION_COUNTER,stationCounterVO);
		} catch (SQLException e) {
			logger.error("Exception in updateStationCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	public StationCounterVO getStationCounter(Long stationId) throws DataAccessFailedException{
		try {
			return (StationCounterVO) sqlMapClient_.queryForObject(StationCounterContants.GET_STATION_COUNTER,stationId);
		} catch (SQLException e) {
			logger.error("Exception in getStationCounter : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<StationCounterVO> getStationCounterList(int startLimit,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
		return (List<StationCounterVO>) sqlMapClient_.queryForList(StationCounterContants.GET_STATION_COUNTER_LIST,params);
	} catch (SQLException e) {
		logger.error("Exception in getStationCounterList : " + e.getMessage());
		throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
	}
  }
	



}
