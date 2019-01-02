package com.lbg.ib.api.sales.gozo;

import java.util.LinkedList;
import java.util.ListIterator;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.tasks.Instruction;

public class InstructionChain {

	private final LinkedList<Instruction> chain;

	public InstructionChain() {
		chain = new LinkedList<Instruction>();
	}

	public void add(Instruction instruction) {
		chain.addLast(instruction);
	}

	public void execute(InstructionContext _context) {
		ListIterator<Instruction> itr = chain.listIterator();
		while (itr.hasNext()) {
			Instruction instruction = itr.next();
			instruction.execute(_context);
		}
	}
}
