package com.lbg.ib.api.sales.dao;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;

@RunWith(MockitoJUnitRunner.class)
public class HeaderUtilityTest {

    @Mock
    private SessionManagementDAO    session;

    @Mock
    private ConfigurationDAO        configDAO;

    @Mock
    private ReferenceDataServiceDAO referenceDataService;

    @InjectMocks
    HeaderUtility                   headerUtility;

    @Test
    public void testPrepareSoapHeader() {
        when(session.getUserContext())
                .thenReturn(new UserContext(null, null, null, null, null, null, null, null, null, null, null));
        headerUtility.prepareSoapHeader("dummy", "dummy");
    }
}
