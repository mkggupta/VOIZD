package com.voizd.dao.modules.amplify;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.AmplifyConstant;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class AmplifyDAOImpl extends AbstractDBManager implements AmplifyDAO {
	private static Logger logger = LoggerFactory.getLogger(AmplifyDAOImpl.class);
	@Override
	public void createAmplifyInfo(AmplifyInfoVO amplifyInfoVO)throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(AmplifyConstant.INSERT_AMPLIFY_INFO,amplifyInfoVO);
		} catch (SQLException e) {
			if(e instanceof MySQLIntegrityConstraintViolationException | e instanceof NestedSQLException){
				//logger.error("Exception in createContentCounter : " + e.getMessage()); 
				//not log anything
			}else{
				logger.error("Exception in createContentCounter : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
			
		}
	}

	@Override
	public AmplifyInfoVO getAmplifyInfo(Long contentId, Long userId)throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("amplifierId", userId);
			return (AmplifyInfoVO) sqlMapClient_.queryForObject(AmplifyConstant.GET_AMPLIFY_INFO,params);
		} catch (SQLException e) {
			logger.error("Exception in getAmplifyInfo : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateAmplifyInfo(AmplifyInfoVO amplifyInfoVO)throws DataUpdateFailedException {
		try {
			sqlMapClient_.update(AmplifyConstant.UPDATE_AMPLIFY_INFO,amplifyInfoVO);
		} catch (SQLException e) {
			logger.error("Exception in updateContentCounter : " + e.getMessage());
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmplifyInfoVO> getAmplifierList(Long contentId,int startLimit,int endLimit) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.ACTIVE_AMPLIFY_STATUS);
			params.put("startLimit", startLimit);
			params.put("endLimit", endLimit);
			return (List<AmplifyInfoVO>) sqlMapClientSlave_.queryForList(AmplifyConstant.GET_AMPLIFIER_INFO,params);
		} catch (SQLException e) {
			logger.error("Exception in getAmplifierList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
