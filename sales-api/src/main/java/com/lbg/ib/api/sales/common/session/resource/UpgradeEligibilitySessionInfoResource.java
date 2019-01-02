package com.lbg.ib.api.sales.common.session.resource;

import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.UpgradeEligibilitySessionInfo;
import com.lbg.ib.api.sales.product.service.UpgradeEligibilityService;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;

/**
 * Created by 8601769 on 06/03/2018. Resource controller for getting session information during
 * Upgrade
 */
@Path("/session/upgradeSessionInfo")
public class UpgradeEligibilitySessionInfoResource extends Base {

    @Autowired
    private SessionManagementDAO session;
    @Autowired
    private UpgradeEligibilityService service;
    @Autowired
    private GalaxyErrorCodeResolver errorResolver;
    @Autowired
    private ApiServiceProperties apiServiceProperties;
    @Autowired
    private ConfigurationDAO configuration;

    @GET
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response getUpgradeEligibilitySessionInfo() throws Exception {
        logger.traceLog(this.getClass(), "Getting the session info during Upgrade");
        UpgradeEligibilitySessionInfo upgradeEligibilitySessionInfo = new UpgradeEligibilitySessionInfo();
        //Setting party details
        if (session.getAllPartyDetailsSessionInfo() != null) {
            String primaryPartyEmail = session.getUserInfo().getPrimaryInvolvedParty().getEmail();
            logger.traceLog(this.getClass(), "Primary party email is: " + primaryPartyEmail);
            List<PartyDetails> allPartyDetails = new ArrayList<PartyDetails> (session.getAllPartyDetailsSessionInfo().values());

            for (PartyDetails partyDetails : allPartyDetails) {
                if(partyDetails.isPrimaryParty()) {
                    partyDetails.setEmail(primaryPartyEmail);
                }
            }
            upgradeEligibilitySessionInfo.setPartyDetails(allPartyDetails);
        }
        //Setting environment
        Map<String, Object> configurationItems = apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES);
        if ((configurationItems != null) && (configurationItems.get(CommonConstant.ENVIRONMENT_NAME) != null)) {
            upgradeEligibilitySessionInfo.setEnvironmentName((String) configurationItems.get(CommonConstant.ENVIRONMENT_NAME));
        }
        //Setting account information
        upgradeEligibilitySessionInfo.setSelectedAccount(service.getSelectedAccount());
        BranchContext context = session.getBranchContext();
        if (null != context) {
            upgradeEligibilitySessionInfo.setColleagueId(context.getColleagueId());
            upgradeEligibilitySessionInfo.setDomain(context.getDomain());
            upgradeEligibilitySessionInfo.setOriginatingSortCode(context.getOriginatingSortCode());
        }
        Arrangement arrangement = (Arrangement) session.getUserInfo();
        String brandName = configuration.getConfigurationStringValue(BRAND, session.getUserContext().getChannelId());
        Double totalOverDraftLimit = 0D;
        if (null != arrangement.getAccounts()) {
            for (Account account : arrangement.getAccounts()) {
                if (("C").equalsIgnoreCase(account.getProductType())
                        && account.getManufacturingLegalEntity().equalsIgnoreCase(brandName)
                        && account.getOverdraftLimit() != null) {
                    totalOverDraftLimit += account.getOverdraftLimit();
                }
            }
        }
        upgradeEligibilitySessionInfo.setTotalOverdraftLimit(totalOverDraftLimit);
        //AOB-1883 Getting the max overdraft limit from Q250 response which is further stored in the session.
        upgradeEligibilitySessionInfo.setMaxOverDraftLimit(session.getMaxOverDraftLimit());
        logger.traceLog(this.getClass(), "Completed getting the session info during Upgrade");
        if ((upgradeEligibilitySessionInfo.getPartyDetails() == null) || (upgradeEligibilitySessionInfo.getSelectedAccount() == null) || (upgradeEligibilitySessionInfo.getEnvironmentName() == null)) {
            return respond(Status.OK, new ResponseError(ResponseErrorConstants.DETAILS_NOT_FOUND_IN_SESSION, "Details not found in session"));
        }
        return respond(Status.OK, upgradeEligibilitySessionInfo);
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}