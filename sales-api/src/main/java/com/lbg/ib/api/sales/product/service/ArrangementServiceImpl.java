/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.sales.product.domain.arrangement.ASMScoreType.findASMTypeFromCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lbg.ib.api.sales.common.service.BankHolidayService;
import com.lbg.ib.api.sales.product.domain.arrangement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.address.service.AddressService;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.auditing.Auditor;
import com.lbg.ib.api.sales.common.auditing.FraudAuditor;
import com.lbg.ib.api.sales.common.auditing.ThreatAuditor;
import com.lbg.ib.api.sales.common.constant.ErrorConstants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.party.register.PartyRegistrationDAO;
import com.lbg.ib.api.sales.dao.product.eligibility.EligibiltyDAO;
import com.lbg.ib.api.sales.dao.product.offer.OfferProductDAO;
import com.lbg.ib.api.sales.dao.product.offercrosssell.CrossSellEligibilityDAO;
import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lbg.ib.api.sales.dao.threatmatrix.ThreatMatrixDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.party.ChannelDTO;
import com.lbg.ib.api.sales.dto.party.PartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.PartyResponseDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.RelatedApplicationDTO;
import com.lbg.ib.api.sales.dto.product.offer.TaxResidencyDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.TinDetailsDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.utils.CommonUtils;

@Component
public class ArrangementServiceImpl implements ArrangementService {

    public static final String         APPLICATION_STATUS_DECLINED = "1004";

    private static final String        APPLICATION_STATUS_APPROVED = "1002";

    private static final String        APPLICATION_STATUS_REJECTED = "1003";

    private static final String        ASM_STATUS_CODE             = "601";

    private static final String        ASM_STATUS_ACCEPT_CODE      = "1";

    public static final String         IB_ERROR_CODE               = "IB_ERROR_CODE";

    private final boolean                    isIbMandateAvailable        = false;

    private final boolean                    isPwdState                  = false;

    private static final String        OVER_DRAFT_FLAG_VALUE       = "Y";

    private static final String        OVERDRAFT_KEY_VALUE         = "Overdraft Offer Flag";

    private static final String        RELATED_VANTAGE_MNEMONIC    = "Related Vantage Mnemonic";

    private static final String        RELATED_VANTAGE_PRODUCT     = "Related Vantage product";

    private static final String        PRODUCT_NOT_AVLBL_ERROR_MSG = "Selected product not available in session";

    private static final String        OFFER_RESP_NULL_ERROR_MSG   = "Offer response is null";

    private static final String        OFFER_ERROR_MSG             = "Error response from offer";

    private static final String        TRUE                        = "true";

    private static final String        STL                         = "STL";

    private static final String        STS                         = "STS";

    private static final String        REVOKED                     = "R";

    private Boolean                    isIA;

    private final OfferProductDAO            offerProductDAO;

    private final SessionManagementDAO       session;

    private final GalaxyErrorCodeResolver    resolver;

    private final LoggerDAO                  logger;

    private final EligibiltyDAO              customerEligibiltyDAO;

    private final OverdraftDAO               overdraftDAO;

    private final ChannelBrandingDAO         channelService;

    private final ConfigurationDAO           configurationService;

    private final CrossSellEligibilityDAO    crossSellEligibilityDAO;

    private final PartyRegistrationDAO       partyDAO;

    private final Auditor<ProductOfferedDTO> auditor;

    // AFDI2 Strategic changes starts
    private static final String        SIRA_SWITCH                 = "SW_EnSIRAFrdChk";

    private static final String        INTEREST_RATES              = "0000000";

    private final FraudAuditor               fraudAuditor;

    private final ThreatMatrixDAO            threatMatrixDAO;

    private final SwitchesManagementDAO      switchesDAO;

    private final CommonUtils                commonUtils;

    private final AddressService             addressService;
    // AFDI2 Strategic changes ends

    //PCA-6522

    private final BankHolidayService bankHolidayService;

    @Autowired
    public ArrangementServiceImpl(final OfferProductDAO offerProductDAO, final SessionManagementDAO session,
            final GalaxyErrorCodeResolver resolver, final LoggerDAO logger, final EligibiltyDAO customerEligibiltyDAO,
            final OverdraftDAO overdraftDAO, final ChannelBrandingDAO channelService, final ConfigurationDAO configurationService, final CrossSellEligibilityDAO crossSellEligibilityDAO,
            final PartyRegistrationDAO partyDAO, final Auditor<ProductOfferedDTO> auditor, final FraudAuditor fraudAuditor,
            final ThreatMatrixDAO threatMatrixDAO, final SwitchesManagementDAO switchesDAO, final CommonUtils commonUtils,
            final AddressService addressService, final BankHolidayService bankHolidayService) {
        this.offerProductDAO = offerProductDAO;
        this.session = session;
        this.resolver = resolver;
        this.logger = logger;
        this.customerEligibiltyDAO = customerEligibiltyDAO;
        this.overdraftDAO = overdraftDAO;
        this.channelService = channelService;
        this.configurationService = configurationService;
        this.crossSellEligibilityDAO = crossSellEligibilityDAO;
        this.partyDAO = partyDAO;
        this.auditor = auditor;
        this.fraudAuditor = fraudAuditor;
        this.threatMatrixDAO = threatMatrixDAO;
        this.switchesDAO = switchesDAO;
        this.commonUtils = commonUtils;
        this.addressService = addressService;
        this.bankHolidayService = bankHolidayService;
    }

    @TraceLog
    public Arranged arrange(final Arrangement arrangement) throws ServiceException {
        logger.traceLog(this.getClass(), "Inside the arrange.");
        DAOResponse<ProductOfferedDTO> offer;
        final SelectedProduct selectedProduct = session.getSelectedProduct();

        boolean vantageEligible = false;
        List<ProductAttributes> productAttributesList = null;
        OfferProductDTO offerReqDTO;
        // AFDI2 Strategic changes
        if (selectedProduct == null) {
            logger.logError(ResponseErrorConstants.BAD_REQUEST_PRODUCT_NOT_SELECTED, PRODUCT_NOT_AVLBL_ERROR_MSG,
                    ArrangementServiceImpl.class);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.BAD_REQUEST_PRODUCT_NOT_SELECTED));
        }
        // AFDI2 Strategic changes starts
        logger.traceLog(this.getClass(), "About to trigger the threat Matrix.");
        ThreatMatrixDTO threatMatrixDTO = null;

        if (arrangement.getAccountType().equals(AccountType.CA) && !commonUtils.isBranchContext()) {
            threatMatrixDTO = populateTMResults(ThreatAuditor.CURRENT_ACCOUNT_APPLICATION_TYPE);
        }

        // AFDI2 Strategic changes ends
        try {
            logger.traceLog(this.getClass(), "Set the Sira Switch Status.");
            offerReqDTO = populateOfferProductDTO(arrangement, selectedProduct, threatMatrixDTO);
            offerReqDTO.setSiraEnabledSwitch(commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session));
            // Setting the account Type as part of cross-sell implementation
            offerReqDTO.setAccType(arrangement.getAccountType().toString());
            offerReqDTO.setRelatedInvolvedParty(arrangement.getRelatedInvolvedParty());
            logger.traceLog(this.getClass(), "Offer Service Invocation.");
            offer = offerProductDAO.offer(offerReqDTO);
            logger.traceLog(this.getClass(), "After Offer Service Invocation.");
        } catch (final Exception e) {
            logger.logException(this.getClass(), e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR));
        }
        if (offer == null) {

            logger.logError(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR_OFFER_DOES_NOT_EXIST,
                    OFFER_RESP_NULL_ERROR_MSG, ArrangementServiceImpl.class);
            throw new ServiceException(
                    resolver.resolve(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR_OFFER_DOES_NOT_EXIST));
        }
        if (offer.getError() != null) {
            logger.logError(offer.getError().getErrorCode(), OFFER_ERROR_MSG, ArrangementServiceImpl.class);
            if (offer.getError().getErrorMessage() != null) {
                throw new ServiceException(
                        resolver.customResolve(offer.getError().getErrorCode(), offer.getError().getErrorMessage()));
            } else {
                throw new ServiceException(resolver.resolve(offer.getError().getErrorCode()));
            }

        }

        final ProductOfferedDTO result = offer.getResult();
        // Adding Customer details to session
        final CustomerInfo custInfo = getCustomerDetails(arrangement);
        if (custInfo != null) {
            custInfo.setArrangementId(result.arrangementId());
            session.setCustomerDetails(custInfo);
        }
        CustomerInfo relatedCustomerInfo = getRelatedCustomerDetails(arrangement);
        if(relatedCustomerInfo != null) {
            session.setRelatedCustomerDetails(relatedCustomerInfo);
        }

        // Adding this field to be used later.
        result.setCurrentYearOfStudy(arrangement.getPrimaryInvolvedParty().getCurrentYearOfStudy());
        // End of Adding this field to be used later.
        if (!commonUtils.isBranchContext()) {
            logger.traceLog(this.getClass(), "Auditor Service being Invoked.");
            auditor.audit(result);
        }
        // AFDI2 Strategic solution audit changes
        if (arrangement.getAccountType().equals(AccountType.CA)
                && commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session)) {
            result.getSiraScoreDTO().setSiraWorkFlowName(offerReqDTO.getSiraWorkFlowName());
            logger.traceLog(this.getClass(), "Fraud Auditor Service being Invoked.");
            fraudAuditor.audit(result);
        }

        final ProductArrangement product = arrangement.getProductArrangement();

        final ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementType(result.getArrangementType());
        arrangeToActivateParameters.setApplicationType(result.getApplicationType());
        arrangeToActivateParameters.setAmtOverdraft(result.getOverdraftAmount());
        arrangeToActivateParameters.setOcisId(result.customerIdentifier());
        arrangeToActivateParameters.setPartyId(result.cidPersID());
        arrangeToActivateParameters.setSortcode(result.getExistingProductSortCode());

        // Defect 2901 fix
        arrangeToActivateParameters.setProductId(result.getProductIdentifier());
        arrangeToActivateParameters.setProductName(result.productName());
        arrangeToActivateParameters.setProductMnemonic(result.mnemonic());
        arrangeToActivateParameters.setCbsProductNumberTrimmed(result.getcbsProductNumberTrimmed());
        arrangeToActivateParameters.setCbsProductNumber(result.getCbsProductNumber());
        arrangeToActivateParameters.setCurrentYearOfStudy(result.getCurrentYearOfStudy());
        arrangeToActivateParameters.setProductOptions(result.getProductOptions());

        arrangeToActivateParameters.setOfferedProductId(result.getProductIdentifier());
        arrangeToActivateParameters.setOfferedProductName(result.productName());
        arrangeToActivateParameters.setOfferedProductMnemonic(result.mnemonic());

        arrangeToActivateParameters.setProductFamilyIdentifier(result.getProductFamilyIdentifier());
        arrangeToActivateParameters.setAppStatus(result.applicationStatus());

        logger.traceLog(this.getClass(), "Setting ocis id in session.");
        // Setting ocis and party id in session
        setOcisPartyIdInSession(result);

        // Downsell changes starts - PCA
        logger.traceLog(this.getClass(), "Downsell Changes starts.");
        if (result.getOfferType() != null && result.getOfferType().equals(OfferType.DOWNSELL.toString())) {

            productAttributesList = new ArrayList<ProductAttributes>();
            for (final Map.Entry<String, String> entry : result.getProductOptions().entrySet()) {
                final ProductAttributes productAttributes = new ProductAttributes(entry.getKey(), entry.getValue());
                productAttributesList.add(productAttributes);
                logger.traceLog(this.getClass(), "Downsell Products" + productAttributes);
            }
        }

        logger.traceLog(this.getClass(), "Downsell Changes ends.");
        // Downsell changes ends - PCA

        final Arranged arranged = new Arranged(getFirst(result.productName(), product.getName()),
                getFirst(result.mnemonic(), product.getMnemonic()), result.applicationStatus(),
                result.applicationSubStatus(), result.arrangementId(), result.getOfferType(), productAttributesList,
                conditions(result.conditions()), marketingPreferences(result.getMarketingPreferences()));

        if (!isCrossSellRequest(arrangement) && !isApplnStatusDeclined(result)) {
            // B801 call
            logger.traceLog(this.getClass(), "Check Mandate B801.");
            checkMandateAvailability(arrangement, result, arranged);

            // Vantage Eligibility Changes Start - PCA
            if (session.getAddPartyContext() == null && isApplnStatusApproveOrRefer(result)) {
                vantageEligible = checkVantage(arrangement, result);
                arranged.setIsVantageEligible(vantageEligible);
            }
            logger.traceLog(this.getClass(), "End of Check Mandate B801.");
            // Vantage Eligibility Changes End - PCA

            // Set the Vantage Product Identifier if vantage is available- Start
            if (vantageEligible) {

                if (result != null && result.getProductOptions() != null && !result.getProductOptions().isEmpty()
                        && result.getProductOptions().containsKey(RELATED_VANTAGE_PRODUCT)) {
                    final String vantageProductIdentifier = result.getProductOptions().get(RELATED_VANTAGE_PRODUCT);
                    if (vantageProductIdentifier != null && vantageProductIdentifier.length() > 0) {
                        arrangeToActivateParameters.setVantagePrdIdentifier(vantageProductIdentifier);
                    }
                }
            }
            // Set the Vantage Product Identifier if vantage is available- End

            // fetching holiday list
            // PCA-162 Optimised it to fetch holidayMap only once
            //PCA-6522 changes to use common holiday service
            if(bankHolidayService != null){
                final Set<String> bankHolidays = bankHolidayService.getBankHolidaysList().getBankHolidays();
                if (offerReqDTO.getIntendSwitch() != null && offerReqDTO.getIntendSwitch() != SwitchOptions.No
                        && (bankHolidays!=null) && (!(bankHolidays.isEmpty()))) {
                    arranged.setBankHolidays(bankHolidays);
                }
            }
            //PCA-6522 changes to use common holiday service-- ENDS
            // End of PCA-162 Optimised it to fetch holidayMap only once
            // Overdraft API changes Starts. Code changed to change the asm
            // status accept code condition
            if (!isApplnStatusDeclined(result) && isOverdraftFlagStatus(result) && offerReqDTO.isIntendOverDraft() && isCallb274(arrangement)) {
                if (result.isOverdraftRequired() && result.getOverdraftAmount() != null
                        && result.getOverdraftAmount().intValue() > 0) {
                    logger.traceLog(this.getClass(), "retrieveOverdraftIntrestRates B274 Invocation.");
                    final DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274 = retrieveOverdraftIntrestRates(
                            result, null);
                    if (overdraftResponseFromBAPI274 != null && overdraftResponseFromBAPI274.getResult() != null) {
                        final OverdraftResponseDTO responseFromBAPI274 = overdraftResponseFromBAPI274.getResult();
                        Overdraft overdraftFeatures = new Overdraft();
                        overdraftFeatures = mappingIntrestRatesToOfferResponse(responseFromBAPI274, overdraftFeatures,
                                result, null);
                        arranged.setOverdraft(overdraftFeatures);
                        arrangeToActivateParameters.setOverdraft(overdraftFeatures);
                        arrangeToActivateParameters.setOverdraftIntrestRates(
                                mappingIntrestRatesToDomainObject(responseFromBAPI274, result, null));
                        session.setArrangeToActivateParameters(arrangeToActivateParameters);
                    } else if (overdraftResponseFromBAPI274 == null
                            || overdraftResponseFromBAPI274.getError() != null) {
                        arranged.setOverdraft(null);
                        arrangeToActivateParameters.setOverdraftIntrestRates(null);
                    }
                    logger.traceLog(this.getClass(), "End of retrieveOverdraftIntrestRates B274 Invocation.");
                }
            }

            //Code for fetching interest ratest for Add Party

            if (arrangement.getRelatedInvolvedParty()!=null && arrangement.getRelatedInvolvedParty().getOverDraftLimit()!=null && isCallb274(arrangement)){
                logger.traceLog(this.getClass(), "retrieveOverdraftIntrestRates B274 Invocation for Add Party.");
                final BigDecimal addPartyOverDraftAmount = new BigDecimal(arrangement.getRelatedInvolvedParty().getOverDraftLimit());
                final DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274 = retrieveOverdraftIntrestRates(
                        result,addPartyOverDraftAmount);
                if (overdraftResponseFromBAPI274 != null && overdraftResponseFromBAPI274.getResult() != null) {
                    final OverdraftResponseDTO responseFromBAPI274 = overdraftResponseFromBAPI274.getResult();
                    Overdraft overdraftFeatures = new Overdraft();
                    overdraftFeatures = mappingIntrestRatesToOfferResponse(responseFromBAPI274, overdraftFeatures,
                            result, addPartyOverDraftAmount);
                    arranged.setOverdraft(overdraftFeatures);
                    arrangeToActivateParameters.setOverdraftIntrestRates(
                            mappingIntrestRatesToDomainObject(responseFromBAPI274, result, addPartyOverDraftAmount));

                }
            }
            // End of Code for fetching interest ratest for Add Party
            // Cross Sell Eligibility Changes Starts
            if (!isApplnStatusDeclined(result)) {

                try {
                    // PCA-162 Commenting out as api is not working
                    logger.traceLog(this.getClass(), "retrievePromotionalCustomerInstructionsResponse Invocation.");
                    final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic = retrievePromotionalCustomerInstructionsResponse(
                            arrangement, result);
                    if (promotionalInstructionMnemonic != null) {
                        mapMnemonicIntoActivateSession(arrangeToActivateParameters, promotionalInstructionMnemonic);
                    }
                    logger.traceLog(this.getClass(),
                            "End of retrievePromotionalCustomerInstructionsResponse Invocation.");
                } catch (final Exception e) {
                    logger.logException(this.getClass(), e);
                    throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION));
                }
            }
            // Cross Sell Eligibility Changes ends
        }

        // Populate ASM & EIDV Score change- start
        if (result.eidvScoreDTO() != null && result.eidvScoreDTO().getScoreResult() != null) {
            final String scoreResult = result.eidvScoreDTO().getScoreResult();
            arranged.setEidvScore(scoreResult);
            arrangeToActivateParameters.setEidvStatus(scoreResult);
        }
        if (result.asmScoreDTO() != null && result.asmScoreDTO().getScoreResult() != null) {
            final String asmScore = findASMTypeFromCode(result.asmScoreDTO().getScoreResult()).toString();
            arrangeToActivateParameters.setAsmScore(asmScore);
            arranged.setAsmScore(asmScore);
        }
        // Populate ASM & EIDV Score change- end

        // Populate Decline Reasons Code & Description - Start
        if (result.getDeclineReason() != null) {
            arranged.setDeclineReason(populateDeclineReason(result.getDeclineReason()));
        }
        // Populate Decline Reasons Code & Description - End

        // Pipeline chasing Changes -- To set the status to false if the
        // application status is
        // declined, Refer or if offer type is false.
        // Code commented as it is redundant code
        /*
         * if (isApplnStatusDeclinedOrRejectedOrDownSell(result)) {
         * session.clearSessionAttributeForPipelineChasing(); }
         */
        arrangeToActivateParameters.setArrangementId(result.arrangementId());
        session.setArrangeToActivateParameters(arrangeToActivateParameters);

        arranged.setOcisId(result.customerIdentifier());
        arranged.setPartyId(result.cidPersID());
        arranged.setSiraScoreDTO(result.getSiraScoreDTO());
        return arranged;

    }

    private ThreatMatrixDTO populateTMResults(final String accountType) {
        ThreatMatrixDTO threatMatrixDTO = null;
        DAOResponse<Map<String, String>> tmResults = null;
        if (!commonUtils.isBranchContext()) {
            tmResults = threatMatrixDAO.retrieveThreadMatrixResults("", accountType, "");

        }

        threatMatrixDTO = populateTmxDTO(tmResults != null ? tmResults.getResult() : null);
        return threatMatrixDTO;
    }

    /**
     * Method to populate decline reasons error code & description
     *
     * @param declineReason
     *            ConditionDTO
     * @return Condition
     */
    public Condition populateDeclineReason(final ConditionDTO declineReason) {
        if (declineReason == null) {
            return null;
        }
        final Condition condition = new Condition();
        condition.setName(declineReason.getName());
        condition.setValue(declineReason.getValue());
        return condition;
    }

    private void checkMandateAvailability(final Arrangement arrangement, final ProductOfferedDTO result, final Arranged arranged) {
        final PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
        String postCode = null;
        if (primaryInvolvedParty.getCurrentAddress() != null
                && primaryInvolvedParty.getCurrentAddress().getStructuredAddress() != null) {
            postCode = primaryInvolvedParty.getCurrentAddress().getStructuredAddress().getPostcode();
        }
        final Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(primaryInvolvedParty.getDob());
        final PartyRequestDTO partyRequestDTO = new PartyRequestDTO(null, postCode, primaryInvolvedParty.getTitle(),
                primaryInvolvedParty.getFirstName(), primaryInvolvedParty.getLastName(), dateOfBirth, null,
                result.getArrangementType(), result.cidPersID());
        final DAOResponse<PartyResponseDTO> response = partyDAO.retrievePartyMandate(partyRequestDTO);
        if (response == null) {
            return;
        }
        final DAOError error = response.getError();
        if (error != null) {
            // Error response handling
            arranged.setIsIBMandate(handleErrorResponse(error));
        } else {
            // is already an IB registered customer
            arranged.setIsPwdActive(checkPwdState(response.getResult()));
            arranged.setIsIBMandate(true);
        }
    }

    public boolean checkPwdState(final PartyResponseDTO responseDTO) {
        final List<ChannelDTO> channelDetails = responseDTO.getChannels();
        if (channelDetails != null && !channelDetails.isEmpty()) {
            /*
             * Commenting out the code as it does not makes sense if
             * (channelDetails.contains(STL) || channelDetails.contains(STS)) {
             * boolean pwdstate = false; String passwordStatus =
             * responseDTO.getPwdState(); if (!(passwordStatus != null &&
             * REVOKED.equalsIgnoreCase(passwordStatus))) { pwdstate = true; }
             * return pwdstate; }
             */
            return false;
        }
        return false;
    }

    private boolean handleErrorResponse(final DAOError error) {
        boolean ibMandateAvailable = true;
        final String ibErrorCode = configurationService.getConfigurationStringValue(error.getErrorCode(), IB_ERROR_CODE);
        final ErrorConstants errorType = ErrorConstants.fromTypeCode(ibErrorCode);
        final List<ErrorConstants> ibMandateAvailableTrueErrorConstants = new ArrayList<ErrorConstants>();
        ibMandateAvailableTrueErrorConstants.add(ErrorConstants.EXCP_ERR_APP_NO_PARTIES);
        ibMandateAvailableTrueErrorConstants.add(ErrorConstants.EXCP_ERR_APP_NO_MATCHES);
        ibMandateAvailableTrueErrorConstants.add(ErrorConstants.EXCP_ERR_APP_INELIGIBLE);
        if (ibMandateAvailableTrueErrorConstants.contains(errorType)) {
            ibMandateAvailable = false;
        }
        return (ibMandateAvailable);
    }

    public void mapMnemonicIntoActivateSession(final ArrangeToActivateParameters arrangeToActivateParameters,
            final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic) throws ServiceException {
        if (promotionalInstructionMnemonic.getResult() != null) {
            arrangeToActivateParameters.setCrossSellMnemonicMap(promotionalInstructionMnemonic.getResult());
            session.setArrangeToActivateParameters(arrangeToActivateParameters);
        } else if (promotionalInstructionMnemonic != null && promotionalInstructionMnemonic.getError() != null) {
            arrangeToActivateParameters.setCrossSellMnemonicMap(null);
            session.setArrangeToActivateParameters(arrangeToActivateParameters);
        }
    }

    private DAOResponse<TreeMap<String, String>> retrievePromotionalCustomerInstructionsResponse(
            final Arrangement arrangement, final ProductOfferedDTO result) throws Exception {
        final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic = crossSellEligibilityDAO
                .determineCrossSellEligibilityForCustomer(
                        populateCustomerInstructionReqDTO(arrangement, result, result.mnemonic()));
        return promotionalInstructionMnemonic;
    }

    private Overdraft mappingIntrestRatesToOfferResponse(final OverdraftResponseDTO responseFromBAPI274,
            final Overdraft overdraftFeatures, final ProductOfferedDTO result,final BigDecimal addPartyOverDraftAmount) {
        overdraftFeatures.setAmount(result.getOverdraftAmount());
        if (addPartyOverDraftAmount != null) {
            overdraftFeatures.setAmount(addPartyOverDraftAmount);
        }
        overdraftFeatures.setIntFreeAmount(responseFromBAPI274.getAmtIntFreeOverdraft());
        overdraftFeatures.setUsageFee(responseFromBAPI274.getAmtUsageFeeOverdraft());
        overdraftFeatures.setIntEar(responseFromBAPI274.getIntrateAuthEAR());
        overdraftFeatures.setIntMnthEar(responseFromBAPI274.getIntrateAuthMnthly());
        overdraftFeatures.setAmtExcessFee(responseFromBAPI274.getAmtExcessFee());
        overdraftFeatures.setAmtExcessFeeBalIncr(responseFromBAPI274.getAmtExcessFeeBalIncr());
        overdraftFeatures.setAmtTotalCreditCost(responseFromBAPI274.getAmtTotalCreditCost());
        overdraftFeatures.setIntrateBase(responseFromBAPI274.getIntrateBase());
        overdraftFeatures.setIntrateMarginOBR(responseFromBAPI274.getIntrateMarginOBR());
        overdraftFeatures.setIntrateUnauthEAR(responseFromBAPI274.getIntrateUnauthEAR());
        overdraftFeatures.setIntrateUnauthMnthly(responseFromBAPI274.getIntrateUnauthMnthly());
        overdraftFeatures.setnExcessFeeCap(responseFromBAPI274.getNExcessFeeCap());
        return overdraftFeatures;
    }

    private OverdraftIntrestRates mappingIntrestRatesToDomainObject(final OverdraftResponseDTO responseFromBAPI274,
            final ProductOfferedDTO result,final BigDecimal addPartyOverDraftAmount) {
        final OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();
        overdraftIntrestRates.setAmtOverdraft(result.getOverdraftAmount());
        if (addPartyOverDraftAmount != null) {
            overdraftIntrestRates.setAmtOverdraft(addPartyOverDraftAmount);
        }
        overdraftIntrestRates.setAmtIntFreeOverdraft(responseFromBAPI274.getAmtIntFreeOverdraft());
        overdraftIntrestRates.setAmtUsageFeeOverdraft(responseFromBAPI274.getAmtUsageFeeOverdraft());
        overdraftIntrestRates.setIntrateAuthEAR(responseFromBAPI274.getIntrateAuthEAR());
        overdraftIntrestRates.setIntrateAuthMnthly(responseFromBAPI274.getIntrateAuthMnthly());
        overdraftIntrestRates.setAmtExcessFee(responseFromBAPI274.getAmtExcessFee());
        overdraftIntrestRates.setAmtExcessFeeBalIncr(responseFromBAPI274.getAmtExcessFeeBalIncr());
        overdraftIntrestRates.setAmtTotalCreditCost(responseFromBAPI274.getAmtTotalCreditCost());
        overdraftIntrestRates.setIntrateBase(responseFromBAPI274.getIntrateBase());
        overdraftIntrestRates.setIntrateMarginOBR(responseFromBAPI274.getIntrateMarginOBR());
        overdraftIntrestRates.setIntrateUnauthEAR(responseFromBAPI274.getIntrateUnauthEAR());
        overdraftIntrestRates.setIntrateUnauthMnthly(responseFromBAPI274.getIntrateUnauthMnthly());
        overdraftIntrestRates.setNExcessFeeCap(responseFromBAPI274.getNExcessFeeCap());
        return overdraftIntrestRates;
    }

    private DAOResponse<OverdraftResponseDTO> retrieveOverdraftIntrestRates(final ProductOfferedDTO result,final BigDecimal addPartyOverDraftAmount) {
        final OverdraftRequestDTO overdraftRequestDTO = mapOverdraftRequestAttributes(result, addPartyOverDraftAmount);
        DAOResponse<OverdraftResponseDTO> overdraftResponse = overdraftDAO.fetchOverdraftDetails(overdraftRequestDTO);
        if (overdraftResponse != null && overdraftResponse.getResult() != null) {
            if (overdraftResponse.getResult().getAmtIntFreeOverdraft().intValue() > 0
                    && INTEREST_RATES.equals(overdraftResponse.getResult().getIntrateAuthMnthly())
                    && INTEREST_RATES.equals(overdraftResponse.getResult().getIntrateAuthEAR())
                    && overdraftResponse.getResult().getAmtUsageFeeOverdraft().intValue() == 0) {
                overdraftRequestDTO.setAmtOverdraft(
                        overdraftResponse.getResult().getAmtIntFreeOverdraft().add(new BigDecimal(BigInteger.ONE)));
                overdraftResponse = overdraftDAO.fetchOverdraftDetails(overdraftRequestDTO);
            }
            return overdraftResponse;
        } else {
            logger.logError("800001", "Overdraft Response  error is : " + overdraftResponse.getError(),
                    this.getClass());
            return null;
        }
    }

    private OverdraftRequestDTO mapOverdraftRequestAttributes(final ProductOfferedDTO result,final BigDecimal addPartyOverDraftAmount) {
        final OverdraftRequestDTO overdraftRequestDTO = new OverdraftRequestDTO();
        overdraftRequestDTO.setUserContext(session.getUserContext());
        final DAOResponse<ChannelBrandDTO> channel = channelService.getChannelBrand();
        if (channel != null && channel.getError() == null) {
            final String contactPointId = generateSortCode(channel);
            if (contactPointId != null) {
                overdraftRequestDTO.setSortcode(contactPointId);
            }
        }
        // Default Behaviours
        overdraftRequestDTO.setAmtOverdraft(result.getOverdraftAmount());
        if (addPartyOverDraftAmount != null) {
            overdraftRequestDTO.setAmtOverdraft(addPartyOverDraftAmount);
        }
        overdraftRequestDTO.setCbsprodnum(new BigInteger(
                result.getcbsProductNumberTrimmed() == null ? "0" : result.getcbsProductNumberTrimmed()));

        // Adding Logic for Tariff fetching logic for OD
        String tariffKey = "Tariff";
        if ("P_STUDENT".equals(result.mnemonic())) {
            final BigInteger currentYearOfStudy = result.getCurrentYearOfStudy();
            tariffKey = ArrangementService.Tariffs
                    .getTariffKeyFromStudentYear(currentYearOfStudy != null ? currentYearOfStudy.intValue() : 0);

        }
        overdraftRequestDTO.setCbstariff(result.getProductOptions().get(tariffKey));

        if (addPartyOverDraftAmount != null) {
            fetchAddPartyTariff(overdraftRequestDTO,tariffKey);
        }

        return overdraftRequestDTO;
    }

    private void fetchAddPartyTariff(OverdraftRequestDTO overdraftRequestDTO,String tariffKey){
        SelectedProduct selectedProduct = session.getSelectedProduct();
        if (selectedProduct.getOptionsMap()!=null) {
            overdraftRequestDTO.setCbstariff(selectedProduct.getOptionsMap().get(tariffKey));
        }
    }

    private String generateSortCode(final DAOResponse<ChannelBrandDTO> channel) {
        final String contactPointId = configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID",
                channel.getResult().getChannelId());
        return contactPointId;
    }

    private List<Condition> conditions(final List<ConditionDTO> conditions) {
        List<Condition> list = null;
        if (conditions != null) {
            list = new ArrayList<Condition>();
            for (final ConditionDTO condition : conditions) {
                list.add(new Condition(condition.getName(), toInt(condition), condition.getValue()));
            }
        }
        return list;

    }

    private static List<MarketingPreference> marketingPreferences(final List<MarketingPreferenceDTO> marketingPreferenceDTOs) {
    final List<MarketingPreference> result = new ArrayList<MarketingPreference>();
    if (marketingPreferenceDTOs != null) {
        for (final MarketingPreferenceDTO marketingPreferenceDTO : marketingPreferenceDTOs) {
        final MarketingPreference marketingPreference = new MarketingPreference();
        marketingPreference.setEntitlementId(marketingPreferenceDTO.getEntitlementId());
        marketingPreference.setConsentOption(marketingPreferenceDTO.getConsentOption());
        result.add(marketingPreference);
        }
    }
    return result;
    }

    public Integer toInt(final ConditionDTO condition) {
        try {
            return Integer.parseInt(condition.getKey());
        } catch (final NumberFormatException e) {
            return null;
        }

    }

    private OfferProductDTO populateOfferProductDTO(final Arrangement arrangement, final SelectedProduct product,
            final ThreatMatrixDTO threatMatrixDTO) {
        final PrimaryInvolvedParty pip = arrangement.getPrimaryInvolvedParty();
        final ProductArrangement productArrangement = arrangement.getProductArrangement();

        // Cross-sell changes starts
        RelatedApplicationDTO relAppDTO = new RelatedApplicationDTO();

        // RelatedApplication attributes are expected in the input payload for a
        // cross-sell request
        final RelatedApplication relatedApplication = arrangement.getRelatedApplication();
        if (relatedApplication != null) {
            relAppDTO.setApplicationId(relatedApplication.getApplicationId());
            relAppDTO.setApplicationStatus(relatedApplication.getApplicationStatus());
        } else {
            relAppDTO = null;
        }
        // Cross-sell changes ends

        BigInteger numberOfDependents = null;
        if (pip.getNumberOfDependents() != null) {
            numberOfDependents = new BigInteger(pip.getNumberOfDependents().toString());
        }

        final List<MarketingPreferenceDTO> marketingPreferenceDTOs = populateMarketingPrecencesDTO(pip.getMarketingPreferences());

        final OfferProductDTO offerProductDTO = new OfferProductDTO(pip.getIntendSwitch(), pip.getIntendOverDraft(),
                pip.getIntendOdAmount(), pip.getTitle(), pip.getFirstName(), pip.getMiddleName(), pip.getLastName(),
                pip.getEmail(), commonUtils.phone(pip.getMobileNumber(), "Mobile"),
                commonUtils.phone(pip.getHomePhone(), "Fixed"), commonUtils.phone(pip.getWorkPhone(), "Work"),
                commonUtils.address(pip.getCurrentAddress(), "CURRENT",commonUtils.isAddPartyJourney(arrangement)),
                commonUtils.address(pip.getPreviousAddress(), "PREVIOUS",commonUtils.isAddPartyJourney(arrangement)), pip.getGender(), pip.getDob(),
                pip.getMaritalStatus(), numberOfDependents, employment(pip),
                new BigDecimal(commonUtils.defaultZero(pip.getIncome())), pip.getResidentialStatus(),
                pip.getUkArrivalDate(), pip.getVisaExpiryDate(), pip.getMarketPrefEmail(), pip.getMarketPrefPost(),
                pip.getMarketPrefPhone(), pip.getMarketPrefText(),
                pip.getRentMortgCost() != null ? new BigDecimal(pip.getRentMortgCost()) : null,
                pip.getMaintnCost() != null ? new BigDecimal(pip.getMaintnCost()) : null,
                pip.getSavingsAmount() != null ? new BigDecimal(pip.getSavingsAmount()) : null, pip.getFundSource(),
                pip.getPurpose(), product.getIdentifier(), options(productArrangement.getConditions()),
                extIdentifiers(product.getExternalProductIdentifiers()), productArrangement.getName(),
                productArrangement.getMnemonic(), relAppDTO, threatMatrixDTO, marketingPreferenceDTOs,pip.getOverDraftLimit());
        if (pip.getBirthCity() != null) {
            offerProductDTO.setBirthCity(pip.getBirthCity());
        }
        offerProductDTO.setPartyID(pip.getPartyId());
        TinDetailsDTO tinDetails = null;
        if (pip.getTinDetails() != null) {
            tinDetails = populateTinDetailsDTO(pip.getTinDetails());
            offerProductDTO.setTinDetails(tinDetails);
        }
        if (pip.getExptdMntlyDepAmt() != null) {
            offerProductDTO.setExptdMntlyDepAmt(pip.getExptdMntlyDepAmt());
        }

        offerProductDTO.setUcasCode(arrangement.getPrimaryInvolvedParty().getUcasCode());
        offerProductDTO
                .setAnticipateDateOfGraduation(arrangement.getPrimaryInvolvedParty().getAnticipateDateOfGraduation());
        offerProductDTO.setCurrentYearOfStudy(arrangement.getPrimaryInvolvedParty().getCurrentYearOfStudy());

        // check for authenticated request
        if (session.getUserInfo() != null && session.getAddPartyContext() == null) {
            final com.lbg.ib.api.sso.domain.user.Arrangement userArrangement = session.getUserInfo();
            final String ocisId = userArrangement.getOcisId();
            offerProductDTO.setOcisId(ocisId);
        } else if (session.getAddPartyContext() != null && session.getAddPartyContext().isExistingParty()) {
            offerProductDTO.setOcisId(pip.getOcisId());
        }
        offerProductDTO.setApplicantType(pip.getApplicantType());
        return offerProductDTO;
    }

    private TinDetailsDTO populateTinDetailsDTO(final TinDetails tinDetails) {
        final TinDetailsDTO tinDetailsDTO = new TinDetailsDTO();
        if (tinDetails.getBirthCountry() != null) {
            tinDetailsDTO.setBirthCountry(tinDetails.getBirthCountry());
        }
        if (tinDetails.getNationalities() != null && !tinDetails.getNationalities().isEmpty()) {
            tinDetailsDTO.setNationalities(tinDetails.getNationalities());
        }
        if (tinDetails.getTaxResidencies() != null && !tinDetails.getTaxResidencies().isEmpty()) {
            tinDetailsDTO.setTaxResidencies(populateTaxResidencyDetailsDTO(tinDetails.getTaxResidencies()));
        }

        return tinDetailsDTO;
    }

    private LinkedHashSet<TaxResidencyDetailsDTO> populateTaxResidencyDetailsDTO(
            final Set<TaxResidencyDetails> taxResidencies) {
        final LinkedHashSet<TaxResidencyDetailsDTO> taxResidencyDetailsDTOSet = new LinkedHashSet<TaxResidencyDetailsDTO>();
        TaxResidencyDetailsDTO taxResidencyDetailsDTO = null;
        TaxResidencyDetails taxDetails = null;
        for (final Iterator<TaxResidencyDetails> it = taxResidencies.iterator(); it.hasNext();) {
            taxDetails = it.next();
            taxResidencyDetailsDTO = new TaxResidencyDetailsDTO();
            if (taxDetails.getTaxResidency() != null) {
                taxResidencyDetailsDTO.setTaxResidency(taxDetails.getTaxResidency());
            }
            if (taxDetails.getTaxResidencyType() != null) {
                taxResidencyDetailsDTO.setTaxResidencyType(taxDetails.getTaxResidencyType());
            }
            if (taxDetails.getTinNumber() != null) {
                taxResidencyDetailsDTO.setTinNumber(taxDetails.getTinNumber());
            }
            taxResidencyDetailsDTOSet.add(taxResidencyDetailsDTO);
        }

        return taxResidencyDetailsDTOSet;
    }

    private Map<String, String> options(final List<ProductArrangementCondition> productAttributes) {
        if (productAttributes == null) {
            return null;
        }
        final Map<String, String> options = new HashMap<String, String>();
        for (final ProductArrangementCondition productAttribute : productAttributes) {
            options.put(productAttribute.getName(), productAttribute.getValue());
        }
        return options;
    }

    private Map<String, String> extIdentifiers(final ExternalProductIdentifier[] externalProductIdentifiers) {
        if (externalProductIdentifiers == null) {
            return null;
        }
        final Map<String, String> identifiers = new HashMap<String, String>();
        for (final ExternalProductIdentifier productAttribute : externalProductIdentifiers) {
            identifiers.put(productAttribute.getCode(), productAttribute.getId());
        }
        return identifiers;
    }

    private EmploymentDTO employment(final PrimaryInvolvedParty pip) {
        final EmployerDetails employer = pip.getEmployer();
        if (employer == null) {
            return new EmploymentDTO(pip.getOccupnType(), pip.getEmploymentStatus());
        } else {
            final Integer elapsedYear = employer.getElapsedYears();
            final Integer elapsedMonth = employer.getElapsedMonths();

            return new EmploymentDTO(pip.getOccupnType(), pip.getEmploymentStatus(), employer.getName(),
                    employer.getAddressLine1(), employer.getAddressLine2(), employer.getPostcode(),
                    (elapsedYear != null) ? (elapsedYear.toString()) : (null),
                    (elapsedMonth != null) ? (elapsedMonth.toString()) : (null));
        }
    }

    private <T> T getFirst(final T first, final T second) {
        return first == null ? second : first;
    }

    /**
     * Method to determine whether the application status is approved or
     * referred
     *
     * @param result
     * @return boolean
     */
    public boolean isApplnStatusApproveOrRefer(final ProductOfferedDTO result) {
        // 1002->approved, 1003-> referred
        return APPLICATION_STATUS_APPROVED.equalsIgnoreCase(result.applicationStatus())
                || APPLICATION_STATUS_REJECTED.equalsIgnoreCase(result.applicationStatus());
    }

    private boolean isApplnStatusDeclined(final ProductOfferedDTO result) {
        return APPLICATION_STATUS_DECLINED.equalsIgnoreCase(result.applicationStatus());
    }

    public boolean isApplnStatusDeclinedOrRejectedOrDownSell(final ProductOfferedDTO result) {
        return APPLICATION_STATUS_DECLINED.equalsIgnoreCase(result.applicationStatus())
                || APPLICATION_STATUS_REJECTED.equalsIgnoreCase(result.applicationStatus())
                || (OfferType.DOWNSELL.toString().equals(result.getOfferType()));
    }

    /**
     * Method to check vantage eligibility
     *
     * @param arrangement
     * @param result
     *
     */
    private boolean checkVantage(final Arrangement arrangement, final ProductOfferedDTO result) {
        String vantageElgIndicator = null;
        DAOResponse<HashMap<String, String>> response;
        try {
            // Current MPB implementation uses OptionsType"RelVatageMn" as the
            // key to retrieve the mnemonic.
            // Now retrieved with Optionsname since the name & type doesnt
            // differ for products. Have to be changed if required later.
            if (result != null && result.getProductOptions() != null && !result.getProductOptions().isEmpty()
                    && result.getProductOptions().containsKey(RELATED_VANTAGE_MNEMONIC)) {
                final String vantageMnemonic = result.getProductOptions().get(RELATED_VANTAGE_MNEMONIC);
                if (vantageMnemonic != null && vantageMnemonic.length() > 0) {
                    response = customerEligibiltyDAO.determineEligibleCustomerInstructions(
                            populateCustomerInstructionReqDTO(arrangement, result, vantageMnemonic));
                    if (response == null || response.getError() != null) {
                        return false;
                    }
                    if (response.getResult() != null && response.getResult().containsKey(vantageMnemonic)
                            && response.getResult().get(vantageMnemonic) != null) {
                        vantageElgIndicator = response.getResult().get(vantageMnemonic);
                        if (TRUE.equalsIgnoreCase(vantageElgIndicator)) {
                            // Set elig ind as true in offer rest service
                            // response
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;

        } catch (final Exception e) {
            logger.logException(this.getClass(), e);
            return false;
        }

    }

    /**
     * Method to populate EligibilityRequestDTO
     *
     * @param arrangement
     * @param result
     * @param mnemonic
     * @return EligibilityRequestDTO
     */
    private EligibilityRequestDTO populateCustomerInstructionReqDTO(final Arrangement arrangement, final ProductOfferedDTO result,
            final String mnemonic) {
        final PrimaryInvolvedParty pip = arrangement.getPrimaryInvolvedParty();
        final EligibilityRequestDTO eligibilityRequestDTO = new EligibilityRequestDTO(result.getArrangementType(),
                pip.getDob(), result.customerIdentifier(), result.existingProductArrangements(),
                new String[] { mnemonic }, result.getCbsProductNumber(),
                generateSortCode(channelService.getChannelBrand()));
        return eligibilityRequestDTO;
    }

    private boolean isOverdraftFlagStatus(final ProductOfferedDTO result) {
        return OVER_DRAFT_FLAG_VALUE.equalsIgnoreCase(result.getProductOptions().get(OVERDRAFT_KEY_VALUE));
    }

    private boolean isCrossSellRequest(final Arrangement arrangement) {
        boolean crossSellRequestFlag = false;
        if (arrangement.getRelatedApplication() != null
                && arrangement.getRelatedApplication().getApplicationId() != null) {
            crossSellRequestFlag = true;
        }
        return (crossSellRequestFlag);
    }

    private void setOcisPartyIdInSession(final ProductOfferedDTO responseDTO) {
        session.setOcisPartyIdInUserContext(responseDTO.customerIdentifier(), responseDTO.cidPersID());
    }

    public ThreatMatrixDTO populateTmxDTO(final Map<String, String> map) {
        if (map != null) {
            return new ThreatMatrixDTO(map);
        } else {
            return new ThreatMatrixDTO();
        }
    }

    public CustomerInfo getCustomerDetails(final Arrangement arrangement) {
        CustomerInfo customerInfo = null;
        if (arrangement != null && arrangement.getPrimaryInvolvedParty() != null) {
            customerInfo = new CustomerInfo();
            final PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
            customerInfo.setTitle(primaryInvolvedParty.getTitle());
            customerInfo.setForeName(primaryInvolvedParty.getFirstName());
            customerInfo.setSurName(primaryInvolvedParty.getLastName());
            customerInfo.setIntendSwitch(primaryInvolvedParty.getIntendSwitch());
            customerInfo.setIntendOverDraft(primaryInvolvedParty.getIntendOverDraft());
            customerInfo.setCurrentAddress(primaryInvolvedParty.getCurrentAddress());
            customerInfo.setMobileNumber(primaryInvolvedParty.getMobileNumber());
            customerInfo.setWorkPhone(primaryInvolvedParty.getWorkPhone());
            customerInfo.setHomePhone(primaryInvolvedParty.getHomePhone());
            customerInfo.setEmail(primaryInvolvedParty.getEmail());
            customerInfo.setDob(primaryInvolvedParty.getDob());
        }
        return customerInfo;
    }

    public CustomerInfo getRelatedCustomerDetails(final Arrangement arrangement) {
        CustomerInfo customerInfo = null;
        if (arrangement != null && arrangement.getRelatedInvolvedParty() != null) {
            customerInfo = new CustomerInfo();
            final RelatedInvolvedParty relatedInvolvedParty = arrangement.getRelatedInvolvedParty();
            customerInfo.setTitle(relatedInvolvedParty.getTitle());
            customerInfo.setForeName(relatedInvolvedParty.getFirstName());
            customerInfo.setSurName(relatedInvolvedParty.getLastName());
            customerInfo.setIntendSwitch(relatedInvolvedParty.getIntendSwitch());
            customerInfo.setIntendOverDraft(relatedInvolvedParty.getIntendOverDraft());
            customerInfo.setCurrentAddress(relatedInvolvedParty.getCurrentAddress());
            customerInfo.setMobileNumber(relatedInvolvedParty.getMobileNumber());
            customerInfo.setWorkPhone(relatedInvolvedParty.getWorkPhone());
            customerInfo.setHomePhone(relatedInvolvedParty.getHomePhone());
            customerInfo.setEmail(relatedInvolvedParty.getEmail());
            customerInfo.setOcisId(relatedInvolvedParty.getOcisId());
        }
        return customerInfo;
    }

    public CommonUtils getCommonUtils() {
        return commonUtils;
    }

    private static List<MarketingPreferenceDTO> populateMarketingPrecencesDTO(final List<MarketingPreference> marketingPreferences) {
    if (marketingPreferences == null) {
        return Collections.emptyList();
    }

    final List<MarketingPreferenceDTO> result = new ArrayList<MarketingPreferenceDTO>(marketingPreferences.size());
    for (final MarketingPreference marketingPreference : marketingPreferences) {
        result.add(new MarketingPreferenceDTO(marketingPreference.getEntitlementId(), marketingPreference.isConsentOption()));
    }
    return result;
    }
    
    /*
     * No Need to call B274 if flag is true
     */
    private boolean isCallb274(final Arrangement arrangement) {
        if(arrangement!=null && !arrangement.isSkipOverdraft()) {
            logger.traceLog(this.getClass(), ":::B274 will be called:::");
            return true;
        }
        logger.traceLog(this.getClass(), ":::B274 will not be called:::");
        return false;
    }
}
