/**
 * 
 */
package com.voizd.dao.modules.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentLikeVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.ContentLikeConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author arvind
 *
 */
public class ContentLikeDAOimpl extends AbstractDBManager implements ContentLikeDAO {
	Logger logger = LoggerFactory.getLogger(ContentLikeDAOimpl.class);
	@Override
	public void insertContentLike(Long contentId, Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.insert(ContentLikeConstants.INSERT_CONTENT_LIKE_INFO, params);
		} catch (Exception e) {
			logger.error("Exception in insertContentLike : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateContentLikeStatus(Long contentId, Long userId,Byte status) throws DataUpdateFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("userId", userId);
			params.put("like", status);
			sqlMapClient_.update(ContentLikeConstants.UPDATE_CONTENT_LIKE_STATUS, params);
		} catch (Exception e) {
			logger.error("Exception in updateContentLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public Byte getUserContentLike(Long contentId, Long userId) throws DataAccessFailedException {
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("userId", userId);
			return (Byte)sqlMapClient_.queryForObject(ContentLikeConstants.GET_USER_CONTENT_LIKE_EXISTS, params);
		} catch (Exception e) {
			logger.error("Exception in isUserContentLikeExits : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@Override
	public ContentLikeVO userContentLikeStatus(Long contentId, Long userId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("userId", userId);
			return (ContentLikeVO) sqlMapClient_.queryForObject(ContentLikeConstants.GET_USER_CONTENT_LIKE_STATUS, params);
		} catch (Exception e) {
			logger.error("Exception in userContentLikeStatus : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentLikeVO> getLikedContentList(Long userId,int startLimit,int endLimit) throws DataAccessFailedException{
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("like", VoizdConstant.CONTENT_LIKE_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<ContentLikeVO>) sqlMapClientSlave_.queryForList(ContentLikeConstants.GET_USER_LIKED_CONTENT, params);
		} catch (Exception e) {
			logger.error("Exception in getLikedContentList : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
