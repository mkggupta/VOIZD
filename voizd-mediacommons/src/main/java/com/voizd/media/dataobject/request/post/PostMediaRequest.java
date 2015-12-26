package com.voizd.media.dataobject.request.post;


import com.voizd.media.dataobject.*;
import com.voizd.media.dataobject.request.BaseMediaRequest;

/**
 * User: shalabh
 * Date: 24/7/12
 * Time: 11:49 AM
 */
public class PostMediaRequest extends BaseMediaRequest {
    private byte[] data_;
    protected String ownerKey_;
   

    public PostMediaRequest(MediaType mediaType, MediaCategory mediaCategory, byte[] data, String ownerKey) {
    	super(MediaOperation.POST, mediaType, mediaCategory);
    	data_ = data;
    	ownerKey_ = ownerKey;
    	addMediaAttributes(RequestParam.MEDIATYPE, mediaType.getValue());
    	addMediaAttributes(RequestParam.MEDIACATEGORY, mediaCategory.getValue());
    	addMediaAttributes(RequestParam.RESPONSE_TYPE, MediaConstants.JSON_RESPONSE_TYPE);
    	if(ownerKey!= null && ownerKey.trim().length() > 0){
    		addMediaAttributes(RequestParam.OWNERKEY, ownerKey);
    	}
    }

    public byte[] getData() {
        return data_;
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
