package com.lbg.ib.api.sales.product.service;


import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.sales.product.resources.ModifyProductArrangementResource;
import com.lbg.ib.api.sales.product.service.ModifyProductArrangementService;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModifyProductArrangementResourceTest {
    @Mock
    private ModifyProductArrangementService service;
    @Mock
    private FieldValidator fieldValidator;
    @Mock
    private LoggerDAO logger;
    @Mock
    private RequestBodyResolver resolver;
    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    
    @InjectMocks
    private ModifyProductArrangementResource modifyProductArrangementResource;
    
    
    @Test(expected=ServiceException.class)
    public void testRetrieveProductArrangementWithNoArrangementId() throws Exception {
        ModifyProductArrangementResource dummy = new ModifyProductArrangementResource();
        String request = "dummy_request";
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        when(resolver.resolve(any(String.class), eq(ModifyProductArrangement.class)))
        .thenReturn(modifyProductArrangement);
        when(fieldValidator.validateInstanceFields(modifyProductArrangement)).thenReturn(new ValidationError("'arrangementId' required to be set but it was 'null'"));
        modifyProductArrangementResource.modifyProductArrangement(request);
    }
    
    @Test(expected=ServiceException.class)
    public void testRetrieveProductArrangementWithNoAccountType() throws Exception {
        String request = "dummy_request";
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        modifyProductArrangement.setArrangementId("1234");
        when(resolver.resolve(any(String.class), eq(ModifyProductArrangement.class)))
        .thenReturn(modifyProductArrangement);
        when(fieldValidator.validateInstanceFields(modifyProductArrangement)).thenReturn(new ValidationError("'arrangementId' required to be set but it was 'null'"));
        modifyProductArrangementResource.modifyProductArrangement(request);
    }


    @Test
    public void testRetrieveProductArrangementWithAccountType() throws Exception {
        String request = "dummy_request";
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        modifyProductArrangement.setArrangementId("1234");
        modifyProductArrangement.setArrangementType("CA");
        when(resolver.resolve(any(String.class), eq(ModifyProductArrangement.class)))
                .thenReturn(modifyProductArrangement);
        Response response = modifyProductArrangementResource.modifyProductArrangement(request);
        assertTrue(response!=null);
        assertTrue(response.getStatus()==200);
    }




}
