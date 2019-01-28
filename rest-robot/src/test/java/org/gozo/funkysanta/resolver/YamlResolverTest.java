package org.gozo.funkysanta.resolver;

import org.gozo.funkysanta.config.AppConfig;
import org.gozo.funkysanta.rest.client.model.WSConfiguration;
import org.gozo.funkysanta.util.ClassPathFileReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class YamlResolverTest {

	@Autowired
	private YamlTemplateResolver yamlTemplateResolver;

	@Autowired
	private ClassPathFileReader classPathFileReader;

	@Test
	public void testYamlReader() {
		String content = classPathFileReader.read("/config.yaml");
		WSConfiguration config = yamlTemplateResolver.resolve(content, WSConfiguration.class);
		assertNotNull(config.getMeta().getAddress());	
	}

	
}
