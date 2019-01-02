/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.CFCNewLimitResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.domain.response.ElectronicAddress;
import com.lbg.ib.api.sales.party.domain.response.PartyDetail;
import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.party.service.CreditAssessmentQ250Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
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

@RunWith(MockitoJUnitRunner.class)
public class UpgradeEligibilityServiceTest {

    @InjectMocks
    private UpgradeEligibilityService service;

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
    private final ResponseError NO_ELIGIBLE_PRODUCT_FOUND            = new ResponseError("9310015",
            "NO_ELIGIBLE_PRODUCT_FOUND");
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
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
        request.setAccountNumber("12345");
        request.setSortCode("503020");
        when(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE))
                .thenReturn(INVALID_ACCOUNT_NUMBER_SORT_CODE);

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
       // thrown.expectCause(new ServiceException(INVALID_ACCOUNT_NUMBER_SORT_CODE));
    }
    
    @Test(expected = InvalidFormatException.class)
    public void shouldReturnInvalidFormatExceptionWhenNoAccountInSession() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
        request.setAccountNumber("12345");
        request.setSortCode("503020");
        Arrangement arrangement = new Arrangement();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE))
                .thenReturn(INVALID_ACCOUNT_NUMBER_SORT_CODE);

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
       // thrown.expectCause(new ServiceException(INVALID_ACCOUNT_NUMBER_SORT_CODE));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnExceptionWhenNoEligibleProductForSingleParty() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("LTB");
        /*when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))));*/
        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))),Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));
        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductFamilyIdentifier(any(RetrieveProductDTO.class)))
                .thenReturn(productFeatures_productIdentifierMatchesWithSelectedAccountType());
        when(resolver.resolve(ResponseErrorConstants.NO_ELIGIBLE_PRODUCT_FOUND))
                .thenReturn(NO_ELIGIBLE_PRODUCT_FOUND);

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnExceptionWhenNullResponseFromQ250() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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


        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("106");
        /*when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0107", "098"))));*/
        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))),Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));
        when(resolver.resolve(ResponseErrorConstants.ERROR_RESPONSE_FROM_Q250))
                .thenReturn(ERROR_RESPONSE_FROM_Q250);

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
        verify(session, times(1)).setMaxOverDraftLimit("+000100");
    }

    public void shouldReturnNotNullResponseFromQ250() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }

    @Test
    public void shouldReturnTwoEligibleProductForSingleParty() {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);

        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("106");
        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))),Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));
        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductFamilyIdentifier(any(RetrieveProductDTO.class)))
                .thenReturn(productFeatures_productIdentifierDoesNotMatchesWithSelectedAccountType());

        when(pcaDetermineEligibilityService.determineUpgradeEligibility(any(PcaDetermineEligibilityRequest.class),
                any(Arrangement.class), any(List.class), any(List.class)))
                .thenReturn(determineEligibilityResponse());

        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNotNull(response);
        assertEquals("Successfully fetched the upgrade eligibility details.", response.getMsg());
        assertEquals(2, response.getProducts().size());
        verify(session, times(1)).setMaxOverDraftLimit("+000100");
    }

    @Test
    public void shouldReturnTwoEligibleProductForOneJointParty() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<String> ocisIds = Lists.newArrayList();
        ocisIds.add("807610517");
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

        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);

        RetrievePartyDetailsResponse retrievePartyDetailsResponse = new RetrievePartyDetailsResponse();
        PartyDetail partyDetail = new PartyDetail();
        partyDetail.setTitle("Mr.");
        partyDetail.setFirstName("Andrea");
        partyDetail.setLastName("Spellucci");

        ElectronicAddress electronicAddress = new ElectronicAddress();
        electronicAddress.setAddress("abc@abc.com");
        electronicAddress.setContactPointStatus("ACTIVE");
        electronicAddress.setElectronicAddressType("EMAIL");
        List<ElectronicAddress> addressList = new ArrayList<ElectronicAddress>();
        addressList.add(electronicAddress);

        retrievePartyDetailsResponse.setElectronicAddress(addressList);
        retrievePartyDetailsResponse.setParty(partyDetail);
//        when(retrievePartyDetailsService.retrievePartyDetails(anyString())).thenReturn(retrievePartyDetailsResponse);
        PartyDetails secondaryPartyDetails = new PartyDetails();
        when(retrievePartyDetailsService.getPartyDetails(anyString())).thenReturn(secondaryPartyDetails);
        secondaryPartyDetails.setIsPrimaryParty(false);

        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("106");
        when(creditAssessmentQ250Service.retrieveCreditAssessmentData(any(TriadRequestDTO.class))).
                thenReturn(new TriadCFCResultDTO(Lists.<TriadResultDTO>newArrayList((new TriadResultDTO("0106", "100")),
                        (new TriadResultDTO("0107", "098"))),
                        Lists.<CFCNewLimitResultDTO>newArrayList((new CFCNewLimitResultDTO("+0106", "+000100")))));

        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductFamilyIdentifier(any(RetrieveProductDTO.class)))
                .thenReturn(productFeatures_productIdentifierDoesNotMatchesWithSelectedAccountType());

        when(pcaDetermineEligibilityService.determineUpgradeEligibility(any(PcaDetermineEligibilityRequest.class),
                any(Arrangement.class), any(List.class), any(List.class)))
                .thenReturn(determineEligibilityResponse());


        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNotNull(response.getMsg());
        assertEquals(2, session.getUserInfo().getAccounts().size());
        assertEquals("Successfully fetched the upgrade eligibility details.", response.getMsg());
        assertEquals(2, response.getProducts().size());
        verify(session, times(1)).setMaxOverDraftLimit("+000100");

    }
    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenMoreThanTWOPartyFound() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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
        when(retrievePartyDetailsService.getPartyDetails(anyString())).thenReturn(secondaryPartyDetails);
        secondaryPartyDetails.setIsPrimaryParty(false);
        when(resolver.resolve(ResponseErrorConstants.MORE_THAN_TWO_PARTY_FOUND))
                .thenReturn(MORE_THAN_TWO_PARTY_FOUND);
        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }
    
    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenNoPrimaryPartyFound() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<IBParties> list = new ArrayList<IBParties>();
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);

        when(resolver.resolve(ResponseErrorConstants.NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY))
                .thenReturn(NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY);
        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenNoPartyMatched() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
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
        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }

    /*@Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenEmailNotFoundForOtherParty() throws ServiceException {
        UpgradeEligibilityRequest request = new UpgradeEligibilityRequest();
        request.setAccountNumber("1234567");
        request.setSortCode("503020");

        List<String> ocisIds = Lists.newArrayList();
        ocisIds.add("807610517");

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

        PartyDetails secondaryPartyDetails = new PartyDetails();
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class))).thenReturn(list);
        when(configuration.getConfigurationStringValue(anyString(),anyString())).thenReturn("LTB").thenReturn("106");
        RetrievePartyDetailsResponse retrievePartyDetailsResponse = new RetrievePartyDetailsResponse();
        retrievePartyDetailsResponse.setElectronicAddress(new ArrayList<ElectronicAddress>());
        when(retrievePartyDetailsService.getPartyDetails(anyString())).thenReturn(secondaryPartyDetails);

        when(resolver.resolve(ResponseErrorConstants.MISSING_EMAIL_ADDRESS))
                .thenReturn(MISSING_EMAIL_ADDRESS);
        UpgradeEligibilityResponse response = service.upgradeEligibility(request);
        assertNull(response.getMsg());
    }*/


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

    private List<ProductFeatures> productFeatures_productIdentifierMatchesWithSelectedAccountType() {
        List<ProductFeatures> productFeaturesList = Lists.newArrayList();
        List<ProductAttributes> attributes_1 = Lists.newArrayList();
        attributes_1.add(new ProductAttributes("isVantage", "No"));
        attributes_1.add(new ProductAttributes("ICOBS", "N"));
        attributes_1.add(new ProductAttributes("Tariff", "1"));
        attributes_1.add(new ProductAttributes("Product priority", "2"));
        attributes_1.add(new ProductAttributes("Account Switching", "Y"));
        attributes_1.add(new ProductAttributes("Overdraft Offer Flag", "Y"));

        Map<String, List<String>> extProdIdentifiers_1 = Maps.newHashMap();
        extProdIdentifiers_1.put("00004", Lists.newArrayList("3001116000", "3001116001"));
        extProdIdentifiers_1.put("00107", Lists.newArrayList("2901", "2201"));
        extProdIdentifiers_1.put("00010", Lists.newArrayList("P_REWARD"));

        ProductFeatures productFeatures_reward = new ProductFeatures(new Product("Reward Current Account", "3001", "P_REWARD",
                "107116", attributes_1, extProdIdentifiers_1), null);
        productFeaturesList.add(productFeatures_reward);
        return productFeaturesList;
    }

    private List<ProductFeatures> productFeatures_productIdentifierDoesNotMatchesWithSelectedAccountType() {
        List<ProductFeatures> productFeaturesList = Lists.newArrayList();
        List<ProductAttributes> attributes_1 = Lists.newArrayList();
        attributes_1.add(new ProductAttributes("isVantage", "No"));
        attributes_1.add(new ProductAttributes("ICOBS", "N"));
        attributes_1.add(new ProductAttributes("Tariff", "1"));
        attributes_1.add(new ProductAttributes("Product priority", "2"));
        attributes_1.add(new ProductAttributes("Account Switching", "Y"));
        attributes_1.add(new ProductAttributes("Overdraft Offer Flag", "Y"));

        Map<String, List<String>> extProdIdentifiers_1 = Maps.newHashMap();
        extProdIdentifiers_1.put("00004", Lists.newArrayList("3001116033", "3001116044"));
        extProdIdentifiers_1.put("00107", Lists.newArrayList("2901", "2201"));
        extProdIdentifiers_1.put("00010", Lists.newArrayList("P_REWARD"));

        List<ProductAttributes> attributes_2 = Lists.newArrayList();
        attributes_2.add(new ProductAttributes("isVantage", "Yes"));
        attributes_2.add(new ProductAttributes("ICOBS", "Y"));
        attributes_2.add(new ProductAttributes("Tariff", "1"));
        attributes_2.add(new ProductAttributes("Product priority", "99"));
        attributes_2.add(new ProductAttributes("Account Switching", "Y"));
        attributes_2.add(new ProductAttributes("Overdraft Offer Flag", "Y"));

        Map<String, List<String>> extProdIdentifiers_2 = Maps.newHashMap();
        extProdIdentifiers_2.put("00004", Lists.newArrayList("3002116011"));
        extProdIdentifiers_2.put("00107", Lists.newArrayList("2901", "2201"));
        extProdIdentifiers_2.put("00010", Lists.newArrayList("P_ULTIMATE"));
        extProdIdentifiers_2.put("01005", Lists.newArrayList("1"));

        ProductFeatures productFeatures_reward = new ProductFeatures(new Product("Reward Current Account", "3001", "P_REWARD",
                "107116", attributes_1, extProdIdentifiers_1), null);
        ProductFeatures productFeatures_ultimate = new ProductFeatures(new Product("Ultimate Reward Current Account", "3002", "P_ULTIMATE",
                "107116", attributes_2, extProdIdentifiers_2), null);
        productFeaturesList.add(productFeatures_reward);
        productFeaturesList.add(productFeatures_ultimate);
        return productFeaturesList;
    }

    private PcaDetermineEligibilityResponse determineEligibilityResponse(){
        PcaDetermineEligibilityResponse determineEligibilityResponse = new PcaDetermineEligibilityResponse();

        List<EligibilityDetails>  eligibilityDetailsList = Lists.newArrayList();
        EligibilityDetails eligibilityDetails_1 = new EligibilityDetails("P_REWARD", true);
        EligibilityDetails eligibilityDetails_2 = new EligibilityDetails("P_ULTIMATE", true);
        eligibilityDetailsList.add(eligibilityDetails_1);
        eligibilityDetailsList.add(eligibilityDetails_2);

        determineEligibilityResponse.setProducts(eligibilityDetailsList);
        return determineEligibilityResponse;
    }
}