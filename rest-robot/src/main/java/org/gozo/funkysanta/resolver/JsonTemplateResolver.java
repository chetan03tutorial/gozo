package org.gozo.funkysanta.resolver;


import org.gozo.funkysanta.util.ClassPathFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonTemplateResolver implements TemplateResolver {

    @Autowired
    private JsonResolver jsonResolver;

    @Autowired
    private ClassPathFileReader classPathFileReader;

    public <T> T resolve(String location, Class<T> clazz) {
        return jsonResolver.resolve(classPathFileReader.read(location), clazz);
    }
}
