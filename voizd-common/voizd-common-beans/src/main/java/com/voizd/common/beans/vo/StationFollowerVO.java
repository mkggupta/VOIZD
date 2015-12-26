/**
 * 
 */
package com.voizd.common.beans.vo;

public class StationFollowerVO {
	
	private Long followerId;
	private Long stationId;
	private Long id;
	private Byte status;
	private Long followeeId;
	
	
	public Long getFolloweeId() {
		return followeeId;
	}
	public void setFolloweeId(Long followeeId) {
		this.followeeId = followeeId;
	}
	public Long getFollowerId() {
		return followerId;
	}
	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	

}
