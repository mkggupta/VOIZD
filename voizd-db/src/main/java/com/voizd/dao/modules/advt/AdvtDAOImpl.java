package com.voizd.dao.modules.advt;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.dao.constants.AdvtConstant;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class AdvtDAOImpl extends AbstractDBManager implements AdvtDAO {
	private static Logger logger = LoggerFactory.getLogger(AdvtDAOImpl.class);
	
	public AdvtMicInfo getMicAdvt()throws DataAccessFailedException{
		try {
			return (AdvtMicInfo) sqlMapClientSlave_.queryForObject(AdvtConstant.GET_MIC_ADVT_INFO);
		} catch (SQLException e) {
			logger.error("Exception in getting getMicAdvt : " + e.getLocalizedMessage(), e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

}
