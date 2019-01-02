package com.lbg.ib.api.sales.overdraft.domain;

import java.util.List;

public class AppealedOverdraftDecision {

	private String creditScoreResultCode;
	private List<AsmDecision> asmDecisions;
	private String affordableAmount;
	private String overdraftDecision;
	private String creditScoreReference;
	
	public String getCreditScoreResultCode() {
		return creditScoreResultCode;
	}
	public void setCreditScoreResultCode(String creditScoreResultCode) {
		this.creditScoreResultCode = creditScoreResultCode;
	}
	public List<AsmDecision> getAsmDecisions() {
		return asmDecisions;
	}
	public void setAsmDecisions(List<AsmDecision> asmDecisions) {
		this.asmDecisions = asmDecisions;
	}
	public String getAffordableAmount() {
		return affordableAmount;
	}
	public void setAffordableAmount(String affordableAmount) {
		this.affordableAmount = affordableAmount;
	}
	public String getOverdraftDecision() {
		return overdraftDecision;
	}
	public void setOverdraftDecision(String overdraftDecision) {
		this.overdraftDecision = overdraftDecision;
	}
	public String getCreditScoreReference() {
		return creditScoreReference;
	}
	public void setCreditScoreReference(String creditScoreReference) {
		this.creditScoreReference = creditScoreReference;
	}
	
	
}
