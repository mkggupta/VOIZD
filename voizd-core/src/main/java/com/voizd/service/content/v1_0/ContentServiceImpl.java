package com.voizd.service.content.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.VStreamVO;
import com.voizd.service.content.exception.ContentServiceException;
import com.voizd.service.content.manager.ContentManager;

public class ContentServiceImpl implements ContentService {
	private ContentManager contentManager;
	public HashMap<String, Object> createStationContent(ContentVO contentVO) throws ContentServiceException {
		return contentManager.createStationContent(contentVO);
	}
	
	public void setContentManager(ContentManager contentManager) {
		this.contentManager = contentManager;
	}

	@Override
	public HashMap<String, Object> getStationContents(Long stationId,
			Long contentId, Long userId, Byte status, int endLimit,
			boolean order,Map<String, Object> clientMap) throws ContentServiceException{
		return contentManager.getStationContents(stationId,contentId,userId,status,endLimit,order,clientMap);
	}
	public HashMap<String, Object> getStationContentList(Long contentId,Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		
		return contentManager.getStationContentList(contentId,userId,visitorId,status,startLimit,endLimit,order,clientMap);
	}
		@Override
	public void updateStationContent(String tag,String fileId,Long contentId,Long userId) throws ContentServiceException {
		contentManager.updateStationContent(tag,fileId,contentId,userId);
	}

	@Override
	public void deleteStationContent(Long stationId,Long contentId, Long userId) throws ContentServiceException {
		contentManager.deleteStationContent(stationId,contentId, userId);
	}
	
	public HashMap<String, Object> getStationContentDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
		return contentManager.getStationContentDetail(stationId,contentId,userId,clientMap);
	}
	public HashMap<String, Object> getVoizDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
		return contentManager.getVoizDetail(stationId,contentId,userId,clientMap);
	}
	public HashMap<String, Object> getContentShareUrl(Long stationId,Long contentId,Byte appId,Long userId)throws ContentServiceException {
		return contentManager.getContentShareUrl(stationId,contentId,appId,userId);
	}
	public HashMap<String, Object> getMyBookMarkContentList(Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		return contentManager.getMyBookMarkContentList(userId,visitorId,status,startLimit,endLimit,order,clientMap);
	}

	@Override
	public HashMap<String, Object> getUserVStreamContents(VStreamVO vStreamVO,Map<String, Object> clientMap) throws ContentServiceException {
		return contentManager.getUserVStreamContents(vStreamVO, clientMap);
	}

	@Override
	public HashMap<String, Object> getRecentVStreamContents(VStreamVO vStreamVO, Map<String, Object> clientMap) throws ContentServiceException {
		return contentManager.getRecentVStreamContents(vStreamVO, clientMap);
	}
}
