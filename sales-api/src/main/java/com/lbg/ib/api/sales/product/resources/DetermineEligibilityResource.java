/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.common.validation.ValidationException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.rules.DetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.rules.PcaDetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.service.DetermineEligibilityService;
import com.lbg.ib.api.sales.product.service.PcaDetermineEligibilityService;
import com.lbg.ib.api.sales.utils.CommonUtils;

@Path("/products/features/uid/eligibleCustomerInstructions")
public class DetermineEligibilityResource {

    private DetermineEligibilityService          service;
    private PcaDetermineEligibilityService       authService;
    private RequestBodyResolver                  resolver;
    private FieldValidator                       fieldValidator;
    private DetermineEligibilityRuleValidator    ruleValidator;
    private GalaxyErrorCodeResolver              errorResolver;
    private CommonUtils                          commonUtils;
    private SessionManagementDAO                 session;
    private PcaDetermineEligibilityRuleValidator authRuleValidator;
    private LoggerDAO                            logger;

    public DetermineEligibilityResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public DetermineEligibilityResource(DetermineEligibilityService service, RequestBodyResolver resolver,
            FieldValidator fieldValidator, GalaxyErrorCodeResolver errorResolver,
            DetermineEligibilityRuleValidator ruleValidator, CommonUtils commonUtils, SessionManagementDAO session,
            PcaDetermineEligibilityRuleValidator authRuleValidator, PcaDetermineEligibilityService authService,
            LoggerDAO logger) {
        this.service = service;
        this.resolver = resolver;
        this.fieldValidator = fieldValidator;
        this.errorResolver = errorResolver;
        this.ruleValidator = ruleValidator;
        this.commonUtils = commonUtils;
        this.session = session;
        this.authRuleValidator = authRuleValidator;
        this.authService = authService;
        this.logger = logger;
    }

    /**
     * This method is used to determine Eligibility for given product.
     *
     * @param requestBody
     * @return
     * @throws ServiceException
     * @throws InvalidFormatException
     */
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    // Do not add Trace Logging as to avoid Printing of DOB
    public Response determineEligibility(String requestBody) throws InvalidFormatException {
        if (commonUtils.isAuth(session)) {
            PcaDetermineEligibilityRequest pcaDetermineEligibilityRequest = resolver.resolve(requestBody,
                    PcaDetermineEligibilityRequest.class);
            logger.traceLog(this.getClass(),
                    "Inside the DECI Resource. Input Request is : " + pcaDetermineEligibilityRequest);
            validateAuthRequest(pcaDetermineEligibilityRequest);
            DetermineEligibilityResponse response = authService.determineAuthEligiblity(pcaDetermineEligibilityRequest);
            if (response != null) {
                logger.traceLog(this.getClass(), "Inside the DECI Resource. Output Response is : " + response);
                return respond(Status.OK, response);
            }
        } else {

            DetermineEligibilityRequest determineEligibilityRequest = resolver.resolve(requestBody,
                    DetermineEligibilityRequest.class);
            logger.traceLog(this.getClass(),
                    "Inside the DECI Resource. Input Request is : " + determineEligibilityRequest);
            validate(determineEligibilityRequest);
            DetermineEligibilityResponse response = service.determineEligiblity(determineEligibilityRequest);
            if (response != null) {
                logger.traceLog(this.getClass(), "Inside the DECI Resource. Output Response is : " + response);
                return respond(Status.OK, response);
            }
        }
        logger.traceLog(this.getClass(), "Inside the DECI Resource. Output Response has error. ");
        return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    /*
     * This methods validate the request object for DetermineEligibility.
     */
    private void validateAuthRequest(PcaDetermineEligibilityRequest request) throws InvalidFormatException {
        if (request.getArrangementType() != null) {
            ValidationError validationError = authRuleValidator.validateRules(request, true);
            if (validationError != null) {
                throw new InvalidFormatException(validationError.getMessage());
            }
        } else {
            throw new InvalidFormatException(
                    "The account type in the request url is not in the accepted set of values");
        }
    }

    /*
     * This methods validate the request object for DetermineEligibility.
     */
    private void validate(DetermineEligibilityRequest request) throws InvalidFormatException {
        if (request.getArrangementType() != null) {
            ValidationError validationError = null;
            try {
                fieldValidator.validateInstanceFields(request, request.getArrangementType());
            } catch (ValidationException e) {
                logger.logException(this.getClass(), e);
                validationError = e.getValidationError();
            }
            if (validationError != null) {
                throw new InvalidFormatException(validationError.getMessage());
            }
            validationError = ruleValidator.validateRules(request.getPrimaryInvolvedParty());
            if (validationError != null) {
                throw new InvalidFormatException(validationError.getMessage());
            }
        } else {
            throw new InvalidFormatException(
                    "The account type in the request url is not in the accepted set of values");
        }
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
