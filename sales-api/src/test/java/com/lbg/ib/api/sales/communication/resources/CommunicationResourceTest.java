package com.lbg.ib.api.sales.communication.resources;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import com.lbg.ib.api.sales.communication.service.CommunicationDispatchService;
import com.lbg.ib.api.sales.communication.service.CommunicationService;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;



/**
 * Created by rabaja on 18/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommunicationResourceTest {
    @Mock
    private CommunicationService communicationService;
    @Mock
    private LoggerDAO logger;
    @Mock
    private RequestBodyResolver resolver;
    @Mock
    private SessionManagementDAO sessionManager;
    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    @InjectMocks
    private CommunicationResource commsResource;
    
    @Mock
    private CommunicationDispatchService communicationDispatchService;
    
    @Mock
    private ModuleContext beanLoader;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateTestWhenServiceReturnTrue() throws Exception {
        PartyCommunicationDetails communicationDetails = createDummyData();
        when(resolver.resolve(any(String.class), eq(PartyCommunicationDetails.class))).thenReturn(communicationDetails);
        when(communicationService.sendEmailCommunication(communicationDetails)).thenReturn(new Boolean("true"));
        Response response = commsResource.sendEmail("Request");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        assertTrue(response.getEntity().toString().contains(Constants.CommunicationConstants.EMAIL_SUCCESSFUL));
    }
    
    @Test
    public void validateTestWhenServiceReturnFalse() throws Exception {
        PartyCommunicationDetails communicationDetails = createDummyData();
        when(resolver.resolve(any(String.class), eq(PartyCommunicationDetails.class))).thenReturn(communicationDetails);
        when(communicationService.sendEmailCommunication(communicationDetails)).thenReturn(new Boolean("false"));
        ResponseError responseError = new ResponseError(ResponseErrorConstants.EMAIL_FAILURE,"Email Delivery Failure");
        when(errorResolver.resolve(ResponseErrorConstants.EMAIL_FAILURE)).thenReturn(responseError);
        try {
            commsResource.sendEmail("Request");
        }catch (ServiceException se){
            assertEquals(ResponseErrorConstants.EMAIL_FAILURE, se.getResponseError().getCode());
            assertEquals("Email Delivery Failure",se.getResponseError().getMessage());
        }
    }

    @Test
    public void validateTestIfTemplateIsNullOrBlank() throws Exception {
        PartyCommunicationDetails communicationDetails = createDummyData();
        communicationDetails.setTemplateId("");
        when(resolver.resolve(any(String.class), eq(PartyCommunicationDetails.class))).thenReturn(communicationDetails);
        when(communicationService.sendEmailCommunication(communicationDetails)).thenReturn(new Boolean("true"));
        ResponseError responseError = new ResponseError(ResponseErrorConstants.TEMPLATE_ID_NOT_FOUND,"Template Id not found");
        when(errorResolver.resolve(ResponseErrorConstants.TEMPLATE_ID_NOT_FOUND)).thenReturn(responseError);
        try {
            commsResource.sendEmail("Request");
        }catch (ServiceException se){
            assertEquals(ResponseErrorConstants.TEMPLATE_ID_NOT_FOUND, se.getResponseError().getCode());
            assertEquals("Template Id not found",se.getResponseError().getMessage());
        }
    }

    @Test
    public void validateTestIfActionItemMapIsNull() throws Exception {
        PartyCommunicationDetails communicationDetails = createDummyData();
        communicationDetails.setTokenMap(null);
        when(resolver.resolve(any(String.class), eq(PartyCommunicationDetails.class))).thenReturn(communicationDetails);
        when(communicationService.sendEmailCommunication(communicationDetails)).thenReturn(new Boolean("true"));
        ResponseError responseError = new ResponseError(ResponseErrorConstants.NO_ACTION_ITEMS,"No action item has been selected by user");
        when(errorResolver.resolve(ResponseErrorConstants.NO_ACTION_ITEMS)).thenReturn(responseError);
        try {
            commsResource.sendEmail("Request");
        }catch (ServiceException se){
            assertEquals(ResponseErrorConstants.NO_ACTION_ITEMS, se.getResponseError().getCode());
            assertEquals("No action item has been selected by user",se.getResponseError().getMessage());
        }
    }

    @Test
    public void shouldReturnResponseIfInputJsonIsNotValid() throws Exception {
        when(resolver.resolve(any(String.class), eq(PartyCommunicationDetails.class))).thenThrow(InvalidFormatException.class);
        Response response = commsResource.sendEmail("Request");
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }
    
    
    
    @Test
    public void testScheduleEmail(){
        ScheduleEmailSmsRequest request = new ScheduleEmailSmsRequest();
        ScheduleEmailSmsResponse response = new ScheduleEmailSmsResponse();
        when(resolver.resolve("requestBody", ScheduleEmailSmsRequest.class)).thenReturn(request);
        when(beanLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(communicationDispatchService.scheduleCommunication(request)).thenReturn(response);
        Response serviceResponse = commsResource.scheduledEmail("requestBody");
        assertEquals(serviceResponse.getStatus(), Response.Status.OK.getStatusCode());
    }


    @Test
    public void testSendEmail(){
        ScheduleEmailSmsRequest request = new ScheduleEmailSmsRequest();
        ScheduleEmailSmsResponse response = new ScheduleEmailSmsResponse();
        when(resolver.resolve("requestBody", ScheduleEmailSmsRequest.class)).thenReturn(request);
        when(beanLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(sessionManager.getEmailRetryCount()).thenReturn(new AtomicInteger(2));
        when(communicationDispatchService.sendCommunication(request)).thenReturn(response);
        Response serviceResponse = commsResource.email("requestBody");
        assertEquals(serviceResponse.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSendEmailFailureWithMaxRetryReached(){
        ScheduleEmailSmsRequest request = new ScheduleEmailSmsRequest();
        ScheduleEmailSmsResponse response = new ScheduleEmailSmsResponse();
        when(resolver.resolve("requestBody", ScheduleEmailSmsRequest.class)).thenReturn(request);
        when(beanLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(sessionManager.getEmailRetryCount()).thenReturn(new AtomicInteger(5));
        when(communicationDispatchService.sendCommunication(request)).thenReturn(response);
        try {
            Response serviceResponse = commsResource.email("requestBody");
            assertEquals(serviceResponse.getStatus(), Response.Status.OK.getStatusCode());
        }catch (ServiceException se){
            assertEquals(ResponseErrorConstants.MAX_EMAIL_RETRY_REACHES, se.getResponseError().getCode());
            assertEquals("Max email retry limit reaches",se.getResponseError().getMessage());
        }
    }

    private PartyCommunicationDetails createDummyData(){
        PartyCommunicationDetails communicationDetails = new PartyCommunicationDetails();
        communicationDetails.setTemplateId("NEXT_BEST_ACTION_ITEM_EMAIL");
        communicationDetails.setRecipientEmail(new String[]{"dummyUser@test.llyods.com"});
        Map<String, String> actionMap = new HashMap<String, String>();
        actionMap.put("Best Action Item Header 1", "Best Action Item Value 1");
        actionMap.put("Best Action Item Header 2", "Best Action Item Value 2");
        communicationDetails.setTokenMap(actionMap);
        return communicationDetails;
    }
}
