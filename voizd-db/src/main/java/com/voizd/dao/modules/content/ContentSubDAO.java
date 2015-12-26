package com.voizd.dao.modules.content;

import java.util.List;

import com.voizd.common.beans.vo.ContentFollowerVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface ContentSubDAO {
	
	public Byte getContentTap(Long contentId,Long follwerId) throws DataAccessFailedException;
	public Byte getContentTap(Long contentId,Long streamId,Long follwerId) throws DataAccessFailedException;
	public void saveContentTap(Long contentId,Long streamId,Long follwerId) throws DataUpdateFailedException;
	public void updateContentTap(Long contentId,Long streamId, Long follwerId,Byte status)throws DataUpdateFailedException;
	public List<ContentFollowerVO> getTappedContentList(Long follwerId,int startLimit,int endLimit) throws DataAccessFailedException;
	
}
