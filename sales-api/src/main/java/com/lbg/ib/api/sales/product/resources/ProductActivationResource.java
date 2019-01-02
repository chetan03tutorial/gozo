/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.filter.RoleValidator;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.activate.Activated;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sales.product.domain.activate.ArrangementId;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.rules.ActivationRuleValidator;
import com.lbg.ib.api.sales.product.service.ActivateProductService;

@Path("/product/arrangement/{arrangementId}")
public class ProductActivationResource {

    private ActivateProductService         service;
    private RequestBodyResolver            resolver;
    private FieldValidator                 validator;
    private GalaxyErrorCodeResolver        errorResolver;
    private ActivationRuleValidator        ruleValidator;
    private SessionManagementDAO           session;
    private LoggerDAO                      logger;
    private ResumeProductActivationService resumeService;
    private BranchContextService           branchContextService;

    public ProductActivationResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public ProductActivationResource(ActivateProductService service, RequestBodyResolver resolver,
            FieldValidator validator, GalaxyErrorCodeResolver errorResolver, ActivationRuleValidator ruleValidator,
            SessionManagementDAO session, LoggerDAO logger, ResumeProductActivationService resumeService,
            BranchContextService branchContextService) {
        this.service = service;
        this.resolver = resolver;
        this.validator = validator;
        this.errorResolver = errorResolver;
        this.ruleValidator = ruleValidator;
        this.session = session;
        this.logger = logger;
        this.resumeService = resumeService;
        this.branchContextService = branchContextService;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @RoleValidator
    // Warning do not add @TraceLog as we do not intend to print the Request as
    // the Request contains the User Name and Password.
    public Response activateProductArrangement(@PathParam("arrangementId") ArrangementId arrangementId,
            String requestBody) throws InvalidFormatException {

        Activation activation = resolver.resolve(requestBody, Activation.class);

        logger.traceLog(this.getClass(), "Inside the Activation Resource. Input Request is : " + activation);

        if (activation.isSnr() && activation.getCurrentColleagueId() != null) {
            BranchContext branchContext = new BranchContext();
            branchContext.setDomain(activation.getCurrentDomain());
            branchContext.setColleagueId(activation.getCurrentColleagueId());
            branchContext.setOriginatingSortCode(activation.getCurrentSortCode());
            branchContextService.checkSnRRole(branchContext);
        }

        if (activation.getCustomerPendingDetails() != null
                && !CollectionUtils.isEmpty(activation.getCustomerPendingDetails().getCustomerPendingDetails())) {
            resumeService
                    .resumeProductActivation(activation.getCustomerPendingDetails().getCustomerPendingDetails().get(0));
        }

        // We first check if request is having Colleague Id is coming in the
        // Request. Then checks if Branch context is present and then invoke
        // Service if not present
        if (session.getBranchContext() == null && activation.getColleagueId() != null && activation.getDomain() != null
                && activation.getOriginatingSortCode() != null) {
            BranchContext branchContext = new BranchContext();
            branchContext.setDomain(activation.getDomain());
            branchContext.setColleagueId(activation.getColleagueId());
            branchContext.setOriginatingSortCode(activation.getOriginatingSortCode());
            branchContextService.setBranchContextToSession(branchContext);
        }
        // End of Branch Context Code

        ArrangeToActivateParameters params = session.getArrangeToActivateParameters();

        ValidationError error = validateRequest(arrangementId, activation, params);
        if (error != null) {
            throw new InvalidFormatException(error.getMessage());
        }

        if (params == null) {
            logger.traceLog(this.getClass(), "Session params not available in Activate Session. Check cookie.");
            return respond(Status.OK, errorResolver.customResolve(ResponseErrorConstants.SERVICE_UNAVAILABLE,
                    "Session params not available in Activate Session. Check cookie."));

        }

        Activated activated = service.activateProduct(arrangementId, activation);
        if (activated != null) {
            logger.traceLog(this.getClass(), "Inside the Activation Resource. Output Response is : " + activated);
            return respond(Status.OK, activated);
        }

        logger.traceLog(this.getClass(), "Inside the Activation Resource. Output Response has error. ");
        return respond(Status.OK, errorResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    private ValidationError validateRequest(ArrangementId arrangementId, Activation activation,
            ArrangeToActivateParameters params) {

        ValidationError error = validator.validateInstanceFields(arrangementId);
        if (error != null) {
            return error;
        }

        if (activation.getCustomerDocuments() == null || activation.getCustomerDocuments().size() == 0) {
            if (session.getAddPartyContext() != null) {
                // validate activation request for addparty against userInfo

                error = ruleValidator.validateAccountDetailsForAddParty(activation, session.getUserInfo());
                if (error != null) {
                    return error;
                }
            }

            error = validator.validateInstanceFields(activation);
            if (error != null) {
                return error;
            }
            error = ruleValidator.validateRules(activation, params);
            if (error != null) {
                return error;
            }

        }

        return null;
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
