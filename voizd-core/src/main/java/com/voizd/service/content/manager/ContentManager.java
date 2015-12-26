package com.voizd.service.content.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.VStreamVO;
import com.voizd.service.content.exception.ContentServiceException;

public interface ContentManager {
	public HashMap<String, Object> createStationContent(ContentVO contentVO) throws ContentServiceException ;
	public HashMap<String, Object> getStationContents(Long stationId,Long contentId,Long userId, Byte status, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException ;
	public HashMap<String, Object> getStationContentList(Long contentId,Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException ;
	public void updateStationContent(String tag,String fileId,Long contentId,Long userId) throws ContentServiceException; 
	public void deleteStationContent(Long stationId,Long contentId,Long userId) throws ContentServiceException; 
	public HashMap<String, Object> getStationContentDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException ;
	public HashMap<String, Object> getVoizDetail(Long stationId,Long contentId,Long userId,Map<String, Object> clientMap)throws ContentServiceException ;
	public HashMap<String, Object> getContentShareUrl(Long stationId,Long contentId,Byte appId,Long userId)throws ContentServiceException ;
	public HashMap<String, Object> getMyBookMarkContentList(Long userId,Long visitorId, Byte status, int startLimit, int endLimit, boolean order,Map<String, Object> clientMap)throws ContentServiceException ;
	public HashMap<String, Object> getUserVStreamContents(VStreamVO vStreamVO,Map<String, Object> clientMap)throws ContentServiceException ;
	public HashMap<String, Object> getRecentVStreamContents(VStreamVO vStreamVO,Map<String, Object> clientMap)throws ContentServiceException ;
}
