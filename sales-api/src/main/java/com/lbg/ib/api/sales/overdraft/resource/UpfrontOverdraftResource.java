package com.lbg.ib.api.sales.overdraft.resource;


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
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftRequest;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftResponse;
import com.lbg.ib.api.sales.overdraft.service.UpfrontOverdraftService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/upfront/overdraft")
public class UpfrontOverdraftResource {

    private UpfrontOverdraftService service;
    private RequestBodyResolver resolver;
    private GalaxyErrorCodeResolver errorResolver;
    private LoggerDAO logger;

    public UpfrontOverdraftResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public UpfrontOverdraftResource(UpfrontOverdraftService service, RequestBodyResolver resolver,
                                      GalaxyErrorCodeResolver errorResolver, LoggerDAO logger) {
        this.service = service;
        this.resolver = resolver;
        this.errorResolver = errorResolver;
        this.logger = logger;
    }

    /**
     * This method is used to determine upgrade Eligibility for given account number and sort code.
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
    public Response checkUpfrontOverdraft(String requestBody) throws InvalidFormatException {

        UpfrontOverdraftRequest upfrontOverdraftRequest = resolver.resolve(requestBody,
                UpfrontOverdraftRequest.class);
        logger.traceLog(this.getClass(),
                "Inside the Upfront Overdraft Resource. Input Request is : " + upfrontOverdraftRequest);

        UpfrontOverdraftResponse response = service.upfrontOverdraft(upfrontOverdraftRequest);
        if (response != null) {
            logger.traceLog(this.getClass(), "Inside the Upfront Overdraft Resource. Output Response is : " + response);
            return respond(Status.OK, response);
        }
        logger.traceLog(this.getClass(), "Inside the Upfront Overdraft Resource. Output Response has error. ");
        return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }


    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
