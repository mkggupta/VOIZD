package com.voizd.search.service;

import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.beans.vo.StationSearchVO;
import com.voizd.search.dataobject.SearchResult;
import com.voizd.search.dataobject.SearchResponse;
import com.voizd.search.exception.ElasticServerClientException;


public interface VoizdSearch {
	public SearchResponse executeStation(StationSearchVO stationSearchVO, boolean refresh, String type) throws ElasticServerClientException;
	public SearchResponse executeContent(ContentSearchVO contentSearchVO, boolean refresh, String type) throws ElasticServerClientException;
	public SearchResponse executeDelete(Long id, boolean refresh, String type) throws ElasticServerClientException;
	public SearchResult executeStationSearch(String text,int startLimit,int pageLimit) throws ElasticServerClientException;
	public SearchResult executeContentSearch(String text,int startLimit,int pageLimit) throws ElasticServerClientException;
	public SearchResult executeAdvanceStationSearch(SearchVO searchVO,int startLimit,int pageLimit) throws ElasticServerClientException;
	public SearchResult executeAdvanceContentSearch(SearchVO searchVO,int startLimit,int pageLimit) throws ElasticServerClientException;
	public SearchResponse executeUpdateContent(ContentSearchVO contentSearchVO, boolean refresh, String type) throws ElasticServerClientException;
	public SearchResult executeContentTagSearch(String text,int startLimit,int pageLimit) throws ElasticServerClientException;
}
