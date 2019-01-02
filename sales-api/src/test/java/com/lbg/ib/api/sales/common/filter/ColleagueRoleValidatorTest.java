package com.lbg.ib.api.sales.common.filter;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by dbhatt on 7/22/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ColleagueRoleValidatorTest {

    @InjectMocks
    ColleagueRoleValidator       colleagueRoleValidator;

    @Mock
    private LoggerDAO            loggerDAO;

    @Mock
    private SessionManagementDAO session;

    @Test(expected = ServiceException.class)
    public void testValidateColleagueRoleForException() throws Exception {
        when(session.getBranchContext()).thenReturn(new BranchContext("dummy_colleague_Id", "", "", ""));
        colleagueRoleValidator.validateColleagueRole(null, null);
    }

    @Test
    public void testValidateColleagueRoleForExcepiton() throws Exception {
        BranchContext branchContext = new BranchContext("dummy_colleague_Id", "", "", "");
        branchContext.setRoles(new ArrayList<String>());
        when(session.getBranchContext()).thenReturn(branchContext);
        colleagueRoleValidator.validateColleagueRole(null, null);
    }

}