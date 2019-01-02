package com.lbg.ib.api.sales.product.resources;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.PRODUCT_NOT_FOUND;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;

import java.util.List;

@Path("/product/features")
public class ProductFeaturesResource {

    private RetrieveProductFeaturesService service;
    private FieldValidator                 fieldValidator;
    private GalaxyErrorCodeResolver        errorResolver;

    public ProductFeaturesResource() {
        /* required for trace logging */}

    @Autowired
    public ProductFeaturesResource(RetrieveProductFeaturesService service, FieldValidator fieldValidator,
            GalaxyErrorCodeResolver errorResolver) {
        this.service = service;
        this.fieldValidator = fieldValidator;
        this.errorResolver = errorResolver;
    }

    @GET
    @Produces("application/json")
    @Path("/productFamily/{productFamilyId}") // {searchType:(/searchType/[^/]+?)?}
    @TraceLog
    public Response fetchProductsFromFamily(@PathParam("productFamilyId") String productFamilyId) throws InvalidFormatException {
        RetrieveProductDTO retrieveProduct  = new RetrieveProductDTO(null,productFamilyId);

        RetrieveProductConditionsResponse response =  service.retrieveProductFromFamily(retrieveProduct);
        return respond(Status.OK, response);
    }


    @GET
    @Produces("application/json")
    @Path("/{searchKey}") // {searchType:(/searchType/[^/]+?)?}
    @TraceLog
    public Response productFeature(@PathParam("searchKey") String searchKey) throws InvalidFormatException {

        ProductFeatures productFeatures = null;
        ProductReference reference = new ProductReference(searchKey);
        validate(reference);
        productFeatures = service.retrieveProductFeatures(reference);
        if (productFeatures != null) {
            return respond(Status.OK, productFeatures);
        }
        return respond(Status.OK, errorResolver.createResponseError(PRODUCT_NOT_FOUND));
    }

    @GET
    @Produces("application/json")
    @Path("/{searchKey}/idType") // {searchType:(/searchType/[^/]+?)?}
    @TraceLog
    public Response productFeatureById(@PathParam("searchKey") String searchKey) throws InvalidFormatException {

        ProductFeatures productFeatures = null;
        ProductReference reference = new ProductReference(searchKey);
        validate(reference);
        productFeatures = service.retrieveProductFeaturesByProductId(reference);

        if (productFeatures != null) {
            return respond(Status.OK, productFeatures);
        }
        return respond(Status.OK, errorResolver.createResponseError(PRODUCT_NOT_FOUND));
    }

    @GET
    @Produces("application/json")
    @Path("/productId/{productId}")
    @TraceLog
    public Response productFeatures(@PathParam("productId") ProductReference reference) throws Exception {
        validate(reference);
        ProductFeatures productFeatures = service.retrieveProductFeaturesUsingProductId(reference);
        if (productFeatures != null) {
            return respond(Status.OK, productFeatures);
        }
        return respond(Status.NOT_FOUND, errorResolver.createResponseError(PRODUCT_NOT_FOUND));
    }

    private void validate(ProductReference reference) throws InvalidFormatException {
        ValidationError validationError = fieldValidator.validateInstanceFields(reference);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
