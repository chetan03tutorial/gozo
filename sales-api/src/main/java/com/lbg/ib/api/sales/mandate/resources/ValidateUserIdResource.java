
package com.lbg.ib.api.sales.mandate.resources;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidation;
import com.lbg.ib.api.sales.mandate.service.ValidateUserIdService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Path("/userid")
public class ValidateUserIdResource {

    private RequestBodyResolver     resolver;

    private ValidateUserIdService   service;

    private GalaxyErrorCodeResolver errorResolver;

    private LoggerDAO               logger;

    public ValidateUserIdResource() {/* required for trace logging */
    }

    @Autowired
    public ValidateUserIdResource(RequestBodyResolver resolver, ValidateUserIdService service,
            GalaxyErrorCodeResolver errorResolver, LoggerDAO logger) {
        this.resolver = resolver;
        this.service = service;
        this.errorResolver = errorResolver;
        this.logger = logger;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    // Warning do not add @TraceLog as we do not intend to print the Request as
    // the Request contains the DOB.
    public Response validate(String requestBody) throws ServiceException {
        try {
            UserIdValidation validation = resolver.resolve(requestBody, UserIdValidation.class);
            logger.traceLog(this.getClass(), "Inside the ValidateUserId Resource. Input Request is : " + validation);
            return respond(Status.OK, service.validate(validation));
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
