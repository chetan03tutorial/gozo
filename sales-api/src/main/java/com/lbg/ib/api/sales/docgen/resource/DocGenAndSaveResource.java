package com.lbg.ib.api.sales.docgen.resource;
/*
Created by Rohit.Soni at 07/05/2018 12:35
*/

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveResponseParty;
import com.lbg.ib.api.sales.docgen.service.DocGenAndSaveService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/account/genDocAndSave")
public class DocGenAndSaveResource extends BaseResource {

    @Autowired
    DocGenAndSaveService docGenAndSaveService;

    @Autowired
    private FieldValidator fieldValidator;

    /**
     * API to generate doument and save it with customer ocis record.
     * @param docGenAndSaveRequest
     * @return
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response generateAndSaveDocument(DocGenAndSaveRequest docGenAndSaveRequest) {
        validate(docGenAndSaveRequest);
        List<DocGenAndSaveResponseParty> response = docGenAndSaveService.generateAndSaveDocument(docGenAndSaveRequest);
        return respond(Response.Status.OK, response);
    }

    private void validate(DocGenAndSaveRequest docGenAndSaveRequest) throws InvalidFormatException {
        ValidationError validationError = fieldValidator.validateInstanceFields(docGenAndSaveRequest);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }
}
