package com.voizd.dao.modules.content;

import java.util.List;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface ContentDAO {
	public Long createContent(ContentVO content) throws DataUpdateFailedException;
	public ContentVO getContent(Long contentId,Long stationId) throws DataAccessFailedException;
	public List<ContentVO> getContentList(Long stationId,Byte status,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getContentListAsc(Long stationId,Long contentId,Byte status,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getContentListDesc(Long stationId,Long contentId,Byte status,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getRecentContentList(int startLimit,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getRecentContentListDesc(Long contentId,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getRecentContentListAsc(Long contentId,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getMyContentList(Long creatorId,int startLimit,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getMyContentListDesc(Long contentId,Long creatorId,int endLimit) throws DataAccessFailedException;
	public List<ContentVO> getMyContentListAsc(Long contentId,Long creatorId,int endLimit) throws DataAccessFailedException;
	public ContentVO getContentById(Long contentId,Byte status) throws DataAccessFailedException;
	public ContentVO getContentByContentId(Long contentId) throws DataAccessFailedException;
	public void updateContentTag(String tag,Long contentId) throws DataUpdateFailedException;
	public void updateContentTagAndPicture(String tag,String fileId,Long contentId) throws DataUpdateFailedException;
	public int updateContentStatusByContentId(Long contentId,Long stationId,Long userId,Byte status) throws DataUpdateFailedException;
	public List<TapClipVO> getRecentContentListDesc(Byte status,int startLimit,int endLimit) throws DataAccessFailedException;
}
