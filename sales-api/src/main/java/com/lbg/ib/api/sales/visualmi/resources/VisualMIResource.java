package com.lbg.ib.api.sales.visualmi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

@Path("/visualMI/{path :.*}")
public class VisualMIResource {
    private static final int SC_OK = 200;

    @GET
    public Response getRequest() {
        return new ResponseBuilderImpl().status(SC_OK).build();
    }

    @POST
    public Response postRequest(String requestBody) {
        return new ResponseBuilderImpl().status(SC_OK).build();
    }
}
