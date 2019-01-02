package com.lbg.ib.api.sales.overdraft.service;


import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Response;
import com.lloydstsb.www.E170_ChaCBSFormalOd_ServiceLocator;

public interface E170Service {

    @PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class,
            SecurityHeader.class, CbsAppGroupHeader.class }, serviceName = "E170", serviceAction = "E170")
    @HeaderRegistry(portNameMethod = "getE170_ChaCBSFormalOdWSDDPortName", serviceLocator = E170_ChaCBSFormalOd_ServiceLocator.class)
    public E170Response invokeE170(E170Request serviceRequest);
}
