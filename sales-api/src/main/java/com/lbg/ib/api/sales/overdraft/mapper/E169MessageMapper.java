package com.lbg.ib.api.sales.overdraft.mapper;

import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Response;
import com.lloydstsb.www.CBSRequestGp2;
import com.lloydstsb.www.CustNo1Gp;
import com.lloydstsb.www.CustNo2Gp;
import com.lloydstsb.www.E169Req;
import com.lloydstsb.www.E169Resp;

public class E169MessageMapper {


	public E169Req buildE169Req(E169Request clientSideValues) {
		CustNo1Gp custNo1Grp = new CustNo1Gp();
		CustNo2Gp custNo2Grp = new CustNo2Gp();
		E169Req e169Req = new E169Req();
		e169Req.setMaxRepeatGroupQy(0);
		CBSRequestGp2 cbsRequestGroup = new CBSRequestGp2();
		if (StringUtils.isNotEmpty(clientSideValues.getOriginatingSortcode())) {
			cbsRequestGroup.setSourceId(clientSideValues.getOriginatingSortcode());
		}
		String concatenatedAccountAndSortCode = clientSideValues.getSortCode() + clientSideValues.getAccountNumber();
		e169Req.setCBSAccountNoId(concatenatedAccountAndSortCode);
		e169Req.setCBSRequestGp2(cbsRequestGroup);
		e169Req.setCurrencyCd("GBP");
		e169Req.setCurrencyFlagCd(1);
		e169Req.setDebitLimitBl(clientSideValues.getDebitLimitBalance());
		e169Req.setFeatureCreatedDt(clientSideValues.getFeatureCreationDate());
		e169Req.setFeatureNextReviewDt(clientSideValues.getFeatureNextReviewDate());
		e169Req.setFinalPaymntDueDt(clientSideValues.getFinalPaymentDueDate());
		e169Req.setSecurityFlagCd(1);
		e169Req.setSecurityCd(1);
		e169Req.setLoanPurposeFlagCd(1);
		e169Req.setLoanPurposeCd(0);
		e169Req.setLendingAuthnFlagCd(1);
		e169Req.setLendingAuthnCd(1);
		e169Req.setAccountReportFlagIn(0);
		e169Req.setLoanSecurityFlagCd(0);
		e169Req.setCAPSDcnFlagIn(0);
		e169Req.setCAPSDcnOverruleFlagCd(0);
		e169Req.setCAPSDcnOverrideFlagCd(0);
		e169Req.setLoanAppSrcFlagCd(0);
		e169Req.setDbtLmtTypeFlagCd(1);
		e169Req.setDbtLmtTypeCd(1);
		e169Req.setDbtLmtFndSrcFlagCd(1);
		e169Req.setDbtLmtFndSrcSrceCd(14);
		e169Req.setDbtLmtDcrFrqFlagCd(0);
		e169Req.setDbtLmtDcrOcrFlagQy(0);
		e169Req.setCustNo1Gp(custNo1Grp);
		e169Req.setCustNo2Gp(custNo2Grp);
		e169Req.setDbtLmtRevertToAm(null);
		return e169Req;

	}

	public E169Response buildE169Response(E169Resp serviceResponse) {
		E169Response response = new E169Response();
		response.setHasOdIssued(true);
		return response;
	}

}
