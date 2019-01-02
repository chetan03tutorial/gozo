package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.client.RestContext.SalsaRestContextBuilder;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.party.dto.CardOrderResponse;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CardOrderService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ModuleContext beanLoader;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private UriResolver ocisUriResolver;

    @Autowired
    private OcisRestHeaderUtil ocisRestHeaderUtil;

    @Autowired
    private ExternalRestApiClientDAO externalRestApiClientDAO;

    /**
     * Retrieve Party Details
     * @param ocisId
     * @return {@link RetrievePartyDetailsResponse}
     */
    @TraceLog
    public CardOrderResponse upgradeCard(Object requestBody) {
        String endpoint = ocisUriResolver.getEndpoint(SalsaEndpoints.CARD_ORDER_CMAS_ENDPOINT);
        RestContext externalRestContext = buildRequest(endpoint, requestBody);
        CardOrderResponse response = externalRestApiClientDAO.customPost(externalRestContext,
                CardOrderResponse.class);
        if (null == response) {
            logger.traceLog(this.getClass(), "upgradeCard(): Null response from CMAS API");
            throw new ServiceException(
                    new ResponseError("CMAS Error", "Null response from CMAS API"));
        }
        return response;
    }

    /**
     * Builds the request for rest call
     * @param pathParameters
     * @param endpoint
     * @return {@link RestContext}
     */
    private RestContext buildRequest(String endpoint, Object requestBody) {
        Map<String, Object> headers = ocisRestHeaderUtil.getSalsaHeader();
        SalsaRestContextBuilder builder = RestContext.SalsaRestContextBuilder.getBuilder(endpoint).requestBody(requestBody);
        builder.requestHeaders(headers);
        return builder.requestBody(requestBody).build();
    }

}
