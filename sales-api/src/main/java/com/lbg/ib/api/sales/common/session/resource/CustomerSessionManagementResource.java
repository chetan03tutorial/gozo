package com.lbg.ib.api.sales.common.session.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.domain.MessageResponse;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/session")
public class CustomerSessionManagementResource extends Base {

    @Autowired
    private RequestBodyResolver  resolver;

    @Autowired
    private SessionManagementDAO session;

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("/setInfo")
    @TraceLog
    public Response setCustomerInfo(@Context HttpServletRequest req, String requestBody) throws Exception {
        logger.traceLog(this.getClass(), "Setting the Customer Info in session " + requestBody);
        CustomerInfo reqCustomerInfo = resolver.resolve(requestBody, CustomerInfo.class);

        if (reqCustomerInfo != null) {
            CustomerInfo updatedCustInfo = null;
            CustomerInfo sessionCustInfo = session.getCustomerDetails();

            if (sessionCustInfo != null) {
                sessionCustInfo.setCustomerDocuments(reqCustomerInfo.getCustomerDocuments());
                sessionCustInfo.setParentAssessmentEvidence(reqCustomerInfo.getParentAssessmentEvidence());
                sessionCustInfo.setPrimaryAssessmentEvidence(reqCustomerInfo.getPrimaryAssessmentEvidence());
                updatedCustInfo = sessionCustInfo;
            } else {
                updatedCustInfo = reqCustomerInfo;
            }
            if (session.getBranchContext() != null) {
                sessionCustInfo.setOriginatingSortCode(session.getBranchContext().getOriginatingSortCode());
                sessionCustInfo.setColleagueId(session.getBranchContext().getColleagueId());
            }

            session.setCustomerDetails(updatedCustInfo);
        }

        logger.traceLog(this.getClass(), "Completed the setting of Customer details to Session.");

        return respond(Status.OK, new MessageResponse(true, "Customer details set in session"));
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/getInfo")
    public Response getCustomerInfo() throws Exception {
        logger.traceLog(this.getClass(), "Getting the Customer Info from session");
        CustomerInfo customerInfo = session.getCustomerDetails();
        if (customerInfo == null) {
            customerInfo = new CustomerInfo();
        }

        if (session.getBranchContext() != null) {
            customerInfo.setOriginatingSortCode(session.getBranchContext().getOriginatingSortCode());
            customerInfo.setColleagueId(session.getBranchContext().getColleagueId());
        }
        logger.traceLog(this.getClass(), "Completed getting the Customer Info from session");
        return respond(Status.OK, customerInfo);
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}
