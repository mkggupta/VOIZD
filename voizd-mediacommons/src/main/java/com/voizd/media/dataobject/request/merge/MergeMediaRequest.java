package com.voizd.media.dataobject.request.merge;


import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaConstants;
import com.voizd.media.dataobject.MediaOperation;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 30/7/12
 * Time: 6:19 PM
 */
public class MergeMediaRequest extends BaseMediaRequest {
    private byte[] secondMediaData_;
    protected String ownerKey_;

    public MergeMediaRequest(MediaCategory mediaCategory, String firstFileId, String secondFileId, String ownerKey) throws UnsupportedTypeException {
        super(MediaOperation.MERGE, mediaCategory, firstFileId);
        addMediaAttributes(RequestParam.MEDIACATEGORY, mediaCategory.getValue());
        addMediaAttributes(RequestParam.FILE_ID1, firstFileId);
        addMediaAttributes(RequestParam.FILE_ID2, secondFileId);
        addMediaAttributes(RequestParam.RESPONSE_TYPE, MediaConstants.JSON_RESPONSE_TYPE);
        if(ownerKey!= null && ownerKey.trim().length() > 0){
    		addMediaAttributes(RequestParam.OWNERKEY, ownerKey);
    	}
    }

    public MergeMediaRequest(MediaCategory mediaCategory, String firstFileId, byte[] secondMediaData, String ownerKey) throws UnsupportedTypeException {
        super(MediaOperation.MERGE, mediaCategory, firstFileId);
        addMediaAttributes(RequestParam.MEDIACATEGORY, mediaCategory.getValue());
        addMediaAttributes(RequestParam.FILE_ID1, firstFileId);
        addMediaAttributes(RequestParam.RESPONSE_TYPE, MediaConstants.JSON_RESPONSE_TYPE);
        secondMediaData_ = secondMediaData;
        if(ownerKey!= null && ownerKey.trim().length() > 0){
    		addMediaAttributes(RequestParam.OWNERKEY, ownerKey);
    	}
    }

    public String getFirstFileId() {
        return (String) mediaAttributesMap_.get(RequestParam.FILE_ID1);
    }

    public String getSecondFileId() {
        return (String) mediaAttributesMap_.get(RequestParam.FILE_ID2);
    }

    public byte[] getSecondMediaData() {
        return secondMediaData_;
    }
    
    public String getOwnerKey() {
		return ownerKey_;
	}

    public String getResponseType() {
        return (String) mediaAttributesMap_.get(RequestParam.RESPONSE_TYPE);
    }

    @Override
    protected String getUrlPath() {
        return mediaOperation_.getUrlSuffix();
    }
}
