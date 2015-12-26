package com.voizd.service.media.v1_0;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.MediaMasterVO;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.media.client.MediaServerClient;
import com.voizd.media.client.MediaServerClientImpl;
import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaResponse;
import com.voizd.media.dataobject.MediaType;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.exception.MediaServiceException;
import com.voizd.media.dataobject.file.MediaFile;
import com.voizd.media.dataobject.request.convert.ConvertAudioRequest;
import com.voizd.media.dataobject.request.convert.ConvertImageRequest;
import com.voizd.media.dataobject.request.convert.ConvertMediaRequest;
import com.voizd.media.dataobject.request.post.PostMediaRequest;
import com.voizd.media.utils.file.MediaFileFactory;
import com.voizd.service.media.MediaBO;
import com.voizd.service.media.exception.MediaServiceFailedException;

public class MediaServiceImpl implements MediaService {
	private static Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);
    public void setMediaBO(MediaBO mediaBO) {
		this.mediaBO = mediaBO;
	}

	private MediaBO mediaBO; 
	
	@Override
	public String postMedia(MediaType mediaType, MediaCategory mediaCategory, byte[] data, String ownerKey) throws MediaServiceFailedException {
		if (ownerKey == null || ownerKey.trim().length() == 0) {
			ownerKey = "RTSYSTEM";
		}
		PostMediaRequest postMediaRequest = new PostMediaRequest(mediaType, mediaCategory, data, ownerKey);
		try {
			logger.debug("postMedia() ::  data.length ::" + data.length);
			MediaServerClient mediaServerClient = MediaServerClientImpl.getInstance();
			MediaResponse mediaResponse = mediaServerClient.postMedia(postMediaRequest);
			String type = mediaType.getValue();
			logger.debug("postMedia() :: type client ::" + type);
			String duration =null;
			if(mediaType != null && "audio".equalsIgnoreCase(type)){
				try {
					duration = mediaServerClient.getMediaDuration(mediaResponse.getFileId());
					logger.debug("duration :: duration ::" + duration);
				} catch (MediaServiceException e) {
					logger.debug("Exception in post media" + e.getLocalizedMessage(), e);
				}
			}
			MediaFile mediaFile = MediaFileFactory.getMediaFile(mediaResponse.getFileId());
			logger.debug("postMedia() :: Server type ::" + mediaFile.getMediaType().getValue()+" ,FileId :: "+mediaResponse.getFileId());
			insertMediaMaster(mediaResponse,mediaFile.getMediaType().getValue(), duration);
			logger.debug("postMedia() :: type client one end ::" + type);
			return mediaResponse.getFileId();
		} catch (Exception e) {
			logger.error("Exception in post media" + e.getLocalizedMessage(), e);
			throw new MediaServiceFailedException(ErrorCodesEnum.SEVERE_ERROR);
		}

	}


	@Override
	public MediaVO getMediaInfo(String fileId) throws MediaServiceFailedException {
		try {
			MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
			MediaVO media = new MediaVO();
			media.setFileId(fileId);
			media.setMediaType(mediaFile.getMediaType().getValue());
			media.setMimeType(mediaFile.getMimeType().getExt());
			media.setCategory(mediaFile.getMediaCategory().getValue());
			MediaMasterVO mediaMasterVO = getMediaMaster(fileId);
			media.setSize(mediaMasterVO.getSize());
			media.setDuration(mediaMasterVO.getDuration());
			return media;
		}catch (Exception e) {
				logger.error("Exception in getMediaInfo" + e.getLocalizedMessage(), e);
				throw new MediaServiceFailedException(ErrorCodesEnum.SEVERE_ERROR);
		}
	}

	private void insertMediaMaster(MediaResponse mediaResponse,String mediaType,String duration) throws MediaServiceFailedException{
		MediaMasterVO mediaMasterVO = new MediaMasterVO();
		logger.debug("insertMediaMaster() ::");
		mediaMasterVO.setFileId(mediaResponse.getFileId());
		mediaMasterVO.setMediaType(mediaType);
		mediaMasterVO.setExt(mediaResponse.getExtension());
		mediaMasterVO.setSize(mediaResponse.getContentLength());
		mediaMasterVO.setStatus(1);
		mediaMasterVO.setDuration(duration);
		logger.debug("insertMediaMaster() 1 ::");
		mediaBO.createMediaMaster(mediaMasterVO);
		logger.debug("insertMediaMaster() ::");
	}
	
	private MediaMasterVO getMediaMaster(String fileId) throws MediaServiceFailedException{
		return mediaBO.getMediaMaster(fileId);
	}
	
	public void convertMedia(String mediaType, String fileId, String ext, String screenResolution) {
		try {
			MediaServerClient mediaServerClient = MediaServerClientImpl.getInstance();
			ConvertMediaRequest mediaRequest = null;
			if ("audio".equalsIgnoreCase(mediaType)) {
				mediaRequest = new ConvertAudioRequest(fileId, MimeType.getMimeTypeForExtension(ext));

			} else if ("image".equalsIgnoreCase(mediaType) && StringUtils.isNotBlank(screenResolution)) {
				mediaRequest = new ConvertImageRequest(fileId, MimeType.IMAGE_JPG, screenResolution);
			}
			mediaServerClient.convertMedia(mediaRequest);
		} catch (Exception e) {
			logger.error("Exception in convertMedia" + e.getLocalizedMessage(), e);
		}
	}
}
