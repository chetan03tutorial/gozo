package com.lbg.ib.api.sales.conversion.resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.sales.conversion.service.ConversionService;
import com.lbg.ib.api.sales.conversion.service.ConversionServiceImpl;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ConversionResourceTest {

    @InjectMocks
    private ConversionResource conversionResource;

    @Mock
    private RequestBodyResolver resolver;

    @Mock
    private ConversionServiceImpl conversionServiceImpl;

    @Mock
    private ModuleContext moduleContext;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(moduleContext.getService(ConversionService.class)).thenReturn(conversionServiceImpl);
    }

    @Test
    public void upgradeAccount() {
        String requestString = "{ \"templateName\": \"CA_WELCOME_MSG_PDF\",\"productMnemonic\":\"P_PLAT\",\"emailTokens\":{\"IB.Product.Mnemonic\":\"P_PLAT\" } }";
        Response response = conversionResource.upgradeAccount(requestString, "Q226");
        Assert.notNull(response.getStatus());
        assertThat(response.getStatus(), is(200));
    }

}
