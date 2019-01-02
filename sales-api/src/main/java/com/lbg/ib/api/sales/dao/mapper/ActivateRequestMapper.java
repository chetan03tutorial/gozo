/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.Constants.ActivateConstants;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AccountDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AssessmentEvidence;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CurrencyAmount;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDeviceDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DirectDebit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Location;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OrganisationUnit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Rates;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.BaseRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;

@Component
public class ActivateRequestMapper {

    private static final String VALID_STRING = "VALID";

    private static final String BLANK = "BLANK";

    private static final String SERVICE_NAME = "ActivateProductArrangement";

    private static final String SERVICE_ACTION = "activateProductArrangement";

    private static final String AMT_EXCESS_FEE = "AMT_EXCESS_FEE";

    private static final String AMT_EXCESS_FEE_BAL_INC = "AMT_EXCESS_FEE_BAL_INC";

    private static final String EXCESS_FEE_CAP = "EXCESS_FEE_CAP";

    private static final String BASE_INT_RATE = "BASE_INT_RATE";

    private static final String MARGIN_OBR_RATE = "MARGIN_OBR_RATE";

    private static final String AUTH_EAR = "AUTH_EAR";

    private static final String AUTH_MONTHLY = "AUTH_MONTHLY";

    private static final String AMT_MONTHLY_FEE = "AMT_MONTHLY_FEE";

    private static final String INT_FREE_OVERDRAFT = "INT_FREE_OVERDRAFT";

    private static final String TOTAL_COST_OF_CREDIT = "TOTAL_COST_OF_CREDIT";

    private static final String UNAUTH_EAR = "UNAUTH_EAR";

    private static final String UNAUTH_MONTHLY_AMT_MONTHLY_FEE = "UNAUTH_MONTHLY_AMT_MONTHLY_FEE";

    private static final String SOURCE_SYSTEM_IDENTIFIER_ONLINE = "1";
    private static final String SOURCE_SYSTEM_IDENTIFIER_OFFLINE = "4";
    private static final String SOURCE_SYSTEM_IDENTIFIER_ICVA = "3";

    private static final String ASM_REFER_APP_STATUS = "1003";

    static final int TEN_THOUSAND = 10000;

    static final String MNEMONIC_ID = "00010";

    static final String METHOD_NAME = "activateProduct";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility gboHeaderUtility;

    @Autowired
    private MCAHeaderUtility mcaHeaderUtility;

    @Autowired
    private ConfigurationDAO configuration;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public ActivateProductArrangementRequest mapRequestForActivateService(ActivateProductDTO requestDTO) {
        ActivateProductArrangementRequest request = new ActivateProductArrangementRequest();
        request.setHeader(populateHeaders(requestDTO).getHeader());
        request.setProductArrangement(mapDepositArrangmentToActivateRequest(requestDTO));
        request.setSourceSystemIdentifier(extractSourceSystemIdentifier(requestDTO));
        return request;
    }

    private String extractSourceSystemIdentifier(ActivateProductDTO requestDTO) {
        String appStatus = requestDTO.getAppStatus();
        if (requestDTO.getPrimaryAssessmentEvidence() != null) {
            return SOURCE_SYSTEM_IDENTIFIER_ICVA;
        } else if (appStatus != null && appStatus.equals(ASM_REFER_APP_STATUS)) {
            return SOURCE_SYSTEM_IDENTIFIER_OFFLINE;
        } else {
            return SOURCE_SYSTEM_IDENTIFIER_ONLINE;
        }
    }

    private DepositArrangement mapDepositArrangmentToActivateRequest(ActivateProductDTO requestDTO) {
        DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setArrangementId(requestDTO.getArrangementId());
        productArrangement.setArrangementType(requestDTO.getArrangementType());
        productArrangement.setApplicationType(requestDTO.getApplicationType());
        productArrangement.setFinancialInstitution(mapFinancialInstitution(requestDTO));
        productArrangement.setAssociatedProduct(mapAssociatedProductsToRequest(requestDTO));
        productArrangement.setPrimaryInvolvedParty(mapPrimaryInvolvedParty(requestDTO));
        productArrangement.setCbsProductId(requestDTO.getCbsProductId());
        productArrangement.setSellerLegalEntity(requestDTO.getSellerLegalEntity());
        productArrangement.setAccountNumber(requestDTO.getAccountNumber());

        if (requestDTO.getSortCode() != null && requestDTO.getAccountNumber() != null) {
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setAccountNumber(requestDTO.getAccountNumber());
            accountDetails.setSortCode(requestDTO.getSortCode());
            productArrangement.setAccountDetails(accountDetails);
        }
        if (null != requestDTO.getParentAssessmentEvidence()) {
            productArrangement.setGuardianDetails(setGuardianDetails(requestDTO));
        }
        productArrangement.setIsJointParty(false);
        productArrangement.setConditions(mapProductConditions(requestDTO));
        // Map Overdraft details
        productArrangement.setIsOverdraftRequired(requestDTO.getOverDraftOpted());
        if (requestDTO.getOverDraftOpted()) {
            productArrangement.setOverdraftDetails(mapOverDraftDetails(requestDTO));
        }
        // Map Account Switching Details
        if (requestDTO.getSwitchDetails() != null) {
            DirectDebit directDebit = mapAccountSwitchingDetails(requestDTO);
            productArrangement.setAccountSwitchingDetails(directDebit);
            session.setSwitchingDetails(directDebit);
        }

        if (session.getSwitchingDetails() != null && null != session.getBranchContext()) {
            productArrangement.setAccountSwitchingDetails(session.getSwitchingDetails());
        }

        if (requestDTO.getCustomerDocuments() != null) {
            List<com.lbg.ib.api.sales.product.domain.activate.CustomerDocument> customerDocumentsList = requestDTO
                    .getCustomerDocuments();
            CustomerDocument[] customerDocumentsArray = new CustomerDocument[requestDTO.getCustomerDocuments().size()];

            // Get the current date and set in the document additional info as
            // the document timestamp
            Calendar now = Calendar.getInstance();
            String formattedDate = dateFormatter.format(now.getTime());

            int count = 0;
            for (com.lbg.ib.api.sales.product.domain.activate.CustomerDocument cDocument : customerDocumentsList) {
                CustomerDocument customerDocument = new CustomerDocument();
                customerDocument.setDocumentPurpose(cDocument.getDocumentPurpose());
                customerDocument.setDocumentType(cDocument.getDocumentType());
                customerDocument.setDocumentReferenceText(cDocument.getDocumentReferenceText());
                if (cDocument.getDocumentReferenceIndex() != null
                        && StringUtils.isEmpty(cDocument.getDocumentReferenceIndex().trim())) {
                    customerDocument.setDocumentReferenceIndex(BLANK);
                } else {
                    customerDocument.setDocumentReferenceIndex(cDocument.getDocumentReferenceIndex());
                }
                customerDocument.setDocumentCountryOfIssue(cDocument.getDocumentCountryOfIssue());

                // persist the document date in additional info.
                String documentAdditionalInfo = cDocument.getDocumentAdditionalInfo();
                if (VALID_STRING.equals(documentAdditionalInfo)) {
                    customerDocument.setDocumentAdditionalInfo(formattedDate);
                } else {
                    customerDocument.setDocumentAdditionalInfo(documentAdditionalInfo);
                }

                customerDocumentsArray[count++] = customerDocument;
            }

            productArrangement.setCustomerDocuments(customerDocumentsArray);
        }

        productArrangement.setSIRAEnabledSwitch(requestDTO.getSiraEnabledSwitch());
        return productArrangement;
    }

    private Customer setGuardianDetails(ActivateProductDTO requestDTO) {
        Customer parentInvolvedParty = new Customer();
        CustomerScore score = null;
        if (requestDTO.getParentAssessmentEvidence() != null) {
            score = populateCustomerScore(requestDTO.getParentAssessmentEvidence());

        }
        parentInvolvedParty.setCustomerScore(new CustomerScore[] { score });

        return parentInvolvedParty;
    }

    private CustomerScore populateCustomerScore(
            com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence assessmentEvidence) {
        CustomerScore score = new CustomerScore();
        AssessmentEvidence[] aEvidenceArr = new AssessmentEvidence[1];
        AssessmentEvidence assesment = new AssessmentEvidence();
        assesment.setAddressStrength(assessmentEvidence.getAddressStrength());
        assesment.setEvidenceIdentifier(assessmentEvidence.getEvidenceIdentifier());
        assesment.setIdentityStrength(assessmentEvidence.getIdentityStrength());
        aEvidenceArr[0] = assesment;
        score.setAssessmentEvidence(aEvidenceArr);
        return score;
    }

    private RuleCondition[] mapProductConditions(ActivateProductDTO requestDTO) {
        List<ConditionDTO> conditions = requestDTO.getConditions();
        RuleCondition[] ruleConditions = new RuleCondition[conditions.size()];
        if (requestDTO.getOverdraftIntrestRates() != null) {
            // To accomodate OD rates
            ruleConditions = new RuleCondition[conditions.size() + 1];
        }

        int conditionsSize = conditions.size();
        for (int i = 0; i < conditionsSize; i++) {
            ruleConditions[i] = new RuleCondition();
            if (conditions.get(i) != null) {
                ruleConditions[i].setName(conditions.get(i).getName());
                ruleConditions[i].setResult(conditions.get(i).getValue());
            }
        }

        if (requestDTO.getOverdraftIntrestRates() != null) {
            ruleConditions[conditionsSize] = new RuleCondition();
            if (requestDTO.getOverdraftIntrestRates().getAmtOverdraft() != null) {
                ruleConditions[conditionsSize].setName("OFFERED_OVERDRAFT_AMOUNT");
                ruleConditions[conditionsSize]
                        .setResult(requestDTO.getOverdraftIntrestRates().getAmtOverdraft().toString());
            }
        }
        return ruleConditions;
    }

    private Customer mapPrimaryInvolvedParty(ActivateProductDTO requestDTO) {
        Customer primaryInvolvedParty = new Customer();

        // set the assessment evidence in case its a ICVA integration
        if (null != requestDTO.getPrimaryAssessmentEvidence()) {
            CustomerScore score = populateCustomerScore(requestDTO.getPrimaryAssessmentEvidence());
            primaryInvolvedParty.setCustomerScore(new CustomerScore[] { score });
            primaryInvolvedParty.setUserType(ActivateConstants.CUSTOMER_BRANCH);
            if (null != session.getBranchContext()) {
                BranchContext branchContext = session.getBranchContext();
                primaryInvolvedParty.setInternalUserIdentifier(branchContext.getColleagueId());
            }
            if (requestDTO.isSnr() && requestDTO.getColleagueId() != null) {
                primaryInvolvedParty.setInternalUserIdentifier(requestDTO.getColleagueId());
            }
        } else {
            if (validateRequestPassword(requestDTO)) {
                primaryInvolvedParty.setPassword(requestDTO.getPassword());
                primaryInvolvedParty.setIsRegistrationSelected(true);
            } else {
                primaryInvolvedParty.setPassword(null);
                primaryInvolvedParty.setIsRegistrationSelected(false);
            }
            CustomerDeviceDetails customerDeviceDetails = playedBy(requestDTO);
            Location location = null;
            // Map latitude & logitude
            if (requestDTO.getLocation() != null) {
                location = new Location();
                location.setLatitude(requestDTO.getLocation().getLat());
                location.setLongitude(requestDTO.getLocation().getLng());

            }

            if (null != location || null != customerDeviceDetails) {
                Individual isPlayedBy = new Individual();
                isPlayedBy.setCustomerLocation(location);
                isPlayedBy.setCustomerDeviceDetails(customerDeviceDetails);
                primaryInvolvedParty.setIsPlayedBy(isPlayedBy);

            }
            primaryInvolvedParty.setAccountingSortCode(requestDTO.getAccountingSortCode());

            if (null != session.getBranchContext()) {
                primaryInvolvedParty.setUserType(ActivateConstants.CUSTOMER_BRANCH);
                BranchContext branchContext = session.getBranchContext();
                primaryInvolvedParty.setInternalUserIdentifier(branchContext.getColleagueId());
            } else {
                primaryInvolvedParty.setUserType(ActivateConstants.CUSTOMER);
                primaryInvolvedParty.setInternalUserIdentifier(ActivateConstants.INTERNAL_USER_IDENTIFIER);
            }
        }
        return primaryInvolvedParty;
    }

    // Sonar fix
    private boolean validateRequestPassword(ActivateProductDTO requestDTO) {
        return (requestDTO.getPassword() != null && !"".equals(requestDTO.getPassword()));
    }

    private Organisation mapFinancialInstitution(ActivateProductDTO requestDTO) {
        OrganisationUnit orgUnit = new OrganisationUnit();
        String sortCode = null;
        if (null != session.getBranchContext() && session.getAddPartyContext() == null) {
            if (requestDTO.getAlternateSortCode() != null) {
                sortCode = requestDTO.getAlternateSortCode();
            } else {
                sortCode = session.getBranchContext().getOriginatingSortCode();
            }
        } else {
            sortCode = requestDTO.getSortCode();
        }
        orgUnit.setSortCode(sortCode);
        OrganisationUnit[] orgUnits = new OrganisationUnit[] { orgUnit };
        Organisation financialInstitution = new Organisation();
        financialInstitution.setHasOrganisationUnits(orgUnits);
        return financialInstitution;
    }

    private Product mapAssociatedProductsToRequest(ActivateProductDTO requestDTO) {
        Product product = new Product();
        product.setProductName(requestDTO.getProductName());
        product.setProductIdentifier(requestDTO.getProductIdentifier());

        ProductFamily pdtFamily = new ProductFamily();
        ExtSysProdFamilyIdentifier extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier();
        extsysprodfamilyidentifier.setProductFamilyIdentifier(requestDTO.getProductFamilyIdentifier());
        ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = new ExtSysProdFamilyIdentifier[] {
                extsysprodfamilyidentifier };
        pdtFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifiers);
        ProductFamily[] associatedFamily = new ProductFamily[] { pdtFamily };
        product.setAssociatedFamily(associatedFamily);
        return product;
    }

    private BaseRequest populateHeaders(ActivateProductDTO requestDTO) {

        RequestHeader requestHeader = new RequestHeader();

        List<SOAPHeader> soapHeaders = null;

        String appStatus = null;
        ArrangeToActivateParameters appParam = session.getArrangeToActivateParameters();
        if (appParam != null) {
            appStatus = appParam.getEidvStatus();
        }

        // To be reverted. Strategic fix for JIRA PCA-4826
        if (ActivateConstants.REFER_APP_STATUS.equalsIgnoreCase(appStatus)
                && (null != requestDTO.getPrimaryAssessmentEvidence())) {
            soapHeaders = gboHeaderUtility.customPrepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        }
        if (null != session.getBranchContext()) {
            // To be reverted. Strategic fix for JIRA PCA-4826
            soapHeaders = mcaHeaderUtility.customPrepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
            if (requestDTO.isSnr()) {
                for (SOAPHeader soapHeader : soapHeaders) { // changed the index
                                                            // as the index for
                                                            // contact header
                                                            // has been changed
                    if (soapHeader.getValue() instanceof ContactPoint) {
                        ContactPoint contactPoint = (ContactPoint) soapHeader.getValue();
                        contactPoint.setContactPointId("0000" + requestDTO.getOriginatingSortCode());
                    }
                }
            }
        } else {
            // To be reverted. Strategic fix for JIRA PCA-4826
            soapHeaders = gboHeaderUtility.customPrepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        }

        if (null != session.getUserContext()) {
            Map<String, Object> map = configuration.getConfigurationItems("ChannelIdMapping");
            requestHeader.setChannelId(map.get(session.getUserContext().getChannelId()).toString());
        }

        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction("activateProductArrangement");
        requestHeader.setInteractionId(session.getSessionId());

        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }

    private OverdraftDetails mapOverDraftDetails(ActivateProductDTO requestDTO) {
        OverdraftDetails od = new OverdraftDetails();
        od.setAmount(currency(requestDTO.getOdAmountOpted()));
        Calendar cal = new GregorianCalendar();
        od.setExpiryDate(cal);
        OverdraftIntrestRates overdraftIntrestRates = null;
        if (requestDTO.getOverdraftIntrestRates() != null) {
            overdraftIntrestRates = requestDTO.getOverdraftIntrestRates();
            Rates[] interestRates = populateInterestRatesForOverdraft(overdraftIntrestRates);
            od.setInterestRates(interestRates);
        }
        return od;
    }

    private DirectDebit mapAccountSwitchingDetails(ActivateProductDTO requestDTO) {
        DirectDebit accountSwitchingDetails = new DirectDebit();
        AccountSwitching switchDetails = requestDTO.getSwitchDetails();
        accountSwitchingDetails.setSortCode(switchDetails.getSortCode());
        accountSwitchingDetails.setAccountNumber(switchDetails.getAccountNumber());
        accountSwitchingDetails.setAccountHolderName(switchDetails.getAccountName());
        accountSwitchingDetails.setBankName(switchDetails.getBankName());
        if (switchDetails.isHasDebitCard() != null) {
            accountSwitchingDetails.setDebitCardHeld(switchDetails.isHasDebitCard().toString());
        }

        accountSwitchingDetails.setCardNumber(switchDetails.getCardNumber());
        accountSwitchingDetails.setCardExpiryDate(switchDetails.getCardExpiryDate());
        accountSwitchingDetails.setOverdraftHeldIndicator(switchDetails.isCanBeOverDrawn().toString());
        if (switchDetails.isCanBeOverDrawn()) {
            CurrencyAmount currencyAmount = currency(
                    switchDetails.getPayOdAmount() != null ? switchDetails.getPayOdAmount() : new BigDecimal("0"));
            // GBP added for Switching Requirement
            currencyAmount.setCurrencyCode("GBP");
            accountSwitchingDetails.setAmount(currencyAmount);
        }
        accountSwitchingDetails.setTextAlert(switchDetails.isTextAlert().toString());
        if (switchDetails.isTextAlert()) {
            accountSwitchingDetails.setMobileNumber(switchDetails.getMobileNumber().toString());
        }
        if (switchDetails.getSwitchingDate() != null) {
            accountSwitchingDetails.setSwitchDate(calendar(switchDetails.getSwitchingDate()));
        }
        if (switchDetails.isConsent() != null) {
            accountSwitchingDetails.setConsent(switchDetails.isConsent().toString());
        } else {
            accountSwitchingDetails.setConsent(Boolean.FALSE.toString());
        }

        return accountSwitchingDetails;
    }

    private CurrencyAmount currency(BigDecimal currency) {
        CurrencyAmount currencyAmount = new CurrencyAmount();
        currencyAmount.setAmount(currency);

        return currencyAmount;
    }

    private Rates[] populateInterestRatesForOverdraft(OverdraftIntrestRates overDraftIntrestRates) {
        List<Rates> rates = new ArrayList<Rates>();
        populateInterstRates(overDraftIntrestRates, rates);
        if (overDraftIntrestRates.getAmtUsageFeeOverdraft() != null) {
            rates.add(setInterestRateParams(AMT_MONTHLY_FEE, overDraftIntrestRates.getAmtUsageFeeOverdraft()));
        }
        if (overDraftIntrestRates.getAmtIntFreeOverdraft() != null) {
            rates.add(setInterestRateParams(INT_FREE_OVERDRAFT, overDraftIntrestRates.getAmtIntFreeOverdraft()));
        }
        if (overDraftIntrestRates.getAmtTotalCreditCost() != null) {
            rates.add(setInterestRateParams(TOTAL_COST_OF_CREDIT, overDraftIntrestRates.getAmtTotalCreditCost()));
        }
        if (overDraftIntrestRates.getIntrateUnauthEAR() != null
                && StringUtils.isNotEmpty(overDraftIntrestRates.getIntrateUnauthEAR())) {
            rates.add(setInterestRateParams(UNAUTH_EAR,
                    divideByTenThousand(overDraftIntrestRates.getIntrateUnauthEAR())));
        }
        if (overDraftIntrestRates.getIntrateUnauthMnthly() != null
                && StringUtils.isNotEmpty(overDraftIntrestRates.getIntrateUnauthMnthly())) {
            rates.add(setInterestRateParams(UNAUTH_MONTHLY_AMT_MONTHLY_FEE,
                    divideByTenThousand(overDraftIntrestRates.getIntrateUnauthMnthly())));
        }

        return rates.toArray(new Rates[rates.size()]);
    }

    /**
     * @param overDraftIntrestRates
     * @param rates
     */
    private void populateInterstRates(OverdraftIntrestRates overDraftIntrestRates, List<Rates> rates) {
        if (overDraftIntrestRates.getAmtExcessFee() != null) {
            rates.add(setInterestRateParams(AMT_EXCESS_FEE, overDraftIntrestRates.getAmtExcessFee()));
        }
        if (overDraftIntrestRates.getAmtExcessFeeBalIncr() != null) {
            rates.add(setInterestRateParams(AMT_EXCESS_FEE_BAL_INC, overDraftIntrestRates.getAmtExcessFeeBalIncr()));
        }
        if (overDraftIntrestRates.getNExcessFeeCap() != null) {
            rates.add((setInterestRateParams(EXCESS_FEE_CAP,
                    convertToBigDecimal(overDraftIntrestRates.getNExcessFeeCap()))));
        }
        if (overDraftIntrestRates.getIntrateBase() != null) {
            rates.add(
                    setInterestRateParams(BASE_INT_RATE, convertToBigDecimal(overDraftIntrestRates.getIntrateBase())));
        }
        if (overDraftIntrestRates.getIntrateMarginOBR() != null
                && StringUtils.isNotEmpty(overDraftIntrestRates.getIntrateMarginOBR())) {
            rates.add(setInterestRateParams(MARGIN_OBR_RATE,
                    divideByTenThousand(overDraftIntrestRates.getIntrateMarginOBR())));
        }
        if (overDraftIntrestRates.getIntrateAuthEAR() != null
                && StringUtils.isNotEmpty(overDraftIntrestRates.getIntrateAuthEAR())) {
            rates.add(setInterestRateParams(AUTH_EAR, divideByTenThousand(overDraftIntrestRates.getIntrateAuthEAR())));
        }
        if (overDraftIntrestRates.getIntrateAuthMnthly() != null
                && StringUtils.isNotEmpty(overDraftIntrestRates.getIntrateAuthMnthly())) {
            rates.add(setInterestRateParams(AUTH_MONTHLY,
                    divideByTenThousand(overDraftIntrestRates.getIntrateAuthMnthly())));
        }
    }

    private BigDecimal divideByTenThousand(String input) {
        BigDecimal d = new BigDecimal(input);
        d = d.setScale(4);
        return (d.divide(new BigDecimal(TEN_THOUSAND)));
    }

    private BigDecimal convertToBigDecimal(String a) {
        return new BigDecimal(a);
    }

    /**
     * Method to convert a BigInteger to BigDecimal
     *
     * @param a
     * @return BigDecimal
     * @throws NumberFormatException
     */
    private BigDecimal convertToBigDecimal(BigInteger a) {
        return new BigDecimal(a);
    }

    /**
     * Method to set the Interest rate type & value
     *
     * @param interestRateType
     * @param interestRateValue
     * @return Rates
     */
    private Rates setInterestRateParams(String interestRateType, BigDecimal interestRateValue) {
        Rates rate = new Rates();
        rate.setType(interestRateType);
        rate.setValue(interestRateValue);
        return rate;
    }

    private Calendar calendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    private CustomerDeviceDetails playedBy(ActivateProductDTO activateProductDTO) {
        CustomerDeviceDetails aCustomerDeviceDetails = null;
        if ((AccountType.CA.toString()).equalsIgnoreCase(activateProductDTO.getArrangementType())) {
            aCustomerDeviceDetails = new CustomerDeviceDetails();
            aCustomerDeviceDetails.setWorkFlowName(activateProductDTO.getSiraWorkFlowName());
        }
        return aCustomerDeviceDetails;
    }
}
