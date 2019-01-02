package com.lbg.ib.api.sales.user.service;

import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND;
import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.ARRANGEMENT_ID_NOT_FOUND;
import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE;
import static com.lbg.ib.api.sales.switching.constants.AccountSwitchingConstants.CURRENT_ACCOUNT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;
import com.lbg.ib.api.shared.exception.ResponseError;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.kyc.FO75KYCReviewDAO;
import com.lbg.ib.api.sales.dao.product.holding.ProductHoldingDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ProductHoldingRequestDTO;
import com.lbg.ib.api.sales.product.domain.Product;
import com.lbg.ib.api.sales.product.domain.ProductHolding;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.sales.user.domain.UserDetails;
import com.lbg.ib.api.sales.utils.DecoderUtil;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
public class UserServiceImpl implements UserService {

    private SessionManagementDAO session;
    private LoggerDAO logger;
    private final ProductHoldingDAO productHoldingDAO;
    private ConfigurationDAO configuration;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String EVENTS_FOR_REMITTANCE = "EventsForRemittance";
    public static final String EVENTS_FOR_FUNDINGS = "EventsForFundings";
    public static final String INDICATORS_FOR_FUNDINGS = "IndicatorsForFundings";
    private final FO75KYCReviewDAO fO75KYCReviewDAO;
    public static final String KYC_DATE_FORMAT = "ddMMyyyy";
    public static final String EMPTY_NEXT_REVIEW_DATE = "Next review date is empty";
    private static final String END = "End";
    private static final String NUMBER_OF_PARTIES_ATTRIBUTE = "Number of Parties";
    private final DecoderUtil decoderUtil;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private ChannelBrandingDAO channelService;

    private RetrieveProductFeaturesService retrieveProductFeaturesService;

    private SearchPartyService searchPartyService;

    @Autowired
    public UserServiceImpl(LoggerDAO logger, SessionManagementDAO session, ProductHoldingDAO productHoldingDAO,
            ConfigurationDAO configuration, FO75KYCReviewDAO fO75KYCReviewDAO, DecoderUtil decoderUtil,
            RetrieveProductFeaturesService retrieveProductFeaturesService, SearchPartyService searchPartyService) {
        this.logger = logger;
        this.session = session;
        this.productHoldingDAO = productHoldingDAO;
        this.configuration = configuration;
        this.fO75KYCReviewDAO = fO75KYCReviewDAO;
        this.decoderUtil = decoderUtil;
        this.retrieveProductFeaturesService = retrieveProductFeaturesService;
        this.searchPartyService = searchPartyService;
    }

    @TraceLog
    public Arrangement fetchUserInfo(boolean isGoJoint) throws ServiceException {
        logger.traceLog(this.getClass(), "Fetching User Info and KYC Details for Current accounts.");

        Arrangement arrangement = (Arrangement) session.getUserInfo();
        if (null != arrangement) {
            logger.traceLog(this.getClass(), "Fetching KYC Details.");
            arrangement.setKycRefreshRequired(false);
            DAOResponse<String> response = fO75KYCReviewDAO.nextReviewDate(arrangement.getOcisId());
            if (null != response && null == response.getError()) {
                if (null != response.getResult() && !StringUtils.isEmpty(response.getResult().trim())) {
                    SimpleDateFormat formatter = new SimpleDateFormat(KYC_DATE_FORMAT);
                    try {
                        Date nextReviewDate = formatter.parse(response.getResult());
                        if (new Date().compareTo(nextReviewDate) > 0) {
                            logger.traceLog(this.getClass(), "Setting the KYC for Next Review Date.");
                            arrangement.setKycRefreshRequired(true);
                        }
                    } catch (ParseException e) {
                        logger.logException(this.getClass(), e);
                    }
                } else {
                    logger.logError(EMPTY_NEXT_REVIEW_DATE, EMPTY_NEXT_REVIEW_DATE, this.getClass());
                }
            }
            logger.traceLog(this.getClass(), "Successfully fetched KYC Details.");
        }
        if (null != arrangement && null != arrangement.getPrimaryInvolvedParty()
                && null != arrangement.getIbSessionId()) {

            logger.traceLog(this.getClass(),
                    String.format(
                            "Fetching Product Holding Information from Service having sessionId,partyId and ocisId as %s ,%s , %s",
                            arrangement.getIbSessionId(), arrangement.getPartyId(), arrangement.getOcisId()));
            DAOResponse<ProductHolding> productHolding = productHoldingDAO
                    .fetchProductHoldings(new ProductHoldingRequestDTO(arrangement.getIbSessionId(),
                            arrangement.getPartyId(), arrangement.getOcisId()));
            if (null != productHolding && null == productHolding.getError()) {
                checkOverDraft(arrangement, productHolding);
                arrangement = setArrangement(arrangement, productHolding.getResult());
                //Checks eligibility if the request is for Go Joint
                if(isGoJoint) {
                    boolean isError = checkGoJointEligibility(arrangement);
                    arrangement.setIsErrorInGoJointEligibilityCheck(isError);
                }
                session.setUserInfo(arrangement);
                // Flusing User Data
                // session.setUserInfo(null);
                // End of Flusing User Data
                logger.traceLog(this.getClass(),
                        "Fetching Product Holding Information from Service and setting the OverDraft flag "
                                + arrangement);
                return arrangement;
            }
        } else {
            return null;
        }

        logger.traceLog(this.getClass(), "End of Fetching User Info from Service");
        arrangement.setAccountsFetchedStatus(false);
        return arrangement;
    }

    /**
     * Checks the go joint eligibility for the accounts
     * @param arrangement
     * @return isError - returns true if there is any error while calling the RPC or Q226
     */
    private boolean checkGoJointEligibility(Arrangement arrangement) {
        boolean isError = false;
        if (arrangement != null) {
            List<Account> accountList = arrangement.getAccounts();
            if (CollectionUtils.isNotEmpty(accountList)) {
                logger.traceLog(this.getClass(), "Number of accounts for the party " + arrangement.getPartyId() + "are - " + accountList.size());
                isError = checkAndUpdateEligibilityForAccounts(accountList);
            }
        }
        return isError;
    }

    /**
     * Checks and updates the go joint eligibility flag for the accounts
     * @param accountList
     * @return isError - returns true if there is any error while calling the RPC or Q226
     */
    private boolean checkAndUpdateEligibilityForAccounts(List<Account> accountList) {
        boolean isEligible;
        boolean isError = false;
        List<Account> filteredAccountList = filterAccountsBasedOnBrand("C", accountList);
        for (Account account : filteredAccountList) {
            String productId = account.getProductIdentifier();
            Integer numberOfAllowedParties = 0;
            Integer numberOfParties = 0;
            try {
                numberOfAllowedParties = getAllowedNumberOfPartiesForTheProduct(productId);
                logger.traceLog(this.getClass(), "Number of parties allowed in the product - " + numberOfAllowedParties);

                // Get number of parties from the Q226
                numberOfParties = getNumberOfPartiesForTheAccount(account.getSortCode(), account.getAccountNumber());
                logger.traceLog(this.getClass(), "Number of parties returned for the account - "
                        + account.getSortCode() + account.getAccountNumber() + " are - " + numberOfParties);
                // check if the account is eligible for go joint
                isEligible = checkNumberOfPartiesEligibility(numberOfAllowedParties, numberOfParties);
                account.setEligibleForGoJoint(isEligible);
            } catch (Exception e) {
                logger.logException(this.getClass(), e);
                isError = true;
            }
            if (isError) {
                break;
            }
        }
        return isError;
    }

    /**
     * Retrieves the features for a product from RPC and then checks for the newly added feature 'Number of Parites'
     * @param productId
     * @return numberOfPartiesAllowed
     */
    private Integer getAllowedNumberOfPartiesForTheProduct(String productId) {
        logger.traceLog(this.getClass(), "Get the product features for the product id - " + productId);

        ProductFeatures productFeatures = retrieveProductFeaturesService.retrieveProductFeaturesByProductIdNoSessionUpdate(
                    new ProductReference(productId));
        Integer numberOfPartiesAllowed = 0;
        if (productFeatures != null && productFeatures.getProduct() != null) {
            List<ProductAttributes> productAttributesList = productFeatures.getProduct().getProductAttributes();
            if (CollectionUtils.isNotEmpty(productAttributesList)) {
                for (ProductAttributes productAttributes : productAttributesList) {
                    if (NUMBER_OF_PARTIES_ATTRIBUTE.equals(productAttributes.getName())) {
                        numberOfPartiesAllowed = Integer.valueOf(productAttributes.getValue());
                        break;
                    }
                }
            }
        }
        return  numberOfPartiesAllowed;
    }

    /**
     * Uses Q226 API to get the number of parties for an account
     * @param sortCode
     * @param accountNumber
     * @return number Of Parties associated with the account
     */
    private Integer getNumberOfPartiesForTheAccount(String sortCode, String accountNumber) {

        final SearchPartyRequest partyDetails = new SearchPartyRequest(sortCode + accountNumber);
        partyDetails.setAgreementType(CURRENT_ACCOUNT);

        List<IBParties> accountPartyList = searchPartyService.retrieveParty(partyDetails);

        return  (accountPartyList != null ? accountPartyList.size() : 0);
    }

    /**
     * Checks if the numberOfPartiesInAccount are greater than or equal to allowedNumberOfParties
     * @param allowedNumberOfParties
     * @param numberOfPartiesInAccount
     * @return isEligible
     */
    private boolean checkNumberOfPartiesEligibility(Integer allowedNumberOfParties, Integer numberOfPartiesInAccount) {
        boolean isEligible = true;
        if (numberOfPartiesInAccount >= allowedNumberOfParties) {
            isEligible= false;
        }
        return isEligible;
    }

    private void checkOverDraft(Arrangement arrangement, DAOResponse<ProductHolding> productHolding) {
        List<Product> products = productHolding.getResult().getProducts();
        for (Product product : products) {
            if (null != product.getOverdraftLimit() && product.getOverdraftLimit() > 0) {
                arrangement.setMultiOverDraft(false);
                break;
            }
        }
    }

    private Arrangement setArrangement(Arrangement arrangement, ProductHolding productHolding) {
        List<Account> accounts = new ArrayList<Account>();
        List<Product> accountsData = productHolding.getProducts();
        if (null != accountsData) {
            for (Product account : accountsData) {
                Account newAccount = new Account();
                newAccount.setAccountName(account.getAccountName());
                newAccount.setAccountCategory(account.getAccountCategory());
                newAccount.setAccountNumber(account.getAccountNumber());
                newAccount.setAccountNickName(account.getAccountNickName());
                newAccount.setSortCode(account.getSortCode());
                newAccount.setAccountStatus(account.getAccountStatus());
                newAccount.setAvailableBal(account.getAvailableBal());
                newAccount.setLedgerBal(account.getLedgerBal());
                newAccount.setCurrencyCode(account.getCurrencyCode());
                newAccount.setProductType(account.getProductType());
                newAccount.setDisplayOrder(account.getDisplayOrder());
                newAccount.setAccountStatus(account.getAccountStatus());
                newAccount.setEligibleForRemittance(checkRemittanceEligibility(account));
                newAccount.setEligibleForFunding(checkFundingEligibility(account));
                newAccount.setEvents(account.getEvents());
                newAccount.setIndicators(account.getIndicators());
                newAccount.setProductIdentifier(account.getProductIdentifier());
                newAccount.setLifecyclestatus(account.getLifecyclestatus());
                newAccount.setExternalSystemId(account.getExternalSystemId());
                newAccount.setOverdraftLimit(account.getOverdraftLimit());
                newAccount.setAccountType(account.getAccountType());
                newAccount.setEligibleForGoJoint(true);
                String strdate = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                if (account.getAccountOpenedDate() != null) {
                    strdate = dateFormat.format(account.getAccountOpenedDate().getTime());
                }
                newAccount.setAccountOpenedDate(strdate);
                if (newAccount.getEligibleForFunding() || newAccount.getEligibleForRemittance()) {
                    accounts.add(newAccount);
                }

                newAccount.setManufacturingLegalEntity(account.getManufacturingLegalEntity());
                newAccount.setSellingLegalEntity(account.getSellingLegalEntity());
            }
        }
        arrangement.setAccounts(accounts);

        //set the cbsCustomerNumber
        arrangement.setCbsCustomerNumber(productHolding.getCbsCustomerNumber());

        // Setting KYC Refresh Required.
        if (!arrangement.isKycRefreshRequired()) {
            arrangement.setKycRefreshRequired(productHolding.getKycRefreshRequired());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String strdate = null;
        if (productHolding.getLastLoginDate() != null) {
            strdate = dateFormat.format(productHolding.getLastLoginDate().getTime());
        }
        arrangement.setLastLoggedInTime(strdate);
        arrangement.setInternalUserIdentifier(productHolding.getInternalUserIdentifier());
        arrangement.setAccountsFetchedStatus(true);
        arrangement.setCategories(productHolding.getCategories());
        return arrangement;
    }

    public Boolean checkRemittanceEligibility(Product account) {
        if (null != account.getDormant() && !account.getDormant() && null != account.getEvents()
                && !account.getEvents().isEmpty()) {
            for (Event event : account.getEvents()) {
                Map<String, Object> remittanceEvents = configuration.getConfigurationItems(EVENTS_FOR_REMITTANCE);
                if (null != remittanceEvents && null != event.getEventId()
                        && remittanceEvents.containsValue(event.getEventId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean checkFundingEligibility(Product account) {
        if (checkAccount(account)) {
            for (Event event : account.getEvents()) {
                Map<String, Object> fundingEvents = configuration.getConfigurationItems(EVENTS_FOR_FUNDINGS);
                if (null != fundingEvents && null != event.getEventId()
                        && fundingEvents.containsValue(event.getEventId()) && !checkSignatureCheckIndicator(account)) {
                    return true;
                }
            }
        }
        return false;
    }

    //SONAR fix
    private boolean checkAccount(Product account) {
        return (null != account.getDormant() && !account.getDormant() && null != account.getEvents()
        && !account.getEvents().isEmpty());
    }

    public Boolean checkSignatureCheckIndicator(Product account) {
        if (null != account.getIndicators() && !account.getIndicators().isEmpty()) {
            for (Indicator indicator : account.getIndicators()) {
                Map<String, Object> fundingIndicators = configuration.getConfigurationItems(INDICATORS_FOR_FUNDINGS);
                if (null != fundingIndicators && null != indicator.getIndicatorCode()
                        && fundingIndicators.containsValue(indicator.getIndicatorCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.sales.user.service.UserService#fetchUserInfo(boolean,
     * com.lbg.ib.api.sales.user.domain.UserInfoRequest)
     */
    public UserDetails fetchUserInfo(String accountType, String encryptedAccount, boolean isGoJoint) throws ServiceException {
        String methodName = "fetchUserInfo";
        logger.traceLog(this.getClass(), methodName + "accountType:" + accountType + "encryptedAccount:" + encryptedAccount);
        UserDetails userDetails = new UserDetails();
        Arrangement arrangement = session.getUserInfo();
        if (!(null != arrangement && null != arrangement.getAccounts())) {
            arrangement = fetchUserInfo(isGoJoint);
        }
        SelectedAccount selectedAccount = new SelectedAccount();
        if (null != encryptedAccount && !encryptedAccount.isEmpty()) {
            selectedAccount.setAccountNumber((decoderUtil.getDecryptedParam(encryptedAccount).split("\\|\\|")[1]));
            selectedAccount.setSortCode((decoderUtil.getDecryptedParam(encryptedAccount).split("\\|\\|")[2]));
            userDetails.setSelectedAccount(selectedAccount);
        }
        if (null != arrangement && null != arrangement.getAccounts()) {
            UserContext context = session.getUserContext();
            userDetails.setAccounts(extractAccountsByType(accountType, arrangement, selectedAccount));
            userDetails.setAccountsFetchedStatus(arrangement.isAccountsFetchedStatus());
            userDetails.setCategories(arrangement.getCategories());
            userDetails.setKycRefreshRequired(arrangement.isKycRefreshRequired());
            userDetails.setLastLoggedInTime(arrangement.getLastLoggedInTime());
            userDetails.setMultiOverDraft(arrangement.isMultiOverDraft());
            userDetails.setPrimaryInvolvedParty(arrangement.getPrimaryInvolvedParty());
            userDetails.setIsErrorInGoJointEligibilityCheck(arrangement.getIsErrorInGoJointEligibilityCheck());
            context.setOcisId(arrangement.getOcisId());
            context.setPartyId(arrangement.getPartyId());
        } else {
            throw new ServiceException(resolver.resolve(ARRANGEMENT_ID_NOT_FOUND));
        }

        logger.traceLog(this.getClass(), methodName + END);
        return userDetails;
    }

    /**
     * Filter conversion products.
     * @param accountType String
     * @param arrangement Arrangement
     * @param selectedAccount SelectedAccount
     * @return
     */
    private List<Account> extractAccountsByType(String accountType, Arrangement arrangement, SelectedAccount selectedAccount) {
        List<Account> filteredAccount = new ArrayList<Account>();
        String brandName = configuration.getConfigurationStringValue(BRAND, session.getUserContext().getChannelId());
        boolean isError = Boolean.TRUE;
        for (Account account : arrangement.getAccounts()) {
            if (null != accountType && account.getProductType().equalsIgnoreCase(accountType) && account.getManufacturingLegalEntity().equalsIgnoreCase(brandName)) {
                if (null != selectedAccount.getAccountNumber() && (selectedAccount.getAccountNumber().equals(account.getAccountNumber()) && selectedAccount.getSortCode().equals(account.getSortCode()))) {
                    isError = Boolean.FALSE;
                }
                filteredAccount.add(account);
            }
        }
        if (isError && null != selectedAccount.getAccountNumber()) {
            throw new ServiceException(resolver.resolve(INVALID_ACCOUNT_NUMBER_SORT_CODE));
        }
        return filteredAccount;
    }

    private List<Account> filterAccountsBasedOnBrand(String accountType, List<Account> accounts) {
        List<Account> filteredAccount = new ArrayList<Account>();
        String brandName = configuration.getConfigurationStringValue(BRAND, session.getUserContext().getChannelId());
        boolean isError = Boolean.TRUE;
        for (Account account : accounts) {
            if (null != accountType && account.getProductType().equalsIgnoreCase(accountType) && account.getManufacturingLegalEntity().equalsIgnoreCase(brandName)) {
                filteredAccount.add(account);
            }
        }
        return filteredAccount;
    }
}
