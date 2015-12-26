package com.voizd.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.constant.NotificationConstant;
import com.voizd.framework.jms.messaging.JmsSender;
import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;

public class JmsUtil {
	private static Logger logger = LoggerFactory.getLogger(JmsUtil.class);
	
	public void sendJmsPushMessage(JmsSender jmsSender,Long userId,Long id,boolean isPush,Byte opCode){
		try {
			HashMap<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put(NotificationConstant.CONTENTID,id);
			msgMap.put(NotificationConstant.PUSHMESSAGE,isPush);
			msgMap.put(NotificationConstant.USERID, userId);
			msgMap.put(NotificationConstant.OPERATION, opCode);
			RoutingInfo info = new RoutingInfo();
			info.setAgentHeader(NotificationConstant.NOTIFICATION_AGENT);
			info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
			jmsSender.sendMesage(msgMap, info);
			logger.info("JmsUtil.sendJmsPushMessage--"+id+"-- userId="+userId);
		}catch(Exception e){
			logger.error("sendJmsMessage " + e.getLocalizedMessage(), e);
		}
	}
}
