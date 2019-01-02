/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.colleagues.involvedparty.service.ModifyCommunicationProfileService;
import com.lbg.ib.api.sales.common.domain.MessageResponse;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.paperless.dto.PaperlessResult;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetails;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoResult;
import com.lbg.ib.api.sales.paperless.dto.UserPreferences;
import com.lbg.ib.api.sales.paperless.mapper.PaperlessMessageMapper;
import com.lbg.ib.api.sales.paperless.service.PaperlessService;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.validator.business.user.UserInfoValidator;
import com.lbg.ib.api.sales.user.service.UserProductHoldingService;

/**
 * Contain Paperless API's.
 * @author tkhann/cshar8
 */
@Path("/paperless")
public class PaperlessResource {
    /**
     * Paperless Service object
     */
    @Autowired
    private PaperlessService paperlessService;
    /**
     * Session object.
     */
    @Autowired
    private SessionManagementDAO session;

    /**
     * RequestBodyResolver object
     */
    @Autowired
    private RequestBodyResolver requestResolver;
    /**
     * Constant
     */
    private static final String SUCCESS_RESP = "Details updated Successfully";
    /**
     * Object of Field Validator
     */
    @Autowired
    private FieldValidator fieldValidator;
    /**
     * Object of moduleContext.
     */
    @Autowired
    private ModuleContext moduleContext;
    /**
     * Object of Logger.
     */
    @Autowired
    private LoggerDAO logger;

    /**
     * API to return user mandate details.
     * @return Response
     */

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/userMandateInfo")
    public Response getUserMandateInfo() {
        ArrangeToActivateParameters param = session.getArrangeToActivateParameters();
        UserContext context = session.getUserContext();
        if (null != param && !StringUtils.isEmpty(param.getOcisId())) {
            context.setOcisId(param.getOcisId());
            context.setPartyId(param.getPartyId());
        }
        UserInfoValidator.validateUserSession(context);
        UserInfoValidator.validateOcisId(context.getOcisId());
        UserMandateInfoResult paperlessData = paperlessService.getUserMandateInfo(context.getOcisId());
        return respond(Response.Status.OK, paperlessData);
    }

    /**
     * API to return user mandate details.
     * @return Response
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/updateEmail")
    public Response updateEmail(String requestBody) throws InvalidFormatException {
        PersonalDetails personalDetails = requestResolver.resolve(requestBody, PersonalDetails.class);
        ValidationError validationError = fieldValidator.validateInstanceFields(personalDetails);
        UserContext context = session.getUserContext();
        String ocisId = context.getOcisId();
        String partyId = context.getPartyId();
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
        paperlessService.updateEmail(personalDetails, ocisId, partyId);
        return respond(Response.Status.OK, new MessageResponse(true, SUCCESS_RESP));
    }

    /**
     * Method to return the response.
     * @param status Status
     * @param content Object
     * @return Response
     */
    public Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    /**
     * @author cshar8 API to return Go Green status of user.
     * @return Response API to return Go Green status of user.
     * @return Response
     */
    @Path("/status")
    @GET
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response getGoGreenStatus() throws InvalidFormatException {
        ArrangeToActivateParameters param = session.getArrangeToActivateParameters();
        UserContext context = session.getUserContext();
        if (null != param && !StringUtils.isEmpty(param.getOcisId()) && !StringUtils.isEmpty(param.getPartyId()))
        {
            context.setOcisId(param.getOcisId());
            context.setPartyId(param.getPartyId());
        }
        String ocis = context.getOcisId();
        String partyId = context.getPartyId();
        BigInteger ocisId = UserInfoValidator.validateOcisId(ocis);
        UserInfoValidator.validatePartyId(partyId);
        UserProductHoldingService userProductHoldingService = moduleContext.getService(UserProductHoldingService.class);
        List<Account> accountList = userProductHoldingService.retrieveProductHolding(partyId, ocisId);
        PaperlessResult paperlessData = PaperlessMessageMapper.buildPaperlessStatus(accountList);
        return respond(Response.Status.OK, paperlessData);
    }

    /**
     * API to return update preferences.
     * @return Response
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/updatePreferences")
    public Response updatePreferences(String requestBody) throws InvalidFormatException {
        UserPreferences preferences = requestResolver.resolve(requestBody, UserPreferences.class);
        ValidationError validationError = fieldValidator.validateInstanceFields(preferences);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
        UserContext context = session.getUserContext();
        String ocisId = context.getOcisId();
        String partyId = context.getPartyId();
        UserInfoValidator.validatePartyId(partyId);
        UserInfoValidator.validateOcisId(ocisId);
        ModifyCommunicationProfileService profileService = moduleContext
                .getService(ModifyCommunicationProfileService.class);
        profileService.updatePreferences(preferences, ocisId, partyId);
        return respond(Response.Status.OK, new MessageResponse(true, SUCCESS_RESP));
    }
}