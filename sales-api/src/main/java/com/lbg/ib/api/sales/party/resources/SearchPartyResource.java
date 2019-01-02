package com.lbg.ib.api.sales.party.resources;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * SearchPartyResource
 */
@Path("/parties")
public class SearchPartyResource {

    @Autowired
    private SearchPartyService searchPartyService;

    @Autowired
    private FieldValidator     fieldValidator;

    @Autowired
    private LoggerDAO          logger;

    @GET
    @Path("/{agreementIdentifier}")
    @Produces("application/json")
    @TraceLog
    public Response searchParty(@PathParam("agreementIdentifier") SearchPartyRequest partyDetails,
                                @QueryParam("type") String agreementType, @QueryParam("role") String partyRole) {
        partyDetails.setAgreementType(agreementType);
        partyDetails.setPartyRole(partyRole);
        validate(partyDetails);
        ValidationError validationError = validateAgreementIdentifierLength(partyDetails);
        if (null != validationError) {
            Map<String, String> errors = new HashMap<String, String>();
            errors.put("errors", "Invalid Agreement Identifier");
            return respond(Response.Status.BAD_REQUEST, errors);
        }
        return respond(Response.Status.OK, searchPartyService.retrieveParty(partyDetails));
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    private ValidationError validate(Object instance) throws InvalidFormatException {

        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }

        return validationError;
    }

    private ValidationError validateAgreementIdentifierLength(SearchPartyRequest partyDetails) {
        ValidationError validationError = null;
        if (!(partyDetails.getAgreementIdentifier().length() >= 14
                && partyDetails.getAgreementIdentifier().length() <= 16)) {
            validationError = new ValidationError("Invalid Agreement Identifier");
        }
        return validationError;
    }
}
