package com.lbg.ib.api.sales.overdraft.resource;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftRequest;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftResponse;
import com.lbg.ib.api.sales.overdraft.resource.UpfrontOverdraftResource;
import com.lbg.ib.api.sales.overdraft.service.UpfrontOverdraftService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.shared.validation.AccountType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpfrontOverdraftResourceTest {

    @InjectMocks
    private UpfrontOverdraftResource resource;

    @Mock
    private UpfrontOverdraftService service;
    
    @Mock
    private RequestBodyResolver resolver;
    
    @Mock
    private GalaxyErrorCodeResolver errorResolver;

    @Mock
    private LoggerDAO logger;
    
    @Mock
    UpfrontOverdraftRequest upfrontOverdraftRequest;
    
    @Mock
    UpfrontOverdraftResponse response;

    @Mock
    ResponseError responseError;
    
    @Before
    public void setup() {
        when(errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE)).thenReturn(responseError);
    }
    
    @Test
    public void testCheckUpgradeEligibilityNotNullResponse()
    {
        UpfrontOverdraftRequest upfrontOverdraftRequest = new UpfrontOverdraftRequest();
        upfrontOverdraftRequest.setAccountNumber("12391123");
        upfrontOverdraftRequest.setSortCode("110264");

        when(resolver.resolve(Mockito.anyString(), eq(UpfrontOverdraftRequest.class))).thenReturn(upfrontOverdraftRequest);
        
        when(service.upfrontOverdraft(upfrontOverdraftRequest)).thenReturn(response);
        
        String requestBody = new String("requestBody");
        Response response = resource.checkUpfrontOverdraft(requestBody);
        
        assertNotNull(response);
    }
    
    @Test
    public void testCheckUpgradeEligibilityNullResponse()
    {
        UpfrontOverdraftRequest upfrontOverdraftRequest = new UpfrontOverdraftRequest();
        upfrontOverdraftRequest.setAccountNumber("12391123");
        upfrontOverdraftRequest.setSortCode("110264");

        when(resolver.resolve(Mockito.anyString(), eq(UpfrontOverdraftRequest.class))).thenReturn(upfrontOverdraftRequest);
        
        when(service.upfrontOverdraft(upfrontOverdraftRequest)).thenReturn(null);
        
        String requestBody = new String("requestBody");
        Response errorResponse = resource.checkUpfrontOverdraft(requestBody);
        assertNotNull(errorResponse);
    }
}
