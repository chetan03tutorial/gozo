package org.gozo.funkysanta.resolver;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonResolver {

	public <T> T resolve(String content, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(content, clazz);
		} catch (IOException var7) {
			String message = var7.getMessage();
			String[] split = var7.getMessage().split("\n");
			if (split.length > 0) {
				message = split[0];
			}
			return null;
		}
	}
}
