package com.voizd.dao.modules.stats;

import com.voizd.common.beans.vo.StatsVO;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface ClientStatsDAO {
	
	public void saveListenStats(StatsVO statsVO) throws DataUpdateFailedException;
	public void saveShareStats(StatsVO statsVO) throws DataUpdateFailedException;
	public void saveCommentListenStats(StatsVO statsVO) throws DataUpdateFailedException;

}
