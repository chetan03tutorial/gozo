package com.lbg.ib.api.sales.mca.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lloydstsb.ea.config.ConfigurationService;

/**
 * Created by dbhatt on 7/18/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchContextResourceTest {

    @InjectMocks
    BranchContextResource        branchContextResource = null;

    @Mock
    private RequestBodyResolver  resolver;

    @Mock
    HttpServletRequest           req;

    @Mock
    private LoggerDAO            logger                = mock(LoggerDAO.class);

    @Mock
    private BranchContextService branchContextService;

    HttpSession                  session               = null;

    HttpServletRequest           request               = null;

    ConfigurationService         configurationService  = null;

    @Before
    public void setUp() throws Exception {
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        configurationService = mock(ConfigurationService.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void setBranchContextWithValidColleagueIdAndDomain() throws Exception {
        BranchContext branchContext = new BranchContext();
        String requestString = "{\"originatingSortCode\":\"779122\"}";
        when(resolver.resolve(requestString, BranchContext.class)).thenReturn(branchContext);
        when(req.getSession(any(Boolean.class))).thenReturn(session);
        when(session.getAttribute(BranchContextConstants.COLLEGUE_ID)).thenReturn("dummy_colleagueId");
        when(session.getAttribute(BranchContextConstants.COLLEAGUE_DOMAIN)).thenReturn("dummy_colleagueId");
        when(branchContextService.setBranchContextToSession(any(BranchContext.class))).thenReturn(new ColleagueContext(null,false));

        Response response = branchContextResource.setBranchContext(req, requestString);

        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void setBranchContextWithValidColleagueIdAndDomainGetWithAccreditionFlagNotSet() throws Exception {
        Map<String, Object> appConfig = new HashMap<String, Object>();
        when(configurationService.getConfigurationItems(any(String.class))).thenReturn(appConfig);
        branchContextResource.setConfigurationService(configurationService);
        Response response = branchContextResource.setColleagueToBranchContext("dummy_id", "dummy_id", request);
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void setBranchContextWithValidColleagueIdAndDomainGetWithAccreditionFlagSetFalse() throws Exception {
        Map<String, Object> appConfig = new HashMap<String, Object>();
        appConfig.put(BranchContextConstants.ACCREDITION_FLAG, "false");
        when(configurationService.getConfigurationItems(any(String.class))).thenReturn(appConfig);
        when(request.getSession()).thenReturn(session);
        branchContextResource.setConfigurationService(configurationService);
        Response response = branchContextResource.setColleagueToBranchContext("dummy_id", "dummy_id", request);
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void setBranchContextWithValidColleagueIdAndDomainGetWithAccreditionFlagSetTrue() throws Exception {
        Map<String, Object> appConfig = new HashMap<String, Object>();
        appConfig.put(BranchContextConstants.ACCREDITION_FLAG, "true");
        when(configurationService.getConfigurationItems(any(String.class))).thenReturn(appConfig);
        branchContextResource.setConfigurationService(configurationService);
        Response response = branchContextResource.setColleagueToBranchContext("dummy_id", "dummy_id", request);

        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
    }
}