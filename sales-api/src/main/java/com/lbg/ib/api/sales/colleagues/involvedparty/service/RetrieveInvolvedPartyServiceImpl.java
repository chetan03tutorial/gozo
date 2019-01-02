/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import static com.lbg.ib.api.sales.common.constant.Constants.CHANNEL_IDENTIFIER;
import static com.lbg.ib.api.sales.common.constant.Constants.MCA_DOMAIN;
import static com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants.CURRENT_ACCOUNTS;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.colleagues.involvedparty.dao.RetrieveInvolvedPartyDAO;
import com.lbg.ib.api.sales.colleagues.involvedparty.dto.RetrieveInvolvedPartyResponseDTO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.lcsm.common.RuleCondition;
import com.lloydstsb.lcsm.involvedparty.AccessToken;
import com.lloydstsb.lcsm.involvedparty.Individual;
import com.lloydstsb.lcsm.involvedparty.InvolvedParty;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsRequest;

/**
 * @author ssama1
 **/

@Component
public class RetrieveInvolvedPartyServiceImpl extends Base implements RetrieveInvolvedPartyService {

    private static final String      TRIGGER             = "TRIGGER";
    private static final String      RETURN_GROUPS       = "RETURN_GROUPS";
    private static final String      USER_PRINCIPAL_NAME = "USER_PRINCIPAL_NAME";
    private static final String      COLLEAGUE           = "COLLEAGUE";
    private static final String      SEPARATOR           = "\\";
    private static final String      BTRSAVINGS          = "BTRSAVINGS";
    private RetrieveInvolvedPartyDAO retrieveInvolvedPartyDAO;
    private SessionManagementDAO     session;
    private LoggerDAO                logger;
    private ConfigurationService     configurationService;
    @Autowired
    private GalaxyErrorCodeResolver  resolver;
    private static final Class       CLASS_NAME          = RetrieveInvolvedPartyServiceImpl.class;

    public RetrieveInvolvedPartyServiceImpl() {
        /* Default constructor */
    }

    @Autowired
    public RetrieveInvolvedPartyServiceImpl(RetrieveInvolvedPartyDAO retrieveInvolvedPartyDAO,
            ConfigurationService configurationService, SessionManagementDAO session, LoggerDAO logger) {
        this.retrieveInvolvedPartyDAO = retrieveInvolvedPartyDAO;
        this.configurationService = configurationService;
        this.session = session;
        this.logger = logger;
    }

    /**
     * @param colleagueId
     * @return
     */

    public String checkDomain(String colleagueId) {
        BranchContext context = session.getBranchContext();
        String domain = null;
        if (null != context) {
            domain = context.getDomain();
        }
        if (StringUtils.isEmpty(domain)) {
            domain = configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN);

        }

        return domain + SEPARATOR + colleagueId;
    }

    /**
     * @param fileID
     * @param retrieveInvolvedPartyRoleDetailsRequest
     */

    public void populatedADAMServiceRequest(String fileID,
            RetrieveInvolvedPartyRoleDetailsRequest retrieveInvolvedPartyRoleDetailsRequest) {

        RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setName(TRIGGER);
        ruleCondition.setResult(RETURN_GROUPS);
        AccessToken accessToken = new AccessToken();
        accessToken.setTokenDetails(USER_PRINCIPAL_NAME);
        accessToken.setTokenData(fileID);
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue(COLLEAGUE);
        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
        involvedPartyRole.setAccessToken(new AccessToken[] { accessToken });
        involvedPartyRole.setType(involvedPartyRoleType);
        InvolvedParty involvedParty = new Individual();
        involvedParty.setHasObjectConditions(new RuleCondition[] { ruleCondition });
        involvedParty.setInvolvedPartyRole(involvedPartyRole);
        retrieveInvolvedPartyRoleDetailsRequest.setInvolvedPartyDetails(involvedParty);

    }

    @TraceLog
    public boolean retrieveSnRRolesForInvolvedParty(BranchContext branchContext) throws ServiceException {
        Set<String> roles = null;
        RetrieveInvolvedPartyRoleDetailsRequest retrieveInvolvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        String colleagueId = branchContext.getColleagueId();
        logger.traceLog(this.getClass(), "Fetching the colleague Id : " + colleagueId);
        String colleagueIdWithDoamin = checkDomain(colleagueId);
        logger.traceLog(this.getClass(), "Fetching the colleague Id after domain : " + colleagueIdWithDoamin);

        populatedADAMServiceRequest(colleagueIdWithDoamin, retrieveInvolvedPartyRoleDetailsRequest);

        DAOResponse<RetrieveInvolvedPartyResponseDTO> involvedPartyResponse = retrieveInvolvedPartyDAO
                .retrieveRolesForInvolvedParty(retrieveInvolvedPartyRoleDetailsRequest);

        logger.traceLog(this.getClass(), "After invocation of  retrieveRolesForInvolvedParty.");

        RetrieveInvolvedPartyResponseDTO involvedPartyResponseDTO = involvedPartyResponse.getResult();
        DAOError error = involvedPartyResponse.getError();
        if (null != error) {
            logger.logError(error.getErrorCode(), error.getErrorMessage(), error.getClass());
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION));
        } else if (null != involvedPartyResponseDTO) {
            roles = involvedPartyResponseDTO.getRoles();
        }

        return validateSnRRoles(roles, branchContext);
    }

    @TraceLog
    private boolean validateSnRRoles(Set<String> groups, BranchContext context) {
        Map<String, Object> configuredWriteCancelRoles = configurationService.getConfigurationItems("SAVENRESUMEWRITE");
        Iterator it = groups.iterator();
        while (it.hasNext()) {
            logger.traceLog(this.getClass(), "Fetched UserRole:: " + it.next() + " :: ");
        }

        // Accredition Flag check and Corresponding Hard Coded Mappings.
        boolean isAuthorized = false;
        Map<String, Object> appConfig = configurationService
                .getConfigurationItems(BranchContextConstants.APPLICATION_PROPERTIES);
        String accreditionFlag = (String) appConfig.get(BranchContextConstants.ACCREDITION_FLAG);
        if (!(Boolean.valueOf(accreditionFlag))) {
            return true;
        }

        return validateRole(configuredWriteCancelRoles, groups, context);

    }

    @TraceLog
    public boolean validateRole(Map<String, Object> configuredAdamRoles, Set<String> fetchGroups,
            BranchContext context) {
        for (Entry<String, Object> configuredGroupAndRole : configuredAdamRoles.entrySet()) {
            String configuredGroup = (String) configuredGroupAndRole.getValue();
            if (fetchGroups.contains(configuredGroup)) {
                // colleague is authorized
                String colleagueRole = configuredGroupAndRole.getKey();
                context.setRoles(Arrays.asList(colleagueRole));
                return true;
            }
        }

        return false;
    }

    public ColleagueContext retrieveRolesForInvolvedParty() throws ServiceException {
        Set<String> roles = null;
        BranchContext branchContext = session.getBranchContext();
        String colleagueId = branchContext.getColleagueId();

        logger.traceLog(this.getClass(), "Fetching the colleague Id : " + colleagueId);

        colleagueId = checkDomain(colleagueId);

        logger.traceLog(this.getClass(), "Fetching the colleague Id after domain : " + colleagueId);

        RetrieveInvolvedPartyRoleDetailsRequest retrieveInvolvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        populatedADAMServiceRequest(colleagueId, retrieveInvolvedPartyRoleDetailsRequest);

        logger.traceLog(this.getClass(), "After Population of Adam Service Request.");

        DAOResponse<RetrieveInvolvedPartyResponseDTO> involvedPartyResponse = retrieveInvolvedPartyDAO
                .retrieveRolesForInvolvedParty(retrieveInvolvedPartyRoleDetailsRequest);

        logger.traceLog(this.getClass(), "After invocation of  retrieveRolesForInvolvedParty.");

        RetrieveInvolvedPartyResponseDTO involvedPartyResponseDTO = involvedPartyResponse.getResult();
        DAOError error = involvedPartyResponse.getError();
        if (null != error) {
            logger.logError(error.getErrorCode(), error.getErrorMessage(), error.getClass());
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION));
        } else if (null != involvedPartyResponseDTO) {
            roles = involvedPartyResponseDTO.getRoles();
        }

        return validateColleagueContext(roles, branchContext);
    }

    private ColleagueContext validateColleagueContext(Set<String> groups, BranchContext context) {
        logger.traceLog(this.getClass(), "Invocation of validateColleagueContext.");
        // find roles that are configured for existing journey
        Map<String, Object> adamEntitledGroups = configurationService.getConfigurationItems(
                context.getApplicationName() == null ? CURRENT_ACCOUNTS : context.getApplicationName());
        Iterator iter = groups.iterator();
        while (iter.hasNext()) {
            logger.traceLog(this.getClass(), "UserRole:: " + iter.next() + " :: ");
        }

        Iterator it = adamEntitledGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            logger.traceLog(this.getClass(), "Adam Service:: " + pair.getKey() + " :: " + pair.getValue());
        }
        boolean isAuthorized = false;
        Map<String, Object> appConfig = configurationService
                .getConfigurationItems(BranchContextConstants.APPLICATION_PROPERTIES);
        String accreditionFlag = (String) appConfig.get(BranchContextConstants.ACCREDITION_FLAG);
        if (!(Boolean.valueOf(accreditionFlag))) {
            logger.traceLog(this.getClass(), "inside Accredition value:: " + accreditionFlag);
            isAuthorized = true;
            context.setRoles(Arrays.asList(BTRSAVINGS));
            session.setBranchContext(context);
            return new ColleagueContext(context.getColleagueId(), isAuthorized);

        }

        for (Entry<String, Object> configuredGroupAndRole : adamEntitledGroups.entrySet()) {
            String configuredGroup = (String) configuredGroupAndRole.getValue();
            if (groups.contains(configuredGroup)) {
                // colleague is authorized
                isAuthorized = true;
                String colleagueRole = configuredGroupAndRole.getKey();
                context.setRoles(Arrays.asList(colleagueRole));
                session.setBranchContext(context);
                break;

            }
        }
        logger.traceLog(this.getClass(), "Accredition flag value in validateColleagueContext is:: " + isAuthorized);
        return new ColleagueContext(context.getColleagueId(), isAuthorized);

    }

    public void setResolver(GalaxyErrorCodeResolver resolver) {
        this.resolver = resolver;
    }

}