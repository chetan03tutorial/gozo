package com.lbg.ib.api.alligator.annotations.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class AbstractAnnotationHandler implements AnnotationHandler {

	//private static Logger logger = Logger.getLogger(AbstractAnnotationHandler.class);
	protected final Log logger = LogFactory.getLog(getClass());
	
	public Map<String, Object> doHandle(Annotation paramAnnotation)
	{
		/*System.out.println("Jai Shri Ganeshaya Namh !");
		logger.debug("Jai Shri Ganeshaya Namh !");
		if(logger.isDebugEnabled()){
			logger.debug("Jai Shri Ganeshaya Namh !!");
			logger.debug("getWelcome is executed!");
		}*/

		Map<String,Object> result = new HashMap<String, Object>();
		
		Object propertyValue = null;
		for(Method annotationMethod : paramAnnotation.annotationType().getDeclaredMethods())
		{
			String methodName = annotationMethod.getName();
			Class<?> returnType = annotationMethod.getReturnType();
			try 
			{
				propertyValue = annotationMethod.invoke(paramAnnotation);
				// DO NOT DELETE THIS COMMENT
				/*Object argValue = annotationMethod.invoke(paramAnnotation);*/
			} catch (IllegalAccessException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
			result.put(methodName, propertyValue);
		}
		return result;
	}
	
}
