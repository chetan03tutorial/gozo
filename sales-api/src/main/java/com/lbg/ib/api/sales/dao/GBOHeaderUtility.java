package com.lbg.ib.api.sales.dao;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.OperatorTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.UNPMechanismTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.UsernameToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.net.URI.create;

@Component
public class GBOHeaderUtility {

    /**
     * Constant for CONTACT_POINT_ID.
     */
    public static final String            CONTACT_POINT_ID        = "CONTACT_POINT_ID";

    /**
     * Constant for CONTACT_POINT_TYPE.
     */
    public static final String            CONTACT_POINT_TYPE      = "CONTACT_POINT_TYPE";

    /**
     * Constant for CONTACT_POINT_HEADER.
     */
    public static final String            CONTACT_POINT_HEADER    = "ContactPointHeaderData";

    /**
     * Constant for CONTACT_POINT_DETAILS.
     */
    public static final String            CONTACT_POINT_DETAILS   = "ContactPointDetails";
    /**
     * Constant for CONTACT_POINT_PORTFOLIO.
     */
    public static final String            CONTACT_POINT_PORTFOLIO = "Cnt_Pnt_Prtflio";

    /**
     * Constant for APPLICATION_ID.
     */
    public static final String            APPLICATION_ID          = "APPLICATION_ID";

    /**
     * Constant for INITIAL_ORIGINATOR_TYPE.
     */
    public static final String            INITIAL_ORIGINATOR_TYPE = "INITIAL_ORIGINATOR_TYPE";
    public static final String            CONTACT_POINT           = "ContactPoint";
    public static final String            BAPI_INFORMATION        = "bapiInformation";

    private SessionManagementDAO          session;
    private final ConfigurationDAO        configDAO;
    private final ReferenceDataServiceDAO referenceDataService;

    @Autowired
    public GBOHeaderUtility(SessionManagementDAO session, ChannelBrandingDAO channelBrandingDAO,
            ConfigurationDAO configDAO, ReferenceDataServiceDAO referenceDataService) {
        this.session = session;
        this.configDAO = configDAO;
        this.referenceDataService = referenceDataService;
    }

    /**
     * Method to prepare headers for the GBO request.
     *
     * @return soapHeaders - List<SOAPHeader>
     */
    public List<SOAPHeader> prepareSoapHeader(String serviceAction, String serviceName) {
        return prepareSoapHeader(serviceAction, serviceName, false);
    }

    public List<SOAPHeader> prepareSoapHeader(final String serviceAction, final String serviceName, final boolean useServicename) {
        return prepareSoapHeader(serviceAction, useServicename ? serviceName : serviceAction, session.getUserContext());
    }

    //Custom Header added for Making the BAPI Header First

    public List<SOAPHeader> customPrepareSoapHeader(String serviceAction, String serviceName) {
        return customPrepareSoapHeader(serviceAction, serviceAction, session.getUserContext());
    }

    /**
     *
     * @param serviceAction
     * @param serviceName
     * @param userContext
     * @return
     */
    public List<SOAPHeader> customPrepareSoapHeader(String serviceAction, String serviceName, UserContext userContext) {
        List<SOAPHeader> soapHeaders = new ArrayList<SOAPHeader>();
        soapHeaders.add(prepareBAPIHeader(getBapiInformation(userContext, session.getUserInfo())));
        soapHeaders.add(prepareServiceRequestHeader(serviceAction, serviceName, userContext.getSessionId()));
        soapHeaders.add(prepareContactPointHeader(userContext.getChannelId()));
        soapHeaders.add(prepareSecurityRequestHeader());
        return soapHeaders;
    }

    /**
     *
     * @param serviceAction
     * @param serviceName
     * @param userContext
     * @return
     */
    public List<SOAPHeader> prepareSoapHeader(String serviceAction, String serviceName, UserContext userContext) {
        List<SOAPHeader> soapHeaders = new ArrayList<SOAPHeader>();
        soapHeaders.add(prepareServiceRequestHeader(serviceAction, serviceName, userContext.getSessionId()));
        soapHeaders.add(prepareContactPointHeader(userContext.getChannelId()));
        soapHeaders.add(prepareBAPIHeader(getBapiInformation(userContext, session.getUserInfo())));
        soapHeaders.add(prepareSecurityRequestHeader());
        return soapHeaders;
    }

    private static SOAPHeader prepareSecurityRequestHeader() {
        final SecurityHeaderType securityHeaderType = new SecurityHeaderType();
        final UsernameToken usernameToken = new UsernameToken();
        usernameToken.setId("LloydsTSBSecurityToken");
        usernameToken.setUsername("UNAUTHSALE");
        usernameToken.setUserType("013");
        usernameToken.setUNPMechanismType(UNPMechanismTypeEnum.value1);
        securityHeaderType.setMustReturn(false);
        securityHeaderType.setUsernameToken(usernameToken);
        SOAPHeader securityRequestSoapHeader = new SOAPHeader();
        securityRequestSoapHeader.setName("Security");
        securityRequestSoapHeader.setNameSpace(create("http://LB_GBO_Sales/Messages"));
        securityRequestSoapHeader.setPrefix("lgsm");
        securityRequestSoapHeader.setValue(securityHeaderType);
        return securityRequestSoapHeader;
    }

    private SOAPHeader prepareContactPointHeader(String channelId) {
        // Fetch Contact point ID and Contact point type from reference data.
        String contactPointID = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_ID);
        String contactPointType = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_TYPE);

        // if reference data value is null, the Contact point ID is picked from
        // configuration
        if (null == contactPointID) {
            contactPointID = (String) configDAO.getConfigurationValue(CONTACT_POINT_ID, channelId);
        }
        // if reference data value is null, the Contact point type is picked
        // from configuration
        if (null == contactPointType) {
            contactPointType = (String) configDAO.getConfigurationValue(CONTACT_POINT_DETAILS, CONTACT_POINT_TYPE);
        }
        Map<String, Object> contactPointMap = configDAO.getConfigurationItems(CONTACT_POINT_HEADER);

        final ContactPoint contactPointHeader = new ContactPoint();
        contactPointHeader.setContactPointType(contactPointType);
        contactPointHeader.setContactPointId(contactPointID);
        contactPointHeader.setApplicationId((String) contactPointMap.get(APPLICATION_ID));
        contactPointHeader.setInitialOriginatorType((String) contactPointMap.get(INITIAL_ORIGINATOR_TYPE));
        contactPointHeader.setInitialOriginatorId("10.245.224.125");
        contactPointHeader.setOperatorType(OperatorTypeEnum.Customer);
        contactPointHeader.setMustReturn(false);

        SOAPHeader contactPointSoapHeader = new SOAPHeader();
        contactPointSoapHeader.setName(CONTACT_POINT);
        contactPointSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        contactPointSoapHeader.setPrefix("ns4");
        contactPointSoapHeader.setValue(contactPointHeader);

        return contactPointSoapHeader;
    }

    public static SOAPHeader prepareServiceRequestHeader(String serviceAction, String serviceName,
            String correlationId) {
        final ServiceRequest serviceRequestHeader = new ServiceRequest();
        serviceRequestHeader.setAction(serviceAction);
        serviceRequestHeader.setFrom(create("10.245.211.214"));
        serviceRequestHeader.setMessageId(correlationId);
        serviceRequestHeader.setMustReturn(false);
        serviceRequestHeader.setServiceName(create(serviceName));

        SOAPHeader serviceRequestSoapHeader = new SOAPHeader();
        serviceRequestSoapHeader.setValue(serviceRequestHeader);
        serviceRequestSoapHeader.setName("ServiceRequest");
        serviceRequestSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        serviceRequestSoapHeader.setPrefix("ns4");
        return serviceRequestSoapHeader;
    }

    private static SOAPHeader prepareBAPIHeader(BapiInformation bapiInfo) {
        SOAPHeader bapiSoapHeader = new SOAPHeader();
        bapiSoapHeader.setValue(bapiInfo);
        bapiSoapHeader.setName(BAPI_INFORMATION);
        bapiSoapHeader.setPrefix("ns5");
        bapiSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        return bapiSoapHeader;
    }

    private static BapiInformation getBapiInformation(UserContext userContext,
            com.lbg.ib.api.sso.domain.user.Arrangement arrangement) {
        BapiInformation bapiInfo = new BapiInformation();
        bapiInfo.setBAPIId("B001");

        final OperationalVariables operationalVariables = new OperationalVariables();
        operationalVariables.setBForceHostCall(Boolean.FALSE);
        operationalVariables.setBPopulateCache(Boolean.FALSE);
        operationalVariables.setBBatchRetry(Boolean.FALSE);
        bapiInfo.setBAPIOperationalVariables(operationalVariables);

        if (null != userContext) {
            BAPIHeader bapiHeader = new BAPIHeader();
            bapiHeader.setUseridAuthor(userContext.getUserId());
            if (arrangement != null && arrangement.getInternalUserIdentifier() != null) {
                bapiHeader.setUseridAuthor(arrangement.getInternalUserIdentifier());
            }

            final HostInformation hostInformation = new HostInformation();
            hostInformation.setHost(userContext.getHost());
            hostInformation.setPartyid(userContext.getPartyId());
            hostInformation.setOcisid(userContext.getOcisId());
            bapiHeader.setStpartyObo(hostInformation);

            bapiHeader.setChanid(userContext.getChannelId());
            bapiHeader.setChansecmode(userContext.getChansecmode());
            bapiHeader.setSessionid(userContext.getSessionId());
            bapiHeader.setUserAgent(userContext.getUserAgent());
            bapiHeader.setInboxidClient(userContext.getInboxIdClient());
            bapiHeader.setChanctxt(BigInteger.valueOf(1L));
            bapiInfo.setBAPIHeader(bapiHeader);
        }
        return bapiInfo;
    }
}
