package com.lbg.ib.api.automation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EnvironmentConfig {

    private static final String ENV = "env";
    @Autowired
    @Qualifier("configProps")
    private Properties property;

    public String getProperty(String key) {
        return (String) property.get(key);
    }

    public String getCurrentEnvironment() {
        return System.getProperty(ENV);
    }

    public String getHostInformation(String brand) {
        System.out.println("brand information::::" + brand);
        String host;
        switch (brand) {
            case "lloyds": {
                host = System.getProperty("endpoint.lloyds");
                break;
            }
            case "bos": {
                host = System.getProperty("endpoint.bos");
                break;
            }
            case "halifax": {
                host = System.getProperty("endpoint.halifax");
                break;
            }
            case "ib": {
                host = System.getProperty("endpoint.ib");
                break;
            }
            default: {
                host = System.getProperty("endpoint.lloyds");
            }
        }
        System.out.println("host value::::::::::::" + host);
        return host;
    }
}
