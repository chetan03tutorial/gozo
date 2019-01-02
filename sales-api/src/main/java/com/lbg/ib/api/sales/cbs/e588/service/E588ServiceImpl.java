package com.lbg.ib.api.sales.cbs.e588.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.cbs.e588.domain.E588Request;
import com.lbg.ib.api.sales.cbs.e588.domain.E588Response;
import com.lbg.ib.api.sales.cbs.e588.mapper.E588MessageMapper;
import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.service.AbstractCbsService;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydsbanking.xml.E588Req;
import com.lloydsbanking.xml.E588Resp;
import com.lloydsbanking.xml.E588_AddCBSNewAc_PortType;
import com.lloydsbanking.xml.E588_AddCBSNewAc_ServiceLocator;

@Component
public class E588ServiceImpl extends AbstractCbsService implements E588Service {

	@Autowired
	private ServiceErrorValidator serviceErrorValidator;

	@Override
	public Class<?> getPort() {
		return E588_AddCBSNewAc_PortType.class;
	}

	@TraceLog
	@ServiceExceptionManager
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E588", serviceAction = "E588")
	@HeaderRegistry(portNameMethod = "getE588_AddCBSNewAcWSDDPortName", serviceLocator = E588_AddCBSNewAc_ServiceLocator.class)
	public E588Response invokeE170(E588Request clientRequest) {
		setOriginatingSortCode(clientRequest);
		E588MessageMapper messageMapper = getMessageMapper(E588MessageMapper.class);
		E588Req serviceRequest = (E588Req)messageMapper.buildRequest(clientRequest);
		E588Resp serviceResponse = (E588Resp)invoke("e588", serviceRequest);
		serviceErrorValidator.validateResponseError(serviceResponse.getE588Result().getResultCondition());
		return (E588Response)messageMapper.buildResponse(serviceResponse);
	}
}