package com.lbg.ib.api.sales.conversion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.activatebenefitarrangement.service.ActivateBenefitArrangementService;
import com.lbg.ib.api.sales.cbs.service.E592Service;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.service.CommunicationDispatchService;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionRequest;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionResponse;
import com.lbg.ib.api.sales.conversion.service.ConversionServiceImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.party.service.CardOrderService;
import com.lbg.ib.api.sales.party.service.OcisC542Service;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import com.lbg.ib.api.sales.product.service.E632Service;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConversionServiceTest {

    @Mock
    private CommunicationDispatchService communicationDispatchService;

    @Mock
    private E592Service e592Service;

    @Mock
    private OcisC542Service c542Service;

    @Mock
    private ModuleContext beansLoader;

    @Mock
    private SessionManagementDAO session;

    @InjectMocks
    private ConversionServiceImpl service;

    @Mock
    private E632Service e632Service;

    @Mock
    private CardOrderService cardOrderService;

    @Mock
    private ActivateBenefitArrangementService activateBenefitArrangementService;

    @Mock
    private LoggerDAO logger;
    
    @Mock
    private ChannelBrandingDAO            channelBrandingService;

    @Mock
    private DAOResponse<ChannelBrandDTO>  channelBrandDTOResponse;

    @Mock
    private ChannelBrandDTO               channelBrandDTO;

    @Before
    public void setup() {
        when(session.getUserInfo()).thenReturn(arrangement());
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("1234567");
        selectedAccount.setSortCode("503020");
        when(session.getAccountToConvertInContext()).thenReturn(selectedAccount);
    }

    @Test
    public void testConversion() {
        AccountConversionRequest conversionRequest = new AccountConversionRequest();
        when(beansLoader.getService(E592Service.class)).thenReturn(e592Service);
        conversionRequest.setProductMnemonic("P_CLUB");
        e592Service.convertProductE592(any(SelectedAccount.class), any(UpgradeOption.class));
        when(beansLoader.getService(OcisC542Service.class)).thenReturn(c542Service);
        when(beansLoader.getService(E632Service.class)).thenReturn(e632Service);
        when(session.getAvailableUpgradeOptions()).thenReturn(getAvailableUpgradeOptions());
        when(beansLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(communicationDispatchService.scheduleCommunication(any(ScheduleEmailSmsRequest.class))).thenReturn(emailResponse());
        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);
        when(channelBrandingService.getChannelBrand().getResult().getBrand()).thenReturn("LLOYDS");
        AccountConversionResponse conversionResponse = service.convert(conversionRequest, "Q226");
        assertNotNull(conversionResponse);
        assertEquals(conversionResponse.isUpgradeDone(), true);
        assertEquals(conversionResponse.isUpdateOcisDone(), true);
        assertEquals(conversionResponse.isLifeStyleDone(), false);
    }

    @Test(expected = Exception.class)
    public void testConversionFail() {
        AccountConversionRequest conversionRequest = new AccountConversionRequest();
        when(beansLoader.getService(E592Service.class)).thenReturn(e592Service);
        conversionRequest.setProductMnemonic("P_CLUB");
        e592Service.convertProductE592(any(SelectedAccount.class), any(UpgradeOption.class));
        when(beansLoader.getService(OcisC542Service.class)).thenReturn(c542Service);
        when(beansLoader.getService(E632Service.class)).thenReturn(e632Service);
        when(beansLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(communicationDispatchService.scheduleCommunication(any(ScheduleEmailSmsRequest.class))).thenReturn(emailResponse());
        AccountConversionResponse conversionResponse = service.convert(conversionRequest, "Q226");
        assertNotNull(conversionResponse);
    }

    @Test()
    public void testConversion1() {
        AccountConversionRequest conversionRequest = new AccountConversionRequest();
        when(beansLoader.getService(E592Service.class)).thenReturn(e592Service);
        conversionRequest.setProductMnemonic("P_CLUB");
        conversionRequest.setCardUpgradeReq(Boolean.TRUE);
        e592Service.convertProductE592(any(SelectedAccount.class), any(UpgradeOption.class));
        when(beansLoader.getService(OcisC542Service.class)).thenReturn(c542Service);
        when(beansLoader.getService(E632Service.class)).thenReturn(e632Service);
        when(beansLoader.getService(CardOrderService.class)).thenReturn(cardOrderService);
        when(session.getAvailableUpgradeOptions()).thenReturn(getAvailableUpgradeOptions());
        when(beansLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(session.getTriadDetails()).thenReturn(getTriadResultDTO());
        when(communicationDispatchService.scheduleCommunication(any(ScheduleEmailSmsRequest.class))).thenReturn(emailResponse());
        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);
        when(channelBrandingService.getChannelBrand().getResult().getBrand()).thenReturn("LLOYDS");
        AccountConversionResponse conversionResponse = service.convert(conversionRequest, "Q226");
        assertNotNull(conversionResponse);
        assertEquals(conversionResponse.isUpgradeDone(), true);
        assertEquals(conversionResponse.isUpdateOcisDone(), true);
        assertEquals(conversionResponse.isLifeStyleDone(), false);
    }

    @Test()
    public void testConversion2() {
        AccountConversionRequest conversionRequest = new AccountConversionRequest();
        when(beansLoader.getService(E592Service.class)).thenReturn(e592Service);
        conversionRequest.setProductMnemonic("P_CLUB");
        conversionRequest.setCardUpgradeReq(Boolean.TRUE);
        conversionRequest.setLifeStyleReq(Boolean.TRUE);
        e592Service.convertProductE592(any(SelectedAccount.class), any(UpgradeOption.class));
        when(beansLoader.getService(OcisC542Service.class)).thenReturn(c542Service);
        when(beansLoader.getService(E632Service.class)).thenReturn(e632Service);
        when(beansLoader.getService(CardOrderService.class)).thenReturn(cardOrderService);
        when(session.getAvailableUpgradeOptions()).thenReturn(getAvailableUpgradeOptions());
        when(activateBenefitArrangementService.createArrangementSetupService(any(CreateServiceArrangement.class))).thenReturn(null);
        when(beansLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(communicationDispatchService.scheduleCommunication(any(ScheduleEmailSmsRequest.class))).thenReturn(emailResponse());
        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);
        when(channelBrandingService.getChannelBrand().getResult().getBrand()).thenReturn("LLOYDS");
        AccountConversionResponse conversionResponse = service.convert(conversionRequest, "Q226");
        assertEquals(conversionResponse.isUpgradeDone(), true);
        assertEquals(conversionResponse.isUpdateOcisDone(), true);
        assertEquals(conversionResponse.isLifeStyleDone(), true);
    }

    @Test()
    public void testConversion2_LifeStyleFailure() {
        AccountConversionRequest conversionRequest = new AccountConversionRequest();
        when(beansLoader.getService(E592Service.class)).thenReturn(e592Service);
        conversionRequest.setProductMnemonic("P_CLUB");
        conversionRequest.setCardUpgradeReq(Boolean.TRUE);
        conversionRequest.setLifeStyleReq(Boolean.TRUE);
        e592Service.convertProductE592(any(SelectedAccount.class), any(UpgradeOption.class));
        when(beansLoader.getService(OcisC542Service.class)).thenReturn(c542Service);
        when(beansLoader.getService(E632Service.class)).thenReturn(e632Service);
        when(beansLoader.getService(CardOrderService.class)).thenReturn(cardOrderService);
        when(session.getAvailableUpgradeOptions()).thenReturn(getAvailableUpgradeOptions());
        when(activateBenefitArrangementService.createArrangementSetupService(any(CreateServiceArrangement.class))).
                thenThrow(new ServiceException(new ResponseError("Error in response of LifeStyle API", "Error in response of LifeStyle API")));
        when(beansLoader.getService(CommunicationDispatchService.class)).thenReturn(communicationDispatchService);
        when(communicationDispatchService.scheduleCommunication(any(ScheduleEmailSmsRequest.class))).thenReturn(emailResponse());
        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);
        when(channelBrandingService.getChannelBrand().getResult().getBrand()).thenReturn("LLOYDS");
        AccountConversionResponse conversionResponse = service.convert(conversionRequest, "Q226");
        assertEquals(conversionResponse.isUpgradeDone(), true);
        assertEquals(conversionResponse.isUpdateOcisDone(), true);
        assertEquals(conversionResponse.isLifeStyleDone(), false);
    }

    private TriadResultDTO getTriadResultDTO() {
        TriadResultDTO dto = new TriadResultDTO("1005", "1005");
        return dto;
    }

    private Map<String, UpgradeOption> getAvailableUpgradeOptions() {
        Map<String, UpgradeOption> availableUpgradeOptions = Maps.newHashMap();
        UpgradeOption upgradeOption = new UpgradeOption();
        upgradeOption.setMnemonic("P_CLUB");
        upgradeOption.setEligible(Boolean.TRUE);
        upgradeOption.setTariff("1");
        upgradeOption.setVantageEligible(Boolean.TRUE);
        upgradeOption.setProductIdentifier("3001");
        String cbsProductIds = "3001";
        List<String> cbsList = new ArrayList();
        cbsList.add(cbsProductIds);
        upgradeOption.setCbsProductIds(cbsList);
        availableUpgradeOptions.put("P_CLUB", upgradeOption);
        return availableUpgradeOptions;

    }

    private ScheduleEmailSmsResponse emailResponse() {
        ScheduleEmailSmsResponse response = new ScheduleEmailSmsResponse();
        return response;
    }

    private Arrangement arrangement() {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setFirstName("Amit");
        primaryInvolvedParty.setLastName("Gupta");
        primaryInvolvedParty.setEmail("abc@abc.com");
        primaryInvolvedParty.setDob("1995-11-23");
        ContactNumber contactNumber = new ContactNumber("+44", "020", "23456723", "222");
        primaryInvolvedParty.setMobileNumber(contactNumber);

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
