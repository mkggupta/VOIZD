package com.voizd.service.earth.v1_0;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.service.earth.exception.EarthServiceException;
import com.voizd.service.earth.manager.EarthManager;

public class EarthServiceImpl implements EarthService {
	private EarthManager earthManager;



	public void setEarthManager(EarthManager earthManager) {
		this.earthManager = earthManager;
	}

	@Override
	public HashMap<String, Object> getEarthView(String country, int limit) throws EarthServiceException {
		return earthManager.getEarthView(country, limit);
	}

	@Override
	public HashMap<String, Object> getLocationView(GlobeVO globeVO) throws EarthServiceException {
		return earthManager.getLocationView(globeVO);
	}

	@Override
	public HashMap<String, Object> getTagView(GlobeVO globeVO,Map<String, Object> clientMap) throws EarthServiceException {
			return earthManager.getTagView(globeVO,clientMap);
	}
	public HashMap<String, Object> getVTags(int startLimit, int endLimit,boolean order) throws EarthServiceException{
		return earthManager.getVTags(startLimit,endLimit,order);
	}

	@Override
	public HashMap<String, Object> getMapView(MapVO mapVO,Map<String, Object> clientMap) throws EarthServiceException {
		return earthManager.getMapView(mapVO,clientMap);
	}

}
