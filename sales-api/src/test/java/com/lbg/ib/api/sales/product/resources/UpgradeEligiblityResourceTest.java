package com.lbg.ib.api.sales.product.resources;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityResponse;
import com.lbg.ib.api.sales.product.service.UpgradeEligibilityService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.shared.validation.AccountType;

@RunWith(MockitoJUnitRunner.class)
public class UpgradeEligiblityResourceTest {

    @InjectMocks
    private UpgradeEligibilityResource resource;

    @Mock
    private UpgradeEligibilityService service;
    
    @Mock
    private RequestBodyResolver resolver;
    
    @Mock
    private GalaxyErrorCodeResolver errorResolver;

    @Mock
    private LoggerDAO logger;
    
    @Mock
    UpgradeEligibilityRequest upgradeEligibilityRequest;
    
    @Mock
    UpgradeEligibilityResponse response;

    @Mock
    ResponseError responseError;
    
    @Before
    public void setup() {
        when(errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE)).thenReturn(responseError);
    }
    
    @Test
    public void testCheckUpgradeEligibilityNotNullResponse()
    {
        UpgradeEligibilityRequest upgradeEligibilityRequest = new UpgradeEligibilityRequest();
        upgradeEligibilityRequest.setAccountNumber("12391123");
        upgradeEligibilityRequest.setSortCode("110264");
        upgradeEligibilityRequest.setArrangementType(AccountType.CA);
        
        when(resolver.resolve(Mockito.anyString(), eq(UpgradeEligibilityRequest.class))).thenReturn(upgradeEligibilityRequest);
        
        when(service.upgradeEligibility(upgradeEligibilityRequest)).thenReturn(response);
        
        String requestBody = new String("requestBody");
        Response response = resource.checkUpgradeEligibility(requestBody);
        
        assertNotNull(response);
    }
    
    @Test
    public void testCheckUpgradeEligibilityNullResponse()
    {
        UpgradeEligibilityRequest upgradeEligibilityRequest = new UpgradeEligibilityRequest();
        upgradeEligibilityRequest.setAccountNumber("12391123");
        upgradeEligibilityRequest.setSortCode("110264");
        upgradeEligibilityRequest.setArrangementType(AccountType.CA);
        
        when(resolver.resolve(Mockito.anyString(), eq(UpgradeEligibilityRequest.class))).thenReturn(upgradeEligibilityRequest);
        
        when(service.upgradeEligibility(upgradeEligibilityRequest)).thenReturn(null);
        
        String requestBody = new String("requestBody");
        Response errorResponse = resource.checkUpgradeEligibility(requestBody);
        assertNotNull(errorResponse);
    }
}
