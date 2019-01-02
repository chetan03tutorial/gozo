package com.lbg.ib.api.sales.overdraft.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.SelectedAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AccountSessionInfoService {

    @Autowired
    private SessionManagementDAO session;
    @Autowired
    private AccountInContextUtility contextUtility;
    @Autowired
    private ApiServiceProperties apiServiceProperties;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private LoggerDAO logger;

    /**
     * Prepare userInfo if not present in session and update it
     * Prepare partyDetail if not present in session and update it
     * @return
     * @throws ServiceException
     */
    public SelectedAccountSessionInfo fetchSessionDetail() throws ServiceException {

        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        CustomerInfo primaryCustomerInfo = session.getCustomerDetails();

        if(session.getUserInfo() == null) {
            logger.traceLog(this.getClass(), "Preparing Arrangement and setting in session");
            session.setUserInfo(prepareArrangement());
        }
        // check all party detail in session
        if(session.getAllPartyDetailsSessionInfo() == null) {
            logger.traceLog(this.getClass(), "Preparing partyDetail and setting in session");
            session.setAllPartyDetailsSessionInfo(getAllPartyDetails());
        }
        if(session.getAccountToConvertInContext() == null && primaryCustomerInfo != null) {
            SelectedAccount selectedAccount = new SelectedAccount();
            selectedAccount.setAccountNumber(primaryCustomerInfo.getAccountNumber());
            selectedAccount.setSortCode(primaryCustomerInfo.getSortCode());
            logger.traceLog(this.getClass(), "setting selectedAccount as accountNumber: "+selectedAccount.getAccountNumber() +" and sortCode: "+ selectedAccount.getSortCode());
            session.setAccountToConvertInContext(selectedAccount);
        }
        if(arrangeToActivateParameters != null && arrangeToActivateParameters.getAmtOverdraft() != null) {
            contextUtility.setMaximumOverdraftLimit(arrangeToActivateParameters.getAmtOverdraft().doubleValue());
        }
        return prepareResponse(arrangeToActivateParameters, primaryCustomerInfo);
    }

    private SelectedAccountSessionInfo prepareResponse(ArrangeToActivateParameters arrangeToActivateParameters, CustomerInfo primaryCustomerInfo) {
        SelectedAccountSessionInfo response = new SelectedAccountSessionInfo();

        if(arrangeToActivateParameters != null) {
            response.setProductMnemonic(arrangeToActivateParameters.getProductMnemonic());
        }
        List<PartyDetails> allPartyDetails = new ArrayList<PartyDetails>(session.getAllPartyDetailsSessionInfo().values());
        response.setPartyDetails(allPartyDetails);
        if (contextUtility.getMaximumOverdraftLimit() != null) {
            response.setMaxOverDraftLimit(contextUtility.getMaximumOverdraftLimit());
        }
        if(session.getDemandedOD() != null) {
            response.setDemandedOD(session.getDemandedOD());
        } else if (contextUtility.getMaximumOverdraftLimit() != null){
            response.setDemandedOD(contextUtility.getMaximumOverdraftLimit());
        }
        //Setting environment
        Map<String, Object> configurationItems = apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES);
        if ((configurationItems != null) && (configurationItems.get(CommonConstant.ENVIRONMENT_NAME) != null)) {
            response.setEnvironmentName((String) configurationItems.get(CommonConstant.ENVIRONMENT_NAME));
        }
        //Setting account information
        response.setSelectedAccount(contextUtility.getSelectedAccountDetail());
        BranchContext context = session.getBranchContext();
        if (null != context) {
            response.setColleagueId(context.getColleagueId());
            response.setDomain(context.getDomain());
            response.setOriginatingSortCode(context.getOriginatingSortCode());
        }
        if(primaryCustomerInfo != null) {
            response.setUserName(primaryCustomerInfo.getUserName());
            response.setAppScoreReferenceNumber(primaryCustomerInfo.getArrangementId());
            response.setSwitchingDate(primaryCustomerInfo.getSwitchingDate());
            response.setConditions(primaryCustomerInfo.getConditions());
        }
        return response;
    }

    private Arrangement prepareArrangement() {

        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        CustomerInfo primaryCustomerInfo = session.getCustomerDetails();
        if(arrangeToActivateParameters == null || primaryCustomerInfo == null) {
            throw new ServiceException(new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE,
                    "Values not present in session"));
        }
        Arrangement arrangement = new Arrangement();
        List<Account> accounts = constructAccounts(arrangeToActivateParameters, primaryCustomerInfo);

        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setTitle(primaryCustomerInfo.getTitle());
        primaryInvolvedParty.setFirstName(primaryCustomerInfo.getForeName());
        primaryInvolvedParty.setLastName(primaryCustomerInfo.getSurName());
        primaryInvolvedParty.setEmail(primaryCustomerInfo.getEmail());
        if(primaryCustomerInfo.getDob() != null) {
            primaryInvolvedParty.setDob(String.valueOf(primaryCustomerInfo.getDob()));
        }
        primaryInvolvedParty.setCurrentAddress(primaryCustomerInfo.getCurrentAddress());
        logger.traceLog(this.getClass(), "primaryInvolvedParty value is: " + primaryInvolvedParty.toString());
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        arrangement.setAccounts(accounts);
        arrangement.setCbsCustomerNumber(primaryCustomerInfo.getCustomerNumber());
        if(session.getUserContext() != null) {
            arrangement.setOcisId(session.getUserContext().getOcisId());
            arrangement.setPartyId(session.getUserContext().getPartyId());
        } else {
            arrangement.setOcisId(arrangeToActivateParameters.getOcisId());
            arrangement.setPartyId(arrangeToActivateParameters.getPartyId());
        }
        return arrangement;
    }

    private List<Account> constructAccounts(ArrangeToActivateParameters arrangeToActivateParameters, CustomerInfo primaryCustomerInfo) {
        List<Account> accounts = Lists.newArrayList();
        Account account = new Account();
        account.setAccountName(arrangeToActivateParameters.getProductName());
        account.setAccountNickName(arrangeToActivateParameters.getProductName());
        account.setAccountNumber(primaryCustomerInfo.getAccountNumber());
        account.setSortCode(primaryCustomerInfo.getSortCode());
        account.setAccountStatus("OPEN");
        logger.traceLog(this.getClass(), "setting CbsProductNumber is: "+arrangeToActivateParameters.getCbsProductNumber());
        if(arrangeToActivateParameters.getCbsProductNumber() != null) {
            account.setAccountType("T".concat(arrangeToActivateParameters.getCbsProductNumber()));   // T1001306000
        }
        account.setAvailableBal(0.0);
        account.setCurrencyCode("GBP");
        account.setOverdraftLimit(0.0);
        logger.traceLog(this.getClass(), "setting ProductIdentifier is: "+arrangeToActivateParameters.getProductId());
        account.setProductIdentifier(arrangeToActivateParameters.getProductId());   // TODO: to check
        account.setProductType("C");
        account.setDisplayOrder(1);
        account.setExternalSystemId("4");
        accounts.add(account);
        return accounts;
    }

    private Map<String, PartyDetails> getAllPartyDetails() {
        Map<String, PartyDetails> allPartyDetails = Maps.newHashMap();

        CustomerInfo primaryCustomerInfo = session.getCustomerDetails();
        CustomerInfo relatedCustomerInfo = session.getRelatedCustomerDetails();
        Arrangement arrangement = session.getUserInfo();
        if(primaryCustomerInfo == null || arrangement == null) {
            throw new ServiceException(new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE,
                    "Values not present in session"));
        }
        PartyDetails primaryPartyDetail = getPartyDetails(primaryCustomerInfo);
        primaryPartyDetail.setIsPrimaryParty(true);
        // Check for joint party
        if(relatedCustomerInfo != null) {
            PartyDetails otherPartyDetail = getPartyDetails(relatedCustomerInfo);
            otherPartyDetail.setIsPrimaryParty(false);
            otherPartyDetail.setJoint(true);
            primaryPartyDetail.setJoint(true);
            allPartyDetails.put(relatedCustomerInfo.getOcisId(), otherPartyDetail);
            logger.traceLog(this.getClass(), "secondParty OcisId: " + relatedCustomerInfo.getOcisId());
        }
        logger.traceLog(this.getClass(), "primaryParty OcisId: " + arrangement.getOcisId());
        allPartyDetails.put(arrangement.getOcisId(),primaryPartyDetail);
        return allPartyDetails;
    }

    private PartyDetails getPartyDetails(CustomerInfo customerInfo) {
        PartyDetails partyDetail = new PartyDetails();
        partyDetail.setTitle(customerInfo.getTitle());
        partyDetail.setFirstName(customerInfo.getForeName());
        partyDetail.setSurname(customerInfo.getSurName());
        partyDetail.setEmail(customerInfo.getEmail());
        partyDetail.setCurrentAddress(customerInfo.getCurrentAddress());
        if(customerInfo.getDob() != null) {
            partyDetail.setDob(String.valueOf(customerInfo.getDob()));
        }
        String[] addressLines = commonUtils.getNormalizedAddressLines(customerInfo.getCurrentAddress());
        partyDetail.setAddressLines(addressLines);
        if (null != customerInfo.getCurrentAddress() && null != customerInfo.getCurrentAddress().getUnstructuredAddress()) {
            partyDetail.setPostalCode(customerInfo.getCurrentAddress().getUnstructuredAddress().getPostcode());
        } else if (null != customerInfo.getCurrentAddress() && null != customerInfo.getCurrentAddress().getStructuredAddress()) {
            partyDetail.setPostalCode(customerInfo.getCurrentAddress().getStructuredAddress().getPostcode());
        }
        return partyDetail;
    }

}