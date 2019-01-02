package com.lbg.ib.api.sales.gozo.tasks;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.mappers.MessageMapper;

public class MapMessage extends AbstractInstruction {

	public MapMessage(Phase phase) {
		super(phase);
	}

	public void processIncomingPhase(InstructionContext _context) {
		MessageMapper messageMapper = _context.get(InstructionContext.MESSAGE_MAPPER, MessageMapper.class);
		if(messageMapper == null) {
			return;
		}
		Object[] args = _context.get(InstructionContext.METHOD_ARGUMENTS, Object[].class);
		Object dto = messageMapper.buildRequestDto(args);
		Object request = messageMapper.buildRequest(dto);
		_context.set(InstructionContext.SERVICE_REQUEST, request);
	}

	@Override
	public void processOutgoingPhase(InstructionContext _context) {
		MessageMapper messageMapper = _context.get(InstructionContext.MESSAGE_MAPPER, MessageMapper.class);
		if(messageMapper == null) {
			return;
		}
		Object args = _context.get(InstructionContext.SERVICE_RESPONSE, Object.class);
		Object dto = messageMapper.buildResponseDto(args);
		Object response = messageMapper.buildResponse(dto);
		_context.set(InstructionContext.SERVICE_RESPONSE, response);
	}

}
