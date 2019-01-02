package com.lbg.ib.api.sales.gozo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.tasks.AbstractInstruction;
import com.lbg.ib.api.sales.gozo.tasks.Instruction;

public class RequestProcessor<T> {

	@Autowired
	private AbstractInstruction chain;
	private List<Instruction> inbound;
	private List<Instruction> outbound;
	
	/*public RequestProcessor() {
		AbstractInstructionChain incomingValidator = new ValidateMessage(Phase.IN);
		AbstractInstructionChain outgoingValidator = new ValidateMessage(Phase.OUT);
		AbstractInstructionChain incomingMessageInstruction = new MapMessage(Phase.IN);
		AbstractInstructionChain outgoingMessageInstruction = new MapMessage(Phase.OUT);
		AbstractInstructionChain serviceInvokerInstruction = new SoapInvoker(Phase.IN);

		incomingValidator.setNext(incomingMessageInstruction);
		incomingValidator.setNext(serviceInvokerInstruction);
		serviceInvokerInstruction.setNext(outgoingMessageInstruction);
		outgoingMessageInstruction.setNext(outgoingValidator);
	}*/
	
	
	public void process(InstructionContext _context) {
		chain.execute(_context);
	}

}
