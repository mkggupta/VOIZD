package com.voizd.service.media.v1_0;

import com.voizd.common.beans.vo.MediaVO;
import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaType;
import com.voizd.service.media.exception.MediaServiceFailedException;



public interface MediaService {
  public String postMedia(MediaType mediaType,MediaCategory mediaCategory, byte[] data, String ownerKey) throws MediaServiceFailedException;
  public MediaVO getMediaInfo(String fileid) throws MediaServiceFailedException;
  public void convertMedia(String mediaType,String fileId, String ext,String screenResolution);
}
