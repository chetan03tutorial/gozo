package com.lbg.ib.api.sales.cbs.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.E592Resp;
import com.lloydsbanking.xml.E592Result;
import com.lloydsbanking.xml.E592_ChaCBSTrnsfrProd_ServiceLocator;

@RunWith(MockitoJUnitRunner.class)
public class E592ServiceTest {

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private SOAInvoker soaInvoker;

    @InjectMocks
    private E592ServiceImpl service;

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
    private SessionManagementDAO session;

    @Mock
    BranchContext branchContext;

    @Mock
    private E592_ChaCBSTrnsfrProd_ServiceLocator serviceLocator;
    private static final String brand = "LTB";
    private static final String productMnemonic = "P_CLUB";

    @Before
    public void setup() {

        when(serviceLocator.getE592_ChaCBSTrnsfrProdWSDDPortName()).thenReturn("e592");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("e592"));
        when(configManager.getConfigurationStringValue(anyString(), anyString())).thenReturn(brand);
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(SessionServiceUtil.buildParties(2));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(E592_ChaCBSTrnsfrProd_ServiceLocator.class)).thenReturn(serviceLocator);
        when(sessionManager.getAccountToConvertInContext()).thenReturn(selectedAccount());
        when(sessionManager.getAvailableUpgradeOptions()).thenReturn(getAvailableUpgradeOptions());
        when(session.getBranchContext()).thenReturn(branchContext);
        when(branchContext.getOriginatingSortCode()).thenReturn("111111");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testE592Success() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic("P_PLAT");
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        service.convertProductE592(account, option);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void testE592Fail() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic("P_PLAT");
        option.setTariff("1");
        service.convertProductE592(account, option);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void testE592Fail2() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalFailServiceResponse());
        SelectedAccount account = new SelectedAccount();
        account.setAccountNumber("12391131");
        account.setSortCode("110267");
        UpgradeOption option = new UpgradeOption();
        option.setEligible(false);
        option.setMnemonic("P_PLAT");
        option.setTariff("1");
        List<String> cbsId = new ArrayList<String>();
        cbsId.add("1345567");
        option.setCbsProductIds(cbsId);
        service.convertProductE592(account, option);
        ;
    }

    private E592Resp externalSuccessServiceResponse() {
        E592Resp externalServiceResponse = new E592Resp();
        E592Result e592Result = new E592Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(0);
        e592Result.setResultCondition(resultCondition);
        externalServiceResponse.setE592Result(e592Result);
        return externalServiceResponse;
    }

    private E592Resp externalFailServiceResponse() {
        E592Resp externalServiceResponse = new E592Resp();
        E592Result e592Result = new E592Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(SeverityCode.value3);
        resultCondition.setReasonCode(1);
        e592Result.setResultCondition(resultCondition);
        externalServiceResponse.setE592Result(e592Result);
        return externalServiceResponse;
    }

    private SelectedAccount selectedAccount() {
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("125672132");
        selectedAccount.setSortCode("092398");
        return selectedAccount;
    }

    private Map<String, UpgradeOption> getAvailableUpgradeOptions() {
        Map<String, UpgradeOption> availableUpgradeOptions = Maps.newHashMap();
        UpgradeOption upgradeOption = new UpgradeOption();
        upgradeOption.setMnemonic(productMnemonic);
        upgradeOption.setEligible(Boolean.TRUE);
        upgradeOption.setTariff("1");
        upgradeOption.setVantageEligible(Boolean.TRUE);
        upgradeOption.setProductIdentifier("3001");
        availableUpgradeOptions.put(productMnemonic, upgradeOption);
        return availableUpgradeOptions;

    }
}
