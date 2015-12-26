package com.voizd.framework.jms.messaging.util;

/**
 * Data Structure for defining the routing information on a message. The message 
 * sender needs to create and populate this structure so that the routing component
 * can create the appropriate routing Slip and attach it with the message header.
 * Since its quite possible that all attributes might not be available with the
 * producer, the information on the mandatory and optional attributes are listed
 * here so that the sender can take an informed decision.
 * 
 * <br/><b>Mandatory Information</b><br/>
 * agentHeader - The message destination name either in its trimmed format or 
 * agent friendly format.
 * <br/><b>Optional Information</b><br/>
 * mimeType - based on the message contentBitMap its either 
 * {@link}com.rocketalk.utils.jms.JMSUtils.MIME_TYPE_TEXT or com.rocketalk.utils.jms.JMSUtils.MIME_TYPE_VOICEFRAME
 * This will be of help if we plan to route messages based on mime type.
 * <br/>vendor - The Vendor name associated with the source user id. This will help
 * with providing quality of service based different vendors.
 * <br/>msgText - the actual text message that is being sent. This is needed in 
 * cases where multiple operations are associated with a particular agent.ex.
 * a:friendmgr has operations like add ,remove,bookmark etc. In case where 
 * operation level split is to be deployed then this parameter becomes important.
 * <br/>userid - sender user id.
 * <br/>requestid - a ramdom number that helps track message flow for diagnostics.
 * 
 * @author 
 *
 */
public class RoutingInfo {
	
	private String agentHeader;
	private String mimeType;
	private String vendor;
	private String msgText;
	private Integer userid;
	private String requestid;
	private String requestType;
	
	public String getAgentHeader() {
		return agentHeader;
	}
	public void setAgentHeader(String agentHeader) {
		this.agentHeader = agentHeader;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
