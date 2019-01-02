package com.lbg.ib.api.sales.shared.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.content.service.ContentService;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants;
import com.lbg.ib.api.sales.overdraft.dto.OverdraftMetaDataIndex;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
public class AccountInContextUtility {

	private static final String MAX_OVERDRAFT_LIMIT_BY_ASM = "maxOdLimitAsm";
	private static final String CREDIT_REQUEST_REFERENCE = "creditRequestReference";

	@Autowired
	private SessionManagementDAO sessionManager;

	@Autowired
	private LoggerDAO logger;

	@Autowired
	private ContentService contentService;

	public AccountInContextUtility() {
	}

	public PrimaryInvolvedParty getPrimaryInvolvedParty() {
		Arrangement arrangement = (Arrangement) sessionManager.getUserInfo();
		return arrangement.getPrimaryInvolvedParty();
	}

	public PartyDetails getPrimaryPartyDetails() {
		PartyDetails primaryParty = null;
		Map<String, PartyDetails> partyDetails = sessionManager.getAllPartyDetailsSessionInfo();
		for (Map.Entry<String, PartyDetails> entry : partyDetails.entrySet()) {
			if (entry.getValue().isPrimaryParty()) {
				primaryParty = entry.getValue();
				logger.traceLog(this.getClass(), "Primary party details are " + primaryParty.toString());
				return primaryParty;
			}
		}
		return null;
	}

	public void updatePartyDetails(String ocisId, PartyDetails party) {
		Map<String, PartyDetails> partyDetails = sessionManager.getAllPartyDetailsSessionInfo();
		if (partyDetails == null) {
			partyDetails = new HashMap<String, PartyDetails>();
		}
		partyDetails.put(ocisId, party);
	}

	public String getPrimaryPartyOcisId() {
		Map<String, PartyDetails> partyDetails = sessionManager.getAllPartyDetailsSessionInfo();
		for (Map.Entry<String, PartyDetails> entry : partyDetails.entrySet()) {
			if (entry.getValue().isPrimaryParty()) {
				return entry.getKey();
			}
		}
		return null;
	}

	public String getJointPartyOcisId() {
		Map<String, PartyDetails> partyDetails = sessionManager.getAllPartyDetailsSessionInfo();
		for (Map.Entry<String, PartyDetails> entry : partyDetails.entrySet()) {
			if (!entry.getValue().isPrimaryParty()) {
				return entry.getKey();
			}
		}
		return null;
	}

	public PartyDetails getJointPartyDetails() {
		Map<String, PartyDetails> partyDetails = sessionManager.getAllPartyDetailsSessionInfo();
		for (Map.Entry<String, PartyDetails> entry : partyDetails.entrySet()) {
			if (!entry.getValue().isPrimaryParty()) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void setMaximumOverdraftLimit(Double value) {
		sessionManager.setInUserMap(MAX_OVERDRAFT_LIMIT_BY_ASM, value);
	}

	public Double getMaximumOverdraftLimit() {

		Double maxOdLimit = (Double) sessionManager.getFromUserMap(MAX_OVERDRAFT_LIMIT_BY_ASM);
		if (maxOdLimit == null) {
			logger.traceLog(this.getClass(), "Maximum Overdraft Limit is unavailable");
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "Internal Server Error"));
		}
		return maxOdLimit;
	}

	public Double getAvailedOverdraft() {
		Double cumilativeOd = new Double("0");
		Arrangement arrangement = (Arrangement) sessionManager.getUserInfo();
		for (Account account : arrangement.getAccounts()) {
			cumilativeOd += account.getOverdraftLimit();
		}
		return cumilativeOd;
	}

	public Account getSelectedAccountDetail() {

		Arrangement arrangement = (Arrangement) sessionManager.getUserInfo();
		SelectedAccount selectedAccountDetails = sessionManager.getAccountToConvertInContext();
		if (selectedAccountDetails == null) {
			throw new ServiceException(
					new ResponseError(com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.INVALID_PRODUCT,
							"Selected Product not found"));
		}
		for (Account account : arrangement.getAccounts()) {
			if (account.getAccountNumber().equalsIgnoreCase(selectedAccountDetails.getAccountNumber())
					&& account.getSortCode().equalsIgnoreCase(selectedAccountDetails.getSortCode())) {
				return account;
			}
		}

		throw new ServiceException(
				new ResponseError(com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.INVALID_PRODUCT,
						"Selected Product not found"));

	}

	public UserContext getUserContext() {
		return sessionManager.getUserContext();
	}

	public void updateOdLimitInSelectedAccount(Double updatedOdLimit) {
		Account account = this.getSelectedAccountDetail();
		logger.traceLog(this.getClass(), "Updating the overdraft limit in the session cache to " + updatedOdLimit);
		account.setOverdraftLimit(updatedOdLimit);
		logger.traceLog(this.getClass(),
				"Updated the overdraft limit in the session cache to " + account.getOverdraftLimit());
	}

	public void updateAvailableBalanceInSelectedAccount(Double updatedValue) {
		Account account = this.getSelectedAccountDetail();
		logger.traceLog(this.getClass(), "Updating the available balance in the session cache to " + updatedValue);
		account.setAvailableBal(updatedValue);
		logger.traceLog(this.getClass(),
				"Updated the available balance in the session cache to " + account.getAvailableBal());
	}

	public Map<String, PartyDetails> getAllPartyDetails() {
		return sessionManager.getAllPartyDetailsSessionInfo();
	}

	public String getPrimaryPartyCidPersonalId() {
		return sessionManager.getUserInfo().getPartyId();
	}

	public String getPrimaryPartyCbsCustomerNumber() {
		return sessionManager.getUserInfo().getCbsCustomerNumber();
	}

	public String getMaxOfferedByAsm() {
		return sessionManager.getMaxOverDraftLimit();
	}

	public String getCreditScoreSourceSystemCd() {
		String fileName = OverdraftConstants.FILE_NAME_CREDIT_SCORE_SOURCE_SYSTEM;
		String creditScoreSourceSystemCd = null;
		try {
			logger.traceLog(this.getClass(), "Going to fetch creditScoreSourceSystemCode from fileName: " + fileName);
			OverdraftMetaDataIndex overdraftMetaDataIndex = contentService.genericContent(fileName,
					OverdraftMetaDataIndex.class);
			if (overdraftMetaDataIndex != null && overdraftMetaDataIndex.getOverdraftMetaData() != null) {
				Map<String, String> overdraftParams = contentService
						.genericContent(overdraftMetaDataIndex.getOverdraftMetaData().getUrl(), Map.class);
				creditScoreSourceSystemCd = overdraftParams.get("creditScoreSourceSystemCd");
			}
		} catch (Exception ex) {
			logger.traceLog(this.getClass(),
					"Exception in fetching creditScoreSourceSystemCode from IIS: " + ex.getMessage());
			logger.logException(this.getClass(), ex);
		}
		logger.traceLog(this.getClass(), "creditScoreSourceSystemCode value is: " + creditScoreSourceSystemCd);
		return creditScoreSourceSystemCd;
	}

	public void setDemandedOverdraft(Double demandedOd) {
		sessionManager.setDemandedOD(demandedOd);
	}

	public Double getDemandedOverdraft() {
		return sessionManager.getDemandedOD();
	}

	public void setCreditRequestReferenceNumber(String value) {
		sessionManager.setInUserMap(CREDIT_REQUEST_REFERENCE, value);
	}

	public String getCreditRequestReferenceNumber() {
		return (String) sessionManager.getFromUserMap(CREDIT_REQUEST_REFERENCE);
	}
}
