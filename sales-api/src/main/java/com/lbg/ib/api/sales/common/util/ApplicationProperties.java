package com.lbg.ib.api.sales.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    private static Properties properties;

    private ApplicationProperties() {
        // Comments for Sonar Violations
    }

    public static synchronized Properties getProperties() throws IOException {

        if (properties != null) {
            return properties;
        }
        InputStream inputStream = new ClassPathResource("/config/props/urca.properties").getInputStream();
        properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }

    public static String getPropValue(String propName) throws IOException {
        return (String) getProperties().get(propName);
    }

}
