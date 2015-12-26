package com.voizd.media.dataobject.request.check;

import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.RequestParam;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * 
 * @author Vikrant Singh
 * 
 */
public class GetFileCheckRequest extends CheckMediaRequest {

	public GetFileCheckRequest(String fileId, MimeType mimeType, String ownerKey)
			throws UnsupportedTypeException {
		this(fileId, mimeType);
		addMediaAttributes(ownerKey);
	}

	public GetFileCheckRequest(String fileId, MimeType mimeType)
			throws UnsupportedTypeException {
		super(fileId, mimeType);
	}

	private void addMediaAttributes(String ownerKey) {
		addMediaAttributes(RequestParam.OWNERKEY, ownerKey);
	}

	public String getOwnerKey() {
		return (String) mediaAttributesMap_.get(RequestParam.OWNERKEY);
	}
}
