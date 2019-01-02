package com.lbg.ib.api.sales.bankwizard.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponse;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.bankwizard.domain.BankAccountDetails;
import com.lbg.ib.api.sales.bankwizard.domain.BankWizardResponse;
import com.lbg.ib.api.sales.bankwizard.service.BankWizardService;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.mandate.resources.ValidateUserIdResource;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
@Path("/bankwizard")
public class ValidateAccountResource {

    private BankWizardService       bankWizardService;
    private RequestBodyResolver     resolver;
    private GalaxyErrorCodeResolver errorResolver;
    private LoggerDAO               logger;

    public ValidateAccountResource() {
        /* required for trace logging */}

    @Autowired
    public ValidateAccountResource(BankWizardService bankWizardService, RequestBodyResolver resolver,
            GalaxyErrorCodeResolver errorResolver, LoggerDAO logger) {
        this.bankWizardService = bankWizardService;
        this.resolver = resolver;
        this.errorResolver = errorResolver;
        this.logger = logger;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/validateAccount")
    @TraceLog
    public Response validate(String requestBody) throws ServiceException {
        try {
            logger.traceLog(this.getClass(), "validating combination of account number and sort code");
            BankAccountDetails accountDetails = resolver.resolve(requestBody, BankAccountDetails.class);
            ValidateBankDetailstResponse bankWizardResponse = bankWizardService.validateBankDetails(accountDetails);
            logger.traceLog(this.getClass(), "End of validating combination of account number and sort code");
            return respond(Status.OK, bankWizardResponse);
        } catch (InvalidFormatException e) {
            logger.logException(ValidateUserIdResource.class, e);
            return respond(Status.BAD_REQUEST,
                    errorResolver.createResponseError(ResponseErrorConstants.BAD_REQUEST_FORMAT));
        }
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}
