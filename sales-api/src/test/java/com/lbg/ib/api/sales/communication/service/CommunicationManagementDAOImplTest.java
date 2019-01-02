
package com.lbg.ib.api.sales.communication.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.communicationmanagement.CommunicationMangementDAOImpl;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.CommunicationManagementRequestMapper;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Communication;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CommunicationTemplate;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouter;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;

public class CommunicationManagementDAOImplTest {

    @InjectMocks
    CommunicationMangementDAOImpl                           communictaionMangementDAOImpl;

    @Mock
    private LoggerDAO                                       logger;

    private ID_CommunicationManagerRouter                   sendCommunicationService;

    private CommunicationManagementRequestMapper            requestMapper;

    private DAOExceptionHandler                             daoExceptionHandler;

    private DAOResponse<CommunicationManagementResponseDTO> response;

    private static SendCommunicationRequest                 SEND_COMMUNICATION_REQUEST = new SendCommunicationRequest();

    private UserContext                                     userContext                = new UserContext("userId",
            "ipAddress", "sessionId", "partyId", "ocisId", "channelId", "chansecMode", "userAgent", "language",
            "inboxIdClient", "host");

    @Before
    public void setUp() throws Exception {

        requestMapper = Mockito.mock(CommunicationManagementRequestMapper.class);
        sendCommunicationService = Mockito.mock(ID_CommunicationManagerRouter.class);
        daoExceptionHandler = Mockito.mock(DAOExceptionHandler.class);
        when(requestMapper.mapSendCommunicationRequestAttributes(testReq(), userContext))
                .thenReturn(SEND_COMMUNICATION_REQUEST);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldReturnSucessResponseIfCorrectRequestIsPopulated() throws Exception {
        when(sendCommunicationService.sendCommunication(sendCommunicationRequest())).thenReturn(sampleResponse());
        response = communictaionMangementDAOImpl.sendEmailCommunictaion(testReq(), userContext);
        boolean successResponseFromSendCommunication = response.getResult().getIsSuccessful();
        assertThat(successResponseFromSendCommunication, is(true));
    }

    @Test
    public void shouldThrowErrorIfSendCommunicationServiceThrowsException() throws Exception {
        ExternalBusinessError expectedException = new ExternalBusinessError(null, null, null, null);
        when(sendCommunicationService.sendCommunication(sendCommunicationRequest())).thenThrow(expectedException);
        DAOError expectedError = new DAOError("someErrorCode", "someMessage");
        when(daoExceptionHandler.handleException(expectedException, CommunicationMangementDAOImpl.class,
                "sendEmailCommunictaion", testReq())).thenReturn(expectedError);
        response = communictaionMangementDAOImpl.sendEmailCommunictaion(testReq(), userContext);
        assertThat(response.getError(), CoreMatchers.sameInstance(expectedError));

    }

    @Test
    public void shouldThrowErrorIfResposneIsNull() throws Exception {
        SendCommunicationResponse response = new SendCommunicationResponse();
        response.setIsSuccessful(null);
        response.setHeader(mapResponseHeader());
        when(sendCommunicationService.sendCommunication(sendCommunicationRequest())).thenReturn(response);
        assertEquals(communictaionMangementDAOImpl.sendEmailCommunictaion(testReq(), userContext).getError(),
                new DAOError("813003", "Send Communicatiom WPS call is failed"));
    }

    private static CommunicationManagementDTO testReq() {
        CommunicationManagementDTO communictaionRequestDTO = new CommunicationManagementDTO();
        HashMap<String, String> tokenisedMap = new HashMap<String, String>();
        communictaionRequestDTO.setTemplateID("Template ID");
        communictaionRequestDTO.setCommunicationType("Communication Type");
        communictaionRequestDTO.setContactPointID("EMAIL");
        tokenisedMap.put("ib.customer.title", "Mr");
        tokenisedMap.put("ib.customer.lastname", "Test");
        tokenisedMap.put("ib.customer.postcode", "E18EP");
        communictaionRequestDTO.setTokenisedMap(tokenisedMap);
        return communictaionRequestDTO;

    }

    private SendCommunicationResponse sampleResponse() {
        SendCommunicationResponse sendCommunicationResponse = new SendCommunicationResponse();
        ResponseHeader responseHeader = mapResponseHeader();
        sendCommunicationResponse.setIsSuccessful(true);
        sendCommunicationResponse.setHeader(responseHeader);
        return sendCommunicationResponse;

    }

    private ResponseHeader mapResponseHeader() {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setChannelId("LBG");
        responseHeader.setBusinessTransaction("SendCommunication");
        responseHeader.setChannelId("0000805121");
        return responseHeader;
    }

    private SendCommunicationRequest sendCommunicationRequest() {
        Communication communication = new Communication();
        RequestHeader requestHeader = new RequestHeader();
        CommunicationTemplate communicationTemplate = new CommunicationTemplate();
        communicationTemplate.setTemplateId("Template ID");
        communication.setCommunicationTemplate(communicationTemplate);
        communication.setCommunicationType("Communication Type");
        communication.setContactPointId("EMAIL");
        communication.setHasCommunicationContent(new InformationContent[] {});
        requestHeader.setBusinessTransaction("sendCommunication");
        requestHeader.setChannelId("LBG");
        SEND_COMMUNICATION_REQUEST.setHeader(requestHeader);
        SEND_COMMUNICATION_REQUEST.setCommunication(communication);
        return SEND_COMMUNICATION_REQUEST;
    }

}
