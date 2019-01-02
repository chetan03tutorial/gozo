package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.asm.dto.C078ResponseDto;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lloydstsb.www.C078Resp;
import com.lloydstsb.www.C078_Enq_Decision_Request_ServiceLocator;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018. The class is created to
 * work as a Service for C078 resource to fetch App Score information
 */

public interface C078Service {

	/*
	 * The invokeC078 is used to invoke the CO78 to fetch the App Score response
	 * 
	 * @param AppScoreRequest
	 * 
	 * @return The C078Resp
	 */
	public C078Resp invokeC078(AppScoreRequest appScoreRequest);
	
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class,
			SecurityHeader.class }, serviceName = "C078", serviceAction = "C078")
	@HeaderRegistry(portNameMethod = "getC078_Enq_Decision_RequestWSDDPortName", serviceLocator = C078_Enq_Decision_Request_ServiceLocator.class)
	public C078ResponseDto invokeC078(ApplicationType applicationType);
}
