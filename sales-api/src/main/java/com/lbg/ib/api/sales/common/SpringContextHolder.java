
package com.lbg.ib.api.sales.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {

        applicationContext = context;

    }

    /**
     * Returns the bean defined, to be used in non spring wired component.
     * 
     * @param clazzType
     * @return
     */
    public static <T> T getBean(Class<T> clazzType) {
        return applicationContext.getBean(clazzType);

    }
}
