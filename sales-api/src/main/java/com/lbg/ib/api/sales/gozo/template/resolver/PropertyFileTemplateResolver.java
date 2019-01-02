package com.lbg.ib.api.sales.gozo.template.resolver;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.gozo.util.ClassPathFileReader;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

public class PropertyFileTemplateResolver implements TemplateResolver {

	@Autowired
	private RequestBodyResolver jsonResolver;

	@Autowired
	private ClassPathFileReader classPathFileReader;
	
	public <T> T resolve(String location, Class<T> clazz) {
		return jsonResolver.resolve(classPathFileReader.read(location), clazz);
	}
}
