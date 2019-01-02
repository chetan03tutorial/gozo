package com.lbg.ib.api.sales.product.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.mockito.Mockito;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
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
public class RetrieveProductArrangementResourceTest {

    public static final CustomerPendingDetails PENDING_PRODUCT_ARRANGEMENT = new CustomerPendingDetails(null, "ArrType",
            "121366", "1003", null, null, "dob", "gender", "town", "countryOfBirth", null, "email", null) {
                                                                           };
    private static final ArrangementId         Arrangement                 = new ArrangementId("121366");
    private static final ArrangementId         INVALID_Arrangement         = new ArrangementId("999999sfsadfsd");
    private RetrieveProductArrangementService  service                     = mock(
            RetrieveProductArrangementService.class);
    private GalaxyErrorCodeResolver            errorResolver               = mock(GalaxyErrorCodeResolver.class);
    private FieldValidator                     fieldValidator              = mock(FieldValidator.class);

    @Test
    public void shouldReturnNotFoundWhenTheProductRetrieverServiceReturnsNull() throws Exception {
        RetrieveProductArrangementResource resource = new RetrieveProductArrangementResource();
        when(service.retrieveProductArrangement(Arrangement)).thenReturn(null);
        when(fieldValidator.validateInstanceFields(Mockito.any(ArrangementId.class))).thenReturn(null);
        resource = new RetrieveProductArrangementResource(service, fieldValidator, errorResolver);
        Response result = resource.retrieveProductArrangement(Arrangement);

        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((ResponseError) result.getEntity(),
                is(errorResolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND)));
    }

    @Test
    public void shouldReturnProductArrangementWhenSALSAServiceReturnsArrangment() throws Exception {
        when(service.retrieveProductArrangement(Arrangement)).thenReturn(PENDING_PRODUCT_ARRANGEMENT);
        when(fieldValidator.validateInstanceFields(Mockito.any(ArrangementId.class))).thenReturn(null);
        Response result = new RetrieveProductArrangementResource(service, fieldValidator, errorResolver)
                .retrieveProductArrangement(Arrangement);

        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        // assertThat((CustomerPendingDetails) result.getEntity(),
        // is(PENDING_PRODUCT_ARRANGEMENT));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowIllegalFormatExceptionWhenProductReferenceIsInInvalidFormat() throws Exception {
        when(fieldValidator.validateInstanceFields(Mockito.any(ArrangementId.class)))
                .thenReturn(new ValidationError("error"));
        new RetrieveProductArrangementResource(service, fieldValidator, errorResolver)
                .retrieveProductArrangement(INVALID_Arrangement);
    }
}
