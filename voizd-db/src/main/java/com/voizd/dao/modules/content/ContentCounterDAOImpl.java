/**
 * 
 */
package com.voizd.dao.modules.content;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.dao.constants.ContentCounterContants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author arvind
 *
 */
public class ContentCounterDAOImpl extends AbstractDBManager implements ContentCounterDAO{
	private static Logger logger = LoggerFactory.getLogger(ContentCounterDAOImpl.class);
	
	@Override
	public void createContentCounter(Long contentId) throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(ContentCounterContants.INSERT_CONTENT_COUNTER,contentId);
		} catch (SQLException e) {
			logger.error("Exception in createContentCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateContentCounter(ContentCounterVO contentCounterVO) throws DataUpdateFailedException {
		try {
			sqlMapClient_.update(ContentCounterContants.UPDATE_CONTENT_COUNTER,contentCounterVO);
		} catch (SQLException e) {
			logger.error("Exception in updateContentCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public ContentCounterVO getContentCounter(Long contentId) throws DataAccessFailedException {
		try {
			return (ContentCounterVO) sqlMapClient_.queryForObject(ContentCounterContants.GET_CONTENT_COUNTER,contentId);
		} catch (SQLException e) {
			logger.error("Exception in getContentCounter : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentCounterVO> getContentCounterList(int startLimit,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<ContentCounterVO>) sqlMapClientSlave_.queryForList(ContentCounterContants.GET_CONTENT_COUNTER_LIST,params);
		} catch (SQLException e) {
			logger.error("Exception in getContentCounterList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentCounterVO> getContentCounterListDesc(Long contentId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("endLimit", endLimit);
			return (List<ContentCounterVO>) sqlMapClientSlave_.queryForList(ContentCounterContants.GET_CONTENT_COUNTER_LIST_MORE_DESC,params);
		} catch (SQLException e) {
			logger.error("Exception in getContentCounterList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ContentCounterVO> getContentCounterListAsc(Long contentId,int endLimit) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("endLimit", endLimit);
			return (List<ContentCounterVO>) sqlMapClientSlave_.queryForList(ContentCounterContants.GET_CONTENT_COUNTER_LIST_MORE_ASC,params);
		} catch (SQLException e) {
			logger.error("Exception in getContentCounterList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
