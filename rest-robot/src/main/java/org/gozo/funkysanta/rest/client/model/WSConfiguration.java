package org.gozo.funkysanta.rest.client.model;

import java.util.List;

public class WSConfiguration {

	private MetaData meta;

	private List<Directive> commands;

	public MetaData getMeta() {
		return meta;
	}

	public void setMeta(MetaData meta) {
		this.meta = meta;
	}

	public List<Directive> getCommands() {
		return commands;
	}

	public void setCommands(List<Directive> commands) {
		this.commands = commands;
	}


}
