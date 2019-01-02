/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.party.domain.Classifications;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.party.service.TinDetailsService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/party/tin")
public class TinDetailsResource {

    @Autowired
    private TinDetailsService       classifyPartyResidencyService;

    @Autowired
    private RequestBodyResolver     resolver;

    @Autowired
    private FieldValidator          fieldValidator;

    @Autowired
    private GalaxyErrorCodeResolver errorResolver;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response taxIdentify(String requestBody) throws InvalidFormatException {
        TinDetails partyDetails = resolver.resolve(requestBody, TinDetails.class);
        validateRequest(partyDetails);
        Classifications classifications = new Classifications();
        classifications.setClassifications(classifyPartyResidencyService.identify(partyDetails));
        if (classifications.getClassifications() != null) {
            return respond(Status.OK, classifications);
        }
        return respond(Status.OK, errorResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    private void validateRequest(TinDetails partyDetails) throws InvalidFormatException {
        ValidationError validationError = fieldValidator.validateInstanceFields(partyDetails);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}
