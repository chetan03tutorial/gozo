/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.party.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.party.domain.Classifications;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.party.service.TinDetailsService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@RunWith(MockitoJUnitRunner.class)
public class TinDetailsResourceTest {

    @InjectMocks
    private TinDetailsResource                 resource;

    @Mock
    private TinDetailsService                  service;

    @Mock
    private RequestBodyResolver                resolver;

    @Mock
    private FieldValidator                     fieldValidator;

    @Mock
    private GalaxyErrorCodeResolver            errorResolver;

    private static final String                REQUEST         = "request";

    private TinDetails                         TIN_DETAILS     = new TinDetails();

    private List<ClassifiedPartyDetails>       PARTY_DETAILS   = new ArrayList<ClassifiedPartyDetails>();

    private Classifications                    CLASSIFICATIONS = new Classifications();

    private LinkedHashSet<String>              nationalities   = new LinkedHashSet<String>();

    private LinkedHashSet<TaxResidencyDetails> taxResidencies  = new LinkedHashSet<TaxResidencyDetails>();

    @Test
    public void shouldReturnClassifiedPartyDetailsWhenServiceReturnsResponse() throws Exception {
        TIN_DETAILS.setBirthCountry("GBR");
        nationalities.add("IND");
        TIN_DETAILS.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("ALA");
        TIN_DETAILS.setTaxResidencies(taxResidencies);
        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
        classifiedPartyDetails.setCountryName("ALA");
        classifiedPartyDetails.setTinRequired(false);
        classifiedPartyDetails.setRegex(null);
        PARTY_DETAILS.add(classifiedPartyDetails);
        CLASSIFICATIONS.setClassifications(PARTY_DETAILS);

        when(resolver.resolve(any(String.class), eq(TinDetails.class))).thenReturn(TIN_DETAILS);
        when(service.identify(TIN_DETAILS)).thenReturn(PARTY_DETAILS);

        Response result = resource.taxIdentify(REQUEST);

        assertThat((Classifications) result.getEntity(), is(CLASSIFICATIONS));
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowExceptionWhenValidationErrorReturns() throws Exception {
        // TIN_DETAILS.setBirthCountry("GBR");
        nationalities.add("IND");
        TIN_DETAILS.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("ALA");
        TIN_DETAILS.setTaxResidencies(taxResidencies);
        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
        classifiedPartyDetails.setCountryName("ALA");
        classifiedPartyDetails.setTinRequired(false);
        classifiedPartyDetails.setRegex(null);
        PARTY_DETAILS.add(classifiedPartyDetails);
        CLASSIFICATIONS.setClassifications(PARTY_DETAILS);
        ValidationError error = new ValidationError("error");

        when(resolver.resolve(any(String.class), eq(TinDetails.class))).thenReturn(TIN_DETAILS);
        when(service.identify(TIN_DETAILS)).thenReturn(PARTY_DETAILS);

        when(fieldValidator.validateInstanceFields(any(Object.class))).thenReturn(error);

        resource.taxIdentify(REQUEST);
    }

    @Test
    public void shouldReturnServiceUnavailableWhenServiceReturnsNull() throws Exception {
        when(resolver.resolve(any(String.class), eq(TinDetails.class))).thenReturn(TIN_DETAILS);
        when(service.identify(TIN_DETAILS)).thenReturn(null);
        ResponseError result = (ResponseError) resource.taxIdentify(REQUEST).getEntity();

        assertThat(result, is(errorResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE)));
    }
}
