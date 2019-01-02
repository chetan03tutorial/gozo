package com.lbg.ib.api.sales.user.service;

import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND;
import static com.lbg.ib.api.sales.switching.constants.AccountSwitchingConstants.CURRENT_ACCOUNT;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.product.domain.features.DeviceID;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.kyc.FO75KYCReviewDAO;
import com.lbg.ib.api.sales.dao.product.holding.ProductHoldingDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ProductHoldingRequestDTO;
import com.lbg.ib.api.sales.product.domain.Product;
import com.lbg.ib.api.sales.product.domain.ProductHolding;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.user.domain.UserDetails;
import com.lbg.ib.api.sales.utils.DecoderUtil;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.product.Category;
import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;

/**
 * Created by pbabb1 on 5/25/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private SessionManagementDAO session;

    @Mock
    private LoggerDAO logger;

    @Mock
    private ProductHoldingDAO productHoldingDAO;

    @Mock
    private ConfigurationDAO configuration;

    @Mock
    private DecoderUtil decoderUtil;

    @Mock
    private DAOResponse<ProductHolding> productHoldingDAOResponse;

    @Mock
    private FO75KYCReviewDAO fO75KYCReviewDAO;

    @Mock
    private ProductHolding productHolding;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    private RetrieveProductFeaturesService retrieveProductFeaturesService;

    @Mock
    private SearchPartyService searchPartyService;

    @Before
    public void init() {
        UserContext context = SessionServiceUtil.prepareUserContext("lloyds");
        when(session.getUserContext()).thenReturn(context);

    }

    @Test
    public void returnNullWhenFetchUserInfoIsCalledAndSessionDoesntHaveUserInfo() throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        when(session.getUserInfo()).thenReturn(null);
        assertNull(userService.fetchUserInfo(false));
    }

    @Test
    public void returnArrangementWithAccountsFetchedStatusAsFalseWhenProductHoldingIsNull() throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class))).thenReturn(null);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertFalse(arrangement1.isAccountsFetchedStatus());
    }
    @Test
    public void returnArrangementWithAccountsFetchedStatusAsFalseWhenProductHoldingCallHasErrors()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(productHoldingDAOResponse.getError()).thenReturn(new DAOResponse.DAOError("somevalue", "somevalue"));
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertFalse(arrangement1.isAccountsFetchedStatus());
    }

    @Test
    public void returnArrangementWithAccountsFetchedStatusAsFalseWhenNextReviewDatelHasErrors()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);

        when(fO75KYCReviewDAO.nextReviewDate(any(String.class)))
                .thenReturn(DAOResponse.<String> withError(new DAOError("hostErrorCode", "message")));
        when(productHoldingDAOResponse.getError()).thenReturn(new DAOResponse.DAOError("somevalue", "somevalue"));
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertFalse(arrangement1.isAccountsFetchedStatus());
    }

    @Test
    public void returnArrangementWithZeroAccountsWhenGotProductHoldingSuccessfullyWithNoProducts()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        when(productHolding.getProducts()).thenReturn(new ArrayList<Product>());
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertTrue(arrangement1.getAccounts().isEmpty());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
    }

     @Test(expected = Exception.class)
    public void returnArrangementWithZeroAccountsWhenGotProductHoldingSuccessfullyWithNoProducts1()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        when(productHolding.getProducts()).thenReturn(new ArrayList<Product>());
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        when(decoderUtil.getDecryptedParam("OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ")).thenReturn("ACCOUNT||22579360||774824");
        when(resolver.resolve(ResponseErrorConstants.ARRANGEMENT_ID_NOT_FOUND))
                 .thenReturn(new ResponseError());
        UserDetails userDetails = userService.fetchUserInfo("C", "OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ", false);
        assertNotNull(userDetails);
        assertTrue(userDetails.getAccounts().isEmpty());
        assertEquals(categories, userDetails.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProductsWithNullString()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(null);
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProductsWithEmptyString()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(withResult(""));
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProductsWithInvalidDate()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(withResult("021xxx120"));
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProductsWithInvalidDate1()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(withResult("021xxx120"));
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(configuration.getConfigurationStringValue(BRAND, "lloyds")).thenReturn("LTB");
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        when(decoderUtil.getDecryptedParam("OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ")).thenReturn("ACCOUNT||22579360||774824");
        UserDetails userDetails = userService.fetchUserInfo("C", "OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ", false);
        assertNotNull(userDetails);
        assertEquals(1, userDetails.getAccounts().size());
        assertEquals(categories, userDetails.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = userDetails.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void testCheckRemittanceEligibility() {
        UserServiceImpl userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);

        Product product = new Product();
        assertTrue(!userService.checkRemittanceEligibility(product));

        product.setDormant(true);
        assertTrue(!userService.checkRemittanceEligibility(product));

        product.setDormant(false);
        assertTrue(!userService.checkRemittanceEligibility(product));

        List<Event> eventList = new ArrayList<Event>();
        product.setEvents(eventList);

        assertTrue(!userService.checkRemittanceEligibility(product));

        Event event = new Event();
        eventList.add(event);
        assertTrue(!userService.checkRemittanceEligibility(product));

        when(configuration.getConfigurationItems(any(String.class))).thenReturn(null);
        assertTrue(!userService.checkRemittanceEligibility(product));

        Map<String, Object> remittanceEvents = new HashMap<String, Object>();
        when(configuration.getConfigurationItems(any(String.class))).thenReturn(remittanceEvents);
        assertTrue(!userService.checkRemittanceEligibility(product));

        event.setEventId("DUMMY");
        remittanceEvents.put("DUMMY", "DUMMY");

        assertTrue(userService.checkRemittanceEligibility(product));

    }

    @Test
    public void testCheckFundingEligibility() {
        UserServiceImpl userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Product product = new Product();
        assertTrue(!userService.checkFundingEligibility(product));

        product.setDormant(true);
        assertTrue(!userService.checkFundingEligibility(product));

        product.setDormant(false);
        assertTrue(!userService.checkFundingEligibility(product));

        List<Event> eventList = new ArrayList<Event>();
        product.setEvents(eventList);

        assertTrue(!userService.checkFundingEligibility(product));

        Event event = new Event();
        eventList.add(event);
        assertTrue(!userService.checkFundingEligibility(product));

        when(configuration.getConfigurationItems(any(String.class))).thenReturn(null);
        assertTrue(!userService.checkFundingEligibility(product));

        Map<String, Object> fundingEvents = new HashMap<String, Object>();
        when(configuration.getConfigurationItems(any(String.class))).thenReturn(fundingEvents);
        assertTrue(!userService.checkFundingEligibility(product));

        event.setEventId("DUMMY");
        fundingEvents.put("DUMMY", "DUMMY");

        assertTrue(userService.checkFundingEligibility(product));
    }

    @Test
    public void testCheckSignatureCheckIndicator() {
        UserServiceImpl userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Product product = new Product();
        assertTrue(!userService.checkSignatureCheckIndicator(product));

        List<Indicator> list = new ArrayList<Indicator>();
        product.setIndicators(list);

        assertTrue(!userService.checkSignatureCheckIndicator(product));

        Indicator indicator = new Indicator();
        list.add(indicator);

        Map<String, Object> fundingIndicators = new HashMap<String, Object>();
        when(configuration.getConfigurationItems(any(String.class))).thenReturn(fundingIndicators);

        assertTrue(!userService.checkSignatureCheckIndicator(product));

        indicator.setIndicatorCode("DUMMY");
        fundingIndicators.put("DUMMY", "DUMMY");

        assertTrue(userService.checkSignatureCheckIndicator(product));

        when(configuration.getConfigurationItems(any(String.class))).thenReturn(null);
        assertTrue(!userService.checkSignatureCheckIndicator(product));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProductsWithFutureDate()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(withResult("02112100"));
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyWithProducts() throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(withResult("02112015"));
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Arrangement arrangement1 = userService.fetchUserInfo(false);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    private List<Product> createProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        Product prod = createProduct("22579360", "774824", "prodId");
        products.add(prod);
        return products;
    }

    private Product createProduct(String accountNumber, String sortCode, String prodIdentifier) {
        Product prod = new Product();
        prod.setAccountName("a");
        prod.setAccountCategory("a");
        prod.setAccountNumber(accountNumber);
        prod.setAccountNickName("a");
        prod.setSortCode(sortCode);
        prod.setAccountStatus("a");
        prod.setAvailableBal(20.0);
        prod.setLedgerBal(20.0);
        prod.setCurrencyCode("GBP");
        prod.setProductType("C");
        prod.setDisplayOrder(1234);
        prod.setDormant(false);
        prod.setManufacturingLegalEntity("LTB");
        prod.setAccountOpenedDate(Calendar.getInstance());
        prod.setProductIdentifier(prodIdentifier);

        ArrayList<Event> events = new ArrayList<Event>();
        Event event1 = new Event();
        event1.setEventId("10");
        events.add(event1);
        prod.setEvents(events);
        List<Indicator> indicators = new ArrayList<Indicator>();
        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("20");
        indicators.add(indicator);
        prod.setIndicators(indicators);
        return prod;
    }

    private Account createAccount() {
        com.lbg.ib.api.sso.domain.user.Account newAccount = new com.lbg.ib.api.sso.domain.user.Account();
        newAccount.setAccountName("a");
        newAccount.setAccountCategory("a");
        newAccount.setAccountNumber("a");
        newAccount.setAccountNickName("a");
        newAccount.setSortCode("a");
        newAccount.setAccountStatus("a");
        newAccount.setAvailableBal(20.0);
        newAccount.setLedgerBal(20.0);
        newAccount.setCurrencyCode("GBP");
        newAccount.setProductType("abcd");
        newAccount.setDisplayOrder(1234);
        newAccount.setEligibleForRemittance(true);
        newAccount.setEligibleForFunding(true);
        newAccount.setAccountOpenedDate(null);
        return newAccount;
    }

    public boolean isDateValid(String dateString, String dateFormat) {
        if (dateString == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyForGoJoint()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(null);
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(productHolding.getProducts()).thenReturn(products);
        when(configuration.getConfigurationStringValue(anyString(), anyString())).thenReturn("LTB");
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Product product = products.get(0);
        ProductAttributes productAttributes = new ProductAttributes("Number of Parties", "3");
        List<ProductAttributes> productAttributesList = new ArrayList<ProductAttributes>();
        productAttributesList.add(productAttributes);
        com.lbg.ib.api.sales.product.domain.features.Product featureProduct =
                new com.lbg.ib.api.sales.product.domain.features.Product("jointProduct", product.getProductIdentifier(), "prodMnemonic", "familyIdentifier", productAttributesList, null);
        ProductFeatures productFeatures = new ProductFeatures(featureProduct, new DeviceID());

        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductIdNoSessionUpdate(any(ProductReference.class))).thenReturn(productFeatures);
        SearchPartyRequest partyDetails = new SearchPartyRequest(product.getSortCode() + product.getAccountNumber());
        partyDetails.setAgreementType(CURRENT_ACCOUNT);
        List<IBParties> partyList = new ArrayList<IBParties>();
        IBParties ibParty1 = new IBParties();
        partyList.add(ibParty1);
        when(searchPartyService.retrieveParty(partyDetails)).thenReturn(partyList);
        Arrangement arrangement1 = userService.fetchUserInfo(true);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(1, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        assertTrue(arrangement1.getAccounts().get(0).getEligibleForGoJoint());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }

    @Test
    public void returnArrangementWithAccountsWhenGotProductHoldingSuccessfullyForGoJointWithOneEligibleAndNonEligible()
            throws ServiceException {
        UserService userService = new UserServiceImpl(logger, session, productHoldingDAO, configuration,
                fO75KYCReviewDAO, decoderUtil, retrieveProductFeaturesService, searchPartyService);
        Arrangement arrangement = new Arrangement();
        arrangement.setPrimaryInvolvedParty(new PrimaryInvolvedParty());
        arrangement.setIbSessionId("some-id");
        List<Product> products = createProducts();
        products.add(createProduct("123456", "111111", "prodId1"));
        when(session.getUserInfo()).thenReturn(arrangement);
        when(productHoldingDAO.fetchProductHoldings(any(ProductHoldingRequestDTO.class)))
                .thenReturn(productHoldingDAOResponse);
        when(fO75KYCReviewDAO.nextReviewDate(any(String.class))).thenReturn(null);
        when(productHoldingDAOResponse.getError()).thenReturn(null);
        when(productHoldingDAOResponse.getResult()).thenReturn(productHolding);
        Map<String, Object> reusableEvents = new HashMap<String, Object>();
        reusableEvents.put("someKey", "10");
        HashMap<String, Object> indicatorsMap = new HashMap<String, Object>();
        indicatorsMap.put("someIndicator", "10");
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_REMITTANCE)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.EVENTS_FOR_FUNDINGS)).thenReturn(reusableEvents);
        when(configuration.getConfigurationItems(UserServiceImpl.INDICATORS_FOR_FUNDINGS)).thenReturn(indicatorsMap);
        when(configuration.getConfigurationStringValue(anyString(), anyString())).thenReturn("LTB");
        when(productHolding.getProducts()).thenReturn(products);
        List<Category> categories = new ArrayList<Category>();
        when(productHolding.getCategories()).thenReturn(categories);
        Product product = products.get(0);
        Product product1 = products.get(1);
        ProductAttributes productAttributes = new ProductAttributes("Number of Parties", "3");
        ProductAttributes productAttributes1 = new ProductAttributes("Number of Parties", "2");
        List<ProductAttributes> productAttributesList = new ArrayList<ProductAttributes>();
        List<ProductAttributes> productAttributesList1 = new ArrayList<ProductAttributes>();
        productAttributesList.add(productAttributes);
        productAttributesList1.add(productAttributes1);
        com.lbg.ib.api.sales.product.domain.features.Product featureProduct =
                new com.lbg.ib.api.sales.product.domain.features.Product("jointProduct", product.getProductIdentifier(), "prodMnemonic", "familyIdentifier", productAttributesList, null);
        ProductFeatures productFeatures = new ProductFeatures(featureProduct, new DeviceID());

        com.lbg.ib.api.sales.product.domain.features.Product featureProduct1 =
                new com.lbg.ib.api.sales.product.domain.features.Product("jointProduct", product1.getProductIdentifier(), "prodMnemonic", "familyIdentifier", productAttributesList1, null);
        ProductFeatures productFeatures1 = new ProductFeatures(featureProduct1, new DeviceID());

        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductIdNoSessionUpdate(new ProductReference("prodId"))).thenReturn(productFeatures);
        when(retrieveProductFeaturesService.retrieveProductFeaturesByProductIdNoSessionUpdate(new ProductReference("prodId1"))).thenReturn(productFeatures1);
        SearchPartyRequest partyDetails = new SearchPartyRequest(product.getSortCode() + product.getAccountNumber());
        SearchPartyRequest partyDetails1 = new SearchPartyRequest(product1.getSortCode() + product1.getAccountNumber());
        partyDetails.setAgreementType(CURRENT_ACCOUNT);
        partyDetails1.setAgreementType(CURRENT_ACCOUNT);
        List<IBParties> partyList = new ArrayList<IBParties>();
        List<IBParties> partyList1 = new ArrayList<IBParties>();
        IBParties ibParty1 = new IBParties();
        IBParties ibParty2 = new IBParties();
        partyList.add(ibParty1);
        partyList1.add(ibParty1);
        partyList1.add(ibParty2);
        when(searchPartyService.retrieveParty(partyDetails)).thenReturn(partyList);
        when(searchPartyService.retrieveParty(partyDetails1)).thenReturn(partyList1);
        Arrangement arrangement1 = userService.fetchUserInfo(true);
        assertNotNull(arrangement1);
        assertTrue(arrangement1.isAccountsFetchedStatus());
        assertEquals(2, arrangement1.getAccounts().size());
        assertEquals(categories, arrangement1.getCategories());
        assertTrue(arrangement1.getAccounts().get(0).getEligibleForGoJoint());
        assertFalse(arrangement1.getAccounts().get(1).getEligibleForGoJoint());
        // assertTrue(isDateValid(arrangement1.getLastLoggedInTime().toString(),
        // "yyyy-MMM-dd hh:mm:ss"));
        Account account = arrangement1.getAccounts().get(0);
        // assertTrue(account.equals(createAccount()));
    }
}
