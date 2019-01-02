package com.lbg.ib.api.sales.shared.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ServiceErrorHandlerTest {

	@InjectMocks 
	private ServiceErrorHandler serviceErrorHandler;
	
	@Mock
	private LoggerDAO logger;
	
	@Test
	public void testExceptionMapping() {
		ServiceError error = new ServiceError(new Object());
		serviceErrorHandler.toResponse(error);
	}
}
