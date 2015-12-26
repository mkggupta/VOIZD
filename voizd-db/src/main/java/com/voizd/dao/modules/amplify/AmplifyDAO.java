package com.voizd.dao.modules.amplify;



import java.util.List;

import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface AmplifyDAO {
	
	public void createAmplifyInfo(AmplifyInfoVO amplifyInfoVO) throws DataUpdateFailedException;
	public void updateAmplifyInfo(AmplifyInfoVO amplifyInfoVO) throws DataUpdateFailedException;
	public AmplifyInfoVO getAmplifyInfo(Long contentId,Long userId) throws DataAccessFailedException;
	public List<AmplifyInfoVO> getAmplifierList(Long contentId,int startLimit,int endLimit) throws DataAccessFailedException;

}
