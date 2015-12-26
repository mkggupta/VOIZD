package com.voizd.service.user.action.manager;

import com.voizd.service.user.action.exception.UserActionServiceFailedException;

public interface UserActionManager {

	public void tapStation(Long stationId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException;
	public void userLikeOperation(Long id,Long userId,Byte status,String type) throws UserActionServiceFailedException;
	public void tapContent(Long contentId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException;
	public void tapStation(Long creatorId, Long follwerId, Byte status)throws UserActionServiceFailedException;
	
}
