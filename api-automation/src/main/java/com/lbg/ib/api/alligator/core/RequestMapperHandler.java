package com.lbg.ib.api.alligator.core;

import static com.lbg.ib.api.alligator.web.response.ServiceResponse.withResult;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.alligator.constants.ContentType;
import org.aopalliance.aop.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.annotations.handler.AnnotationHandler;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.http.HttpClientManager;
import com.lbg.ib.api.alligator.http.UriResolver;
import com.lbg.ib.api.alligator.lang.JoinPointHelper;
import com.lbg.ib.api.alligator.lang.MethodDetails;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.util.InvalidFormatException;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;
import com.lbg.ib.api.alligator.web.response.ServiceResponse;


@Aspect
public class RequestMapperHandler implements Advice {

	@Autowired
	protected UriResolver uriResolver;

	@Autowired
	private HttpClientManager clientManager;

	private Map<Class<?>, AnnotationHandler> handlers;

	public void setHandlers(Map<Class<?>, AnnotationHandler> handlers) {
		this.handlers = handlers;
	}
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Around("@annotation(com.lbg.ib.api.alligator.annotations.RestInvoker)")
	public void mapRequest(ProceedingJoinPoint joinPoint) throws InvalidFormatException {
		
		
		BaseFeatures feature = ((BaseFeatures) joinPoint.getTarget());
		ServiceRequest request = new ServiceRequest(); 
		feature.setRequest(request);		
		request.setCookies(feature.getCookies());
		
		MethodDetails methodDetails = JoinPointHelper.getMethodDetails(joinPoint);
		Map<String, MethodParameter> methodParams = methodDetails.getMethodParams();
		
		for (Annotation annotation : methodDetails.getAnnotations()) {
			if (RestInvoker.class.isAssignableFrom(annotation.annotationType())) {
				RestInvoker callerAnnotation = (RestInvoker) annotation;
				request.setEndPoint(uriResolver.getApiEndpoint(callerAnnotation.service()));
				request.setRequestMethod(callerAnnotation.method());
				request.addHeaderParam("Content-Type", resolveContentType(callerAnnotation.contentType()));
			}
		}
		
		for(String paramName : methodParams.keySet()) {
			MethodParameter parameter = methodParams.get(paramName);
			validateParameterAnnotations(parameter.getParamName(), parameter.getAnnotations());
			processParameterAnnotations(request, parameter);
		}
		
		try {
			joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		logger.info("Invoking " + request.getEndPoint());
		ServiceResponse response = withResult(clientManager.invoke(request));
		feature.setCookies(response.getCookies());
		feature.setResponse(response);

	}
	
	private String resolveContentType(ContentType contentType)
	{
		switch(contentType)
		{
			case JSON :
				return "application/json";
			case XML :
				return "text/xml";
			case MULTIPART :
				return "multipart/form-data";
			default :
				return "application/json";
				
		}
	}

	private boolean validateParameterAnnotations(String paramName, List<Annotation> paramAnnotations) {
		if (  /*paramAnnotations.isEmpty() ||*/ paramAnnotations.size() > 1) {
			throw new IllegalArgumentException("Invalid number of marker annotation on the parameter, " + paramName);
		}
		return true;
	}

	private void processParameterAnnotations(ServiceRequest request, MethodParameter parameter) {
		for (Annotation paramAnnotation : parameter.getAnnotations()) {
			logger.debug("Jai Shri Ganeshaya Namh !");
			if(logger.isDebugEnabled()){
				logger.debug("Jai Shri Ganeshaya Namh !!");
				logger.debug("getWelcome is executed!");
			}
			/*System.out.println("Jai Shri Ram !! ");*/
			AnnotationHandler handler = handlers.get(paramAnnotation.annotationType());
			if (handler != null) {
				handler.handle(request, parameter);//, paramAnnotation);
			} else {
				throw new IllegalArgumentException(
						"No handler is registered for the annotation " + paramAnnotation.annotationType().getName());
			}
		}
	}
}
