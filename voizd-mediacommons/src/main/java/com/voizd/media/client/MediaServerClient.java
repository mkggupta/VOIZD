package com.voizd.media.client;


import com.voizd.media.dataobject.MediaResponse;
import com.voizd.media.dataobject.exception.MediaClientException;
import com.voizd.media.dataobject.exception.MediaServiceException;
import com.voizd.media.dataobject.request.check.GetFileCheckRequest;
import com.voizd.media.dataobject.request.convert.ConvertGetMediaRequest;
import com.voizd.media.dataobject.request.convert.ConvertMediaRequest;
import com.voizd.media.dataobject.request.cut.CutGetMediaRequest;
import com.voizd.media.dataobject.request.cut.CutMediaRequest;
import com.voizd.media.dataobject.request.get.GetMediaRequest;
import com.voizd.media.dataobject.request.merge.MergeMediaRequest;
import com.voizd.media.dataobject.request.post.PostMediaRequest;
import com.voizd.media.dataobject.request.thumb.CreateGetThumbMediaRequest;
import com.voizd.media.dataobject.request.thumb.CreateThumbMediaRequest;
import com.voizd.media.dataobject.request.thumb.GetThumbMediaRequest;

/**
 * User: shalabh
 * Date: 26/6/12
 * Time: 2:13 PM
 */
public interface MediaServerClient {
    public MediaResponse postMedia(PostMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public byte[] getMedia(GetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public String convertMedia(ConvertMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public byte[] convertAndGetMedia(ConvertGetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public MediaResponse mergeMedia(MergeMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public String cutMedia(CutMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public byte[] cutAndGetMedia(CutGetMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public byte[] createAndGetThumbnail(CreateGetThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public String createThumbnail(CreateThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;

    public byte[] getThumbnail(GetThumbMediaRequest mediaRequest) throws MediaServiceException, MediaClientException;
    
    public boolean fileExist(GetFileCheckRequest mediaRequest) throws MediaServiceException, MediaClientException;
    
    public String getMediaDuration(String fileId) throws MediaServiceException, MediaClientException;
}
