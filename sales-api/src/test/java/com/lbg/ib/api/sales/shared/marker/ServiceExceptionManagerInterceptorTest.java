package com.lbg.ib.api.sales.shared.marker;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManagerInterceptor;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ServiceExceptionManagerInterceptorTest {

	@InjectMocks
	private ServiceExceptionManagerInterceptor interceptor;

	@Mock
	private ProceedingJoinPoint joinPoint;

	@Mock
	private MethodSignature methodSignature;

	private Method method;

	@Mock
	private StaticPart staticPart;

	@Mock
	private LoggerDAO logger;

	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		method = this.getClass().getMethod("dummyMethod");
		when(joinPoint.getStaticPart()).thenReturn(staticPart);
		when(staticPart.getSignature()).thenReturn(methodSignature);
		when(methodSignature.getMethod()).thenReturn(method);
		when(joinPoint.getTarget()).thenReturn(this);
	}

	@Test
	public void testNoExceptionTracingWhenMethodIsNotFound() throws Throwable {
		interceptor.handlePcaException(joinPoint);
	}

	@Test(expected = ServiceError.class)
	public void testServiceErrorWhenMethodExecutionResultServerError() throws Throwable {
		doThrow(ServiceError.class).when(joinPoint).proceed();
		interceptor.handlePcaException(joinPoint);
		verify(logger, times(1)).logException(any(Class.class), any(Exception.class));
	}

	@Test(expected = ServiceException.class)
	public void testServiceExceptionWhenMethodExecutionResultServiceException() throws Throwable {
		doThrow(ServiceException.class).when(joinPoint).proceed();
		interceptor.handlePcaException(joinPoint);
		verify(logger, times(1)).logException(any(Class.class), any(Exception.class));
	}

	@Test(expected = ServiceException.class)
	public void testServiceExceptionWhenMethodExecutionResultRuntimeException() throws Throwable {
		doThrow(NullPointerException.class).when(joinPoint).proceed();
		interceptor.handlePcaException(joinPoint);
		verify(logger, times(1)).logException(any(Class.class), any(Exception.class));
	}

	public void dummyMethod() {
	}
}
