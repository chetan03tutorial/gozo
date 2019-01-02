package com.lbg.ib.api.sales.product.resources;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangementResponse;
import com.lbg.ib.api.sales.terminate.service.TerminateArrangementService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;


@RunWith(MockitoJUnitRunner.class)
public class TerminateArrangementResourceTest {
    @Mock
    private FieldValidator                         fieldValidator;
    
    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    
    @Mock
    private LoggerDAO logger;
    
    @Mock
    private RequestBodyResolver resolver;
    
    @Mock
    TerminateArrangementService terminateArrangementService;
    
    @InjectMocks
    private TerminateArrangementResource       resource;
    
    @Test(expected=ServiceException.class)
    public void testForTerminateArrangementWhereArrangementIdIsMissing() throws Exception {
        TerminateArrangement terminateArrangement = new TerminateArrangement();
        terminateArrangement.setUserId(null);
        when(resolver.resolve(any(String.class), eq(TerminateArrangement.class)))
                .thenReturn(terminateArrangement);
        
        when(fieldValidator.validateInstanceFields(terminateArrangement)).thenReturn(new ValidationError("'arrangementId' required to be set but it was 'null'"));
        Response result = resource.terminateArrangement("dummy_Request");
        assertTrue(result.getStatus()==200);
        
    }
    
    @Test
    public void testForTerminateArrangementWithValidRequest() throws Exception {
        TerminateArrangement terminateArrangement = new TerminateArrangement("30082677","CT067484","Cancel","110");
        
        TerminateArrangementResponse terminateArrangementResponse = new TerminateArrangementResponse();
        terminateArrangementResponse.setArrangementId("30082677");
        when(resolver.resolve(any(String.class), eq(TerminateArrangement.class)))
                .thenReturn(terminateArrangement);
        when(terminateArrangementService.terminateArrangement(terminateArrangement)).thenReturn(terminateArrangementResponse);
        Response result = resource.terminateArrangement("dummy_Request");
        assertTrue(result.getStatus()==200);
        assertTrue(((TerminateArrangementResponse)result.getEntity()).getArrangementId().equals("30082677"));
    }
    
    
}
