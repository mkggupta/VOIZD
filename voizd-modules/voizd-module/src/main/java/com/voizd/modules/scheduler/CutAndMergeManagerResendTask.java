package com.voizd.modules.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.util.ControllerUtils;
import com.voizd.dao.entities.MediaVoice;
import com.voizd.dao.modules.earth.EarthDAO;
import com.voizd.dao.modules.media.MediaDAO;
import com.voizd.media.client.MediaServerClient;
import com.voizd.media.client.MediaServerClientImpl;
import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaResponse;
import com.voizd.media.dataobject.request.convert.ConvertAudioRequest;
import com.voizd.media.dataobject.request.convert.ConvertMediaRequest;
import com.voizd.media.dataobject.request.merge.MergeMediaRequest;
import com.voizd.media.dataobject.MimeType;

public class CutAndMergeManagerResendTask extends TimerTask {
	private static Logger logger = LoggerFactory.getLogger(CutAndMergeManagerResendTask.class);
	private static String INDIA = "india";
	private static String US = "United States";
	private static String FILE_EXT = "mp3";
	private static String VOICE = "voice";
	private EarthDAO earthDAO;
	private MediaDAO mediaDAO;

	public void setMediaDAO(MediaDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}

	public void setEarthDAO(EarthDAO earthDAO) {
		this.earthDAO = earthDAO;
	}

	/**
	 * @param earthDAO
	 */
	public CutAndMergeManagerResendTask() {

	}

	@Override
	public void run() {
		try {
			processMergeRequest();
		} catch (Exception ex) {
			logger.error("Error while processing run :: " + ex.getLocalizedMessage(), ex);
		}

	}

	/*
	 * private void processMergeRequest() { int startLimit = 0; int endLimit =
	 * 10; String location = "india"; try { MediaServerClient mediaServerClient
	 * = MediaServerClientImpl.getInstance(); List<String> fileList =
	 * earthDAO.getFilesByLocation(location, startLimit, endLimit);
	 * logger.error("processMergeRequest  ::fileList ::  "+fileList);
	 * List<String> files = filterFileList(fileList); String fileId = null;
	 * MediaResponse mediaResponse = null;
	 * logger.error("processMergeRequest  ::files ::  "+files); int count = 1;
	 * for (String file : files) {
	 * logger.error("processMergeRequest  :: loop count  "+count
	 * +" , file :: "+file); if (count > 1) { MediaCategory mediaCategory =
	 * com.voizd
	 * .media.dataobject.MediaCategory.getMediaCategoryByName(ControllerUtils
	 * .getMediaCategory("APPSTORE")); MergeMediaRequest mediaRequest = new
	 * MergeMediaRequest(mediaCategory,fileId,file,null); mediaResponse =
	 * mediaServerClient.mergeMedia(mediaRequest); fileId =
	 * mediaResponse.getFileId();
	 * logger.error("processMergeRequest if  ::fileId  "+fileId); } else {
	 * fileId = file;
	 * logger.error("processMergeRequest else  ::fileId  "+fileId); } count++; }
	 * mergeRequest(files);
	 * //logger.error("processMergeRequest ::fileId  "+mediaResponse
	 * .getFileId()+
	 * " ,Extension:: "+mediaResponse.getExtension()+" ,ContentLength :: "
	 * +mediaResponse.getContentLength());
	 * if(!("mp3".equalsIgnoreCase(mediaResponse.getExtension()))){
	 * ConvertMediaRequest mediaRequest = new
	 * ConvertAudioRequest(mediaResponse.getFileId
	 * (),MimeType.getMimeTypeForExtension("mp3")); String convertedFileId =
	 * mediaServerClient.convertMedia(mediaRequest); MediaVoice mediaVoice = new
	 * MediaVoice(); mediaVoice.setFileId(convertedFileId);
	 * mediaVoice.setMediaType("audio"); mediaVoice.setExt("mp3");
	 * mediaVoice.setGlobe(1);
	 * mediaVoice.setDuration(mediaServerClient.getMediaDuration
	 * (convertedFileId)); mediaDAO.createEarthVoiceMedia(mediaVoice);
	 * logger.error("processMergeRequest ::convertedFileId ::"+convertedFileId);
	 * } } catch (Exception e) {
	 * logger.error("Error while processing processMergeRequest :: " +
	 * e.getLocalizedMessage(), e); } }
	 */

	private void processMergeRequest() {
		int startLimit = 0;
		int endLimit = 10;
		String location = INDIA;
		int globe = 1;
		processRequest(location, startLimit, endLimit, globe);
		globe = 2;
		location = US;
		processRequest(location, startLimit, endLimit, globe);
	}

	private void processRequest(String location, int startLimit, int endLimit, int globe) {
		try {
			List<String> fileList = earthDAO.getFilesByLocation(location, startLimit, endLimit);
			logger.debug("processMergeRequest  ::fileList ::  " + fileList);
			List<String> files = filterFileList(fileList);
			mergeRequest(files, globe);
		} catch (Exception e) {
			logger.error("Error while processing processMergeRequest :: " + e.getLocalizedMessage(), e);
		}
	}

	private void mergeRequest(List<String> files, int globe) {
		try {
			String fileId = null;
			MediaResponse mediaResponse = null;
			MediaServerClient mediaServerClient = MediaServerClientImpl.getInstance();
			logger.debug("processMergeRequest  ::files ::  " + files);
			int count = 1;
			for (String file : files) {
				logger.debug("processMergeRequest  :: loop count  " + count + " , file :: " + file);
				if (count > 1) {
					MediaCategory mediaCategory = com.voizd.media.dataobject.MediaCategory.getMediaCategoryByName(ControllerUtils.getMediaCategory("APPSTORE"));
					MergeMediaRequest mediaRequest = new MergeMediaRequest(mediaCategory, fileId, file, null);
					mediaResponse = mediaServerClient.mergeMedia(mediaRequest);
					fileId = mediaResponse.getFileId();
					logger.debug("processMergeRequest if  ::fileId  " + fileId);
				} else {
					fileId = file;
					logger.debug("processMergeRequest else  ::fileId  " + fileId);
				}
				count++;
			}
			logger.debug("processMergeRequest ::fileId  " + mediaResponse.getFileId() + " ,Extension:: " + mediaResponse.getExtension() + " ,ContentLength :: "
					+ mediaResponse.getContentLength());
			createEarthVoice(mediaResponse, globe);
		} catch (Exception e) {
			logger.error("mergeRequest : error while mergeRequest() :: " + e.getLocalizedMessage(), e);
		}
	}

	private void createEarthVoice(MediaResponse mediaResponse, int globe) {
		try {
			MediaServerClient mediaServerClient = MediaServerClientImpl.getInstance();
			MediaVoice mediaVoice = new MediaVoice();
			mediaVoice.setMediaType(VOICE);
			mediaVoice.setGlobe(globe);
			mediaVoice.setDuration(mediaServerClient.getMediaDuration(mediaResponse.getFileId()));
			mediaVoice.setFileId(mediaResponse.getFileId());
			if (!(FILE_EXT.equalsIgnoreCase(mediaResponse.getExtension()))) {
				ConvertMediaRequest mediaRequest = new ConvertAudioRequest(mediaResponse.getFileId(), MimeType.getMimeTypeForExtension(FILE_EXT));
				String convertedFileId = mediaServerClient.convertMedia(mediaRequest);
				mediaVoice.setExt(FILE_EXT);
				logger.error("processMergeRequest ::convertedFileId ::" + convertedFileId);
			} else {
				mediaVoice.setExt(mediaResponse.getExtension());
			}
			//mediaDAO.createEarthVoiceMedia(mediaVoice);
		} catch (Exception e) {
			logger.error("mergeRequest : error while mergeRequest() :: " + e.getLocalizedMessage(), e);
		}
	}

	private List<String> filterFileList(List<String> fileList) {
		ArrayList<String> fileId = new ArrayList<String>();
		for (String files : fileList) {
			String file[] = files.split(",");
			fileId.add(file[0]);
		}
		return fileId;
	}

}
