package com.lbg.ib.api.sales.overdraft.mapper;

import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Response;
import com.lloydstsb.www.CBSRequestGp2;
import com.lloydstsb.www.CustNo1Gp;
import com.lloydstsb.www.CustNo2Gp;
import com.lloydstsb.www.E170Req;
import com.lloydstsb.www.E170Resp;

public class E170MessageMapper  {

	public E170Req buildE170Req(E170Request clientSideValues) {
		CustNo1Gp custNo1Grp = new CustNo1Gp();
		CustNo2Gp custNo2Grp = new CustNo2Gp();
		E170Req e170Req = new E170Req();
		e170Req.setMaxRepeatGroupQy(0);
		CBSRequestGp2 cbsRequestGroup = new CBSRequestGp2();
		if (StringUtils.isNotEmpty(clientSideValues.getOriginatingSortcode())) {
			cbsRequestGroup.setSourceId(clientSideValues.getOriginatingSortcode());
		}
		e170Req.setCBSRequestGp2(cbsRequestGroup);
		String concatenatedAccountAndSortCode = clientSideValues.getSortCode() + clientSideValues.getAccountNumber();
		e170Req.setCBSAccountNoId(concatenatedAccountAndSortCode);
		e170Req.setCurrencyCd("GBP");
		e170Req.setCurrencyFlagCd(1);
		e170Req.setDebitLimitBl(clientSideValues.getDebitLimitBalance());
		e170Req.setCustNo1Gp(custNo1Grp);
		e170Req.setCustNo2Gp(custNo2Grp);
		return e170Req;
	}

	public E170Response buildE170Response(E170Resp serviceResponse) {
		E170Response response = new E170Response();
		response.setFeatureNextReviewDate(serviceResponse.getFeatNextRvwGrp().getFeatureNextReviewDt());
		response.setFeatureNextReviewFlagDate(
				String.valueOf(serviceResponse.getFeatNextRvwGrp().getFeatNextRvwFlagDt()));
		return response;
	}
}
