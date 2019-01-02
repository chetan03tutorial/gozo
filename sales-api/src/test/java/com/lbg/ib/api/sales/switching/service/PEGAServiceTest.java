package com.lbg.ib.api.sales.switching.service;

import com.lbg.ib.api.sales.application.service.ApplicationService;
import com.lbg.ib.api.sales.dao.mapper.CreateCaseAccountMapper;
import com.lbg.ib.api.sales.dao.mapper.CreateCaseRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.pega.GenericResponseType;
import com.lbg.ib.api.sales.soapapis.pega.messages.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.pega.messages.ResultCondition;
import com.lbg.ib.api.sales.soapapis.pega.objects.CreateCaseRequestType;
import com.lbg.ib.api.sales.soapapis.pega.objects.ServicesServiceLocator;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.SwitchingAccount;
import com.lbg.ib.api.sales.switching.domain.SwitchingParty;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lloydsbanking.xml.CreateCasePayloadRequestType;
import com.lloydsbanking.xml.CreateCasePayloadResponseType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PEGAServiceTest {

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private ConfigurationDAO configManager;

    @Mock
    private SearchPartyService searchPartyService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ConfigurationDAO configurationDAO;

    @InjectMocks
    private CreateCaseAccountMapper createCaseAccountMapper = new CreateCaseAccountMapper(searchPartyService, applicationService, configurationDAO);

    @InjectMocks
    private CreateCaseRequestMapper createCaseRequestMapper = new CreateCaseRequestMapper(sessionManager, createCaseAccountMapper);

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    private LoggerDAO logger;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private HandlerRegistry handleRegistry;

    @Mock
    private SOAInvoker soaInvoker;

    @Mock
    private ServicesServiceLocator serviceLocator;

    @InjectMocks
    private PEGAService pegaService = new PEGAService(sessionManager, createCaseRequestMapper, beanLoader, resolver, logger);

    private static final String brand = "LTB";

    @Before
    public void setup() {
        when(serviceLocator.getServicesPortSOAPWSDDPortName()).thenReturn("genericCreateCase");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("CreateCaseDetails"));
        when(configManager.getConfigurationStringValue(anyString(), anyString())).thenReturn(brand);
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(SessionServiceUtil.buildParties(2));
        when(sessionManager.getSessionId()).thenReturn("abcz");
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(ServicesServiceLocator.class)).thenReturn(serviceLocator);
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(parties());
    }

    private AccountSwitchingRequest accountSwitchingRequest(){
        final AccountSwitchingRequest request = new AccountSwitchingRequest();
        final Date switchDate = new Date();
        request.setSwitchDate(switchDate);
        request.setCanBeOverDrawn(true);
        request.setPayOdAmount(new BigDecimal(34534.43));

        final SwitchingAccount newAccountDetails = new SwitchingAccount("402715", "74885074", "JohnyyDoeyy", "Lloyds Bank Ltd");
        request.setNewAccountDetails(newAccountDetails);

        final SwitchingAccount oldAccountDetails = new SwitchingAccount("203492", "37442575", "JohnyyDoeyy", "Barclays Bank");
        request.setOldAccountDetails(oldAccountDetails);

        final UnstructuredPostalAddress unstructuredPostalAddress = new UnstructuredPostalAddress("Empire Way", "Wembley", null, null, null, null, null, null, "HA9 0NH", null);
        final PostalAddressComponent postalAddressComponent = new PostalAddressComponent("0505", true, false, unstructuredPostalAddress);
        final SwitchingParty party = new SwitchingParty("Mr", "Johnyy", "", "Doeyy", "", "British", null, "45455", "05/21", postalAddressComponent, true, "1234567890", true, true);

        request.setParties(Arrays.asList(party));

        return request;
    }

    @Test
    public void testCreateCase(){
        final AccountSwitchingRequest accountSwitchingRequest = accountSwitchingRequest();
        Object response = successResponse(accountSwitchingRequest, "0", null, null);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(response);
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(parties());
        final String caseId = pegaService.createPegaCase(accountSwitchingRequest);
        assertEquals("132342", caseId);
    }

    @Test(expected = ServiceException.class)
    public void testErrorResponse(){
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(null);
        final AccountSwitchingRequest accountSwitchingRequest = accountSwitchingRequest();
        Object response = successResponse(accountSwitchingRequest, "1", "ERROR_404", "Unable to find backend service");
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(response);
        pegaService.createPegaCase(accountSwitchingRequest());
    }

    @Test(expected = ServiceException.class)
    public void testNullResponse(){
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(null);
        when(resolver.resolve(anyString())).thenReturn(new ResponseError("XXX","Service Unavailable"));
        pegaService.createPegaCase(accountSwitchingRequest());
    }

    private List<IBParties> parties(){
        final List<IBParties> parties = new ArrayList<IBParties>();
        final IBParties party = new IBParties();
        party.setFirstName("Johnyy");
        party.setLastName("Doeyy");
        party.setPartyId("5436645");
        party.setBirthDate("1962-01-01");
        parties.add(party);
        return parties;
    }

    private GenericResponseType successResponse(final AccountSwitchingRequest accountSwitchingRequest, final String severityCode, final String reasonCode, final String reasonText){
        final GenericResponseType response = new GenericResponseType();
        final ResponseHeader responseHeader = new ResponseHeader();
        final ResultCondition resultConditions = new ResultCondition();
        resultConditions.setSeverityCode(severityCode);
        resultConditions.setReasonCode(reasonCode);
        resultConditions.setReasonText(reasonText);
        responseHeader.setResultConditions(resultConditions);
        response.setResponseHeader(responseHeader);

        final CreateCaseRequestType request = createCaseRequestMapper.create(accountSwitchingRequest);
        final CreateCasePayloadRequestType payloadRequestType = (CreateCasePayloadRequestType)request.getPayload();

        final CreateCasePayloadResponseType payloadResponse = new CreateCasePayloadResponseType();
        payloadResponse.setRequestResponseCorrelationId(payloadRequestType.getRequestResponseCorrelationId());
        payloadRequestType.getInitiateSwitchIn().setCaseId("132342");
        payloadResponse.setInitiateSwitchIn(payloadRequestType.getInitiateSwitchIn());
        response.setPayload(payloadResponse);

        return response;
    }
}
