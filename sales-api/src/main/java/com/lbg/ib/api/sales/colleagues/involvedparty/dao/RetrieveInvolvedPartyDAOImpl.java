/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.colleagues.involvedparty.dao;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.colleagues.involvedparty.dto.RetrieveInvolvedPartyResponseDTO;
import com.lbg.ib.api.sales.common.constant.ServiceActionHeaderConstants;
import com.lbg.ib.api.sales.common.util.SOAPMCAHeaderUtility;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.DAOBase;
import com.lloydstsb.ea.dao.header.BAPISOAPHeader;
import com.lloydstsb.ea.dao.header.ContextBAPIHeader;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.ea.lcsm.RequestHeader;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagement;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsRequest;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsResponse;

@Component
public class RetrieveInvolvedPartyDAOImpl extends DAOBase implements RetrieveInvolvedPartyDAO {
    private static final String ROLE_SEPARATOR = "\\|";
    private static final String AAGATEWAY = null;
    private String serviceName;
    private InvolvedPartyManagement involvedPartyManagement;
    private DAOExceptionHandler exceptionHandler;
    private SOAPMCAHeaderUtility soapmcaHeaderUtility;
    private SessionManagementDAO session;
    LoggerDAO logger;

    public RetrieveInvolvedPartyDAOImpl() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public RetrieveInvolvedPartyDAOImpl(SessionManagementDAO session, SOAPMCAHeaderUtility soapmcaHeaderUtility,
            DAOExceptionHandler exceptionHandler, LoggerDAO logger, InvolvedPartyManagement involvedPartyManagement)
            throws ServiceException, MalformedURLException {
        this.session = session;
        this.soapmcaHeaderUtility = soapmcaHeaderUtility;
        this.exceptionHandler = exceptionHandler;
        this.logger = logger;
        this.involvedPartyManagement = involvedPartyManagement;
        this.serviceName = "retrieveInvolvedPartyRoleDetails";

    }

    /**
     * Creates request for retrieveInvolvedPartyRoles
     * @return
     */

    private void mapRequestHeaderToRequest(RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest) {
        logger.traceLog(this.getClass(), "mapRequestHeaderToRequest.");
        HeaderData retrieveInvolvedHeaderData = soapmcaHeaderUtility.prepareHeaderData(this.serviceName,
                ServiceActionHeaderConstants.RETRIEVE_INVOLVED_PARTY_ROLES);
        BAPISOAPHeader bapiHeader = new BAPISOAPHeader();
        ContextBAPIHeader contextBAPIHeader = new ContextBAPIHeader();
        contextBAPIHeader.setUserIdAuthor(AAGATEWAY);
        bapiHeader.setContextBAPIHeader(contextBAPIHeader);
        retrieveInvolvedHeaderData.setHeader(bapiHeader);
        addHeaderData(session.getUserContext().toClientContext(), retrieveInvolvedHeaderData);
        involvedPartyRoleDetailsRequest.setRequestHeader(addHeaderDetails());
    }

    public void addHeaderData(ClientContext clientContext, HeaderData retrieveInvolvedHeaderData) {
        super.addBAPIHeaderDataInformation(clientContext, retrieveInvolvedHeaderData);
    }

    public RequestHeader addHeaderDetails() {
        return getHeader();
    }

    /**
     * Call adam services retrieveRolesForInvolvedParty to fetch the roles for involved party.
     * Required for colleague authorization Possible values are: 1-Colleague 2-Teller 4-Manager
     * retrieveRolesForInvolvedParty will be called to retrieve colleague role for branch colleagues
     * only. Teller role will be assigned to telephony colleague by default @throws
     */
    public DAOResponse<RetrieveInvolvedPartyResponseDTO> retrieveRolesForInvolvedParty(
            RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest) {

        logger.traceLog(this.getClass(), "After invocation of  retrieveRolesForInvolvedParty.");

        mapRequestHeaderToRequest(involvedPartyRoleDetailsRequest);
        RetrieveInvolvedPartyRoleDetailsResponse involvedPartyRoleDetailsResponse = null;
        try {
            logger.traceLog(this.getClass(), "Invocation of  retrieveRolesForInvolvedParty.");
            involvedPartyRoleDetailsResponse = involvedPartyManagement
                    .retrieveInvolvedPartyRoleDetails(involvedPartyRoleDetailsRequest);

            RetrieveInvolvedPartyResponseDTO involvedPartyResponseDTO = mapResponseToDTO(
                    involvedPartyRoleDetailsResponse);
            if (involvedPartyResponseDTO != null) {
                logger.traceLog(this.getClass(),
                        "Invocation of  retrieveRolesForInvolvedParty." + involvedPartyResponseDTO.toString());
            }

            logger.traceLog(this.getClass(), "Returning the Result.");
            return withResult(involvedPartyResponseDTO);
        } catch (Exception ex) {
            if (null != involvedPartyRoleDetailsResponse) {
                ResultCondition condition = involvedPartyRoleDetailsResponse.getResponseHeader().getResultCondition();
                logger.logError(condition == null ? null : condition.getReasonCode().toString(),
                        condition == null ? null : condition.getReasonText(), this.getClass());
            }
            DAOError daoError = exceptionHandler.handleException(ex, this.getClass(), "retrieveRolesForInvolvedParty",
                    involvedPartyRoleDetailsRequest);
            return withError(daoError);
        }
    }

    public RetrieveInvolvedPartyResponseDTO mapResponseToDTO(RetrieveInvolvedPartyRoleDetailsResponse response) {
        InvolvedPartyRole role = response.getRoleDetails();
        String value = role.getType().getValue();
        String name = role.getType().getName();
        Set<String> roles = StringUtils.isEmpty(name) ? null
                : new HashSet<String>(Arrays.asList(name.split(ROLE_SEPARATOR)));
        ResultCondition condition = response.getResponseHeader().getResultCondition();
        return new RetrieveInvolvedPartyResponseDTO(
                condition == null ? null
                        : String.valueOf(condition.getReasonCode() == null ? "" : condition.getReasonCode().intValue()),
                condition == null ? null : condition.getReasonText(), value, roles);
    }
}
