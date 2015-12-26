package com.voizd.service.media;

import com.voizd.common.beans.vo.MediaMasterVO;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.service.media.exception.MediaServiceFailedException;

public class MediaBOImpl implements MediaBO {
	public MediaDAO getMediaDAO() {
		return mediaDAO;
	}

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	private MediaDAO mediaDAO;
	
	@Override
	public void createMediaMaster(MediaMasterVO mediaMasterVO) throws MediaServiceFailedException {
		try {
			mediaDAO.createMediaMaster(mediaMasterVO);
		} catch (DataUpdateFailedException e) {
			throw new MediaServiceFailedException(ErrorCodesEnum.STATION_CONTENT_UPDATE_SERVICE_FAILED_EXCEPTION);
		}
	}

	@Override
	public MediaMasterVO getMediaMaster(String fileId) throws MediaServiceFailedException {
		MediaMasterVO mediaMasterVO = null;
		try {
			mediaMasterVO = mediaDAO.getMediaMaster(fileId);
		} catch (DataAccessFailedException e) {
			throw new MediaServiceFailedException(ErrorCodesEnum.STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION);
		}
		return mediaMasterVO;
	}

}
