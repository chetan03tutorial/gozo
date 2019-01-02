package com.lbg.ib.api.sales.product.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.sales.terminate.service.TerminateArrangementService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 16thFeb2017
 ***********************************************************************/

@Path("/terminate/arrangement")
public class TerminateArrangementResource {
    
    /** The resolver. */
    @Autowired
    private RequestBodyResolver resolver;
    
    /** The field validator. */
    @Autowired
    private FieldValidator          fieldValidator;
    
    /** The error resolver. */
    @Autowired
    private GalaxyErrorCodeResolver errorResolver;
    
    /** The logger. */
    @Autowired
    private LoggerDAO logger;
    
    @Autowired
    private TerminateArrangementService terminateArrangementService;
    
    public TerminateArrangementResource() {
        /* required for trace logging */
    }
    
    /**
     * Gets aqcu cases count details.
     *
     * @param requestBody
     *            for getCase service.
     * @return aqcu cases count details.
     * @throws ServiceException
     *             in case for any business and system failures.
     * @throws InvalidFormatException
     *             for invalid request body.
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response terminateArrangement(String requestBody) throws InvalidFormatException {
        TerminateArrangement terminateArrangement = resolver.resolve(requestBody, TerminateArrangement.class);
        ValidationError validationError = validateRequest(terminateArrangement);
        if(validationError!=null){
            throw new ServiceException(errorResolver.customResolve(ResponseErrorConstants.RETRIEVE_PENDING_ARRANGEMENT_SERVICE_EXCEPTION,validationError.getMessage()));
        }
        
        return respond(Status.OK, terminateArrangementService.terminateArrangement(terminateArrangement));
    }
    
    
    private ValidationError validateRequest(TerminateArrangement terminateArrangement) throws InvalidFormatException {
        return fieldValidator.validateInstanceFields(terminateArrangement);
    }
    
    
    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
    
}
