package com.voizd.dao.modules.country;

import com.voizd.dao.exception.DataAccessFailedException;

public interface CountryDAO {
	public String getUserCountry(String code) throws DataAccessFailedException;
	public String getUserLanguage(String code) throws DataAccessFailedException;
}
