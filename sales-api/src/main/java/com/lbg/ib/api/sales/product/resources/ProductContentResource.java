package com.lbg.ib.api.sales.product.resources;

import static com.lbg.ib.api.sales.dao.content.ContentDAOImpl.CONTENT_NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.product.service.ProductContentService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Path("/product/content")
public class ProductContentResource {

    private ProductContentService productContentService;
    private GalaxyErrorCodeResolver errorResolver;
    private LoggerDAO logger;

    public ProductContentResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public ProductContentResource(ProductContentService productContentService, GalaxyErrorCodeResolver errorResolver,
            LoggerDAO logger) {
        this.productContentService = productContentService;
        this.errorResolver = errorResolver;
        this.logger = logger;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response content(@QueryParam("path") String path) throws ReferenceDataException {
        logger.traceLog(this.getClass(), "Fetching Product Content Resource");
        Map<String, String> responseMap = productContentService.getAllProductContent(path);
        if (responseMap != null && !responseMap.isEmpty()) {
            logger.traceLog(this.getClass(), "Successful Fetching Product Content Resource");
            return respond(Status.OK, responseMap);
        }

        logger.traceLog(this.getClass(), "Not Successful Fetching Product Content Resource");

        return respond(Status.OK, errorResolver.createResponseError(CONTENT_NOT_FOUND));

    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}
