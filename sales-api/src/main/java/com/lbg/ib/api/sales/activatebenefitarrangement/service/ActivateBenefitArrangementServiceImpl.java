package com.lbg.ib.api.sales.activatebenefitarrangement.service;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.mapper.CreateArrangementSetupRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.arrangementsetup.ArrangementSetUpResponse;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.CreateServiceArrangementRequest;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.CreateServiceArrangementResponse;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.ServiceArrangementSetup;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.ServiceArrangementSetupServiceLocator;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.ServiceArrangement;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.www.Schema.Enterprise.IFWXML.ResultCondition;

/**
 * Created by 8796528 on 06/03/2018.
 */

@Component
public class ActivateBenefitArrangementServiceImpl extends SoaAbstractService implements ActivateBenefitArrangementService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ConfigurationDAO configManager;

    @Autowired
    private ModuleContext beanLoader;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    CreateArrangementSetupRequestMapper createArrangementSetupRequestMapper;

    @TraceLog
    public ArrangementSetUpResponse createArrangementSetupService(CreateServiceArrangement createServiceArrangement) {
        ServiceArrangementSetupServiceLocator serviceLocator = beanLoader.getService(ServiceArrangementSetupServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getServiceArrangementSetupSOAPPortWSDDPortName()), serviceLocator.getHandlerRegistry());
        ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        setSoapHeader(clientContext, "http://www.lloydstsb.com/Schema/Enterprise/LCSM_ArrangementNegotiation/activateBenefitArrangement", serviceLocator.getServiceName().getNamespaceURI());
        CreateServiceArrangementResponse response = (CreateServiceArrangementResponse) invoke("createServiceArrangement", buildRequest(createServiceArrangement));
        return populateArrangementSetUpResponse(response);

    }

    @TraceLog
    public ArrangementSetUpResponse populateArrangementSetUpResponse(CreateServiceArrangementResponse response) {
        ResultCondition[] resultConditions = response.getResponseHeader().getResultConditions();
        ArrangementSetUpResponse arrangementSetUpResponse = new ArrangementSetUpResponse(resultConditions[0].getSeverityCode(),
                resultConditions[0].getReasonCode(),
                resultConditions[0].getReasonText(),
                resultConditions[0].getReasonDetail());
        if (null != arrangementSetUpResponse.getSeverityCode() && arrangementSetUpResponse.getReasonCode().equals('0')) {
            logger.traceLog(this.getClass(), "Error in response of LifeStyle API");
            throw new ServiceException(new ResponseError("Error in response of LifeStyle API", "Error in response of LifeStyle API"));
        }
        return arrangementSetUpResponse;
    }

    public CreateServiceArrangementRequest buildRequest(CreateServiceArrangement createServiceArrangement) {
        CreateServiceArrangementRequest request = new CreateServiceArrangementRequest();
        ProductArrangement arrangement = createArrangementSetupRequestMapper.getRelatedArrangement(createServiceArrangement);
        ServiceArrangement serviceArrangement = createArrangementSetupRequestMapper.getServiceArrangement(createServiceArrangement, "PENDING_SELECTION");
        request.setServiceArrangement(serviceArrangement);
        request.setRelatedArrangement(new ProductArrangement[]{arrangement});
        return request;
    }

    public Class<?> getPort() {
        return ServiceArrangementSetup.class;
    }

}
