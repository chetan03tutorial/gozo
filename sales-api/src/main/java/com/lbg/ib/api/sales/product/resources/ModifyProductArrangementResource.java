package com.lbg.ib.api.sales.product.resources;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.sales.product.service.ModifyProductArrangementService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
@Path("/modify/product/arrangement")
public class ModifyProductArrangementResource {

    private ModifyProductArrangementService service;
    private FieldValidator fieldValidator;
    private LoggerDAO logger;
    private RequestBodyResolver resolver;
    private GalaxyErrorCodeResolver errorResolver;

    public ModifyProductArrangementResource() {
        /* required for trace logging */
    }

    @Autowired
    public ModifyProductArrangementResource(ModifyProductArrangementService service, FieldValidator fieldValidator,
            LoggerDAO logger, RequestBodyResolver resolver, GalaxyErrorCodeResolver errorResolver) {
        this.service = service;
        this.fieldValidator = fieldValidator;
        this.logger = logger;
        this.resolver = resolver;
        this.errorResolver = errorResolver;
    }

    /**
     *
     * @param requestBody
     *            for MOdify Product Arrangement Service.
     * @return
     * @throws Exception
     * @throws ServiceException
     *             in case for any business and system failures.
     * @throws InvalidFormatException
     *             for invalid request body.
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response modifyProductArrangement(String requestBody) throws Exception {
        ModifyProductArrangement modifyProductArrangement = resolver.resolve(requestBody,
                ModifyProductArrangement.class);
        ValidationError validationError = validate(modifyProductArrangement);
        if (validationError != null) {
            throw new ServiceException(
                    errorResolver.customResolve(ResponseErrorConstants.RETRIEVE_PENDING_ARRANGEMENT_SERVICE_EXCEPTION,
                            validationError.getMessage()));
        }
        ModifyProductArrangement response = service.modifyProductArrangement(modifyProductArrangement);
        return respond(Response.Status.OK, response);
    }

    private ValidationError validate(ModifyProductArrangement modifyProductArrangement) throws Exception {
        return fieldValidator.validateInstanceFields(modifyProductArrangement);
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
