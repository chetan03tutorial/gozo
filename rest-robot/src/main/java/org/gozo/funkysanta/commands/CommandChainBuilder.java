package org.gozo.funkysanta.commands;

import org.gozo.funkysanta.rest.client.model.CommandEnvelope;
import org.gozo.funkysanta.rest.client.model.WebServiceConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CommandChainBuilder {

	public CommandChain buildCommandChain(WebServiceConfiguration wsConfig) {
		BaseCommandChain commandChain = new BaseCommandChain();
		for (CommandEnvelope command : wsConfig.getCommandEnvelopes()) {
			commandChain.add(command);
		}
		return commandChain;
	}

}
