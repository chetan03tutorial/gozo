package com.lbg.ib.api.sales.questionnaire;

import static java.net.URI.create;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.HeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.questionnaire.domain.QuestionnaireRequestBuilder;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultRequest;
import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultResponse;
import LIB_SIM_BO.Communication;
import LIB_SIM_BO.ProductArrangement;
import LIB_SIM_BO.ResultCondition;
import LIB_SIM_GMO.SOAPHeader;

public class QuestionnaireRequestBuilderTest {

    @InjectMocks
    private QuestionnaireRequestBuilder requestBuilder;

    /*
     * private SessionManagementDAO session = mock(SessionManagementDAO.class);
     */

    @Mock
    private HeaderUtility               headerUtility;

    @Mock
    private SessionManagementDAO        session;

    @Mock
    private ConfigurationDAO            configuration;

    @Mock
    private UserContext                 context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(context.getChannelId()).thenReturn("channelId");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelId", "channelId");
        when(configuration.getConfigurationItems("ChannelIdMapping")).thenReturn(map);
    }

    @Test
    public void testBuildRequest() {
        when(headerUtility.prepareSoapHeader(anyString(), anyString())).thenReturn(createHeader());
        when(session.getUserContext()).thenReturn(createUserContext());
        RecordArrangementQuestionnaireResultRequest request = requestBuilder
                .buildRequest(createQuestionnaireRequestDTO(), createUserContext());
        assertEquals(request.getCommunication().getBrand(), "BRAND");
        assertEquals(request.getHeader().getLloydsHeaders().length, 1);
        assertEquals(request.getHeader().getChannelId(), "channelId");
    }

    private List<SOAPHeader> createHeader() {
        List<SOAPHeader> soapHeaders = new ArrayList<SOAPHeader>();
        SOAPHeader header = new SOAPHeader();
        header.setName("NAME");
        header.setNameSpace(create("NAMESPACE"));
        header.setPrefix("PREFIX");
        header.setValue("VALUE");
        soapHeaders.add(header);
        return soapHeaders;
    }

    private UserContext createUserContext() {
        UserContext userContext = new UserContext("userId", "ipAddress", "sessionId", "partyId", "ocisId", "channelId",
                "chansecMode", "userAgent", "language", "inboxIdClient", "host");
        return userContext;
    }

    private QuestionnaireRequestDTO createQuestionnaireRequestDTO() {
        QuestionnaireRequestDTO requestDTO = new QuestionnaireRequestDTO();
        Communication communication = new Communication();
        communication.setCommunicationId("COMMUNICATION-ID");
        communication.setBrand("BRAND");
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setAccountNumber("ACCOUNT-NUMBER");
        requestDTO.setCommunication(communication);
        requestDTO.setProductArrangement(productArrangement);
        return requestDTO;
        // FileUtil.readFile("/questionnaireDTO.json")
    }

    @Test
    public void testBuildResponse() {
        RecordArrangementQuestionnaireResultResponse response = new RecordArrangementQuestionnaireResultResponse();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode("REASON-CODE");
        resultCondition.setSeverityCode("SEVERITY-CODE");
        resultCondition.setReasonText("TEXT");
        response.setResultCondition(resultCondition);
        QuestionnaireResponseDTO responseDTo = requestBuilder.buildResponse(response);
        assertEquals(resultCondition.getReasonCode(), responseDTo.getReasonCode());
        assertEquals(resultCondition.getReasonText(), responseDTo.getReasonText());
    }

}
