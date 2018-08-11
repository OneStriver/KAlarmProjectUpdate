package com.fh.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** 
 * 通过该类即可在普通工具类里获取spring管理的bean 
 */  
public final class SpringContextUtil implements ApplicationContextAware {  
    private static ApplicationContext applicationContext = null;  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  
        if (SpringContextUtil.applicationContext == null) {  
            SpringContextUtil.applicationContext = applicationContext;  
        }  
    }  
  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
  
    public static Object getBean(String name) {  
        return getApplicationContext().getBean(name);  
    }
    
} 
