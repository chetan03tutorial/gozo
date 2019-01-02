package com.lbg.ib.api.sales.docmanager.util;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.enums.DAOConditionType;
import com.lloydstsb.ea.dao.header.*;
import com.lloydstsb.ea.lcsm.BAPIHeader;
import com.lloydstsb.ea.lcsm.BapiInformation;
import com.lloydstsb.ea.lcsm.HostInformation;
import com.lloydstsb.ea.lcsm.OperationalVariables;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import com.lloydstsb.ea.webservices.handler.WSHeaderDataHandler;
import com.lloydstsb.schema.ea.infrastructure.UNPMechanismTypeEnum;
import com.lloydstsb.www.DocumentationManagerServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Header utility class for SOA DocumentManagerService
 * @author 8903735
 *
 */
@Component
public class DocumentManagerHeaderUtility {
    @Autowired
    private LoggerDAO logger;

    private static final String  CONTACT_POINT    = "ContactPoint";

    private static final String  SERVICE_REQUEST  = "ServiceRequest";

    private static final String  FALSE            = "false";

    private static final String  BAPI_INFORMATION = "bapiInformation";

    private static String        SERVICE_NAME     = "DocumentManagerService";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility gboHeaderUtility;

    @Autowired
    private MCAHeaderUtility mcaHeaderUtility;

    /**
     *
     * @param serviceLocator
     * @return
     */
    public HandlerRegistry setupAndGetDataHandler(DocumentationManagerServiceLocator serviceLocator,
            String serviceAction) {
        logger.traceLog(this.getClass(), "Setup the data handler for the request.");
        HandlerRegistry handlerRegistry = serviceLocator.getHandlerRegistry();
        // following line will add the default header in the application context
        ClientContext clientContext = session.getUserContext().toClientContext();

        List<SOAPHeader> headerData = null;
        ContactPointHeader contactPoint = null;
        ServiceRequestHeader serviceRequest = null;
        BapiInformation bapiInformation = null;

        if (null != session.getBranchContext()) {
            headerData = mcaHeaderUtility.prepareSoapHeader(serviceAction, SERVICE_NAME);
        } else {
            headerData = gboHeaderUtility.prepareSoapHeader(serviceAction, SERVICE_NAME);
        }
        ApplicationRequestContext.set(DAOConditionType.CLIENT_CTX_SOAP_HEADER.code(), clientContext);
        HeaderData headerDataNew = new HeaderData();

        for (SOAPHeader soapHeader : headerData) {
            if (CONTACT_POINT.equalsIgnoreCase(soapHeader.getName())) {
                contactPoint = contactPointHeader((ContactPoint) soapHeader.getValue());
            }
            if (SERVICE_REQUEST.equalsIgnoreCase(soapHeader.getName())) {
                try {
                    serviceRequest = serviceRequestHeader((ServiceRequest) soapHeader.getValue(), serviceLocator);
                } catch (URISyntaxException e) {
                    logger.logException(this.getClass(), e);
                }
            }
            if (BAPI_INFORMATION.equalsIgnoreCase(soapHeader.getName())) {
                bapiInformation = bapiInfomation(
                        (com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation) soapHeader
                                .getValue());

            }
        }
        headerDataNew.setContactPointHeader(contactPoint);
        headerDataNew.setServiceRequestHeader(serviceRequest);
        headerDataNew.setSecurityHeader(securityHeader());
        //        ProcessHeader processHeader = new ProcessHeader();
        ApplicationRequestContext.set(DAOConditionType.HEADER_DATA_SOAP_HEADER.code(), headerDataNew);
        ApplicationRequestContext.set(DAOConditionType.BAPI_INFO_SOAP_HEADER.code(), bapiInformation);
        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        handlerList.add(new HandlerInfo(WSHeaderDataHandler.class, null, null));
        handlerRegistry.setHandlerChain(new QName(serviceLocator.getDocumentationManagerSOAPPortWSDDPortName()),
                handlerList);
        logger.traceLog(this.getClass(), "Data handler setup is completed.");
        return handlerRegistry;
    }


    /**
     * @param aServiceName
     *            -String
     * @param anAction
     *            -String
     * @return Prepares headerdata for SOAPHeader
     */
    public HeaderData prepareHeaderData(String aServiceName, String anAction) {
        HeaderData headerData = new HeaderData();
        headerData.setServiceName(aServiceName);
        headerData.setAction(anAction);
        return headerData;
    }

    /**
     *
     * @param contactPointHeader
     * @return
     */
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

    /**
     *
     * @param serviceRequestHeader
     * @param serviceLocator
     * @return
     * @throws URISyntaxException
     */
    private ServiceRequestHeader serviceRequestHeader(ServiceRequest serviceRequestHeader,
            DocumentationManagerServiceLocator serviceLocator) throws URISyntaxException {
        ServiceRequestHeader serviceActualRequest = new ServiceRequestHeader();
        serviceActualRequest.setMustReturn(FALSE);
        if (null != serviceRequestHeader) {
            serviceActualRequest.setFrom(serviceRequestHeader.getFrom().toString());
        }
        serviceActualRequest.setServiceName(serviceLocator.getServiceName().toString());
        serviceActualRequest.setAction(serviceRequestHeader.getAction());
        return serviceActualRequest;
    }

    // check if it can be moved to configurations
    private SecurityHeader securityHeader() {
        UsernameToken userNameToken = new UsernameToken();
        userNameToken.setUsername("CT067484");
        userNameToken.setUserType("010");
        userNameToken.setUNPMechanismType(UNPMechanismTypeEnum.IB.getValue());
        userNameToken.setId("LloydsTSBSecurityToken");

        SecurityHeader securityHeaderType = new SecurityHeader();
        securityHeaderType.setMustReturn(FALSE);
        securityHeaderType.setUsernameToken(userNameToken);
        return securityHeaderType;
    }

    /**
     *
     * @param hostInformtionHeader
     * @return
     */
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
