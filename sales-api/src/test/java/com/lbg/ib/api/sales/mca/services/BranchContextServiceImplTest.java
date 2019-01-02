package com.lbg.ib.api.sales.mca.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.colleagues.involvedparty.service.RetrieveInvolvedPartyService;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;

/**
 * Created by dbhatt on 7/25/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchContextServiceImplTest {

    @InjectMocks
    BranchContextServiceImpl     branchContextService;

    LoggerDAO                    logger = mock(LoggerDAO.class);

    @Mock
    SessionManagementDAO         session;

    @Mock
    RetrieveInvolvedPartyService retrieveInvolvedPartyService;


    @Test
    public void testCheckSnRRoleWithValidRoleForColleague(){
        branchContextService.setLogger(logger);
        BranchContext branchContext = new BranchContext();
        when(retrieveInvolvedPartyService.retrieveSnRRolesForInvolvedParty(branchContext)).thenReturn(true);
        assertTrue(branchContextService.checkSnRRole(branchContext));
    }


    @Test(expected=ServiceException.class)
    public void testCheckSnRRoleWithInValidRoleForColleague(){
        branchContextService.setLogger(logger);
        BranchContext branchContext = new BranchContext();
        when(retrieveInvolvedPartyService.retrieveSnRRolesForInvolvedParty(branchContext)).thenReturn(false);
        branchContextService.checkSnRRole(branchContext);
    }

    @Test
    public void testSetBranchContextToSessionReturningAuthorisedColleagueContext() throws Exception {
        branchContextService.setLogger(logger);
        when(retrieveInvolvedPartyService.retrieveRolesForInvolvedParty())
                .thenReturn(new ColleagueContext("dummy_colleague_id", true));

        BranchContext branchContext = new BranchContext();
        ColleagueContext colleagueContext = branchContextService.setBranchContextToSession(branchContext);
        assertTrue(colleagueContext.isAuthorized());
    }

    @Test
    public void testSetBranchContextToSessionReturningUnAuthorisedColleagueContext() throws Exception {
        branchContextService.setLogger(logger);
        when(retrieveInvolvedPartyService.retrieveRolesForInvolvedParty())
                .thenReturn(new ColleagueContext("dummy_colleague_id", false));

        BranchContext branchContext = new BranchContext();
        ColleagueContext colleagueContext = branchContextService.setBranchContextToSession(branchContext);
        assertTrue(!colleagueContext.isAuthorized());
    }

}