package com.lbg.ib.api.sales.user.resources;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.USER_NOT_FOUND;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.UserDetails;
import com.lbg.ib.api.sales.user.service.PartyDetailService;
import com.lbg.ib.api.sales.user.service.UserService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sso.domain.user.Arrangement;

/**
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved. The class UserResource is the RestApi responsible for fetching the UserInfo
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 */

@Path("/userInfo")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private ModuleContext moduleContext;

    @Autowired
    private RequestBodyResolver requestResolver;

    private String userNotAvailable = "Cannot find user Info";

    /*
     * Zero Argument Constructor to comments to avoid Sonar Violations.
     */
    public UserResource() {
    }

    /**
     * Fetch user details based on input request.
     * @param requestBody encrypted account
     * @return userDetails.
     * @throws InvalidFormatException
     */
    @GET
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response fetchUserInfo(@QueryParam("accountType") String accountType, @QueryParam("account") String encryptedAccount) throws InvalidFormatException {
        logger.traceLog(this.getClass(), "Fetching User Info and KYC Details");
        UserService service = moduleContext.getService(UserService.class);
        if (null != accountType || null != encryptedAccount) {
            UserDetails userInfo = service.fetchUserInfo(accountType, encryptedAccount, false);
            logger.traceLog(this.getClass(), "End of Fetching User Info");
            return respond(Status.OK, userInfo);
        } else {
            Arrangement userInfo = userService.fetchUserInfo(false);
            if (userInfo == null) {
                return respond(Status.OK, new ResponseError(USER_NOT_FOUND, format(userNotAvailable)));
            }
            logger.traceLog(this.getClass(), "End of Fetching User Info");
            return respond(Status.OK, userInfo);
        }
    }

    /**
     * Fetch user details based on input request.
     * @param requestBody encrypted account
     * @return userDetails.
     * @throws InvalidFormatException
     */
    @GET
    @Produces(APPLICATION_JSON)
    @Path("/goJoint")
    @TraceLog
    public Response fetchUserInfoForGoJoint(@QueryParam("accountType") String accountType, @QueryParam("account") String encryptedAccount) throws InvalidFormatException {
        logger.traceLog(this.getClass(), "Fetching User Info and KYC Details");
        UserService service = moduleContext.getService(UserService.class);
        if (null != accountType || null != encryptedAccount) {
            UserDetails userInfo = service.fetchUserInfo(accountType, encryptedAccount, true);
            logger.traceLog(this.getClass(), "End of Fetching User Info");
            return respond(Status.OK, userInfo);
        } else {
            Arrangement userInfo = userService.fetchUserInfo(true);
            if (userInfo == null) {
                return respond(Status.OK, new ResponseError(USER_NOT_FOUND, format(userNotAvailable)));
            }
            logger.traceLog(this.getClass(), "End of Fetching User Info");
            return respond(Status.OK, userInfo);
        }
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    /**
     * @author cshar8
     * @return PartyDetails
     * @throws InvalidFormatException
     */
    @GET
    @Produces({APPLICATION_JSON})
    @Path("/demographics")
    public Response retrieveInvolvedPartyDemographicDetails() throws InvalidFormatException {
        PartyDetailService service = moduleContext.getService(PartyDetailService.class);
        PartyDetails partyResponse = service.retrievePartyDetails();
        return respond(Response.Status.OK, partyResponse);
    }

}
