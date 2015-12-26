package com.voizd.service.user.action.manager;

import com.voizd.service.user.action.bo.UserActionBO;
import com.voizd.service.user.action.exception.UserActionServiceFailedException;

public class UserActionManagerImpl implements UserActionManager {

	private UserActionBO  userActionBO;
	@Override
	public void tapStation(Long stationId,Long streamId, Long follwerId, Byte status)throws UserActionServiceFailedException {
		userActionBO.tapStation(stationId,streamId,follwerId,status);
		
	}
	public void setUserActionBO(UserActionBO userActionBO) {
		this.userActionBO = userActionBO;
	}
	@Override
	public void userLikeOperation(Long id, Long userId,Byte status,String type) throws UserActionServiceFailedException {
		userActionBO.userLikeOperation(id, userId, status,type);
	}
	
	public void tapContent(Long contentId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException{
		userActionBO.tapContent(contentId,streamId,follwerId,status);
	}
	public void tapStation(Long creatorId, Long follwerId, Byte status)throws UserActionServiceFailedException{
		userActionBO.tapStation(creatorId,follwerId,status);
	}

	
}
