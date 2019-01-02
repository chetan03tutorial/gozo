package com.lbg.ib.api.sales.docmanager.domain;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class CustomerDocumentInfo {

	// Indexes the Waiver,Notes,Document Reference
	private String                 ocisId;          // this is the OCIS party
	// id
	private List<CustomerDocument> customerDocuments;

	public String getOcisId() {
		return ocisId;
	}

	public void setOcisId(String ocisId) {
		this.ocisId = ocisId;
	}

	public List<CustomerDocument> getCustomerDocuments() {
		return customerDocuments;
	}

	public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
		this.customerDocuments = customerDocuments;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

}
