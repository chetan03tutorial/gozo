package com.lbg.ib.api.sales.gozo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;

@Component
public class InstructionProcessor {
	

	@Autowired
	private InstructionChain instructionChain;

	public void process(InstructionContext context) {
		instructionChain.execute(context);
	}
}
