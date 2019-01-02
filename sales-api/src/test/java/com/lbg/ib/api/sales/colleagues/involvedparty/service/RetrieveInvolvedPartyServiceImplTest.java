package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import static com.lbg.ib.api.sales.common.constant.Constants.CHANNEL_IDENTIFIER;
import static com.lbg.ib.api.sales.common.constant.Constants.MCA_DOMAIN;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.colleagues.involvedparty.dao.RetrieveInvolvedPartyDAO;
import com.lbg.ib.api.sales.colleagues.involvedparty.dto.RetrieveInvolvedPartyResponseDTO;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.lcsm.ErrorInfo;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsRequest;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveInvolvedPartyServiceImplTest {
    @InjectMocks
    RetrieveInvolvedPartyServiceImpl   retrieveInvolvedPartyServiceImpl;

    @Mock
    private SessionManagementDAO       session;

    @Mock
    private RetrieveInvolvedPartyDAO   retrieveInvolvedPartyDAO;

    @Mock
    private ConfigurationService       configurationService;

    @Mock
    private GalaxyErrorCodeResolver    resolver;

    @Mock
    private LoggerDAO                  logger;

    private static final ResponseError RESPONSE_ERROR = new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE,
            "Service Unavailable");

    @Test(expected=ServiceException.class)
    public void testRetrieveSnRRolesForInvolvedParty() {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");
        retrieveInvolvedPartyServiceImpl.setResolver(resolver);

        RetrieveInvolvedPartyResponseDTO roles = new RetrieveInvolvedPartyResponseDTO();

        Set<String> stSet = new HashSet<String>();
        stSet.add("GG_SDI_RemoteDesktop");
        stSet.add("GG_RETAIL_BRANCH_SAVING_SELLER");
        roles.setRoles(stSet);

        Map<String, Object> entitledGroupMap = new HashMap<String, Object>();
        entitledGroupMap.put("BMR", "GV_SBAO_SAVINGS");
        entitledGroupMap.put("BTRSAVINGS", "GG_RETAIL_BRANCH_SAVING_SELLER");

        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        when(configurationService.getConfigurationItems("CURRENT_ACCOUNTS")).thenReturn(entitledGroupMap);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
        .thenReturn(DAOResponse
                .<RetrieveInvolvedPartyResponseDTO> withError(new DAOResponse.DAOError("DUMMY", "DUMMY")));
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);

        assertTrue(retrieveInvolvedPartyServiceImpl.retrieveSnRRolesForInvolvedParty(branchContext));

        assertTrue(retrieveInvolvedPartyServiceImpl.retrieveSnRRolesForInvolvedParty(branchContext));
    }


    @Test
    public void testRetrieveSnRRolesForInvolvedPartyWithNoErrorValidRolesAccreditionFlagTrue() {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");

        RetrieveInvolvedPartyResponseDTO roles = new RetrieveInvolvedPartyResponseDTO();

        Set<String> stSet = new HashSet<String>();
        stSet.add("GG_SDI_RemoteDesktop");
        stSet.add("GG_RETAIL_BRANCH_SAVING_SELLER");
        roles.setRoles(stSet);

        Map<String, Object> entitledGroupMap = new HashMap<String, Object>();
        entitledGroupMap.put("BMR", "GV_SBAO_SAVINGS");
        entitledGroupMap.put("BTRSAVINGS", "GG_RETAIL_BRANCH_SAVING_SELLER");

        Map<String, Object> applicationPropertiesMap = new HashMap<String, Object>();
        applicationPropertiesMap.put("accreditionFlag", "true");

        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        when(configurationService.getConfigurationItems("SAVENRESUMEWRITE")).thenReturn(entitledGroupMap);
        when(configurationService.getConfigurationItems("ApplicationProperties")).thenReturn(applicationPropertiesMap);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
                .thenReturn(withResult(roles));

        assertTrue(retrieveInvolvedPartyServiceImpl.retrieveSnRRolesForInvolvedParty(branchContext));


    }

    @Test
    public void testRetrieveRolesForInvolvedPartyWithNoErrorValidRoles()
            throws ServiceException, ErrorInfo, RemoteException {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");

        RetrieveInvolvedPartyResponseDTO roles = new RetrieveInvolvedPartyResponseDTO();

        Set<String> stSet = new HashSet<String>();
        stSet.add("GG_SDI_RemoteDesktop");
        stSet.add("GG_RETAIL_BRANCH_SAVING_SELLER");
        roles.setRoles(stSet);

        Map<String, Object> entitledGroupMap = new HashMap<String, Object>();
        entitledGroupMap.put("BMR", "GV_SBAO_SAVINGS");
        entitledGroupMap.put("BTRSAVINGS", "GG_RETAIL_BRANCH_SAVING_SELLER");

        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        when(configurationService.getConfigurationItems("CURRENT_ACCOUNTS")).thenReturn(entitledGroupMap);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
                .thenReturn(withResult(roles));

        ColleagueContext colleagueContext = retrieveInvolvedPartyServiceImpl.retrieveRolesForInvolvedParty();

        assertTrue("dummy_colleagueId".equals(colleagueContext.getColleagueId()));
        assertTrue(colleagueContext.isAuthorized());
    }

    @Test
    public void testRetrieveRolesForInvolvedPartyWithNoErrorValidRolesAccreditionFlagTrue()
            throws ServiceException, ErrorInfo, RemoteException {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");

        RetrieveInvolvedPartyResponseDTO roles = new RetrieveInvolvedPartyResponseDTO();

        Set<String> stSet = new HashSet<String>();
        stSet.add("GG_SDI_RemoteDesktop");
        stSet.add("GG_RETAIL_BRANCH_SAVING_SELLER");
        roles.setRoles(stSet);

        Map<String, Object> entitledGroupMap = new HashMap<String, Object>();
        entitledGroupMap.put("BMR", "GV_SBAO_SAVINGS");
        entitledGroupMap.put("BTRSAVINGS", "GG_RETAIL_BRANCH_SAVING_SELLER");

        Map<String, Object> applicationPropertiesMap = new HashMap<String, Object>();
        applicationPropertiesMap.put("accreditionFlag", "true");

        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        when(configurationService.getConfigurationItems("CURRENT_ACCOUNTS")).thenReturn(entitledGroupMap);
        when(configurationService.getConfigurationItems("ApplicationProperties")).thenReturn(applicationPropertiesMap);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
                .thenReturn(withResult(roles));

        ColleagueContext colleagueContext = retrieveInvolvedPartyServiceImpl.retrieveRolesForInvolvedParty();

        assertTrue("dummy_colleagueId".equals(colleagueContext.getColleagueId()));
        assertTrue(colleagueContext.isAuthorized());
    }

    @Test
    public void chechDomainTestWhenBranchContextIsNull() {
        when(session.getBranchContext()).thenReturn(null);
        assertTrue(retrieveInvolvedPartyServiceImpl.checkDomain("DUMMY_DOMAIN").contains("DUMMY_DOMAIN"));
    }

    @Test
    public void chechDomainTestWhenBranchContextWithDomainAsNull() {
        BranchContext branchContext = new BranchContext();
        branchContext.setDomain("DUMMY_DOMAIN");
        when(session.getBranchContext()).thenReturn(branchContext);
        assertTrue(retrieveInvolvedPartyServiceImpl.checkDomain("DUMMY_DOMAIN").contains("DUMMY_DOMAIN"));
    }

    @Test(expected = ServiceException.class)
    public void testRetrieveRolesForInvolvedPartyWithErrorValidRoles()
            throws ServiceException, ErrorInfo, RemoteException {
        RetrieveInvolvedPartyServiceImpl dummyServiceImpl = new RetrieveInvolvedPartyServiceImpl();
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");

        RetrieveInvolvedPartyResponseDTO roles = new RetrieveInvolvedPartyResponseDTO();

        Set<String> stSet = new HashSet<String>();
        stSet.add("GG_SDI_RemoteDesktop");
        stSet.add("GG_RETAIL_BRANCH_SAVING_SELLER");
        roles.setRoles(stSet);

        Map<String, Object> entitledGroupMap = new HashMap<String, Object>();
        entitledGroupMap.put("BMR", "GV_SBAO_SAVINGS");
        entitledGroupMap.put("BTRSAVINGS", "GG_RETAIL_BRANCH_SAVING_SELLER");

        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        when(configurationService.getConfigurationItems("CURRENT_ACCOUNTS")).thenReturn(entitledGroupMap);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
                .thenReturn(DAOResponse
                        .<RetrieveInvolvedPartyResponseDTO> withError(new DAOResponse.DAOError("DUMMY", "DUMMY")));
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        retrieveInvolvedPartyServiceImpl.setResolver(resolver);
        retrieveInvolvedPartyServiceImpl.retrieveRolesForInvolvedParty();

    }

    // @Test
    public void testRetrieveRolesForInvolvedPartyWithError() throws ServiceException, ErrorInfo, RemoteException {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("dummy_colleagueId");
        when(session.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationValueAsString(CHANNEL_IDENTIFIER, MCA_DOMAIN)).thenReturn("DUMMY");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(BranchContextConstants.ACCREDITION_FLAG, "false");
        when(configurationService.getConfigurationItems(BranchContextConstants.APPLICATION_PROPERTIES)).thenReturn(map);
        when(retrieveInvolvedPartyDAO.retrieveRolesForInvolvedParty(any(RetrieveInvolvedPartyRoleDetailsRequest.class)))
                .thenReturn(DAOResponse
                        .<RetrieveInvolvedPartyResponseDTO> withError(new DAOResponse.DAOError("INVALID", "INVALID")));
        ColleagueContext colleagueContext = retrieveInvolvedPartyServiceImpl.retrieveRolesForInvolvedParty();
        assertTrue(colleagueContext != null);
    }
}