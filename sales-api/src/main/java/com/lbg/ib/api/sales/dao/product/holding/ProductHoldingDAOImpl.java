/**********************************************************************
 * This source code is the property of Lloyd Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.holding;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ProductHoldingResponseMapper;
import com.lbg.ib.api.sales.dao.product.holding.domain.ProductHolding;
import com.lbg.ib.api.sales.dto.product.ProductHoldingRequestDTO;

@Component
public class ProductHoldingDAOImpl extends Base implements ProductHoldingDAO {

    @Autowired
    private ProductHoldingResponseMapper responseMapper;

    @Autowired
    private URL                          productHoldingURL;

    private boolean                      isTestRun;

    // move this into config , a 10 second timeout is very short
    private static final int             TIMEOUT    = 10000;

    @TraceLog
    public DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> fetchProductHoldings(
            ProductHoldingRequestDTO request) {
        logger.traceLog(this.getClass(), "Fetching User Info");
        String url = generateURL(request);

        logger.traceLog(this.getClass(), String.format("Url is %s", url));

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("accept-type", "application/json");
        httpGet.addHeader("Content-Type", "application/json");
        BufferedReader rd = null;
        try {
            logger.traceLog(this.getClass(), "About to fetch the response from Product Holdings " + url);
            HttpResponse httpResponse = httpClient().execute(httpGet);
            StringBuffer result = new StringBuffer();
            if (null != httpResponse) {
                rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                logger.traceLog(this.getClass(), "Mapping the response from the Pre-fetch session data.");

                ProductHolding account = mapper.readValue(result.toString(), ProductHolding.class);
                String reasonCode = account.getCode();

                logger.traceLog(this.getClass(),
                        "Reason Code of the Response : " + (reasonCode != null ? reasonCode : null));
                if (null != reasonCode) {
                    return withError(new DAOResponse.DAOError(reasonCode, account.getMessage()));
                } else {
                    return withResult(responseMapper.populateProductHoldingResponse(account));
                }
            }
        } catch (ClientProtocolException e) {
            logger.logException(this.getClass(), e);
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
        } finally {
            if (null != rd) {
                try {
                    rd.close();
                } catch (IOException e) {
                    logger.logException(this.getClass(), e);
                }
            }
        }
        return null;
    }

    /*
    public DefaultHttpClient httpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 3000);
        return new DefaultHttpClient(params);
    }
    */

    /** SSL compatible ssl client
    * .useSystemProperties causes client to pick up WAS sslSocketFactory
    * thanks to this param: ssl.SocketFactory.provider
    */
    public HttpClient httpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(TIMEOUT)
                    .setSocketTimeout(TIMEOUT)
                    .build())
                .useSystemProperties()
                .build();
    }

    private String generateURL(ProductHoldingRequestDTO request) {
        String url = null;
        if (productHoldingURL.toString().contains("{}")) {
            url = productHoldingURL.toString().replace("{}", request.getIbSessionId());
        } else if (productHoldingURL.toString().endsWith("/")) {
            url = productHoldingURL.toString() + request.getIbSessionId();
        } else {
            url = productHoldingURL.toString() + "/" + request.getIbSessionId();
        }
        return url;
    }

    public void setLoggerDAO(LoggerDAO logger) {
        this.logger = logger;
    }

    public void setProductHoldingResponseMapper(ProductHoldingResponseMapper responseMapper) {
        this.responseMapper = responseMapper;
    }

    public void setIsTestRun(boolean isTestRun) {
        this.isTestRun = isTestRun;
    }

    public URL getProductHoldingURL() {
        return productHoldingURL;
    }

    public void setProductHoldingURL(URL productHoldingURL) {
        this.productHoldingURL = productHoldingURL;
    }
}
