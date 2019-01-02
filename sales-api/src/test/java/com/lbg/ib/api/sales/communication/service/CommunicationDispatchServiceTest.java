package com.lbg.ib.api.sales.communication.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import com.lbg.ib.api.sales.communication.mapper.SendCommunicationMapper;
import com.lbg.ib.api.sales.conversion.domain.AdditionalPartyDetails;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.ea.lcsm.ResponseHeader;
import com.lloydstsb.lcsm.communicationmanagement.CommunicationDispatchServiceLocator;
import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationRequest;
import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommunicationDispatchServiceTest {

    @Mock
    private ModuleContext beanLoader;
    
    @Mock
    private SOAInvoker soaInvoker;
    
    @InjectMocks
    private CommunicationDispatchServiceImpl service;
    
    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private ConfigurationDAO configManager;
    
    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;
    
    @Mock
    private HandlerRegistry handleRegistry;
    
    @Mock
    private LoggerDAO logger;
    
    @Mock
    private CommunicationDispatchServiceLocator serviceLocator;

    @Mock
    private ScheduleCommunicationService scheduleCommunicationService;

    @Mock
    private CommunicationService communicationService;

    @Mock
    private SendCommunicationMapper sendCommunicationMapper;

    private static final String brand = "LTB";
    private static final String GROUP_EMAIL_ID = "group@email.com";
    private static final String OPS_EMAIL = "ops@email.com";
    
    
    private ScheduleCommunicationResponse externalSuccessServiceResponse(){
        ScheduleCommunicationResponse externalServiceResponse = new ScheduleCommunicationResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        externalServiceResponse.setResponseHeader(responseHeader);
        ResultCondition resultCondition = new ResultCondition();
        ResultCondition extraConditon = new ResultCondition();
        extraConditon.setReasonCode(0);
        extraConditon.setReasonText("No Error");
        resultCondition.setExtraConditions(new ResultCondition[]{extraConditon});
        responseHeader.setResultCondition(resultCondition);
        return externalServiceResponse;
    }
    
    private ScheduleCommunicationResponse externalFailureServiceResponse(){
        ScheduleCommunicationResponse externalServiceResponse = new ScheduleCommunicationResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        externalServiceResponse.setResponseHeader(responseHeader);
        ResultCondition resultCondition = new ResultCondition();
        ResultCondition extraConditon = new ResultCondition();
        extraConditon.setReasonCode(1000);
        extraConditon.setReasonText("No Error");
        resultCondition.setExtraConditions(new ResultCondition[]{extraConditon});
        responseHeader.setResultCondition(resultCondition);
        return externalServiceResponse;
    }
    
    private ScheduleCommunicationResponse externalServiceResponseWithNullReasonCode(){
        ScheduleCommunicationResponse externalServiceResponse = new ScheduleCommunicationResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        externalServiceResponse.setResponseHeader(responseHeader);
        ResultCondition resultCondition = new ResultCondition();
        ResultCondition extraConditon = new ResultCondition();
        resultCondition.setExtraConditions(new ResultCondition[]{extraConditon});
        responseHeader.setResultCondition(resultCondition);
        return externalServiceResponse;
    }
    
    private SelectedAccount selectedAccount(){
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("125672132");
        selectedAccount.setSortCode("09-23-98");
        return selectedAccount;
    }
    
    @Before
    public void setup(){
        
        when(serviceLocator.getCommunicationDispatchSOAPPortWSDDPortName()).thenReturn("scheduleCommunication");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("scheduleCommunication"));
        when(configManager.getConfigurationStringValue(anyString(), anyString())).thenReturn(brand);
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(SessionServiceUtil.buildParties(2));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(CommunicationDispatchServiceLocator.class)).thenReturn(serviceLocator);
        when(beanLoader.getService(ScheduleCommunicationService.class)).thenReturn(scheduleCommunicationService);
        when(beanLoader.getService(CommunicationService.class)).thenReturn(communicationService);
        when(beanLoader.getService(SendCommunicationMapper.class)).thenReturn(sendCommunicationMapper);
        when(sessionManager.getAccountToConvertInContext()).thenReturn(selectedAccount());
        when(sessionManager.getUserInfo()).thenReturn(arrangement());
    }

    private Arrangement arrangement() {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setFirstName("Amit");
        primaryInvolvedParty.setLastName("Gupta");
        primaryInvolvedParty.setEmail("abc@abc.com");
        primaryInvolvedParty.setDob("1995-11-23");

        List<Account> accounts = Lists.newArrayList();
        Account account1 = new Account();
        account1.setAccountNumber("1234567");
        account1.setSortCode("503020");
        account1.setAccountStatus("ACTIVE");
        account1.setAccountName("First");
        account1.setAccountType("T3001116001");

        Account account2 = new Account();
        account2.setAccountNumber("987654");
        account2.setSortCode("404040");
        account2.setAccountStatus("ACTIVE");
        account2.setAccountName("Second");
        account2.setAccountType("T245222222");

        accounts.add(account1);
        accounts.add(account2);
        arrangement.setAccounts(accounts);
        arrangement.setOcisId("807610516");
        arrangement.setPartyId("+00433279025");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        return arrangement;
    }

    private ScheduleEmailSmsRequest scheduleCommunicationRequest(){
        ScheduleEmailSmsRequest request = new ScheduleEmailSmsRequest();
        List<AdditionalPartyDetails> additionalPartyDetails = null;
        request.setProductMnemonic("P_CLASSIC");
        request.setTemplateName("CA_WELCOME_MSG_PDF");
        request.setCommunicationMedia("Email");
        Map<String, String> emailTokens = new HashMap<String, String>();
        emailTokens.put("key", "value");
        request.setEmailTokens(emailTokens);
        request.setAdditionalPartyDetailsList(additionalPartyDetails);
        return request;
    }
    
    @Test
    public void testSuccessfulSchedulingOfEmail(){
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        ScheduleEmailSmsResponse response = service.scheduleCommunication(scheduleCommunicationRequest());
        assertNotNull(response);
    }

    @Test
    public void testSuccessfulSendOfEmail(){
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        when(sessionManager.getBranchContext()).thenReturn(new BranchContext());
        when(sessionManager.getEmailRetryCount()).thenReturn(null);
        ScheduleEmailSmsResponse response = service.sendCommunication(scheduleCommunicationRequest());
        assertNotNull(response);
    }
    
    @Test
    public void testSuccessInSchedulingOfEmailDueToNullReasonCode(){
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalServiceResponseWithNullReasonCode());
        assertNotNull(service.scheduleCommunication(scheduleCommunicationRequest()));
    }

    @Test
    public void testEmailWhenAdditionalDetailsNotPresent(){
        //Check only party emails present
        service.scheduleCommunication(scheduleCommunicationRequest());
    }
    @Test
    public void testSendEmailWhenAdditionalDetailsNotPresent(){
        //Check only party emails present
        service.sendCommunication(scheduleCommunicationRequest());
    }
    /*
    Conditions: 2 parties, Additional Details not present, ScheduleCommunicationSuccess
     */
    @Test
    public void testEmailWhenAdditionalDetailsIsNotPresentAndScheduleCommIsSuccess(){
        //Check only party emails present
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleCommunicationRequest());
        //assert SendCommunicationIs Not called
        verify(communicationService,times(0)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 2 times- one for each party
        verify(scheduleCommunicationService,times(2)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
    }

    /*
    Conditions: 2 parties, Additional Details not present, ScheduleCommunicationFails And SendCommunication returns False
     */
    @Test
    public void testEmailWhenAdditionalDetailsIsNotPresentAndScheduleCommFails(){
        Mockito.doThrow(Exception.class).when(scheduleCommunicationService).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleCommunicationRequest());
        //assert SendCommunicationIs Not called
        verify(communicationService,times(2)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 2 times- one for each party
        verify(scheduleCommunicationService,times(2)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
    }

    /*
    Conditions: 2 parties, Additional Details not present, ScheduleCommunicationFails And SendCommunication is successful
     */
    @Test
    public void testEmailWhenAdditionalDetailsIsNotPresentAndScheduleCommFailsAndSendCommSuccess(){
        Mockito.doThrow(Exception.class).when(scheduleCommunicationService).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        when(communicationService.sendEmailCommunication(any(PartyCommunicationDetails.class))).thenReturn(true);
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleCommunicationRequest());
        //assert SendCommunicationIs Not called
        verify(communicationService,times(2)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 2 times- one for each party
        verify(scheduleCommunicationService,times(2)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
    }

    /*
    Conditions: 2 parties, Additional Details not present, ScheduleCommunicationFails And SendCommunication throwsException
     */
    @Test
    public void testEmailWhenAdditionalDetailsIsNotPresentAndScheduleCommFailsAndSendCommThrowsException(){
        Mockito.doThrow(Exception.class).when(scheduleCommunicationService).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        when(communicationService.sendEmailCommunication(any(PartyCommunicationDetails.class))).thenThrow(Exception.class);
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleCommunicationRequest());
        //assert SendCommunicationIs Not called
        verify(communicationService,times(2)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 2 times- one for each party
        verify(scheduleCommunicationService,times(2)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
    }

    /*
   Conditions: 2 parties, Additional Details present but for both parties but does not match session parties
    */
    @Test
    public void testEmailWhenAdditionalDetailsIsPresentForBothPartiesButDoesNotMatchSessionParties(){
        final ScheduleEmailSmsRequest scheduleEmailSmsRequest = scheduleCommunicationRequest();
        AdditionalPartyDetails additionalPartyDetailsPrimary = new AdditionalPartyDetails();
        AdditionalPartyDetails additionalPartyDetailsSecondary = new AdditionalPartyDetails();
        List<AdditionalPartyDetails> additionalPartyDetailsList = new ArrayList<AdditionalPartyDetails>();
        additionalPartyDetailsList.add(additionalPartyDetailsPrimary);
        additionalPartyDetailsList.add(additionalPartyDetailsSecondary);
        scheduleEmailSmsRequest.setAdditionalPartyDetailsList(additionalPartyDetailsList);
        scheduleEmailSmsRequest.setOpsEmail(OPS_EMAIL);
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleEmailSmsRequest);
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(OPS_EMAIL));
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(OPS_EMAIL));
    }

    /*
    Conditions: 2 parties,Group email is present, Additional Details not present, ScheduleCommunicationSuccess
     */
    @Test
    public void testGroupEmailWhenAdditionalDetailsIsNotPresentAndScheduleCommIsSuccess(){
        final ScheduleEmailSmsRequest scheduleEmailSmsRequest = scheduleCommunicationRequest();
        scheduleEmailSmsRequest.setGroupEmailId(GROUP_EMAIL_ID);
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleEmailSmsRequest);
        //assert SendCommunicationIs Not called
        verify(communicationService,times(0)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 4 times- one for each party, 2 for group
        verify(scheduleCommunicationService,times(4)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(2, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(2, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
    }

    /*
        Conditions: 2 parties,Group email is present, Additional Details present,
        and recording is successful for Primary party and failed for Secondary party
    */
    @Test
    public void testGroupEmailWhenAdditionalDetailsIsPresentWithDiffRecordingInfoAndScheduleCommIsSuccess(){
        final ScheduleEmailSmsRequest scheduleEmailSmsRequest = scheduleCommunicationRequest();
        AdditionalPartyDetails additionalPartyDetailsPrimary = new AdditionalPartyDetails();
        additionalPartyDetailsPrimary.setPrimaryParty(true);
        additionalPartyDetailsPrimary.setRecordingSuccessful(true);
        AdditionalPartyDetails additionalPartyDetailsSecondary = new AdditionalPartyDetails();
        additionalPartyDetailsSecondary.setPrimaryParty(false);
        additionalPartyDetailsSecondary.setRecordingSuccessful(false);
        List<AdditionalPartyDetails> additionalPartyDetailsList = new ArrayList<AdditionalPartyDetails>();
        additionalPartyDetailsList.add(additionalPartyDetailsPrimary);
        additionalPartyDetailsList.add(additionalPartyDetailsSecondary);
        scheduleEmailSmsRequest.setAdditionalPartyDetailsList(additionalPartyDetailsList);
        scheduleEmailSmsRequest.setGroupEmailId(GROUP_EMAIL_ID);
        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleEmailSmsRequest);
        //assert SendCommunicationIs Not called
        verify(communicationService,times(0)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 4 times- one for each party, 1 for group for Primary party as secondary party in ineligible
        verify(scheduleCommunicationService,times(3)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(2, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
    }

    /*
        Conditions: 2 parties,Group email is present,Ops email is present, Additional Details present,
        and recording is successful for Primary party and failed for Secondary party
        and it is branch context
        and Primary party present
        and Secondary party not present and secondary party email is not present
    */
    @Test
    public void testGroupEmailWhenAdditionalDetailsIsPresentWithDiffRecordingAndPartyPresenceInfoAndScheduleCommIsSuccess(){
        final ScheduleEmailSmsRequest scheduleEmailSmsRequest = scheduleCommunicationRequest();
        AdditionalPartyDetails additionalPartyDetailsPrimary = new AdditionalPartyDetails();
        additionalPartyDetailsPrimary.setPrimaryParty(true);
        additionalPartyDetailsPrimary.setRecordingSuccessful(true);
        additionalPartyDetailsPrimary.setPartyPresent(true);
        AdditionalPartyDetails additionalPartyDetailsSecondary = new AdditionalPartyDetails();
        additionalPartyDetailsSecondary.setPrimaryParty(false);
        additionalPartyDetailsSecondary.setRecordingSuccessful(false);
        additionalPartyDetailsSecondary.setPartyPresent(false);
        List<AdditionalPartyDetails> additionalPartyDetailsList = new ArrayList<AdditionalPartyDetails>();
        additionalPartyDetailsList.add(additionalPartyDetailsPrimary);
        additionalPartyDetailsList.add(additionalPartyDetailsSecondary);
        scheduleEmailSmsRequest.setAdditionalPartyDetailsList(additionalPartyDetailsList);
        scheduleEmailSmsRequest.setGroupEmailId(GROUP_EMAIL_ID);
        scheduleEmailSmsRequest.setOpsEmail(OPS_EMAIL);

        final Map<String, PartyDetails> stringPartyDetailsMap = SessionServiceUtil.buildParties(2);
        stringPartyDetailsMap.get("1").setEmail(null);
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(stringPartyDetailsMap);
        when(sessionManager.getBranchContext()).thenReturn(new BranchContext());

        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleEmailSmsRequest);
        //assert SendCommunicationIs Not called
        verify(communicationService,times(0)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 4 times- one for each party, 1 for group for Primary party as secondary party in ineligible, 1 for ops email
        verify(scheduleCommunicationService,times(3)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(2, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(OPS_EMAIL));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(OPS_EMAIL));
    }

    /*
        Conditions: 2 parties,Group email is present,Ops email is present, Additional Details present,
        and recording is successful for Primary party and failed for Secondary party
        and it is digital context
        and Primary party present
        and Secondary party not present and secondary party email is not present
    */
    @Test
    public void testGroupEmailWhenDigitalContextWithDiffRecordingAndPartyPresenceInfoAndScheduleCommIsSuccess(){
        final ScheduleEmailSmsRequest scheduleEmailSmsRequest = scheduleCommunicationRequest();
        AdditionalPartyDetails additionalPartyDetailsPrimary = new AdditionalPartyDetails();
        additionalPartyDetailsPrimary.setPrimaryParty(true);
        additionalPartyDetailsPrimary.setRecordingSuccessful(true);
        additionalPartyDetailsPrimary.setPartyPresent(true);
        AdditionalPartyDetails additionalPartyDetailsSecondary = new AdditionalPartyDetails();
        additionalPartyDetailsSecondary.setPrimaryParty(false);
        additionalPartyDetailsSecondary.setRecordingSuccessful(false);
        additionalPartyDetailsSecondary.setPartyPresent(true);
        List<AdditionalPartyDetails> additionalPartyDetailsList = new ArrayList<AdditionalPartyDetails>();
        additionalPartyDetailsList.add(additionalPartyDetailsPrimary);
        additionalPartyDetailsList.add(additionalPartyDetailsSecondary);
        scheduleEmailSmsRequest.setAdditionalPartyDetailsList(additionalPartyDetailsList);
        scheduleEmailSmsRequest.setGroupEmailId(GROUP_EMAIL_ID);
        scheduleEmailSmsRequest.setOpsEmail(OPS_EMAIL);

        final Map<String, PartyDetails> stringPartyDetailsMap = SessionServiceUtil.buildParties(2);
        stringPartyDetailsMap.get("1").setEmail(null);
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(stringPartyDetailsMap);
        when(sessionManager.getBranchContext()).thenReturn(null);

        final ScheduleEmailSmsResponse scheduleEmailSmsResponse = service.scheduleCommunication(scheduleEmailSmsRequest);
        //assert SendCommunicationIs Not called
        verify(communicationService,times(0)).sendEmailCommunication(any(PartyCommunicationDetails.class));
        //assert Schedule Communication is called 4 times- one for each party, 1 for group for Primary party as secondary party in ineligible, 1 for ops email
        verify(scheduleCommunicationService,times(3)).invokeScheduleCommunication(any(ScheduleCommunicationRequest.class));
        //verify that the success list has entries and the failed list is empty
        assertEquals(1, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").size());
        assertEquals(2, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("FailedEmailList").size());
        assertEquals(0, scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("FailedEmailList").size());
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(GROUP_EMAIL_ID));
        assertEquals(false,scheduleEmailSmsResponse.getEmailSentInfo().get("Primary").get("SuccessEmailList").contains(OPS_EMAIL));
        assertEquals(true,scheduleEmailSmsResponse.getEmailSentInfo().get("Secondary").get("SuccessEmailList").contains(OPS_EMAIL));
    }
}
