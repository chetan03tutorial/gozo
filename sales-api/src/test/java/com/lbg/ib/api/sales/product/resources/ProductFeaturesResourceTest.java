package com.lbg.ib.api.sales.product.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;

public class ProductFeaturesResourceTest {

    @InjectMocks
    ProductFeaturesResource                resource;

    @Mock
    private RetrieveProductFeaturesService service;

    @Mock
    private GalaxyErrorCodeResolver        errorResolver;

    @Mock
    private FieldValidator                 fieldValidator;

    private static ProductFeatures         PRODUCT_FEATURES;

    private static final ValidationError   error = new ValidationError("Invalid type identifier");

    private static final ProductReference REFERENCE = new ProductReference("ref12_");

    private static ProductFeatures productFeature() {
        if (PRODUCT_FEATURES == null)
            PRODUCT_FEATURES = new ProductFeatures(new Product("n", "id", null, "pdt", null, null), null);
        return PRODUCT_FEATURES;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productFeature();
    }


    @Test
    public void testSuccessfulRetrivalOfProductByProductId() throws Exception {
        when(service.retrieveProductFeaturesUsingProductId(REFERENCE)).thenReturn(product());
        Response result = resource.productFeatures(REFERENCE);
        assertEquals(Status.OK.getStatusCode(), result.getStatus());
    }

    @Test
    public void testFailureInRetrivalOfProductByProductId() throws Exception {
        when(service.retrieveProductFeaturesUsingProductId(REFERENCE)).thenReturn(null);
        when(errorResolver.createResponseError(anyString())).thenReturn(new ResponseError());
        Response result = resource.productFeatures(REFERENCE);
        assertEquals(Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    @Test
    public void testProductFoundWhenUrlIdentifierIsPassed() throws ServiceException, InvalidFormatException {
        ProductFeaturesResource productFeatures = new ProductFeaturesResource();
        when(service.retrieveProductFeatures(any(ProductReference.class))).thenReturn(PRODUCT_FEATURES);
        Response result = resource.productFeature("3001");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((ProductFeatures) result.getEntity(), is(PRODUCT_FEATURES));
    }

    @Test
    public void testProductFoundWhenProductIdentifierIsPassed() throws ServiceException, InvalidFormatException {
        when(service.retrieveProductFeaturesByProductId(any(ProductReference.class))).thenReturn(PRODUCT_FEATURES);
        Response result = resource.productFeatureById("3001");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((ProductFeatures) result.getEntity(), is(PRODUCT_FEATURES));
    }

    @Test
    public void testProductNotFoundWhenUrlIdentifierIsPassed() throws Exception {
        when(service.retrieveProductFeatures(any(ProductReference.class))).thenReturn(null);
        Response result = new ProductFeaturesResource(service, fieldValidator, errorResolver).productFeature("3001");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((ResponseError) result.getEntity(),
                is(errorResolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND)));
    }

    @Test
    public void testProductNotFoundWhenProductIdentifierIsPassed() throws ServiceException, InvalidFormatException {
        when(service.retrieveProductFeaturesByProductId(any(ProductReference.class))).thenReturn(null);
        Response result = resource.productFeatureById("3004");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((ResponseError) result.getEntity(),
                is(errorResolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND)));
    }

    @Test(expected = InvalidFormatException.class)
    public void testBadRequestFormat() throws Exception {
        when(fieldValidator.validateInstanceFields(any(ProductReference.class))).thenReturn(error);
        resource.productFeature("3001");
    }

    @Test
    public void testBadRequestWithNullSearchtype() throws ServiceException, InvalidFormatException {
        resource.productFeature("3001");
        verify(service, times(1)).retrieveProductFeatures(any(ProductReference.class));
    }

    private ProductFeatures product() {
        ProductFeatures product = new ProductFeatures();
        return product;
    }

}