package com.voizd.modules.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import com.voizd.modules.push.message.service.PushMessageService;


public class AgentFactory implements ApplicationContextAware{
	private static ApplicationContext cntxt;
	private static Logger logger = LoggerFactory.getLogger(AgentFactory.class);
	public static PushMessageService getAgent(String destination) {
		if (destination.equalsIgnoreCase(AgentName.ANDROID)) {
			return getImpl("androidAgent");
		} else if(destination.equalsIgnoreCase(AgentName.IPHONE)) {
			return getImpl("iphoneAgent");
		}else {
			throw new RuntimeException("No Agent with name '" + destination + "'");
		}
		
	}
	
	private static PushMessageService getImpl(String name)
	{
		logger.debug("cntxt :::: "+cntxt+" , name "+cntxt);
		return (PushMessageService)cntxt.getBean(name);
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		cntxt = arg0;		
	}
}
