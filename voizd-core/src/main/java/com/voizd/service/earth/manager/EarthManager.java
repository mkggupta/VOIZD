package com.voizd.service.earth.manager;

import java.util.HashMap;
import java.util.Map;

import com.voizd.common.beans.vo.GlobeVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.service.earth.exception.EarthServiceException;

public interface EarthManager {
	public HashMap<String, Object> getEarthView(String country,int limit) throws EarthServiceException;
	public HashMap<String, Object> getLocationView(GlobeVO globeVO) throws EarthServiceException;
	public HashMap<String, Object> getTagView(GlobeVO globeVO,Map<String, Object> clientMap) throws EarthServiceException;
	public HashMap<String, Object> getVTags(int startLimit, int endLimit,boolean order) throws EarthServiceException;
	public HashMap<String, Object> getMapView(MapVO mapVO,Map<String, Object> clientMap) throws EarthServiceException;
}
