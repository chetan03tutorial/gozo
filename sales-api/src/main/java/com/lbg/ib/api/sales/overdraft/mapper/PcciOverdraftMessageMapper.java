package com.lbg.ib.api.sales.overdraft.mapper;

import com.lbg.ib.api.sales.overdraft.domain.PcciResponse;
import com.lbg.ib.api.sales.product.domain.E632Response;

public class PcciOverdraftMessageMapper {

    public static PcciResponse buildPcciResponse(E632Response e632Response) {
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

        return pcciResponse;
    }
}
