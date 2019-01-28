package org.gozo.funkysanta.commands;

import org.gozo.funkysanta.rest.client.model.CommandEnvelope;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class BaseCommandChain implements CommandChain {

	private final LinkedList<Command> chain;

	private final Map<Class<? extends Command>, Object> receiverChain;

	public BaseCommandChain() {
		chain = new LinkedList<Command>();
		receiverChain = new HashMap<Class<? extends Command>, Object>();
	}

	public void add(CommandEnvelope command) {
		chain.addLast(command.getCommand());
		receiverChain.put(command.getCommand().getClass(),command.getReceiver());
	}

	public void execute(CommandContext _context) {
		ListIterator<Command> itr = chain.listIterator();
		while (itr.hasNext()) {
			Command command = itr.next();
			_context.setReceiver(receiverChain.get(command.getClass()));
			command.execute(_context);
		}
	}
}
