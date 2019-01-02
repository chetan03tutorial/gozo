package com.lbg.ib.api.sales.overdraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Response;
import com.lbg.ib.api.sales.overdraft.mapper.E170MessageMapper;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.E170Req;
import com.lloydstsb.www.E170Resp;
import com.lloydstsb.www.E170_ChaCBSFormalOd_PortType;
import com.lloydstsb.www.E170_ChaCBSFormalOd_ServiceLocator;

@Component
public class E170ServiceImpl extends AbstractCbsService implements E170Service {

	@Autowired
	private ServiceErrorValidator serviceErrorValidator;

	@Override
	public Class<?> getPort() {
		return E170_ChaCBSFormalOd_PortType.class;
	}

	
	@TraceLog
	@ServiceExceptionManager
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E170", serviceAction = "E170")
	@HeaderRegistry(portNameMethod = "getE170_ChaCBSFormalOdWSDDPortName", serviceLocator = E170_ChaCBSFormalOd_ServiceLocator.class)
	public E170Response invokeE170(E170Request clientRequest) {
		setOriginatingSortCode(clientRequest);
		E170MessageMapper messageMapper = getMessageMapper(E170MessageMapper.class);
		E170Req serviceRequest = messageMapper.buildE170Req(clientRequest);
		E170Resp serviceResponse = (E170Resp) invoke("e170", serviceRequest);
		serviceErrorValidator.validateResponseError(serviceResponse.getE170Result().getResultCondition());
		return messageMapper.buildE170Response(serviceResponse);
	}
}
