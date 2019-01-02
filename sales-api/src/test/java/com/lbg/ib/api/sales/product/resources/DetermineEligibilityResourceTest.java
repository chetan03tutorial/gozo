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
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.common.validation.ValidationException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.rules.DetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.rules.PcaDetermineEligibilityRuleValidator;
import com.lbg.ib.api.sales.product.service.DetermineEligibilityService;
import com.lbg.ib.api.sales.product.service.PcaDetermineEligibilityService;
import com.lbg.ib.api.sales.utils.CommonUtils;

@RunWith(MockitoJUnitRunner.class)
public class DetermineEligibilityResourceTest {

    private DetermineEligibilityService          service                      = mock(DetermineEligibilityService.class);
    private GalaxyErrorCodeResolver              errorResolver                = mock(GalaxyErrorCodeResolver.class);
    private RequestBodyResolver                  resolver                     = mock(RequestBodyResolver.class);
    private PrimaryInvolvedParty                 primaryInvolvedParty         = mock(PrimaryInvolvedParty.class);
    private FieldValidator                       fieldValidator               = mock(FieldValidator.class);
    private DetermineEligibilityRuleValidator    ruleValidator                = mock(
            DetermineEligibilityRuleValidator.class);
    private PcaDetermineEligibilityRuleValidator authRuleValidator            = mock(
            PcaDetermineEligibilityRuleValidator.class);
    private CommonUtils                          commonUtils                  = mock(CommonUtils.class);
    private SessionManagementDAO                 session                      = mock(SessionManagementDAO.class);
    private PcaDetermineEligibilityService       authService                  = mock(
            PcaDetermineEligibilityService.class);
    private LoggerDAO                            logger                       = mock(LoggerDAO.class);

    private DetermineEligibilityResource         determineEligibilityResource = new DetermineEligibilityResource(
            service, resolver, fieldValidator, errorResolver, ruleValidator, commonUtils, session, authRuleValidator,
            authService, logger);

    @Test
    public void shouldReturn200WhenServiceisUnavailable() throws Exception {
        DetermineEligibilityResource resource = new DetermineEligibilityResource();
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);
        Response response = determineEligibilityResource.determineEligibility(requestString);
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

    @Test(expected = InvalidFormatException.class)
    public void testValidationsForArrangementType() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(null);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);
        determineEligibilityResource.determineEligibility(requestString);

    }

    @Test(expected = InvalidFormatException.class)
    public void testValidationsForMissingFileds() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);
        when(ruleValidator.validateRules(null)).thenReturn(new ValidationError("error"));
        Response response = determineEligibilityResource.determineEligibility(requestString);

    }

    @Test
    public void shouldReturn400WhenRequestisInvalid() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        // request.setDob(new GregorianCalendar().getTime());
        request.setPrimaryInvolvedParty(primaryInvolvedParty);
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);
        when(fieldValidator.validateInstanceFields(request, request.getArrangementType()))
                .thenThrow(new ValidationException(new ValidationError("Error validting fields")));

        try {
            determineEligibilityResource.determineEligibility(requestString);
        } catch (Exception ex) {
            Assert.notNull(ex.getMessage());
        }

    }

    @Test
    public void shouldReturn400WhenInvalidDateRequestisInvalid() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setPrimaryInvolvedParty(primaryInvolvedParty);
        primaryInvolvedParty.setDob(new GregorianCalendar().getTime());
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);

        try {
            determineEligibilityResource.determineEligibility(requestString);
        } catch (Exception ex) {
            Assert.notNull(ex.getMessage());
        }

    }

    @Test
    public void shouldReturnEligibilityDetailsWhenServiceisAvailable() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setPrimaryInvolvedParty(primaryInvolvedParty);
        primaryInvolvedParty.setDob(new GregorianCalendar().getTime());
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, DetermineEligibilityRequest.class)).thenReturn(request);
        DetermineEligibilityResponse expectedResponse = getValidResponse();
        when(service.determineEligiblity(request)).thenReturn(expectedResponse);
        Response response = determineEligibilityResource.determineEligibility(requestString);
        assertThat(response.getStatus(), is(200));
        assertThat(((DetermineEligibilityResponse) response.getEntity()).getMsg(),
                is("Successfully fetched the eligibility details."));
        Assert.notNull(
                ((DetermineEligibilityResponse) response.getEntity()).getEligibilityDetails().get(0).getMnemonic());
        Assert.notNull(
                ((DetermineEligibilityResponse) response.getEntity()).getEligibilityDetails().get(0).getIsEligible());

    }

    @Test
    public void shouldReturnEligibilityDetailsWhenServiceisAvailableinAuth() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();

        request.setDob("1983-01-01");
        request.setArrangementType(AccountType.CA);
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        DetermineEligibilityResponse expectedResponse = getValidResponse();
        when(authService.determineAuthEligiblity(any(PcaDetermineEligibilityRequest.class)))
                .thenReturn(expectedResponse);
        when(commonUtils.isAuth(any(SessionManagementDAO.class))).thenReturn(true);
        Response response = determineEligibilityResource.determineEligibility(requestString);
        assertThat(response.getStatus(), is(200));
        assertThat(((DetermineEligibilityResponse) response.getEntity()).getMsg(),
                is("Successfully fetched the eligibility details."));
        Assert.notNull(
                ((DetermineEligibilityResponse) response.getEntity()).getEligibilityDetails().get(0).getMnemonic());
        Assert.notNull(
                ((DetermineEligibilityResponse) response.getEntity()).getEligibilityDetails().get(0).getIsEligible());

    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnEligibilityDetailsWhenServiceisAvailableinAuthWithArrangementTypeNull() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();

        request.setDob("1983-01-01");
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        DetermineEligibilityResponse expectedResponse = getValidResponse();
        when(authService.determineAuthEligiblity(any(PcaDetermineEligibilityRequest.class)))
                .thenReturn(expectedResponse);
        when(commonUtils.isAuth(any(SessionManagementDAO.class))).thenReturn(true);
        determineEligibilityResource.determineEligibility(requestString);
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnEligibilityDetailsWhenServiceisAvailableinAuthWithValidationError() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        request.setDob("1983-01-01");
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        DetermineEligibilityResponse expectedResponse = getValidResponse();
        when(authService.determineAuthEligiblity(any(PcaDetermineEligibilityRequest.class)))
                .thenReturn(expectedResponse);
        when(commonUtils.isAuth(any(SessionManagementDAO.class))).thenReturn(true);
        when(authRuleValidator.validateRules(any(PcaDetermineEligibilityRequest.class), any(Boolean.class)))
                .thenReturn(new ValidationError("Validation Error"));
        determineEligibilityResource.determineEligibility(requestString);
    }

    @Test
    public void shouldReturnEligibilityDetailsWhenServiceisAvailableisNull() throws Exception {
        String requestString = "{ \"dob\":\"1935-01-01\",\"arrangementType\":\"CA\"}";
        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        request.setDob("1983-01-01");
        when(resolver.resolve(requestString, PcaDetermineEligibilityRequest.class)).thenReturn(request);
        when(authService.determineAuthEligiblity(any(PcaDetermineEligibilityRequest.class))).thenReturn(null);
        when(commonUtils.isAuth(any(SessionManagementDAO.class))).thenReturn(true);
        when(errorResolver.createResponseError(any(String.class))).thenReturn(new ResponseError());
        determineEligibilityResource.determineEligibility(requestString);
    }

    private DetermineEligibilityResponse getValidResponse() {
        DetermineEligibilityResponse response = new DetermineEligibilityResponse();
        response.setMsg("Successfully fetched the eligibility details.");
        List<EligibilityDetails> eligibilityDetailsList = new ArrayList<EligibilityDetails>();
        EligibilityDetails details = new EligibilityDetails();
        details.setMnemonic("P_REWARD");
        details.setIsEligible(true);
        eligibilityDetailsList.add(details);
        response.setEligibilityDetails(eligibilityDetailsList);
        return response;
    }

}