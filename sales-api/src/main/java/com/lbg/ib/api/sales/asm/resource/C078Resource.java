package com.lbg.ib.api.sales.asm.resource;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.service.C078Service;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.C078Resp;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 * The class is created to work as a Resource for C078 resource to fetch App Score information
 */
@Path("/C078")
public class C078Resource {

    @Autowired
    private C078Service service;
    /**
     * API to enquire c078 for app score
     * @param appScoreRequest
     * @return
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response fetchAppScoreInformation(AppScoreRequest appScoreRequest) {
        C078Resp response = service.invokeC078(appScoreRequest);
        return respond(Response.Status.OK, response);
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
