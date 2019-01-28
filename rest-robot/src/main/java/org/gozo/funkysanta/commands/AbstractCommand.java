package org.gozo.funkysanta.commands;

public abstract class AbstractCommand implements Command {

    private Command next;
    private CommandContext context;
    public Command next() {
        return this.next;
    }
    public void setNext(Command next) {
        this.next = next;
    }

    public CommandContext getContext() {
        return context;
    }

    public void setContext(CommandContext context) {
        this.context = context;
    }

	/*public void execute(CommandContext _context){
		if (this.getPhase() == Phase.IN) {
			processIncomingPhase(_context);
		}
		if (this.getPhase() == Phase.OUT) {
			processOutgoingPhase(_context);
		}
		next.execute(_context);
	}*/

    //abstract public void processIncomingPhase(CommandContext _context);

    //abstract public void processOutgoingPhase(CommandContext _context);

}
