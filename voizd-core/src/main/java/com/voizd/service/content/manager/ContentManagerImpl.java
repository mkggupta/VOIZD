package com.voizd.service.content.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.VStreamVO;
import com.voizd.service.content.bo.ContentBO;
import com.voizd.service.content.exception.ContentServiceException;

public class ContentManagerImpl implements ContentManager {
	private ContentBO  contentBO ;
	public HashMap<String, Object> createStationContent(ContentVO contentVO) throws ContentServiceException {
		return contentBO.createStationContent(contentVO);
	}
	public HashMap<String, Object> getStationContents(Long stationId,Long contentId,Long userId, Byte status, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		return contentBO.getStationContents(stationId,contentId,userId,status,endLimit,order,clientMap);
	}
	public void setContentBO(ContentBO contentBO) {
		this.contentBO = contentBO;
	}
	@Override
	public HashMap<String, Object> getStationContentList(Long contentId,Long userId, Long visitorId,Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException{
		return contentBO.getStationContentList(contentId,userId,visitorId,status,startLimit,endLimit,order,clientMap);
	}
    @Override
	public void updateStationContent(String tag,String fileId,Long contentId,Long userId) throws ContentServiceException {
		contentBO.updateStationContent(tag,fileId,contentId,userId);
	}
	@Override
	public void deleteStationContent(Long stationId,Long contentId, Long userId) throws ContentServiceException {
		contentBO.deleteStationContent(stationId,contentId, userId);
	}
	
	public HashMap<String, Object> getStationContentDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
		return contentBO.getStationContentDetail(stationId,contentId,userId,clientMap);
	}
	public HashMap<String, Object> getVoizDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException {
		return contentBO.getVoizDetail(stationId,contentId,userId,clientMap);
	}
	public HashMap<String, Object> getContentShareUrl(Long stationId,Long contentId,Byte appId,Long userId)throws ContentServiceException {
		return contentBO.getContentShareUrl(stationId,contentId,appId,userId);
	}
	public HashMap<String, Object> getMyBookMarkContentList(Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException {
		return contentBO.getMyBookMarkContentList(userId,visitorId,status,startLimit,endLimit,order,clientMap);
	}
	@Override
	public HashMap<String, Object> getUserVStreamContents(VStreamVO vStreamVO,Map<String, Object> clientMap) throws ContentServiceException {
		return contentBO.getUserVStreamContents(vStreamVO, clientMap);
	}
	@Override
	public HashMap<String, Object> getRecentVStreamContents(VStreamVO vStreamVO, Map<String, Object> clientMap) throws ContentServiceException {
		return contentBO.getRecentVStreamContents(vStreamVO, clientMap);
	}
	
}
