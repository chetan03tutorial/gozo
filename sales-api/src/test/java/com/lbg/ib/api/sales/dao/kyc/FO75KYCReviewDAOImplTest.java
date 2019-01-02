package com.lbg.ib.api.sales.dao.kyc;

import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.OperatorTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075Req;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075Resp;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075Result;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075_EnqKYCDetails_PortType;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075_EnqKYCDetails_ServiceLocator;
import com.lbg.ib.api.sales.soapapis.ocis.f075.KYCControlData;

@RunWith(MockitoJUnitRunner.class)
public class FO75KYCReviewDAOImplTest {

    public static final String                CODE            = "code";
    public static final String                MESSAGE         = "message, Called Method: search, Method Call Params: PostcodeDTO{inPostcode='asd', outPostcode='ad'}";
    public static final String                MARSHALLED_BODY = "marshalledBody";
    @InjectMocks
    private FO75KYCReviewDAOImpl              fo75;
    @Mock
    private F075_EnqKYCDetails_PortType       f075KycDetails;

    @Mock
    private LoggerDAO                         logger;

    @Mock
    private SessionManagementDAO              session;

    @Mock
    private GBOHeaderUtility                  headerUtility;

    @Mock
    private MCAHeaderUtility                  mcaheaderUtility;

    @Mock
    private F075_EnqKYCDetails_ServiceLocator serviceLocator;

    @Mock
    private HandlerRegistry                   handleRegistry;

    @Test
    public void nextReviewDateWithError() throws Exception {

        UserContext userContext = new UserContext("userId", "ipAddress", "sessionId", "partyId", "ocisId", "channelId",
                "chansecMode", "userAgent", "language", "inboxIdClient", "host");

        when(session.getUserContext()).thenReturn(userContext);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(headerUtility.prepareSoapHeader(any(String.class), any(String.class)))
                .thenReturn(asList(bapiHeader(), contactPointHeader(), prepareServiceRequestHeader("fo75", "fo75")));
        when(serviceLocator.getServiceName()).thenReturn(new QName("FO75"));
        when(serviceLocator.getF075_EnqKYCDetailsWSDDPortName()).thenReturn("FO75");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        F075Resp fo75Resp = new F075Resp();
        F075Result fo75Result = new F075Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(123);
        fo75Result.setResultCondition(resultCondition);
        fo75Resp.setF075Result(fo75Result);
        when(f075KycDetails.f075(any(F075Req.class))).thenReturn(fo75Resp);
        // fo75 = new FO75KYCReviewDAOImpl(f075KycDetails, logger, session,
        // headerUtility, mcaheaderUtility, serviceLocator);
        DAOResponse<String> response = fo75.nextReviewDate("123");
        response.getError();
        assertEquals(response.getError().getErrorCode(), "123");

    }

    @Test
    public void nextReviewDate() throws Exception {

        UserContext userContext = new UserContext("userId", "ipAddress", "sessionId", "partyId", "ocisId", "channelId",
                "chansecMode", "userAgent", "language", "inboxIdClient", "host");

        when(session.getUserContext()).thenReturn(userContext);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(headerUtility.prepareSoapHeader(any(String.class), any(String.class)))
                .thenReturn(asList(bapiHeader(), contactPointHeader(), prepareServiceRequestHeader("fo75", "fo75")));
        when(serviceLocator.getServiceName()).thenReturn(new QName("FO75"));
        when(serviceLocator.getF075_EnqKYCDetailsWSDDPortName()).thenReturn("FO75");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        F075Resp fo75Resp = new F075Resp();
        F075Result fo75Result = new F075Result();
        ResultCondition resultCondition = new ResultCondition();

        fo75Result.setResultCondition(resultCondition);
        KYCControlData kycControlData = new KYCControlData();
        kycControlData.setDataNxtReviewDt("17-12-1985");
        fo75Resp.setF075Result(fo75Result);
        fo75Resp.setKYCControlData(kycControlData);
        when(f075KycDetails.f075(any(F075Req.class))).thenReturn(fo75Resp);
        // fo75 = new FO75KYCReviewDAOImpl(f075KycDetails, logger, session,
        // headerUtility, mcaheaderUtility, serviceLocator);
        DAOResponse<String> response = fo75.nextReviewDate("123");
        response.getError();
        assertEquals(response.getResult(), "17-12-1985");

    }

    private SOAPHeader contactPointHeader() {
        SOAPHeader soapHeader = new SOAPHeader();
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setApplicationId("appId");
        contactPoint.setContactPointId("pointId");
        contactPoint.setContactPointType("pointType");
        contactPoint.setInitialOriginatorType("originatorType");
        contactPoint.setOperatorType(OperatorTypeEnum.Customer);
        soapHeader.setName(GBOHeaderUtility.CONTACT_POINT);
        soapHeader.setValue(contactPoint);
        return soapHeader;
    }

    private SOAPHeader bapiHeader() {
        SOAPHeader soapHeader = new SOAPHeader();
        BapiInformation bapiInformation = new BapiInformation();
        BAPIHeader bapiHeader = new BAPIHeader();
        bapiHeader.setChanid("chanId");
        bapiHeader.setChansecmode("chansemode");
        bapiHeader.setInboxidClient("inboxId");
        bapiHeader.setUserAgent("agent");
        bapiHeader.setSessionid("session");
        bapiHeader.setUseridAuthor("author");

        HostInformation stpartyObo = new HostInformation();
        stpartyObo.setPartyid("partyId");
        stpartyObo.setHost("host");
        stpartyObo.setOcisid("ocis");
        bapiHeader.setStpartyObo(stpartyObo);
        bapiInformation.setBAPIHeader(bapiHeader);
        bapiInformation.setBAPIOperationalVariables(new OperationalVariables());
        soapHeader.setName(GBOHeaderUtility.BAPI_INFORMATION);
        soapHeader.setValue(bapiInformation);
        return soapHeader;
    }

    private SOAPHeader prepareServiceRequestHeader(String serviceAction, String serviceName) throws URISyntaxException {
        final ServiceRequest serviceRequestHeader = new ServiceRequest();
        serviceRequestHeader.setAction(serviceAction);
        serviceRequestHeader.setMessageId("...");
        serviceRequestHeader.setMustReturn(false);
        serviceRequestHeader.setServiceName(create(serviceName));
        serviceRequestHeader.setFrom(new URI("http://localhost"));
        SOAPHeader serviceRequestSoapHeader = new SOAPHeader();
        serviceRequestSoapHeader.setValue(serviceRequestHeader);
        serviceRequestSoapHeader.setName("ServiceRequest");
        serviceRequestSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        serviceRequestSoapHeader.setPrefix("ns4");
        return serviceRequestSoapHeader;
    }
}