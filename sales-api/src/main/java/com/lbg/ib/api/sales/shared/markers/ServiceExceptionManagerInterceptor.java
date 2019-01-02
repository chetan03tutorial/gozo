package com.lbg.ib.api.sales.shared.markers;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
@Aspect
public class ServiceExceptionManagerInterceptor {

	@Autowired
	private LoggerDAO logger;

	@Around("@annotation(com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager)")
	public Object handlePcaException(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = methodName(joinPoint);
		try {
			Object result = joinPoint.proceed();
			logger.traceLog(ServiceExceptionManagerInterceptor.class,
					"Successfully executed the method " + methodName + " in service " + joinPoint.getTarget().getClass());
			return result;
		} catch (ServiceError ex) {
			logger.traceLog(ServiceError.class, " Service Error in executing method " + methodName
					+ " in service " + joinPoint.getTarget().getClass().getSimpleName());
			logger.logException(this.getClass(), ex);
			throw ex;
		} catch (InvalidFormatException ex) {
			logger.traceLog(InvalidFormatException.class, " Invalid Format exception in the user journey " + methodName
					+ " in service " + joinPoint.getTarget().getClass().getSimpleName());
			logger.logException(this.getClass(), ex);
			throw ex;
		} catch (ServiceException ex) {
			logger.traceLog(ServiceException.class, " Service Exception in executing method " + methodName
					+ " in service " + joinPoint.getTarget().getClass().getSimpleName());
			logger.logException(this.getClass(), ex);
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		} catch (RuntimeException e) {
			logger.traceLog(ServiceException.class, "Unexpected exception in executing method " + methodName
					+ " in service " + joinPoint.getTarget().getClass().getSimpleName());
			logger.logException(this.getClass(), e);
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		}
	}

	private String methodName(ProceedingJoinPoint joinPoint) {
		String methodName = "";
		final Signature signature = joinPoint.getStaticPart().getSignature();
		if (signature instanceof MethodSignature) {

			MethodSignature ms = (MethodSignature) signature;
			Method method = ms.getMethod();
			methodName = method.getName();
		}
		return methodName;
	}
}
