package org.gozo.funkysanta.rest.client.model;

import org.gozo.funkysanta.commands.Command;

public class CommandEnvelope {

    private Command command;
    private Object receiver;

    public CommandEnvelope(Command command, Object receiver) {
        this.command = command;
        this.receiver = receiver;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Object getReceiver() {
        return receiver;
    }

    public void setReceiver(Object receiver) {
        this.receiver = receiver;
    }
}
