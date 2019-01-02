package com.lbg.ib.api.sales.overdraft.mapper;

import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.overdraft.domain.E160Response;
import com.lloydstsb.www.CBSRequestGp;
import com.lloydstsb.www.CustNoGp1;
import com.lloydstsb.www.CustNoGp2;
import com.lloydstsb.www.E160Req;
import com.lloydstsb.www.E160Resp;

public class E160MessageMapper {

	public E160Req buildE160Req(E160Request serviceRequest) {
		CustNoGp1 custGroup1 = new CustNoGp1();
		CustNoGp2 custGroup2 = new CustNoGp2();
		E160Req e160Req = new E160Req();
		e160Req.setMaxRepeatGroupQy(0);
		CBSRequestGp cbsRequestGroup = new CBSRequestGp();
		if (StringUtils.isNotEmpty(serviceRequest.getOriginatingSortcode())) {
			cbsRequestGroup.setSourceId(serviceRequest.getOriginatingSortcode());
		}
		String concatenatedAccountAndSortCode = serviceRequest.getSortCode() + serviceRequest.getAccountNumber();
		e160Req.setCBSRequestGp(cbsRequestGroup);
		e160Req.setCBSAccountNoId(concatenatedAccountAndSortCode);
		e160Req.setCustNoGp1(custGroup1);
		e160Req.setCustNoGp2(custGroup2);
		return e160Req;

	}

	public E160Response buildE160Response(E160Resp serviceResponse) {
		E160Response response = new E160Response();
		response.setCurrencyCode(serviceResponse.getCurrencyCd());
		response.setDebitLimit(serviceResponse.getDebitLimitBl());
		response.setDifferentialInterestRate1(serviceResponse.getDifferentialIntRt1());
		response.setDifferentialInterestRate2(serviceResponse.getDifferentialIntRt2());
		response.setDifferentialInterestRate3(serviceResponse.getDifferentialIntRt3());
		response.setFeatureNextReviewDate(serviceResponse.getFeatureNextReviewDt());
		response.setLendingAuthenticationCode(serviceResponse.getLendingAuthnCd());
		response.setLoanPurposeCode(serviceResponse.getLoanPurposeCd());
		response.setOverdraftLowerLimit1(serviceResponse.getOverdraftLwrLmtAm1());
		response.setOverdraftLowerLimit2(serviceResponse.getOverdraftLwrLmtAm2());
		response.setProductFeatureBeginDate(serviceResponse.getProdFeatureBeginDt());
		response.setSecurityCode(serviceResponse.getSecurityCd());
		return response;
	}

}
