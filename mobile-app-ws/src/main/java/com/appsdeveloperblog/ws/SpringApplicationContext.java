package com.appsdeveloperblog.ws;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringApplicationContext implements ApplicationContextAware{

	@Autowired
	private static  ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		CONTEXT = context;
	}
	
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}
}
