package com.voizd.dao.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.voizd.dao.service.ServiceFactory;

public class ContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx = null;
		    public static ApplicationContext getApplicationContext() {         
		        return ctx;    
		    }
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.ctx = arg0;
		ServiceFactory.setApplicationContext(arg0);

	}

}
