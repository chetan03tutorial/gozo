package com.lbg.ib.api.sales.docmanager.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 4thApril2017
 ***********************************************************************/

public class RecordMetaDataContent {
	private List<CustomerDocumentInfo> customerDocumentInfo;

	public List<CustomerDocumentInfo> getCustomerDocumentInfo() {
		return customerDocumentInfo;
	}

	public void setCustomerDocumentInfo(List<CustomerDocumentInfo> customerDocumentInfo) {
		this.customerDocumentInfo = customerDocumentInfo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}
}
