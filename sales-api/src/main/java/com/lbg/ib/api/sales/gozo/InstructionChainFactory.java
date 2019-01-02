package com.lbg.ib.api.sales.gozo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.lbg.ib.api.sales.gozo.tasks.Instruction;
import com.lbg.ib.api.sales.gozo.tasks.Phase;

public class InstructionChainFactory {

	private final InstructionChain instructions;
	private List<Class<? extends Instruction>> inbound;
	private List<Class<? extends Instruction>> outbound;

	public InstructionChainFactory() {
		instructions = new InstructionChain();
	}

	public InstructionChain getInstructionChain() {
		return instructions;
	}

	public InstructionChain init() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		for (Class<? extends Instruction> instructionClazz : inbound) {
			instructions.add(instructionClazz.getConstructor(Phase.class).newInstance(Phase.IN));
		}
		for (Class<? extends Instruction> instructionClazz : outbound) {
			instructions.add(instructionClazz.getConstructor(Phase.class).newInstance(Phase.OUT));
		}
		return instructions;
	}

	public List<Class<? extends Instruction>> getInbound() {
		return inbound;
	}

	public void setInbound(List<Class<? extends Instruction>> inbound) {
		this.inbound = inbound;
	}

	public List<Class<? extends Instruction>> getOutbound() {
		return outbound;
	}

	public void setOutbound(List<Class<? extends Instruction>> outbound) {
		this.outbound = outbound;
	}
}
