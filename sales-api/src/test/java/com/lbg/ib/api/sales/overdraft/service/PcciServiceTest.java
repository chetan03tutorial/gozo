package com.lbg.ib.api.sales.overdraft.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.overdraft.domain.PcciResponse;
import com.lbg.ib.api.sales.product.domain.E632Response;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.product.service.E632Service;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;

@RunWith(MockitoJUnitRunner.class)
public class PcciServiceTest {

    @Mock
    private ModuleContext beansLoader;

    @Mock
    E632Service service;

    @InjectMocks
    PcciServiceImpl pcciServiceImpl;
    
    @Mock
    PcciOverdraftRequest overdraftOption;

    @Before
    public void init() {
      //  MockitoAnnotations.initMocks(this);
        when(beansLoader.getService(E632Service.class)).thenReturn(service);
    }
    @Test
    public void getPcciOverdraftDetailsTest()
    {
        E632Response e632Response = new E632Response();

        e632Response.setFeeFreeOverdraftAmount("freeAmount");
        e632Response.setDailyOverdraftAmount("amout");
        e632Response.setDailyStep("dailyStep");
        e632Response.setDailyCharge("dailyCharge");
        e632Response.setCurrency("currency");
        e632Response.setExcessFeeAmount("excessFeeAmount");
        e632Response.setExcessFeeBalance("excessFeeBalance");
        e632Response.setInterestFeeWaiverAmount("interestFeeWaiverAmount");
        e632Response.setPlannedOverdraftFee("plannedOverdraftFee");
        e632Response.setTotalCostOfCreditAmount("totalCostOfCreditAmount");
        e632Response.setUnauthorisedAnnualInterestRate("unauthorisedAnnualInterestRate");
        e632Response.setUnauthrorisedGrossProductInterestRate("unauthrorisedGrossProductInterestRate");
        e632Response.setUnauthorisedOverdraftRate("unauthorisedOverdraftRate");
        e632Response.setUsageFee("usageFee");

        when(service.e632(anyString())).thenReturn(e632Response);

        PcciResponse pcciResponse = new PcciResponse();
        pcciResponse.setFeeFreeOverdraftAmount(e632Response.getFeeFreeOverdraftAmount());
        pcciResponse.setDailyOverdraftAmount(e632Response.getDailyOverdraftAmount());
        pcciResponse.setDailyStep(e632Response.getDailyStep());
        pcciResponse.setDailyCharge(e632Response.getDailyCharge());
        pcciResponse.setCurrency(e632Response.getCurrency());
        pcciResponse.setExcessFeeAmount(e632Response.getExcessFeeAmount());
        pcciResponse.setExcessFeeBalance(e632Response.getExcessFeeBalance());
        pcciResponse.setInterestFeeWaiverAmount(e632Response.getInterestFeeWaiverAmount());
        pcciResponse.setPlannedOverdraftFee(e632Response.getPlannedOverdraftFee());
        pcciResponse.setTotalCostOfCreditAmount(e632Response.getTotalCostOfCreditAmount());
        pcciResponse.setUnauthorisedAnnualInterestRate(e632Response.getUnauthorisedAnnualInterestRate());
        pcciResponse.setUnauthrorisedGrossProductInterestRate(e632Response.getUnauthrorisedGrossProductInterestRate());
        pcciResponse.setUnauthorisedOverdraftRate(e632Response.getUnauthorisedOverdraftRate());
        pcciResponse.setUsageFee(e632Response.getUsageFee());

       // PcciServiceImpl pcciServiceImpl = new PcciServiceImpl();
        //getPcciOverdraftDetails this is the method to be tested.
        PcciResponse pcciResponse1 = pcciServiceImpl.getPcciOverdraftDetails(anyString());

        assertEquals(pcciResponse.getFeeFreeOverdraftAmount(), pcciResponse1.getFeeFreeOverdraftAmount());
        assertEquals(pcciResponse.getDailyOverdraftAmount(),pcciResponse1.getDailyOverdraftAmount());
        assertEquals(pcciResponse.getDailyStep(),pcciResponse1.getDailyStep());
        assertEquals(pcciResponse.getDailyCharge(),pcciResponse1.getDailyCharge());
        assertEquals(pcciResponse.getCurrency(),pcciResponse1.getCurrency());
        assertEquals(pcciResponse.getExcessFeeAmount(),pcciResponse1.getExcessFeeAmount());
        assertEquals(pcciResponse.getExcessFeeBalance(),pcciResponse1.getExcessFeeBalance());
        assertEquals(pcciResponse.getInterestFeeWaiverAmount(),pcciResponse1.getInterestFeeWaiverAmount());
        assertEquals(pcciResponse.getPlannedOverdraftFee(),pcciResponse1.getPlannedOverdraftFee());
        assertEquals(pcciResponse.getTotalCostOfCreditAmount(),pcciResponse1.getTotalCostOfCreditAmount());
        assertEquals(pcciResponse.getUnauthorisedAnnualInterestRate(),pcciResponse1.getUnauthorisedAnnualInterestRate());
        assertEquals(pcciResponse.getUnauthrorisedGrossProductInterestRate(),pcciResponse1.getUnauthrorisedGrossProductInterestRate());
        assertEquals(pcciResponse.getUnauthorisedOverdraftRate(),pcciResponse1.getUnauthorisedOverdraftRate());
        assertEquals(pcciResponse.getUsageFee(),pcciResponse1.getUsageFee());
    }
    @Test
    public void shouldReturnNonNullResponse()
    {
        E632Response e632Response = new E632Response();

        e632Response.setFeeFreeOverdraftAmount("freeAmount");
        e632Response.setDailyOverdraftAmount("amout");
        e632Response.setDailyStep("dailyStep");
        e632Response.setDailyCharge("dailyCharge");
        e632Response.setCurrency("currency");
        e632Response.setExcessFeeAmount("excessFeeAmount");
        e632Response.setExcessFeeBalance("excessFeeBalance");
        e632Response.setInterestFeeWaiverAmount("interestFeeWaiverAmount");
        e632Response.setPlannedOverdraftFee("plannedOverdraftFee");
        e632Response.setTotalCostOfCreditAmount("totalCostOfCreditAmount");
        e632Response.setUnauthorisedAnnualInterestRate("unauthorisedAnnualInterestRate");
        e632Response.setUnauthrorisedGrossProductInterestRate("unauthrorisedGrossProductInterestRate");
        e632Response.setUnauthorisedOverdraftRate("unauthorisedOverdraftRate");
        e632Response.setUsageFee("usageFee");

        when(service.e632(anyString())).thenReturn(e632Response);

        PcciResponse pcciResponse1 = pcciServiceImpl.getPcciOverdraftDetails(anyString());

        assertNotNull(pcciResponse1);
    }
    
    @Test
    public void checkOverDraftresponse() {

        E632Response e632Response = new E632Response();

        e632Response.setFeeFreeOverdraftAmount("freeAmount");
        e632Response.setDailyOverdraftAmount("amout");
        e632Response.setDailyStep("dailyStep");
        e632Response.setDailyCharge("dailyCharge");
        e632Response.setCurrency("currency");
        e632Response.setExcessFeeAmount("excessFeeAmount");
        e632Response.setExcessFeeBalance("excessFeeBalance");
        e632Response.setInterestFeeWaiverAmount("interestFeeWaiverAmount");
        e632Response.setPlannedOverdraftFee("plannedOverdraftFee");
        e632Response.setTotalCostOfCreditAmount("totalCostOfCreditAmount");
        e632Response.setUnauthorisedAnnualInterestRate("unauthorisedAnnualInterestRate");
        e632Response.setUnauthrorisedGrossProductInterestRate("unauthrorisedGrossProductInterestRate");
        e632Response.setUnauthorisedOverdraftRate("unauthorisedOverdraftRate");
        e632Response.setUsageFee("usageFee");
        
        PcciOverdraftRequest overDraftOption=new PcciOverdraftRequest(anyString());

        when(service.e632WithOverDraftOption(overDraftOption)).thenReturn(e632Response);
        
  
        PcciResponse pcciResponse = pcciServiceImpl.getPcciOverdraftDetailsWithOverdraftOption(overDraftOption);

        assertNotNull(pcciResponse);
    
    }

}
