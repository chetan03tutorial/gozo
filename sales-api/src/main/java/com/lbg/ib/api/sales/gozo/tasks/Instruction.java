package com.lbg.ib.api.sales.gozo.tasks;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;

public interface Instruction {

	public void execute(InstructionContext _context);
}
