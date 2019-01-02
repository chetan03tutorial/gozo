/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.product.crosssell;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.PromotionalCustomerInstructionRequestMapper;
import com.lbg.ib.api.sales.dao.product.offercrosssell.CrossSellEligibilityDAOImpl;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.determinepromotional.conditions.IA_DeterminePromotionalCustomerInstructions;
import com.lbg.ib.api.sales.soapapis.determinepromotional.reqres.DeterminePromotionalCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determinepromotional.reqres.DeterminePromotionalCustomerInstructionsResponse;

@RunWith(MockitoJUnitRunner.class)
public class CrossSellEligibilityDAOImplTest {

    @InjectMocks
    private CrossSellEligibilityDAOImpl                     eligibilityDAOImpl;

    @Mock
    private LoggerDAO                                       logger;

    private static final EligibilityRequestDTO              TEST_REQ                      = Test_Req();

    @Mock
    private ApiServiceProperties                            properties;

    @Mock
    private IA_DeterminePromotionalCustomerInstructions     promotionalCustomerInstructionsService;

    @Mock
    private PromotionalCustomerInstructionRequestMapper     requestMapper;

    @Mock
    private DAOError                                        error;

    @Mock
    private DAOExceptionHandler                             exceptionHandler;

    private DeterminePromotionalCustomerInstructionsRequest DETERMINE_ELIGIBILITY_REQUEST = new DeterminePromotionalCustomerInstructionsRequest();

    private InstructionDetails                              instructionDetails            = new InstructionDetails();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        instructionDetails.setInstructionMnemonic("P_CLUB");
        instructionDetails.setInstructionState("007");
        when(requestMapper.mapRequest(TEST_REQ)).thenReturn(DETERMINE_ELIGIBILITY_REQUEST);
    }




    @Test
    public void shouldReturnCrossSellMnemonicFromTheResponseWithNullResponse() throws Exception {

        Customer customer = new Customer();
        RequestHeader requestHeader = new RequestHeader();

        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        Date birthDate = new Date();
        Individual isPlayedBy = new Individual();
        requestHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        requestHeader.setChannelId("LTB");

        isPlayedBy.setBirthDate(calendar(birthDate));
        customer.setCustomerIdentifier("12334");
        customer.setIsPlayedBy(isPlayedBy);
        customer.setCustomerSegment("3");
        DETERMINE_ELIGIBILITY_REQUEST.setHeader(requestHeader);
        DETERMINE_ELIGIBILITY_REQUEST.setCustomerDetails(customer);
        DETERMINE_ELIGIBILITY_REQUEST.setInstructionDetails(instructionDetails);
        DETERMINE_ELIGIBILITY_REQUEST.setExistingProductArrangments(new ProductArrangement[] { productArrangement });
        when(promotionalCustomerInstructionsService
                .determinePromotionalCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST)).thenReturn(null);

        assertEquals(eligibilityDAOImpl.determineCrossSellEligibilityForCustomer(TEST_REQ),
                null);
        //verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnCrossSellMnemonicFromTheResponse() throws Exception {

        Customer customer = new Customer();
        RequestHeader requestHeader = new RequestHeader();

        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setApplicationStatus("appStatus");
        productArrangement.setApplicationSubStatus("appSubStatus");
        Date birthDate = new Date();
        Individual isPlayedBy = new Individual();
        requestHeader.setBusinessTransaction("DetermineEligibleCustomerInstructions");
        requestHeader.setChannelId("LTB");

        isPlayedBy.setBirthDate(calendar(birthDate));
        customer.setCustomerIdentifier("12334");
        customer.setIsPlayedBy(isPlayedBy);
        customer.setCustomerSegment("3");
        DETERMINE_ELIGIBILITY_REQUEST.setHeader(requestHeader);
        DETERMINE_ELIGIBILITY_REQUEST.setCustomerDetails(customer);
        DETERMINE_ELIGIBILITY_REQUEST.setInstructionDetails(instructionDetails);
        DETERMINE_ELIGIBILITY_REQUEST.setExistingProductArrangments(new ProductArrangement[] { productArrangement });
        when(promotionalCustomerInstructionsService
                .determinePromotionalCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST)).thenReturn(sampleResponse());

        assertEquals(eligibilityDAOImpl.determineCrossSellEligibilityForCustomer(TEST_REQ).getResult(),
                eligibleProductsMap());
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldThrowErrorIfInstructionMnemonicIsNull() throws Exception {
        DeterminePromotionalCustomerInstructionsResponse custResponse = new DeterminePromotionalCustomerInstructionsResponse();
        custResponse.setInstructionDetails(null);
        when(promotionalCustomerInstructionsService
                .determinePromotionalCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST)).thenReturn(custResponse);
        assertEquals(eligibilityDAOImpl.determineCrossSellEligibilityForCustomer(TEST_REQ).getError(),
                new DAOError("813003", "Cross sell product details cannot found in determine promotional response"));
        verify(logger).logError("813003", "Cross sell product details cannot found in determine promotional response",
                CrossSellEligibilityDAOImpl.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnErrorCodeWhenRemoteExceptionThrownByService() throws Exception {
        when(promotionalCustomerInstructionsService
                .determinePromotionalCustomerInstructions(DETERMINE_ELIGIBILITY_REQUEST))
                        .thenThrow(RemoteException.class);
        eligibilityDAOImpl.determineCrossSellEligibilityForCustomer(TEST_REQ);
        verify(exceptionHandler).handleException(any(RemoteException.class), eq(CrossSellEligibilityDAOImpl.class),
                eq("determineCrossSellEligibilityForCustomer"), eq(TEST_REQ));
    }

    private DeterminePromotionalCustomerInstructionsResponse sampleResponse() {
        DeterminePromotionalCustomerInstructionsResponse custResponse = new DeterminePromotionalCustomerInstructionsResponse();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("P_EASY_SAVER");
        instructionDetails.setPriority("2");
        custResponse.setInstructionDetails(new InstructionDetails[] { instructionDetails });
        return custResponse;
    }

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
                existingProductArrangementDTO, cnadidateInstruction, "1001776000", "777505");

    }

    private Calendar calendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    private HashMap<String, String> eligibleProductsMap() {
        HashMap<String, String> eligibleProductsMap = new HashMap<String, String>();
        eligibleProductsMap.put("2", "P_EASY_SAVER");
        return (eligibleProductsMap);
    }

    @Test
    public void testSetService(){
        eligibilityDAOImpl.setService(null);
    }
}
