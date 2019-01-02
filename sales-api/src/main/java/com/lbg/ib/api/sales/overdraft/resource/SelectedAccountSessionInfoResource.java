package com.lbg.ib.api.sales.overdraft.resource;

import com.lbg.ib.api.sales.common.domain.MessageResponse;
import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.SelectedAccountSessionInfo;
import com.lbg.ib.api.sales.overdraft.domain.UpdateSessionRequest;
import com.lbg.ib.api.sales.overdraft.service.AccountSessionInfoService;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/sessionInfo")
public class SelectedAccountSessionInfoResource extends BaseResource {

    @Autowired
    private SessionManagementDAO session;
    @Autowired
    private AccountSessionInfoService service;
    @Autowired
    private GalaxyErrorCodeResolver errorResolver;
    @Autowired
    private LoggerDAO logger;

    private static final String SUCCESS_RESP = "Session updated Successfully";

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/update")
    @TraceLog
    public Response setOverdraftLimit(UpdateSessionRequest request) {
        logger.traceLog(this.getClass(), "Demanded overdraft in request is: "+ request.getDemandedOd());
        validateUpdateSession(request);
        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        if(arrangeToActivateParameters != null) {
            session.setDemandedOD(request.getDemandedOd());
        }
        return respond(Response.Status.OK, new MessageResponse(true, SUCCESS_RESP));
    }


    @GET
    @Produces(APPLICATION_JSON)
    @Path("/selectedAccount")
    @TraceLog
    public Response getAccountSessionInfo() {
        logger.traceLog(this.getClass(), "Getting the session info for overdraft");
        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        // Check if Buy New journey. If yes, reset userInfo
        if(arrangeToActivateParameters != null) {
            logger.traceLog(this.getClass(), "Check the activation status: " + arrangeToActivateParameters.getActivationStatus());
            if(!CommonConstant.APPLICATION_SUCESS_STATUS.equalsIgnoreCase(arrangeToActivateParameters.getActivationStatus())) {
                throw new ServiceException(new ResponseError(ResponseErrorConstants.INVALID_REQUEST_ACTIVATION_FAILED,
                        "Invalid request as application status is not active"));
            } else {
                session.setUserInfo(null);  // To make userInfo will have primary party detail
            }
        }
        SelectedAccountSessionInfo response = service.fetchSessionDetail();
        if (response != null) {
            return respond(Response.Status.OK, response);
        }
        logger.traceLog(this.getClass(), "Output Response has error. ");
        return respond(Response.Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    /**
     * Validate that DemandedOD shouldn't be greater than approved OD during offer
     * @param request
     */
    private void validateUpdateSession(UpdateSessionRequest request) {
        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        if(arrangeToActivateParameters != null && request.getDemandedOd() != null) {
            logger.traceLog(this.getClass(), "Approved overdraft amount in session is: "+ arrangeToActivateParameters.getAmtOverdraft().doubleValue());
            if(request.getDemandedOd() > arrangeToActivateParameters.getAmtOverdraft().doubleValue()) {
                throw new ServiceException(new ResponseError(ResponseErrorConstants.INVALID_OVERDRAFT_AMOUNT,
                        "Demanded overdraft can't exceed with approved amount"));
            }
        }
    }
}