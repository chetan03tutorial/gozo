package com.lbg.ib.api.sales.docupload.error;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.exceptions.runtime.ApplicationRuntimeException;

@RunWith(MockitoJUnitRunner.class)
public class ResponseErrorCodeMapperTest {
    
    @InjectMocks
    ResponseErrorCodeMapper errorCodeMapper;
    
    @Mock
    private ConfigurationService configurationService;
    
    @Test
    public void shouldResolveWithValidResponse() {
        when(configurationService.getConfigurationValueAsString(any(String.class), any(String.class)))
                .thenReturn("1001");
        ResponseErrorVO responseErrorVO = errorCodeMapper.resolve("errorCode");
        assertTrue(responseErrorVO != null);
        assertTrue(responseErrorVO.getCode() == "1001");
        assertTrue(responseErrorVO.getErrorStatus() == 200);
        assertTrue(responseErrorVO.getMessage() != null);
        
        responseErrorVO = new ResponseErrorVO();
        
    }
    
    @Test(expected = ApplicationRuntimeException.class)
    public void shouldResolveWithResponseCodeAsString() {
        when(configurationService.getConfigurationValueAsString(any(String.class), any(String.class)))
                .thenReturn("TESTING");
        errorCodeMapper.resolve("errorCode");
    }
    
    @Test
    public void shouldResolveWithResponseCodeAsNull() {
        when(configurationService.getConfigurationValueAsString(any(String.class), any(String.class))).thenReturn(null);
        try {
            errorCodeMapper.resolve("errorCode");
        } catch (Exception e) {
            
        }
        
    }
}
