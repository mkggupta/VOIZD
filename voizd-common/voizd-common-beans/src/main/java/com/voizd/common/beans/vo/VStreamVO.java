package com.voizd.common.beans.vo;

public class VStreamVO {
 public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStartLimit() {
		return startLimit;
	}
	public void setStartLimit(int startLimit) {
		this.startLimit = startLimit;
	}
	public int getEndLimit() {
		return endLimit;
	}
	public void setEndLimit(int endLimit) {
		this.endLimit = endLimit;
	}
	 public Long getVisitorId() {
			return visitorId;
	}
	public void setVisitorId(Long visitorId) {
			this.visitorId = visitorId;
	}
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
	 private Long visitorId;
	 private Long userId;
	 private String type;
	 private int startLimit;
	 private int endLimit;
     private boolean hasNext;

}
