package com.lbg.ib.api.sales.questionnaire.domain;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.HeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultRequest;
import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultResponse;
import LIB_SIM_GMO.RequestHeader;
import LIB_SIM_GMO.SOAPHeader;

@Component
public class QuestionnaireRequestBuilder {

    @Autowired
    private HeaderUtility        headerUtility;

    @Autowired
    private SessionManagementDAO session;

    private static final String  SERVICE_NAME   = "ArrangementSetupService";
    private static final String  SERVICE_ACTION = "recordArrangementQuestionnaireResult";

    @Autowired
    private ConfigurationDAO     configuration;

    public RecordArrangementQuestionnaireResultRequest buildRequest(QuestionnaireRequestDTO requestDTO,
            UserContext userContext) {

        RecordArrangementQuestionnaireResultRequest serviceRequest = new RecordArrangementQuestionnaireResultRequest();
        serviceRequest.setCommunication(requestDTO.getCommunication());
        serviceRequest.setProductArrangement(requestDTO.getProductArrangement());
        serviceRequest.setHeader(prepareSoapHeader());
        return serviceRequest;
    }

    private RequestHeader prepareSoapHeader() {
        RequestHeader headers = new RequestHeader();
        headers.setInteractionId("1u9ftstz6xb7m1btccxhhe40qb");
        headers.setBusinessTransaction("recordArrangementQuestionnaireResult");

        List<SOAPHeader> soapHeaders = headerUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);

        headers.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));

        UserContext userContext = session.getUserContext();

        if (null != session.getUserContext()) {
            Map<String, Object> map = configuration.getConfigurationItems("ChannelIdMapping");
            headers.setChannelId(map.get(session.getUserContext().getChannelId()).toString());
        }

        return headers;

    }

    public QuestionnaireResponseDTO buildResponse(RecordArrangementQuestionnaireResultResponse response) {
        QuestionnaireResponseDTO responseDTO = new QuestionnaireResponseDTO();
        responseDTO.setReasonCode(response.getResultCondition().getReasonCode());
        responseDTO.setReasonText(response.getResultCondition().getReasonText());
        return responseDTO;
    }
}
