/**
 * 
 */
package com.voizd.dao.modules.content;

import java.util.List;

import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author arvind
 *
 */
public interface ContentCounterDAO {
	public void createContentCounter(Long contentId) throws DataUpdateFailedException;
	public void updateContentCounter(ContentCounterVO contentCounterVO) throws DataUpdateFailedException;
	public ContentCounterVO getContentCounter(Long contentId) throws DataAccessFailedException;
	public List<ContentCounterVO> getContentCounterList(int startLimit,int endLimit) throws DataAccessFailedException;
	public List<ContentCounterVO> getContentCounterListDesc(Long contentId,int endLimit) throws DataAccessFailedException;
	public List<ContentCounterVO> getContentCounterListAsc(Long contentId,int endLimit) throws DataAccessFailedException;
}
