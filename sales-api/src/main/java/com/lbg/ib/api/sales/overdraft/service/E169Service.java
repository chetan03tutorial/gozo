package com.lbg.ib.api.sales.overdraft.service;

import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Response;
import com.lloydstsb.www.E169_AddCBSFrmOd_ServiceLocator;

public interface E169Service {

	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E169", serviceAction = "E169")
	@HeaderRegistry(portNameMethod="getE169_AddCBSFrmOdWSDDPortName", serviceLocator=E169_AddCBSFrmOd_ServiceLocator.class)
	public E169Response invokeE169(E169Request serviceRequest);
}
