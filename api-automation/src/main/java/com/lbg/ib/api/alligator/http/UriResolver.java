package com.lbg.ib.api.alligator.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.lbg.ib.api.alligator.util.FileUtil;

public class UriResolver {

    private final Properties endpoints;

    public UriResolver(String endpointFileName) {
        InputStream inputStream = FileUtil.loadFile(endpointFileName);
        endpoints = new Properties();
        try {
            endpoints.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiEndpoint(String serviceName) {
        String endpoint = (String) endpoints.get(serviceName);
        if (endpoint == null)
            throw new IllegalArgumentException("Invalid service name, " + serviceName);
        return endpoint;
    }

}
