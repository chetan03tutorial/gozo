package com.lbg.ib.api.sales.gozo.mappers;

public interface MessageMapper {

	public Object buildRequest(Object... clientRequest);
	
	public Object buildRequestDto(Object... clientRequest);
	
	public Object buildResponse(Object... serviceResponse);
	
	public Object buildResponseDto(Object... serviceResponse);
}
