package com.voizd.dao.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceFactory {
    private static ApplicationContext context_;

    public static <T> T getService(Class<T> type) {
        if (context_ == null) {
            synchronized (ServiceFactory.class) {
                context_ = new ClassPathXmlApplicationContext("datamodel-context.xml");
            }
        }
        return (T) context_.getBean(type.getName());
    }

    public static void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        context_ = arg0;
    }
}
