package com.lbg.ib.api.sales.communication.resources;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.dto.StatusMessage;
import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import com.lbg.ib.api.sales.communication.service.CommunicationDispatchService;
import com.lbg.ib.api.sales.communication.service.CommunicationService;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;



@Path("/communication")
public class CommunicationResource extends BaseResource {

    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private LoggerDAO logger;
    @Autowired
    private RequestBodyResolver resolver;
    @Autowired
    private GalaxyErrorCodeResolver errorResolver;
    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ModuleContext moduleContext;
    public CommunicationResource() {
        /*required for trace logging*/
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/email")
    @TraceLog
    public Response sendEmail(String requestBody) throws ServiceException {
        try{
            PartyCommunicationDetails communicationDetails = resolver.resolve(requestBody, PartyCommunicationDetails.class);
            validateInputRequestObject(communicationDetails);
            boolean result = communicationService.sendEmailCommunication(communicationDetails);
            if(result){
                StatusMessage message = new StatusMessage();
                message.setStatusMessage(Constants.CommunicationConstants.EMAIL_SUCCESSFUL);
                return respond(Response.Status.OK, message);
            }else{
                ResponseError responseError = errorResolver.resolve(ResponseErrorConstants.EMAIL_FAILURE);
                throw new ServiceException(responseError);
            }
        }catch (InvalidFormatException e) {
            logger.logException(this.getClass(), e);
            return respond(Response.Status.BAD_REQUEST, errorResolver.createResponseError(ResponseErrorConstants.BAD_REQUEST_FORMAT));
        }
    }

    private void validateInputRequestObject(PartyCommunicationDetails communicationDetails) throws ServiceException {
        if (StringUtils.isBlank(communicationDetails.getTemplateId())) {
            ResponseError responseError = errorResolver.resolve(ResponseErrorConstants.TEMPLATE_ID_NOT_FOUND);
            throw new ServiceException(responseError);
        }
        Map<String, String> tokenMap = communicationDetails.getTokenMap();
        if(tokenMap == null || tokenMap.size() == 0){
            ResponseError responseError = errorResolver.resolve(ResponseErrorConstants.NO_ACTION_ITEMS);
            throw new ServiceException(responseError);
        }
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/scheduled/email")
    @TraceLog
    public Response scheduledEmail(String requestBody) {
        ScheduleEmailSmsRequest userRequest = resolver.resolve(requestBody, ScheduleEmailSmsRequest.class);
        CommunicationDispatchService manager = moduleContext.getService(CommunicationDispatchService.class);
        return respond(Response.Status.OK, manager.scheduleCommunication(userRequest));
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/send/email")
    @TraceLog
    public Response email(String requestBody) {
        AtomicInteger emailCounter = (AtomicInteger)sessionManager.getEmailRetryCount();
        if(sessionManager.getBranchContext() == null &&
                emailCounter != null && emailCounter.intValue() > Constants.MAX_EMAIL_RETRY_LIMIT.intValue()) {
            throw new ServiceException(new ResponseError(ResponseErrorConstants.MAX_EMAIL_RETRY_REACHES,
                    "Max email retry limit reaches"));
        }
        ScheduleEmailSmsRequest userRequest = resolver.resolve(requestBody, ScheduleEmailSmsRequest.class);
        CommunicationDispatchService manager = moduleContext.getService(CommunicationDispatchService.class);
        ScheduleEmailSmsResponse response =  manager.sendCommunication(userRequest);
       // AtomicInteger counter = null;
        if(emailCounter == null) {
            emailCounter = new AtomicInteger(0);
        }
        sessionManager.setEmailRetryCount(new AtomicInteger(emailCounter.incrementAndGet()));
        return respond(Response.Status.OK, response);
    }
}
