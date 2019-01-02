package com.lbg.ib.api.sales.common.resource;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Base class for All REST Resources.
 * Contains re-usable methods common to all resources
 */
public abstract class BaseResource {

    public Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    public Response respond(Response.Status status, Object content, Map<String, Object> headerMap) {
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        builder.status(status);
        builder.entity(content);
        for(String headerKey : headerMap.keySet()){
            builder.header(headerKey, headerMap.get(headerKey));
        }
        return builder.build();
    }

    public Response respond(Response.Status status) {
        return new ResponseBuilderImpl().status(status).build();
    }
}
