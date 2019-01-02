package com.lbg.ib.api.sales.communication.service;
/*
Created by Rohit.Soni at 18/06/2018 13:50
*/

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.infrastructure.soap.Condition;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.lcsm.communicationmanagement.CommunicationDispatch;
import com.lloydstsb.lcsm.communicationmanagement.CommunicationDispatchServiceLocator;
import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationRequest;
import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.namespace.QName;
import com.lloydstsb.ea.context.ClientContext;


@Component
public class ScheduleCommunicationServiceImpl extends SoaAbstractService implements ScheduleCommunicationService {

    @Autowired
    private LoggerDAO logger;
    @Autowired
    private ModuleContext beanLoader;

    @Autowired
    private SessionManagementDAO sessionManager;

    @TraceLog
    public void invokeScheduleCommunication(ScheduleCommunicationRequest scheduleCommunicationRequest) {

        CommunicationDispatchServiceLocator serviceLocator = beanLoader.getService(CommunicationDispatchServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getCommunicationDispatchSOAPPortWSDDPortName()), serviceLocator.getHandlerRegistry());
        ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        setSoapHeader(clientContext, "scheduleCommunication", serviceLocator.getServiceName().getNamespaceURI());
        extractAndValidateServiceResponse((ScheduleCommunicationResponse)invoke("scheduleCommunication",scheduleCommunicationRequest));
    }

    @TraceLog
    private void extractAndValidateServiceResponse(ScheduleCommunicationResponse response) {
        try {
            ResultCondition resultCondition = response.getResponseHeader().getResultCondition();
            Condition[] extraConditions = resultCondition.getExtraConditions();
            Condition extraCondition = extraConditions[0];
            Integer reasonCode = extraCondition.getReasonCode();
            if (reasonCode != null && reasonCode.intValue() != 0) {
                throw new ServiceException(new ResponseError(String.valueOf(extraCondition.getReasonCode()), extraCondition.getReasonText()));
            }
        } catch (Exception exception) {
            logger.traceLog(this.getClass(), exception);
            throw new ServiceException(new ResponseError("Error in response of scheduleCommunication", "Error in response of scheduleCommunication"));
        }
    }

    public Class<?> getPort() {
        return CommunicationDispatch.class;
    }
}
