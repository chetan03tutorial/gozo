package com.lbg.ib.api.sales.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public class Base {

    @Autowired
    protected LoggerDAO logger;

    public void setLogger(LoggerDAO logger) {
        this.logger = logger;
    }
}
