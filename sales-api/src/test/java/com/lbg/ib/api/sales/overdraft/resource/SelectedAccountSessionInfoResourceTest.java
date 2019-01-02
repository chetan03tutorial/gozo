package com.lbg.ib.api.sales.overdraft.resource;

import com.lbg.ib.api.sales.common.domain.MessageResponse;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.SelectedAccountSessionInfo;
import com.lbg.ib.api.sales.overdraft.domain.UpdateSessionRequest;
import com.lbg.ib.api.sales.overdraft.service.AccountSessionInfoService;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SelectedAccountSessionInfoResourceTest {

    @InjectMocks
    private SelectedAccountSessionInfoResource resource;

    @Mock
    private AccountSessionInfoService service;

    @Mock
    private LoggerDAO logger;

    @Mock
    SelectedAccountSessionInfo response;
    @Mock
    private SessionManagementDAO session;

    @Test
    public void testOverdraftLimitNotNullResponse()
    {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setDemandedOd(200.0);
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setAmtOverdraft(new BigDecimal(500.0));
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        Response response = resource.setOverdraftLimit(request);
        assertNotNull(response);
        MessageResponse messageResponse = (MessageResponse) response.getEntity();
        assertEquals(messageResponse.getSuccess(), true);
        assertEquals(messageResponse.getMessage(), "Session updated Successfully");
    }

    @Test(expected = ServiceException.class)
    public void testInvalidOverdraftLimitException()
    {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setDemandedOd(800.0);
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setAmtOverdraft(new BigDecimal(500.0));
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        Response response = resource.setOverdraftLimit(request);
        assertNotNull(response);
    }

    @Test
    public void testGetAccountSessionInfoNotNullResponse()
    {
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setActivationStatus("1010");
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        SelectedAccountSessionInfo sessionInfo = new SelectedAccountSessionInfo() ;
        when(service.fetchSessionDetail()).thenReturn(sessionInfo);
        Response response = resource.getAccountSessionInfo();
        assertNotNull(response);
        assertEquals(sessionInfo, response.getEntity());
    }

    @Test(expected = ServiceException.class)
    public void testGetAccountSessionInfoException()
    {
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setActivationStatus("1009");
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        Response response = resource.getAccountSessionInfo();
        assertNotNull(response);
    }

}
