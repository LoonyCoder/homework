package com.lagou.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getObject(String type) {
        Object object = null;
       // object = applicationContext.getBean(id);
        try {
            String[] beanNamesForType = applicationContext.getBeanNamesForType(Class.forName(type));
            object = applicationContext.getBean(beanNamesForType[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }
}

