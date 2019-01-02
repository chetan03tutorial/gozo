package com.lbg.ib.api.sales.overdraft.service;

import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.Q122Request;
import com.lbg.ib.api.sales.overdraft.domain.Q122Response;
import com.lloydsbanking.xml.Q122_FunOvrdrtDecisnReq_ServiceLocator;

public interface Q122Service {

	@HeaderRegistry(portNameMethod = "getQ122_FunOvrdrtDecisnReqWSDDPortName", serviceLocator = Q122_FunOvrdrtDecisnReq_ServiceLocator.class)
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class,
			SecurityHeader.class }, serviceName = "Q122", serviceAction = "Q122")
	public Q122Response invokeQ122(Q122Request q122Request, String creditScoreSourceSystemCd) ;
}
