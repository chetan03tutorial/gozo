package com.lbg.ib.api.sales.shared;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;

@Path("/trace")
public class TraceRequestEnabler extends BaseResource {

    @Autowired
    private SessionManagementDAO sessionManager;

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/enable")
    @TraceLog
    public Response enableLogging() {
        sessionManager.setTraceRequestFlag(String.valueOf(Boolean.TRUE));
        return respond(Response.Status.OK, true);
    }

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/disable")
    @TraceLog
    public Response disableLogging() {
        sessionManager.setTraceRequestFlag(String.valueOf(Boolean.FALSE));
        return respond(Response.Status.OK, true);
    }
}
