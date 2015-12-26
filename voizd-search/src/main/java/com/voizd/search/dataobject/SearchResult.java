package com.voizd.search.dataobject;

import java.util.List;

import com.voizd.common.beans.vo.ContentSearchVO;
import com.voizd.common.beans.vo.StationSearchVO;

public class SearchResult {
	public List<ContentSearchVO> getContentSearchVOList() {
		return contentSearchVOList;
	}
	public void setContentSearchVOList(List<ContentSearchVO> contentSearchVOList) {
		this.contentSearchVOList = contentSearchVOList;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	List<ContentSearchVO> contentSearchVOList;
	public List<StationSearchVO> getStationSearchVOList() {
		return stationSearchVOList;
	}
	public void setStationSearchVOList(List<StationSearchVO> stationSearchVOList) {
		this.stationSearchVOList = stationSearchVOList;
	}

	List<StationSearchVO> stationSearchVOList;
	int size;
}
