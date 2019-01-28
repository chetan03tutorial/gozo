package org.gozo.funkysanta.rest.client;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.gozo.funkysanta.commands.Command;
import org.gozo.funkysanta.rest.client.model.CommandEnvelope;
import org.gozo.funkysanta.rest.client.model.InterceptorEnvelope;
import org.gozo.funkysanta.exception.ServiceConfigurationException;
import org.gozo.funkysanta.exception.UnrecognizedCommandException;
import org.gozo.funkysanta.lang.BeanResolver;
import org.gozo.funkysanta.rest.client.model.Directive;
import org.gozo.funkysanta.rest.client.model.HandlerEnvelope;
import org.gozo.funkysanta.rest.client.model.WSConfiguration;
import org.gozo.funkysanta.rest.client.model.WebServiceConfiguration;
import org.gozo.funkysanta.resolver.TemplateResolver;
import org.gozo.funkysanta.util.ClassLocator;
import org.gozo.funkysanta.util.ClassPathFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.rpc.handler.Handler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@Component
public class RestConfigurationParser {

    @Autowired
    private ClassPathFileReader classPathFileReader;

    @Autowired
    @Qualifier("yamlTemplateResolver")
    private TemplateResolver templateResolver;

    @Autowired
    private BeanResolver beanResolver;

    @Autowired
    private ClassLocator classLocator;


    private void listFilesForFolder(final File folder, List<String> fileNames) {
        if (!folder.isDirectory()){
            fileNames.add(folder.getName());
        }
        for (final File fileEntry : folder.listFiles()) {
            listFilesForFolder(fileEntry, fileNames);
        }
    }


    @PostConstruct
    public void buildWebServiceConfiguration() {
        /*ServiceConfigurationParser path = beanResolver.getBean("serviceConfigurationParser",ServiceConfigurationParser.class);
        final File folder = new File(path.getLocation());
        List<String> files = new LinkedList<>();
        listFilesForFolder(folder,files);
        files.add(path.getLocation());
        for (String file : files) {
            String content = classPathFileReader.read(file);
            WSConfiguration wsConfig = templateResolver.resolve(content, WSConfiguration.class);
            prepareWebServiceConfiguration(wsConfig);
        }*/
        List<String> files = new LinkedList<>();
        files.add("/config.yaml");
        for (String file : files) {
            String content = classPathFileReader.read(file);
            WSConfiguration wsConfig = templateResolver.resolve(content, WSConfiguration.class);
            prepareWebServiceConfiguration(wsConfig);
        }
    }


    private WebServiceConfiguration prepareWebServiceConfiguration(WSConfiguration wsConfig) {
        String serviceName = wsConfig.getMeta().getServiceName();
        beanResolver.registerBeanDefinition(serviceName, WebServiceConfiguration.class);
        WebServiceConfiguration configuration = beanResolver.getBean(serviceName, WebServiceConfiguration.class);
        Class<? extends AgnosticService> clazz = classLocator.loadClass(wsConfig.getMeta().getLocator());
        configuration.setOperation(wsConfig.getMeta().getOperation());
        configuration.setLocator(beanResolver.getBean(clazz));
        configuration.setLocatorType(clazz);
        configuration.setCommandEnvelopes(Collections.unmodifiableList(buildCommandList(wsConfig)));
        configuration.setMessageInterceptors((buildInterceptorList(wsConfig)));
        try {
            configuration.setAddress(beanResolver.getBean(wsConfig.getMeta().getAddress(), URL.class));
        } catch (MalformedURLException e) {
            throw new ServiceConfigurationException("Invalid Service Endpoint " + e.getMessage(), e);
        }
        return configuration;
    }


    public List<CommandEnvelope> buildCommandList(WSConfiguration wsConfig) {
        List<CommandEnvelope> commandEnvelopes = new LinkedList<CommandEnvelope>();
        for (Directive directive : wsConfig.getCommands()) {
            Object receiver = StringUtils.isNotEmpty(directive.getReceiver())
                    ? classLocator.resolveBeanObjectVsClass(directive.getReceiver())
                    : null;
            Command command = (Command) classLocator.resolveBeanObjectVsClass(directive.getCommand());
            commandEnvelopes.add(new CommandEnvelope(command, receiver));
        }
        return commandEnvelopes;
    }

    public Map<String, InterceptorEnvelope> buildInterceptorList(WSConfiguration wsConfig) {
        Map<String, InterceptorEnvelope> interceptors = new TreeMap<>();
        List<HandlerEnvelope> handlers = wsConfig.getMeta().getInterceptors();
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (HandlerEnvelope handlerEnvelope : wsConfig.getMeta().getInterceptors()) {
                Object interceptor = StringUtils.isNotEmpty(handlerEnvelope.getHandler())
                        ? classLocator.resolveBeanObjectVsClass(handlerEnvelope.getHandler())
                        : null;
                if (!Handler.class.isAssignableFrom(interceptor.getClass())) {
                    throw new UnrecognizedCommandException("HandlerClass must implement the javax.xml.rpc.handler.Handler interface");
                }
                Handler handler = (Handler) interceptor;
                InterceptorEnvelope interceptorEnvelope = new InterceptorEnvelope();
                interceptorEnvelope.setHandler(handler);
                interceptorEnvelope.setConfig(handlerEnvelope.getConfig());
                int order = handlerEnvelope.getOrder() == 0 ? interceptors.size() : handlerEnvelope.getOrder();
                interceptorEnvelope.setOrder(order);
                interceptors.put(String.valueOf(interceptorEnvelope.getOrder()), interceptorEnvelope);
            }
        }
        return interceptors;
    }

    /*public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }*/
}
