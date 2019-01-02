package com.lbg.ib.api.sales.overdraft.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.overdraft.service.PcciService;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/pcci")
public class PcciOverdraftResource extends BaseResource {

    @Autowired
    private ModuleContext beanLoader;
    
    @Autowired
    private RequestBodyResolver  resolver;

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/overdraft/{product}")
    @TraceLog
    public Response pcciOverdraftDetails(@PathParam("product") String productMnemonic) {
        PcciService pcciService = beanLoader.getService(PcciService.class);
        return respond(Response.Status.OK, pcciService.getPcciOverdraftDetails(productMnemonic));
    }

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/overdraft")
    @TraceLog
    public Response pcciOverdraftDetails() {
        PcciService pcciService = beanLoader.getService(PcciService.class);
        return respond(Response.Status.OK, pcciService.getPcciOverdraftDetails());
    }
    
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/overdraft")
    @TraceLog
    public Response pcciOverdraftDetailsAmount(String requestBody) {
        PcciOverdraftRequest overDraftOption = resolver.resolve(requestBody, PcciOverdraftRequest.class);
        PcciService pcciService = beanLoader.getService(PcciService.class);
        return respond(Response.Status.OK, pcciService.getPcciOverdraftDetailsWithOverdraftOption(overDraftOption));
    }
}
