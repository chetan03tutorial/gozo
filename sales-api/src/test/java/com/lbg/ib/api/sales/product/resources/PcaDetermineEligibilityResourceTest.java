/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.rules.PcaDetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.service.PcaDetermineEligibilityService;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

@RunWith(MockitoJUnitRunner.class)
public class PcaDetermineEligibilityResourceTest {

    private PcaDetermineEligibilityService       service                         = mock(
            PcaDetermineEligibilityService.class);
    private GalaxyErrorCodeResolver              errorResolver                   = mock(GalaxyErrorCodeResolver.class);
    private RequestBodyResolver                  resolver                        = mock(RequestBodyResolver.class);
    private PrimaryInvolvedParty                 primaryInvolvedParty            = mock(PrimaryInvolvedParty.class);
    private FieldValidator                       fieldValidator                  = mock(FieldValidator.class);
    private PcaDetermineEligibilityRuleValidator ruleValidator                   = mock(
            PcaDetermineEligibilityRuleValidator.class);

    private PcaDetermineEligibilityResource      pcaDetermineEligibilityResource = new PcaDetermineEligibilityResource(
            service, resolver, errorResolver, ruleValidator);

    @Mock
    private LoggerDAO                            logger                          = mock(LoggerDAO.class);

    @Before
    public void setup() {
        pcaDetermineEligibilityResource.setLogger(logger);

    }

    @Test
    public void shouldReturn200WhenServiceisUnavailable() throws Exception {
        PcaDetermineEligibilityResource dummy = new PcaDetermineEligibilityResource();
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.SA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);

        Response response = pcaDetermineEligibilityResource.determineEligibility(requestString);
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturn200WhenServiceisAvailable() throws Exception {
        PcaDetermineEligibilityResource dummy = new PcaDetermineEligibilityResource();
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        when(service.determineEligiblitySuitability(request)).thenReturn(new PcaDetermineEligibilityResponse());

        Response response = pcaDetermineEligibilityResource.determineEligibility(requestString);
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturn200WhenServiceisAvailableRequestInvalid() throws Exception {
        PcaDetermineEligibilityResource dummy = new PcaDetermineEligibilityResource();
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        when(service.determineEligiblitySuitability(request)).thenReturn(new PcaDetermineEligibilityResponse());
        pcaDetermineEligibilityResource.determineEligibility(requestString);

    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturn200WhenServiceisAvailableRequestInvalidValidationError() throws Exception {
        PcaDetermineEligibilityResource dummy = new PcaDetermineEligibilityResource();
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        when(service.determineEligiblitySuitability(request)).thenReturn(new PcaDetermineEligibilityResponse());
        when(ruleValidator.validateRules(any(PcaDetermineEligibilityRequest.class)))
                .thenReturn(new ValidationError("Validation Error"));
        pcaDetermineEligibilityResource.determineEligibility(requestString);

    }

    @Test
    public void shouldReturn400WhenRequestisInvalid() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        // request.setDob(new GregorianCalendar().getTime());
        request.setArrangementType(AccountType.SA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        /*
         * when(fieldValidator.validateInstanceFields(request,
         * request.getArrangementType())) .thenReturn(new ValidationError(
         * "Error validting fields"));
         */

        try {
            pcaDetermineEligibilityResource.determineEligibility(requestString);
        } catch (Exception ex) {
            Assert.notNull(ex.getMessage());
        }

    }

    @Test
    public void shouldReturn400WhenInvalidDateRequestisInvalid() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"SA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        primaryInvolvedParty.setDob("1985-12-17");
        request.setArrangementType(AccountType.SA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        try {
            pcaDetermineEligibilityResource.determineEligibility(requestString);
        } catch (Exception ex) {
            Assert.notNull(ex.getMessage());
        }

    }

    private PcaDetermineEligibilityResponse getValidResponse() {
        PcaDetermineEligibilityResponse response = new PcaDetermineEligibilityResponse();
        response.setMsg("Successfully fetched the eligibility details.");
        List<EligibilityDetails> eligibilityDetailsList = new ArrayList<EligibilityDetails>();
        EligibilityDetails details = new EligibilityDetails();
        details.setMnemonic("P_REWARD");
        details.setIsEligible(true);

        eligibilityDetailsList.add(details);
        response.setProducts(eligibilityDetailsList);
        response.setSuitableProducts(new ArrayList<String>());
        return response;
    }

}