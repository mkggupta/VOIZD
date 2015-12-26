package com.voizd.framework.jms.messaging.util;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.voizd.framework.jms.messaging.util.JMSUtils;

public class RoutingSlipAccessor {

	private static final Logger logger = LoggerFactory
			.getLogger(RoutingSlipAccessor.class);
	/*private Map<String, Integer> priorityMap;

	public void setPriorityMap(Map<String, Integer> priorityMap) {
		this.priorityMap = priorityMap;
	}*/

	public Map<String, String> getRoutingInfo(RoutingInfo info) {
		Preconditions.checkNotNull(info);
		String appType = "Agents";
		String operation = "";
		String header = (info.getAgentHeader() != null) ? info.getAgentHeader().toLowerCase(): "";
		
		operation = getCommand(info.getMsgText());

		String routingSlip = appType + "." + header + "." + operation;
		Map<String, String> map = new HashMap<String, String>();
		map.put(JMSUtils.ROUTING_SLIP, routingSlip);
		map.put(JMSUtils.ROUTING_PARAM, info.getMsgText());
		if (info.getMimeType() != null)
			map.put(JMSUtils.MIME_TYPE, info.getMimeType());
		if (info.getVendor() != null)
			map.put(JMSUtils.VENDOR, info.getVendor());
		if (info.getUserid() != null)
			map.put(JMSUtils.USERID, info.getUserid().toString());
		if(info.getRequestid() !=null)
			map.put("requestid", info.getRequestid());
		if(info.getRequestType() !=null)
			map.put("requestType", info.getRequestType());

	/*	Integer priority = priorityMap.get(header);
		if (priority != null)
			map.put(JMSUtils.PRIORITY, priority.toString());*/

		return map;
	}

	private String getCommand(String text) {

		if (text != null && text.length() > 0) {
			int index = text.indexOf("::");
			if (index == -1)
				index = text.length();

			return text.substring(0, index);
		}
		return "";
	}

	public Message populateRoutingInfo(Message msg, RoutingInfo info) {
		try {
			Map<String, String> map = getRoutingInfo(info);
			for (String key : map.keySet()) {
				msg.setStringProperty(key, map.get(key));
			}

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}

		return msg;
	}

}
