package com.lbg.ib.api.sales.dao.product.offer;

import static com.lbg.ib.api.sales.party.domain.TaxResidencyType.findTaxResidencyTypeFromCode;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;
import com.lbg.ib.api.sales.dto.product.offer.ASMScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.EIDVScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.RelatedApplicationDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.TaxResidencyDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.TinDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.StructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.UnstructuredPostalAddressDTO;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AssessmentEvidence;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CurrencyAmount;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDecision;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OrganisationUnit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOffer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReferralCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.offerproduct.arrangement.IA_OfferProductArrangement;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementResponse;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class OfferProductDAOImplTest {

    private static final String                   ERROR_CODE              = "code";

    private static final String                   ERROR_MESSAGE           = "message";

    private static final String                   MNEMONIC_ID             = "00010";

    private final String                                BIRTH_CITY              = "Leeds";

    private final String                                UNITED_KINGDOM          = "United Kingdom";

    private static final String                   CBS_PRODUCT_SYSTEM_CODE = "00004";

    private final TinDetailsDTO                         tinDetails              = new TinDetailsDTO();

    private final LinkedHashSet<String>                 nationalities           = new LinkedHashSet<String>();

    private final LinkedHashSet<TaxResidencyDetailsDTO> taxResidencies          = new LinkedHashSet<TaxResidencyDetailsDTO>();

    @Mock
    private OfferProductArrangementRequest        arrangementRequest;

    @Mock
    private OfferProductArrangementRequest        arrangementRequest2;

    @Mock
    @Qualifier("offerPCAService")
    private IA_OfferProductArrangement            service;

    @Mock
    @Qualifier("offerService")
    private IA_OfferProductArrangement            offerService;

    @Mock
    private OfferProductArrangementRequestBuilder requestBuilder;

    @Mock
    private LoggerDAO                             logger;

    @Mock
    private DAOExceptionHandler                   daoExceptionHandler;

    private final OfferProductDTO                       request                 = testRequest();

    private final OfferProductDTO                       request2                = testRequestSA();
    private DAOResponse<ProductOfferedDTO>        response;

    @InjectMocks
    private OfferProductDAOImpl                   offerProductDAO;

    // @Before
    public void setup() {
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseCA() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(testResponse());
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldsetOfferService() throws Exception {
        offerProductDAO.setOfferService(offerService);

    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseDecline() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(testResponse3());
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseDeclinewithEIDV() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(testResponse4());
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseDeclinewithASM() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(testResponse5());
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseSIRA() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(testResponseSIRA());
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOForDAOError() throws Exception {
        when(requestBuilder.build(request2)).thenReturn(arrangementRequest2);
        when(service.offerProductArrangement(arrangementRequest2)).thenReturn(noArrangementIdResponse());
        response = whenOfferIsCalled2();
        Assert.assertNotNull(response.getError());

    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOForDAOErrorforArrangementNotNull() throws Exception {
        when(requestBuilder.build(request2)).thenReturn(arrangementRequest2);
        when(service.offerProductArrangement(arrangementRequest2))
                .thenReturn(ineligibleProductResponse("232323", "error"));
        response = whenOfferIsCalled2();
        Assert.assertNull(response.getError());

    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOForException() throws Exception {
        when(offerService.offerProductArrangement(arrangementRequest2)).thenReturn(testResponse());
        response = whenOfferIsCalled2();
        Assert.assertNull(response.getResult());
    }

    @Test
    public void testValidateValidateResponse() {
        assertTrue(offerProductDAO.validateResponse(null) == null);

        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        assertTrue(offerProductDAO.validateResponse(response) == null);
    }

    @Test
    public void testIsProductInEligible() {
        final ProductArrangement productArrangement = new ProductArrangement();
        assertTrue(!offerProductDAO.isProductInEligible(productArrangement));

        final Product product = new Product();
        productArrangement.setAssociatedProduct(product);

        assertTrue(!offerProductDAO.isProductInEligible(productArrangement));

        final ProductEligibilityDetails eligibilityDetails = new ProductEligibilityDetails();
        product.setEligibilityDetails(eligibilityDetails);

        assertTrue(!offerProductDAO.isProductInEligible(productArrangement));
    }

    @Test
    public void testConditions() {
        final ProductArrangement productArrangement = new ProductArrangement();
        assertTrue(offerProductDAO.conditions(productArrangement) != null);
    }

    @Test
    public void testAsmScoreWithConditional() {
        assertTrue(offerProductDAO.asmScore(null) == null);

        final CustomerScore[] customerScoreArray = new CustomerScore[1];
        final CustomerScore customerScore = new CustomerScore();

        customerScoreArray[0] = customerScore;
        assertTrue(offerProductDAO.asmScore(customerScoreArray) == null);

        customerScore.setAssessmentType("ASM");
        assertTrue(offerProductDAO.asmScore(customerScoreArray) == null);
    }

    @Test
    public void testEidvScoreWithConditional() {
        assertTrue(offerProductDAO.eidvScore(null) == null);

        final CustomerScore[] customerScoreArray = new CustomerScore[1];
        final CustomerScore customerScore = new CustomerScore();
        customerScore.setAssessmentType("EIDV");
        final ReferralCode referralCode[] = new ReferralCode[0];
        customerScore.setReferralCode(referralCode);
        customerScoreArray[0] = customerScore;

        assertTrue(offerProductDAO.eidvScore(customerScoreArray) != null);

        customerScore.setReferralCode(null);
        assertTrue(offerProductDAO.eidvScore(customerScoreArray) != null);
    }

    @Test
    public void testPopulateDeclineCondition() {
        assertTrue(offerProductDAO.populateDeclineCondition(null, null) == null);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseSIRAWithConditionValue() throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);

        final OfferProductArrangementResponse offerResponse = testResponseSIRA();
        final ProductArrangement productArrangement = offerResponse.getProductArrangement();
        final RuleCondition[] ruleConditions = productArrangement.getConditions();
        for (int i = 0; i < ruleConditions.length; i++) {
            ruleConditions[i].setValue(null);
        }
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(offerResponse);
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseSIRAWithCustomerDecisionAsNull()
            throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);

        final OfferProductArrangementResponse offerResponse = testResponseSIRA();
        final ProductArrangement productArrangement = offerResponse.getProductArrangement();
        final CustomerScore[] customerScoreArray = productArrangement.getPrimaryInvolvedParty().getCustomerScore();
        for (int i = 0; i < customerScoreArray.length; i++) {
            customerScoreArray[i].setCustomerDecision(null);
        }
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(offerResponse);
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCorrectOfferedProductDTOWhenWeGetAHappyResponseSIRAWithNullCustomerScore()
            throws Exception {
        when(requestBuilder.build(request)).thenReturn(arrangementRequest);

        final OfferProductArrangementResponse offerResponse = testResponseSIRA();
        final ProductArrangement productArrangement = offerResponse.getProductArrangement();
        productArrangement.getPrimaryInvolvedParty().setCustomerScore(null);
        when(offerService.offerProductArrangement(arrangementRequest)).thenReturn(offerResponse);
        response = whenOfferIsCalled();
        assertThat(response.getResult().getProductIdentifier(), is(expectedDto().getProductIdentifier()));
        verifyNoMoreInteractions(logger);
    }

    // @Test
    public void shouldReturnErrorFromExceptionHandlerWhenServiceThrowsException() throws Exception {
        final ExternalBusinessError expectedException = new ExternalBusinessError(null, null, null, null);
        when(service.offerProductArrangement(arrangementRequest)).thenThrow(expectedException);
        final DAOError expectedError = new DAOError("someErrorCode", "someMessage");
        when(daoExceptionHandler.handleException(expectedException, OfferProductDAOImpl.class, "offer", request))
                .thenReturn(expectedError);

        response = whenOfferIsCalled();

        assertThat(response.getError(), CoreMatchers.sameInstance(expectedError));
    }

    private OfferProductArrangementResponse noArrangementIdResponse() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        response.setProductArrangement(new ProductArrangement());
        return response;
    }

    private OfferProductArrangementResponse ineligibleProductResponse(final String errorCode, final String errorMessage) {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final ProductArrangement arrangement = new ProductArrangement();
        final Product product = new Product();
        final ProductEligibilityDetails eligibility = new ProductEligibilityDetails();
        final ReasonCode reasonCode = new ReasonCode();
        reasonCode.setCode(errorCode);
        reasonCode.setDescription(errorMessage);
        eligibility.setDeclineReasons(new ReasonCode[] { reasonCode });
        product.setEligibilityDetails(eligibility);
        arrangement.setAssociatedProduct(product);
        response.setProductArrangement(arrangement);
        return response;
    }

    private ProductOfferedDTO expectedDto() {
        final Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("Make_It_Joint", "N");
    return new ProductOfferedDTO(
        "aid",
        "arrType",
        "appType",
        new EIDVScoreDTO("sr", "EiDV", "ei", "is", "c", "d"),
        new ASMScoreDTO("sra", "ASm", "dc", "dt", "ca", "da"),
        "cid",
        "cidPers",
        "cn",
        "indv",
        "pn",
        "NORMAL",
        optionsMap,
        asList(new ConditionDTO("n", "k", "v"),
            new ConditionDTO("INTEND_TO_SWITCH", null, "true"),
            new ConditionDTO("INTEND_TO_OVERDRAFT", null, "true"),
            new ConditionDTO("OVERDRAFT_AMOUNT", null, "200")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        null,
        "cbsProductNumber",
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        "pid",
        null,
        "pfid",
        null,
        new SIRAScoreDTO(),
        new ThreatMatrixDTO(),
        Collections.<MarketingPreferenceDTO>emptyList());
    }

    private ProductOfferedDTO expectedDtoWithCBSSortCode() {
        final Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("Make_It_Joint", "N");
    return new ProductOfferedDTO(
        "aid",
        "arrType",
        "appType",
        new EIDVScoreDTO("sr", "EiDV", "ei", "is", "c", "d"),
        new ASMScoreDTO("sra", "ASm", "dc", "dt", "ca", "da"),
        "cid",
        "cidPers",
        "cn",
        "indv",
        "pn",
        "NORMAL",
        optionsMap,
        asList(new ConditionDTO("n", "k", "v"),
            new ConditionDTO("INTEND_TO_SWITCH", null, "true"),
            new ConditionDTO("INTEND_TO_OVERDRAFT", null, "true"),
            new ConditionDTO("OVERDRAFT_AMOUNT", null, "200")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        null,
        "cbsProductNumber",
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        "pid",
        "309073",
        "pfid",
        null,
        new SIRAScoreDTO(),
        new ThreatMatrixDTO(),
        Collections.<MarketingPreferenceDTO>emptyList());
        /*
         * return new ProductOfferedDTO("aid", "arrType", "appType", new
         * EIDVScoreDTO("sr", "EiDV", "ei", "is", "c", "d"), new
         * ASMScoreDTO("sra", "ASm", "dc", "dt", "ca", "da"), "cid", "cidPers",
         * "cn", "indv", "pn", "NORMAL", optionsMap, asList(new
         * ConditionDTO("n", "k", "v"), new ConditionDTO("INTEND_TO_SWITCH",
         * null, "true"), new ConditionDTO("INTEND_TO_OVERDRAFT", null, "true"),
         * new ConditionDTO(
         *
         * "OVERDRAFT_AMOUNT", null, null)), "appStatus", "appSubStatus",
         * "mnemonic", null, new ExistingProductArrangementDTO(null), true, new
         * BigDecimal("200"), null, "309073", "pdtFamilyId");
         */ }

    private OfferProductArrangementResponse testResponse() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { eidvScore(), asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        return response;
    }

    private OfferProductArrangementResponse testResponse3() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setApplicationStatus("1004");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { eidvScore(), asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        return response;
    }

    private OfferProductArrangementResponse testResponse5() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setApplicationStatus("1004");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        return response;
    }

    private OfferProductArrangementResponse testResponse4() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        final Product assproduct = new Product();
        final ProductEligibilityDetails detailpro = new ProductEligibilityDetails();
        final ReasonCode[] codes = new ReasonCode[1];
        final ReasonCode code = new ReasonCode();
        code.setCode("232323");
        code.setDescription("SomeError");
        codes[0] = code;

        detailpro.setDeclineReasons(codes);
        assproduct.setEligibilityDetails(detailpro);
        productArrangement.setAssociatedProduct(assproduct);
        productArrangement.setApplicationStatus("1004");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { eidvScore(), asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        return response;
    }

    private OfferProductArrangementResponse testResponseSIRA() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { SIRAScore(), asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        return response;
    }

    private OfferProductArrangementResponse testResponseWithExistingProductArrangements() {
        final OfferProductArrangementResponse response = new OfferProductArrangementResponse();
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        productArrangement.setArrangementType("arrType");
        productArrangement.setApplicationType("appType");
        final OverdraftDetails overdraftDetails = new OverdraftDetails();
        final CurrencyAmount amount = new CurrencyAmount();
        amount.setAmount(new BigDecimal("200"));
        overdraftDetails.setAmount(amount);
        productArrangement.setOverdraftDetails(overdraftDetails);
        productArrangement.setIsOverdraftRequired(true);
        final Product product = new Product();
        final ExtSysProdIdentifier identifier = new ExtSysProdIdentifier();
        identifier.setSystemCode(MNEMONIC_ID);
        identifier.setProductIdentifier("mnemonic");
        product.setProductName("pn");
        product.setProductIdentifier("pid");

        final ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier("pfid");
        final ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        final ProductFamily pdtFamily = new ProductFamily();
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        final ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);

        final ProductOffer productOffer = new ProductOffer();
        productOffer.setOfferType("2001");
        final ProductOffer[] productOffers = new ProductOffer[] { productOffer };
        product.setProductoffer(productOffers);

        final ProductOptions pdtOptions = new ProductOptions();
        pdtOptions.setOptionsName("Make_It_Joint");
        pdtOptions.setOptionsValue("N");
        final ProductOptions[] productoptions = new ProductOptions[] { pdtOptions };
        product.setProductoptions(productoptions);

        product.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { identifier });
        productArrangement.setOfferedProducts(new Product[] { product });
        final Customer customer = new Customer();
        customer.setCustomerScore(new CustomerScore[] { eidvScore(), asmScore() });
        customer.setCustomerIdentifier("cid");
        customer.setCidPersID("cidPers");
        customer.setCustomerNumber("cn");
        customer.setIndividualIdentifier("indv");
        productArrangement.setPrimaryInvolvedParty(customer);
        productArrangement.setArrangementId("aid");
        final RuleCondition condition = new RuleCondition();
        condition.setName("n");
        condition.setResult("v");
        condition.setRuleCode("k");
        // New Attributes changes
        final RuleCondition condition1 = new RuleCondition();
        condition1.setName("INTEND_TO_SWITCH");
        condition1.setResult("true");

        final RuleCondition condition2 = new RuleCondition();
        condition2.setName("INTEND_TO_OVERDRAFT");
        condition2.setResult("true");

        final CurrencyAmount odAmount = new CurrencyAmount();
        odAmount.setAmount(new BigDecimal(200));

        final RuleCondition condition3 = new RuleCondition();
        condition3.setName("OVERDRAFT_AMOUNT");
        condition3.setValue(odAmount);

        productArrangement.setConditions(new RuleCondition[] { condition, condition1, condition2, condition3 });
        response.setProductArrangement(productArrangement);
        final ProductArrangement existingProductArrangement = new ProductArrangement();
        existingProductArrangement.setAccountNumber("accountNumber");
        final Product associatedProduct = new Product();
        associatedProduct.setProductIdentifier("productIdentifier");
        final ExtSysProdIdentifier externalSystemProductIdentifier = new ExtSysProdIdentifier();
        externalSystemProductIdentifier.setSystemCode(CBS_PRODUCT_SYSTEM_CODE);
        externalSystemProductIdentifier.setProductIdentifier("productIdentifier");
        associatedProduct
                .setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { externalSystemProductIdentifier });
        existingProductArrangement.setLifecycleStatus("lifecycleStatus");
        final Organisation financialInstitution = new Organisation();
        final OrganisationUnit unit = new OrganisationUnit();
        unit.setSortCode("309073");
        financialInstitution.setHasOrganisationUnits(new OrganisationUnit[] { unit });
        existingProductArrangement.setFinancialInstitution(financialInstitution);
        existingProductArrangement.setAssociatedProduct(associatedProduct);
        final Calendar arrangementStartDate = Calendar.getInstance();
        existingProductArrangement.setArrangementStartDate(arrangementStartDate);
        // existingProductArrangement.getE
        response.setExistingProductArrangements(new ProductArrangement[] { existingProductArrangement });

        return response;
    }

    private CustomerScore eidvScore() {
        final CustomerScore customerScore = new CustomerScore();
        customerScore.setScoreResult("sr");
        customerScore.setAssessmentType("EiDV");
        final AssessmentEvidence assessmentEvidence = new AssessmentEvidence();
        assessmentEvidence.setEvidenceIdentifier("ei");
        assessmentEvidence.setIdentityStrength("is");
        final ReferralCode referralCode = new ReferralCode();
        referralCode.setCode("c");
        referralCode.setDescription("d");
        customerScore.setReferralCode(new ReferralCode[] { referralCode });
        customerScore.setAssessmentEvidence(new AssessmentEvidence[] { assessmentEvidence });
        return customerScore;
    }

    private CustomerScore SIRAScore() {
        final CustomerScore customerScore = new CustomerScore();
        customerScore.setScoreResult("sr");
        final CustomerDecision dec = new CustomerDecision();
        dec.setResultStatus("sds");
        dec.setConnectivityErrorFlag("ss");
        dec.setTotalEnquiryMatchCount("ss");
        dec.setTotalRuleScore("ss");
        dec.setConnectivityErrorFlag("false");
        customerScore.setCustomerDecision(dec);
        customerScore.setAssessmentType("SIRA");
        final AssessmentEvidence assessmentEvidence = new AssessmentEvidence();
        assessmentEvidence.setEvidenceIdentifier("ei");
        assessmentEvidence.setIdentityStrength("is");
        final ReferralCode referralCode = new ReferralCode();
        referralCode.setCode("c");
        referralCode.setDescription("d");
        customerScore.setReferralCode(new ReferralCode[] { referralCode });
        customerScore.setAssessmentEvidence(new AssessmentEvidence[] { assessmentEvidence });
        return customerScore;
    }

    private CustomerScore asmScore() {
        final CustomerScore customerScore = new CustomerScore();
        customerScore.setScoreResult("sra");
        customerScore.setAssessmentType("ASm");
        customerScore.setDecisionCode("dc");
        customerScore.setDecisionText("dt");
        final ReferralCode referralCode = new ReferralCode();
        referralCode.setCode("ca");
        referralCode.setDescription("da");
        customerScore.setReferralCode(new ReferralCode[] { referralCode });
        return customerScore;
    }

    private OfferProductDTO testRequest() {
        final PhoneDTO mobile = new PhoneDTO("44", "207", "5555555", "", "001");
        final PhoneDTO homePhone = new PhoneDTO("44", "207", "5555555", "", "002");
        final PhoneDTO workPhone = new PhoneDTO("44", "207", "5555555", "", "003");
        final PostalAddressComponentDTO currentAddress = new PostalAddressComponentDTO(
                new StructuredPostalAddressDTO("district", "town", "county", "organisation", "subBuilding", "building",
                        "buildingNumber", asList("addressLines")),
                "1010", "CURRENT", "suffix", "postcode", false, false);
        final PostalAddressComponentDTO previousAddress = new PostalAddressComponentDTO(
                new UnstructuredPostalAddressDTO("line1", "line2", "line3", "line4"), "1010", "CURRENT", "suffix",
                "postcode", false, false);
        final Date birthDate = new Date();
        final Date ukResidenceStartDate = new Date();
        final Date visaExpiryDate = new Date();
        final EmploymentDTO employment = new EmploymentDTO("occupation", "employmentStatus", "employerName",
                "employerAddressLine1", "employerAddressLine2", "employerPostcode", "10", "10");
        final Map<String, String> map1 = new HashMap<String, String>();
        map1.put("cname", "cvalue");
        final Map<String, String> map2 = new HashMap<String, String>();
        map2.put("epic", "epii");

        final OfferProductDTO offerProductDTO = new OfferProductDTO(SwitchOptions.Yes, false, 200, "prefixTitleName",
                "firstName", "middleName", "lastName", "emailAddress", mobile, homePhone, workPhone, currentAddress,
                previousAddress, "gender", birthDate, "maritalStatus", new BigInteger("1"), employment,
                new BigDecimal("2"), "residentialStatus", ukResidenceStartDate, visaExpiryDate, true, true, true, true,
                new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("4"), "fundingSource", "accountPurpose", "pid",
                map1, map2, "pname", "mnemonic", new RelatedApplicationDTO(), new ThreatMatrixDTO(), null,"100");
        offerProductDTO.setAccType("CA");
        setBirthCityAndTinDetails(offerProductDTO);
        return offerProductDTO;
    }

    private OfferProductDTO testRequestSA() {
        final PhoneDTO mobile = new PhoneDTO("44", "207", "5555555", "", "001");
        final PhoneDTO homePhone = new PhoneDTO("44", "207", "5555555", "", "002");
        final PhoneDTO workPhone = new PhoneDTO("44", "207", "5555555", "", "003");
        final PostalAddressComponentDTO currentAddress = new PostalAddressComponentDTO(
                new StructuredPostalAddressDTO("district", "town", "county", "organisation", "subBuilding", "building",
                        "buildingNumber", asList("addressLines")),
                "1010", "CURRENT", "suffix", "postcode", false, false);
        final PostalAddressComponentDTO previousAddress = new PostalAddressComponentDTO(
                new UnstructuredPostalAddressDTO("line1", "line2", "line3", "line4"), "1010", "CURRENT", "suffix",
                "postcode", false, false);
        final Date birthDate = new Date();
        final Date ukResidenceStartDate = new Date();
        final Date visaExpiryDate = new Date();
        final EmploymentDTO employment = new EmploymentDTO("occupation", "employmentStatus", "employerName",
                "employerAddressLine1", "employerAddressLine2", "employerPostcode", "10", "10");
        final Map<String, String> map1 = new HashMap<String, String>();
        map1.put("cname", "cvalue");
        final Map<String, String> map2 = new HashMap<String, String>();
        map2.put("epic", "epii");

        final OfferProductDTO offerProductDTO = new OfferProductDTO(SwitchOptions.Yes, false, 200, "prefixTitleName",
                "firstName", "middleName", "lastName", "emailAddress", mobile, homePhone, workPhone, currentAddress,
                previousAddress, "gender", birthDate, "maritalStatus", new BigInteger("1"), employment,
                new BigDecimal("2"), "residentialStatus", ukResidenceStartDate, visaExpiryDate, true, true, true, true,
                new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("4"), "fundingSource", "accountPurpose", "pid",
                map1, map2, "pname", "mnemonic", new RelatedApplicationDTO(), new ThreatMatrixDTO(), null,"100");
        offerProductDTO.setAccType("SA");
        setBirthCityAndTinDetails(offerProductDTO);
        return offerProductDTO;
    }

    private void setBirthCityAndTinDetails(final OfferProductDTO offerProductDTO) {
        offerProductDTO.setBirthCity(BIRTH_CITY);
        tinDetails.setBirthCountry(UNITED_KINGDOM);
        nationalities.add("GBR");
        nationalities.add("DZA");
        nationalities.add("ALA");
        tinDetails.setNationalities(nationalities);
        final TaxResidencyDetailsDTO taxResidencyDetails = new TaxResidencyDetailsDTO();
        taxResidencyDetails.setTaxResidency("ALA");
        final TaxResidencyDetailsDTO taxResidencyDetails1 = new TaxResidencyDetailsDTO();
        taxResidencyDetails1.setTaxResidency("GBR");
        taxResidencyDetails1.setTinNumber("ABC");
        final TaxResidencyDetailsDTO taxResidencyDetails2 = new TaxResidencyDetailsDTO();
        taxResidencyDetails2.setTaxResidency("USA");
        taxResidencyDetails2.setTinNumber("1234");
        taxResidencyDetails2.setTaxResidencyType(findTaxResidencyTypeFromCode("1").toString());
        taxResidencies.add(taxResidencyDetails);
        taxResidencies.add(taxResidencyDetails1);
        taxResidencies.add(taxResidencyDetails2);
        tinDetails.setTaxResidencies(taxResidencies);
        offerProductDTO.setTinDetails(tinDetails);
    }

    private DAOResponse<ProductOfferedDTO> whenOfferIsCalled() throws ServiceException {
        // OfferProductDAOImpl offerProductDAO =
        // Mockito.mock(OfferProductDAOImpl.class);
        return offerProductDAO.offer(request);
    }

    private DAOResponse<ProductOfferedDTO> whenOfferIsCalled2() throws ServiceException {
        // OfferProductDAOImpl offerProductDAO = new OfferProductDAOImpl();

        return offerProductDAO.offer(request2);
    }

}