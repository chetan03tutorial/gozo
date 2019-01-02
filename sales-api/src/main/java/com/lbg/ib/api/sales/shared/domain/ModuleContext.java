package com.lbg.ib.api.sales.shared.domain;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.spring.SpringContextHolder;

@Component
public class ModuleContext {

    public <T> T getService(final Class<T> requiredType) {
        return SpringContextHolder.getBean(requiredType);
    }
    
    public <T> T getBeanById(final String id, final Class<T> requiredType) {
        return SpringContextHolder.getBean(id,requiredType);
    }
}
