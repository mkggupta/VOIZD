/**
 * 
 */
package com.voizd.dao.modules.media;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaMasterVO;
import com.voizd.common.beans.vo.MediaServerVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StreamMediaVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.constants.MediaConstants;
import com.voizd.dao.entities.MediaVoice;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.base.AbstractDBManager;
import com.voizd.framework.exception.util.ErrorCodesEnum;


/**
 * @author arvind
 *
 */
public class MediaDAOImpl  extends AbstractDBManager implements MediaDAO{
	private static Logger logger = LoggerFactory.getLogger(MediaDAOImpl.class);
	
	@Override
	public String getDefaultMedia(String fileName)
			throws DataAccessFailedException {
		try {
			return (String) sqlMapClientSlave_.queryForObject(MediaConstants.GET_DEFAULT_MEDIA,fileName);
		} catch (SQLException e) {
			logger.error("Exception in getDefaultMedia : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	@Override
	public DefaultMediaVO getDefaultMediaVO(String fileId) throws DataAccessFailedException {
		try {
			return (DefaultMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_DEFAULT_MEDIA_VO,fileId);
		} catch (SQLException e) {
			logger.error("Exception in getDefaultMediaVO : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	public DefaultMediaVO getDefaultMediaVOByName(String defaultName) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("defaultName", defaultName);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
		  return (DefaultMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_DEFAULT_MEDIA_VO_BY_NAME,params);
		} catch (SQLException e) {
			logger.error("Exception in getDefaultMediaVOByName : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void createStationMedia(StationMediaVO stationMedia)
			throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(MediaConstants.INSERT_STATION_MEDIA_INFO,stationMedia);
			}catch(SQLException e){
				logger.error("Exception in createStationMedia : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@Override
	public StationMediaVO getStationMedia(Long stationId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
			return (StationMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_STATION_MEDIA_BY_STATION_ID,params);
		} catch (SQLException e) {
			logger.error("Exception in getStationMedia : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<StationMediaVO> getStationMediaList(Long stationId,String mediaType) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stationId", stationId);
			params.put("mediaType", mediaType);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
			return (List<StationMediaVO>) sqlMapClientSlave_.queryForList(MediaConstants.GET_STATION_MEDIA_LIST,params);
		} catch (SQLException e) {
			logger.error("Exception in getStationMediaList : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentMediaVO> getContentMediaList(Long contentId,String mediaType) throws DataAccessFailedException{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("mediaType", mediaType);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
			return (List<ContentMediaVO>) sqlMapClientSlave_.queryForList(MediaConstants.GET_CONTENT_MEDIA_LIST,params);
		} catch (SQLException e) {
			logger.error("Exception in getContentMediaList : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void createContentMedia(ContentMediaVO contentMedia) throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(MediaConstants.INSERT_CONTENT_MEDIA_INFO,contentMedia);
			}catch(SQLException e){
				logger.error("Exception in createContentMedia : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentMediaVO> getContentMedia(Long contentId) throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contentId", contentId);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
			return (List<ContentMediaVO>) sqlMapClientSlave_.queryForList(MediaConstants.GET_CONTENT_MEDIA_INFO,params);
		} catch (SQLException e) {
			logger.error("Exception in getConetntMedia : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public StationMediaVO getStationMediaByFileId(String fileId) throws DataAccessFailedException {
		try {
			return (StationMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_STATION_MEDIA_BY_FILE_ID,fileId);
		} catch (SQLException e) {
			logger.error("Exception in getStationMediaByFileId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public ContentMediaVO getContentMediaByFileId(String fileId) throws DataAccessFailedException {
		try {
			return (ContentMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_CONTENT_MEDIA_BY_FILE_ID,fileId);
		} catch (SQLException e) {
			logger.error("Exception in getConetntMediaByFileId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public  List<MediaServerVO> getAllMediaServers() throws DataAccessFailedException {
		try {
		
		return  sqlMapClient_.queryForList(MediaConstants.GET_ALL_MEDIA_SERVER_LIST);
		} catch (SQLException e) {
			logger.error("Exception in getAllMediaServers : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateStationMedia(Long stationId) throws DataUpdateFailedException {
		 try {
			sqlMapClient_.update(MediaConstants.UPDATE_STATION_MEDIA_FILE_STATUS,stationId);
		}catch(SQLException e){
			logger.error("Exception in createContentMedia : " + e.getMessage(),e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void createMediaMaster(MediaMasterVO mediaMasterVO) throws DataUpdateFailedException {
		try {
			sqlMapClient_.insert(MediaConstants.INSERT_MEDIA_MASTER_INFO, mediaMasterVO);
		} catch (SQLException e) {
			logger.error("Exception in createMediaMaster : " + e.getMessage(),e);
			throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public MediaMasterVO getMediaMaster(String fileId) throws DataAccessFailedException {
		try {
			return (MediaMasterVO) sqlMapClient_.queryForObject(MediaConstants.GET_MEDIA_MASTER_INFO_BY_FILE_ID,fileId);
		}catch(SQLException e) {
			logger.error("Exception in getStationMediaByFileId : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void updateContentMedia(Long contentId) throws DataUpdateFailedException {
		 try {
			 sqlMapClient_.update(MediaConstants.UPDATE_CONTENT_MEDIA_FILE_STATUS,contentId);
			}catch(SQLException e){
				logger.error("Exception in updateContentMedia : " + e.getMessage(),e);
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@Override
	public void createStreamMedia(StreamMediaVO streamMediaVO)throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(MediaConstants.INSERT_STREAM_MEDIA_INFO,streamMediaVO);
			}catch(SQLException e){
				logger.error("Exception in createStreamMedia : " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
		
	}

	@Override
	public StreamMediaVO getStreamMedia(Long streamId)throws DataAccessFailedException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("streamId", streamId);
			params.put("status", VoizdConstant.MEDIA_ACTIVE_STATUS);
			return (StreamMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_STREAM_MEDIA_BY_STREAM_ID,params);
		} catch (SQLException e) {
			logger.error("Exception in getStreamMedia : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public StreamMediaVO getStreamMediaByFileId(String fileId)throws DataAccessFailedException {
		try {
			return (StreamMediaVO) sqlMapClientSlave_.queryForObject(MediaConstants.GET_STREAM_MEDIA_BY_FILE_ID,fileId);
		} catch (SQLException e) {
			logger.error("Exception in getStationMediaByFileId : " + e.getMessage());
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}
	
	public void updateStreamMedia(Long streamId) throws DataUpdateFailedException{
		try {
			 sqlMapClient_.update(MediaConstants.UPDATE_STREAM_MEDIA_BY_STREAM_ID,streamId);
			}catch(SQLException e){
				logger.error("Exception in updateStreamMedia : " + e.getMessage(),e);
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

	@Override
	public MediaVoice getMediaVoice(int globe) throws DataAccessFailedException {
		try {
			return (MediaVoice) sqlMapClientSlave_.queryForObject(MediaConstants.GET_MEDIA_VOICE_BY_GLOBE_ID,globe);
		} catch (SQLException e) {
			logger.error("Exception in getMediaVoice : " + e.getMessage(),e);
			throw new DataAccessFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
		}
	}

	@Override
	public void createEarthVoiceMedia(MediaVoice mediaVoice) throws DataUpdateFailedException {
		try {
			  sqlMapClient_.insert(MediaConstants.INSERT_EARTH_VOICE_INFO,mediaVoice);
			}catch(SQLException e){
				logger.error("Exception in createEarthVoiceMedia :: " + e.getMessage());
				throw new DataUpdateFailedException(ErrorCodesEnum.DATABASE_LAYER_EXCEPTION, e);
			}
	}

}
