package com.lbg.ib.api.sales.overdraft.domain;

import java.util.List;

public class Q122Response {

	private String creditScoreResultCode;
	private List<AsmDecision> asmDecisions;
	private String affordableAmount;
	private String addressDetailIndicator;
	private String applicationNumber;
	private String applicantAddressSequenceNumber;
	private String bureauAddressIndicator;
	private String creditReferenceAgencyCode;
	private String bureauReferenceId;
	private int creditScoreId;
	private int additionalDetailIndicator;
	private String overdraftDecision;
	private String creditScoreReference;
	
	public String getCreditScoreResultCode() {
		return creditScoreResultCode;
	}
	public void setCreditScoreResultCode(String creditScoreResultCode) {
		this.creditScoreResultCode = creditScoreResultCode;
	}
	
	public String getAffordableAmount() {
		return affordableAmount;
	}
	public void setAffordableAmount(String affordableAmount) {
		this.affordableAmount = affordableAmount;
	}
	public String getAddressDetailIndicator() {
		return addressDetailIndicator;
	}
	public void setAddressDetailIndicator(String addressDetailIndicator) {
		this.addressDetailIndicator = addressDetailIndicator;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getApplicantAddressSequenceNumber() {
		return applicantAddressSequenceNumber;
	}
	public void setApplicantAddressSequenceNumber(String applicantAddressSequenceNumber) {
		this.applicantAddressSequenceNumber = applicantAddressSequenceNumber;
	}
	public String getBureauAddressIndicator() {
		return bureauAddressIndicator;
	}
	public void setBureauAddressIndicator(String bureauAddressIndicator) {
		this.bureauAddressIndicator = bureauAddressIndicator;
	}
	public String getCreditReferenceAgencyCode() {
		return creditReferenceAgencyCode;
	}
	public void setCreditReferenceAgencyCode(String creditReferenceAgencyCode) {
		this.creditReferenceAgencyCode = creditReferenceAgencyCode;
	}
	public String getBureauReferenceId() {
		return bureauReferenceId;
	}
	public void setBureauReferenceId(String bureauReferenceId) {
		this.bureauReferenceId = bureauReferenceId;
	}
	public int getCreditScoreId() {
		return creditScoreId;
	}
	public void setCreditScoreId(int creditScoreId) {
		this.creditScoreId = creditScoreId;
	}
	public List<AsmDecision> getAsmDecisions() {
		return asmDecisions;
	}
	public void setAsmDecisions(List<AsmDecision> asmDecisions) {
		this.asmDecisions = asmDecisions;
	}
	public int getAdditionalDetailIndicator() {
		return additionalDetailIndicator;
	}
	public void setAdditionalDetailIndicator(int additionalDetailIndicator) {
		this.additionalDetailIndicator = additionalDetailIndicator;
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
