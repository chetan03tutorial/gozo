/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.resources;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.product.service.ProductContentService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

@RunWith(MockitoJUnitRunner.class)
public class ProductContentResourceTest {

    @InjectMocks
    ProductContentResource resource;

    @Mock
    ProductContentService productContentService;

    @Mock
    GalaxyErrorCodeResolver errorResolver;

    @Mock
    LoggerDAO logger;

    @Test
    public void testContentValidContentPresent() throws ReferenceDataException {
        ProductContentResource productContentResource = new ProductContentResource();
        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("P_STUDENT", "dummy Content");
        when(productContentService.getAllProductContent(null)).thenReturn(responseMap);

        Response response = resource.content(null);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testContentValidContentPresentWithNullResponse() throws ReferenceDataException {
        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("P_STUDENT", "dummy Content");
        when(productContentService.getAllProductContent(null)).thenReturn(null);

        Response response = resource.content(null);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testContentValidContentPresentWithEmptyResponse() throws ReferenceDataException {
        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("P_STUDENT", "dummy Content");
        when(productContentService.getAllProductContent(null)).thenReturn(new HashMap<String, String>());

        Response response = resource.content(null);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testContentValidContentAbsent() throws ReferenceDataException {
        when(productContentService.getAllProductContent(null)).thenReturn(null);

        Response response = resource.content(null);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }
}