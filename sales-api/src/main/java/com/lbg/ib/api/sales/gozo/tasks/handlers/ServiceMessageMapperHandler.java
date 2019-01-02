package com.lbg.ib.api.sales.gozo.tasks.handlers;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.gozo.mappers.MessageMapper;

@Component
public class ServiceMessageMapperHandler {

	public Object prepareRequest(MessageMapper messageMapper, Object[] methodParameters) {

		Object request = null;
		Object requestDto = messageMapper.buildRequestDto(methodParameters);
		if (requestDto != null) {
			request = messageMapper.buildRequest(requestDto);
		}
		// Modify it to identify the exception with the code
		if (request == null) {
			throw new RuntimeException();
		}
		return request;
	}

	public Object prepareResponse(MessageMapper messageMapper, Object serviceResponse) {

		Object response = null;
		Object responseDto = messageMapper.buildResponseDto(serviceResponse);

		if (responseDto != null) {
			response = messageMapper.buildResponse(responseDto);
		}

		if (response == null) {
			// Handle the response politely
		}
		return response;

	}
}
