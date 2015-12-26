package com.voizd.service.user.action.bo;

import com.voizd.service.user.action.exception.UserActionServiceFailedException;

public interface UserActionBO {
	public void tapStation(Long stationId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException;
	public void userLikeOperation(Long id,Long userId,Byte status,String type) throws UserActionServiceFailedException;
	public void tapContent(Long contentId,Long streamId,Long follwerId,Byte status) throws UserActionServiceFailedException;
	public void tapStation(Long creatorId, Long follwerId, Byte status) throws UserActionServiceFailedException;
	
}
