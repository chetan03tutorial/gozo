/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.overdraft.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.CFCNewLimitResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftRequest;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftResponse;
import com.lbg.ib.api.sales.overdraft.service.UpfrontOverdraftService;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.CreditAssessmentQ250Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.product.service.PcaDetermineEligibilityService;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.sales.user.service.AccountPortTypeService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpfrontOverdraftServiceTest {

    @InjectMocks
    private UpfrontOverdraftService service;

    @Mock
    private ApiServiceProperties apiServiceProperties;

    @Mock
    private AccountPortTypeService accountPortTypeService;
    @Mock
    private SearchPartyService searchPartyService;
    @Mock
    private RetrieveProductFeaturesService retrieveProductFeaturesService;
    @Mock
    private ConfigurationDAO configuration;
    @Mock
    private CreditAssessmentQ250Service creditAssessmentQ250Service;
    @Mock
    private RetrievePartyDetailsService retrievePartyDetailsService;
    @Mock
    private PcaDetermineEligibilityService pcaDetermineEligibilityService;

    @Mock
    private GalaxyErrorCodeResolver         resolver;

    @Mock
    private LoggerDAO                       logger;

    @Mock
    private SessionManagementDAO            session;

    private final ResponseError INVALID_ACCOUNT_NUMBER_SORT_CODE            = new ResponseError("9310017",
            "INVALID_ACCOUNT_NUMBER_SORT_CODE");
    private final ResponseError MORE_THAN_TWO_PARTY_FOUND            = new ResponseError("9310011",
            "MORE_THAN_TWO_PARTY_FOUND");
    private final ResponseError MISSING_EMAIL_ADDRESS            = new ResponseError("9310012",
            "MISSING_EMAIL_ADDRESS");
    private final ResponseError ERROR_RESPONSE_FROM_Q250        = new ResponseError("9310016",
            "ERROR_RESPONSE_FROM_Q250");

    private final ResponseError NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY        = new ResponseError("9310020",
            "NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY");


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        when(session.getUserInfo()).thenReturn(arrangement());
        when(session.getUserContext()).thenReturn(new UserContext("111", "0.0.0.0", "1212", "2222", "1111", "1111",
                "5555", "555", "555", "22", "66"));
        when(resolver.resolve(anyString())).thenReturn(new ResponseError("code", "message"));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnInvalidAccountException() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("12345");
        request.setSortCode("503020");
        when(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE))
                .thenReturn(INVALID_ACCOUNT_NUMBER_SORT_CODE);

        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNull(response);
       // thrown.expectCause(new ServiceException(INVALID_ACCOUNT_NUMBER_SORT_CODE));
    }
    
    @Test(expected = InvalidFormatException.class)
    public void shouldReturnInvalidFormatExceptionWhenNoAccountInSession() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("12345");
        request.setSortCode("503020");
        Arrangement arrangement = new Arrangement();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE))
                .thenReturn(INVALID_ACCOUNT_NUMBER_SORT_CODE);

        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNull(response);
       // thrown.expectCause(new ServiceException(INVALID_ACCOUNT_NUMBER_SORT_CODE));
    }

    @Test
    public void shouldNotReturnExceptionWhenNullResponseFromQ250() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<String> ocisIds = Lists.newArrayList();
        ocisIds.add("807610516");
        IBParties party = new IBParties();
        party.setPartyId("123");
        party.setFirstName("Amit");
        party.setLastName("Gupta");
        party.setBirthDate("1995-11-23");
        List<IBParties> list = new ArrayList<IBParties>();
        list.add(party);


        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(createVersionInfoData());
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(createBrandMap());

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("106");
        /*when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0107", "098"))));*/
//        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
//                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
//                        (new TriadResultDTO("0107", "098"))),Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("1234567");
        selectedAccount.setSortCode("503020");
        when(session.getAccountToConvertInContext()).thenReturn(selectedAccount);
        when(resolver.resolve(ResponseErrorConstants.ERROR_RESPONSE_FROM_Q250))
                .thenReturn(ERROR_RESPONSE_FROM_Q250);

        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNotNull(response);
    }
    @Test
    public void shouldReturnNotNullResponseFromQ250() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<String> ocisIds = Lists.newArrayList();
        ocisIds.add("807610516");
        IBParties party = new IBParties();
        party.setPartyId("123");
        party.setFirstName("Amit");
        party.setLastName("Gupta");
        party.setBirthDate("1995-11-23");
        List<IBParties> list = new ArrayList<IBParties>();
        list.add(party);


        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(createVersionInfoData());
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(createBrandMap());

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("106");
        /*when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0107", "098"))));*/
        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))),Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber("1234567");
        selectedAccount.setSortCode("503020");
        when(session.getAccountToConvertInContext()).thenReturn(selectedAccount);
        when(resolver.resolve(ResponseErrorConstants.ERROR_RESPONSE_FROM_Q250))
                .thenReturn(ERROR_RESPONSE_FROM_Q250);

        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNotNull(response);
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenMoreThanTWOPartyFound() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<String> ocisIds = Lists.newArrayList();
        ocisIds.add("807610517");
        ocisIds.add("807610518");
        ocisIds.add("807610519");
//        when(accountPortTypeService.retrieveJointPartyOcisIds(any(Account.class)))
//                .thenReturn(ocisIds);
        IBParties party = new IBParties();
        party.setPartyId("123");
        party.setFirstName("Amit");
        party.setLastName("Gupta");
        party.setBirthDate("1995-11-23");
        List<IBParties> list = new ArrayList<IBParties>();
        list.add(party);

        IBParties party1 = new IBParties();
        party1.setPartyId("123");
        party1.setFirstName("Amit1");
        party1.setLastName("Gupta1");
        party1.setBirthDate("1995-11-21");
        list.add(party1);


        IBParties party2 = new IBParties();
        party2.setPartyId("124");
        party2.setFirstName("Amit2");
        party2.setLastName("Gupta2");
        party2.setBirthDate("1995-11-22");
        list.add(party2);

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        PartyDetails secondaryPartyDetails = new PartyDetails();
        secondaryPartyDetails.setIsPrimaryParty(false);
        when(retrievePartyDetailsService.getPartyDetails(anyString())).thenReturn(secondaryPartyDetails);
        when(resolver.resolve(ResponseErrorConstants.MORE_THAN_TWO_PARTY_FOUND))
                .thenReturn(MORE_THAN_TWO_PARTY_FOUND);
        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNull(response);
    }
    
    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenNoPrimaryPartyFound() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<IBParties> list = new ArrayList<IBParties>();
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);

        when(resolver.resolve(ResponseErrorConstants.NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY))
                .thenReturn(NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY);
        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNull(response);
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenNoPartyMatched() throws ServiceException {
        UpfrontOverdraftRequest request = new UpfrontOverdraftRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<IBParties> list = new ArrayList<IBParties>();
        IBParties party = new IBParties();
        party.setPartyId("123");
        party.setFirstName("Amit11");
        party.setLastName("Gupta11");
        party.setBirthDate("1995-11-23");
        list.add(party);

        IBParties party1 = new IBParties();
        party1.setPartyId("123");
        party1.setFirstName("Amit1");
        party1.setLastName("Gupta1");
        party1.setBirthDate("1995-11-21");
        list.add(party1);

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);

        when(resolver.resolve(ResponseErrorConstants.NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY))
                .thenReturn(NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY);
        UpfrontOverdraftResponse response = service.upfrontOverdraft(request);
        assertNull(response);
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
}
