package com.lbg.ib.api.sales.address.resources;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.ADDRESS_NOT_FOUND;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.address.domain.Postcode;
import com.lbg.ib.api.sales.address.domain.RawPostCode;
import com.lbg.ib.api.sales.address.service.AddressService;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@Path("/address")
public class AddressResource {

    private RequestBodyResolver resolver;

    private AddressService      addressService;

    public AddressResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public AddressResource(AddressService addressService, RequestBodyResolver resolver) {
        this.addressService = addressService;
        this.resolver = resolver;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response addressSearch(String requestBody) throws InvalidFormatException {
        RawPostCode rawPostCode = resolver.resolve(requestBody, RawPostCode.class);
        List<PostalAddress> postalAddresses = addressService.check(new Postcode(rawPostCode.getPostCode()));
        if (postalAddresses == null || postalAddresses.isEmpty()) {
            return respond(Status.OK, new ResponseError(ADDRESS_NOT_FOUND,
                    format("Cannot find matching address for '%s'", rawPostCode.getPostCode())));
        }
        return respond(Status.OK, postalAddresses);
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}