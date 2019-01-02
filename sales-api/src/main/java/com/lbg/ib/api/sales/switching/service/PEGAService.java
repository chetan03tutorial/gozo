package com.lbg.ib.api.sales.switching.service;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.pega.GenericResponseType;
import com.lbg.ib.api.sales.soapapis.pega.messages.ResultCondition;
import com.lbg.ib.api.sales.soapapis.pega.objects.CreateCaseRequestType;
import com.lbg.ib.api.sales.soapapis.pega.objects.ServicesServiceLocator;
import com.lbg.ib.api.sales.soapapis.pega.objects.ServicesType;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.dao.mapper.CreateCaseRequestMapper;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.CreateCasePayloadResponseType;
import com.lloydstsb.ea.context.ClientContext;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
public class PEGAService extends SoaAbstractService {

    private static final String SERVICE_NAME   = "CreateCaseDetails";
    private static final String SERVICE_METHOD = "genericCreateCase";
    private static final String SERVICE_ACTION = "PEGA-IAS";
    private static final Integer NUMBER_OF_RETRIES = 2;

    private SessionManagementDAO     sessionManager;

    private CreateCaseRequestMapper  createCaseRequestMapper;

    private ModuleContext            beanLoader;

    private GalaxyErrorCodeResolver  resolver;

    private LoggerDAO                logger;

    @Autowired
    public PEGAService( SessionManagementDAO sessionManager,
                        CreateCaseRequestMapper createCaseRequestMapper,
                        ModuleContext beanLoader,
                        GalaxyErrorCodeResolver resolver,
                        LoggerDAO logger){
        this.sessionManager = sessionManager;
        this.createCaseRequestMapper = createCaseRequestMapper;
        this.beanLoader = beanLoader;
        this.resolver = resolver;
        this.logger = logger;
    }

    public String createPegaCase(AccountSwitchingRequest accountSwitchingRequest){
        logger.traceLog(this.getClass(), "createPegaCase: Enter");
        final ServicesServiceLocator serviceLocator = beanLoader.getService(ServicesServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getServicesPortSOAPWSDDPortName()), serviceLocator.getHandlerRegistry());
        final ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        setSoapHeader(clientContext, SERVICE_ACTION, SERVICE_NAME, true);

        final CreateCaseRequestType request = createCaseRequestMapper.create(accountSwitchingRequest);
        logger.traceLog(this.getClass(), "createPegaCase: Calling Create Case API");
        Integer attempts =0;
        while(true){
            try{
                final GenericResponseType response = (GenericResponseType)invoke(SERVICE_METHOD, request);
                logger.traceLog(this.getClass(), "createPegaCase: Response received from Create Case API");
                return extractAndValidateServiceResponse(response);
            }catch(ServiceException serviceException){
                if(attempts == NUMBER_OF_RETRIES){
                    logger.traceLog(this.getClass(), "Maximum number of retrying attempts reached");
                    throw serviceException;
                }
                attempts++;
                logger.traceLog(this.getClass(), "createPegaCase: Failed due to "+ serviceException.getResponseError().getMessage() +" Retrying Attempt: "+ attempts);
            }
        }
    }

    public Class<?> getPort() {
        return ServicesType.class;
    }

    private String extractAndValidateServiceResponse(final GenericResponseType response){
        String caseId = EMPTY;
        if (response == null || response.getResponseHeader() == null) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        } else if (response.getResponseHeader().getResultConditions() != null &&
                !("0").equals(response.getResponseHeader().getResultConditions().getSeverityCode())) {

            final ResultCondition resultCondition = response.getResponseHeader().getResultConditions();
            logger.traceLog(this.getClass(), "Error PEGA response: " + resultCondition.getSeverityCode());
            throw new ServiceException(new ResponseError(resultCondition.getReasonCode(), resultCondition.getReasonText()+" "+resultCondition.getReasonDetail()));

        } else if(response.getPayload() instanceof CreateCasePayloadResponseType){

            final CreateCasePayloadResponseType createCasePayloadResponseType = (CreateCasePayloadResponseType) response.getPayload();
            if(!StringUtils.isEmpty(createCasePayloadResponseType.getInitiateSwitchIn().getCaseId())){
                caseId =  ((CreateCasePayloadResponseType) response.getPayload()).getInitiateSwitchIn().getCaseId();
                logger.traceLog(this.getClass(), "PEGA case created: " + caseId);
            }
        }
        return caseId;
    }
}
