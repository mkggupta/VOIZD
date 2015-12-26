/**
 * 
 */
package com.voizd.service.search.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.SearchVO;
import com.voizd.service.search.exception.SearchServiceException;


/**
 * @author arvind
 *
 */
public interface SearchService {
	public HashMap<String, Object> getStationSearch(SearchVO searchVO,int pageLimit,int endLimit,Map<String, Object> clientMap) throws SearchServiceException;
	public HashMap<String, Object> getContentSearch(SearchVO searchVO,int pageLimit,int endLimit,Map<String, Object> clientMap) throws SearchServiceException;
}
