package com.lbg.ib.api.sales.product.resources;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.PRODUCT_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetialsJsonReponse;
import com.lbg.ib.api.sales.product.service.RetrieveProductArrangementService;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
@Path("/customer/pending/{arrangementId}")
public class RetrieveProductArrangementResource {

    private RetrieveProductArrangementService service;
    private FieldValidator                    fieldValidator;
    private GalaxyErrorCodeResolver           errorResolver;

    public RetrieveProductArrangementResource() {
        /* required for trace logging */}

    @Autowired
    public RetrieveProductArrangementResource(RetrieveProductArrangementService service, FieldValidator fieldValidator,
            GalaxyErrorCodeResolver errorResolver) {
        this.service = service;
        this.fieldValidator = fieldValidator;
        this.errorResolver = errorResolver;
    }

    @GET
    @Produces("application/json")
    //// @TraceLog
    public Response retrieveProductArrangement(@PathParam("arrangementId") ArrangementId reference)
            throws InvalidFormatException, Exception {
        validate(reference);

        List<CustomerPendingDetails> custpendDetailList = new ArrayList<CustomerPendingDetails>();
        CustomerPendingDetialsJsonReponse customerPendingDetialsJsonReponse = new CustomerPendingDetialsJsonReponse();
        CustomerPendingDetails productFeatures = service.retrieveProductArrangement(reference);
        custpendDetailList.add(productFeatures);
        customerPendingDetialsJsonReponse.setCustomerPendingDetails(custpendDetailList);
        if (productFeatures != null) {
            return respond(Status.OK, customerPendingDetialsJsonReponse);
        }
        return respond(Status.OK, errorResolver.createResponseError(PRODUCT_NOT_FOUND));

    }

    private void validate(ArrangementId reference) throws Exception {
        ValidationError validationError = fieldValidator.validateInstanceFields(reference);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
