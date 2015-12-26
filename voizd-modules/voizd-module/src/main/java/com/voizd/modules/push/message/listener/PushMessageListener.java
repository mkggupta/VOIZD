package com.voizd.modules.push.message.listener;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.modules.agent.AgentFactory;
import com.voizd.modules.push.message.service.IphonePushMessageServiceImpl;
import com.voizd.modules.push.message.service.PushMessageService;

public class PushMessageListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(PushMessageListener.class);

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				logger.debug("JMS  messageobj PushMessageListener::"+message);
				Object obj = ((ObjectMessage) message).getObject();
				if (obj instanceof HashMap) {
					@SuppressWarnings("unchecked")
					Map<String, Object> params = (Map<String, Object>) obj;
					logger.debug("JMS  messageobj PushMessageListener:: params :: "+params);
					String name=(String) params.get("platform");
					logger.debug("JMS  messageobj PushMessageListener:: name :: "+name);
					PushMessageService pushMessageService = AgentFactory.getAgent(name);
					pushMessageService.pushNotification(params);
				/*	IphonePushMessageServiceImpl  ipushMessageServiceImpl = new IphonePushMessageServiceImpl();
					logger.debug("JMS  one PushMessageListener:: params :: "+params);
					ipushMessageServiceImpl.pushNotification(params);
					logger.debug("JMS  two PushMessageListener:: params :: "+params);*/
				}
			}
		}catch (Exception e) {
			logger.error("Exception while getting messages "+e.getLocalizedMessage(),e);
		}
	}
}
