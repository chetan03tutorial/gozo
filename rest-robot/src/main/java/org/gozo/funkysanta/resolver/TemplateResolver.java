package org.gozo.funkysanta.resolver;

public interface TemplateResolver {
	
	public <T> T resolve(String location, Class<T> clazz);

}
