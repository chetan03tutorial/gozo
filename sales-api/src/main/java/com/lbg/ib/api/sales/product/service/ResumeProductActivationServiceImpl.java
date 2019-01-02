package com.lbg.ib.api.sales.product.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.dao.constants.CommonConstants.ActivationConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.product.resources.ResumeProductActivationService;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Rates;

@Component
public class ResumeProductActivationServiceImpl implements ResumeProductActivationService {

    @Autowired
    private SessionManagementDAO session;

    @TraceLog
    public void resumeProductActivation(CustomerPendingDetails customerPendingDetails) throws ServiceException {
        // update the session details required for activation by the
        updateArrangementDetailsInSession(customerPendingDetails);
    }

    public void updateArrangementDetailsInSession(CustomerPendingDetails customerPendingDetails) {
        ArrangeToActivateParameters params = new ArrangeToActivateParameters();
        params.setArrangementType(customerPendingDetails.getArrangementType());
        params.setAppStatus(customerPendingDetails.getArrangementStatus());
        params.setApplicationType(customerPendingDetails.getApplicationType());
        params.setOcisId(customerPendingDetails.getCustomerIdentifier());
        params.setPartyId(customerPendingDetails.getCidPersID());

        params.setSortcode(customerPendingDetails.getSortCode());
        Product product = customerPendingDetails.getAssociatedProduct();
        if (product != null) {
            params.setProductFamilyIdentifier(product.getPdtFamilyIdentifier());
            params.setProductId(product.getIdentifier());
            params.setProductMnemonic(product.getMnemonic());
            params.setProductName(product.getName());
            updatedSelectedProductInSession(product);

        }
        if (customerPendingDetails.getCustomerScores() != null) {
            for (CustomerScore custScore : customerPendingDetails.getCustomerScores()) {
                if ("EIDV".equalsIgnoreCase(custScore.getAssessmentType())) {
                    params.setEidvStatus(custScore.getScoreResult());
                    break;
                }
            }
        }
        if (customerPendingDetails.getOverdraftDetails() != null) {
            params.setOverdraftIntrestRates(mapOverDraftInterestRates(customerPendingDetails.getOverdraftDetails()));
        }
        session.setArrangeToActivateParameters(params);
    }

    private void updatedSelectedProductInSession(Product product) {
        SelectedProduct selproduct = new SelectedProduct(product.getName(), product.getIdentifier(),
                product.getPdtFamilyIdentifier(), null, product.getMnemonic(), null,null);
        selproduct.addAlternateProduct(selproduct);
        session.setSelectedProduct(selproduct);
    }

    private OverdraftIntrestRates mapOverDraftInterestRates(OverdraftDetails overdraftDetails) {
        OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();

        if (overdraftDetails.getAmount() != null) {
            overdraftIntrestRates.setAmtOverdraft(overdraftDetails.getAmount().getAmount());
        }

        Rates[] rates = overdraftDetails.getInterestRates();
        if (rates != null && rates.length > 0) {
            Map<String, BigDecimal> rateMap = createRatesMap(rates);
            overdraftIntrestRates.setAmtExcessFee(getFeeAmountForType(ActivationConstant.AMT_EXCESS_FEE, rateMap));
            overdraftIntrestRates
                    .setAmtExcessFeeBalIncr(getFeeAmountForType(ActivationConstant.AMT_EXCESS_FEE_BAL_INC, rateMap));
            overdraftIntrestRates
                    .setAmtIntFreeOverdraft(getFeeAmountForType(ActivationConstant.INT_FREE_OVERDRAFT, rateMap));
            overdraftIntrestRates
                    .setAmtTotalCreditCost(getFeeAmountForType(ActivationConstant.TOTAL_COST_OF_CREDIT, rateMap));

            overdraftIntrestRates
                    .setAmtUsageFeeOverdraft(getFeeAmountForType(ActivationConstant.AMT_MONTHLY_FEE, rateMap));
            overdraftIntrestRates.setIntrateAuthEAR(getInterestRateForType(ActivationConstant.AUTH_EAR, rateMap));
            overdraftIntrestRates
                    .setIntrateAuthMnthly(getInterestRateForType(ActivationConstant.AUTH_MONTHLY, rateMap));
            overdraftIntrestRates.setIntrateBase(getInterestRateForType(ActivationConstant.BASE_INT_RATE, rateMap));
            overdraftIntrestRates
                    .setIntrateMarginOBR(getInterestRateForType(ActivationConstant.MARGIN_OBR_RATE, rateMap));
            overdraftIntrestRates.setIntrateUnauthEAR(getInterestRateForType(ActivationConstant.UNAUTH_EAR, rateMap));
            overdraftIntrestRates.setIntrateUnauthMnthly(
                    getInterestRateForType(ActivationConstant.UNAUTH_MONTHLY_AMT_MONTHLY_FEE, rateMap));

            overdraftIntrestRates.setNExcessFeeCap(getExcessFeeForType(ActivationConstant.EXCESS_FEE_CAP, rateMap));
        }
        return overdraftIntrestRates;

    }

    private BigDecimal getFeeAmountForType(String type, Map<String, BigDecimal> map) {
        return map.get(type);
    }

    private String getInterestRateForType(String type, Map<String, BigDecimal> map) {
        return String.valueOf(map.get(type).doubleValue());
    }

    private BigInteger getExcessFeeForType(String type, Map<String, BigDecimal> map) {
        return map.get(type).toBigInteger();
    }

    private Map<String, BigDecimal> createRatesMap(Rates[] rates) {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (Rates rate : rates) {
            map.put(rate.getType(), rate.getValue());
        }
        return map;

    }

}
