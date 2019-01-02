package com.lbg.ib.api.sales.gozo.template.resolver;

public interface TemplateResolver {
	
	public <T> T resolve(String location, Class<T> clazz);

}
