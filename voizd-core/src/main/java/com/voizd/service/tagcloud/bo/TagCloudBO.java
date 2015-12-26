package com.voizd.service.tagcloud.bo;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.service.tagcloud.exception.TagCloudServiceException;

public interface TagCloudBO {
	
	void createTagCloud (ContentVO contentVO) throws TagCloudServiceException;
	String createTagCloud (ContentVO contentVO, int status) throws TagCloudServiceException;

}
