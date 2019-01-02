/**
 * 8711247 T Senthil Kumar
 */

package com.lbg.ib.api.sales.dao.product.eligibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.cxf.common.i18n.Exception;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.PcaEligibilityRequestMapper;
import com.lbg.ib.api.sales.dto.product.eligibility.PcaEligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.IA_DetermineEligibleCustomerInstructions;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class PcaEligibilityDAOImplTest {

    @InjectMocks
    private PcaEligibilityDAOImpl                    pcaEligibilityDAOImpl;

    @Mock
    PcaEligibilityRequestMapper                      requestMapper;

    @Mock
    private LoggerDAO                                logger;

    @Mock
    private IA_DetermineEligibleCustomerInstructions eligibilityService;

    PcaEligibilityRequestDTO                         testPcaEligibilityRequestDTO                     = new PcaEligibilityRequestDTO();

    DetermineEligibleCustomerInstructionsRequest     testDetermineEligibleCustomerInstructionsRequest = new DetermineEligibleCustomerInstructionsRequest();

    DetermineEligibleCustomerInstructionsResponse    determineEligibleCustomerInstructionsResponse    = new DetermineEligibleCustomerInstructionsResponse();

    @Before
    public void setUp() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob("");
        testPcaEligibilityRequestDTO.setPrimaryInvolvedParty(primaryInvolvedParty);
        testPcaEligibilityRequestDTO.setArrangementType("CA");
        String[] candidateInstructions = { "G_AVA", "G_PCA" };
        testPcaEligibilityRequestDTO.setCandidateInstructions(candidateInstructions);
    }

    @Test
    public void testDetermineEligibility() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        DetermineEligibleCustomerInstructionsResponse serviceResponse = new DetermineEligibleCustomerInstructionsResponse();
        ProductEligibilityDetails[] productEligibilityDetails = new ProductEligibilityDetails[1];
        ProductEligibilityDetails productEligibilityDetail = new ProductEligibilityDetails();
        productEligibilityDetail.setIsEligible("true");
        Product[] products = new Product[1];
        Product prod = new Product();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("P_NEW_BASIC");
        prod.setInstructionDetails(instructionDetails);
        products[0] = prod;
        productEligibilityDetail.setProduct(products);
        productEligibilityDetails[0] = productEligibilityDetail;
        serviceResponse.setProductEligibilityDetails(productEligibilityDetails);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenReturn(serviceResponse);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibility(testPcaEligibilityRequestDTO);
        assertNotNull(response);
        assertTrue(response.getResult().get("P_NEW_BASIC").getIsEligible());
        assertEquals(response.getResult().get("P_NEW_BASIC").getMnemonic(), "P_NEW_BASIC");
    }

    @Test
    public void testDetermineEligibilityForDecline() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        DetermineEligibleCustomerInstructionsResponse serviceResponse = new DetermineEligibleCustomerInstructionsResponse();
        ProductEligibilityDetails[] productEligibilityDetails = new ProductEligibilityDetails[1];
        ProductEligibilityDetails productEligibilityDetail = new ProductEligibilityDetails();
        productEligibilityDetail.setIsEligible("false");
        ReasonCode[] declineReasons = new ReasonCode[1];
        ReasonCode declineReason = new ReasonCode();
        declineReason.setCode("code");
        declineReason.setDescription("description");
        declineReasons[0] = declineReason;
        productEligibilityDetail.setDeclineReasons(declineReasons);
        Product[] products = new Product[1];
        Product prod = new Product();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("instructionMnemonic");
        prod.setInstructionDetails(instructionDetails);
        products[0] = prod;
        productEligibilityDetail.setProduct(products);
        productEligibilityDetails[0] = productEligibilityDetail;
        serviceResponse.setProductEligibilityDetails(productEligibilityDetails);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenReturn(serviceResponse);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibility(testPcaEligibilityRequestDTO);
        assertEquals(response.getResult().get("instructionMnemonic").getCode(), "code");
        assertEquals(response.getResult().get("instructionMnemonic").getDesc(), "description");
    }

    @Test
    public void testDetermineEligibilityThrowsException() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenThrow(RemoteException.class);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibility(testPcaEligibilityRequestDTO);
        assertNotNull(response.getError());
    }

    @Test
    public void testDetermineEligibilityForAuth() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        DetermineEligibleCustomerInstructionsResponse serviceResponse = new DetermineEligibleCustomerInstructionsResponse();
        ProductEligibilityDetails[] productEligibilityDetails = new ProductEligibilityDetails[1];
        ProductEligibilityDetails productEligibilityDetail = new ProductEligibilityDetails();
        productEligibilityDetail.setIsEligible("true");
        Product[] products = new Product[1];
        Product prod = new Product();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("instructionMnemonic");
        prod.setInstructionDetails(instructionDetails);
        products[0] = prod;
        productEligibilityDetail.setProduct(products);
        productEligibilityDetails[0] = productEligibilityDetail;
        serviceResponse.setProductEligibilityDetails(productEligibilityDetails);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenReturn(serviceResponse);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibilityAuth(testPcaEligibilityRequestDTO);
        assertTrue(response.getResult().get("instructionMnemonic").getIsEligible());
        assertEquals(response.getResult().get("instructionMnemonic").getMnemonic(), "instructionMnemonic");
    }

    @Test
    public void testDetermineEligibilityForAuthForDecline() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        DetermineEligibleCustomerInstructionsResponse serviceResponse = new DetermineEligibleCustomerInstructionsResponse();
        ProductEligibilityDetails[] productEligibilityDetails = new ProductEligibilityDetails[1];
        ProductEligibilityDetails productEligibilityDetail = new ProductEligibilityDetails();
        productEligibilityDetail.setIsEligible("false");
        ReasonCode[] declineReasons = new ReasonCode[1];
        ReasonCode declineReason = new ReasonCode();
        declineReason.setCode("code");
        declineReason.setDescription("description");
        declineReasons[0] = declineReason;
        productEligibilityDetail.setDeclineReasons(declineReasons);
        Product[] products = new Product[1];
        Product prod = new Product();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("instructionMnemonic");
        prod.setInstructionDetails(instructionDetails);
        products[0] = prod;
        productEligibilityDetail.setProduct(products);
        productEligibilityDetails[0] = productEligibilityDetail;
        serviceResponse.setProductEligibilityDetails(productEligibilityDetails);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenReturn(serviceResponse);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibilityAuth(testPcaEligibilityRequestDTO);
        assertEquals(response.getResult().get("instructionMnemonic").getCode(), "code");
        assertEquals(response.getResult().get("instructionMnemonic").getDesc(), "description");
    }

    @Test
    public void testDetermineEligibilityForAuthException() throws java.lang.Exception {

        when(requestMapper.populateRequest(testPcaEligibilityRequestDTO))
                .thenReturn(testDetermineEligibleCustomerInstructionsRequest);
        when(eligibilityService.determineEligibleCustomerInstructions(testDetermineEligibleCustomerInstructionsRequest))
                .thenThrow(RemoteException.class);
        DAOResponse<HashMap<String, EligibilityDetails>> response = pcaEligibilityDAOImpl
                .determineEligibilityAuth(testPcaEligibilityRequestDTO);
        assertEquals(response.getError().getErrorCode(), "x1");
    }

}
