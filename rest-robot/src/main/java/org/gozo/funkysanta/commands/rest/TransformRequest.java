package org.gozo.funkysanta.commands.rest;

import org.gozo.funkysanta.commands.AbstractCommand;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.components.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformRequest extends AbstractCommand {

	public void execute(CommandContext _context) {
		// Replace request mappers with Lambdas
		RequestMapper messageMapper = _context.getReceiver(RequestMapper.class);
		Object[] args = _context.get(CommandContext.METHOD_ARGUMENTS, Object[].class);
		Object request = messageMapper.buildRequest(args[0]);
		_context.set(CommandContext.SERVICE_REQUEST, request);
	}


	/*public void processIncomingPhase(InstructionContext _context) {
		MessageMapper messageMapper = _context.get(InstructionContext.MESSAGE_MAPPER, MessageMapper.class);
		Object[] args = _context.get(InstructionContext.METHOD_ARGUMENTS, Object[].class);
		Object request = messageMapperHandler.prepareRequest(messageMapper, args);
		_context.set(InstructionContext.SERVICE_REQUEST, request);
	}


	public void processOutgoingPhase(InstructionContext _context) {
		MessageMapper messageMapper = _context.get(InstructionContext.MESSAGE_MAPPER, MessageMapper.class);
		Object args = _context.get(InstructionContext.SERVICE_RESPONSE, Object.class);
		Object dto = messageMapper.buildResponseDto(args);
		Object response = messageMapper.buildResponse(dto);
		_context.set(InstructionContext.SERVICE_RESPONSE, response);
	}*/


}
