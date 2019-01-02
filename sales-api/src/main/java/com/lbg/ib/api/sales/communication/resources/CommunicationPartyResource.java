/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.communication.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.communication.domain.InvolvedPartyDetails;
import com.lbg.ib.api.sales.communication.service.CommunicationPartyService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/communication/party")
public class CommunicationPartyResource {
    @Autowired
    private CommunicationPartyService communicationPartyService;

    @Autowired
    private RequestBodyResolver       resolver;

    @Autowired
    private FieldValidator            fieldValidator;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response populateDetailsInSession(String requestBody) throws InvalidFormatException {
        InvolvedPartyDetails partyDetails = resolver.resolve(requestBody, InvolvedPartyDetails.class);
        validateRequest(partyDetails);
        communicationPartyService.savePartyDetailsForSendCommunication(partyDetails);
        return respond(Status.OK);
    }

    private void validateRequest(InvolvedPartyDetails partyDetails) throws InvalidFormatException {
        ValidationError validationError = fieldValidator.validateInstanceFields(partyDetails);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }

    private Response respond(Status status) {
        return new ResponseBuilderImpl().status(status).entity(null).build();
    }

}
