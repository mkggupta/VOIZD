package com.voizd.dao.modules.user;

import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface UserCounterDAO {
	public void createUserCounter(Long userId) throws DataUpdateFailedException;
	public void updateUserCounter(UserCounterVO userCounterVO) throws DataUpdateFailedException;
	public UserCounterVO getUserCounter(Long userId) throws DataAccessFailedException;

}
