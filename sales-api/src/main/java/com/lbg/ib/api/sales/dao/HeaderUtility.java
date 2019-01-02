package com.lbg.ib.api.sales.dao;

import static java.net.URI.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.oasis_open.docs.SecurityHeaderType;
import org.oasis_open.docs.UsernameToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.ColleagueDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.TransactionDetails;
import com.lloydstsb.www.Schema.Enterprise.LCSM.BAPIHeader;
import com.lloydstsb.www.Schema.Enterprise.LCSM.BapiInformation;
import com.lloydstsb.www.Schema.Enterprise.LCSM.HostInformation;
import com.lloydstsb.www.Schema.Enterprise.LCSM.OperationalVariables;
import com.lloydstsb.www.Schema.Infrastructure.OperatorTypeEnum;
import com.lloydstsb.www.Schema.Infrastructure.UNPMechanismTypeEnum;
import com.lloydstsb.www.Schema.Infrastructure.SOAP.ContactPoint;
import com.lloydstsb.www.Schema.Infrastructure.SOAP.ServiceRequest;

import LIB_SIM_GMO.SOAPHeader;

@Component
public class HeaderUtility {

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
    public HeaderUtility(SessionManagementDAO session, ChannelBrandingDAO channelBrandingDAO,
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
        return prepareSoapHeader(serviceAction, serviceAction, session.getUserContext());
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
        soapHeaders.add(prepareServiceRequestHeader(serviceAction, serviceName));
        soapHeaders.add(prepareContactPointHeader(userContext.getChannelId()));
        soapHeaders.add(prepareBAPIHeader(getBapiInformation(userContext), userContext));
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
        // securityRequestSoapHeader.setNameSpace(create("http://LB_GBO_Sales/Messages"));
        securityRequestSoapHeader.setPrefix("lgsm");
        securityRequestSoapHeader.setValue(securityHeaderType);
        return securityRequestSoapHeader;
    }

    private SOAPHeader prepareContactPointHeader(String channelId) {
        // Fetch Contact point ID and Contact point type from reference data.
        String contactPointID = "0000115219";// referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                                             // CONTACT_POINT_ID);
        String contactPointType = "003";// referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                                        // CONTACT_POINT_TYPE);

        Map<String, Object> contactPointMap = configDAO.getConfigurationItems(CONTACT_POINT_HEADER);

        final ContactPoint contactPointHeader = new ContactPoint();
        contactPointHeader.setContactPointType(contactPointType);
        /*
         * if (null != session.getBranchContext()) {
         * contactPointHeader.setContactPointId("0000" +
         * session.getBranchContext().getOriginatingSortCode());
         * contactPointHeader.setInitialOriginatorType((String)
         * contactPointMap.get(INITIAL_ORIGINATOR_TYPE));
         * contactPointHeader.setOperatorType(OperatorTypeEnum.Staff);
         * contactPointHeader.setApplicationId("Digital Branch");
         * contactPointHeader.setInitialOriginatorId(session.getBranchContext().
         * getColleagueId()); }else{
         * contactPointHeader.setContactPointId(contactPointID);
         * contactPointHeader.setApplicationId("Internet Banking");
         * contactPointHeader.setOperatorType(OperatorTypeEnum.Customer);
         * contactPointHeader.setInitialOriginatorId("127.0.0.1");
         * contactPointHeader.setInitialOriginatorType("Browser"); }
         */

        contactPointHeader.setContactPointId(contactPointID);
        contactPointHeader.setApplicationId("Internet Banking");
        contactPointHeader.setOperatorType(OperatorTypeEnum.Customer);
        contactPointHeader.setInitialOriginatorId("127.0.0.1");
        contactPointHeader.setInitialOriginatorType("Browser");

        contactPointHeader.setMustReturn(false);

        SOAPHeader contactPointSoapHeader = new SOAPHeader();
        contactPointSoapHeader.setName(CONTACT_POINT);
        contactPointSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        contactPointSoapHeader.setPrefix("ns4");
        contactPointSoapHeader.setValue(contactPointHeader);

        return contactPointSoapHeader;
    }

    private static SOAPHeader prepareServiceRequestHeader(String serviceAction, String serviceName) {

        final ServiceRequest serviceRequestHeader = new ServiceRequest();
        serviceRequestHeader.setAction("recordArrangementQuestionnaireResult");
        /* serviceRequestHeader.setFrom(create("10.16.17.188")); */
        serviceRequestHeader.setFrom(create("10.16.17.188"));
        serviceRequestHeader.setMessageId("1u9ftstz6xb7m2btccxhhe40qb");
        serviceRequestHeader.setMustReturn(false);
        /* serviceRequestHeader.setServiceName(create(serviceName)); */
        // "{http://www.lloydstsb.com/Schema/Enterprise/LCSM_ArrangementNegotiation}ArrangementSetupService"

        serviceRequestHeader.setServiceName(create(
                "http://www.lloydstsb.com/Schema/Enterprise/LCSM_ArrangementNegotiationArrangementSetupService"));

        SOAPHeader serviceRequestSoapHeader = new SOAPHeader();
        serviceRequestSoapHeader.setValue(serviceRequestHeader);
        serviceRequestSoapHeader.setName("ServiceRequest");
        /*
         * serviceRequestSoapHeader.setNameSpace(create(
         * "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
         */
        serviceRequestSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        serviceRequestSoapHeader.setPrefix("ns4");
        return serviceRequestSoapHeader;
    }

    private SOAPHeader prepareBAPIHeader(BapiInformation bapiInfo, UserContext userContext) {
        SOAPHeader bapiSoapHeader = new SOAPHeader();
        bapiSoapHeader.setValue(bapiInfo);
        bapiSoapHeader.setName(BAPI_INFORMATION);
        bapiSoapHeader.setPrefix("ns5");
        /*
         * bapiSoapHeader.setNameSpace(create(
         * "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
         */

        bapiSoapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        return bapiSoapHeader;
    }

    private BapiInformation getBapiInformation(UserContext userContext) {
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
            /*
             * if (null != session.getBranchContext()) {
             * bapiHeader.setChanid(userContext.getChannelId().replace("I",
             * "B"));
             *
             * BranchContext branchContext = session.getBranchContext(); if
             * (null != branchContext.getColleagueRoles()) {
             * com.lloydstsb.www.Schema.Enterprise.LCSM.ColleagueDetails[]
             * stcolleaguedetails = new
             * com.lloydstsb.www.Schema.Enterprise.LCSM.ColleagueDetails[
             * branchContext.getColleagueRoles().size()]; int i = 0; for (String
             * role : branchContext.getColleagueRoles()) {
             * com.lloydstsb.www.Schema.Enterprise.LCSM.ColleagueDetails
             * colleagueDetails = new
             * com.lloydstsb.www.Schema.Enterprise.LCSM.ColleagueDetails();
             * colleagueDetails.setRoleColleague(role);
             * colleagueDetails.setColleagueid(branchContext.getColleagueId());
             * stcolleaguedetails[i++] = colleagueDetails; }
             * bapiHeader.setStcolleaguedetails(stcolleaguedetails); }
             *
             *
             * com.lloydstsb.www.Schema.Enterprise.LCSM.TransactionDetails[]
             * transactionDetailsArray = new
             * com.lloydstsb.www.Schema.Enterprise.LCSM.TransactionDetails[1];
             * com.lloydstsb.www.Schema.Enterprise.LCSM.TransactionDetails
             * transactionDetails = new
             * com.lloydstsb.www.Schema.Enterprise.LCSM.TransactionDetails();
             * transactionDetails.setInputofcflgstatuscd(BigInteger.ZERO);
             * transactionDetails.setInputofcstatuscd(BigInteger.ZERO);
             * transactionDetails.setOverridedtlscd(BigInteger.ONE);
             * transactionDetails.setSkllvlacqdcd(BigInteger.ZERO);
             * transactionDetails.setHostapplicationid("Digital Branch");
             * transactionDetails.setExtsysid(94); transactionDetailsArray[0] =
             * transactionDetails;
             * bapiHeader.setSttransactiondetails(transactionDetailsArray);
             *
             * }else{ bapiHeader.setChanid(userContext.getChannelId()); }
             */

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
