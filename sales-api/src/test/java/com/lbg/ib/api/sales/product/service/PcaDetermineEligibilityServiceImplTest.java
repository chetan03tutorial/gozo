/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Mockito.verify;
import static com.lbg.ib.api.sales.testing.TestingHelper.contentOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.eligibility.PcaEligibiltyDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.PcaEligibilityRequestDTO;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.suitability.ProductQualiferOptions;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;


@RunWith(MockitoJUnitRunner.class)
public class PcaDetermineEligibilityServiceImplTest {

    @InjectMocks
    private PcaDetermineEligibilityServiceImpl service;

    @Mock
    private PcaEligibiltyDAO                   dao;

    @Mock
    private GalaxyErrorCodeResolver            resolver;

    @Mock
    private SessionManagementDAO               session;

    @Mock
    private ConfigurationDAO                   configurationService;

    @Mock
    private ChannelBrandingDAO                 branding;

    private Map<String, Object>                productGroupMnemonics;

    @Mock
    private LoggerDAO                          logger         = mock(LoggerDAO.class);

    private static final ResponseError         RESPONSE_ERROR = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");

    @Before
    public void setUp() {
        mockPopulateConfiguration();
        service.setLogger(logger);
    }

    private PrimaryInvolvedParty setPrimaryInvolvedParty() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob("1985-12-17");
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);
        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("GBR");
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private HashMap<String, EligibilityDetails> getExpectedResult() {
        HashMap<String, EligibilityDetails> productMap = new HashMap<String, EligibilityDetails>();
        EligibilityDetails details = new EligibilityDetails();
        details.setMnemonic("P_NEW_BASIC");
        details.setIsEligible(true);
        productMap.put("P_NEW_BASIC", details);
        return productMap;
    }


    @Test(expected=ServiceException.class)
    public void testPerformSuitabilityWithAllEntriesNull(){
         service.performSuitability(null, null, null);

    }

    @Test(expected=ServiceException.class)
    public void testPerformSuitabilityWithProductOptions(){

        ProductQualiferOptions productOptions = new ProductQualiferOptions();
        service.performSuitability(productOptions, null, null);
    }

    @Test
    public void testPerformSuitabilityWithProductOptionsWithNullServerResponse(){
        ProductQualiferOptions productOptions = new ProductQualiferOptions();
        List<String> list = new ArrayList<String>();
        list.add("dummy");
        productOptions.setValues(list);
        service.performSuitability(productOptions, null, null);
    }

    @Test
    public void testEligibilityPreCheck(){
        Arrangement arrangement = new Arrangement();
        Assert.isTrue(service.eligibilityPreCheck(arrangement));

        List<Account> accountList = new ArrayList<Account>();
        accountList.add(new Account());
        arrangement.setAccounts(accountList);

        Assert.isTrue(service.eligibilityPreCheck(arrangement));
    }

    @Test
    public void testCheckBankruptcyIndicator(){
        Arrangement arrangement = new Arrangement();
        Assert.isTrue(!service.checkBankruptcyIndicator(arrangement));

        List<Account> accountList = new ArrayList<Account>();
        accountList.add(new Account());
        arrangement.setAccounts(accountList);

        Assert.isTrue(!service.checkBankruptcyIndicator(arrangement));
    }


    @Test(expected=ServiceException.class)
    public void testDetermineSuitability(){
        service.determineSuitability(null,null,null,false);
    }

    @Test
    public void shouldReturnServiceResponseWhenDaoReturnsWithResult() throws Exception {

        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        // request.setDob(new GregorianCalendar().getTime());

        // PrimaryInvolvedParty primaryInvolvedParty =
        // setPrimaryInvolvedParty();
        HashMap<String, EligibilityDetails> productMap = getExpectedResult();
        String[] mnenomicArray = { "G_PCA", "G_AVA" };
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        PcaEligibilityRequestDTO requestDTO = new PcaEligibilityRequestDTO("CA", primaryInvolvedParty, mnenomicArray,
                null);

        when(dao.determineEligibility(requestDTO)).thenReturn(withResult(productMap));
        when(configurationService.getConfigurationItems("ProductGroupMnemonics"))
                .thenReturn(populateProductGroupMnemonics());
        PcaDetermineEligibilityResponse response = service.determineEligiblity(request, "LLOYDS");
        Assert.isTrue(response.getProducts().get(0).getMnemonic().equals("P_NEW_BASIC"));
        Assert.isTrue(response.getProducts().get(0).getIsEligible().equals(true));
    }

    @Test
    public void shouldThrowServiceExceptionWhenDaoReturnsWithError() throws Exception {

        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob("1985-12-17");
        PcaEligibilityRequestDTO requestDTO = new PcaEligibilityRequestDTO("CA", primaryInvolvedParty,
                new String[] { "P_NEW_BASIC" }, null);

        when(configurationService.getConfigurationStringValue("1100017", "IB_ERROR_CODE")).thenReturn("1100017");
        when(configurationService.getConfigurationItems("ProductGroupMnemonics"))
                .thenReturn(populateProductGroupMnemonics());

        PcaDetermineEligibilityResponse response = service.determineEligiblity(request, "LLOYDS");
        Assert.isNull(response);
    }



    @Test
    public void testProductShouldReturnResponseWithResultsForLloydsWithNull() throws ServiceException {

        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        request.setExistingCustomer(null);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob("1985-12-17");

        List<ProductQualiferOptions> productQualifierOptionsList = new ArrayList<ProductQualiferOptions>();
        List<String> values = new ArrayList<String>();
        values.add("EVERYDAY");
        ProductQualiferOptions productQualiferOptions = new ProductQualiferOptions(values, "ACCOUNT_TYPE");

        productQualifierOptionsList.add(productQualiferOptions);
        request.setProductOptions(productQualifierOptionsList);

        String[] mnenomicArray = { "G_PCA", "G_AVA" };
        PcaEligibilityRequestDTO requestDTO = new PcaEligibilityRequestDTO("CA", primaryInvolvedParty, mnenomicArray,
                null);
        when(branding.getChannelBrand()).thenReturn(
                DAOResponse.<ChannelBrandDTO> withResult(new ChannelBrandDTO("LLOYDS", "LLOYDS", "LLOYDS")));
        when(dao.determineEligibility(requestDTO))
                .thenReturn(DAOResponse.<HashMap<String, EligibilityDetails>> withResult(prepareLloydsDeciResponse()));

        PcaDetermineEligibilityResponse response = service.determineEligiblitySuitability(request);

        List<EligibilityDetails> eligibilityDetailsList = response.getProducts();

        List<String> prodMnemonicList = prepareLloydsProductMap();
        for (EligibilityDetails eligibilityDetails : eligibilityDetailsList) {
            Assert.isTrue(prodMnemonicList.remove(eligibilityDetails.getMnemonic()));
        }

    }

    @Test
    public void testProductShouldReturnResponseWithResultsForLloyds() throws ServiceException {

        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob("1985-12-17");

        List<ProductQualiferOptions> productQualifierOptionsList = new ArrayList<ProductQualiferOptions>();
        List<String> values = new ArrayList<String>();
        values.add("EVERYDAY");
        ProductQualiferOptions productQualiferOptions = new ProductQualiferOptions(values, "ACCOUNT_TYPE");

        productQualifierOptionsList.add(productQualiferOptions);
        request.setProductOptions(productQualifierOptionsList);

        String[] mnenomicArray = { "G_PCA", "G_AVA" };
        PcaEligibilityRequestDTO requestDTO = new PcaEligibilityRequestDTO("CA", primaryInvolvedParty, mnenomicArray,
                null);
        when(branding.getChannelBrand()).thenReturn(
                DAOResponse.<ChannelBrandDTO> withResult(new ChannelBrandDTO("LLOYDS", "LLOYDS", "LLOYDS")));
        when(dao.determineEligibility(requestDTO))
                .thenReturn(DAOResponse.<HashMap<String, EligibilityDetails>> withResult(prepareLloydsDeciResponse()));

        PcaDetermineEligibilityResponse response = service.determineEligiblitySuitability(request);

        List<EligibilityDetails> eligibilityDetailsList = response.getProducts();

        List<String> prodMnemonicList = prepareLloydsProductMap();
        for (EligibilityDetails eligibilityDetails : eligibilityDetailsList) {
            Assert.isTrue(prodMnemonicList.remove(eligibilityDetails.getMnemonic()));
        }

    }

    @Test(expected = ServiceException.class)
    public void testProductShouldAuthWithNoValueinSession() throws ServiceException {

        PcaDetermineEligibilityRequest request = new PcaDetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        request.setExistingCustomer(true);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob("1985-12-17");

        List<ProductQualiferOptions> productQualifierOptionsList = new ArrayList<ProductQualiferOptions>();
        List<String> values = new ArrayList<String>();
        values.add("EVERYDAY");
        ProductQualiferOptions productQualiferOptions = new ProductQualiferOptions(values, "ACCOUNT_TYPE");

        productQualifierOptionsList.add(productQualiferOptions);
        request.setProductOptions(productQualifierOptionsList);

        String[] mnenomicArray = { "G_PCA", "G_AVA" };
        PcaEligibilityRequestDTO requestDTO = new PcaEligibilityRequestDTO("CA", primaryInvolvedParty, mnenomicArray,
                null);
        when(branding.getChannelBrand()).thenReturn(
                DAOResponse.<ChannelBrandDTO> withResult(new ChannelBrandDTO("LLOYDS", "LLOYDS", "LLOYDS")));
        when(dao.determineEligibility(requestDTO))
                .thenReturn(DAOResponse.<HashMap<String, EligibilityDetails>> withResult(prepareLloydsDeciResponse()));

        when(resolver.resolve(any(String.class))).thenReturn(RESPONSE_ERROR);

        PcaDetermineEligibilityResponse response = service.determineEligiblitySuitability(request);

        List<EligibilityDetails> eligibilityDetailsList = response.getProducts();

        List<String> prodMnemonicList = prepareLloydsProductMap();
        for (EligibilityDetails eligibilityDetails : eligibilityDetailsList) {
            Assert.isTrue(prodMnemonicList.remove(eligibilityDetails.getMnemonic()));
        }

    }

    @Test
    public void testDetermineSuitabilityHalifaxEveryday() throws ServiceException {
        List<String> result = service.determineSuitability("EVERYDAY", prepareHalifaxProducts(), "HALIFAX", false);
        Assert.isTrue(result.size() == 3);
        Assert.isTrue(result.contains("P_REWARD"));
        Assert.isTrue(result.contains("P_ULTIMATE"));
        Assert.isTrue(result.contains("P_NEW_BASIC"));
    }

    @Test
    public void testDetermineSuitabilityHalifaxYouth() throws ServiceException {
        List<String> result = service.determineSuitability("YOUTH", prepareHalifaxProducts(), "HALIFAX", false);
        Assert.isTrue(result.size() == 1);
        Assert.isTrue(result.contains("P_EXPRESSCASH"));
    }

    @Test
    public void testDetermineSuitabilityHalifaxStudent() throws ServiceException {
        List<String> result = service.determineSuitability("STUDENT", prepareHalifaxProducts(), "HALIFAX", false);
        Assert.isTrue(result.size() == 2);
        Assert.isTrue(result.contains("P_NEW_BASIC"));
        Assert.isTrue(result.contains("P_STUDENT"));
    }

    @Test
    public void testDetermineSuitabilityBosStudent() throws ServiceException {
        List<String> result = service.determineSuitability("STUDENT", prepareBosProducts(), "BOS", false);
        Assert.isTrue(result.size() == 2);
        Assert.isTrue(result.contains("P_NEW_BASIC"));
        Assert.isTrue(result.contains("P_STUDENT"));
    }

    @Test
    public void testDetermineSuitabilityBosEveryday() throws ServiceException {
        List<String> result = service.determineSuitability("EVERYDAY", prepareBosProducts(), "BOS", false);
        Assert.isTrue(result.size() == 3);
        Assert.isTrue(result.contains("P_CLASSIC"));
        Assert.isTrue(result.contains("P_NEW_BASIC"));
        Assert.isTrue(result.contains("P_PLAT"));
    }

    @Test
    public void testDetermineSuitabilityBosYouth() throws ServiceException {
        List<String> result = service.determineSuitability("YOUTH", prepareBosProducts(), "BOS", false);
        Assert.isTrue(result.size() == 1);
        Assert.isTrue(result.contains("P_UNDER19"));
    }

    @Test
    public void testDetermineSuitabilityLloydsYouth() throws ServiceException {
        List<String> result = service.determineSuitability("YOUTH", prepareLloydsProducts(), "LLOYDS", false);
        Assert.isTrue(result.size() == 1);
        Assert.isTrue(result.contains("P_UNDER19"));

    }

    @Test
    public void testDetermineSuitabilityLloydsEveryday() throws ServiceException {
        List<String> result = service.determineSuitability("EVERYDAY", prepareLloydsProducts(), "LLOYDS", false);
        Assert.isTrue(result.size() == 5);
        Assert.isTrue(result.contains("P_CLASSIC"));
        Assert.isTrue(result.contains("P_PLAT_CLB"));
        Assert.isTrue(result.contains("P_NEW_BASIC"));
        Assert.isTrue(result.contains("P_CLUB"));
        Assert.isTrue(result.contains("P_PLAT"));
    }

    @Test
    public void testDetermineSuitabilityLloydsStudent() throws ServiceException {
        List<String> result = service.determineSuitability("STUDENT", prepareLloydsProducts(), "LLOYDS", false);
        Assert.isTrue(result.size() == 2);
        Assert.isTrue(result.contains("P_NEW_BASIC"));
        Assert.isTrue(result.contains("P_STUDENT"));
    }

    @Test
    public void testEligibilityPreCheckWithNegative() {
        Arrangement arrangement = new Arrangement();

        Account account = new Account();

        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("88");

        List<Indicator> indicatorList = new ArrayList<Indicator>();
        indicatorList.add(indicator);

        account.setIndicators(indicatorList);
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account);

        arrangement.setAccounts(accountList);
        when(configurationService.getConfigurationItems("IndicatorsForDetrmineEligibility"))
                .thenReturn(populateIndicatorsForDetrmineEligibility());
        Assert.isTrue(service.eligibilityPreCheck(arrangement));

    }

    @Test
    public void testEligibilityPreCheckWithPositive() {
        Arrangement arrangement = new Arrangement();

        Account account = new Account();

        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("11");

        List<Indicator> indicatorList = new ArrayList<Indicator>();
        indicatorList.add(indicator);

        account.setIndicators(indicatorList);
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account);

        arrangement.setAccounts(accountList);
        when(configurationService.getConfigurationItems("IndicatorsForDetrmineEligibility"))
                .thenReturn(populateIndicatorsForDetrmineEligibility());
        Assert.isTrue(!service.eligibilityPreCheck(arrangement));

    }

    @Test
    public void testCheckBankruptcyIndicatorWithValidBankruptcyIndicator() {
        Arrangement arrangement = new Arrangement();

        Account account = new Account();

        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("48");

        List<Indicator> indicatorList = new ArrayList<Indicator>();
        indicatorList.add(indicator);

        account.setIndicators(indicatorList);
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account);

        arrangement.setAccounts(accountList);
        when(configurationService.getConfigurationItems("BankruptcyDetrmineEligibility"))
                .thenReturn(populateBankruptcyIndicator());
        Assert.isTrue(service.checkBankruptcyIndicator(arrangement));
    }

    @Test
    public void testCheckBankruptcyIndicatorWithInValidBankruptcyIndicator() {
        Arrangement arrangement = new Arrangement();

        Account account = new Account();

        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("488");

        List<Indicator> indicatorList = new ArrayList<Indicator>();
        indicatorList.add(indicator);

        account.setIndicators(indicatorList);
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account);

        arrangement.setAccounts(accountList);
        when(configurationService.getConfigurationItems("BankruptcyDetrmineEligibility"))
                .thenReturn(populateBankruptcyIndicator());
        Assert.isTrue(!service.checkBankruptcyIndicator(arrangement));
    }

    public void mockPopulateConfiguration() {
        String str = contentOf("/testConfigProductListPerBrand.xml");
        Document document = loadXMLFromString(str);
        document.getDocumentElement().normalize();
        Node node = document.getDocumentElement();
        NodeList nodeList = node.getChildNodes();
        for (int count = 0; count < nodeList.getLength(); count++) {
            node = nodeList.item(count);
            if (node.getNodeName().equals("section")) {
                NodeList childNodeList = node.getChildNodes();
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    Node childNode = childNodeList.item(j);
                    mockPopulateConfiguration(childNode.getNodeValue());
                }

            }
        }
    }

    public void mockPopulateConfiguration(String input) {
        input = input.replaceAll("\n", "");
        input = input.replaceAll("\t", "");
        String[] inputArr = input.split(":");
        Map<String, Object> map = new HashMap<String, Object>();

        String[] subInputArr = inputArr[1].split(",");

        for (int i = 0; i < subInputArr.length; i++) {
            map.put(inputArr[0] + i, subInputArr[i]);
        }

        when(configurationService.getConfigurationItems(inputArr[0])).thenReturn(map);
    }

    public Document loadXMLFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
        }

        return null;
    }

    public List<String> prepareLloydsProductMap() {
        List<String> prodMnemonic = new ArrayList<String>();
        prodMnemonic.add("P_STUDENT");
        prodMnemonic.add("P_NEW_BASIC");
        prodMnemonic.add("P_PLAT");
        prodMnemonic.add("P_CLASSIC");
        prodMnemonic.add("P_PLAT_CLB");
        prodMnemonic.add("P_UNDER19");
        prodMnemonic.add("P_CLUB");

        return prodMnemonic;
    }

    public List<EligibilityDetails> prepareHalifaxProducts() {
        List<EligibilityDetails> list = new ArrayList<EligibilityDetails>();
        list.add(new EligibilityDetails("P_REWARD", true, null, null));
        list.add(new EligibilityDetails("P_ULTIMATE", true, null, null));
        list.add(new EligibilityDetails("P_STANDARD", true, null, null));
        list.add(new EligibilityDetails("P_CARDCASH", true, null, null));
        list.add(new EligibilityDetails("P_NEW_BASIC", true, null, null));
        list.add(new EligibilityDetails("P_EXPRESSCASH", true, null, null));
        list.add(new EligibilityDetails("P_STUDENT", true, null, null));
        return list;
    }

    public List<EligibilityDetails> prepareBosProducts() {
        List<EligibilityDetails> list = new ArrayList<EligibilityDetails>();
        list.add(new EligibilityDetails("P_SILVER", true, null, null));
        list.add(new EligibilityDetails("P_CLASSIC", true, null, null));
        list.add(new EligibilityDetails("P_PREM", true, null, null));
        list.add(new EligibilityDetails("P_URCA", true, null, null));
        list.add(new EligibilityDetails("P_UNDER19", true, null, null));
        list.add(new EligibilityDetails("P_STANDARD", true, null, null));
        list.add(new EligibilityDetails("P_NEW_BASIC", true, null, null));

        list.add(new EligibilityDetails("P_PREM_PB", true, null, null));
        list.add(new EligibilityDetails("P_PLAT", true, null, null));
        list.add(new EligibilityDetails("P_STUDENT", true, null, null));
        list.add(new EligibilityDetails("P_GOLD", true, null, null));

        return list;
    }

    public List<EligibilityDetails> prepareLloydsProducts() {
        List<EligibilityDetails> list = new ArrayList<EligibilityDetails>();
        list.add(new EligibilityDetails("P_STUDENT", true, null, null));
        list.add(new EligibilityDetails("P_NEW_BASIC", true, null, null));
        list.add(new EligibilityDetails("P_PLAT", true, null, null));
        list.add(new EligibilityDetails("P_CLASSIC", true, null, null));
        list.add(new EligibilityDetails("P_PLAT_CLB", true, null, null));
        list.add(new EligibilityDetails("P_UNDER19", true, null, null));
        list.add(new EligibilityDetails("P_CLUB", true, null, null));
        return list;
    }

    public HashMap<String, EligibilityDetails> prepareLloydsDeciResponse() {
        HashMap<String, EligibilityDetails> map = new HashMap<String, EligibilityDetails>();
        EligibilityDetails eligibilityDetails = new EligibilityDetails("P_STUDENT", true, null, null);
        map.put("P_STUDENT", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_NEW_BASIC", true, null, null);
        map.put("P_NEW_BASIC", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_PLAT", true, null, null);
        map.put("P_PLAT", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_CLASSIC", true, null, null);
        map.put("P_CLASSIC", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_PLAT_CLB", true, null, null);
        map.put("P_PLAT_CLB", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_UNDER19", false, null, null);
        map.put("P_UNDER19", eligibilityDetails);

        eligibilityDetails = new EligibilityDetails("P_CLUB", true, null, null);
        map.put("P_CLUB", eligibilityDetails);

        return map;
    }

    private Map<String, Object> populateProductGroupMnemonics() {
        productGroupMnemonics = new HashMap<String, Object>();
        productGroupMnemonics.put("Event1", "G_PCA");
        return productGroupMnemonics;
    }

    private Map<String, Object> populateBankruptcyIndicator() {
        Map<String, Object> bankruptcyIndicatorMap = new HashMap<String, Object>();
        bankruptcyIndicatorMap.put("INDICATOR1", "48");
        return bankruptcyIndicatorMap;
    }

    private Map<String, Object> populateIndicatorsForDetrmineEligibility() {
        Map<String, Object> indicatorMap = new HashMap<String, Object>();
        indicatorMap.put("INDICATOR1", "11");
        indicatorMap.put("INDICATOR2", "13");
        indicatorMap.put("INDICATOR3", "657");
        return indicatorMap;
    }

    @Test(expected = ServiceException.class)
    public void testGetBrandWithInvalidBrand() {
        DAOError daoError = new DAOError("code", "message");
        daoError.toString();
        daoError.hashCode();
        when(branding.getChannelBrand())
                .thenReturn(DAOResponse.<ChannelBrandDTO> withError(new DAOError("code", "message")));
        service.getBrand();
    }

    @Test
    public void testDetermineAuthEligiblity() {
        PcaDetermineEligibilityRequest pcaDetermineEligibilityRequest = new PcaDetermineEligibilityRequest();
        when(branding.getChannelBrand()).thenReturn(
                DAOResponse.<ChannelBrandDTO> withResult(new ChannelBrandDTO("LLOYDS", "LLOYDS", "LLOYDS")));
        HashMap<String, EligibilityDetails> productMap = getExpectedResult();

        when(dao.determineEligibilityAuth(any(PcaEligibilityRequestDTO.class))).thenReturn(withResult(productMap));
        pcaDetermineEligibilityRequest.setArrangementType(AccountType.CA);
        pcaDetermineEligibilityRequest.setCandidateInstructions(new ArrayList<String>());
        DetermineEligibilityResponse response = service.determineAuthEligiblity(pcaDetermineEligibilityRequest);
        Assert.isTrue("Successfully fetched the eligibility details.".equals(response.getMsg()));
    }

    @Test(expected = ServiceException.class)
    public void testDetermineAuthEligiblityWithExistingUser() {
        PcaDetermineEligibilityRequest pcaDetermineEligibilityRequest = new PcaDetermineEligibilityRequest();
        pcaDetermineEligibilityRequest.setExistingCustomer(true);
        when(branding.getChannelBrand()).thenReturn(
                DAOResponse.<ChannelBrandDTO> withResult(new ChannelBrandDTO("LLOYDS", "LLOYDS", "LLOYDS")));
        HashMap<String, EligibilityDetails> productMap = getExpectedResult();

        when(dao.determineEligibilityAuth(any(PcaEligibilityRequestDTO.class))).thenReturn(withResult(productMap));
        pcaDetermineEligibilityRequest.setArrangementType(AccountType.CA);
        pcaDetermineEligibilityRequest.setCandidateInstructions(new ArrayList<String>());
        DetermineEligibilityResponse response = service.determineAuthEligiblity(pcaDetermineEligibilityRequest);

    }

}