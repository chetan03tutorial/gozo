/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.mca.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.ea.config.ConfigurationService;

public class BranchContextResource extends Base {

    @Autowired
    private BranchContextService branchContextService;

    @Autowired
    private RequestBodyResolver  resolver;

    @Autowired
    private ConfigurationService configurationService;

    private static final Class   CLASS_NAME = BranchContextResource.class;

    @POST
    @Produces(APPLICATION_JSON)
    @Path("/branch/setcontext")
    @TraceLog
    public Response setBranchContext(@Context HttpServletRequest req, String requestBody) throws Exception {
        logger.traceLog(this.getClass(), "Setting the Branch Context " + requestBody);
        BranchContext branchContext = resolver.resolve(requestBody, BranchContext.class);
        ColleagueContext context = null;
        if (null != req.getSession(false).getAttribute(BranchContextConstants.COLLEGUE_ID)) {
            String colleagueId = req.getSession(false).getAttribute(BranchContextConstants.COLLEGUE_ID).toString();
            logger.traceLog(this.getClass(), "Fetch the colleagueId from Session " + colleagueId);
            branchContext.setColleagueId(colleagueId);

            if (null != req.getSession(false).getAttribute(BranchContextConstants.COLLEAGUE_DOMAIN)) {
                String domain = req.getSession(false).getAttribute(BranchContextConstants.COLLEAGUE_DOMAIN).toString();
                logger.traceLog(this.getClass(), "Fetch the domain from Session " + domain);
                branchContext.setDomain(domain);
            }
        }
        context = branchContextService.setBranchContextToSession(branchContext);
        context.setDomain(branchContext.getDomain());
        logger.traceLog(this.getClass(), "Completed the setting of BranchContext to Session.");

        return respond(Status.OK, context);
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/branch/setcontext/{colleagueId}/{domain}")
    public Response setColleagueToBranchContext(@PathParam("colleagueId") String colleagueId,
            @PathParam("domain") String domain, @Context HttpServletRequest req) throws Exception {

        Map<String, Object> appConfig = configurationService
                .getConfigurationItems(BranchContextConstants.APPLICATION_PROPERTIES);
        if (!appConfig.isEmpty()) {
            String accreditionFlag = (String) appConfig.get(BranchContextConstants.ACCREDITION_FLAG);
            if (Boolean.valueOf(accreditionFlag)) {
                return respond(Status.OK, "nothing to be done here");
            }

            req.getSession().setAttribute(BranchContextConstants.COLLEGUE_ID, colleagueId);
            req.getSession().setAttribute(BranchContextConstants.COLLEAGUE_DOMAIN, domain);
            return respond(Status.OK, "set correctly");
        }

        return respond(Status.OK, "nothing to be done here");

    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
