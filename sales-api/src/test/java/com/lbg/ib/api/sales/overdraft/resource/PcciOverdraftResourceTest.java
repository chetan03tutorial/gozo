package com.lbg.ib.api.sales.overdraft.resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.sales.overdraft.service.PcciService;
import com.lbg.ib.api.sales.overdraft.service.PcciServiceImpl;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@RunWith(MockitoJUnitRunner.class)
public class PcciOverdraftResourceTest {

    @InjectMocks
    private PcciOverdraftResource overdraftResource;

    @Mock
    private PcciServiceImpl pcciService;

    @Mock
    private ModuleContext beansLoader;
    
    @Mock
    RequestBodyResolver     resolver;

    @Mock
    private LoggerDAO logger;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(beansLoader.getService(PcciService.class)).thenReturn(pcciService);
    }

    @Test
    public void upgradeAccount() {
        Response response = overdraftResource.pcciOverdraftDetails("P_PLAT");
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void testPcciOverdraftDetails() {
        Response response = overdraftResource.pcciOverdraftDetails();
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void testPcciOverdraftDetailsAmount() {
        String overdraftValue = "12345";
        PcciOverdraftRequest overDraftOption = new PcciOverdraftRequest(overdraftValue);
        when(resolver.resolve(any(String.class), eq(PcciOverdraftRequest.class))).thenReturn(overDraftOption);
        Response response = overdraftResource.pcciOverdraftDetailsAmount(overdraftValue);
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

}
