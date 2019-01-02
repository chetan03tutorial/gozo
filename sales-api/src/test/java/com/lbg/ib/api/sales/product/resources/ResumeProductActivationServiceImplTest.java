package com.lbg.ib.api.sales.product.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.constants.CommonConstants.ActivationConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.product.service.ResumeProductActivationServiceImpl;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CurrencyAmount;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Rates;

@RunWith(MockitoJUnitRunner.class)
public class ResumeProductActivationServiceImplTest {
    @InjectMocks
    ResumeProductActivationServiceImpl resumeProductActivationService;

    @Mock
    SessionManagementDAO               session;

    @Test
    public void testResumeProductActivation() {
        CustomerPendingDetails customerPendingDetails = new CustomerPendingDetails();
        resumeProductActivationService.resumeProductActivation(customerPendingDetails);
        verify(session, times(1)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));
    }

    @Test
    public void testResumeProductActivationWithValidProduct() {
        CustomerPendingDetails customerPendingDetails = new CustomerPendingDetails();
        customerPendingDetails.setAssociatedProduct(new Product());
        resumeProductActivationService.resumeProductActivation(customerPendingDetails);
        verify(session, times(1)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));
    }

    @Test
    public void testResumeProductActivationWithValidProductCustomerScore() {
        CustomerPendingDetails customerPendingDetails = new CustomerPendingDetails();
        customerPendingDetails.setAssociatedProduct(new Product());
        List<CustomerScore> customerScoreList = new ArrayList<CustomerScore>();
        customerScoreList.add(new CustomerScore());
        customerPendingDetails.setCustomerScores(customerScoreList);
        resumeProductActivationService.resumeProductActivation(customerPendingDetails);

        verify(session, times(1)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));
    }

    @Test
    public void testResumeProductActivationWithValidProductCustomerScoreWithEidv() {
        CustomerPendingDetails customerPendingDetails = new CustomerPendingDetails();
        customerPendingDetails.setAssociatedProduct(new Product());
        List<CustomerScore> customerScoreList = new ArrayList<CustomerScore>();
        CustomerScore customerScore = new CustomerScore();
        customerScore.setAssessmentType("EIDV");
        customerScoreList.add(customerScore);
        customerPendingDetails.setCustomerScores(customerScoreList);
        resumeProductActivationService.resumeProductActivation(customerPendingDetails);

        verify(session, times(1)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));
    }

    @Test
    public void testResumeProductActivationWithValidProductCustomerScoreWithEidvOD() {
        CustomerPendingDetails customerPendingDetails = new CustomerPendingDetails();
        customerPendingDetails.setAssociatedProduct(new Product());
        List<CustomerScore> customerScoreList = new ArrayList<CustomerScore>();
        CustomerScore customerScore = new CustomerScore();
        customerScore.setAssessmentType("EIDV");
        customerScoreList.add(customerScore);
        customerPendingDetails.setCustomerScores(customerScoreList);

        OverdraftDetails overDraftDetails = new OverdraftDetails();
        customerPendingDetails.setOverdraftDetails(overDraftDetails);
        resumeProductActivationService.resumeProductActivation(customerPendingDetails);

        verify(session, times(1)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));

        CurrencyAmount currencyAmount = new CurrencyAmount();
        currencyAmount.setAmount(new BigDecimal("1"));
        overDraftDetails.setAmount(currencyAmount);

        resumeProductActivationService.resumeProductActivation(customerPendingDetails);
        verify(session, times(2)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));

        List<String> list = new ArrayList<String>();
        list.add(ActivationConstant.AUTH_EAR);
        list.add(ActivationConstant.AUTH_MONTHLY);
        list.add(ActivationConstant.BASE_INT_RATE);
        list.add(ActivationConstant.MARGIN_OBR_RATE);
        list.add(ActivationConstant.UNAUTH_EAR);
        list.add(ActivationConstant.UNAUTH_MONTHLY_AMT_MONTHLY_FEE);
        list.add(ActivationConstant.EXCESS_FEE_CAP);

        Rates[] ratesArray = new Rates[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ratesArray[i] = new Rates();
            ratesArray[i].setType(list.get(i));
            ratesArray[i].setValue(new BigDecimal("1"));
        }

        overDraftDetails.setInterestRates(ratesArray);

        resumeProductActivationService.resumeProductActivation(customerPendingDetails);
        verify(session, times(3)).setArrangeToActivateParameters(any(ArrangeToActivateParameters.class));

    }

}
