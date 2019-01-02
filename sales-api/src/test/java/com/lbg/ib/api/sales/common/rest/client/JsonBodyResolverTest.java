package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.shared.exception.InvalidFormatException;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonBodyResolverTest {

	@InjectMocks
	private JsonBodyResolver jsonBodyResolver;
	
	@Mock
	private static ObjectMapper objectMapper;
	
	private static IOException exception;
	private static Object result = new Object();
	
	@Before
	public void setup(){
		exception = new IOException(" message[0] \n message[1]");
	}
	
	@Test
	public void testResolveStringContent() throws InvalidFormatException {
		try {
			when(objectMapper.readValue(anyString(),any(Class.class))).thenReturn(result);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		jsonBodyResolver.resolve("content", Object.class);
		assertTrue(result != null);
	}

	
	@Test
	public void testResolveInputStreamContent() throws InvalidFormatException {
		try {
			when(objectMapper.readValue(any(InputStream.class),any(Class.class))).thenReturn(result);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream stubInputStream = IOUtils.toInputStream("some test data for my input stream");
		jsonBodyResolver.resolve(stubInputStream, Object.class);
		assertTrue(result != null);
	}
	
	@Test(expected=InvalidFormatException.class)
	public void testInvalidFormatException() throws InvalidFormatException {
		try {
			when(objectMapper.readValue(any(InputStream.class),any(Class.class))).thenThrow(exception);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream stubInputStream = IOUtils.toInputStream("some test data for my input stream");
		jsonBodyResolver.resolve(stubInputStream, Object.class);
		assertTrue(result != null);
	}
	
	@Test(expected=InvalidFormatException.class)
	public void testInvalidFormatExceptionWithContent() throws InvalidFormatException {
		try {
			when(objectMapper.readValue(anyString(),any(Class.class))).thenThrow(exception);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		jsonBodyResolver.resolve("content", Object.class);
		assertTrue(result != null);
	}
}
