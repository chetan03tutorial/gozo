/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.CFCNewLimitResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydsbanking.xml.*;
import com.lloydstsb.ea.context.ClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CreditAssessmentQ250Service extends SoaAbstractService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ModuleContext beanLoader;
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private LoggerDAO logger;

    /**
     *
     * @return
     * @throws ServiceException
     */
    @TraceLog
    public TriadCFCResultDTO retrieveCreditAssessmentData(TriadRequestDTO triadRequestDTO) throws ServiceException {
        logger.traceLog(this.getClass(), "retrieveCreditAssessmentData: Enter");
        Q250_Enq_Triad_Results_ServiceLocator serviceLocator = beanLoader.getService(Q250_Enq_Triad_Results_ServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getQ250_Enq_Triad_ResultsWSDDPortName()), serviceLocator.getHandlerRegistry());
        ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        setSoapHeader(clientContext, "q250", serviceLocator.getServiceName().getNamespaceURI());

        Q250Req request = createRequest(triadRequestDTO);
        logger.traceLog(this.getClass(), "retrieveCreditAssessmentData: Calling Q250 API");
        Q250Resp q250Resp = (Q250Resp) invoke("q250", request);
        logger.traceLog(this.getClass(), "retrieveCreditAssessmentData: Response received from Q250 API");
        return  extractAndValidateServiceResponse(q250Resp);
    }

    private Q250Req createRequest(TriadRequestDTO triadRequestDTO) {
        Q250Req request = new Q250Req();
        request.setCIDPersId("");
        request.setCompanyBrandName("");
        request.setMaxRepeatGroupQy(0);
        request.setOriginatingSystemName("DIGITALPCA");
        request.setPartyId(Long.parseLong(triadRequestDTO.getOcisId()));
        request.setRequestNo(new SimpleDateFormat("YYYYMMDD-HHmmss").format(new Date()));
        request.setSourceSystemCd("010");
        return request;
    }

    @TraceLog
    private TriadCFCResultDTO extractAndValidateServiceResponse(Q250Resp q250Resp) {
        List<TriadResultDTO> triadResults = Lists.newArrayList();
        List<CFCNewLimitResultDTO> cfcNewLimitResults = Lists.newArrayList();
        valiadteResponse(q250Resp);

        if(q250Resp.getResults() != null && q250Resp.getResults().getTriadOMDMResult() != null && q250Resp.getResults().getTriadOMDMResult().getTriadCFCResult() != null) {
            TriadCFCResult[] cfcResults = q250Resp.getResults().getTriadOMDMResult().getTriadCFCResult();
            for (TriadCFCResult cfcResult : cfcResults) {
                for (CFCActionResult cfcActionResult : cfcResult.getCFCActionResult()) {
                    TriadResultDTO resultDTO = new TriadResultDTO(cfcActionResult.getCFCActionID(), cfcActionResult.getCFCActionVl());
                    triadResults.add(resultDTO);
                }
                if(cfcResult.getCFCNewLimitResult() != null ){
                    for(CFCNewLimitResult cfcNewLimitResult : cfcResult.getCFCNewLimitResult()){
                        CFCNewLimitResultDTO cfcNewLimitResultDTO = new CFCNewLimitResultDTO(cfcNewLimitResult.getCFCNewLimitID(),cfcNewLimitResult.getCFCNewLimitVl());
                        cfcNewLimitResults.add(cfcNewLimitResultDTO);
                    }
                }
            }
        }
        TriadCFCResultDTO triadCFCResultDTO = new TriadCFCResultDTO(triadResults, cfcNewLimitResults);
        return triadCFCResultDTO;
    }

    private void valiadteResponse(Q250Resp q250Resp) {
        if (q250Resp == null || q250Resp.getQ250Result() == null) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        } else if (q250Resp.getQ250Result().getResultCondition() != null &&
                q250Resp.getQ250Result().getResultCondition().getSeverityCode().getValue() != 0) {

            ResultCondition resultCondition = q250Resp.getQ250Result().getResultCondition();
            logger.traceLog(this.getClass(), "Error Q250 response: " + q250Resp.getQ250Result().getResultCondition().getSeverityCode().getValue());
            throw new ServiceException(
                    new ResponseError(resultCondition.getReasonCode().toString(), resultCondition.getReasonText()));
        }
    }

    @Override
    public Class<?> getPort() {
        return Q250_Enq_Triad_Results_PortType.class;
    }

}
