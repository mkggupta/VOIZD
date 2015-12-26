package com.voizd.dao.modules.earth;

import java.util.List;

import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface EarthDAO {
	List<EarthInfo> getEarthInfo(String location,int limit) throws DataAccessFailedException;
	List<EarthInfo> getLocationTag(String location,int start,int end) throws DataAccessFailedException;
	void saveOrUpdateEarthTag(EarthInfo earthInfo)throws DataUpdateFailedException;
	List<EarthInfo> getTrendingTag(int start,int end) throws DataAccessFailedException;
	List<String> getFilesByLocation(String location,int start,int end) throws DataAccessFailedException;
	
}
