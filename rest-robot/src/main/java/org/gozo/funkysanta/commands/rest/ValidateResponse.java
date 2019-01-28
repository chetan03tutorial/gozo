package org.gozo.funkysanta.commands.rest;

import org.gozo.funkysanta.commands.AbstractCommand;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.components.ResponseValidator;

public class ValidateResponse extends AbstractCommand {

    public void execute(CommandContext _context) {
        ResponseValidator serviceValidator = _context.getReceiver(ResponseValidator.class);
        Object[] arguments = _context.get(CommandContext.METHOD_ARGUMENTS, Object[].class);
        serviceValidator.validateResponse(serviceValidator, arguments[0]);
    }
}
