package com.lbg.ib.api.sales.dao;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;

@RunWith(MockitoJUnitRunner.class)
public class MCAHeaderUtilityTest {
    @Mock
    private LoggerDAO               logger;

    @Mock
    private SessionManagementDAO    session;

    @Mock
    private ConfigurationDAO        configDAO;

    @Mock
    private ReferenceDataServiceDAO referenceDataService;

    @InjectMocks
    MCAHeaderUtility                headerUtility;

    @Test
    public void testPrepareSoapHeader() {
        when(session.getUserContext())
                .thenReturn(new UserContext(null, null, null, null, null, "IBL", null, null, null, null, null));
        when(session.getBranchContext()).thenReturn(new BranchContext());
        headerUtility.prepareSoapHeader("dummy", "dummy");
    }

    @Test
    public void testPrepareSoapHeaderWithColleagueRoles() {
        BranchContext branchContext = new BranchContext();
        List<String> colleagueRoles = new ArrayList<String>();
        colleagueRoles.add("dummy");
        branchContext.setRoles(colleagueRoles);
        when(session.getUserContext())
                .thenReturn(new UserContext(null, null, null, null, null, "IBL", null, null, null, null, null));
        when(session.getBranchContext()).thenReturn(branchContext);
        headerUtility.prepareSoapHeader("dummy", "dummy");
    }
}
