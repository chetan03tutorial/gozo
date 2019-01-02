/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.conversion.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionRequest;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionResponse;
import com.lbg.ib.api.sales.conversion.service.ConversionService;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

/**
 * Contain Conversion upgrade API's.
 * @author tkhann/amit/cshar8
 */
@Path("/account")
public class ConversionResource extends BaseResource {

    @Autowired
    private RequestBodyResolver resolver;
    /**
     * Object of moduleContext.
     */
    @Autowired
    private ModuleContext moduleContext;

    /**
     * API to convert product of account.
     * @param requestBody
     * @return
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/conversion")
    @TraceLog
    public Response upgradeAccount(String requestBody, @QueryParam("idType") @DefaultValue("Q226") String idType) {
        AccountConversionRequest userRequest = resolver.resolve(requestBody, AccountConversionRequest.class);
        ConversionService conversionService = moduleContext.getService(ConversionService.class);
        AccountConversionResponse response = conversionService.convert(userRequest, idType);
        return respond(Response.Status.OK, response);
    }

}
