package com.voizd.service.search.bo;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.service.search.exception.SearchServiceException;

public interface SearchBO {
	public void indexStationMedia(StationSearchVO stationSearchVO, boolean refresh,String type) throws SearchServiceException;
	public void indexContentMedia(ContentSearchVO contentSearchVO, boolean refresh,String type) throws SearchServiceException;
	public void indexUpdateStationMedia(StationSearchVO stationSearchVO, boolean refresh,String type) throws SearchServiceException;
	public void indexUpdateContentMedia(ContentSearchVO contentSearchVO, boolean refresh,String type) throws SearchServiceException;
	public void indexDeleteContentMedia(Long contentId, boolean refresh,String type) throws SearchServiceException;
	public void indexDeleteStationMedia(Long contentId, boolean refresh,String type) throws SearchServiceException;
	public HashMap<String, Object> getStationSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException;
	public HashMap<String, Object> getContentSearch(SearchVO searchVO,int startLimit,int pageLimit,Map<String, Object> clientMap) throws SearchServiceException;
}
