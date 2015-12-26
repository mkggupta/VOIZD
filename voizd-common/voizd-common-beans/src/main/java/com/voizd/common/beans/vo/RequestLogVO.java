/**
 * 
 */
package com.voizd.common.beans.vo;

import java.util.Date;

/**
 * @author Manish
 * 
 */
public class RequestLogVO {

	private long id;
	private String requestHeader;
	private String request;
	private String response;
	private String requestType;
	private long userId;
	private String imeiId;
	private Date requestDate;
	private String ip;
	private int status;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the requestHeader
	 */
	public String getRequestHeader() {
		return requestHeader;
	}

	/**
	 * @param requestHeader
	 *            the requestHeader to set
	 */
	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the imeiId
	 */
	public String getImeiId() {
		return imeiId;
	}

	/**
	 * @param imeiId
	 *            the imeiId to set
	 */
	public void setImeiId(String imeiId) {
		this.imeiId = imeiId;
	}

	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	/**
	 * @param requestDate
	 *            the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	/**
	 * 
	 */
	public RequestLogVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param requestHeader
	 * @param request
	 * @param response
	 * @param requestType
	 * @param userId
	 * @param imeiId
	 * @param requestDate
	 */
	public RequestLogVO(String requestHeader, String request, String response, String requestType, long userId, String imeiId, Date requestDate, String ip,
			int status) {
		super();
		this.requestHeader = requestHeader;
		this.request = request;
		this.response = response;
		this.requestType = requestType;
		this.userId = userId;
		this.imeiId = imeiId;
		this.requestDate = requestDate;
		this.ip = ip;
		this.status = status;
	}

	/**
	 * @param id
	 * @param requestHeader
	 * @param request
	 * @param response
	 * @param requestType
	 * @param userId
	 * @param imeiId
	 * @param requestDate
	 */
	public RequestLogVO(long id, String requestHeader, String request, String response, String requestType, long userId, String imeiId, Date requestDate,
			String ip, int status) {
		super();
		this.id = id;
		this.requestHeader = requestHeader;
		this.request = request;
		this.response = response;
		this.requestType = requestType;
		this.userId = userId;
		this.imeiId = imeiId;
		this.requestDate = requestDate;
		this.ip = ip;
		this.status = status;
	}

}
