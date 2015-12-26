package com.voizd.dao.modules.content;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentFollowerVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.ContentSubContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class ContentSubDAOImpl extends AbstractDBManager implements ContentSubDAO {
	private static Logger logger = LoggerFactory.getLogger(ContentSubDAOImpl.class);
	@Override
	public Byte getContentTap(Long contentId,Long follwerId) throws DataAccessFailedException {
		
		  try{
		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("follwerId", follwerId);
			return (Byte)sqlMapClient_.queryForObject(ContentSubContants.GET_CONTENT_FOLLOWER_INFO, params);
		  }catch(SQLException e){
				logger.error("Exception in getStationTap : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
	}

	@Override
	public Byte getContentTap(Long contentId, Long streamId, Long follwerId)
			throws DataAccessFailedException {
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("contentId", contentId);
				params.put("follwerId", follwerId);
				params.put("streamId", streamId);
				return (Byte)sqlMapClient_.queryForObject(ContentSubContants.GET_CONTENT_FOLLOWER_INFO_BY_STREAM, params);
			  }catch(SQLException e){
					logger.error("Exception in getStationTap : " + e.getMessage());
					throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			 }
	}

	@Override
	public void saveContentTap(Long contentId,Long streamId,Long follwerId) throws DataUpdateFailedException {
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("contentId", contentId);
				params.put("follwerId", follwerId);
				params.put("streamId", streamId);
				sqlMapClient_.insert(ContentSubContants.INSERT_CONTENT_FOLLOWER_INFO, params);
			  }catch(SQLException e){
					logger.error("Exception in saveStationTap : " + e.getMessage());
					throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public void updateContentTap(Long contentId,Long streamId, Long follwerId,Byte status)throws DataUpdateFailedException {
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("contentId", contentId);
				params.put("follwerId", follwerId);
				params.put("status", status);
				params.put("streamId", streamId);	
				sqlMapClient_.update(ContentSubContants.UPDATE_CONTENT_FOLLOWER_INFO, params);
			  }catch(SQLException e){
					logger.error("Exception in saveStationTap : " + e.getMessage());
					throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentFollowerVO> getTappedContentList(Long follwerId,int startLimit,int endLimit) throws DataAccessFailedException{
		
		 try{
			  Map<String, Object> params = new HashMap<String, Object>();
				params.put("follwerId", follwerId);
				params.put("status", VoizdConstant.CONTENT_FOLLOW_STATUS);
				params.put("startLimit", startLimit);
				params.put("endLimit", endLimit);
				return (List<ContentFollowerVO>)sqlMapClient_.queryForList(ContentSubContants.GET_CONTENT_FOLLOWED_BY_ME, params);
			  }catch(SQLException e){
					logger.error("Exception in getTappedStationList : " + e.getMessage());
					throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		 }
		
	}


	

}
