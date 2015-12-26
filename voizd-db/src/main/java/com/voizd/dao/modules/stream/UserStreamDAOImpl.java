package com.voizd.dao.modules.stream;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.UserStreamContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;



public class UserStreamDAOImpl extends AbstractDBManager implements UserStreamDAO {
	private static Logger logger = LoggerFactory.getLogger(UserStreamDAOImpl.class);
	@Override
	public Long createStream(StreamVO streamVO)throws DataUpdateFailedException {
		try {
			return (Long) sqlMapClient_.insert(UserStreamContants.INSERT_STREAM_INFO,streamVO);
			}catch(SQLException e){
				logger.error("Exception in createStation : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StreamVO> getMyStreamList(Long userId, int startLimit,int endLimit) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("status", VoizdConstant.DELETED_STREAM_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
		return (List<StreamVO>) sqlMapClientSlave_.queryForList(UserStreamContants.GET_MY_STREAM_LIST,params);
	} catch (SQLException e) {
		logger.error("Exception in getMyStreamList : " + e.getMessage());
		throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
	}
	}

	@Override
	public StreamVO getStreamInfo(Long streamId)throws DataAccessFailedException {
		try {
			return (StreamVO) sqlMapClient_.queryForObject(UserStreamContants.GET_STREAM_INFO,streamId);
		} catch (SQLException e) {
			logger.error("Exception in getStreamInfo : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public StreamVO getStreamInfoByUserId(Long userId, Long streamId)throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("streamId", streamId);
			params.put("status", VoizdConstant.DELETED_STREAM_STATUS);
			logger.debug("getStreamInfoByUserId :streamId " + streamId+" userId "+ userId+" "+VoizdConstant.DELETED_STREAM_STATUS);
		   return (StreamVO) sqlMapClient_.queryForObject(UserStreamContants.GET_STREAM_DETAIL_BY_USERID,params);
		} catch (SQLException e) {
			logger.error("Exception in getStreamInfoByUserId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StreamVO> getUserStreamList(Long userId)throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", VoizdConstant.DELETED_STREAM_STATUS);
			params.put("userId", userId);
		 return (List<StreamVO>) sqlMapClient_.queryForList(UserStreamContants.GET_USER_STREAMS,params);
		} catch (SQLException e) {
			logger.error("Exception in getUserStationList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public Boolean streamAlreadyExists(String streamName, Long userId)throws DataAccessFailedException {
		  boolean stationAlreadyExists = false;
		  try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamName", streamName);
			params.put("userId", userId);
			Integer count = (Integer)sqlMapClient_.queryForObject(UserStreamContants.GET_STREAM_ALREADY_EXT_FOR_CREATOR_ID, params);
			logger.debug(" count="+count);
			if (count != null) {
				stationAlreadyExists = count > 0 ? true : false;
			} 
			
		    }catch(SQLException e){
				logger.error("Exception in streamAlreadyExists : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
		    logger.debug(" streamAlreadyExists="+stationAlreadyExists);
			return stationAlreadyExists;
	}

	@Override
	public void updateStream(StreamVO streamVO)throws DataUpdateFailedException {
		try {
			  sqlMapClient_.update(UserStreamContants.UPDATE_STREAM_INFO,streamVO);
		} catch (SQLException e) {
			logger.error("Exception in updateStream : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateStreamStatus(Long userId, Long streamId, Byte status,Boolean hasContent)throws DataUpdateFailedException {
		try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				params.put("streamId", streamId);
				params.put("status", status);
				params.put("hasContent",hasContent);
			  sqlMapClient_.update(UserStreamContants.UPDATE_STREAM_STATUS,params);
		} catch (SQLException e) {
			logger.error("Exception in updateStream : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}

	}

}
