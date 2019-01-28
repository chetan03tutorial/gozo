package org.gozo.funkysanta.commands.rest;

import org.gozo.funkysanta.commands.AbstractCommand;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.rest.client.DefaultSoapMessageInterceptor;
import org.gozo.funkysanta.components.RpcInvoker;
import org.gozo.funkysanta.rest.client.model.InterceptorEnvelope;
import org.gozo.funkysanta.rest.client.model.WebServiceConfiguration;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SoapInvoker extends AbstractCommand {

    public void execute(CommandContext _context) {

        RpcInvoker rpcInvoker = _context.getReceiver(RpcInvoker.class);
        //TODO user Lamdas to resolve this dirtyness
        if (rpcInvoker != null) {
            // Perform user defined action here
            return;
        }
        hookSoapMessageInterceptors(_context);
        Object serviceResponse = invokeSoapService(_context);
        _context.set(CommandContext.SERVICE_RESPONSE, serviceResponse);
    }

    private Object invokeSoapService(CommandContext _context) {
        WebServiceConfiguration configuration = _context.get(CommandContext.SERVICE_CONFIG,
                WebServiceConfiguration.class);
        Object serviceResponse = null;
        Object serviceRequest = _context.get(CommandContext.SERVICE_REQUEST);
        try {
            Remote serviceInstance = configuration.getServiceEndpointInterface();
            Class<? extends Remote> sei = serviceInstance.getClass();
            Method operation = sei.getDeclaredMethod(configuration.getOperation(), serviceRequest.getClass());
            //Class<?> returnType = operation.getReturnType();
            serviceResponse = operation.invoke(serviceInstance, serviceRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serviceResponse;
    }

    private void hookSoapMessageInterceptors(CommandContext _context) {

        WebServiceConfiguration configuration = _context.get(CommandContext.SERVICE_CONFIG,
                WebServiceConfiguration.class);

        Map<String, InterceptorEnvelope> interceptors = configuration.getMessageInterceptors();

        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        QName[] qNames = {new QName(configuration.getServiceName())};
        handlerList.add(new HandlerInfo(DefaultSoapMessageInterceptor.class, interceptors, qNames));

        HandlerRegistry handlerRegistry = configuration.getHandlerRegistry();
        handlerRegistry.setHandlerChain(new QName(configuration.getServiceName()), handlerList);
    }
}
