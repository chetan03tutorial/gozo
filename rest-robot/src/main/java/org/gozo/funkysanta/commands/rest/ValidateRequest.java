package org.gozo.funkysanta.commands.rest;

import org.gozo.funkysanta.commands.AbstractCommand;
import org.gozo.funkysanta.commands.CommandContext;
import org.gozo.funkysanta.components.RequestValidator;

public class ValidateRequest extends AbstractCommand {


    public void execute(CommandContext _context) {
        RequestValidator serviceValidator = _context.getReceiver(RequestValidator.class);
        Object[] arguments = _context.get(CommandContext.METHOD_ARGUMENTS, Object[].class);
        serviceValidator.validateRequest(serviceValidator, arguments[0]);
    }


}
