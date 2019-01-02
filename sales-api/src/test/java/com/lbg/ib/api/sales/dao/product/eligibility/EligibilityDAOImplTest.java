/**
 * 8711247 T Senthil Kumar
 */

package com.lbg.ib.api.sales.dao.product.eligibility;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.EligibilityRequestMapper;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.IA_DetermineEligibleCustomerInstructions;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EligibilityDAOImplTest {

    @InjectMocks
    private EligibilityDAOImpl                           eligibilityDAOImpl;

    @Mock
    private LoggerDAO                                    logger;

    private static final EligibilityRequestDTO           TEST_REQ                      = Test_Req();

    private static final EligibilityRequestDTO           DECI_REQ                      = DECI_Req();

    @Mock
    private ApiServiceProperties                         properties;

    @Mock
    private IA_DetermineEligibleCustomerInstructions     service;

    @Mock
    private EligibilityRequestMapper                     requestMapper;

    @Mock
    private DAOError                                     error;

    private DetermineEligibleCustomerInstructionsRequest DETERMINE_ELIGIBILITY_REQUEST = new DetermineEligibleCustomerInstructionsRequest();

    private InstructionDetails                           instructionDetails            = new InstructionDetails();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        instructionDetails.setInstructionMnemonic("P_CLSCVTG");
        when(requestMapper.mapRequest(TEST_REQ)).thenReturn(DETERMINE_ELIGIBILITY_REQUEST);
    }

    @SuppressWarnings("deprecation")
    private static EligibilityRequestDTO Test_Req() {
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        ExistingProductArrangementDTO existingProductArrangementDTO = new ExistingProductArrangementDTO(
                new ProductArrangement[] { productArrangement });
        String[] cnadidateInstruction = { "CandidateInstruction" };
        Date birthDate = new Date();
        /*
         * birthDate.setDate(15); birthDate.setMonth(01);
         * birthDate.setYear(1991);
         */
        return new EligibilityRequestDTO("Customer Arrangement", birthDate, "customerIdentifier",
                existingProductArrangementDTO, cnadidateInstruction, null, null);

    }

    @SuppressWarnings("deprecation")
    private static EligibilityRequestDTO DECI_Req() {
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        PrimaryInvolvedParty party = setPrimaryInvolvedParty();
        ExistingProductArrangementDTO existingProductArrangementDTO = new ExistingProductArrangementDTO(
                new ProductArrangement[] { productArrangement });
        String[] cnadidateInstruction = { "CandidateInstruction" };
        /*
         * birthDate.setDate(15); birthDate.setMonth(01);
         * birthDate.setYear(1991);
         */
        return new EligibilityRequestDTO("CA", party, cnadidateInstruction, existingProductArrangementDTO);

    }

    @Test
    public void ShouldReturnEligibleCustomerInstructionResponseWhenCorrectRequestIsPopulated() throws Exception {

        Customer customer = new Customer();
        RequestHeader requestHeader = new RequestHeader();

        String[] candidateInstructions = { "PTG" };
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        Date birthDate = new Date();
        Individual isPlayedBy = new Individual();
        requestHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        requestHeader.setChannelId("BOS");

        isPlayedBy.setBirthDate(calendar(birthDate));
        customer.setCustomerIdentifier("12334");
        customer.setIsPlayedBy(isPlayedBy);
        customer.setCustomerIdentifier("1");
        DETERMINE_ELIGIBILITY_REQUEST.setHeader(requestHeader);
        DETERMINE_ELIGIBILITY_REQUEST.setArrangementType("CA");
        DETERMINE_ELIGIBILITY_REQUEST.setCustomerDetails(customer);
        DETERMINE_ELIGIBILITY_REQUEST.setCandidateInstructions(candidateInstructions);
        DETERMINE_ELIGIBILITY_REQUEST.setExistingProductArrangments(new ProductArrangement[] { productArrangement });

        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST)).thenReturn(sampleResponse());

        assertEquals(eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ).getResult(),
                eligibleProductsMap());
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnErrorCodeWhenRemoteExceptionThrownByService() throws Exception {
        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))

                .thenThrow(RemoteException.class);
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ);
        verify(logger).logException(eq(EligibilityDAOImpl.class), any(RemoteException.class));
        assertThat(map.getError().getErrorCode(), is(REMOTE_EXCEPTION));
    }

    @Test
    public void shouldReturnErrorCodeWhenRemoteExceptionThrownByServiceWithRemoteException() throws Exception {
        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))

                .thenThrow(RemoteException.class);
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ);
        verify(logger).logException(eq(EligibilityDAOImpl.class), any(RemoteException.class));
        assertThat(map.getError().getErrorCode(), is(REMOTE_EXCEPTION));
    }


    @Test
    public void shouldReturnErrorWhenThereIsNoProductEligibilityDetails() throws Exception {
        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))
                .thenReturn(noProductEligibilityDetails());
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ);
        assertThat(map.getError(), is(new DAOError(BUSSINESS_ERROR,
                "Product eligiblity details cannot found in determine eligiblity response")));
        verify(logger).logError(BUSSINESS_ERROR,
                "Product eligiblity details cannot found in determine eligiblity response", EligibilityDAOImpl.class);
    }

    @Test
    public void shouldReturnErrorWhenThereIsNoResultCondition() throws Exception {
        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))
                .thenReturn(noResultCondition());
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ);
        assertThat(map.getError(),
                is(new DAOError("100000", "Product eligiblity details cannot found in determine eligiblity response")));
        verify(logger).logError("100000", "Product eligiblity details cannot found in determine eligiblity response",
                EligibilityDAOImpl.class);
    }

    @Test
    public void shouldReturnErrorWhenThereIsProductEligibilityDetails() throws Exception {
        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))
                .thenReturn(productEligibilityDetails());
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl.determineEligibleCustomerInstructions(TEST_REQ);
        assertThat(map.getError(), is(new DAOError("112", "description")));
        verify(logger).logError("112", "description", EligibilityDAOImpl.class);
    }

    private DetermineEligibleCustomerInstructionsResponse sampleResponse() {
        DetermineEligibleCustomerInstructionsResponse Custresponse = new DetermineEligibleCustomerInstructionsResponse();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("P_CLSCVTG");
        Product product = new Product();
        product.setInstructionDetails(instructionDetails);
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setChannelId("BOS");
        responseHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        responseHeader.setChannelId("0000805121");
        ProductEligibilityDetails productEligibilityDetails = new ProductEligibilityDetails();
        productEligibilityDetails.setProduct(new Product[] { product });
        productEligibilityDetails.setIsEligible("true");

        Custresponse.setHeader(responseHeader);
        Custresponse.setResultCondition(null);
        Custresponse.setProductEligibilityDetails(new ProductEligibilityDetails[] { productEligibilityDetails });

        return Custresponse;
    }

    private DetermineEligibleCustomerInstructionsResponse productEligibilityDetails() {
        DetermineEligibleCustomerInstructionsResponse response = new DetermineEligibleCustomerInstructionsResponse();
        ResponseHeader resHeader = new ResponseHeader();
        resHeader.setChannelId("BOS");
        resHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        resHeader.setChannelId("0000805121");
        resHeader.setContactPointId("0000805121");
        response.setHeader(resHeader);
        Product aproduct = new Product();
        aproduct.setInstructionDetails(instructionDetails);
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setName("sk");
        resultCondition.setReasonCode(null);
        resultCondition.setReasonText("Product eligiblity details cannot found in determine eligiblity response");
        response.setResultCondition(resultCondition);
        ProductEligibilityDetails productEligibilityDetails = new ProductEligibilityDetails();
        ReasonCode reasonCode = new ReasonCode();
        reasonCode.setCode("112");
        reasonCode.setDescription("description");
        productEligibilityDetails.setDeclineReasons(new ReasonCode[] { reasonCode });
        response.setProductEligibilityDetails(new ProductEligibilityDetails[] { productEligibilityDetails });
        return response;
    }

    private DetermineEligibleCustomerInstructionsResponse noProductEligibilityDetails() {
        DetermineEligibleCustomerInstructionsResponse response = new DetermineEligibleCustomerInstructionsResponse();
        ResponseHeader resHeader = new ResponseHeader();
        resHeader.setChannelId("BOS");
        resHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        resHeader.setChannelId("0000805121");
        resHeader.setContactPointId("0000805121");
        response.setHeader(resHeader);
        Product aproduct = new Product();
        aproduct.setInstructionDetails(instructionDetails);
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setName("sk");
        resultCondition.setReasonCode(null);
        resultCondition.setReasonText("Product eligiblity details cannot found in determine eligiblity response");
        response.setResultCondition(resultCondition);
        response.setProductEligibilityDetails(null);
        return response;
    }

    private DetermineEligibleCustomerInstructionsResponse noResultCondition() {
        DetermineEligibleCustomerInstructionsResponse response = new DetermineEligibleCustomerInstructionsResponse();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode("100000");
        resultCondition.setReasonText("Product eligiblity details cannot found in determine eligiblity response");
        response.setResultCondition(resultCondition);
        ProductEligibilityDetails productEligibilityDetails = null;
        response.setProductEligibilityDetails(new ProductEligibilityDetails[] { productEligibilityDetails });
        return response;
    }

    private HashMap<String, String> eligibleProductsMap() {
        HashMap<String, String> eligibleProductsMap = new HashMap<String, String>();
        String eligibleProduct = null;
        String mnemonic = null;
        Product aproduct = new Product();
        aproduct.setInstructionDetails(instructionDetails);
        mnemonic = aproduct.getInstructionDetails().getInstructionMnemonic();
        ProductEligibilityDetails productEligibilityDetails = new ProductEligibilityDetails();
        productEligibilityDetails.setProduct(new Product[] { aproduct });
        productEligibilityDetails.setIsEligible("true");
        eligibleProduct = productEligibilityDetails.getIsEligible();
        eligibleProductsMap.put(mnemonic, eligibleProduct);
        return (eligibleProductsMap);
    }

    private Calendar calendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @Test
    public void ShouldReturnEligibilityWhenCorrectRequestIsPopulated() throws Exception {

        Customer customer = new Customer();
        RequestHeader requestHeader = new RequestHeader();

        String[] candidateInstructions = { "PTG" };
        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        Date birthDate = new Date();
        Individual isPlayedBy = new Individual();
        requestHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        requestHeader.setChannelId("BOS");

        isPlayedBy.setBirthDate(calendar(birthDate));
        customer.setCustomerIdentifier("12334");
        customer.setIsPlayedBy(isPlayedBy);
        customer.setCustomerIdentifier("1");
        DETERMINE_ELIGIBILITY_REQUEST.setHeader(requestHeader);
        DETERMINE_ELIGIBILITY_REQUEST.setArrangementType("CA");
        DETERMINE_ELIGIBILITY_REQUEST.setCustomerDetails(customer);
        DETERMINE_ELIGIBILITY_REQUEST.setCandidateInstructions(candidateInstructions);
        DETERMINE_ELIGIBILITY_REQUEST.setExistingProductArrangments(new ProductArrangement[] { productArrangement });

        when(service.determineEligibleCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST)).thenReturn(sampleResponse());

        when(requestMapper.mapRequest(any(EligibilityRequestDTO.class))).thenReturn(DETERMINE_ELIGIBILITY_REQUEST);
        when(requestMapper.populateRequest(any(EligibilityRequestDTO.class))).thenReturn(DETERMINE_ELIGIBILITY_REQUEST);

        Map<String, EligibilityDetails> res = eligibilityDAOImpl.determineEligibility(DECI_REQ).getResult();
        verifyNoMoreInteractions(logger);
    }

    private static PrimaryInvolvedParty setPrimaryInvolvedParty() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
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

}
