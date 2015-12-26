package com.voizd.service.user.action.v1_0;

import com.voizd.service.user.action.exception.UserActionServiceFailedException;
import com.voizd.service.user.action.manager.UserActionManager;

public class UserActionServiceImpl implements UserActionService {

	private UserActionManager userActionManager;
	@Override
	public void tapStation(Long stationId,Long streamId, Long follwerId, Byte status)throws UserActionServiceFailedException {
		userActionManager.tapStation(stationId,streamId,follwerId,status);
		
	}
	public void setUserActionManager(UserActionManager userActionManager) {
		this.userActionManager = userActionManager;
	}
	@Override
	public void likeOperation(Long id, Long userId,Byte status,String type) throws UserActionServiceFailedException {
		userActionManager.userLikeOperation(id, userId, status,type);
	}
	public void tapContent(Long contentId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException{
		userActionManager.tapContent(contentId,streamId,follwerId,status);
	}
	public void tapStation(Long creatorId, Long follwerId, Byte status)throws UserActionServiceFailedException{
		userActionManager.tapStation(creatorId,follwerId,status);
	}

	
}
