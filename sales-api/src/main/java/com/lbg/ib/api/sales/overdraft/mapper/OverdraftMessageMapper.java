package com.lbg.ib.api.sales.overdraft.mapper;

import java.util.Calendar;
import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftResponseError;
import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.OdFulfillmentRequest;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sso.domain.user.Account;

public class OverdraftMessageMapper {

	private OverdraftMessageMapper() {
	}

	public static E160Request buildE160(Account account) {
		E160Request e160 = new E160Request();
		e160.setAccountNumber(account.getAccountNumber());
		e160.setSortCode(account.getSortCode());
		return e160;
	}

	public static E169Request buildE169(OdFulfillmentRequest clientRequest, Account account) {

		E169Request e169 = new E169Request();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		Date featureCreationDate = currentDate;
		calendar.add(Calendar.YEAR, 1);
		Date featureNextReviewDate = calendar.getTime();
		Date finalPaymentDueDate = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("ddMMYYYY");
		e169.setAccountNumber(account.getAccountNumber());
		e169.setSortCode(account.getSortCode());
		e169.setFeatureCreationDate(format.format(featureCreationDate));
		e169.setFeatureNextReviewDate(format.format(featureNextReviewDate));
		e169.setFinalPaymentDueDate(format.format(finalPaymentDueDate));
		e169.setDebitLimitBalance(clientRequest.getDemandedOd());
		return e169;
	}

	public static E170Request buildE170(OdFulfillmentRequest clientRequest, Account account) {
		E170Request e170 = new E170Request();
		e170.setAccountNumber(account.getAccountNumber());
		e170.setSortCode(account.getSortCode());
		e170.setDebitLimitBalance(clientRequest.getDemandedOd());
		return e170;
	}

	public static ResponseError createServiceError(OverdraftResponseError odError) {
		return new ResponseError(odError.getErrorCode(), odError.getErrorMessage());
	}
	
	
}
