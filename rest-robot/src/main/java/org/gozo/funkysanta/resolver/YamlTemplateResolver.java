package org.gozo.funkysanta.resolver;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
public class YamlTemplateResolver implements TemplateResolver {


	public <T> T resolve(String content, Class<T> clazz) {
		T instance = null;
		try {
			instance = new Yaml().loadAs(content, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
			// Handle the exception
			// Stop processing if the configuration is missing
		}
		// throw an exception here
		return instance;
	}

}
