/**
 * 
 */
package com.voizd.dao.modules.media;


import java.util.List;

import com.voizd.common.beans.vo.ContentMediaVO;
import com.voizd.common.beans.vo.DefaultMediaVO;
import com.voizd.common.beans.vo.MediaMasterVO;
import com.voizd.common.beans.vo.MediaServerVO;
import com.voizd.common.beans.vo.StationMediaVO;
import com.voizd.common.beans.vo.StreamMediaVO;
import com.voizd.dao.entities.MediaVoice;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

/**
 * @author arvind
 *
 */
public interface MediaDAO {
	public String getDefaultMedia(String fileName) throws DataAccessFailedException;
	public void createStationMedia(StationMediaVO stationMedia) throws DataUpdateFailedException;
	public StationMediaVO getStationMedia(Long stationId) throws DataAccessFailedException;
	public StationMediaVO getStationMediaByFileId(String fileId) throws DataAccessFailedException;
	public void createContentMedia(ContentMediaVO contentMedia) throws DataUpdateFailedException;
	public List<ContentMediaVO> getContentMedia(Long contentId) throws DataAccessFailedException;
	public ContentMediaVO getContentMediaByFileId(String fileId) throws DataAccessFailedException;
	public  List<MediaServerVO> getAllMediaServers() throws DataAccessFailedException;
	public DefaultMediaVO getDefaultMediaVO(String fileId) throws DataAccessFailedException;
	public DefaultMediaVO getDefaultMediaVOByName(String defaultName) throws DataAccessFailedException;
	public void updateStationMedia(Long stationId) throws DataUpdateFailedException;
	public List<StationMediaVO> getStationMediaList(Long stationId,String mediaType) throws DataAccessFailedException;
	public List<ContentMediaVO> getContentMediaList(Long contentId,String mediaType) throws DataAccessFailedException;
	public void createMediaMaster(MediaMasterVO mediaMasterVO) throws DataUpdateFailedException;
	public MediaMasterVO getMediaMaster(String fileId) throws DataAccessFailedException;
	public void updateContentMedia(Long contentId) throws DataUpdateFailedException;
	public void createStreamMedia(StreamMediaVO streamMediaVO) throws DataUpdateFailedException;
	public StreamMediaVO getStreamMedia(Long streamId) throws DataAccessFailedException;
	public StreamMediaVO getStreamMediaByFileId(String fileId) throws DataAccessFailedException;
	public void updateStreamMedia(Long streamId) throws DataUpdateFailedException;
	public MediaVoice getMediaVoice(int globe) throws DataAccessFailedException;
	public void createEarthVoiceMedia(MediaVoice mediaVoice) throws DataUpdateFailedException;
}
