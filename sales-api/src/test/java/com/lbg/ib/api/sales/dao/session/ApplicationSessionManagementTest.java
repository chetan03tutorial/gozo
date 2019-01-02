package com.lbg.ib.api.sales.dao.session;

import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationSessionManagementTest {
    @Mock
    ChannelBrandingDAO channelBrandingDAO;
    @Mock
    ConfigurationDAO configurationDAO;
    @Mock
    LoggerDAO logger;
    @Mock
    HttpServletRequest request;
    @InjectMocks
    private ApplicationSessionManagement applicationSessionManagement;

    @Test
    public void testNullSession() {
        SelectedProduct mockProduct = Mockito.mock(SelectedProduct.class);
        applicationSessionManagement.setSelectedProduct(mockProduct);
        CustomerInfo customerInfo = applicationSessionManagement.getCustomerDetails();
        HttpSession mockSession = Mockito.mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(mockSession);
        doThrow(Exception.class).when(mockSession).removeAttribute(anyString());
        applicationSessionManagement.clearSessionAttributeForPipelineChasing();
        verify(mockSession,atLeastOnce()).removeAttribute((String)any());
        doThrow(Exception.class).when(mockSession).getAttribute(anyString());
        Assert.assertNull(customerInfo);
    }
}
