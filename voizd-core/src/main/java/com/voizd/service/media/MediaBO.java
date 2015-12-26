package com.voizd.service.media;

import com.voizd.common.beans.vo.MediaMasterVO;
import com.voizd.service.media.exception.MediaServiceFailedException;


public interface MediaBO {
	public void createMediaMaster(MediaMasterVO mediaMasterVO) throws MediaServiceFailedException ;
	public MediaMasterVO getMediaMaster(String fileId) throws MediaServiceFailedException ;
}
