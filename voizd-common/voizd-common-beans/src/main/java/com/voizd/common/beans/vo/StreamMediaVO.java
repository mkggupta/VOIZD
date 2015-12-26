/**
 * 
 */
package com.voizd.common.beans.vo;

import java.util.Date;

public class StreamMediaVO {
	
	  private Long streamId;
	  private Byte status;
	  private String fileId;
	  private String mediaType;
	  private int ordering;
	  private String ext;
	  private long size;
	  private Date timeStamp;
	  
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getStreamId() {
		return streamId;
	}
	public void setStreamId(Long streamId) {
		this.streamId = streamId;
	}

}
