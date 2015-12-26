package com.voizd.dao.modules.content;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.ContentConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class ContentDAOImpl extends AbstractDBManager implements ContentDAO {
	Logger logger = LoggerFactory.getLogger(ContentDAOImpl.class);

	@Override
	public Long createContent(ContentVO contentVO) throws DataUpdateFailedException {
		try {
			
			return (Long) sqlMapClient_.insert(ContentConstants.INSERT_CONTENT_INFO, contentVO);
			
		} catch (SQLException e) {
			logger.error("Exception in createContent : ", e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public ContentVO getContent(Long contentId, Long stationId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("stationId", stationId);
			params.put("status", VoizdConstant.CONTENT_ACTIVE_STATUS);
			return (ContentVO) sqlMapClient_.queryForObject(ContentConstants.GET_CONTENT_INFO, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getContent : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentVO> getContentList(Long stationId,Byte status,int endLimit) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status", status);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_CONTENT_LIST_INFO, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getContent : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getContentListAsc(Long stationId,Long contentId,Byte status,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status", status);
			params.put("endLimit", endLimit);
			params.put("contentId", contentId);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_CONTENT_LIST_INFO_MORE_ASC, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getContent : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getContentListDesc(Long stationId,Long contentId,Byte status,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status", status);
			params.put("endLimit", endLimit);
			params.put("contentId", contentId);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_CONTENT_LIST_INFO_MORE_DESC, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getContent : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentVO> getRecentContentList(int startLimit,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", VoizdConstant.CONTENT_ACTIVE_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_RECENT_CONTENT_LIST, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getRecentContentList : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getRecentContentListDesc(Long contentId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.CONTENT_ACTIVE_STATUS);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_RECENT_CONTENT_LIST_MORE_DESC, params);
		} catch (SQLException e) {
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getRecentContentListAsc(Long contentId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.CONTENT_ACTIVE_STATUS);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_RECENT_CONTENT_LIST_MORE_ASC, params);
		} catch (SQLException e) {
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getMyContentList(Long creatorId,int startLimit,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creatorId", creatorId);
			params.put("status", VoizdConstant.CONTENT_DELETED_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClient_.queryForList(ContentConstants.GET_MY_CONTENT_LIST, params);
		} catch (SQLException e) {
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getMyContentListDesc(Long contentId,Long creatorId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creatorId", creatorId);
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.CONTENT_DELETED_STATUS);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_MY_CONTENT_LIST_MORE_DESC, params);
		} catch (SQLException e) {
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentVO> getMyContentListAsc(Long contentId,Long creatorId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creatorId", creatorId);
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.CONTENT_DELETED_STATUS);
			params.put("endLimit", endLimit);
			return (List<ContentVO>) sqlMapClientSlave_.queryForList(ContentConstants.GET_MY_CONTENT_LIST_MORE_ASC, params);
		} catch (SQLException e) {
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	public ContentVO getContentById(Long contentId,Byte status) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("status", status);
			return (ContentVO) sqlMapClient_.queryForObject(ContentConstants.GET_CONTENT_BY_ID, params);
		} catch (SQLException e) {
			logger.error("Exception in geting getContent : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
@Override
	public void updateContentTag(String tag,Long contentId) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tag", tag);
			params.put("contentId",contentId);
			sqlMapClient_.update(ContentConstants.UPDATE_CONTENT_TAG_INFO,params);
		} catch (SQLException e) {
			logger.error("Exception in updateContentTag : "+e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateContentTagAndPicture(String tag, String fileId,Long contentId) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tag", tag);
			params.put("contentId",contentId);
			sqlMapClient_.update(ContentConstants.UPDATE_CONTENT_TAG_AND_PICTURE, params);
		} catch (SQLException e) {
			logger.error("Exception in updateContentTagAndPicture : ", e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}	
	}

	@Override
	public ContentVO getContentByContentId(Long contentId) throws DataAccessFailedException {
		try {
			return (ContentVO) sqlMapClient_.queryForObject(ContentConstants.GET_CONTENT_INFO_BY_CONTENT_ID,contentId);
		} catch (SQLException e) {
			logger.error("Exception in geting getContentById : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public int updateContentStatusByContentId(Long contentId,Long stationId,Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("stationId", stationId);
			params.put("userId", userId);
			params.put("status",status);
			return sqlMapClient_.update(ContentConstants.UPDATE_CONTENT_STATUS_BY_CONTENT_ID, params);
		} catch (SQLException e) {
			logger.error("Exception in updateContentByContentId : ", e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TapClipVO> getRecentContentListDesc(Byte status, int startLimit, int endLimit) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", status);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return sqlMapClientSlave_.queryForList(ContentConstants.GET_RECENT_CONTENT_INFO_BY_STATUS, params);
		} catch (SQLException e) {
			logger.error("Exception in getRecentContentListDesc : "+e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
}
