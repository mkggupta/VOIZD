package com.voizd.service.earth.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.service.earth.bo.EarthBO;
import com.voizd.service.earth.exception.EarthServiceException;

public class EarthManagerImpl implements EarthManager {
	public void setEarthBO(EarthBO earthBO) {
		this.earthBO = earthBO;
	}
	private EarthBO earthBO;
	
	@Override
	public HashMap<String, Object> getEarthView(String country, int limit) throws EarthServiceException {
		return earthBO.getEarthView(country, limit);
	}

	@Override
	public HashMap<String, Object> getLocationView(GlobeVO globeVO) throws EarthServiceException {
		return earthBO.getLocationView(globeVO);
	}

	@Override
	public HashMap<String, Object> getTagView(GlobeVO globeVO,Map<String, Object> clientMap) throws EarthServiceException {
		return earthBO.getTagView(globeVO,clientMap);
	}
	public HashMap<String, Object> getVTags(int startLimit, int endLimit,boolean order) throws EarthServiceException{
		return earthBO.getVTags(startLimit,endLimit,order);
	}

	@Override
	public HashMap<String, Object> getMapView(MapVO mapVO,Map<String, Object> clientMap) throws EarthServiceException {
		return earthBO.getMapView(mapVO,clientMap);
	}
}
