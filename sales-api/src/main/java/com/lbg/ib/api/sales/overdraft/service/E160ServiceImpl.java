package com.lbg.ib.api.sales.overdraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.header.builder.CbsAppGroupHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.overdraft.domain.E160Response;
import com.lbg.ib.api.sales.overdraft.mapper.E160MessageMapper;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.E160Resp;
import com.lloydstsb.www.E160_DelCBSFormalOdr_PortType;
import com.lloydstsb.www.E160_DelCBSFormalOdr_ServiceLocator;

@Component
public class E160ServiceImpl extends AbstractCbsService implements E160Service {

	
	@Autowired
	private ServiceErrorValidator serviceErrorValidator;
	@Override
	public Class<?> getPort() {
		return E160_DelCBSFormalOdr_PortType.class;
	}

	@TraceLog
	@ServiceExceptionManager
	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class, SecurityHeader.class,
			CbsAppGroupHeader.class }, serviceName = "E160", serviceAction = "E160")
	@HeaderRegistry(portNameMethod = "getE160_DelCBSFormalOdrWSDDPortName", serviceLocator = E160_DelCBSFormalOdr_ServiceLocator.class)
	public E160Response invokeE160(E160Request serviceRequest) {
		logger.traceLog(this.getClass(), "Invoking E160");
		setOriginatingSortCode(serviceRequest);
		E160MessageMapper messageMapper = getMessageMapper(E160MessageMapper.class);
		E160Resp serviceResponse = (E160Resp) invoke("e160", messageMapper.buildE160Req(serviceRequest));
		serviceErrorValidator.validateResponseError(serviceResponse.getE160Result().getResultCondition());
		return messageMapper.buildE160Response(serviceResponse);
	}

}
