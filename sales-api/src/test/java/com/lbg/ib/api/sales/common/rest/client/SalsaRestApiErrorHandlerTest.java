package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.error.ResponseErrorFromOcis;
import com.lbg.ib.api.sales.common.exception.PaoServiceException;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalsaRestApiErrorHandlerTest {

	@InjectMocks
	private SalsaRestApiErrorHandler handler;
	
	@Mock
	private JsonBodyResolver jsonBodyResolver;
	
	@Mock
	private GalaxyErrorCodeResolver resolver;

	@Mock
	private static MockClientHttpResponse clientResponse;
	
	@Mock
	private LoggerDAO logger;
	
	private static final String RESPONSE_ERROR_JSON = "{\"code\":\"400\", \"message\":\"Message\"}";
	
	private InputStream stubInputStream;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void testHasNoError(){
		try {
			when(clientResponse.getStatusCode()).thenReturn(HttpStatus.OK);
			assertFalse(handler.hasError(clientResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testHasError(){
		try {
			when(clientResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
			assertTrue(handler.hasError(clientResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test(expected=PaoServiceException.class)
	public void testHandleError() throws IOException, InvalidFormatException {
		ResponseError responseError = new ResponseError();
		stubInputStream =  IOUtils.toInputStream(RESPONSE_ERROR_JSON);
		when(clientResponse.getBody()).thenReturn(stubInputStream);
		when(jsonBodyResolver.resolve(stubInputStream, ResponseError.class)).thenReturn(responseError);
		handler.handleError(clientResponse);
	}
	
	@Test(expected=PaoServiceException.class)
	public void testInvalidErrorMessageFromService() throws IOException, InvalidFormatException {
	    ResponseErrorFromOcis responseError = new ResponseErrorFromOcis();
	    ResponseError responseErrorNew = new ResponseError();
		stubInputStream =  IOUtils.toInputStream(RESPONSE_ERROR_JSON);
		when(clientResponse.getBody()).thenReturn(stubInputStream);
		when(jsonBodyResolver.resolve(stubInputStream, ResponseErrorFromOcis.class)).thenThrow(new InvalidFormatException("Invalid Format Exception"));
		when(resolver.resolve(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR)).thenReturn(responseErrorNew);
		handler.handleError(clientResponse);
	}
	
	private class MockClientHttpResponse implements ClientHttpResponse{

		public InputStream getBody() throws IOException {
			return null;
		}

		public HttpHeaders getHeaders() {
			return null;
		}

		public void close() {
		}

		public int getRawStatusCode() throws IOException {
			return 0;
		}

		public HttpStatus getStatusCode() throws IOException {
			return HttpStatus.OK;
		}

		public String getStatusText() throws IOException {
			return null;
		}
		
	}
}
