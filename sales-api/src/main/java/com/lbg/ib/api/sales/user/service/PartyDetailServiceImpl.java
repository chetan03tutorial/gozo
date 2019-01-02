/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.user.service;

import static com.lbg.ib.api.sales.user.mapper.PartyDetailMessageMapper.buildRetrieveMandatePartyRequest;
import static com.lbg.ib.api.sales.user.mapper.PartyDetailMessageMapper.mapMandatePartyResponse;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.validator.business.user.UserInfoValidator;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.InvolvedPartyAuthorisation;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.InvolvedPartyAuthorisationServiceLocator;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.RetrieveMandatePartyResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lloydstsb.ea.context.ClientContext;

@Component
public class PartyDetailServiceImpl extends SoaAbstractService implements PartyDetailService {

    @Autowired
    @Qualifier("involvedPartyAuthorisationServiceLocatorFS")
    private InvolvedPartyAuthorisationServiceLocator serviceLocator;

    @Autowired
    @Qualifier("involvedPartyAuthorisationServiceFS")
    private InvolvedPartyAuthorisation involvedPartyAuthorisation;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private CommonUtils commonUtils;
    /**
     * Object of Galaxy Error Code.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private LoggerDAO logger;

    public static final String EXTERNAL_SERVICE_UNAVAILABLE = "600140";
    public static final String EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE = "Remote Connection | Error while invoking external service";

    private static final String RETRIEVE_MANDATE_PARTY_METHOD = "retrieveMandateParty";

    @TraceLog
    public PartyDetails retrievePartyDetails() {
        UserContext context = session.getUserContext();
        String ocisId = context.getOcisId();
        String internalUserId = context.getAuthorId();
        UserInfoValidator.validateOcisId(ocisId);
        ClientContext clientContext = context.toClientContext();
        boolean isBranch = commonUtils.isBranchContext();
        if (!clientContext.getPartyId().equalsIgnoreCase(internalUserId)) {
            clientContext.setHost("T");
        }
        clientContext.setUserId("AAGATEWAY");
        setDataHandler(new QName(serviceLocator.getInvolvedPartyAuthorisationSOAPPortWSDDPortName()),
                serviceLocator.getHandlerRegistry());
        setSoapHeader(clientContext, RETRIEVE_MANDATE_PARTY_METHOD, serviceLocator.getServiceName().getNamespaceURI());
        RetrieveMandatePartyResponse daoResponse = null;
        try {
            daoResponse = involvedPartyAuthorisation.retrieveMandateParty(buildRetrieveMandatePartyRequest(internalUserId, ocisId));
        } catch (Exception ex) {
            logger.logException(PartyDetailServiceImpl.class, ex);
            throw new ServiceException(
                    new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE));
        }
        PartyDetails party = mapMandatePartyResponse(daoResponse, isBranch);
        return party;
    }

    @Override
    public Class<InvolvedPartyAuthorisation> getPort() {
        return InvolvedPartyAuthorisation.class;
    }

}
