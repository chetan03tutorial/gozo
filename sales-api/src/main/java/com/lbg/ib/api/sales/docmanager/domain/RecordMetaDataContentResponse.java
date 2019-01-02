package com.lbg.ib.api.sales.docmanager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Response container for record document
 *
 * @author 8903735
 *
 */
public class RecordMetaDataContentResponse {

	private List<CustomerDocumentInfoResponse> response;

	public List<CustomerDocumentInfoResponse> getResponse() {
		return response;
	}

	public void setResponse(List<CustomerDocumentInfoResponse> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
