package com.lbg.ib.api.sales.common.session.resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;

@RunWith(MockitoJUnitRunner.class)
public class PackageAccountSessionInfoResourceTest {

    @InjectMocks
    PackageAccountSessionInfoResource packageAccountSessionInfoResource;

    @Mock
    HttpServletRequest                req;

    @Mock
    private LoggerDAO                 logger = mock(LoggerDAO.class);

    @Mock
    private SessionManagementDAO      session;

    @Test
    public void getPackagedAccountSessionInfoSuccessTest() throws Exception {
        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        Arrangement offerRequest = new Arrangement();
        offerRequest.setAccountType(AccountType.CA);
        packageAccountSessionInfo.setOfferRequest(offerRequest);

        Arranged offerResponse = new Arranged();
        offerResponse.setOcisId("ocisId");
        packageAccountSessionInfo.setOfferResponse(offerResponse);
        when(session.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);

        Response response = packageAccountSessionInfoResource.getPackagedAccountSessionInfo();

        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
        assertTrue(((PackageAccountSessionInfo) response.getEntity()).getOfferRequest().getAccountType().equals(AccountType.CA));
        assertTrue(((PackageAccountSessionInfo) response.getEntity()).getOfferResponse().getOcisId().equals("ocisId"));
    }

    @Test
    public void getPackagedAccountSessionInfoFailureTest() throws Exception {
        when(session.getPackagedAccountSessionInfo()).thenReturn(null);
        Response response = packageAccountSessionInfoResource.getPackagedAccountSessionInfo();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
    }
}
