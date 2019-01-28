package org.gozo.funkysanta.rest.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.gozo.funkysanta.commands.Command;
import org.gozo.funkysanta.rest.client.model.CommandEnvelope;
import org.gozo.funkysanta.rest.client.model.InterceptorEnvelope;
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

import com.ibm.ws.webservices.multiprotocol.AgnosticService;

import javax.xml.rpc.handler.Handler;

@Deprecated
@Component
public class RestServiceConfigurationResolver {

    @Autowired
    @Qualifier("yamlTemplateResolver")
    private TemplateResolver templateResolver;

    @Autowired
    private BeanResolver beanResolver;

    @Autowired
    private ClassLocator classLocator;

    @Autowired
    private ClassPathFileReader classPathFileReader;

    public WebServiceConfiguration resolve(String location) {
        String content = classPathFileReader.read(location);
        return prepareWebServiceConfiguration(templateResolver.resolve(content, WSConfiguration.class));
    }

    private WebServiceConfiguration prepareWebServiceConfiguration(WSConfiguration wsConfig) {
        WebServiceConfiguration configuration = new WebServiceConfiguration();
        Class<? extends AgnosticService> clazz = classLocator.loadClass(wsConfig.getMeta().getLocator());
        configuration.setOperation(wsConfig.getMeta().getOperation());
        configuration.setLocator(beanResolver.getBean(clazz));
        configuration.setLocatorType(clazz);
        configuration.setCommandEnvelopes(Collections.unmodifiableList(buildCommandList(wsConfig)));
        configuration.setMessageInterceptors((buildInterceptorList(wsConfig)));
        try {
            configuration.setAddress(beanResolver.getBean(wsConfig.getMeta().getAddress(), URL.class));
        } catch (MalformedURLException e) {
            // log exception and inform the administrator about incorrect endpoint
            e.printStackTrace();
        }

        return configuration;
    }

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
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

    /*
     * public <U extends Command, V> CommandEnvelope commandEnvelope(Class<U>
     * commandClass, Class<V> receiverClazz) { CommandEnvelope commandEnvelope = new
     * CommandEnvelope(); U command = (U) beanResolver.getBean(commandClass);
     * commandEnvelope.setCommand(command); V receiver = (V)
     * beanResolver.getBean(receiverClazz); commandEnvelope.setReceiver(receiver);
     * return commandEnvelope; }
     */

}
