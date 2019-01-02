package com.lbg.ib.api.sales.overdraft.service;

import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.overdraft.domain.E160Response;
import com.lloydstsb.www.E160_DelCBSFormalOdr_ServiceLocator;

public interface E160Service {

	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E160", serviceAction = "E160")
	@HeaderRegistry(portNameMethod="getE160_DelCBSFormalOdrWSDDPortName", serviceLocator=E160_DelCBSFormalOdr_ServiceLocator.class)
	public E160Response invokeE160(E160Request serviceRequest);
}
