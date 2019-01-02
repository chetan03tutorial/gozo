/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao;

import static java.net.URI.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.shared.domain.DAOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.BAPIHeader;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.BapiInformation;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.HostInformation;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.OperationalVariables;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.OperatorTypeEnum;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.UNPMechanismTypeEnum;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.ContactPoint;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.ServiceRequest;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.security.UsernameToken;
import com.lloydstsb.ib.salsa.crs.messages.SOAPHeader;

@Component
public class SalsaGBOHeaderUtility {

    /**
     * Constant for CONTACT_POINT_ID.
     */
    public static final String      CONTACT_POINT_ID        = "CONTACT_POINT_ID";

    /**
     * Constant for CONTACT_POINT_TYPE.
     */
    public static final String      CONTACT_POINT_TYPE      = "CONTACT_POINT_TYPE";

    /**
     * Constant for CONTACT_POINT_HEADER.
     */
    public static final String      CONTACT_POINT_HEADER    = "ContactPointHeaderData";

    /**
     * Constant for CONTACT_POINT_DETAILS.
     */
    public static final String      CONTACT_POINT_DETAILS   = "ContactPointDetails";
    /**
     * Constant for CONTACT_POINT_PORTFOLIO.
     */
    public static final String      CONTACT_POINT_PORTFOLIO = "Cnt_Pnt_Prtflio";

    /**
     * Constant for APPLICATION_ID.
     */
    public static final String      APPLICATION_ID          = "APPLICATION_ID";

    /**
     * Constant for INITIAL_ORIGINATOR_TYPE.
     */
    public static final String      INITIAL_ORIGINATOR_TYPE = "INITIAL_ORIGINATOR_TYPE";
    public static final String      CONTACT_POINT           = "ContactPoint";
    public static final String      BAPI_INFORMATION        = "bapiInformation";

    @Autowired
    private ChannelBrandingDAO      channelBrandingDAO;
    @Autowired
    private ConfigurationDAO        configDAO;
    @Autowired
    private ReferenceDataServiceDAO referenceDataService;
    @Autowired
    private SessionManagementDAO    session;

    /**
     * Method to prepare headers for the GBO request.
     *
     * @return soapHeaders - List<SOAPHeader>
     */
    public List<SOAPHeader> prepareSoapHeader(String serviceAction, String serviceName) {
        UserContext userContext = session.getUserContext();
        List<SOAPHeader> soapHeaders = new ArrayList<SOAPHeader>();
        soapHeaders.add(prepareServiceRequestHeader(serviceAction, serviceName, userContext.getSessionId()));
        soapHeaders.add(prepareBAPIHeader(getBapiInformation(userContext)));
        soapHeaders.add(prepareContactPointHeader());
        soapHeaders.add(prepareSecurityRequestHeader());
        return soapHeaders;
    }

    private static SOAPHeader prepareBAPIHeader(BapiInformation bapiInfo) {
        SOAPHeader bapiSoapHeader = new SOAPHeader();
        bapiSoapHeader.setValue(bapiInfo);
        bapiSoapHeader.setName(BAPI_INFORMATION);
        bapiSoapHeader.setPrefix("ns5");
        bapiSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        return bapiSoapHeader;
    }

    private static BapiInformation getBapiInformation(UserContext userContext) {
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

    private SOAPHeader prepareContactPointHeader() {
        // Fetch Contact point ID and Contact point type from reference data.
        String contactPointID = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_ID);
        String contactPointType = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_TYPE);

        // if reference data value is null, the Contact point ID is picked from
        // configuration
        DAOResponse<ChannelBrandDTO> channelBrand = channelBrandingDAO.getChannelBrand();
        if (null == contactPointID && channelBrand.getError() == null) {
            contactPointID = (String) configDAO.getConfigurationValue(CONTACT_POINT_ID,
                    channelBrand.getResult().getChannelId());
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

    private static SOAPHeader prepareServiceRequestHeader(String serviceAction, String serviceName,
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

}
