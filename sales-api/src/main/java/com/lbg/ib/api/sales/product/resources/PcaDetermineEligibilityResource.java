/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.resources;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.rules.PcaDetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.service.PcaDetermineEligibilityService;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/products/features/uid/pcaEligibleCustomerInstructions")
public class PcaDetermineEligibilityResource extends Base {

    private PcaDetermineEligibilityService       service;
    private RequestBodyResolver                  resolver;
    private PcaDetermineEligibilityRuleValidator ruleValidator;
    private GalaxyErrorCodeResolver              errorResolver;

    private static final Class                   CLASS_VALUE = PcaDetermineEligibilityResource.class;

    public PcaDetermineEligibilityResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public PcaDetermineEligibilityResource(PcaDetermineEligibilityService service, RequestBodyResolver resolver,
            GalaxyErrorCodeResolver errorResolver, PcaDetermineEligibilityRuleValidator ruleValidator) {
        this.service = service;
        this.resolver = resolver;
        this.errorResolver = errorResolver;
        this.ruleValidator = ruleValidator;
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
    @TraceLog
    public Response determineEligibility(String requestBody) throws InvalidFormatException {
        logger.traceLog(this.getClass(), "Inside determineEligibility");
        PcaDetermineEligibilityRequest determineEligibilityRequest = resolver.resolve(requestBody,
                PcaDetermineEligibilityRequest.class);
        validate(determineEligibilityRequest);
        logger.traceLog(this.getClass(), "After validation of the Request and about to invoke determineEligibility");
        PcaDetermineEligibilityResponse response = service.determineEligiblitySuitability(determineEligibilityRequest);
        logger.traceLog(this.getClass(), "After determineEligiblitySuitability completion");
        if (response != null) {
            return respond(Status.OK, response);
        }
        return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    /*
     * This methods validate the request object for DetermineEligibility.
     */
    private void validate(PcaDetermineEligibilityRequest request) throws InvalidFormatException {
        if (request.getArrangementType() != null) {
            ValidationError validationError = ruleValidator.validateRules(request);
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