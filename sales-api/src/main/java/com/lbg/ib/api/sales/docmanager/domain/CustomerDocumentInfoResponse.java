package com.lbg.ib.api.sales.docmanager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CustomerDocumentInfoResponse {
	private String ocisId;
	private String message;
	private Boolean isRecorded;

	public CustomerDocumentInfoResponse(String ocisId, String message, Boolean isRecorded) {
		super();
		this.ocisId = ocisId;
		this.message = message;
		this.isRecorded = isRecorded;
	}

	public String getOcisId() {
		return ocisId;
	}

	public void setOcisId(String ocisId) {
		this.ocisId = ocisId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getIsRecorded() {
		return isRecorded;
	}

	public void setIsRecorded(Boolean isRecorded) {
		this.isRecorded = isRecorded;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
