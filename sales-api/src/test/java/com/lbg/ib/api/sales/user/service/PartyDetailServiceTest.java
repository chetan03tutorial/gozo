package com.lbg.ib.api.sales.user.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.InvolvedPartyAuthorisation;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.InvolvedPartyAuthorisationServiceLocator;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.RetrieveMandatePartyRequest;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.RetrieveMandatePartyResponse;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.ContactPoint;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.Customer;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.ElectronicAddress;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.Individual;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.IndividualName;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.InvolvedParty;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.PostalAddress;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.PostalAddressComponent;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.PostalAddressComponentType;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.schema.enterprise.lcsm.ErrorInfo;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.schema.enterprise.lcsm.ResponseHeader;
import com.lbg.ib.api.sales.utils.CommonUtils;

@RunWith(MockitoJUnitRunner.class)
public class PartyDetailServiceTest {

    @Mock
    private SOAInvoker soaInvoker;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private LoggerDAO logger;

    @Mock
    private InvolvedPartyAuthorisationServiceLocator serviceLocator;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    private HandlerRegistry handleRegistry;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private InvolvedPartyAuthorisation involvedPartyAuthorisation;

    @Mock
    private CommonUtils commonUtils;

    @InjectMocks
    private PartyDetailServiceImpl partyDetailService;

    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;

    /*
     * private static final String ocisId = "889732198"; private static final String userId =
     * "DJ98239";
     */

    @Before
    public void setup() {
        when(session.getCustomerDetails()).thenReturn(customerInfo());
        when(session.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext("LTB"));
        session.getUserContext().setOcisId("1061335725");
        session.getUserContext().setPartyId("+440290239");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("involvedParty"));
        when(serviceLocator.getInvolvedPartyAuthorisationSOAPPortWSDDPortName()).thenReturn("ivp");
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString()))
                .thenReturn(HeaderServiceUtil.genericHeaderData());
        when(soapHeaderGenerator.getGenericSoapHeader(anyString(), anyString(), anyBoolean()))
                .thenReturn(HeaderServiceUtil.prepareSoapHeaders());
    }


    @Test
    public void successfullyRetrievePartyDetails() throws RemoteException, ErrorInfo {
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(externalServiceResponse(SUCCESS));
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(externalServiceResponse(SUCCESS));
        partyDetailService.retrievePartyDetails();
    }

    @Test
    public void successfullyRetrievePartyDetailsWhenEmailIsAbsent() throws RemoteException, ErrorInfo {
        RetrieveMandatePartyResponse response = externalServiceResponse(SUCCESS);
        ContactPoint[] contactPoints = response.getInvolvedParty().getContactPoint();
        contactPoints[0] = null;
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(response);
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(response);
        partyDetailService.retrievePartyDetails();
    }

    @Test(expected = ServiceException.class)
    public void failedToRetrievePartyDetails() throws RemoteException, ErrorInfo {
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(externalServiceResponse(FAILURE));
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(externalServiceResponse(FAILURE));
        partyDetailService.retrievePartyDetails();
    }

    @Test(expected = ServiceException.class)
    public void failureInRetrieveInvolvedPartyDetails() throws RemoteException, ErrorInfo {
        RetrieveMandatePartyResponse response = externalServiceResponse(SUCCESS);
        response.setInvolvedParty(null);
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(response);
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(response);
        partyDetailService.retrievePartyDetails();
    }

    @Test(expected = ServiceException.class)
    public void failureInRetrieveInvolvedPartyNameDetails() throws RemoteException, ErrorInfo {
        RetrieveMandatePartyResponse response = externalServiceResponse(SUCCESS);
        response.getInvolvedParty().setIsPlayingPrimaryRole(null);
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(response);
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(response);
        partyDetailService.retrievePartyDetails();
    }

    @Test(expected = ServiceException.class)
    public void failureInRetrieveInvolvedPartyDemographicDetails() throws RemoteException, ErrorInfo {
        RetrieveMandatePartyResponse response = externalServiceResponse(SUCCESS);
        response.getInvolvedParty().setContactPoint(null);
        /*
         * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
         * any(Object[].class))) .thenReturn(response);
         */
        when(involvedPartyAuthorisation.retrieveMandateParty(any(RetrieveMandatePartyRequest.class))).thenReturn(response);
        partyDetailService.retrievePartyDetails();
    }

    private RetrieveMandatePartyResponse retrieveMandatePartyResponse() {
        RetrieveMandatePartyResponse response = new RetrieveMandatePartyResponse();
        InvolvedParty invParty = new Individual();
        ContactPoint[] contactPoints = new ContactPoint[4];
        invParty.setIsPlayingPrimaryRole(customer());
        contactPoints[0] = email();
        contactPoints[1] = postalAddress();
        invParty.setContactPoint(contactPoints);
        response.setInvolvedParty(invParty);
        return response;
    }

    private RetrieveMandatePartyResponse externalServiceResponse(int scenario) {
        RetrieveMandatePartyResponse response = retrieveMandatePartyResponse();
        ResponseHeader headers = null;
        switch (scenario) {
            case 0:
                headers = successHeaders();
                break;
            case 1:
                headers = errorHeaders();
                break;
            default:
                headers = successHeaders();
        }
        response.setResponseHeader(headers);
        return response;
    }

    private ResponseHeader errorHeaders() {
        ResponseHeader headers = defaultResponseHeader(1);
        Condition[] conditions = headers.getResultCondition().getExtraConditions();
        conditions[0] = condition(1000, "SOME_ERROR");
        return headers;
    }

    private ResponseHeader successHeaders() {
        ResponseHeader headers = defaultResponseHeader(1);
        Condition[] conditions = headers.getResultCondition().getExtraConditions();
        conditions[0] = condition(0, null);
        RetrieveMandatePartyResponse response = retrieveMandatePartyResponse();
        response.setResponseHeader(headers);
        return headers;
    }

    private ResponseHeader defaultResponseHeader(int numOfCondition) {
        ResponseHeader headers = new ResponseHeader();
        ResultCondition resultCondition = resultCondition(numOfCondition);
        headers.setResultCondition(resultCondition);
        return headers;
    }

    private ResultCondition resultCondition(int numOfCondition) {
        ResultCondition resultCondition = new ResultCondition();
        Condition[] conditions = new Condition[numOfCondition];
        resultCondition.setExtraConditions(conditions);
        return resultCondition;
    }

    private Condition condition(int reasonCode, String reasonText) {
        Condition condition = new Condition();
        condition.setReasonCode(reasonCode);
        condition.setReasonText(reasonText);
        return condition;
    }

    private Customer customer() {
        Customer customer = new Customer();
        customer.setCustomerName(customerName());
        return customer;
    }

    private IndividualName customerName() {
        IndividualName customerName = new IndividualName();
        customerName.setFirstName("Nikhil");
        return customerName;
    }

    private PostalAddress postalAddress() {
        PostalAddress postalAddress = new PostalAddress();
        PostalAddressComponent[] addressComponents = new PostalAddressComponent[2];
        postalAddress.setAddressComponents(addressComponents);
        String[] addressLines = {"22 LEUCHA ROAD", "WALTHAMSTOW", "LONDON"};
        addressComponents[0] = addressComponent(PostalAddressComponentType.value1, addressLines);
        String[] postalCode = {"E17  7LG"};
        addressComponents[1] = addressComponent(PostalAddressComponentType.value15, postalCode);
        return postalAddress;
    }

    private PostalAddressComponent addressComponent(PostalAddressComponentType type, String[] values) {
        PostalAddressComponent addressComponent = new PostalAddressComponent();
        addressComponent.setType(type);
        addressComponent.setValue(values);
        return addressComponent;
    }

    private ElectronicAddress email() {
        ElectronicAddress email = new ElectronicAddress();
        email.setEmail("test@test.domain");
        return email;
    }

    private CustomerInfo customerInfo() {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setAccountNumber("34658709");
        customerInfo.setSortCode("1292OPSA");
        return customerInfo;
    }
}
