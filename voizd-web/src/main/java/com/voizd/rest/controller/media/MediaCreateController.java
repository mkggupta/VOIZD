package com.voizd.rest.controller.media;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.media.dataobject.MediaType;
import com.voizd.media.dataobject.file.MediaFile;
import com.voizd.media.utils.file.MediaFileFactory;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.service.media.exception.MediaServiceFailedException;
import com.voizd.service.media.v1_0.MediaService;

@Controller
@RequestMapping("/api/media")
public class MediaCreateController {
	private static Logger logger = LoggerFactory.getLogger(MediaCreateController.class);

	private static final String FILE_ID = "fileId";
	private static final String FILE_TYPE = "fileType";
	private static final String TYPE = "type";
	private static final String CAT = "cat";
	private static final String DATA = "data";

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	private MediaService mediaService;

	@RequestMapping(value = "/vzpb/create", method = { RequestMethod.POST })
	public ModelAndView createMedia(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		try {
			HashMap<String, Object> requestMap = ControllerUtils.getMediaRequestMapFromMultipart(httpServletRequest);
			if (requestMap.size() > 0) {
				String fileType = (String) requestMap.get(TYPE);
				String catInfo = (String) requestMap.get(CAT);
				byte[] mediaData = (byte[]) requestMap.get( DATA);
				String userName =null;
				try{
				Map<String, String> headerMap = ControllerUtils.parseHeader(httpServletRequest);
				Map<String, String> authMap = ControllerUtils.extractAuthMap(headerMap.get(VoizdWebConstant.voizd_AUTHETICATION_PROPERTY));
			    userName = authMap.get(VoizdWebConstant.voizd_AUTHETICATION_USER_NAME_PARAM);
				}catch(Exception e){
					logger.error("Exception while getting username "+e.getLocalizedMessage(),e);
				}
				String fileId = mediaService.postMedia(MediaType.getMediaTypeFromValue(fileType),
						com.voizd.media.dataobject.MediaCategory.getMediaCategoryByName(ControllerUtils.getMediaCategory(catInfo)), mediaData, userName);
				logger.debug("Media creation :: fileId ::" + fileId+", catInfo :: "+catInfo +", fileType ::"+fileType +" ,userName :: "+userName);
				if (fileId == null) {
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION.getErrorMessage());
					logger.error("Media posting having some issue .File id getting null ");
				} else if(fileId != null) {
					MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
					fileType = mediaFile.getMediaType().toString();
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.MEDIA_CREATE_SUCCESS.getSuccessMessage());
					dataMap.put(FILE_ID, fileId);
					dataMap.put(FILE_TYPE, fileType);
				}
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION.getErrorMessage());
				logger.error("Media posting having some issue.Post media requestMap is null ");
			}
		} catch (MediaServiceFailedException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION.getErrorMessage());
			logger.error("MediaServiceFailedException "+e.getLocalizedMessage(),e);
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.MEDIA_CREATION_FAILED_EXCEPTION.getErrorMessage());
			logger.error("Exception "+e.getLocalizedMessage(),e);
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
}
