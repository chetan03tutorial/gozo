package org.gozo.funkysanta.util;

import org.gozo.funkysanta.exception.UnrecognizedCommandException;
import org.gozo.funkysanta.lang.BeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassLocator {

    @Autowired
    private BeanResolver beanResolver;

    public Object resolveBeanObjectVsClass(String className) {
        Object beanInstance = beanResolver.getBeanByName(className);
        if(beanInstance == null){
            beanInstance = beanResolver.getBean(loadClass(className));
        }
        return beanInstance;
    }

    public <T> Class<T> loadClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (ClassNotFoundException e) {
            throw new UnrecognizedCommandException("Unrecognized Command Class :" + className, e);
        } finally {
            // do the clean up here.
        }
    }

    public <T> T loadReceiver(Class<T> receiverClass){
        T receiver = beanResolver.getBean(receiverClass);
        return receiver;
    }
}
