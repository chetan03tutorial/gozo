/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.offer;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.product.service.ArrangementServiceImpl.APPLICATION_STATUS_DECLINED;
import static org.apache.commons.lang.ArrayUtils.isNotEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.dataextractors.FirstProductExtractor;
import com.lbg.ib.api.sales.dao.dataextractors.ProductExtractor;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;
import com.lbg.ib.api.sales.dto.product.offer.ASMScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.EIDVScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AssessmentEvidence;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDecision;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.MarketingPreference;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReferralCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;
import com.lbg.ib.api.sales.soapapis.offerproduct.arrangement.IA_OfferProductArrangement;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementResponse;
import com.lloydstsb.ea.config.ConfigurationService;

@Component
public class OfferProductDAOImpl implements OfferProductDAO {

    private static final Class<OfferProductDAOImpl> CLASS_NAME                 = OfferProductDAOImpl.class;

    private static final String                     METHOD_NAME                = "offer";

    private static final String                     ASM                        = "ASM";

    private static final String                     EIDV                       = "EIDV";

    private static final String                     SIRA                       = "SIRA";

    // private static final String CBS_PRODUCT_SYSTEM_CODE = "00004";

    public static final String                      REFERAL_TYPE_BANK          = "Bank";

    public static final String                      REFERAL_TYPE_BUREAU        = "Bureau";

    public static final String                      EIDV_REFERRAL_CODE         = "003";

    public static final String                      REFERAL_CODE_MAPPING       = "currentAccountReferalCodesMapping";

    public static final String                      DECLINE                    = "DECLINE";

    public static final String                      BUREAU_DECLINE_ERROR_CODE  = "51913";

    public static final String                      BANK_DECLINE_ERROR_CODE    = "51914";

    public static final String                      EIDV_DECLINE_ERROR_CODE    = "51915";

    public static final String                      GENERAL_DECLINE_ERROR_CODE = "51916";

    public static final String                      BUREAU_DECLINE_ERROR_DESC  = "Bureau Decline";

    public static final String                      BANK_DECLINE_ERROR_DESC    = "Bank Decline";

    public static final String                      EIDV_DECLINE_ERROR_DESC    = "EIDV Decline";

    public static final String                      GENERAL_DECLINE_ERROR_DESC = "General Decline";

    @Autowired
    @Qualifier("offerService")
    private IA_OfferProductArrangement              service;

    @Autowired
    @Qualifier("offerPCAService")
    private IA_OfferProductArrangement              offerPCAService;

    @Autowired
    private OfferProductArrangementRequestBuilder   requestBuilder;

    @Autowired
    private LoggerDAO                               logger;

    @Autowired
    private DAOExceptionHandler                     exceptionHandler;

    @Autowired
    private ConfigurationService                    configurationService;

    @TraceLog
    public DAOResponse<ProductOfferedDTO> offer(final OfferProductDTO offerProductDTO) {
        try {
            final OfferProductArrangementRequest request = requestBuilder.build(offerProductDTO);
            OfferProductArrangementResponse response = null;
            if ((AccountType.CA.toString()).equalsIgnoreCase(offerProductDTO.getAccType())) {
                response = offerPCAService.offerProductArrangement(request);
            } else {
                response = service.offerProductArrangement(request);
            }
            final DAOError error = validateResponse(response);
            if (error != null) {
                return withError(error);
            }

            return withResult(populateProductOffered(response));

        } catch (final Exception ex) {
            final DAOError daoError = exceptionHandler.handleException(ex, CLASS_NAME, METHOD_NAME, offerProductDTO);
            return withError(daoError);
        }
    }

    public DAOError validateResponse(final OfferProductArrangementResponse response) {
        if (response != null && response.getProductArrangement() != null) {

            final ProductArrangement productArrangement = response.getProductArrangement();

            if (!isProductInEligible(productArrangement) && productArrangement.getArrangementId() == null) {
                final DAOError error = new DAOError(BUSSINESS_ERROR, "Arrangement id cannot found in offer response");
                logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
                return error;
            }

        }
        return null;
    }

    public boolean isProductInEligible(final ProductArrangement productArrangement) {
        if (productArrangement.getAssociatedProduct() != null
                && productArrangement.getAssociatedProduct().getEligibilityDetails() != null
                && isNotEmpty(productArrangement.getAssociatedProduct().getEligibilityDetails().getDeclineReasons())) {
            return true;

        }
        return false;
    }

    private ProductOfferedDTO populateProductOffered(final OfferProductArrangementResponse response) {
        boolean isOverdraftRequired = false;
        BigDecimal overdraftAmount = null;
        final ProductArrangement productArrangement = response.getProductArrangement();
        String appStatus = productArrangement.getApplicationStatus();
        // when ineligible product is returned in response, appstatus wld be
        // null.Since this is a decline scenario,
        // appln status is deliberately set to declined.
        if (appStatus == null) {
            if (isProductInEligible(productArrangement)) {
                appStatus = APPLICATION_STATUS_DECLINED;
            }
        }

        final DepositArrangement depArr = (DepositArrangement) productArrangement;
        if (productArrangement != null && depArr.getIsOverdraftRequired() != null) {
            isOverdraftRequired = depArr.getIsOverdraftRequired();
            final OverdraftDetails overdraftDetails = depArr.getOverdraftDetails();
            if (overdraftDetails != null) {
                overdraftAmount = overdraftDetails.getAmount().getAmount();
            }
        }

        final Customer customer = productArrangement.getPrimaryInvolvedParty();

        final ProductExtractor firstProductExtracted = extractFirstOfferedProduct(productArrangement);

        final SIRAScoreDTO aSIRAScoreDTO = new SIRAScoreDTO();
        final CustomerScore[] customerScore = customer.getCustomerScore();
        if (customerScore != null) {
            for (final CustomerScore score : customerScore) {
                if (SIRA.equalsIgnoreCase(score.getAssessmentType())) {
                    if (score.getCustomerDecision() != null) {
                        final CustomerDecision customerDecision = score.getCustomerDecision();
                        aSIRAScoreDTO.setTotalRuleMatchCount(customerDecision.getTotalRuleMatchCount());
                        aSIRAScoreDTO.setTotalEnquiryMatchCount(customerDecision.getTotalEnquiryMatchCount());
                        aSIRAScoreDTO.setTotalRuleScore(customerDecision.getTotalRuleScore());
                        aSIRAScoreDTO.setDecisionResult(customerDecision.getResultStatus());
                        aSIRAScoreDTO.setSiraWorkFlowExecutionKey(customerDecision.getWorkflowExecutionKey());
                        aSIRAScoreDTO.setSiraConnectionErrorFlag(
                                Boolean.getBoolean(customerDecision.getConnectivityErrorFlag()));
                    }
                }
            }
        }

        return new ProductOfferedDTO(productArrangement.getArrangementId(), productArrangement.getArrangementType(),
                productArrangement.getApplicationType(), eidvScore(customer.getCustomerScore()),
                asmScore(customer.getCustomerScore()), customer.getCustomerIdentifier(), customer.getCidPersID(),
                customer.getCustomerNumber(), customer.getIndividualIdentifier(),
                firstProductExtracted.getProductName(), firstProductExtracted.getOfferTypeString(),
                firstProductExtracted.getMapOfProductOptions(), conditions(productArrangement), appStatus,
                productArrangement.getApplicationSubStatus(), firstProductExtracted.getMnemonic(),
                firstProductExtracted.getCbsProductNumberTrimmed(), firstProductExtracted.getCbsProductNumber(),
                retrieveExistingProdArr(response.getExistingProductArrangements()), isOverdraftRequired,
                overdraftAmount, firstProductExtracted.getProductIdentifier(),
                // Commenting out the piece of Code to prevent sending Existing
                // Sort Code for existing cust.
                null, // retrieveSortCodeOfExistingProdArr(response.getExistingProductArrangements()),
                firstProductExtracted.getProductFamilyIdentifier(),
                populateDeclineCondition(productArrangement, appStatus), aSIRAScoreDTO,
                new ThreatMatrixDTO(null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null),
                marketingPreferences(productArrangement));
    }

    /**
     * Method to populate decline reason code & description
     *
     * @param productArrangement
     *            ProductArrangement
     * @param appStatus
     *            String
     * @return ConditionDTO
     */
    public ConditionDTO populateDeclineCondition(final ProductArrangement productArrangement, final String appStatus) {

        if (productArrangement == null || !(APPLICATION_STATUS_DECLINED.equalsIgnoreCase(appStatus))) {
            return null;
        }
        return validateEIDVAndASM(productArrangement);
    }

    /**
     * @param productArrangement
     * @return
     */
    private ConditionDTO validateEIDVAndASM(final ProductArrangement productArrangement) {
        String declineCode = null;
        String declineDesc = null;
        final Customer customer = productArrangement.getPrimaryInvolvedParty();
        if (isProductInEligible(productArrangement)) {
            final ReasonCode reason = productArrangement.getAssociatedProduct().getEligibilityDetails()
                    .getDeclineReasons()[0];
            declineCode = reason.getCode();
            declineDesc = reason.getDescription();

        } else if (customer != null && customer.getCustomerScore() != null) {
            if (eidvScore(customer.getCustomerScore()) != null
                    && eidvScore(customer.getCustomerScore()).getCode() != null) {
                declineCode = eidvScore(customer.getCustomerScore()).getCode();
                declineDesc = eidvScore(customer.getCustomerScore()).getDescription();
            } else if (asmScore(customer.getCustomerScore()) != null
                    && asmScore(customer.getCustomerScore()).getCode() != null) {
                declineCode = asmScore(customer.getCustomerScore()).getCode();
                declineDesc = asmScore(customer.getCustomerScore()).getDescription();
            }
        }
        return new ConditionDTO(declineCode, null, declineDesc);
    }

    private ProductExtractor extractFirstOfferedProduct(final ProductArrangement productArrangement) {

        return new FirstProductExtractor(productArrangement.getOfferedProducts()).extract().getProductExtractor();
    }

    public List<ConditionDTO> conditions(final ProductArrangement productArrangement) {
        final RuleCondition[] conditions = productArrangement.getConditions();
        final List<ConditionDTO> list = new ArrayList<ConditionDTO>();
        if (conditions != null) {
            for (final RuleCondition condition : conditions) {
                String value = condition.getResult();
                if ("OVERDRAFT_AMOUNT".equalsIgnoreCase(condition.getName())) {
                    value = condition.getValue() != null ? (condition.getValue().getAmount()).toString() : null;
                }
                list.add(new ConditionDTO(condition.getName(), condition.getRuleCode(), value));
            }
        }
        return list;
    }

    private static List<MarketingPreferenceDTO> marketingPreferences(final ProductArrangement productArrangement) {
    final MarketingPreference[] marketingPreferences = productArrangement.getMarketingPreferences();
    if (marketingPreferences == null) {
        return Collections.emptyList();
    }

    final List<MarketingPreferenceDTO> result = new ArrayList<MarketingPreferenceDTO>(marketingPreferences.length);
    for (final MarketingPreference marketingPreference : marketingPreferences) {
        result.add(new MarketingPreferenceDTO(marketingPreference.getEntitlementId(), marketingPreference.getConsentOption()));
    }
    return result;
    }

    private ExistingProductArrangementDTO retrieveExistingProdArr(final ProductArrangement[] productArrangement) {
        return new ExistingProductArrangementDTO(productArrangement);

    }

    /*
     * private String retrieveSortCodeOfExistingProdArr(ProductArrangement[]
     * productArrangement) { String sortCode = null; if (productArrangement !=
     * null) { Calendar tempDate = null; for (ProductArrangement product :
     * productArrangement) { Product existingProduct =
     * product.getAssociatedProduct(); ExtSysProdIdentifier[]
     * externalProductIdentifier =
     * existingProduct.getExternalSystemProductIdentifier(); for
     * (ExtSysProdIdentifier productIdentifier : externalProductIdentifier) { if
     * (CBS_PRODUCT_SYSTEM_CODE.equalsIgnoreCase(productIdentifier.getSystemCode
     * ())) { if (tempDate == null ||
     * tempDate.before(product.getArrangementStartDate()) ||
     * tempDate.equals(product.getArrangementStartDate())) { tempDate =
     * product.getArrangementStartDate(); OrganisationUnit[] orgUnits =
     * product.getFinancialInstitution().getHasOrganisationUnits(); for
     * (OrganisationUnit units : orgUnits) { sortCode = units.getSortCode(); } }
     * } break; }
     *
     * } } return sortCode; }
     */

    // Downsell changes ends here

    public ASMScoreDTO asmScore(final CustomerScore[] customerScore) {
        if (customerScore == null) {
            return null;
        }
        for (final CustomerScore score : customerScore) {
            if (ASM.equalsIgnoreCase(score.getAssessmentType()) && score.getReferralCode() != null) {
                final ReferralCode referralCode = score.getReferralCode(0);
                return new ASMScoreDTO(score.getScoreResult(), score.getAssessmentType(), score.getDecisionCode(),
                        score.getDecisionText(), referralCode.getCode(), referralCode.getDescription());
            }
        }
        return null;
    }

    public EIDVScoreDTO eidvScore(final CustomerScore[] customerScore) {
        if (customerScore == null) {
            return null;
        }
        for (final CustomerScore score : customerScore) {
            if (EIDV.equalsIgnoreCase(score.getAssessmentType())) {
                String evidenceIdentifier = null;
                String identityStrength = null;
                if (score.getAssessmentEvidence() != null) {
                    final AssessmentEvidence assessmentEvidence = score.getAssessmentEvidence(0);
                    evidenceIdentifier = assessmentEvidence.getEvidenceIdentifier();
                    identityStrength = assessmentEvidence.getIdentityStrength();
                }
                String referralCode = null;
                String referraldescription = null;
                if (score.getReferralCode() != null && score.getReferralCode().length > 0) {
                    referralCode = score.getReferralCode(0).getCode();
                    referraldescription = score.getReferralCode(0).getDescription();
                }
                return new EIDVScoreDTO(score.getScoreResult(), score.getAssessmentType(), evidenceIdentifier,
                        identityStrength, referralCode, referraldescription);
            }
        }
        return null;
    }

    public void setOfferService(final IA_OfferProductArrangement service) {
        this.service = service;
    }

}
