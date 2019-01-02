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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.ColleagueDetails;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.TransactionDetails;
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
public class SalsaMCAHeaderUtility {

    private static final String HOST_IP                 = "10.245.211.214";

    /**
     * Constant for CONTACT_POINT_ID.
     */
    public static final String  CONTACT_POINT_ID        = "CONTACT_POINT_ID";

    /**
     * Constant for CONTACT_POINT_TYPE.
     */
    public static final String  CONTACT_POINT_TYPE      = "CONTACT_POINT_TYPE";

    /**
     * Constant for CONTACT_POINT_HEADER.
     */
    public static final String  CONTACT_POINT_HEADER    = "ContactPointHeaderData";

    /**
     * Constant for CONTACT_POINT_DETAILS.
     */
    public static final String  CONTACT_POINT_DETAILS   = "ContactPointDetails";
    /**
     * Constant for CONTACT_POINT_PORTFOLIO.
     */
    public static final String  CONTACT_POINT_PORTFOLIO = "Cnt_Pnt_Prtflio";

    /**
     * Constant for APPLICATION_ID.
     */
    public static final String  APPLICATION_ID          = "APPLICATION_ID";

    /**
     * Constant for INITIAL_ORIGINATOR_TYPE.
     */
    public static final String  INITIAL_ORIGINATOR_TYPE = "INITIAL_ORIGINATOR_TYPE";
    public static final String  CONTACT_POINT           = "ContactPoint";
    public static final String  BAPI_INFORMATION        = "bapiInformation";

    private static BapiInformation getBapiInformation(UserContext userContext, BranchContext branchContext,
            Arrangement arrangement) {
        BapiInformation bapiInfo = new BapiInformation();
        bapiInfo.setBAPIId("B001");

        final OperationalVariables operationalVariables = new OperationalVariables();
        operationalVariables.setBForceHostCall(Boolean.FALSE);
        operationalVariables.setBPopulateCache(Boolean.FALSE);
        operationalVariables.setBBatchRetry(Boolean.FALSE);
        bapiInfo.setBAPIOperationalVariables(operationalVariables);

        if (null != userContext) {
            BAPIHeader bapiHeader = new BAPIHeader();
            if (null != arrangement && null != arrangement.getInternalUserIdentifier()) {
                bapiHeader.setUseridAuthor(arrangement.getInternalUserIdentifier());
            } else {
                bapiHeader.setUseridAuthor(userContext.getUserId());
            }

            final HostInformation hostInformation = new HostInformation();
            hostInformation.setHost(userContext.getHost());
            hostInformation.setPartyid(userContext.getPartyId());
            hostInformation.setOcisid(userContext.getOcisId());
            bapiHeader.setStpartyObo(hostInformation);

            bapiHeader.setChanid(userContext.getChannelId().replace("I", "B"));
            bapiHeader.setChansecmode(userContext.getChansecmode());
            bapiHeader.setSessionid(userContext.getSessionId());
            bapiHeader.setUserAgent(userContext.getUserAgent());
            bapiHeader.setInboxidClient(userContext.getInboxIdClient());
            bapiHeader.setChanctxt(BigInteger.valueOf(1L));
            if (null != branchContext.getColleagueRoles()) {
                ColleagueDetails[] stcolleaguedetails = new ColleagueDetails[branchContext.getColleagueRoles().size()];
                int i = 0;
                for (String role : branchContext.getColleagueRoles()) {
                    ColleagueDetails colleagueDetails = new ColleagueDetails();
                    colleagueDetails.setRoleColleague(role);
                    colleagueDetails.setColleagueid(branchContext.getColleagueId());
                    stcolleaguedetails[i++] = colleagueDetails;
                }
                bapiHeader.setStcolleaguedetails(stcolleaguedetails);
            }
            TransactionDetails[] transactionDetailsArray = new TransactionDetails[1];
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setInputofcflgstatuscd(BigInteger.ZERO);
            transactionDetails.setInputofcstatuscd(BigInteger.ZERO);
            transactionDetails.setOverridedtlscd(BigInteger.ONE);
            transactionDetails.setSkllvlacqdcd(BigInteger.ZERO);
            transactionDetails.setHostapplicationid("Digital Branch");
            transactionDetails.setExtsysid(94);
            transactionDetailsArray[0] = transactionDetails;
            bapiHeader.setSttransactiondetails(transactionDetailsArray);
            bapiInfo.setBAPIHeader(bapiHeader);
        }
        return bapiInfo;
    }

    private static SOAPHeader prepareBAPIHeader(BapiInformation bapiInfo) {
        SOAPHeader bapiSoapHeader = new SOAPHeader();
        bapiSoapHeader.setValue(bapiInfo);
        bapiSoapHeader.setName(BAPI_INFORMATION);
        bapiSoapHeader.setPrefix("ns5");
        bapiSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        return bapiSoapHeader;
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

    private static SOAPHeader prepareServiceRequestHeader(String serviceAction, String serviceName) {
        final ServiceRequest serviceRequestHeader = new ServiceRequest();
        serviceRequestHeader.setAction(serviceAction);
        serviceRequestHeader.setFrom(create(HOST_IP));
        serviceRequestHeader.setMessageId("...");
        serviceRequestHeader.setMustReturn(false);
        serviceRequestHeader.setServiceName(create("CharacterEvaluationService"));

        SOAPHeader serviceRequestSoapHeader = new SOAPHeader();
        serviceRequestSoapHeader.setValue(serviceRequestHeader);
        serviceRequestSoapHeader.setName("ServiceRequest");
        serviceRequestSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        serviceRequestSoapHeader.setPrefix("ns4");
        return serviceRequestSoapHeader;
    }

    private SessionManagementDAO          session;

    private final ConfigurationDAO        configDAO;

    private final ReferenceDataServiceDAO referenceDataService;

    @Autowired
    public SalsaMCAHeaderUtility(SessionManagementDAO session, ChannelBrandingDAO channelBrandingDAO,
            ConfigurationDAO configDAO, ReferenceDataServiceDAO referenceDataService) {
        this.session = session;
        this.configDAO = configDAO;
        this.referenceDataService = referenceDataService;
    }

    private SOAPHeader prepareContactPointHeader(String channelId, BranchContext branchContext) {
        String contactPointID = null;
        String contactPointType = null;
        if (null == contactPointID) {
            contactPointID = (String) configDAO.getConfigurationValue(CONTACT_POINT_ID, channelId);
        }
        if (null == contactPointType) {
            contactPointType = (String) configDAO.getConfigurationValue(CONTACT_POINT_DETAILS, CONTACT_POINT_TYPE);
        }
        Map<String, Object> contactPointMap = configDAO.getConfigurationItems(CONTACT_POINT_HEADER);

        final ContactPoint contactPointHeader = new ContactPoint();
        contactPointHeader.setContactPointType(contactPointType);
        contactPointHeader.setContactPointId("0000" + branchContext.getOriginatingSortCode());
        contactPointHeader.setApplicationId("Digital Branch");
        contactPointHeader.setInitialOriginatorType((String) contactPointMap.get(INITIAL_ORIGINATOR_TYPE));
        contactPointHeader.setInitialOriginatorId(branchContext.getColleagueId());
        contactPointHeader.setOperatorType(OperatorTypeEnum.Staff);
        contactPointHeader.setMustReturn(false);

        SOAPHeader contactPointSoapHeader = new SOAPHeader();
        contactPointSoapHeader.setName(CONTACT_POINT);
        contactPointSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        contactPointSoapHeader.setPrefix("ns4");
        contactPointSoapHeader.setValue(contactPointHeader);

        return contactPointSoapHeader;
    }

    /**
     * Method to prepare headers for the GBO request.
     *
     * @return soapHeaders - List<SOAPHeader>
     */
    public List<SOAPHeader> prepareSoapHeader(String serviceAction, String serviceName) {
        return prepareSoapHeader(serviceAction, serviceAction, session.getUserContext(), session.getBranchContext(),
                session.getUserInfo());
    }

    /**
     * @param serviceAction
     * @param serviceName
     * @param userContext
     * @return
     */
    public List<SOAPHeader> prepareSoapHeader(String serviceAction, String serviceName, UserContext userContext,
            BranchContext branchContext, Arrangement arrangement) {
        List<SOAPHeader> soapHeaders = new ArrayList<SOAPHeader>();
        soapHeaders.add(prepareServiceRequestHeader(serviceAction, serviceName));
        soapHeaders.add(prepareContactPointHeader(userContext.getChannelId(), branchContext));
        soapHeaders.add(prepareBAPIHeader(getBapiInformation(userContext, branchContext, arrangement)));
        soapHeaders.add(prepareSecurityRequestHeader());
        return soapHeaders;
    }
}