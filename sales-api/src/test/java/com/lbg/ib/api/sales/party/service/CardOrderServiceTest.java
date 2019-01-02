package com.lbg.ib.api.sales.party.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.dto.CardOrderResponse;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

@RunWith(MockitoJUnitRunner.class)
public class CardOrderServiceTest {

    @InjectMocks
    CardOrderService service;

    @Mock
    private UriResolver ocisUriResolver;

    @Mock
    private ExternalRestApiClientDAO externalRestApiClientDAO;

    @Mock
    private OcisRestHeaderUtil ocisRestHeaderUtil;

    @Mock
    RestContext externalRestContext;

    @Mock
    RestContext.SalsaRestContextBuilder builder;

    @Mock
    private LoggerDAO logger;

    /*
     * @Mock CardOrderResponse response;
     */
    private static Map<String, Object> headerMap;

    /*
     * @Before public void init() { MockitoAnnotations.initMocks(this); headerMap = new
     * HashMap<String, Object>(); headerMap.put("x-lbg-user-id", "19");
     * when(ocisRestHeaderUtil.getSalsaHeader()).thenReturn(headerMap);
     * when(ocisUriResolver.getEndpoint(SalsaEndpoints.CARD_ORDER_CMAS_ENDPOINT)).thenReturn(
     * "CARD_ORDER_CMAS_ENDPOINT"); }
     */
    //    @PrepareForTest({ AccountManager.class })
    @Test
    public void upgradeCardTest() {
        CardOrderResponse response = new CardOrderResponse();
        when(externalRestApiClientDAO.customPost(any(RestContext.class), eq(CardOrderResponse.class))).thenReturn(response);
        Object requestBody = new Object();
        assertNotNull(service.upgradeCard(requestBody));
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowServiceExceptionWhenNullResponse() {
        CardOrderResponse response = null;//in order to return null response.
        when(externalRestApiClientDAO.customPost(any(RestContext.class), eq(CardOrderResponse.class))).thenReturn(response);
        Object requestBody = new Object();
        when(logger.debugEnabled()).thenReturn(true);
        assertEquals(null, service.upgradeCard(requestBody));
    }
}