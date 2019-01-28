package org.gozo.funkysanta.exception;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanInitializationException;

public class ServiceConfigurationException extends BeanInitializationException {


    public ServiceConfigurationException(String msg) {
        super(msg);
    }

    public ServiceConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
