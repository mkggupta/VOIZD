package com.voizd.service.advt.bo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AdvtMicInfo;
import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdRelativeUrls;
import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.modules.advt.AdvtDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.service.advt.exception.AdvtServiceException;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;

public class AdvtBOImpl implements AdvtBO {
	private static Logger logger = LoggerFactory.getLogger(AdvtBOImpl.class);
	private AdvtDAO advtDAO ;
	private MediaDAO mediaDAO ;
	private MediaService  mediaService;
	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	
	
	public void setAdvtDAO(AdvtDAO advtDAO) {
		this.advtDAO = advtDAO;
	}

	@Override
	public AdvtMicInfo getMicAdvt(Map<String, Object> clientMap)throws AdvtServiceException {
		
		AdvtMicInfo advtMicInfo = new AdvtMicInfo();
		try {
			advtMicInfo = advtDAO.getMicAdvt() ;
			if(null!=advtMicInfo){
				String fileIdArr[] = advtMicInfo.getFileIds().split("\\s*,\\s*");
				
				 String imageResolution = null;
					 //String platform = VoizdConstant.DEFAULT_PLATFORM;
					 if(clientMap!=null){
						 if(null!=clientMap.get(ClientParamConstant.IMAGESIZE)){
						    imageResolution = MediaUtil.getImageSize(clientMap.get(ClientParamConstant.IMAGESIZE).toString());
						 }else{
							 imageResolution = MediaUtil.getImageSize(null);
						 }
						
						/* if(null!=clientMap.get(ClientParamConstant.PLATFORM)){
							 platform =clientMap.get(ClientParamConstant.PLATFORM).toString();
						 }*/
					 }else{
						 imageResolution = MediaUtil.getImageSize(null);
					 }
					 advtMicInfo.setMediaPostUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.ADVT_MEDIA_POST_URL));
					 advtMicInfo.setContentCreationUrl(VoizdUrlUtils.getAbsoluteEncodedUrl(VoizdUrlUtils.getServerUrl(), VoizdRelativeUrls.ADVT_CONTENT_POST_URL));
				 for (String fileId:fileIdArr){
					 try {
							MediaVO mediaVO = mediaService.getMediaInfo(fileId);
							if(null!=mediaVO){
								if(VoizdConstant.AUDIO.equalsIgnoreCase(mediaVO.getMediaType())){
									if(!VoizdConstant.MP3.equalsIgnoreCase(mediaVO.getMimeType())){
										mediaVO.setMimeType(VoizdConstant.MP3);
										mediaService.convertMedia(mediaVO.getMediaType(),mediaVO.getFileId(),mediaVO.getMimeType(),null);
									}
									advtMicInfo.setAudUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.MP3,null),true)));
								}
								
								if(VoizdConstant.IMAGE.equalsIgnoreCase(mediaVO.getMediaType())){
									mediaService.convertMedia(mediaVO.getMediaType(), mediaVO.getFileId(), VoizdConstant.IMAGE_JPG, imageResolution);
									advtMicInfo.setImgUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(mediaVO.getMediaType(),MediaUtil.getMediaContentUrl(mediaVO.getFileId(),VoizdConstant.IMAGE_JPG,imageResolution),false)));
								}
							}
							
						} catch (MediaServiceFailedException e) {
							logger.error("getMicAdvt---"+advtMicInfo);
								throw new AdvtServiceException(ErrorCodesEnum.MEDIA_GET_FAILED_EXCEPTION);
					 }
				 }
			} 
		}catch (DataAccessFailedException e) {
			logger.error("getMicAdvt---"+e.getLocalizedMessage());
			throw new AdvtServiceException(ErrorCodesEnum.ADVT_SERVICE_FAILED_EXCEPTION);
		}
		
		return   advtMicInfo ;
		
	}

}
