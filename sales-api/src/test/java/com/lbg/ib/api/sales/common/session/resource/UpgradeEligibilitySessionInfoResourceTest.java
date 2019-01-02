package com.lbg.ib.api.sales.common.session.resource;
/*
Created by Rohit.Soni at 25/06/2018 17:16
*/

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.UpgradeEligibilitySessionInfo;
import com.lbg.ib.api.sales.product.service.UpgradeEligibilityService;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpgradeEligibilitySessionInfoResourceTest {

    private static final String brand = "LTB";

    @Mock
    private SessionManagementDAO session;
    @Mock
    private UpgradeEligibilityService service;
    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    @Mock
    private ApiServiceProperties apiServiceProperties;
    @Mock
    private ConfigurationDAO configuration;
    @Mock
    private LoggerDAO logger;

    private Account account = new Account();

    @InjectMocks
    UpgradeEligibilitySessionInfoResource classUnderTest = new UpgradeEligibilitySessionInfoResource();

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(UpgradeEligibilitySessionInfoResourceTest.class);
    }

    @Before
    public void setupForEach(){
        when(session.getAllPartyDetailsSessionInfo()).thenReturn(SessionServiceUtil.buildParties(2));
        when(session.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(configuration.getConfigurationStringValue(anyString(), anyString())).thenReturn("LTB");
        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(createVersionInfoData());
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(createBrandMap());

    }

    @Test
    public void testCreateArrangementSetupServiceSuccessinDigital() throws Exception {
        Arrangement arrangement = new Arrangement();
        arrangement.setAccounts(getAccounts());
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setEmail("abc@abc");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(session.getUserInfo()).thenReturn(arrangement);
        when(service.getSelectedAccount()).thenReturn(account);
        final Response upgradeEligibilitySessionInfo = classUnderTest.getUpgradeEligibilitySessionInfo();
        UpgradeEligibilitySessionInfo upgradeEligibilitySessionInfoResult = (UpgradeEligibilitySessionInfo)upgradeEligibilitySessionInfo.getEntity();
        Assert.assertEquals("DEV", upgradeEligibilitySessionInfoResult.getEnvironmentName());
        Assert.assertEquals(account, upgradeEligibilitySessionInfoResult.getSelectedAccount());
    }

    //

    @Test
    public void testCreateArrangementSetupServiceSuccessInBranch() throws Exception {
        when(session.getBranchContext()).thenReturn(new BranchContext());
        Arrangement arrangement = new Arrangement();
        arrangement.setAccounts(getAccounts());
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setEmail("abc@abc");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(session.getUserInfo()).thenReturn(arrangement);
        when(service.getSelectedAccount()).thenReturn(account);
        final Response upgradeEligibilitySessionInfo = classUnderTest.getUpgradeEligibilitySessionInfo();
        UpgradeEligibilitySessionInfo upgradeEligibilitySessionInfoResult = (UpgradeEligibilitySessionInfo)upgradeEligibilitySessionInfo.getEntity();
        Assert.assertEquals("DEV", upgradeEligibilitySessionInfoResult.getEnvironmentName());
        Assert.assertEquals(account, upgradeEligibilitySessionInfoResult.getSelectedAccount());
    }

    @Test
    public void testCreateArrangementSetupServiceWithNoDetailsFound() throws Exception {
        when(session.getBranchContext()).thenReturn(new BranchContext());
        Arrangement arrangement = new Arrangement();
        arrangement.setAccounts(getAccounts());
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setEmail("abc@abc");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(session.getUserInfo()).thenReturn(arrangement);
        //when()
        final Response upgradeEligibilitySessionInfo = classUnderTest.getUpgradeEligibilitySessionInfo();
        final ResponseError responseError = (ResponseError)upgradeEligibilitySessionInfo.getEntity();
        Assert.assertEquals("99000091",responseError.getCode());
    }

    private List<Account> getAccounts(){
        List<Account> accounts = new ArrayList<Account>();
        Account account1 = new Account();
        account1.setAccountNumber("12345");
        account1.setAccountType("T1234567890");
        account1.setSortCode("121212");
        account1.setProductType("C");
        account1.setManufacturingLegalEntity("LTB");
        account1.setOverdraftLimit(50.0);

        Account account2 = new Account();
        account2.setAccountNumber("123456");
        account2.setAccountType("T1234567899");
        account2.setSortCode("121214");
        account2.setProductType("C");
        account2.setManufacturingLegalEntity("LTB");
        account1.setOverdraftLimit(70.0);

        accounts.add(account1);
        accounts.add(account2);

        return accounts;
    }

    private Map<String, Object> createVersionInfoData(){
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");
        versionInfoMap.put("environmentName","DEV");
        //environmentName
        return versionInfoMap;
    }

    private Map<String, Object> createBrandMap(){
        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "true");
        return brandMap;
    }

    private SelectedAccount selectedAccount(){
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("125672132");
        selectedAccount.setSortCode("09-23-98");
        return selectedAccount;
    }

}
