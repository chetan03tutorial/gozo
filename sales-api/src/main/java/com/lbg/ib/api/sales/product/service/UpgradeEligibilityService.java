package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND;
import static com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants.BRAND_CODE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.CFCNewLimitResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.helper.PartyHelper;
import com.lbg.ib.api.sales.party.service.CreditAssessmentQ250Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.shared.validator.business.user.UserInfoValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@Component
public class UpgradeEligibilityService {

	@Autowired
	private SearchPartyService searchPartyService;

	@Autowired
	private RetrievePartyDetailsService retrievePartyDetailsService;
	@Autowired
	private RetrieveProductFeaturesService retrieveProductFeaturesService;
	@Autowired
	private PcaDetermineEligibilityService pcaDetermineEligibilityService;
	@Autowired
	CreditAssessmentQ250Service creditAssessmentQ250Service;
	@Autowired
	private ConfigurationDAO configuration;
	@Autowired
	private LoggerDAO logger;

	@Autowired
	private GalaxyErrorCodeResolver resolver;
	@Autowired
	private SessionManagementDAO session;

	private static final String SYSTEM_CODE_TRIAD = "01005";
	private static final String SUCCESS_MSG = "Successfully fetched the upgrade eligibility details.";
	private static final String ACTIVE = "ACTIVE";

	/**
	 * This method checks eligibility of conversion
	 * 
	 * @param request
	 * @return
	 * @throws ServiceException
	 *             - If more than 2 parties found from B277 - If RPD doesn't
	 *             return EMAIL of Other party - If Q250 response FALSE
	 */
	@TraceLog
	public UpgradeEligibilityResponse upgradeEligibility(final UpgradeEligibilityRequest request)
			throws ServiceException {

		UpgradeEligibilityResponse response = null;
		String primaryPartyOcisId = null;
		Account requestedAccount = null;
		Boolean isSecondaryPartyEmailPresent = null;

		Arrangement arrangement = (Arrangement) session.getUserInfo();
		if (arrangement != null) {
			primaryPartyOcisId = arrangement.getOcisId();
			logger.traceLog(this.getClass(),
					"upgradeEligibility: primaryPartyOcisId from session is: " + primaryPartyOcisId);
			UserInfoValidator.validateOcisId(primaryPartyOcisId);
			requestedAccount = validateRequest(arrangement, request);
		}

		// Code to replace B277 with Q226 - starts
		Map<String, String> allPartiesOcisIdsMap = getAllPartiesOcisIds(arrangement, requestedAccount);

		populatePrimaryPartyOcisIDsInSession(arrangement.getOcisId(), allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));
		// setting the primary ocis id into session.
		arrangement.setOcisId(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));
		session.getUserContext().setOcisId(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY));

		logger.traceLog(this.getClass(), "upgradeEligibility: response from Q226 API: " + allPartiesOcisIdsMap);
		logger.traceLog(this.getClass(), "upgradeEligibility: total parties: " + allPartiesOcisIdsMap.size());
		// Code to replace B277 with Q226 - ends

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
		String brandCode = configuration.getConfigurationStringValue(BRAND_CODE, brandName);
		// Call Q250 API
		// replaced primaryocisId with primary ocisid from Q226 call obtained
		// earlier.
		String triadCode = getQ250Response(allPartiesOcisIdsMap.get(PartyHelper.ACCOUNT_TYPE_PRIMARY), brandCode);

		// Call RPC API based on Q250 response to fetch product features
		List<ProductFeatures> productFeatures = retrieveProductFeatures(triadCode, brandCode);
		if (productFeatures == null || productFeatures.isEmpty()) {
			logger.traceLog(this.getClass(),
					"upgradeEligibility: Empty product from RPC and throwing NO_ELIGIBLE_PRODUCT_FOUND exception");
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.NO_ELIGIBLE_PRODUCT_FOUND));
		}
		logger.traceLog(this.getClass(),
				"upgradeEligibility: size of product features returned from RPC: " + productFeatures.size());
		PcaDetermineEligibilityResponse eligibilityResponse = getUpgradeEligibilityCustomerInstruction(arrangement,
				request, requestedAccount, productFeatures, allPartiesDetails);
		if (eligibilityResponse != null) {
			setSelectedAccountInSession(request);
			response = new UpgradeEligibilityResponse();
			response.setMsg(SUCCESS_MSG);
			response.setProducts(eligibilityResponse.getProducts());
			response.setSecondaryPartyEmailPresent(isSecondaryPartyEmailPresent);
		}
		return response;
	}

	private void populatePrimaryPartyOcisIDsInSession(final String prefetchOcisId, final String q226OcisId) {
		Map<String, String> primaryOcisIdMap = new HashMap<String, String>();

		if (!prefetchOcisId.equalsIgnoreCase(q226OcisId)) {
			logger.traceLog(this.getClass(), "upgradeEligibility: prefetchOcisId >" + prefetchOcisId +
					"< doese not match with q226OcisId >" + q226OcisId + " !!!");
		} else {
			logger.traceLog(this.getClass(), "upgradeEligibility: prefetchOcisId >" + prefetchOcisId +
					"< matches with q226OcisId >" + q226OcisId + " !!!");
		}

		primaryOcisIdMap.put(PartyHelper.PREFETCH_OCIS_ID, prefetchOcisId);
		primaryOcisIdMap.put(PartyHelper.Q226_OCIS_ID, q226OcisId);

		session.setPrimaryPartyOcisIdMap(primaryOcisIdMap);
	}

	private Map<String, String> getAllPartiesOcisIds(final Arrangement arrangement, final Account account) {
		final Map<String, String> allPartiesOcisIds = new HashMap<String, String>();
		final String aggrementIdentifier = account.getSortCode() + account.getAccountNumber(); // sortCode
																								// +
																								// account
																								// number
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
			// if more than 1 parties returned then identify primary and joined
			// party and
			// put on map

			for (IBParties party : partiesRetrieved) {

				if (PartyHelper.isPartyMatchingWithPrimaryPartyFromSession(party,
						arrangement.getPrimaryInvolvedParty())) {
					allPartiesOcisIds.put(PartyHelper.ACCOUNT_TYPE_PRIMARY, party.getPartyId());
				} else {
					allPartiesOcisIds.put(PartyHelper.ACCOUNT_TYPE_JOINT, party.getPartyId());
				}

			}
		}

		// if no primary party found throw an exception.
		if (allPartiesOcisIds.size() == 0
				|| StringUtils.isEmpty(allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_PRIMARY))) {
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY));
		}
		return allPartiesOcisIds;
	}

	/**
	 * Validate request with accounts present in Arrangement
	 * 
	 * @param arrangement
	 * @param request
	 * @return
	 */
	private Account validateRequest(Arrangement arrangement, final UpgradeEligibilityRequest request) {
		for (Account account : arrangement.getAccounts()) {
			if (request.getAccountNumber().equalsIgnoreCase(account.getAccountNumber())
					&& request.getSortCode().equalsIgnoreCase(account.getSortCode())) {
				return account;
			}
		}
		logger.traceLog(this.getClass(), "validateRequest: Return exception for invalid request parameters");
		throw new ServiceException(resolver.resolve(ResponseErrorConstants.INVALID_ACCOUNT_NUMBER_SORT_CODE));
	}

	/**
	 * This method returns all (both primary and other party) parties details
	 * and set it in session too (to be used for sending email after conversion
	 * complete)
	 * 
	 * @param allPartiesOcisIds
	 * @param arrangement
	 * @return
	 */
	@TraceLog
	private Map<String, PartyDetails> retrieveAllPartiesDetails(Map<String, String> allPartiesOcisIds,
			Arrangement arrangement) {
		Map<String, PartyDetails> allPartiesDetailsMap = Maps.newHashMap();

		String primaryOcisID = allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_PRIMARY);
		logger.traceLog(this.getClass(), "retrieveAllPartiesDetails: primaryOcisID: " + primaryOcisID);
		allPartiesDetailsMap.put(primaryOcisID, PartyHelper.getPrimaryPartyDetails(allPartiesOcisIds, arrangement));

		String jointOcisID = allPartiesOcisIds.get(PartyHelper.ACCOUNT_TYPE_JOINT);
		logger.traceLog(this.getClass(), "retrieveAllPartiesDetails: jointOcisID: " + jointOcisID);

		if (null != jointOcisID && !"".equals(jointOcisID)) {
			logger.traceLog(this.getClass(),
					"retrieveAllPartiesDetails: Calling RPD to get other party detail for ocisId: " + jointOcisID);
			allPartiesDetailsMap.put(jointOcisID, getOtherPartyDetails(jointOcisID));
		}

		session.setAllPartyDetailsSessionInfo(allPartiesDetailsMap);
		return allPartiesDetailsMap;
	}

	/**
	 * This method call RetrievePartyDetails API to fetch emailId based on
	 * ocisId
	 * 
	 * @param otherpartyOcisId
	 * @return
	 * @throws ServiceException
	 *             - Email not found for any party
	 */
	private PartyDetails getOtherPartyDetails(String otherpartyOcisId) {
		PartyDetails jointParty = retrievePartyDetailsService.getPartyDetails(otherpartyOcisId);
    	jointParty.setIsPrimaryParty(false);
        return jointParty;
	}

	/**
	 * @param primaryPartyOcisId
	 * @param brandCode
	 * @return
	 */
	@TraceLog
	private String getQ250Response(String primaryPartyOcisId, String brandCode) {
		TriadRequestDTO triadRequestDTO = new TriadRequestDTO();
		triadRequestDTO.setOcisId(primaryPartyOcisId);
		final TriadCFCResultDTO triadCFCResultDTO = creditAssessmentQ250Service
				.retrieveCreditAssessmentData(triadRequestDTO);
		List<TriadResultDTO> triadResults = triadCFCResultDTO.getTriadResultDTOList();
		final List<CFCNewLimitResultDTO> cfcNewLimitResults = triadCFCResultDTO.getCfcNewLimitResultDTOS();
		String actionValue = null;
		if (triadCFCResultDTO == null || triadResults == null || triadResults.isEmpty()) {
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.ERROR_RESPONSE_FROM_Q250));
		}
		logger.traceLog(this.getClass(), "getQ250Response: response received from Q250: " + triadCFCResultDTO);
		for (TriadResultDTO triadDTO : triadResults) {
			if (triadDTO.getActionID().contains(brandCode)) {
				actionValue = triadDTO.getActionValue();
			}
			if (triadDTO.getActionID().equals("0105")) {
				session.setTriadDetails(triadDTO);

			}
		}
		if (cfcNewLimitResults != null) {
			for (CFCNewLimitResultDTO cfcNewLimitResultDTO : cfcNewLimitResults) {
				if (("+0106").equals(cfcNewLimitResultDTO.getCfcNewLimitId())) {
					logger.traceLog(this.getClass(),
							"Setting max overdraft amount to " + cfcNewLimitResultDTO.getCfcNewlimitValue());
					session.setMaxOverDraftLimit(cfcNewLimitResultDTO.getCfcNewlimitValue());
				}
			}
		}
		if (null == actionValue) {
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.ERROR_RESPONSE_FROM_Q250));
		} else {
			return actionValue;
		}
	}

	/**
	 * Call RPC to get product features
	 * 
	 * @return
	 */
	@TraceLog
	private List<ProductFeatures> retrieveProductFeatures(String triadCode, String brandCode) {
		RetrieveProductDTO retrieveProduct = null;
		if (("0").equalsIgnoreCase(triadCode)) {
			retrieveProduct = new RetrieveProductDTO(SYSTEM_CODE_TRIAD, brandCode + "000");
		} else {
			retrieveProduct = new RetrieveProductDTO(SYSTEM_CODE_TRIAD, brandCode + triadCode);
		}
		logger.traceLog(this.getClass(),
				"retrieveProductFeatures: Calling RPC with input:  " + retrieveProduct.toString());
		return retrieveProductFeaturesService.retrieveProductFeaturesByProductFamilyIdentifier(retrieveProduct);
	}

	/**
	 * Call DECI with - Input 1 - Remove the selected account before calling
	 * DECI - Input 2 - Product Mnemonics returned from RPC - Selected Account
	 * product Mnemonic
	 * 
	 * @param arrangement
	 * @param request
	 * @return
	 */
	@TraceLog
	private PcaDetermineEligibilityResponse getUpgradeEligibilityCustomerInstruction(Arrangement arrangement,
			UpgradeEligibilityRequest request, Account requestedAccount, List<ProductFeatures> productFeatures,
			Map<String, PartyDetails> allPartiesDetails) {

		logger.traceLog(this.getClass(), "getUpgradeEligibilityCustomerInstruction: Entering");
		PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();

		// DECI Input 1 - Remove the selected account before calling DECI
		List<Account> accounts = removeSelectedAccount(arrangement, request);
		determineEligibilityRequest.setArrangementType(request.getArrangementType());
		determineEligibilityRequest.setDob(arrangement.getPrimaryInvolvedParty().getDob());
		determineEligibilityRequest.setExistingCustomer(true);

		// DECI Input 2 - Remove selected account product
		removeSelectedAccountProductFeature(productFeatures, requestedAccount);

		List<String> mnemonics = Lists.newArrayList();
		for (ProductFeatures productFeature : productFeatures) {
			mnemonics.add(productFeature.getProduct().getMnemonic());
		}
		if (mnemonics.isEmpty()) {
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.NO_ELIGIBLE_PRODUCT_FOUND));
		}
		logger.traceLog(this.getClass(),
				"getUpgradeEligibilityCustomerInstruction: No. of accounts in DECI input: " + accounts.size());
		logger.traceLog(this.getClass(),
				"getUpgradeEligibilityCustomerInstruction: mnemonics in DECI input: " + mnemonics);
		PcaDetermineEligibilityResponse pcaDetermineEligibilityResponse = pcaDetermineEligibilityService
				.determineUpgradeEligibility(determineEligibilityRequest, arrangement, accounts, mnemonics);
		if (pcaDetermineEligibilityResponse != null) {
			logger.traceLog(this.getClass(), "getUpgradeEligibilityCustomerInstruction: Success response from DECI: "
					+ pcaDetermineEligibilityResponse);
			return parseDetermineEligibilityResponse(productFeatures, allPartiesDetails,
					pcaDetermineEligibilityResponse);
		}
		return null;
	}

	private void setSelectedAccountInSession(final UpgradeEligibilityRequest request) {
		SelectedAccount selectedAccount = new SelectedAccount();
		selectedAccount.setAccountNumber(request.getAccountNumber());
		selectedAccount.setSortCode(request.getSortCode());
		session.setAccountToConvertInContext(selectedAccount);
	}

	/**
	 * @param arrangement
	 * @param request
	 * @return
	 */
	private List<Account> removeSelectedAccount(Arrangement arrangement, final UpgradeEligibilityRequest request) {
		List<Account> accounts = new ArrayList<Account>(arrangement.getAccounts()); // To
																					// prevent
																					// modification
																					// in
																					// original
																					// Arrangement
		Iterator<Account> itr = accounts.iterator();
		while (itr.hasNext()) {
			Account account = itr.next();
			if (account.getAccountNumber().equalsIgnoreCase(request.getAccountNumber())
					&& account.getSortCode().equalsIgnoreCase(request.getSortCode())) {
				itr.remove();
			}
		}
		return accounts;
	}

	/**
	 * Remove the productFeature from list if selected account cbsProductId
	 * matches with externalProductIdentifier
	 * 
	 * @param productFeatures
	 * @param requestedAccount
	 */
	@TraceLog
	private void removeSelectedAccountProductFeature(List<ProductFeatures> productFeatures, Account requestedAccount) {

		String selectedAccountCbsProductId = requestedAccount.getAccountType().substring(1); // Remove
																								// first
																								// char
																								// as
																								// AccountType
																								// =
																								// T4342323239
		logger.traceLog(this.getClass(),
				"removeSelectedAccountProductFeature: selectedAccountCbsProductId: " + selectedAccountCbsProductId);
		Iterator<ProductFeatures> itr = productFeatures.iterator();
		while (itr.hasNext()) {
			ProductFeatures productFeature = itr.next();
			if (productFeature.getProduct().getExtProdIdentifiers() != null
					&& !productFeature.getProduct().getExtProdIdentifiers().isEmpty()) {

				List<String> extProdIdentifiers = productFeature.getProduct().getExtProdIdentifiers().get("00004");
				logger.traceLog(this.getClass(),
						"removeSelectedAccountProductFeature: extProdIdentifiers list: " + extProdIdentifiers);
				if (extProdIdentifiers != null && extProdIdentifiers.contains(selectedAccountCbsProductId)) {
					itr.remove();
				}
			}
		}
		if (productFeatures.isEmpty()) {
			throw new ServiceException(resolver.resolve(ResponseErrorConstants.NO_ELIGIBLE_PRODUCT_FOUND));
		}
	}

	private String getSecondPartyFullName(Map<String, PartyDetails> allPartiesDetails) {
		String secondPartyFullName = null;
		for (PartyDetails partyDetails : allPartiesDetails.values()) {
			if (!partyDetails.isPrimaryParty()) {
				secondPartyFullName = new StringBuilder().append(partyDetails.getTitle()).append(" ")
						.append(partyDetails.getFirstName()).append(" ").append(partyDetails.getSurname()).toString();
			}
		}
		return secondPartyFullName;
	}

	/**
	 * Prepare UpgradeEligibilityResponse from DECI response and save
	 * upgradeEligible options in session
	 * 
	 * @param productFeatures
	 * @param allPartiesDetails
	 * @param pcaDetermineEligibilityResponse
	 * @return
	 */
	@TraceLog
	private PcaDetermineEligibilityResponse parseDetermineEligibilityResponse(List<ProductFeatures> productFeatures,
			Map<String, PartyDetails> allPartiesDetails,
			PcaDetermineEligibilityResponse pcaDetermineEligibilityResponse) {
		boolean isJointParty = false;
		String secondPartyFullName = null;
		if (allPartiesDetails.size() > 1) {
			isJointParty = true;
			secondPartyFullName = getSecondPartyFullName(allPartiesDetails);
		}
		Map<String, UpgradeOption> availableUpgradeOptions = Maps.newHashMap();
		if (pcaDetermineEligibilityResponse.getProducts() != null) {
			for (EligibilityDetails eligibilityDetails : pcaDetermineEligibilityResponse.getProducts()) {
				String productMnemonic = eligibilityDetails.getMnemonic();
				logger.traceLog(this.getClass(),
						"parseDetermineEligibilityResponse: checking for productMnemonic: " + productMnemonic);
				for (ProductFeatures productFeature : productFeatures) {
					if (productMnemonic.equalsIgnoreCase(productFeature.getProduct().getMnemonic())) {
						String tariff = getTariff(isJointParty, secondPartyFullName, eligibilityDetails,
								productFeature);
						UpgradeOption upgradeOption = new UpgradeOption();
						upgradeOption.setMnemonic(productMnemonic);
						upgradeOption.setEligible(eligibilityDetails.getIsEligible());
						upgradeOption.setTariff(tariff);
						upgradeOption.setIsICOBFlag(eligibilityDetails.getIsICOBFlag());
						upgradeOption.setVantageEligible(eligibilityDetails.getIsVantageEligible());
						upgradeOption.setProductIdentifier(productFeature.getProduct().getIdentifier());
						upgradeOption
								.setCbsProductIds(productFeature.getProduct().getExtProdIdentifiers().get("00004"));
						availableUpgradeOptions.put(productMnemonic, upgradeOption);
					}
				}
			}
		}
		session.setAvailableUpgradeOptions(availableUpgradeOptions);
		return pcaDetermineEligibilityResponse;
	}

	/**
	 * Update Eligible flag based on ICOBS flag for joint party and return
	 * tariff
	 * 
	 * @param isJointParty
	 * @param secondPartyFullName
	 * @param eligibilityDetails
	 * @param productFeature
	 * @return
	 */
	private String getTariff(boolean isJointParty, String secondPartyFullName, EligibilityDetails eligibilityDetails,
			ProductFeatures productFeature) {
		String tariff = null;
		if (productFeature.getProduct().getProductAttributes() != null) {
			List<ProductAttributes> productAttributes = productFeature.getProduct().getProductAttributes();
			for (ProductAttributes productAttribute : productAttributes) {
				if (("isVantage").equalsIgnoreCase(productAttribute.getName())) {
					if (("Yes").equalsIgnoreCase(productAttribute.getValue())) {
						eligibilityDetails.setIsVantageEligible(true);
					} else {
						eligibilityDetails.setIsVantageEligible(false);
					}
				}
				// Set isEligible=FALSE in case of joint party and ICOBS=Y
				if (("ICOBS").equalsIgnoreCase(productAttribute.getName())
						&& ("Y").equalsIgnoreCase(productAttribute.getValue())) {
					eligibilityDetails.setIsICOBFlag(Boolean.TRUE);
					if (session.getBranchContext() == null && isJointParty && eligibilityDetails.getIsEligible()) {
						eligibilityDetails.setIsEligible(false);
						eligibilityDetails.setCode(ResponseErrorConstants.JOINT_PARTY_ICOBS_FLAG_TRUE);
						eligibilityDetails.setDesc(secondPartyFullName);
					}
				}
				if (("Tariff").equalsIgnoreCase(productAttribute.getName())) {
					tariff = productAttribute.getValue();
				}
			}
		}
		return tariff;
	}

	/*
	 * Method to get the account object for the session info call
	 */
	public Account getSelectedAccount() {
		if (validateSessionInfoForAccount()) {
			Arrangement arrangement = (Arrangement) session.getUserInfo();
			SelectedAccount selectedAccountDetails = session.getAccountToConvertInContext();
			for (Account account : arrangement.getAccounts()) {
				if (account.getAccountNumber().equalsIgnoreCase(selectedAccountDetails.getAccountNumber())
						&& account.getSortCode().equalsIgnoreCase(selectedAccountDetails.getSortCode())) {
					return account;
				}
			}
		}
		return null;
	}

	private boolean validateSessionInfoForAccount() {
		return ((session != null) && (session.getUserInfo() != null) && (session.getUserInfo() instanceof Arrangement)
				&& (session.getAccountToConvertInContext() != null));
	}

}
