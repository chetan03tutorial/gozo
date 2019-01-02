package com.lbg.ib.api.sales.content.resources;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.content.service.ContentService;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

/**
 * Created by pbabb1 on 5/24/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentResourceTest {
    @Mock
    ContentService service;

    @Test
    public void returnContentForTheGivenKey() throws ServiceException {
        ContentResource contentResource = new ContentResource(service);
        Response response = new ResponseBuilderImpl().build();
        when(service.content("someKey")).thenReturn(response);
        Assert.assertEquals(response, contentResource.content("someKey"));

        contentResource = new ContentResource();
        Assert.assertTrue(contentResource != null);
    }
}
