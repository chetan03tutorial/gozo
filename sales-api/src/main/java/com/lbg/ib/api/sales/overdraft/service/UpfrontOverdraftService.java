package com.lbg.ib.api.sales.overdraft.service;

import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.CFCNewLimitResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.helper.PartyHelper;
import com.lbg.ib.api.sales.party.service.CreditAssessmentQ250Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftRequest;
import com.lbg.ib.api.sales.overdraft.domain.UpfrontOverdraftResponse;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.validator.business.user.UserInfoValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND;

@Component
public class UpfrontOverdraftService {

    @Autowired
    private SearchPartyService searchPartyService;

    @Autowired
    private RetrievePartyDetailsService retrievePartyDetailsService;
    @Autowired
    CreditAssessmentQ250Service creditAssessmentQ250Service;
    @Autowired
    private ApiServiceProperties apiServiceProperties;
    @Autowired
    private ConfigurationDAO configuration;
    @Autowired
    private LoggerDAO logger;

    @Autowired
    private GalaxyErrorCodeResolver resolver;
    @Autowired
    private SessionManagementDAO session;


    /**
     * This method checks upfront overdraft
     * @param request
     * @return
     * @throws ServiceException - If more than 2 parties found from B277 - If RPD doesn't return
     * EMAIL of Other party - If Q250 response FALSE
     */
    @TraceLog
    public UpfrontOverdraftResponse upfrontOverdraft(final UpfrontOverdraftRequest request) throws ServiceException {
        UpfrontOverdraftResponse response = new UpfrontOverdraftResponse();
        String primaryPartyOcisId = null;
        Account requestedAccount = null;
        Boolean isSecondaryPartyEmailPresent = null;

        Arrangement arrangement = (Arrangement) session.getUserInfo();
        if (arrangement != null) {
            primaryPartyOcisId = arrangement.getOcisId();
            logger.traceLog(this.getClass(),
                    "upfrontOverdraft: primaryPartyOcisId from session is: " + primaryPartyOcisId);
            UserInfoValidator.validateOcisId(primaryPartyOcisId);
            requestedAccount = validateRequest(arrangement, request);
        }

        
        setSelectedAccountInSession(request);
        
        // call Q226 - starts
        Map<String, String> allPartiesOcisIdsMap = getAllPartiesOcisIds(arrangement, requestedAccount);

        // setting the primary ocis id into session.
        arrangement.setOcisId(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));
        session.getUserContext().setOcisId(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));

        logger.traceLog(this.getClass(), "upfrontOverdraft: response from Q226 API: " + allPartiesOcisIdsMap);
        logger.traceLog(this.getClass(), "upfrontOverdraft: total parties: " + allPartiesOcisIdsMap.size());
        // call Q226 - ends

        // Call RPD API to fetch emailIds for all parties
        Map<String, PartyDetails> allPartiesDetails = retrieveAllPartiesDetails(allPartiesOcisIdsMap, arrangement);

        // Adding secondaryPartyEmailPresent indicator in eligibility service
        String jointOcisID = allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_JOINT);
        if (StringUtils.isNotEmpty(jointOcisID)) {
            logger.traceLog(this.getClass(), "Joint party scenario.Checking for secondary party email");
            final PartyDetails partyDetails = allPartiesDetails.get(jointOcisID);
            isSecondaryPartyEmailPresent = (StringUtils.isBlank(partyDetails.getEmail())) ? Boolean.FALSE
                    : Boolean.TRUE;
        }

        String brandName = configuration.getConfigurationStringValue(BRAND, session.getUserContext().getChannelId());
        // Call Q250 API
        String maxOverDraftAmount = getQ250Response(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));
        logger.traceLog(this.getClass(), "maxOverDraftAmount from Q250 response is: " + maxOverDraftAmount);

        populateUpfrontOverdraftResponse(response, arrangement, allPartiesDetails, brandName, maxOverDraftAmount);
        return response;
    }

    private void populateUpfrontOverdraftResponse(final UpfrontOverdraftResponse response, final Arrangement arrangement, final Map<String, PartyDetails> allPartiesDetails,
                                                  final String brandName, final String maxOverDraftAmount) {
        response.setMaxOverDraftLimit(maxOverDraftAmount);
        BranchContext context = session.getBranchContext();
        if (null != context) {
            response.setColleagueId(context.getColleagueId());
            response.setDomain(context.getDomain());
            response.setOriginatingSortCode(context.getOriginatingSortCode());
        }
        response.setSelectedAccount(getSelectedAccount());
        response.setPartyDetails(getPartyDetails(allPartiesDetails));
        Map<String, Object> configurationItems = apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES);
        if ((configurationItems != null) && (configurationItems.get(CommonConstant.ENVIRONMENT_NAME) != null)) {
            response.setEnvironmentName((String) configurationItems.get(CommonConstant.ENVIRONMENT_NAME));
        }
        Double totalOverDraftLimit = 0D;
        if (null != arrangement.getAccounts()) {
            for (Account account : arrangement.getAccounts()) {
                if (("C").equalsIgnoreCase(account.getProductType())
                        && account.getManufacturingLegalEntity().equalsIgnoreCase(brandName)
                        && account.getOverdraftLimit() != null) {
                    totalOverDraftLimit += account.getOverdraftLimit();
                }
            }
        }
        response.setTotalOverdraftLimit(totalOverDraftLimit);
    }

    private List<PartyDetails> getPartyDetails(Map<String, PartyDetails> allPartyDeatils) {
        List<PartyDetails> partyDetails = new ArrayList<PartyDetails>();
        for (Map.Entry<String, PartyDetails> entry : allPartyDeatils.entrySet()){
            partyDetails.add(entry.getValue());
        }
        return partyDetails;
    }
    private Map<String, String> getAllPartiesOcisIds(final Arrangement arrangement, final Account account)
    {
        final Map<String, String> allPartiesOcisIds = new HashMap<String, String>();
        final String aggrementIdentifier = account.getSortCode() + account.getAccountNumber(); // sortCode + account number
        final String agreementType = "CURRENT_ACCOUNT";
        SearchPartyRequest partyDetails = new SearchPartyRequest(aggrementIdentifier);
        partyDetails.setAgreementType(agreementType);
        final List<IBParties> partiesRetrieved = searchPartyService.retrieveParty(partyDetails);

        logger.traceLog(this.getClass(),
                "upgradeEligibility: getAllPartiesOcisIds: total parties: " + partiesRetrieved.size());
        // throws error if number of parties retrieved is greater than 2
        if (partiesRetrieved.size() > 2) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.MORE_THAN_TWO_PARTY_FOUND));
        }
        // if only party returned, return it as primary id
        else if (partiesRetrieved.size() == 1) {
            allPartiesOcisIds.put(PartyHelper.ACCOUNT_TYPE_PRIMARY, partiesRetrieved.get(0).getPartyId());

        } else if (partiesRetrieved.size() > 1) {
            // if more than 1 parties returned then identify primary and joined party and
            // put on map
            PartyHelper.populatePrimayAndJointParties(arrangement, allPartiesOcisIds, partiesRetrieved);
        }

        // if no primary party found throw an exception.
        if (allPartiesOcisIds.size() == 0 || StringUtils.isEmpty(allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_PRIMARY))) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY));
        }
        return allPartiesOcisIds;
    }

    /**
     * Validate request with accounts present in Arrangement
     * @param arrangement
     * @param request
     * @return
     */
    private Account validateRequest(Arrangement arrangement, final UpfrontOverdraftRequest request) {
        for (Account account : arrangement.getAccounts()) {
            if (request.getAccountNumber().equalsIgnoreCase(account.getAccountNumber()) &&
                    request.getSortCode().equalsIgnoreCase(account.getSortCode())) {
                return account;
            }
        }
        logger.traceLog(this.getClass(), "validateRequest: Return exception for invalid request parameters");
        throw new ServiceException(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE));
    }

    /**
     * This method returns all (both primary and other party) parties details and set it in session
     * too (to be used for sending email after conversion complete)
     * @param allPartiesOcisIds
     * @param arrangement
     * @return
     */
    @TraceLog
    private Map<String, PartyDetails> retrieveAllPartiesDetails(Map<String, String> allPartiesOcisIds, Arrangement arrangement) {
        Map<String, PartyDetails> allPartiesDetailsMap = Maps.newHashMap();

        String primaryOcisID = allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_PRIMARY);
        logger.traceLog(this.getClass(), "retrieveAllPartiesDetails: primaryOcisID: " + primaryOcisID);
        allPartiesDetailsMap.put(primaryOcisID, PartyHelper.getPrimaryPartyDetails(allPartiesOcisIds, arrangement));

        String jointOcisID = allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_JOINT);
        logger.traceLog(this.getClass(), "retrieveAllPartiesDetails: jointOcisID: " + jointOcisID);

        if(null != jointOcisID && !"".equals(jointOcisID))
        {
        	logger.traceLog(this.getClass(), "retrieveAllPartiesDetails: Calling RPD to get other party detail for ocisId: " + jointOcisID);
            allPartiesDetailsMap.put(jointOcisID, getOtherPartyDetails(jointOcisID));
        }

        session.setAllPartyDetailsSessionInfo(allPartiesDetailsMap);
        return allPartiesDetailsMap;
    }

    /**
     * This method call RetrievePartyDetails API to fetch emailId based on ocisId
     * @param otherpartyOcisId
     * @return
     * @throws ServiceException - Email not found for any party
     */
    private PartyDetails getOtherPartyDetails(String otherpartyOcisId) {
    	PartyDetails jointParty = retrievePartyDetailsService.getPartyDetails(otherpartyOcisId);
    	jointParty.setIsPrimaryParty(false);
        return jointParty;
    }

    /**
     * @param primaryPartyOcisId
     * @return
     */
    @TraceLog
    private String getQ250Response(String primaryPartyOcisId) {
        TriadRequestDTO triadRequestDTO = new TriadRequestDTO();
        triadRequestDTO.setOcisId(primaryPartyOcisId);
        final TriadCFCResultDTO triadCFCResultDTO = creditAssessmentQ250Service.retrieveCreditAssessmentData(triadRequestDTO);
        if (triadCFCResultDTO != null) {
            final List<CFCNewLimitResultDTO> cfcNewLimitResults = triadCFCResultDTO.getCfcNewLimitResultDTOS();
            if (cfcNewLimitResults != null) {
                for (CFCNewLimitResultDTO cfcNewLimitResultDTO : cfcNewLimitResults) {
                    if (("+0106").equals(cfcNewLimitResultDTO.getCfcNewLimitId())) {
                        logger.traceLog(this.getClass(), "Setting max overdraft amount to " + cfcNewLimitResultDTO.getCfcNewlimitValue());
                        session.setMaxOverDraftLimit(cfcNewLimitResultDTO.getCfcNewlimitValue());
                        return cfcNewLimitResultDTO.getCfcNewlimitValue();
                    }
                }
            }
        }
        return null;
    }

    private void setSelectedAccountInSession(final UpfrontOverdraftRequest request) {
        SelectedAccount selectedAccount = new SelectedAccount();
        selectedAccount.setAccountNumber(request.getAccountNumber());
        selectedAccount.setSortCode(request.getSortCode());
        session.setAccountToConvertInContext(selectedAccount);
    }

    /*
     * Method to get the account object for the session info call
     */
    public Account getSelectedAccount() {
        Arrangement arrangement = (Arrangement) session.getUserInfo();
        SelectedAccount selectedAccountDetails = session.getAccountToConvertInContext();
        for (Account account : arrangement.getAccounts()) {
            if (account.getAccountNumber().equalsIgnoreCase(selectedAccountDetails.getAccountNumber()) &&
                    account.getSortCode().equalsIgnoreCase(selectedAccountDetails.getSortCode())) {
                return account;
            }
        }
        return null;
    }
}
