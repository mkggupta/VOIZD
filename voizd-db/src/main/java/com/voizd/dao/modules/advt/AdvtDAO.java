package com.voizd.dao.modules.advt;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.dao.exception.DataAccessFailedException;

public interface AdvtDAO {
	
	public AdvtMicInfo getMicAdvt()throws DataAccessFailedException;

}
