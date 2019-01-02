/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.kyc;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075Req;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075Resp;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075_EnqKYCDetails_PortType;
import com.lbg.ib.api.sales.soapapis.ocis.f075.F075_EnqKYCDetails_ServiceLocator;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.enums.DAOConditionType;
import com.lloydstsb.ea.dao.header.ContactPointHeader;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.dao.header.SecurityHeader;
import com.lloydstsb.ea.dao.header.ServiceRequestHeader;
import com.lloydstsb.ea.dao.header.UsernameToken;
import com.lloydstsb.ea.lcsm.BAPIHeader;
import com.lloydstsb.ea.lcsm.BapiInformation;
import com.lloydstsb.ea.lcsm.HostInformation;
import com.lloydstsb.ea.lcsm.OperationalVariables;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import com.lloydstsb.ea.webservices.handler.WSHeaderDataHandler;
//import com.lbg.ib.api.sales.common.constants.ResponseErrorConstants;
//import com.lbg.ib.api.sales.common.logging.annotations.TraceLog;
//import com.lbg.ib.api.sales.dao.util.GBOHeaderUtility;
//import com.lbg.ib.api.sales.dao.util.MCAHeaderUtility;

@Component
public class FO75KYCReviewDAOImpl implements FO75KYCReviewDAO {

    @Autowired
    private F075_EnqKYCDetails_PortType       f075KycDetails;

    @Autowired
    private LoggerDAO                         logger;

    @Autowired
    private SessionManagementDAO              session;

    @Autowired
    private GBOHeaderUtility                  headerUtility;

    @Autowired
    private MCAHeaderUtility                  mcaheaderUtility;

    @Autowired
    private F075_EnqKYCDetails_ServiceLocator serviceLocator;

    public static final String                CONTACT_POINT_ID               = "CONTACT_POINT_ID";

    public static final String                CANNOT_CONNECT_TO_REMOTE_POINT = "cannotConnectToRemote";

    public static final String                ASSERTION_TOKEN_DECRYPTION     = "ASSERTION_TOKEN_DECRYPTION";

    public static final String                TRIGGER                        = "TRIGGER";

    public static final String                CONTACT_POINT                  = "ContactPoint";

    public static final String                SERVICE_REQUEST                = "ServiceRequest";

    public static final String                BAPI_INFORMATION               = "bapiInformation";

    public static final String                FALSE                          = "false";

    public static final short                 EXT_SYS_ID                     = 19;

    public static final Short                 PARTY_EXT_SYS_ID               = 2;

    public static final int                   MAX_REPEAT_GROUP_QY            = 15;

    private ContactPointHeader                contactPoint                   = null;

    private ServiceRequestHeader              serviceRequest                 = null;

    private BapiInformation                   bapiInformation                = null;

    @TraceLog
    public DAOResponse<String> nextReviewDate(String partyId) {
        HandlerRegistry handlerRegistry = serviceLocator.getHandlerRegistry();
        setupDataHandler(handlerRegistry);
        try {
            F075Resp f075Resp = f075KycDetails.f075(createRequest(partyId));

            if (null != f075Resp) {
                logger.logError(f075Resp.getF075Result().getResultCondition().toString(),
                        " +++++++++++++++++ Inside f075 resp", this.getClass());
                if (null != f075Resp.getF075Result().getResultCondition().getReasonCode()) {
                    logger.logError(f075Resp.getF075Result().getResultCondition().getReasonCode().toString(),
                            f075Resp.getF075Result().getResultCondition().getReasonText(), this.getClass());
                    return withError(new DAOResponse.DAOError(
                            f075Resp.getF075Result().getResultCondition().getReasonCode().toString(),
                            f075Resp.getF075Result().getResultCondition().getReasonText()));
                } else if (null != f075Resp.getKYCControlData()) {
                    logger.logError(f075Resp.getKYCControlData().getDataNxtReviewDt(),
                            "++++++++++++++++ Next review date in Response of party Id" + partyId, this.getClass());
                    return withResult(f075Resp.getKYCControlData().getDataNxtReviewDt());
                }
            }
        } catch (RemoteException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError("75004", "URISyntaxException while creating SOAP request"));
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError("75004", "Exception while creating SOAP request"));
        }
        return null;
    }

    private F075Req createRequest(String partyId) {
        F075Req f075Req = new F075Req();
        f075Req.setPartyId(Long.parseLong(partyId));
        f075Req.setExtSysId(EXT_SYS_ID);
        f075Req.setMaxRepeatGroupQy(MAX_REPEAT_GROUP_QY);
        f075Req.setPartyExtSysId(PARTY_EXT_SYS_ID);
        return f075Req;
    }

    private void setupDataHandler(HandlerRegistry handlerRegistry) {
        // following line will add the default header in the application context
        ClientContext clientContext = session.getUserContext().toClientContext();
        List<SOAPHeader> headerData = null;

        if (null != session.getBranchContext()) {
            headerData = mcaheaderUtility.prepareSoapHeader(serviceLocator.getServiceName().getNamespaceURI(), "f075");
        } else {
            headerData = headerUtility.prepareSoapHeader(serviceLocator.getServiceName().getNamespaceURI(), "f075");
        }
        ApplicationRequestContext.set(DAOConditionType.CLIENT_CTX_SOAP_HEADER.code(), clientContext);
        HeaderData headerDataNew = new HeaderData();

        for (SOAPHeader soap : headerData) {
            if (CONTACT_POINT.equalsIgnoreCase(soap.getName())) {
                contactPoint = contactPointHeader((ContactPoint) soap.getValue());
            }
            if (SERVICE_REQUEST.equalsIgnoreCase(soap.getName())) {
                try {
                    serviceRequest = serviceRequestHeader((ServiceRequest) soap.getValue());
                } catch (URISyntaxException e) {
                    logger.logException(this.getClass(), e);
                }
            }
            if (BAPI_INFORMATION.equalsIgnoreCase(soap.getName())) {
                bapiInformation = bapiInfomation(
                        (com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation) soap
                                .getValue());

            }
        }
        headerDataNew.setContactPointHeader(contactPoint);
        headerDataNew.setServiceRequestHeader(serviceRequest);
        headerDataNew.setSecurityHeader(securityHeader());
        ApplicationRequestContext.set(DAOConditionType.HEADER_DATA_SOAP_HEADER.code(), headerDataNew);
        ApplicationRequestContext.set(DAOConditionType.BAPI_INFO_SOAP_HEADER.code(), bapiInformation);
        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        handlerList.add(new HandlerInfo(WSHeaderDataHandler.class, null, null));
        handlerRegistry.setHandlerChain(new QName(serviceLocator.getF075_EnqKYCDetailsWSDDPortName()), handlerList);
    }

    private ContactPointHeader contactPointHeader(ContactPoint contactPointHeader) {
        ContactPointHeader contactPointActualHeader = new ContactPointHeader();
        contactPointActualHeader.setApplicationId(contactPointHeader.getApplicationId());
        contactPointActualHeader.setContactPointId(contactPointHeader.getContactPointId());
        contactPointActualHeader.setContactPointType(contactPointHeader.getContactPointType());
        contactPointActualHeader.setInitialOriginatorType(contactPointHeader.getInitialOriginatorType());
        contactPointActualHeader.setOperatorType(contactPointHeader.getOperatorType().getValue());
        contactPointActualHeader.setMustReturn(FALSE);
        return contactPointActualHeader;
    }

    private ServiceRequestHeader serviceRequestHeader(ServiceRequest serviceRequestHeader) throws URISyntaxException {
        ServiceRequestHeader serviceActualRequest = new ServiceRequestHeader();
        serviceActualRequest.setMustReturn(FALSE);
        if (null != serviceRequestHeader) {
            serviceActualRequest.setFrom(serviceRequestHeader.getFrom().toString());
        }
        serviceActualRequest.setServiceName(serviceRequestHeader.getServiceName().toString());
        serviceActualRequest.setAction(serviceRequestHeader.getAction());
        return serviceActualRequest;
    }

    private SecurityHeader securityHeader() {
        UsernameToken userNameToken = new UsernameToken();
        userNameToken.setUsername("AAGATEWAY");
        userNameToken.setUserType("013");
        userNameToken.setUNPMechanismType("IB");
        userNameToken.setId("LloydsTSBSecurityToken");

        SecurityHeader securityHeaderType = new SecurityHeader();
        securityHeaderType.setMustReturn(FALSE);
        securityHeaderType.setUsernameToken(userNameToken);
        return securityHeaderType;
    }

    private HostInformation hostInformation(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation hostInformtionHeader) {
        HostInformation hostInformation = new HostInformation();
        hostInformation.setHost(hostInformtionHeader.getHost());
        hostInformation.setOcisid(hostInformtionHeader.getOcisid());
        hostInformation.setPartyid(hostInformtionHeader.getPartyid());
        return hostInformation;
    }

    private OperationalVariables operationalVariables(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables operationalVariablesHeader) {
        OperationalVariables operationalVariables = new OperationalVariables();
        operationalVariables.setBBatchRetry(operationalVariablesHeader.isBBatchRetry());
        operationalVariables.setBForceHostCall(operationalVariablesHeader.isBForceHostCall());
        operationalVariables.setBPopulateCache(operationalVariablesHeader.isBPopulateCache());
        return operationalVariables;
    }

    private BapiInformation bapiInfomation(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation bapiInformationHeader) {
        BapiInformation actualBapiInformation = new BapiInformation();
        actualBapiInformation
                .setBAPIOperationalVariables(operationalVariables(bapiInformationHeader.getBAPIOperationalVariables()));
        actualBapiInformation.setBAPIHeader(bapiHeader(bapiInformationHeader.getBAPIHeader()));
        return actualBapiInformation;
    }

    private BAPIHeader bapiHeader(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader bapiHeaderRequest) {
        BAPIHeader bapiActualHeader = new BAPIHeader();
        bapiActualHeader.setChanid(bapiHeaderRequest.getChanid());
        bapiActualHeader.setChansecmode(bapiHeaderRequest.getChansecmode());
        bapiActualHeader.setSessionid(bapiHeaderRequest.getSessionid());
        bapiActualHeader.setIpAddressCaller(bapiHeaderRequest.getIpAddressCaller());
        bapiActualHeader.setUserAgent(bapiHeaderRequest.getUserAgent());
        bapiActualHeader.setAcceptLanguage(bapiHeaderRequest.getAcceptLanguage());
        bapiActualHeader.setInboxidClient(bapiHeaderRequest.getInboxidClient());
        bapiActualHeader.setUseridAuthor(bapiHeaderRequest.getUseridAuthor());
        bapiActualHeader.setStpartyObo(hostInformation(bapiHeaderRequest.getStpartyObo()));
        return bapiActualHeader;
    }
}
