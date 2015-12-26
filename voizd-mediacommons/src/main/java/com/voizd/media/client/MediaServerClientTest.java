package com.voizd.media.client;

import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.exception.MediaClientException;
import com.voizd.media.dataobject.exception.MediaServiceException;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;
import com.voizd.media.dataobject.request.convert.*;
import com.voizd.media.dataobject.request.get.GetAudioRequest;
import com.voizd.media.dataobject.request.get.GetImageRequest;
import com.voizd.media.dataobject.request.get.GetMediaRequest;
import com.voizd.media.dataobject.request.get.GetVideoRequest;
import com.voizd.media.dataobject.request.merge.MergeMediaRequest;
import com.voizd.media.dataobject.request.post.PostMediaRequest;
import com.voizd.media.utils.file.MediaFileFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: shalabh
 * Date: 14/8/12
 * Time: 1:51 PM
 */
public class MediaServerClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServerClientTest.class);

    private MediaServerClient mediaServerClient_;

    public MediaServerClientTest() throws SQLException, MediaClientException {
        mediaServerClient_ = MediaServerClientImpl.getInstance();
    }

    private void scheduleRequest(String[] fields, boolean isConcurrentRequest, ExecutorService executorService) throws IOException, MediaServiceException, MediaClientException, UnsupportedTypeException {
        if (isConcurrentRequest) {
            MediaServiceRequestExecutor mediaServiceRequestExecutor = new MediaServiceRequestExecutor(fields);
            executorService.execute(mediaServiceRequestExecutor);
        } else {
            executeRequest(fields);
        }
    }

    private void executeRequest(String[] fields) throws IOException, MediaServiceException, MediaClientException, UnsupportedTypeException {
        MediaOperation operation = MediaOperation.getMediaOperation(Integer.parseInt(fields[0]));
        if (operation.equals(MediaOperation.GET)) {
            executeGetRequest(fields);
        } else if (operation.equals(MediaOperation.POST)) {
            executePostRequest(fields);
        } else if (operation.equals(MediaOperation.CONVERT)) {
            executeConvertRequest(fields);
        } else if (operation.equals(MediaOperation.CONVERTGET)) {
            executeConvertGetRequest(fields);
        } else if (operation.equals(MediaOperation.MERGE)) {
            executeMergeRequest(fields);
        }
    }

    private void executeMergeRequest(String[] fields) throws IOException, MediaServiceException, MediaClientException, UnsupportedTypeException {
        MediaCategory category = MediaCategory.getMediaCategoryForCode(fields[1]);
        String firstFileId = fields[2];
        String secondFilePath = fields[3];
        File file = new File(secondFilePath);
        MergeMediaRequest request;
        if (file.exists()) {
            byte[] data = FileUtils.readFileToByteArray(file);
            request = new MergeMediaRequest(category, firstFileId, data, "test");
        } else {
            request = new MergeMediaRequest(category, firstFileId, secondFilePath, "test");
        }
        MediaResponse mediaResponse = mediaServerClient_.mergeMedia(request);
        LOGGER.info("Media merge request successful...Generated file id: " + mediaResponse.getFileId() + "; content length: " + mediaResponse.getContentLength());
    }

    private void executeConvertGetRequest(String[] fields) throws MediaServiceException, MediaClientException, UnsupportedTypeException {
        String fileId = fields[1];
        MimeType mimeType = MimeType.getMimeTypeForCode(fields[2]);
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        MediaType mediaType = mediaFile.getMediaType();
        ConvertGetMediaRequest request = null;
        if (mediaType.equals(MediaType.IMAGE)) {
            if (fields.length > 3) {
                int width = Integer.parseInt(fields[3]);
                int height = Integer.parseInt(fields[4]);
                request = new ConvertGetImageRequest(fileId, mimeType, width, height);
            } else {
                request = new ConvertGetImageRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.AUDIO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new ConvertGetAudioRequest(fileId, mimeType, biteRate, codec);
            } else {
                request = new ConvertGetAudioRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.VIDEO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new ConvertGetVideoRequest(fileId, mimeType, biteRate, codec);
            } else {
                request = new ConvertGetVideoRequest(fileId, mimeType);
            }
        }
        byte[] media = mediaServerClient_.convertAndGetMedia(request);
        LOGGER.info("Media convert and get request successful...Content length: " + media.length);
    }

    private void executeConvertRequest(String[] fields) throws MediaServiceException, MediaClientException, UnsupportedTypeException {
        String fileId = fields[1];
        MimeType mimeType = MimeType.getMimeTypeForCode(fields[2]);
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        MediaType mediaType = mediaFile.getMediaType();
        ConvertMediaRequest request = null;
        if (mediaType.equals(MediaType.IMAGE)) {
            if (fields.length > 3) {
                int width = Integer.parseInt(fields[3]);
                int height = Integer.parseInt(fields[4]);
                request = new ConvertImageRequest(fileId, mimeType, width, height);
            } else {
                request = new ConvertImageRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.AUDIO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new ConvertAudioRequest(fileId, mimeType, biteRate, codec);
            } else {
                request = new ConvertAudioRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.VIDEO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new ConvertVideoRequest(fileId, mimeType, biteRate, codec);
            } else {
                request = new ConvertVideoRequest(fileId, mimeType);
            }
        }
        String convertedFileId = mediaServerClient_.convertMedia(request);
        LOGGER.info("Media convert request successful...Converted file id: " + convertedFileId);
    }

    private void executePostRequest(String[] fields) throws IOException, MediaServiceException, MediaClientException, UnsupportedTypeException {
        MediaCategory category = MediaCategory.getMediaCategoryForCode(fields[1]);
        MediaType mediaType = MediaType.getMediaTypeFromCode(fields[2]);
        String mediaFilePath = fields[3];
        byte[] data = FileUtils.readFileToByteArray(new File(mediaFilePath));
        PostMediaRequest postMediaRequest = new PostMediaRequest(mediaType, category, data,"test");
        MediaResponse mediaResponse = mediaServerClient_.postMedia(postMediaRequest);
        LOGGER.info("Media posted successfully...Generated file id: " + mediaResponse.getFileId() + "::content length: " + mediaResponse.getContentLength());
    }

    private void executeGetRequest(String[] fields) throws MediaServiceException, MediaClientException, UnsupportedTypeException {
        String fileId = fields[1];
        MimeType mimeType = MimeType.getMimeTypeForCode(fields[2]);
        MediaFile mediaFile = MediaFileFactory.getMediaFile(fileId);
        MediaType mediaType = mediaFile.getMediaType();
        GetMediaRequest request = null;
        if (mediaType.equals(MediaType.IMAGE)) {
            if (fields.length > 3) {
                int width = Integer.parseInt(fields[3]);
                int height = Integer.parseInt(fields[4]);
                request = new GetImageRequest(fileId, mimeType, width, height);
            } else {
                request = new GetImageRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.AUDIO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new GetAudioRequest(fileId, mimeType, biteRate, codec);
            } else {
                request = new GetAudioRequest(fileId, mimeType);
            }
        } else if (mediaType.equals(MediaType.VIDEO)) {
            if (fields.length > 3) {
                int biteRate = Integer.parseInt(fields[3]);
                String codec = fields[4];
                request = new GetVideoRequest(fileId, mimeType, biteRate, codec, null, null);
            } else {
                request = new GetVideoRequest(fileId, mimeType);
            }
        }
        byte[] media = mediaServerClient_.getMedia(request);
        LOGGER.info("Media get request successful...Content length: " + media.length);
    }

    private class MediaServiceRequestExecutor implements Runnable {
        private String[] fields_;

        private MediaServiceRequestExecutor(String[] fields) {
            fields_ = fields;
        }

        public void run() {
            try {
                LOGGER.info("Executing media server request");
                executeRequest(fields_);
            } catch (Exception ex) {
                LOGGER.error("Error while executing media service reqquest", ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("application-context.xml");
        String dataFilePath = args[0];
        boolean isConcurrentRequests = false;
        if (args.length > 1) {
            isConcurrentRequests = Boolean.parseBoolean(args[1]);
        }
        int maxThreads = 10;
        if (args.length > 2) {
            maxThreads = Integer.parseInt(args[2]);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFilePath)));
        MediaServerClientTest mediaServerClientTest = new MediaServerClientTest();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] fields = line.split("\t");
            mediaServerClientTest.scheduleRequest(fields, isConcurrentRequests, executorService);
        }
        executorService.shutdown();
        executorService.awaitTermination(86400, TimeUnit.SECONDS);
    }
}
