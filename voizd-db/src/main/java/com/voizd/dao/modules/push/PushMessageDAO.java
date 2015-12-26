package com.voizd.dao.modules.push;


import com.voizd.common.beans.vo.PushMessageVO;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface PushMessageDAO {
	public void createPushMessage(PushMessageVO pushMessageVO) throws DataUpdateFailedException;
}
