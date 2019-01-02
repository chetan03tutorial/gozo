package com.lbg.ib.api.sales.cbs.e588.dto;

public class E588RequestDto {

	private int maxGroupQuantity;
	private String sourceId;
	private int inputOfficerFlag;
	private int overrideFlag;
	private String cbsAccountNumber;
	private int productNumber;
	private int productFlag;
	private int tariffFlag;
	private int tariffId;

	public int getMaxGroupQuantity() {
		return maxGroupQuantity;
	}

	public void setMaxGroupQuantity(int maxGroupQuantity) {
		this.maxGroupQuantity = maxGroupQuantity;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public int getInputOfficerFlag() {
		return inputOfficerFlag;
	}

	public void setInputOfficerFlag(int inputOfficerFlag) {
		this.inputOfficerFlag = inputOfficerFlag;
	}

	public int getOverrideFlag() {
		return overrideFlag;
	}

	public void setOverrideFlag(int overrideFlag) {
		this.overrideFlag = overrideFlag;
	}

	public String getCbsAccountNumber() {
		return cbsAccountNumber;
	}

	public void setCbsAccountNumber(String cbsAccountNumber) {
		this.cbsAccountNumber = cbsAccountNumber;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public int getProductFlag() {
		return productFlag;
	}

	public void setProductFlag(int productFlag) {
		this.productFlag = productFlag;
	}

	public int getTariffFlag() {
		return tariffFlag;
	}

	public void setTariffFlag(int tariffFlag) {
		this.tariffFlag = tariffFlag;
	}

	public int getTariffId() {
		return tariffId;
	}

	public void setTariffId(int tariffId) {
		this.tariffId = tariffId;
	}

}
