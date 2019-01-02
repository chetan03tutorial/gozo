package com.lbg.ib.api.alligator.annotations.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.util.DataHandlerUtil;
import com.lbg.ib.api.alligator.web.request.RequestReader;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class RequestBodyHandler extends AbstractAnnotationHandler{

	private Map<Class<?>, RequestReader> readerMap;

	private List<RequestReader> readers;

	public RequestBodyHandler() {
		readerMap = new HashMap<Class<?>, RequestReader>();
	}

	public void setReaders(List<RequestReader> readers) {
		this.readers = readers;
		if (readers != null && !readers.isEmpty()) {
			for (RequestReader reader : readers)
				readerMap.put(reader.getClass(), reader);
		}
	}

	public List<RequestReader> getReaders() {
		return this.readers;
	}

	@Override
	public void handle(ServiceRequest request, MethodParameter parameter) {
		RequestBody requestBody = (RequestBody) parameter.getAnnotation(RequestBody.class);
		Class<?> readerClass = requestBody.reader();
		boolean isFile = requestBody.file();
		String requestData;
		if (! isFile) {
			requestData = (String) parameter.getParamValue();
		} else {
			requestData = readRequest(readerClass, (String) parameter.getParamValue());
		}
		requestData = DataHandlerUtil.dataHandler(requestBody.dataHandler(), requestData);
		request.setRequestBody(requestData);
	}

	private String readRequest(Class<?> readerClass, String resourceName) {
		RequestReader reader = null;
		if (!RequestReader.class.isAssignableFrom(readerClass)) {
			// throw an exception for illegal request reader
		}
		reader = readerMap.get(readerClass);
		return reader.readRequest(resourceName);
	}
}
