/**
 * 
 */
package com.voizd.dao.modules.content;

import java.util.List;

import com.voizd.common.beans.vo.ContentLikeVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author arvind
 *
 */
public interface ContentLikeDAO {
	public void insertContentLike(Long contentId, Long userId,Byte status) throws DataUpdateFailedException;
	public void updateContentLikeStatus(Long contentId, Long userId,Byte status) throws DataUpdateFailedException;
	public Byte getUserContentLike(Long contentId, Long userId) throws DataAccessFailedException;
	public ContentLikeVO userContentLikeStatus(Long contentId, Long userId) throws DataAccessFailedException;
	public List<ContentLikeVO> getLikedContentList(Long userId,int startLimit,int endLimit) throws DataAccessFailedException;
}
