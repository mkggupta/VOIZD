package com.voizd.service.search.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.SearchVO;
import com.voizd.service.search.bo.SearchBO;
import com.voizd.service.search.exception.SearchServiceException;

public class SearchServiceImpl implements SearchService {
    public void setSearchBO(SearchBO searchBO) {
		this.searchBO = searchBO;
	}

	private SearchBO searchBO;


	@Override
	public HashMap<String, Object> getStationSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException {
		return searchBO.getStationSearch(searchVO,startLimit,pageLimit,clientMap);
	}


	@Override
	public HashMap<String, Object> getContentSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException {
		return searchBO.getContentSearch(searchVO,startLimit,pageLimit,clientMap);
	}
	

}
