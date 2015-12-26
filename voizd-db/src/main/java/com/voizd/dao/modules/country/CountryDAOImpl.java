package com.voizd.dao.modules.country;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.CountryConstants;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class CountryDAOImpl extends AbstractDBManager implements CountryDAO{
	private static Logger logger = LoggerFactory.getLogger(CountryDAOImpl.class);

	@Override
	public String getUserCountry(String code) throws DataAccessFailedException  {
			try {
				return (String) sqlMapClientSlave_.queryForObject(CountryConstants.GET_USER_COUNTRY_BY_CODE,code);
			} catch (SQLException e) {
				logger.error("Exception in getUserCountry : " + e.getMessage());
				throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@Override
	public String getUserLanguage(String code) throws DataAccessFailedException {
		try {
			return (String) sqlMapClientSlave_.queryForObject(CountryConstants.GET_USER_LANGUAGE_BY_CODE,code);
		} catch (SQLException e) {
			logger.error("Exception in getUserLanguage : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
}
