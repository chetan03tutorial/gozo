/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.content.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.junit.Test;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.sales.dao.content.ContentDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;

public class ContentServiceImplTest {
    public static final Response      RESPONSE           = new ResponseBuilderImpl().build();
    public static final ResponseError ERROR              = new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE,
            "Service Unavailable");
    private ChannelBrandingDAO        channelBrandingDao = mock(ChannelBrandingDAO.class);
    private GalaxyErrorCodeResolver   resolver           = mock(GalaxyErrorCodeResolver.class);
    private ContentDAO                contentDao         = mock(ContentDAO.class);

    @Test
    public void shouldReturnResultContent() throws Exception {
        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("channel", "brand", "channelId")));
        when(contentDao.content("channel", "key")).thenReturn(withResult(RESPONSE));
        assertThat(new ContentServiceImpl(contentDao, channelBrandingDao, resolver).content("key"), is(RESPONSE));
    }

    @Test
    public void shouldThrowServiceExceptionWhenChannelBrandingCannotBeFound() throws Exception {
        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("channel", "brand", "channelId")));
        when(contentDao.content("channel", "key"))
                .thenReturn(DAOResponse.<Response> withError(new DAOResponse.DAOError("code", "message")));
        when(resolver.resolve("code")).thenReturn(ERROR);
        try {
            new ContentServiceImpl(contentDao, channelBrandingDao, resolver).content("key");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(ERROR));
        }
    }

    @Test
    public void shouldThrowServiceExceptionWhenContentDaoReturnsError() throws Exception {
        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("channel", "brand", "channelId")));
        when(contentDao.content("channel", "key"))
                .thenReturn(DAOResponse.<Response> withError(new DAOResponse.DAOError("code", "message")));
        when(resolver.resolve("code")).thenReturn(ERROR);
        try {
            new ContentServiceImpl(contentDao, channelBrandingDao, resolver).content("key");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(ERROR));
        }
    }
}