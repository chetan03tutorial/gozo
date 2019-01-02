package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.shared.exception.InvalidFormatException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonBodyResolver {

    @Autowired
    private ObjectMapper mapper;

    public <T> T resolve(InputStream content, Class<T> clazz) throws InvalidFormatException {
        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            String message = e.getMessage();
            String[] split = e.getMessage().split("\n");
            if (split.length > 0) {
                message = split[0];
            }
            throw new InvalidFormatException("Invalid JSON format: " + message, e);
        }
    }

    public <T> T resolve(String content, Class<T> clazz) throws InvalidFormatException {
        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            String message = e.getMessage();
            String[] split = e.getMessage().split("\n");
            if (split.length > 0) {
                message = split[0];
            }
            throw new InvalidFormatException("Invalid JSON format: " + message, e);
        }
    }
}
