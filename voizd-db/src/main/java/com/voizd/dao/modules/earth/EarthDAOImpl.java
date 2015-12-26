package com.voizd.dao.modules.earth;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.dao.constants.EarthConstants;
import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;

public class EarthDAOImpl extends AbstractDBManager implements EarthDAO {
	Logger logger = LoggerFactory.getLogger(EarthDAOImpl.class);
	String TAG_SEPERATOR=",";
	@SuppressWarnings("unchecked")
	@Override
	public List<EarthInfo> getEarthInfo(String location,int limit) throws DataAccessFailedException {
		try{
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("country",location);
			parameterMap.put("limit", limit);
			return sqlMapClientSlave_.queryForList(EarthConstants.GET_EARTH_INFO,parameterMap);
		}catch(SQLException e){
			logger.error("Exception in geting Erath Info from database : " + e.getLocalizedMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);	
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<EarthInfo> getLocationTag(String location, int start,int end) throws DataAccessFailedException {
		try{
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("country",location);
			parameterMap.put("start", start);
			parameterMap.put("end", end);
			return sqlMapClientSlave_.queryForList(EarthConstants.GET_LOCATION_TAGS,parameterMap);
		}catch(SQLException e){
			logger.error("Exception in geting Erath Info from database : " + e.getLocalizedMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);	
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EarthInfo> getTrendingTag(int start,int end) throws DataAccessFailedException{
		try{
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("start", start);
			parameterMap.put("end", end);
			return sqlMapClientSlave_.queryForList(EarthConstants.GET_TRENDING_TAGS,parameterMap);
		}catch(SQLException e){
			logger.error("Exception in geting EarthInfo Info from database : " + e.getLocalizedMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);	
		}
	}
	
	@Override
	public void saveOrUpdateEarthTag(EarthInfo earthInfo)throws DataUpdateFailedException {
		try {
			logger.debug(earthInfo.getCity()+" "+earthInfo.getState()+" "+earthInfo.getCountry()+" "+earthInfo.getTagName()+" "+earthInfo.getLanguage());
			sqlMapClient_.insert(EarthConstants.INSERT_TAGS, earthInfo);	
		} catch (SQLException  e) {
		
			try {
				EarthInfo earthInfoVO = (EarthInfo) sqlMapClient_.queryForObject(EarthConstants.GET_EARTH_INFO_TAGS_BY_ALL, earthInfo);
				if(null!=earthInfoVO){
					String fileIds =  earthInfoVO.getFileIds();
					String fileIdArr[] = earthInfoVO.getFileIds().split("\\s*,\\s*");
					if(fileIdArr.length==3){
						fileIds = fileIds.substring(earthInfoVO.getFileIds().indexOf(TAG_SEPERATOR), earthInfoVO.getFileIds().length());
						earthInfoVO.setFileIds(earthInfo.getFileIds()+fileIds);
					}else{
						earthInfoVO.setFileIds(earthInfo.getFileIds()+TAG_SEPERATOR+earthInfoVO.getFileIds());
					}
					
				}
				sqlMapClient_.update(EarthConstants.UPDATE_TAGS, earthInfoVO);
			} catch (SQLException  e1) {
				logger.error("Exception in saveEarthTag1 : "+ e.getLocalizedMessage(), e);
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
			
		} catch(Exception  e) {
			try {
				EarthInfo earthInfoVO = (EarthInfo) sqlMapClient_.queryForObject(EarthConstants.GET_EARTH_INFO_TAGS_BY_ALL, earthInfo);
				if(null!=earthInfoVO){
					String fileIds =  earthInfoVO.getFileIds();
					String fileIdArr[] = earthInfoVO.getFileIds().split("\\s*,\\s*");
					if(fileIdArr.length==3){
						fileIds = fileIds.substring(earthInfoVO.getFileIds().indexOf(TAG_SEPERATOR), earthInfoVO.getFileIds().length());
						earthInfoVO.setFileIds(earthInfo.getFileIds()+fileIds);
					}else{
						earthInfoVO.setFileIds(earthInfo.getFileIds()+TAG_SEPERATOR+earthInfoVO.getFileIds());
					}
					
				}
				sqlMapClient_.update(EarthConstants.UPDATE_TAGS, earthInfoVO);
			} catch (SQLException  e1) {
				logger.error("Exception in saveEarthTag : "+ e.getLocalizedMessage(), e);
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
			
		}
		
	}
	@Override
	public List<String> getFilesByLocation(String location, int start, int end) throws DataAccessFailedException {
		try{
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("country",location);
			parameterMap.put("start", start);
			parameterMap.put("end", end);
			return sqlMapClientSlave_.queryForList(EarthConstants.GET_FILES_BY_LOCATION,parameterMap);
		}catch(SQLException e){
			logger.error("Exception in geting Erath Info from database : " + e.getLocalizedMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);	
		}
	}

	
}
