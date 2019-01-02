/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.overdraft.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.SelectedAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountSessionInfoServiceTest {

    @InjectMocks
    private AccountSessionInfoService service;
    @Mock
    private ApiServiceProperties apiServiceProperties;
    @Mock
    private AccountInContextUtility contextUtility;
    @Mock
    private ConfigurationDAO configuration;
    @Mock
    private GalaxyErrorCodeResolver         resolver;

    @Mock
    private LoggerDAO                       logger;

    @Mock
    private SessionManagementDAO            session;

    private final ResponseError ERROR_RESPONSE_FROM_Q250        = new ResponseError("9310016",
            "ERROR_RESPONSE_FROM_Q250");

    private final ResponseError NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY        = new ResponseError("9310020",
            "NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY");


    @Before
    public void setup() {
        when(session.getUserContext()).thenReturn(new UserContext("111", "0.0.0.0", "1212", "2222", "1111", "1111",
                "5555", "555", "555", "22", "66"));
    }

    @Test
    public void shouldReturnSelectedAccountSessionInfo() throws ServiceException {

        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters());
        when(session.getCustomerDetails()).thenReturn(primaryCustomerInfo());

        SelectedAccountSessionInfo response = service.fetchSessionDetail();
        assertNotNull(response);
        assertEquals(response.getProductMnemonic(), "P_CLASSIC");
    }
    

    private ArrangeToActivateParameters arrangeToActivateParameters() {
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setActivationStatus(CommonConstant.APPLICATION_SUCESS_STATUS);
        arrangeToActivateParameters.setAmtOverdraft(new BigDecimal(200.0));
        arrangeToActivateParameters.setArrangementId("1234");
        arrangeToActivateParameters.setCbsProductNumber("1234567891");
        arrangeToActivateParameters.setOcisId("1111111111");
        arrangeToActivateParameters.setPartyId("+323232134");
        arrangeToActivateParameters.setProductId("122221221");
        arrangeToActivateParameters.setProductName("Classic Account");
        arrangeToActivateParameters.setProductMnemonic("P_CLASSIC");
        return arrangeToActivateParameters;
    }

    private CustomerInfo primaryCustomerInfo() {
        CustomerInfo primaryCustomerInfo = new CustomerInfo();
        primaryCustomerInfo.setEmail("yarm@test.com");
        primaryCustomerInfo.setAccountNumber("22232323");
        primaryCustomerInfo.setSortCode("203020");
        primaryCustomerInfo.setForeName("Yarm");
        primaryCustomerInfo.setSurName("primary");
        primaryCustomerInfo.setTitle("Mr");
        primaryCustomerInfo.setArrangementId("1111");
        primaryCustomerInfo.setCurrentAddress(currentAddress());
        primaryCustomerInfo.setCustomerNumber("6543222");
        primaryCustomerInfo.setDob(new Date());
        primaryCustomerInfo.setUserName("yarmuser");
        primaryCustomerInfo.setSwitchingDate(new Date());

        return primaryCustomerInfo;
    }

    private PostalAddressComponent currentAddress() {
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        UnstructuredPostalAddress unAddress = new UnstructuredPostalAddress("ss", "ss", "sdsd", "sds", "sdsd", "sdsd",
                "sdsd", "sdsd", "E201BF", "2T");

        currentAddress.setUnstructuredAddress(unAddress);
        PostalAddress stAddress = new PostalAddress();
        stAddress.setBuildingNumber("45");
        stAddress.setBuildingName("East Village");
        stAddress.setPostcode("HA9 0EY");
        stAddress.setOrganisationName("Lloyds");
        stAddress.setTown("Wembley");
        List<String> addresList = new ArrayList<String>();
        addresList.add("22 Egremont House");
        stAddress.setAddressLines(addresList);
        currentAddress.setStructuredAddress(stAddress);
        return currentAddress;
    }

    private Arrangement arrangement() {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setFirstName("Amit");
        primaryInvolvedParty.setLastName("Gupta");
        primaryInvolvedParty.setEmail("abc@abc.com");
        primaryInvolvedParty.setDob("1995-11-23");

        List<Account> accounts = Lists.newArrayList();
        Account account1 = new Account();
        account1.setAccountNumber("1234567");
        account1.setSortCode("503020");
        account1.setAccountStatus("ACTIVE");
        account1.setAccountName("First");
        account1.setAccountType("T3001116001");

        Account account2 = new Account();
        account2.setAccountNumber("987654");
        account2.setSortCode("404040");
        account2.setAccountStatus("ACTIVE");
        account2.setAccountName("Second");
        account2.setAccountType("T245222222");

        accounts.add(account1);
        accounts.add(account2);
        arrangement.setAccounts(accounts);
        arrangement.setOcisId("807610516");
        arrangement.setPartyId("+00433279025");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        return arrangement;
    }
}
