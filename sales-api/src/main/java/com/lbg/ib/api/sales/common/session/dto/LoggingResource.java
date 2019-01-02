package com.lbg.ib.api.sales.common.session.dto;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.domain.MessageResponse;

@Path("/logging")
public class LoggingResource extends Base {

    public LoggingResource() {
        // Logging to avoid sonar violations
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("/cwalogging")
    @TraceLog
    public Response logCWAInfo(@Context HttpServletRequest req, String requestBody) {
        logger.traceLog(this.getClass(), requestBody);
        return respond(Status.OK, new MessageResponse(true, "Logged information from CWA"));
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
