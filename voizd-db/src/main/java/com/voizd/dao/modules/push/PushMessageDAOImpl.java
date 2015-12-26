package com.voizd.dao.modules.push;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.PushMessageVO;
import com.voizd.dao.constants.PushMessageConstants;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class PushMessageDAOImpl extends AbstractDBManager implements PushMessageDAO {
	private static Logger logger = LoggerFactory.getLogger(PushMessageDAOImpl.class);
	
	@Override
	public void createPushMessage(PushMessageVO pushMessageVO)
			throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(PushMessageConstants.INSERT_MESSAGE_MESSAGE_INFO,pushMessageVO);
			}catch(SQLException e){
				logger.error("Exception in createPushMessage : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}
}
