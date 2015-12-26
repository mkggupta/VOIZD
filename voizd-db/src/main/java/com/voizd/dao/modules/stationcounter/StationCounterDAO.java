/**
 * 
 */
package com.voizd.dao.modules.stationcounter;

import java.util.List;

import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author arvind
 *
 */
public interface StationCounterDAO {
	public void createStationCounter(Long stationId) throws DataUpdateFailedException;
	public void updateStationCounter(StationCounterVO stationCounterVO) throws DataUpdateFailedException;
	public StationCounterVO getStationCounter(Long stationId) throws DataAccessFailedException;
	public List<StationCounterVO> getStationCounterList(int startLimit,int endLimit) throws DataAccessFailedException;

}
