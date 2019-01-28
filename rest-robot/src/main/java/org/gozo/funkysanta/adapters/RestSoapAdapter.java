package org.gozo.funkysanta.adapters;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.gozo.funkysanta.commands.CommandChain;
import org.gozo.funkysanta.commands.CommandChainBuilder;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.exception.ServiceConfigurationException;
import org.gozo.funkysanta.lang.BeanResolver;
import org.gozo.funkysanta.lang.JoinPointHelper;
import org.gozo.funkysanta.lang.method.MethodDetails;
import org.gozo.funkysanta.marker.HandleException;
import org.gozo.funkysanta.marker.RestServiceConfiguration;
import org.gozo.funkysanta.rest.client.model.WebServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RestSoapAdapter {

	@Autowired
	private CommandChainBuilder commandBuilder;

	@Autowired
	private BeanResolver beanResolver;

	@HandleException
	@Around("@annotation(org.gozo.funkysanta.marker.RestServiceConfiguration)")
	public Object handle(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {

		MethodDetails methodDetails = JoinPointHelper.getMethodDetails(joinPoint);
		String serviceName = methodDetails.resolveAnnotation(RestServiceConfiguration.class).value();

		if (StringUtils.isEmpty(serviceName) || beanResolver.getBean(serviceName,WebServiceConfiguration.class) == null) {
			throw new ServiceConfigurationException("Service "+ serviceName +" not found in the configuration information provided");
		}

		WebServiceConfiguration wsConfiguration = beanResolver.getBean(serviceName,WebServiceConfiguration.class);
		CommandContext context = buildCommandContext(methodDetails.getMethodArguments(), wsConfiguration);
		CommandChain commandChain = commandBuilder.buildCommandChain(wsConfiguration);

		/*Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}*/


		commandChain.execute(context);
		return new ResponseBuilderImpl().status(Response.Status.OK).entity(context.get(CommandContext.SERVICE_RESPONSE))
				.entity(context.get(CommandContext.SERVICE_RESPONSE)).build();
	}

	private CommandContext buildCommandContext(Object[] args, WebServiceConfiguration wsConfiguration) {
		CommandContext _context = new CommandContext();
		_context.set(CommandContext.METHOD_ARGUMENTS, args);
		_context.set(CommandContext.SERVICE_CONFIG, wsConfiguration);
		return _context;
	}
}
