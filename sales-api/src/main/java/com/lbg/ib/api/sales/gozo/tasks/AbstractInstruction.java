package com.lbg.ib.api.sales.gozo.tasks;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;

public abstract class AbstractInstruction implements Instruction {

	private Instruction next;
	private Phase phase;

	public AbstractInstruction(Phase phase) {
		this.phase = phase;
	}

	public Instruction next() {
		return this.next;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setNext(Instruction next) {
		this.next = next;
	}

	@Override
	public void execute(InstructionContext _context) {
		if (this.getPhase() == Phase.IN) {
			processIncomingPhase(_context);
		}
		if (this.getPhase() == Phase.OUT) {
			processOutgoingPhase(_context);
		}
		//next.execute(_context);
	}

	abstract public void processIncomingPhase(InstructionContext _context);

	abstract public void processOutgoingPhase(InstructionContext _context);

}
