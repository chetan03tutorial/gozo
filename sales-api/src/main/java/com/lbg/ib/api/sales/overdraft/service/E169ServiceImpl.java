package com.lbg.ib.api.sales.overdraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Response;
import com.lbg.ib.api.sales.overdraft.mapper.E169MessageMapper;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.E169Resp;
import com.lloydstsb.www.E169_AddCBSFrmOd_PortType;
import com.lloydstsb.www.E169_AddCBSFrmOd_ServiceLocator;

@Component
public class E169ServiceImpl extends AbstractCbsService implements E169Service {

	@Override
	public Class<?> getPort() {
		return E169_AddCBSFrmOd_PortType.class;
	}


	@Autowired
	private ServiceErrorValidator serviceErrorValidator;
	
	@TraceLog
	@ServiceExceptionManager
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E169", serviceAction = "E169")
	@HeaderRegistry(portNameMethod = "getE169_AddCBSFrmOdWSDDPortName", serviceLocator = E169_AddCBSFrmOd_ServiceLocator.class)
	public E169Response invokeE169(E169Request serviceRequest) {
		setOriginatingSortCode(serviceRequest);
		E169MessageMapper messageMapper = getMessageMapper(E169MessageMapper.class);
		E169Resp serviceResponse = (E169Resp) invoke("e169", messageMapper.buildE169Req(serviceRequest));
		serviceErrorValidator.validateResponseError(serviceResponse.getE169Result().getResultCondition());
		return messageMapper.buildE169Response(serviceResponse);
	}
}
