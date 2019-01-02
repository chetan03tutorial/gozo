package com.lbg.ib.api.sales.dao.product.holding;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ProductHoldingResponseMapper;
import com.lbg.ib.api.sales.dao.product.holding.domain.ProductHolding;
import com.lbg.ib.api.sales.dto.product.ProductHoldingRequestDTO;
import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sales.product.domain.Product;

@RunWith(MockitoJUnitRunner.class)
public class ProductHoldingDAOTest {
    public static final String                                 CODE            = "code";
    public static final String                                 MARSHALLED_BODY = "marshalledBody";
    @Mock
    private LoggerDAO                                          logger;
    @Mock
    private WebClient                                          webClient;
    @Mock
    private DefaultHttpClient                                  httpClient;
    @Mock
    private InputStream                                        INPUT_STREAM;
    @Mock
    private ProductHoldingResponseMapper                       responseMapper;

    private com.lbg.ib.api.sales.product.domain.ProductHolding productHolding;
    private ProductHolding                                     errorProductHolding;
    private ProductHoldingDAOImpl                              dao             = new ProductHoldingDAOImpl();
    private ProductHoldingDAOImpl                              spyDao;
    private URL                                                productHoldingURL;
    @Mock
    private CloseableHttpResponse                              response;
    @Mock
    private HttpEntity                                         entity;

    @Before
    public void setUp() throws MalformedURLException {
        populateProductHolding();
        populateProductHoldingWithErrorCode();
        dao.getProductHoldingURL();
        dao.setProductHoldingURL(productHoldingURL);
        dao.setProductHoldingResponseMapper(responseMapper);
        dao.setLogger(logger);
        dao.setLoggerDAO(logger);
        spyDao = Mockito.spy(dao);
    }

    @Test
    public void shouldResponseWithProductHoldingInCaseValidIbsessionId() throws Exception {
        productHoldingURL = new URL("http://localhost");
        spyDao.setProductHoldingURL(productHoldingURL);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "FINE!"));
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"customer\": {}}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        Mockito.doReturn(httpClient).when(spyDao).httpClient();
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
        when(responseMapper.populateProductHoldingResponse(any(ProductHolding.class))).thenReturn(productHolding);
        DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> products = spyDao
                .fetchProductHoldings(new ProductHoldingRequestDTO("123", null, null));
        assertFalse(products.getResult().getProducts().isEmpty());
        assertTrue(null == products.getError());
    }

    @Test
    public void shouldResponseWithProductHoldingInCaseInvalidIbsessionId() throws Exception {
        productHoldingURL = new URL("http://localhost/");
        spyDao.setProductHoldingURL(productHoldingURL);
        spyDao.setIsTestRun(true);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "FINE!"));
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"code\": \"72003\"}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        Mockito.doReturn(httpClient).when(spyDao).httpClient();
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
        when(responseMapper.populateProductHoldingResponse(any(ProductHolding.class))).thenReturn(productHolding);
        DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> products = spyDao
                .fetchProductHoldings(new ProductHoldingRequestDTO("123", null, null));
        assertTrue(null == products.getResult());
        assertTrue("72003".equalsIgnoreCase(products.getError().getErrorCode()));
    }

    @Test
    public void shouldResponseWithNullWhenInvalidResponse() throws Exception {
        productHoldingURL = new URL("http://localhost/");
        spyDao.setProductHoldingURL(productHoldingURL);
        spyDao.setIsTestRun(true);

        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "FINE!"));
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{code: \"72003\"}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        Mockito.doReturn(httpClient).when(spyDao).httpClient();
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
        when(responseMapper.populateProductHoldingResponse(any(ProductHolding.class))).thenReturn(productHolding);
        DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> products = spyDao
                .fetchProductHoldings(new ProductHoldingRequestDTO("123", null, null));
        assertTrue(null == products);

    }

    @Test
    public void shouldResponsedWithNullWhenClientProtocolException() throws Exception {
        productHoldingURL = new URL("http://localhost/{}");
        spyDao.setProductHoldingURL(productHoldingURL);
        spyDao.setIsTestRun(true);

        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "FINE!"));
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"code\": \"72003\"}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        Mockito.doReturn(httpClient).when(spyDao).httpClient();
        when(httpClient.execute(any(HttpGet.class))).thenThrow(ClientProtocolException.class);
        when(responseMapper.populateProductHoldingResponse(any(ProductHolding.class))).thenReturn(productHolding);
        DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> products = spyDao
                .fetchProductHoldings(new ProductHoldingRequestDTO("123", null, null));
        assertTrue(null == products);

    }

    @Test
    public void testhttpClient() {
        HttpClient httpClient = spyDao.httpClient();
        assertFalse(null == httpClient);
    }

    private com.lbg.ib.api.sales.product.domain.ProductHolding populateProductHolding() {
        productHolding = new com.lbg.ib.api.sales.product.domain.ProductHolding();
        List<Product> products = new ArrayList<Product>();
        Product product = new Product();
        product.setAccountNumber("123");
        product.setSortCode("123");
        product.setAccountName("Club Saver");
        product.setDormant(false);
        Event event1 = new Event();
        event1.setEventId("24");
        Event event2 = new Event();
        event2.setEventId("25");
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);
        product.setEvents(events);
        product.setDisplayOrder(1);
        product.setDormant(false);
        products.add(product);
        productHolding.setProducts(products);
        return productHolding;
    }

    private ProductHolding populateProductHoldingWithErrorCode() {
        errorProductHolding = new ProductHolding();
        errorProductHolding.setCode("72003");
        return errorProductHolding;
    }

}
