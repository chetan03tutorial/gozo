package com.lbg.ib.api.sales.util;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.map.ObjectMapper;

import com.lbg.ib.api.shared.exception.InvalidFormatException;

public class JsonConverter {
    
    public <T> T resolve(String content, Class<T> clazz) throws InvalidFormatException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            String message = e.getMessage();
            String[] split = e.getMessage().split("\n");
            if (split.length > 0)
                message = split[0];
            throw new InvalidFormatException("Invalid JSON format: " + message, e);
        }
    }

    public static <T> T resolve(InputStream content, Class<T> clazz) throws InvalidFormatException {
        ObjectMapper mapper = new ObjectMapper();
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
