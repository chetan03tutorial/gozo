package com.lbg.ib.api.sales.gozo.tasks.handlers;

import org.springframework.stereotype.Component;

@Component
public class ServiceConfigManager {

	/*
	 * @Autowired private ServiceMessageMapperHandler messageMapperManager;
	 * 
	 * @Autowired private ModuleContext beanLoader;
	 * 
	 * private final static String SERVICE_NAME_METHOD = "getServiceName"; private
	 * final static String GETTER_METHOD_PREFIX = "get";
	 * 
	 * public <T> T invokeService(ServiceContext context, Class<T> returnType) {
	 * Method operation = null; Object serviceResponse = null; try { Remote service
	 * = getServiceEndpointInterface(context); Class<? extends Remote>
	 * serviceInterface = service.getClass(); Object serviceRequest =
	 * context.getServiceRequest(); operation =
	 * serviceInterface.getDeclaredMethod(context.getOperation(),
	 * serviceRequest.getClass()); //Class<?> returnType =
	 * operation.getReturnType(); serviceResponse = operation.invoke(service,
	 * serviceRequest); } catch (Exception ex) { ex.printStackTrace(); }
	 * 
	 * return (T)serviceResponse; }
	 * 
	 * private Remote getServiceEndpointInterface(ServiceContext context) throws
	 * NoSuchMethodException, SecurityException, IllegalAccessException,
	 * IllegalArgumentException, InvocationTargetException { Class<AgnosticService>
	 * locatorType = context.getLocator(); AgnosticService serviceLocator =
	 * beanLoader.getService(locatorType); Method serviceNameMethod =
	 * locatorType.getDeclaredMethod(SERVICE_NAME_METHOD); QName qName = (QName)
	 * serviceNameMethod.invoke(serviceLocator); String seiMethodName =
	 * GETTER_METHOD_PREFIX.concat(qName.getLocalPart()); Method seiMethod =
	 * locatorType.getDeclaredMethod(seiMethodName, URL.class); return (Remote)
	 * seiMethod.invoke(serviceLocator, beanLoader.getBeanById(context.getAddress(),
	 * URL.class)); }
	 * 
	 * public String getServiceName(ServiceContext context) throws
	 * NoSuchMethodException, SecurityException, IllegalAccessException,
	 * IllegalArgumentException, InvocationTargetException { Class<AgnosticService>
	 * locatorType = context.getLocator(); AgnosticService serviceLocator =
	 * beanLoader.getService(locatorType); Method serviceNameMethod =
	 * locatorType.getDeclaredMethod(SERVICE_NAME_METHOD); QName qName = (QName)
	 * serviceNameMethod.invoke(serviceLocator); return qName.getLocalPart(); }
	 * 
	 * public Object prepareRequest(MessageMapper messageMapper, Object[]
	 * methodParameters) { return messageMapperManager.prepareRequest(messageMapper,
	 * methodParameters); }
	 * 
	 * public Object prepareResponse(MessageMapper messageMapper, Object
	 * serviceResponse) { return messageMapperManager.prepareResponse(messageMapper,
	 * serviceResponse); }
	 */
}
