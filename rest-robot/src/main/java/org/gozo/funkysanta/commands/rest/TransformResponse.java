package org.gozo.funkysanta.commands.rest;

import org.gozo.funkysanta.commands.AbstractCommand;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.components.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class TransformResponse extends AbstractCommand {

	public void execute(CommandContext _context) {
		ResponseMapper messageMapper = _context.getReceiver(ResponseMapper.class);
		Object serviceResponse = _context.get(CommandContext.SERVICE_RESPONSE, Object.class);
		Object response = messageMapper.buildResponse(serviceResponse);
		_context.set(CommandContext.SERVICE_RESPONSE, response);
	}
}
