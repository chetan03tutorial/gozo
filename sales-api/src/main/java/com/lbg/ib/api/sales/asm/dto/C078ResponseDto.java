package com.lbg.ib.api.sales.asm.dto;

import java.util.List;

import com.lbg.ib.api.sales.overdraft.domain.AsmDecision;

public class C078ResponseDto {

	private String creditRequestReferenceNumber;
	private String creditScore;
	private List<AsmDecision> asmDecisions;
	private String applicationNumber;
	private int additionalDetailIndicator;
	private String creditSystem;
	
	public String getCreditRequestReferenceNumber() {
		return creditRequestReferenceNumber;
	}

	public void setCreditRequestReferenceNumber(String creditRequestReferenceNumber) {
		this.creditRequestReferenceNumber = creditRequestReferenceNumber;
	}

	public String getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}

	public List<AsmDecision> getAsmDecisions() {
		return asmDecisions;
	}

	public void setAsmDecisions(List<AsmDecision> asmDecisions) {
		this.asmDecisions = asmDecisions;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public int getAdditionalDetailIndicator() {
		return additionalDetailIndicator;
	}

	public void setAdditionalDetailIndicator(int additionalDetailIndicator) {
		this.additionalDetailIndicator = additionalDetailIndicator;
	}

	public String getCreditSystem() {
		return creditSystem;
	}

	public void setCreditSystem(String creditSystem) {
		this.creditSystem = creditSystem;
	}
}
