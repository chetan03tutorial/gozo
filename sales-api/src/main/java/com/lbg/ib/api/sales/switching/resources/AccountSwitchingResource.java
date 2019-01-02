package com.lbg.ib.api.sales.switching.resources;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.domain.PegaSwitchTypeEnum;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.util.DateUtil;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.service.AccountSwitchingService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.text.ParseException;

@Path("/switching")
public class AccountSwitchingResource {

    @Autowired
    private AccountSwitchingService accountSwitchingService;

    @Autowired
    private FieldValidator          fieldValidator;

    @Autowired
    private LoggerDAO               logger;

    @POST
    @Path("/switchAccount")
    @Consumes("application/json")
    @Produces("application/json")
    @TraceLog
    public Response switchAccount(AccountSwitchingRequest  accountSwitchingRequest) throws ParseException {
        validate(accountSwitchingRequest);
        return respond(Response.Status.OK, accountSwitchingService.switchAccount(accountSwitchingRequest));
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    private ValidationError validate(AccountSwitchingRequest request) throws InvalidFormatException, ParseException {

        ValidationError validationError = fieldValidator.validateInstanceFields(request);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
        validatePartiesInRequest(request);
        if(DateUtil.isPastDate(request.getSwitchDate())){
            throw new ServiceException(new ResponseError(ResponseErrorConstants.INVALID_SWITCH_DATE
                    ,"Invalid switch date or Switch date in past"));
        }
        return validationError;
    }

    private void validatePartiesInRequest(AccountSwitchingRequest request){
        if((request.getParties()==null)||(request.getParties().size()==0)){
            throw new InvalidFormatException("Party list is missing or has size zero.Party list is mandatory");
        }
        if(((request.getParties().size()==1)&&((request.getSwitchingType()== PegaSwitchTypeEnum.JOINT_TO_JOINT) ||(request.getSwitchingType()== PegaSwitchTypeEnum.SOLE_TO_JOINT)))
                ||((request.getParties().size()==2)&&(request.getSwitchingType()== PegaSwitchTypeEnum.SOLE_TO_SOLE))){
            throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_INCORRECT_PARTY_SIZE
                    ,"Incorrect number of parties for the selected switching journey."));
        }
    }
}
