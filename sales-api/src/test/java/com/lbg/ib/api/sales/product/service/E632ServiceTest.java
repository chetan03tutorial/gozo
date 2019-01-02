package com.lbg.ib.api.sales.product.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lloydsbanking.xml.E632Resp;
import com.lloydsbanking.xml.E632Result;
import com.lloydsbanking.xml.E632_ODIntDetails_ServiceLocator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lloydstsb.ea.lcsm.ResponseHeader;

@RunWith(MockitoJUnitRunner.class)
public class E632ServiceTest {

    @Mock
    private SOAInvoker soaInvoker;

    @InjectMocks
    private E632ServiceImpl service;

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private ConfigurationDAO configManager;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private HandlerRegistry handleRegistry;

    @Mock
    private LoggerDAO logger;

    @Mock
    private E632_ODIntDetails_ServiceLocator serviceLocator;
    private static final String brand = "LTB";
    private static final String productMnemonic = "P_CLUB";

    @Before
    public void setup() {

        when(serviceLocator.getE632_ODIntDetailsWSDDPortName()).thenReturn("E632");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("E632"));
        when(configManager.getConfigurationStringValue(anyString(), anyString())).thenReturn(brand);
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(SessionServiceUtil.buildParties(2));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(E632_ODIntDetails_ServiceLocator.class)).thenReturn(serviceLocator);
        when(sessionManager.getAvailableUpgradeOptions()).thenReturn(upgradeOptions());
        when(sessionManager.getAccountToConvertInContext()).thenReturn(selectedAccount());
        when(sessionManager.getUserInfo()).thenReturn(getArrangement());
    }

    @Test(expected = Exception.class)
    public void testE632Fail() {
        service.e632("P_PLAT");
    }

    @Test(expected = Exception.class)
    public void testE632Fails() {
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic(productMnemonic);
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(null);
        service.e632(productMnemonic);
    }

    @Test
    public void testE632Success() {
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic(productMnemonic);
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        service.e632(productMnemonic);
    }

    @Test(expected = Exception.class)
    public void testE632Fail1() {
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic(productMnemonic);
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalFailServiceResponse());
        service.e632(productMnemonic);
    }
    
    
    @Test
    public void e632WithOverDraftOptionSuccess() {
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic(productMnemonic);
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        PcciOverdraftRequest overDraftOption=new PcciOverdraftRequest("1234");
        service.e632WithOverDraftOption(overDraftOption);
    }

    private Map<String, UpgradeOption> upgradeOptions() {
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic(productMnemonic);
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        Map<String, UpgradeOption> upgrade = Maps.newHashMap();
        upgrade.put(productMnemonic, option);
        return upgrade;

    }

    private SelectedAccount selectedAccount() {
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        return account;
    }

    private Arrangement getArrangement() {
        Arrangement arrangement = new Arrangement();
        Account account = new Account();
        account.setAccountNumber("12391131");
        account.setOverdraftLimit(100.00);
        account.setSortCode("110267");
        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account);
        arrangement.setAccounts(accounts);
        return arrangement;
    }

    private E632Resp externalSuccessServiceResponse() {
        E632Resp externalServiceResponse = new E632Resp();
        externalServiceResponse.setExcessFeeAm("00");
        E632Result e632Result = new E632Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(0);
        resultCondition.setReasonText("No Error Code");
        resultCondition.setSeverityCode(SeverityCode.value1);
        e632Result.setResultCondition(resultCondition);
        externalServiceResponse.setE632Result(e632Result);
        externalServiceResponse.setFeeFreeOvrdrtAm("100");
        ResultCondition extraConditon = new ResultCondition();
        extraConditon.setReasonCode(0);
        extraConditon.setSeverityCode(SeverityCode.value1);
        extraConditon.setReasonText("No Error");
        resultCondition.setExtraConditions(new ResultCondition[]{extraConditon});
        return externalServiceResponse;
    }

    private E632Resp externalFailServiceResponse() {
        E632Resp externalServiceResponse = new E632Resp();
        externalServiceResponse.setExcessFeeAm("00");
        E632Result e632Result = new E632Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(0);
        resultCondition.setReasonText("No Error Code");
        resultCondition.setSeverityCode(SeverityCode.value2);
        e632Result.setResultCondition(resultCondition);
        externalServiceResponse.setE632Result(e632Result);
        externalServiceResponse.setFeeFreeOvrdrtAm("100");
        ResultCondition extraConditon = new ResultCondition();
        extraConditon.setReasonCode(0);
        extraConditon.setSeverityCode(SeverityCode.value1);
        extraConditon.setReasonText("No Error");
        resultCondition.setExtraConditions(new ResultCondition[]{extraConditon});
        return externalServiceResponse;
    }

}
