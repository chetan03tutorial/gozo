package com.lbg.ib.api.sales.asm.resource;

import com.lbg.ib.api.sales.asm.service.PldAppealService;
import com.lbg.ib.api.sales.pld.request.PldAppealRequest;
import com.lbg.ib.api.sales.product.domain.features.PldProductInfo;
import com.lbg.ib.api.sales.product.domain.features.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class PldAppealResourceTest {

    @Mock
    PldAppealService service;

    @InjectMocks
    PldAppealResource pldAppealResource;

    @Test
    public void testPldAppeal(){
        PldProductInfo pldProductInfo = new PldProductInfo();
        Product product = new Product();
        product.setName("Basic");
        product.setMnemonic("P_NEW_BASIC");
        pldProductInfo.setProduct(product);
        when(service.fetchAppScoreResultsAndUpdateDecision(null)).thenReturn(pldProductInfo);
        Response response = pldAppealResource.pldAppeal(new PldAppealRequest());
        assertTrue(response!=null);
    }
}
