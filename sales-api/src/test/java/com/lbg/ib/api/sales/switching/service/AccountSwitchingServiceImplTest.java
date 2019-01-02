package com.lbg.ib.api.sales.switching.service;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingResponse;

@RunWith(MockitoJUnitRunner.class)
public class AccountSwitchingServiceImplTest {

    @InjectMocks 
    AccountSwitchingServiceImpl service;
    
    @Mock
    private PEGAService accountSwitchingPEGAService;
    
    @Mock
    AccountSwitchingRequest accountSwitchingRequest;
    
    @Test
    public void testSwitchAccount()
    {
        when(accountSwitchingPEGAService.createPegaCase(accountSwitchingRequest)).thenReturn(Mockito.anyString());
        AccountSwitchingResponse response = service.switchAccount(accountSwitchingRequest);
        assertNotNull(response);
    }
}
