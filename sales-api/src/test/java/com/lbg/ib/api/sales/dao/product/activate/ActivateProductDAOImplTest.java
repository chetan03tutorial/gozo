package com.lbg.ib.api.sales.dao.product.activate;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ActivateRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.activate.Location;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementResponse;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDecision;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DirectDebit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OrganisationUnit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;

@RunWith(MockitoJUnitRunner.class)
public class ActivateProductDAOImplTest {

    @InjectMocks
    private ActivateProductDAOImpl             activateProductDAOImpl;

    @Mock
    private StubIA_ActivateProductArrangement  service;

    @Mock
    private ActivateProductArrangementRequest  request;

    @Mock
    private SessionManagementDAO               session;

    @Mock
    private LoggerDAO                          logger;

    @Mock
    private GBOHeaderUtility                   headerUtility;

    @Mock
    private DAOExceptionHandler                daoExceptionHandler;

    @Mock
    private ProductArrangement                 productArrangement;

    @Mock
    private ActivateProductArrangementResponse arrangementResponse;

    @Mock
    private ThreatMatrixDTO                    aThreatMatrixDTO;

    @Mock
    private ChannelBrandingDAO                 channelBrandingService;

    @Mock
    private DAOResponse<ChannelBrandDTO>       channelBrandDTOResponse;

    @Mock
    private ChannelBrandDTO                    channelBrandDTO;

    private SIRAScoreDTO                       aScoreDTO                        = getSIRAScoreDTO();

    private static final List<SOAPHeader>      SOAP_HEADERS                     = asList(new SOAPHeader());
    private static final Boolean               AND_REGISTRATION_IS_SELECTED     = true;
    private static final Boolean               AND_REGISTRATION_IS_NOT_SELECTED = false;
    private static final String                SORT_CODE                        = "777505";
    private Boolean                            OD_OPTED                         = false;
    private BigDecimal                         OD_AMT_OPTED                     = null;
    private OverdraftIntrestRates              OD_INT_RATES                     = null;
    private Location                           LOCATION                         = null;
    private AccountSwitching                   ACC_SWITCH_DTLS                  = null;
    public static final String                 WITH_PASSWORD                    = "passwd";
    public static final String                 NO_PASSWORD                      = null;
    public static final RemoteException        REMOTE_EXCEPTION                 = new RemoteException();
    private static final String                SERVICE_NAME                     = "ActivateProductArrangement";
    private static final String                SERVICE_ACTION                   = "activateProductArrangement";
    private static final String                CUSTOMER_NUMBER                  = "1234567890";
    private static final List<ConditionDTO>    CONDITION                        = null;
	private static final String				   ACCOUNTING_SORT_CODE			    = null;

    @Before
    public void setUp() throws Exception {

        intializeRequestMapper();
    }

    private void intializeRequestMapper() {
        ActivateRequestMapper requestMapper = new ActivateRequestMapper();
        when(session.getSessionId()).thenReturn("sesId");
        when(headerUtility.customPrepareSoapHeader(SERVICE_ACTION, SERVICE_NAME)).thenReturn(SOAP_HEADERS);
        Whitebox.setInternalState(activateProductDAOImpl, "requestMapper", requestMapper);
        Whitebox.setInternalState(requestMapper, "gboHeaderUtility", headerUtility);
        Whitebox.setInternalState(requestMapper, "session", session);
    }

    @Test
    public void shouldExchangeRequestAndResponseWhenPasswordIsSpecified() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        activateProductDAOImpl.setActivateService(service);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(WITH_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetIsRegistrationSelectedWhenPasswordIsNotSpecified() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        activateProductDAOImpl.setActivateService(service);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldReturnDaoErrorFromExceptionHandlerWhenServerReturnsWithAnError() throws Exception {
        activateProductDAOImpl.setActivateService(service);
        ExternalBusinessError expectedException = new ExternalBusinessError(null, null, "code", "message");
        when(service.activateProductArrangement(any(ActivateProductArrangementRequest.class)))
                .thenThrow(expectedException);

        ActivateProductDTO request = activateDto(WITH_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                ACC_SWITCH_DTLS);
        DAOError expectedDaoError = new DAOError("code", "message");

        when(daoExceptionHandler.handleException(expectedException, ActivateProductDAOImpl.CLASS_NAME,
                "activateProduct", request)).thenReturn(expectedDaoError);

        DAOError error = activateProductDAOImpl.activateProduct(request).getError();

        assertThat(error, is(expectedDaoError));
    }

    @Test
    public void shouldSetLocationDtlsWithValuesWhenLocationSpecified() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        activateProductDAOImpl.setActivateService(service);

        LOCATION = new Location("10", "20");

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetODDtlsWithValuesWhenODOpted() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        activateProductDAOImpl.setActivateService(service);

        OD_OPTED = true;
        OD_AMT_OPTED = new BigDecimal("100");
        OD_INT_RATES = new OverdraftIntrestRates();
        OD_INT_RATES.setAmtOverdraft(OD_AMT_OPTED);
        OD_INT_RATES.setAmtExcessFee(OD_AMT_OPTED);
        OD_INT_RATES.setIntrateAuthEAR("1234");

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        // assertRequestThatThe(service.isCalledWith(),
        // AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetAccSwitchDtlsWithValuesWhenAccSwitchDtlsAvailable() throws Exception {

        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetAccSwitchDtlsWithValuesWhenAccSwitchDtlsAvailableWithNullPrimaryInvolvedParty()
            throws Exception {
        ActivateProductArrangementResponse response = testResponseWithConditions();
        response.getProductArrangement().setPrimaryInvolvedParty(null);
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(response);
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();

        assertTrue(responseDto != null);
    }

    @Test
    public void testGetMnemonic() {
        ProductArrangement productArrangement = new ProductArrangement();
        String mnemonic = activateProductDAOImpl.getMnemonic(productArrangement);
        assertTrue(mnemonic == null);

        Product[] productArray = new Product[1];
        productArray[0] = new Product();
        productArrangement.setOfferedProducts(productArray);
        mnemonic = activateProductDAOImpl.getMnemonic(productArrangement);
        assertTrue(mnemonic == null);

        ExtSysProdIdentifier[] extSysProdIdentifierArray = new ExtSysProdIdentifier[1];
        extSysProdIdentifierArray[0] = new ExtSysProdIdentifier();
        extSysProdIdentifierArray[0].setSystemCode("00010");
        productArray[0].setExternalSystemProductIdentifier(extSysProdIdentifierArray);
        mnemonic = activateProductDAOImpl.getMnemonic(productArrangement);

        extSysProdIdentifierArray[0].setSystemCode("00011");
        mnemonic = activateProductDAOImpl.getMnemonic(productArrangement);
        assertTrue(mnemonic == null);
    }

    @Test
    public void testHandleErrorCaseScenario() {
        ActivateProductArrangementResponse activateResponse = new ActivateProductArrangementResponse();
        List<ConditionDTO> list = activateProductDAOImpl.handleErrorCaseScenario(activateResponse);
        assertTrue(list == null);

        ResultCondition resultCondition = new ResultCondition();
        activateResponse.setResultCondition(resultCondition);

        list = activateProductDAOImpl.handleErrorCaseScenario(activateResponse);
        assertTrue(list == null);

        ResultCondition[] condition = new ResultCondition[1];
        resultCondition.setExtraConditions(condition);

        condition[0] = new ResultCondition();
        condition[0].setReasonCode("0");
        list = activateProductDAOImpl.handleErrorCaseScenario(activateResponse);
        assertTrue(list.isEmpty());

        condition[0].setReasonCode("1");
        list = activateProductDAOImpl.handleErrorCaseScenario(activateResponse);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void shouldSetAccSwitchDtlsWithValuesWhenAccSwitchDtlsAvailableWithValidResultCondition() throws Exception {

        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement
                .returnWith(testResponseWithConditions());
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetAccSwitchDtlsWithValuesWhenAccSwitchDtlsAvailableWithValidResultConditionWithDocuments()
            throws Exception {

        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement
                .returnWith(testResponseWithConditions());
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductDTO activateProductDTO = activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                ACC_SWITCH_DTLS);
        List<com.lbg.ib.api.sales.product.domain.activate.CustomerDocument> customerDocumentArray = new ArrayList<com.lbg.ib.api.sales.product.domain.activate.CustomerDocument>();
        activateProductDTO.setCustomerDocuments(customerDocumentArray);
        ActivateProductResponseDTO responseDto = activateProductDAOImpl.activateProduct(activateProductDTO).getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetAccSwitchDtlsWithValuesWhenAccSwitchDtlsAvailableWithValidResultConditionWithReasonCode()
            throws Exception {

        ActivateProductArrangementResponse response = testResponseWithConditions();
        response.getResultCondition().setReasonCode("dummy");
        response.getResultCondition().setReasonText("dummy");
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(response);
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(
                        activateDto(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION, ACC_SWITCH_DTLS))
                .getResult();
        assertTrue(responseDto == null);
    }

    @Test
    public void testRequestMapper() {
        activateProductDAOImpl.setRequestMapper(null);
    }

    private ActivateProductArrangementResponse testResponseWithConditions() {
        ActivateProductArrangementResponse response = new ActivateProductArrangementResponse();
        ProductArrangement arrangement = new ProductArrangement();
        Customer customerDetails = new Customer();
        Product product = new Product();
        product.setProductName("pname");
        arrangement.setAssociatedProduct(product);
        Product offeredProduct = new Product();
        ExtSysProdIdentifier extSysProdIdentifier = new ExtSysProdIdentifier();
        extSysProdIdentifier.setSystemCode(ActivateProductDAOImpl.MNEMONIC_ID);
        extSysProdIdentifier.setProductIdentifier("mnemonic");
        offeredProduct.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { extSysProdIdentifier });
        arrangement.setOfferedProducts(new Product[] { offeredProduct });
        arrangement.setArrangementType("atype");
        arrangement.setArrangementId("aid");
        arrangement.setApplicationStatus("appStatus");
        arrangement.setApplicationSubStatus("appSubStatus");
        arrangement.setAccountNumber("accNo");
        Organisation organisation = new Organisation();
        OrganisationUnit unit = new OrganisationUnit();
        unit.setSortCode("sc");
        organisation.setHasOrganisationUnits(new OrganisationUnit[] { unit });
        arrangement.setFinancialInstitution(organisation);
        customerDetails.setCbsCustomerNumber(CUSTOMER_NUMBER);
        arrangement.setPrimaryInvolvedParty(customerDetails);
        response.setProductArrangement(arrangement);
        response.setResultCondition(new ResultCondition());
        return response;
    }

    @Test
    public void shouldSetDebitCardRequiredFlag() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        Condition[] conditions = new Condition[1];
        conditions[0] = new Condition("DEBIT_CARD_REQUIRED_FLAG", 0, "Y");
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(activateDtoForCondition(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                        ACC_SWITCH_DTLS, conditions))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldSetLifeStyleBenefitCode() throws Exception {
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement.returnWith(testResponse());
        Condition[] conditions = new Condition[1];
        conditions[0] = new Condition("LIFE_STYLE_BENEFIT_CODE", 0, "CIN");
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(activateDtoForCondition(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                        ACC_SWITCH_DTLS, conditions))
                .getResult();
        assertThat(responseDto, is(new ActivateProductResponseDTO("pname", "mnemonic", "atype", "aid", "appStatus",
                "appSubStatus", "accNo", "sc", CUSTOMER_NUMBER, CONDITION, aScoreDTO, null)));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    @Test
    public void shouldReturnErrorResponseIfProductArrangemnetIsNull() throws Exception {
        ActivateProductResponseDTO activatereponse = null;
        StubIA_ActivateProductArrangement service = StubIA_ActivateProductArrangement
                .returnWith(testResponseWithProductArrangementNull());
        Condition[] conditions = new Condition[1];
        conditions[0] = new Condition("LIFE_STYLE_BENEFIT_CODE", 0, "CIN");
        activateProductDAOImpl.setActivateService(service);

        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);
        ActivateProductResponseDTO responseDto = activateProductDAOImpl
                .activateProduct(activateDtoForCondition(NO_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                        ACC_SWITCH_DTLS, conditions))
                .getResult();
        assertThat(responseDto, is(activatereponse));
        assertRequestThatThe(service.isCalledWith(), AND_REGISTRATION_IS_NOT_SELECTED, OD_OPTED);
    }

    private void assertRequestThatThe(ActivateProductArrangementRequest request, Boolean isRegistrationSelected,
            Boolean odOpted) {
        DepositArrangement arrangement = (DepositArrangement) request.getProductArrangement();

        assertThat(arrangement.getArrangementId(), is("aid"));
        assertThat(arrangement.getArrangementType(), is("atype"));
        assertThat(arrangement.getApplicationType(), is("appType"));

        OrganisationUnit unit = arrangement.getFinancialInstitution().getHasOrganisationUnits()[0];
        assertThat(unit.getSortCode(), is(SORT_CODE));

        Product product = arrangement.getAssociatedProduct();
        assertThat(product.getProductName(), is("pname"));
        assertThat(product.getProductIdentifier(), is("pid"));

        ExtSysProdFamilyIdentifier productFamily = product.getAssociatedFamily()[0].getExtsysprodfamilyidentifier()[0];
        assertThat(productFamily.getProductFamilyIdentifier(), is("fid"));

        Customer customer = arrangement.getPrimaryInvolvedParty();
        if (isRegistrationSelected) {
            assertTrue(customer.getIsRegistrationSelected());
            assertThat(customer.getPassword(), is("passwd"));
        } else {
            assertFalse(customer.getIsRegistrationSelected());
            assertThat(customer.getPassword(), is(nullValue()));
        }
        // TODO assert username after we start using it
        // TODO: Assert conditions after we start using them

        if (customer.getIsPlayedBy() != null && customer.getIsPlayedBy().getCustomerLocation() != null) {
            assertThat(customer.getIsPlayedBy().getCustomerLocation().getLatitude(), is("10"));
            assertThat(customer.getIsPlayedBy().getCustomerLocation().getLongitude(), is("20"));
        } else if (customer.getIsPlayedBy() != null && customer.getIsPlayedBy().getCustomerDeviceDetails() != null) {
            assertThat(customer.getIsPlayedBy().getCustomerDeviceDetails().getWorkFlowName(),
                    is("LBG_ULLO_RT_WF1_RULES2"));
        } else {
            assertThat(customer.getIsPlayedBy(), is(nullValue()));
        }
        Boolean isOverdraftRequired = arrangement.getIsOverdraftRequired();
        OverdraftDetails overdraftDetails = arrangement.getOverdraftDetails();
        if (odOpted) {
            OD_AMT_OPTED = new BigDecimal("100");
            assertTrue(isOverdraftRequired);
            assertThat(overdraftDetails.getAmount().getAmount(), is(OD_AMT_OPTED));
            assertThat(overdraftDetails.getInterestRates(0).getType(), is("AMT_EXCESS_FEE"));
            assertThat(overdraftDetails.getInterestRates(0).getValue(), is(OD_AMT_OPTED));
            assertThat(overdraftDetails.getInterestRates(1).getType(), is("AUTH_EAR"));
            assertThat(overdraftDetails.getInterestRates(1).getValue(), is(new BigDecimal("1234")));
        } else {
            assertFalse(isOverdraftRequired);
            assertThat(overdraftDetails, is(nullValue()));
        }

        DirectDebit accountSwitchingDetails = arrangement.getAccountSwitchingDetails();
        if (accountSwitchingDetails != null) {
            assertThat(accountSwitchingDetails.getSortCode(), is("402715"));
            assertThat(accountSwitchingDetails.getAccountNumber(), is("12345678"));
            assertThat(accountSwitchingDetails.getAccountHolderName(), is("abc"));
            assertThat(accountSwitchingDetails.getCardNumber(), is("1234567890123456"));
            assertThat(accountSwitchingDetails.getCardExpiryDate(), is("10/20"));
            assertThat(accountSwitchingDetails.getMobileNumber(), is("1234123456"));
            assertThat(accountSwitchingDetails.getSwitchDate().getTime(), is(new Date(0)));
        } else {
            assertThat(accountSwitchingDetails, is(nullValue()));
        }

        RequestHeader header = request.getHeader();
        assertThat(header.getInteractionId(), is("sesId"));
        assertThat(header.getLloydsHeaders().length, is(1));
        assertThat(header.getBusinessTransaction(), is("activateProductArrangement"));
        assertFalse(arrangement.getIsJointParty());
    }

    @Test
    public void shouldNotContainDebitCardDetailsInToString() {
        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        assertFalse(ACC_SWITCH_DTLS.toString().contains("cardNumber"));
    }

    @Test
    public void shouldGetInValidSortCode() {
        ProductArrangement productArrangement = new ProductArrangement();
        assertTrue(activateProductDAOImpl.getSortCode(productArrangement) == null);
    }

    @Test
    public void testExtractSIRACustomerDecision() {
        Customer customerDetails = new Customer();
        SIRAScoreDTO aSIRAScoreDTO = new SIRAScoreDTO();
        activateProductDAOImpl.extractSIRACustomerDecision(customerDetails, aSIRAScoreDTO);

        CustomerScore[] customerScoreArray = new CustomerScore[1];
        CustomerScore customerScore = new CustomerScore();
        customerScoreArray[0] = customerScore;
        customerDetails.setCustomerScore(customerScoreArray);
        activateProductDAOImpl.extractSIRACustomerDecision(customerDetails, aSIRAScoreDTO);

        customerScore.setAssessmentType("SIRA");
        activateProductDAOImpl.extractSIRACustomerDecision(customerDetails, aSIRAScoreDTO);

        CustomerDecision customerDecision = new CustomerDecision();
        customerScore.setCustomerDecision(customerDecision);
        customerDecision.setConnectivityErrorFlag("true");
        activateProductDAOImpl.extractSIRACustomerDecision(customerDetails, aSIRAScoreDTO);
    }

    @Test
    public void shouldGetValidSortCode() {
        ProductArrangement productArrangement = new ProductArrangement();
        Organisation financialInstitution = new Organisation();
        productArrangement.setFinancialInstitution(financialInstitution);

        OrganisationUnit[] oArray = new OrganisationUnit[1];
        financialInstitution.setHasOrganisationUnits(oArray);
        assertTrue(activateProductDAOImpl.getSortCode(productArrangement) == null);

        OrganisationUnit oUnit = new OrganisationUnit();
        oUnit.setSortCode("1000");
        oArray[0] = oUnit;
        financialInstitution.setHasOrganisationUnits(oArray);

        assertTrue(activateProductDAOImpl.getSortCode(productArrangement) == "1000");

    }

    @Test
    public void shouldNotContainPasswordInToString() {
        ActivateProductDTO accountDto = activateDto(WITH_PASSWORD, OD_OPTED, OD_AMT_OPTED, OD_INT_RATES, LOCATION,
                ACC_SWITCH_DTLS);
        ACC_SWITCH_DTLS = new AccountSwitching("402715", "12345678", "abc", true, "1234567890123456", "10/20", true,
                new BigDecimal("500"), true, "1234123456", new Date(0), true);

        assertFalse(accountDto.toString().contains("password"));
    }

    private ActivateProductDTO activateDto(String password, Boolean overDraftOpted, BigDecimal odAmountOpted,
            OverdraftIntrestRates overdraftIntrestRates, Location location, AccountSwitching switchDetails) {
        ActivateProductDTO activateDto = new ActivateProductDTO("aid", "pid", "pname", "fid", "atype", "appType",
                asList(new ConditionDTO("cname", "0", "cvalue")), "uname", password, overDraftOpted, odAmountOpted,
                overdraftIntrestRates, location, switchDetails, SORT_CODE, "appStatus", ACCOUNTING_SORT_CODE);
        activateDto.setCustomerDocuments(null);
        return activateDto;
    }

    private ActivateProductDTO activateDtoForCondition(String password, Boolean overDraftOpted,
            BigDecimal odAmountOpted, OverdraftIntrestRates overdraftIntrestRates, Location location,
            AccountSwitching switchDetails, Condition[] conditions) {
        return new ActivateProductDTO("aid", "pid", "pname", "fid", "atype", "appType",
                asList(new ConditionDTO("DEBIT_CARD_REQUIRED_FLAG", "0", "Y")), "uname", password, overDraftOpted,
                odAmountOpted, overdraftIntrestRates, location, switchDetails, SORT_CODE, "appStatus", ACCOUNTING_SORT_CODE);

    }

    private ActivateProductArrangementResponse testResponse() {
        ActivateProductArrangementResponse response = new ActivateProductArrangementResponse();
        ProductArrangement arrangement = new ProductArrangement();
        Customer customerDetails = new Customer();
        Product product = new Product();
        product.setProductName("pname");
        arrangement.setAssociatedProduct(product);
        Product offeredProduct = new Product();
        ExtSysProdIdentifier extSysProdIdentifier = new ExtSysProdIdentifier();
        extSysProdIdentifier.setSystemCode(ActivateProductDAOImpl.MNEMONIC_ID);
        extSysProdIdentifier.setProductIdentifier("mnemonic");
        offeredProduct.setExternalSystemProductIdentifier(new ExtSysProdIdentifier[] { extSysProdIdentifier });
        arrangement.setOfferedProducts(new Product[] { offeredProduct });
        arrangement.setArrangementType("atype");
        arrangement.setArrangementId("aid");
        arrangement.setApplicationStatus("appStatus");
        arrangement.setApplicationSubStatus("appSubStatus");
        arrangement.setAccountNumber("accNo");
        Organisation organisation = new Organisation();
        OrganisationUnit unit = new OrganisationUnit();
        unit.setSortCode("sc");
        organisation.setHasOrganisationUnits(new OrganisationUnit[] { unit });
        arrangement.setFinancialInstitution(organisation);
        customerDetails.setCbsCustomerNumber(CUSTOMER_NUMBER);
        arrangement.setPrimaryInvolvedParty(customerDetails);
        response.setProductArrangement(arrangement);
        return response;
    }

    private ActivateProductArrangementResponse testResponseWithProductArrangementNull() {
        ActivateProductArrangementResponse response = new ActivateProductArrangementResponse();

        response.setProductArrangement(null);
        response.setHeader(header());
        return response;
    }

    private ResponseHeader header() {
        ResponseHeader header = new ResponseHeader();
        header.setArrangementId("arrangementId");
        return header;
    }

    private ActivateProductArrangementRequest testRequest() {
        ActivateProductArrangementRequest request = new ActivateProductArrangementRequest();
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setArrangementId("aid");
        request.setProductArrangement(productArrangement);
        return request;
    }

    private SIRAScoreDTO getSIRAScoreDTO() {
        return new SIRAScoreDTO(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

}
