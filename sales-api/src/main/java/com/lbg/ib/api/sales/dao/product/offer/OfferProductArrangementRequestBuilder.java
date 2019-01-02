/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.offer;

import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.dto.product.offer.TaxResidencyDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.StructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.UnstructuredPostalAddressDTO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.RelatedInvolvedParty;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AffiliateDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Channel;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CurrencyAmount;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDeviceDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Employer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IdentificationDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IndividualName;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.MarketingPreference;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.StructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TaxResidencyDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.UnstructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementRequest;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lloydstsb.ea.enums.BrandValue;

@Component
public class OfferProductArrangementRequestBuilder {

    private static final String             UNITED_KINGDOM                = "United Kingdom";

    private static final String             APPLICATION_RELATIONSHIP_TYPE = "20001";                      // 20001
                                                                                                          // -
                                                                                                          // Cross-Sell

    private static final String             AFFILIATED_ID                 = "A18";

    private static final String             IDENTIFICATION_DTLS_TYPE      = "001";

    public static final String              CUSTOMER                      = "1001";                       // 1002
                                                                                                          // Branch
                                                                                                          // Staff,
                                                                                                          // 1003
                                                                                                          // System

    public static final String              INTERNET_BANKING              = "003";

    public static final String              INTERNET                      = "004";

    public static final String              INTERNAL_USER_IDENTIFIER      = "10.245.224.125";

    public static final String              APPLICATION_TYPE              = "10001";                      // 10002
                                                                                                          // Trade
                                                                                                          // 10003
    public static final String              JOINT_APPLICATION_TYPE        = "10003";

    public static final Map<String, String> PHONE_DEVICE_TYPE_MAP         = new HashMap<String, String>();

    static {
        PHONE_DEVICE_TYPE_MAP.put("Mobile", "7");
        PHONE_DEVICE_TYPE_MAP.put("Fixed", "1");
        PHONE_DEVICE_TYPE_MAP.put("Work", "4");
    }

    private static final String     EXPECTED_MONTHLY_DEPOSIT_AMOUNT = "EXP_MONTHLY_DEPOSIT_AMOUNT";

    private static final String     EMPTY_STRING                    = "";

    private final SessionManagementDAO    session;

    private final GBOHeaderUtility        gboHeaderUtility;

    private final ReferenceDataServiceDAO referenceDataService;

    private static final String     FATCA                           = "FATCA";

    private final ChannelBrandingDAO      channelBrandingService;

    public static final String      SIRA_WORKFLOW_NAME_LTB          = "LBG_ULLO_RT_WF1_RULES1";

    public static final String      SIRA_WORKFLOW_NAME_HBOS         = "LBG_UHBC_RT_WF1_RULES1";

    private final MCAHeaderUtility        mcaHeaderUtility;

    @Autowired
    private ConfigurationDAO        configuration;

    @Autowired
    private CommonUtils             commonUtils;

    public void setCommonUtils(final CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    @Autowired
    public OfferProductArrangementRequestBuilder(final SessionManagementDAO session, final GBOHeaderUtility gboHeaderUtility,
            final ReferenceDataServiceDAO referenceDataService, final ChannelBrandingDAO channelBrandingService,
            final MCAHeaderUtility mcaHeaderUtility) {
        this.session = session;
        this.gboHeaderUtility = gboHeaderUtility;
        this.referenceDataService = referenceDataService;
        this.channelBrandingService = channelBrandingService; // Added for AFDI2
        this.mcaHeaderUtility = mcaHeaderUtility;
    }

    public OfferProductArrangementRequestBuilder(final SessionManagementDAO session, final GBOHeaderUtility gboHeaderUtility,
            final ReferenceDataServiceDAO referenceDataService, final ChannelBrandingDAO channelBrandingService,
            final MCAHeaderUtility mcaHeaderUtility, final ConfigurationDAO configuration) {
        this.session = session;
        this.gboHeaderUtility = gboHeaderUtility;
        this.referenceDataService = referenceDataService;
        this.channelBrandingService = channelBrandingService; // Added for AFDI2
        this.mcaHeaderUtility = mcaHeaderUtility;
        this.configuration = configuration;
    }

    OfferProductArrangementRequest build(final OfferProductDTO offerProductDTO) {
        final OfferProductArrangementRequest rq = new OfferProductArrangementRequest();
        rq.setProductArrangement(getArrangement(offerProductDTO));
        rq.setHeader(prepareSoapHeaders());
        return rq;
    }

    private RequestHeader prepareSoapHeaders() {
        final RequestHeader requestHeader = new RequestHeader();

        List<SOAPHeader> soapHeaders = null;
        if (null != session.getBranchContext()) {
            soapHeaders = mcaHeaderUtility.prepareSoapHeader("offerProductArrangement", "OfferProductArrangement");
        } else {
            soapHeaders = gboHeaderUtility.prepareSoapHeader("offerProductArrangement", "OfferProductArrangement");
        }

        if (null != session.getUserContext()) {
            final Map<String, Object> map = configuration.getConfigurationItems("ChannelIdMapping");
            requestHeader.setChannelId(map.get(session.getUserContext().getChannelId()).toString());
        }

        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction("OfferProductArrangement");
        requestHeader.setInteractionId(session.getSessionId());
        return requestHeader;
    }

    private DepositArrangement getArrangement(final OfferProductDTO offerProductDTO) {
        final DepositArrangement productArrangement = new DepositArrangement();
        productArrangement.setInitiatedThrough(initChannel());
        productArrangement.setPrimaryInvolvedParty(primaryInvolvedParty(offerProductDTO));
        final RelatedInvolvedParty relatedInvolvedParty = offerProductDTO.getRelatedInvolvedParty();
        if (relatedInvolvedParty != null) {
            productArrangement.setRelatedInvolvedParty(relatedInvolvedParty(relatedInvolvedParty));
        }
        productArrangement.setAssociatedProduct(product(offerProductDTO));
        // Fetching the dynamic account type as part of cross-sell
        // implementation.
        productArrangement.setArrangementType(offerProductDTO.getAccType());
        if (offerProductDTO.getRelatedInvolvedParty() == null) {
            productArrangement.setApplicationType(APPLICATION_TYPE);
        } else {
            productArrangement.setApplicationType(JOINT_APPLICATION_TYPE);
        }

        productArrangement.setMarketingPreferenceByEmail(offerProductDTO.marketPreferencedByEmail());
        productArrangement.setMarketingPreferenceByMail(offerProductDTO.marketPreferencedByMail());
        productArrangement.setMarketingPreferenceByPhone(offerProductDTO.marketPreferencedByPhone());
        productArrangement.setMarketingPreferenceBySMS(offerProductDTO.marketPreferencedBySMS());
        productArrangement.setFundingSource(offerProductDTO.fundingSource());
        productArrangement.setAccountPurpose(offerProductDTO.accountPurpose());

        productArrangement.setConditions(populateConditionsForProductArrangement(offerProductDTO));
        // New Attributes changes

        // Setting the relatedApplication Id and status for cross-sell request
        if (offerProductDTO.getRelatedApplication() != null) {
            productArrangement.setRelatedApplicationExists(true);
            productArrangement.setRelatedApplicationId(offerProductDTO.getRelatedApplication().getApplicationId());
            productArrangement
                    .setRelatedApplicationStatus(offerProductDTO.getRelatedApplication().getApplicationStatus());
            productArrangement.setApplicationRelationShipType(APPLICATION_RELATIONSHIP_TYPE);

            final AffiliateDetails affiliateDetail = new AffiliateDetails();
            // Hardcoding the affiliatedId - Needs to verified
            affiliateDetail.setAffiliateIdentifier(AFFILIATED_ID);

            final AffiliateDetails[] affiliatedetails = new AffiliateDetails[] { affiliateDetail };

            productArrangement.setAffiliatedetails(affiliatedetails);
        }
        // Cross-sell changes ends

        productArrangement.setSIRAEnabledSwitch(offerProductDTO.getSiraEnabledSwitch());

        productArrangement.setMarketingPreferences(marketingPreferences(offerProductDTO));

        return productArrangement;
    }

    private RuleCondition[] populateConditionsForProductArrangement(final OfferProductDTO offerProductDTO) {
        final List<RuleCondition> conditions = new ArrayList<RuleCondition>();
        if (offerProductDTO.getIntendSwitch() != null) {
            conditions.add(setRuleConditions("INTEND_TO_SWITCH", offerProductDTO.getIntendSwitch().toString()));
        }
        if (offerProductDTO.isIntendOverDraft() != null) {
            conditions.add(setRuleConditions("INTEND_TO_OVERDRAFT", offerProductDTO.isIntendOverDraft().toString()));
        }
        if (offerProductDTO.getIntendOdAmount() != null) {
            final CurrencyAmount odAmount = new CurrencyAmount();
            odAmount.setAmount(new BigDecimal(offerProductDTO.getIntendOdAmount()));
            conditions.add(setRuleConditions("OVERDRAFT_AMOUNT", odAmount));
        }
        if (offerProductDTO.getExptdMntlyDepAmt() != null) {
            conditions.add(setRuleConditions(EXPECTED_MONTHLY_DEPOSIT_AMOUNT,
                    offerProductDTO.getExptdMntlyDepAmt().toString()));
        }
        return conditions.toArray(new RuleCondition[conditions.size()]);
    }

    private RuleCondition setRuleConditions(final String name, final String result) {
        final RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setName(name);
        ruleCondition.setResult(result);
        return ruleCondition;
    }

    private RuleCondition setRuleConditions(final String name, final CurrencyAmount amount) {
        final RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setName(name);
        ruleCondition.setValue(amount);
        return ruleCondition;
    }

    private Product product(final OfferProductDTO offerProductDTO) {
        final InstructionDetails instructionDetails = new InstructionDetails();
        final Product product = new Product();
        product.setProductIdentifier(offerProductDTO.productIdentifier());//
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifiers(offerProductDTO));//
        product.setProductoptions(productOptions(offerProductDTO));//
        product.setProductName(offerProductDTO.productName());
        instructionDetails.setInstructionMnemonic(offerProductDTO.mnemonic());//
        product.setInstructionDetails(instructionDetails);
        return product;
    }

    private ExtSysProdIdentifier[] externalSystemProductIdentifiers(final OfferProductDTO offerProductDTO) {
        final List<ExtSysProdIdentifier> ids = new ArrayList<ExtSysProdIdentifier>();
        for (final Entry<String, String> entry : offerProductDTO.productExternalIdentifiers().entrySet()) {
            final ExtSysProdIdentifier id = new ExtSysProdIdentifier();
            id.setSystemCode(entry.getKey());
            id.setProductIdentifier(entry.getValue());
            ids.add(id);
        }
        return ids.toArray(new ExtSysProdIdentifier[ids.size()]);
    }

    private static ProductOptions[] productOptions(final OfferProductDTO offerProductDTO) {
        final List<ProductOptions> list = new ArrayList<ProductOptions>();
        for (final Entry<String, String> entry : offerProductDTO.productOptions().entrySet()) {
            final ProductOptions options = new ProductOptions();
            options.setOptionsCode(entry.getKey());
            options.setOptionsValue(entry.getValue());
            list.add(options);
        }
        return list.toArray(new ProductOptions[list.size()]);
    }

    private static MarketingPreference[] marketingPreferences(final OfferProductDTO offerProductDTO) {
    final List<MarketingPreference> result = new ArrayList<MarketingPreference>();
    if (offerProductDTO.getMarketingPreferences() != null) {
        for (final MarketingPreferenceDTO marketingPreferenceDTO : offerProductDTO.getMarketingPreferences()) {
        final MarketingPreference marketingPreference = new MarketingPreference();
        marketingPreference.setEntitlementId(marketingPreferenceDTO.getEntitlementId());
        marketingPreference.setConsentOption(marketingPreferenceDTO.getConsentOption());
        result.add(marketingPreference);
        }
    }
    return result.toArray(new MarketingPreference[result.size()]);
    }

    private Channel initChannel() {
        final Channel channel = new Channel();
        if (null != session.getBranchContext()) {
            channel.setChannelCode("002");//
            channel.setSubChannelCode("003");//
        } else {
            channel.setChannelCode(INTERNET);//
            channel.setSubChannelCode(INTERNET_BANKING);//
        }

        return channel;
    }

    private Customer primaryInvolvedParty(final OfferProductDTO offerProductDTO) {
        final Customer customer = new Customer();
        customer.setPartyIdentifier("AAGATEWAY");
        customer.setIsPlayedBy(playedBy(offerProductDTO));//
        if (null != session.getBranchContext()) {
            customer.setUserType("1002");//
            final BranchContext branchContext = session.getBranchContext();
            customer.setInternalUserIdentifier(branchContext.getColleagueId());
        } else {
            customer.setUserType(CUSTOMER);//
            customer.setInternalUserIdentifier(INTERNAL_USER_IDENTIFIER);
        }
        customer.setPartyRole("0001");
        customer.setCustomerSegment("3");
        customer.setOtherBankDuration("0000");
        customer.setEmailAddress(offerProductDTO.emailAddress());
        customer.setTelephoneNumber(telephones(offerProductDTO));
        mapTaxResidencyDetails(offerProductDTO, customer);
        customer.setPostalAddress(address(offerProductDTO.currentAddress(), offerProductDTO.previousAddress()));

        if (null != offerProductDTO.getOcisId()) {
            customer.setCustomerIdentifier(offerProductDTO.getOcisId());
        }

        customer.setCidPersID(offerProductDTO.getPartyID());
        customer.setOutstandingMortgageAmount(offerProductDTO.getOverDraftLimit());
        return customer;
    }

    public Customer relatedInvolvedParty(final RelatedInvolvedParty relatedInvolvedParty) {
        final Customer customer = new Customer();
        customer.setPartyIdentifier("AAGATEWAY");
        customer.setIsPlayedBy(relatedPlayedBy(relatedInvolvedParty));//
        if (null != session.getBranchContext()) {
            customer.setUserType("1002");//
            final BranchContext branchContext = session.getBranchContext();
            customer.setInternalUserIdentifier(branchContext.getColleagueId());
        } else {
            customer.setUserType(CUSTOMER);//
            customer.setInternalUserIdentifier(INTERNAL_USER_IDENTIFIER);
        }
        customer.setPartyRole("0001");
        customer.setCustomerSegment("3");
        customer.setOtherBankDuration("0000");
        customer.setEmailAddress(relatedInvolvedParty.getEmail());
        relatedInvolvedParty.getHomePhone();
        relatedInvolvedParty.getWorkPhone();
        relatedInvolvedParty.getMobileNumber();
        final List<PhoneDTO> phoneNumbers = asList(commonUtils.phone(relatedInvolvedParty.getMobileNumber(), "Mobile"),
                commonUtils.phone(relatedInvolvedParty.getHomePhone(), "Fixed"),
                commonUtils.phone(relatedInvolvedParty.getWorkPhone(), "Work"));
        final List<TelephoneNumber> numbers = new ArrayList<TelephoneNumber>();
        customer.setTelephoneNumber(telephones(phoneNumbers, numbers));
        final PostalAddressComponentDTO currentAddress = commonUtils.relatedAddress(relatedInvolvedParty.getCurrentAddress(),
                "CURRENT");
        final PostalAddressComponentDTO previousAddress = commonUtils
                .relatedAddress(relatedInvolvedParty.getPreviousAddress(), "PREVIOUS");
        customer.setPostalAddress(relatedAddress(currentAddress, previousAddress));
        customer.setCidPersID(relatedInvolvedParty.getPartyId());
        customer.setOutstandingMortgageAmount(relatedInvolvedParty.getOverDraftLimit());

        if (null != relatedInvolvedParty.getOcisId()) {
            customer.setCustomerIdentifier(relatedInvolvedParty.getOcisId());
        }

        if (null != relatedInvolvedParty.getCbsCustomerNumber()) {
        	customer.setCbsCustomerNumber(relatedInvolvedParty.getCbsCustomerNumber());
        }
        return customer;
    }

    /**
     * Method to map the Tax Residency Details
     *
     * @param offerProductDTO
     * @param customer
     */
    private void mapTaxResidencyDetails(final OfferProductDTO offerProductDTO, final Customer customer) {

        Set<IdentificationDetails> identificationDtlsSet = null;
        Set<String> taxResidencyCountrySet = null;
        final Set<TaxResidencyDetailsDTO> taxResidencies = offerProductDTO.getTinDetails().getTaxResidencies();

        if (!CollectionUtils.isEmpty(taxResidencies)) {
            String tinNumber = null;
            taxResidencyCountrySet = new LinkedHashSet<String>();
            identificationDtlsSet = new LinkedHashSet<IdentificationDetails>();
            TaxResidencyDetailsDTO taxDetails = null;
            for (final Iterator<TaxResidencyDetailsDTO> it = taxResidencies.iterator(); it.hasNext();) {
                taxDetails = it.next();
                taxResidencyCountrySet.add(taxDetails.getTaxResidency());
                identificationDtlsSet.add(populateIdentificationDetails(taxDetails));
                /**
                 * This IF condition is made to support FATCA as well. For FATCA
                 * we will only get one tax resident(USA) and not more than
                 * that.
                 */
                if (FATCA.equalsIgnoreCase(taxDetails.getTaxResidencyType())) {
                    tinNumber = taxDetails.getTinNumber();
                }
            }
            customer.setTaxResidencyDetails(populateTaxResidencyCountries(tinNumber, taxResidencyCountrySet));
            customer.setIdentificationDetails(
                    identificationDtlsSet.toArray(new IdentificationDetails[identificationDtlsSet.size()]));
        }

    }

    private PostalAddress[] address(final PostalAddressComponentDTO... dtos) {
        final List<PostalAddress> postalAddresses = new ArrayList<PostalAddress>();
        for (final PostalAddressComponentDTO dto : dtos) {
            if (dto == null) {
                continue;
            }
            final PostalAddress postalAddress = createPostalAddress(dto);
            postalAddresses.add(postalAddress);
            if (dto.isStructured()) {
                postalAddress.setStructuredAddress(createStructuredAddress(dto));
            } else {

                if (null != dto.unstructured()) {
                    postalAddress.setIsPAFFormat(Boolean.FALSE);
                    postalAddress.setUnstructuredAddress(createUnstructuredAddress(dto, false));

                }
            }
        }

        return postalAddresses.toArray(new PostalAddress[postalAddresses.size()]);
    }

    private PostalAddress[] relatedAddress(final PostalAddressComponentDTO... dtos) {
        final List<PostalAddress> postalAddresses = new ArrayList<PostalAddress>();
        for (final PostalAddressComponentDTO dto : dtos) {
            if (dto == null) {
                continue;
            }
            final PostalAddress postalAddress = createPostalAddress(dto);
            postalAddresses.add(postalAddress);
            if (dto.isStructured()) {
                postalAddress.setStructuredAddress(createStructuredAddress(dto));
            } else {

                if (null != dto.unstructured()) {
                    postalAddress.setIsPAFFormat(Boolean.FALSE);
                    postalAddress.setUnstructuredAddress(createUnstructuredAddress(dto, true));

                }
            }
        }

        return postalAddresses.toArray(new PostalAddress[postalAddresses.size()]);
    }

    private PostalAddress createPostalAddress(final PostalAddressComponentDTO dto) {
        final PostalAddress postalAddress = new PostalAddress();
        postalAddress.setStatusCode(dto.addressStatus());
        postalAddress.setDurationofStay(dto.durationOfStay());
        postalAddress.setIsBFPOAddress(dto.isBFPOAddress());
        postalAddress.setIsPAFFormat(dto.isPAFFormat());

        return postalAddress;
    }

    private StructuredAddress createStructuredAddress(final PostalAddressComponentDTO dto) {
        final StructuredAddress structuredAddress = new StructuredAddress();
        final StructuredPostalAddressDTO structured = dto.structured();
        structuredAddress.setDistrict(structured.district());
        structuredAddress.setPostTown(structured.town());
        structuredAddress.setCounty(structured.county());
        structuredAddress.setOrganisation(structured.organisation());
        structuredAddress.setSubBuilding(structured.subBuilding());
        structuredAddress.setBuilding(structured.building());
        structuredAddress.setBuildingNumber(structured.buildingNumber());
        structuredAddress.setHouseNumber(structured.buildingNumber());
        structuredAddress
                .setAddressLinePAFData(structured.addressLines().toArray(new String[structured.addressLines().size()]));
        final String postcode = dto.postcode().replace(" ", "");
        structuredAddress.setPostCodeIn(postcode.substring(postcode.length() - 3));
        structuredAddress.setPostCodeOut(postcode.substring(0, postcode.length() - 3));
        structuredAddress.setPointSuffix(dto.deliveryPointSuffix());
        return structuredAddress;
    }

    private UnstructuredAddress createUnstructuredAddress(final PostalAddressComponentDTO dto, final boolean isRelatedParty) {

        final UnstructuredAddress unstructuredAddress = new UnstructuredAddress();
        final UnstructuredPostalAddressDTO unstructured = dto.unstructured();
        populateUnstucturedAddress(unstructured, unstructuredAddress, dto);
        updateUnstructuredAddressLine8(unstructured, unstructuredAddress, isRelatedParty);
        return unstructuredAddress;

    }

    private void updateUnstructuredAddressLine8(final UnstructuredPostalAddressDTO unstructured,
            final UnstructuredAddress unstructuredAddress, final boolean isRelatedParty) {

        if (isNullorBlank(unstructured.unstructuredAddressLine(4))
                && (isRelatedParty || isAuthJourney() || isExistingAddParty())) {
            unstructuredAddress.setAddressLine8(UNITED_KINGDOM);
        }
    }

    private boolean isNullorBlank(final String addressLine) {
        return null == addressLine || StringUtils.isEmpty(addressLine);
    }

    private boolean isExistingAddParty() {
        return session.getAddPartyContext() != null && session.getAddPartyContext().isExistingParty();
    }

    private boolean isAuthJourney() {
        return null != session.getUserInfo() && session.getAddPartyContext() == null;

    }

    private void populateUnstucturedAddress(final UnstructuredPostalAddressDTO unstructured,
            final UnstructuredAddress unstructuredAddress, final PostalAddressComponentDTO dto) {
        unstructuredAddress.setAddressLine1(unstructured.unstructuredAddressLine(0));
        unstructuredAddress.setAddressLine2(unstructured.unstructuredAddressLine(1));
        unstructuredAddress.setAddressLine3(unstructured.unstructuredAddressLine(2));
        unstructuredAddress.setAddressLine4(unstructured.unstructuredAddressLine(3));
        unstructuredAddress.setAddressLine8(unstructured.unstructuredAddressLine(4));
        unstructuredAddress.setPostCode(dto.postcode());
        unstructuredAddress.setPointSuffix(dto.deliveryPointSuffix());

    }

    /**
     * Method to populate tax residency country
     *
     * @return TaxResidencyDetails The tin number gets set only for FATCA
     *         country USA and not for CRS.
     */

    private TaxResidencyDetails populateTaxResidencyCountries(final String tinNumber, final Set<String> taxResidencyCountrySet) {

        final TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        if (tinNumber != null) {
            taxResidencyDetails.setTaxPayerIdNumber(tinNumber);
        }
        taxResidencyDetails
                .setTaxResidencyCountries((taxResidencyCountrySet.toArray(new String[taxResidencyCountrySet.size()])));

        return taxResidencyDetails;
    }

    /**
     * Method to populate tax residency details for CRS
     *
     * @param taxDetails
     * @return IdentificationDetails
     */
    private IdentificationDetails populateIdentificationDetails(final TaxResidencyDetailsDTO taxDetails) {

        final IdentificationDetails identificationDtls = new IdentificationDetails();
        identificationDtls.setValue(taxDetails.getTinNumber());
        identificationDtls.setType(IDENTIFICATION_DTLS_TYPE);
        identificationDtls.setCountryCode(taxDetails.getTaxResidency());
        return identificationDtls;
    }

    private Individual playedBy(final OfferProductDTO offerProductDTO) {
        final Individual individual = new Individual();

        individual.setAnticipateDateOfGraduation(offerProductDTO.getAnticipateDateOfGraduation());
        individual.setCurrentYearOfStudy(offerProductDTO.getCurrentYearOfStudy());

        individual.setIndividualName(new IndividualName[] { individualName(offerProductDTO) });//
        individual.setBirthDate(calendar(offerProductDTO.birthDate()));
        individual.setGender(offerProductDTO.gender());
        individual.setMaritalStatus(offerProductDTO.maritalStatus());
        individual.setResidentialStatus(offerProductDTO.residentialStatus());
        individual.setNumberOfDependents(offerProductDTO.numberOfDependants());//

        individual.setCountryOfBirth(getBirthCountryName(offerProductDTO.getTinDetails().getBirthCountry()));
        final Set<String> nationalities = offerProductDTO.getTinDetails().getNationalities();
        if (!CollectionUtils.isEmpty(nationalities)) {
            populateCurrentAndPreviousNationalities(nationalities, individual);
        }
        individual.setUKResidenceStartDate(offerProductDTO.ukResidenceStartDate() == null ? null
                : calendar(offerProductDTO.ukResidenceStartDate()));
        individual.setVisaExpiryDate(
                offerProductDTO.visaExpiryDate() == null ? null : calendar(offerProductDTO.visaExpiryDate()));
        individual.setNetMonthlyIncome(currency(offerProductDTO.netMonthlyIncome()));//
        individual.setEmploymentStatus(offerProductDTO.employment().employmentStatus());
        individual.setOccupation(offerProductDTO.employment().occupation());
        employer(individual, offerProductDTO);
        if (offerProductDTO.monthlyMortgageAmount() != null) {
            individual.setMonthlyMortgageAmount(currency(offerProductDTO.monthlyMortgageAmount()));//
        }
        if (offerProductDTO.monthlyLoanRepaymentAmount() != null) {
            individual.setMonthlyLoanRepaymentAmount(currency(offerProductDTO.monthlyLoanRepaymentAmount()));
        }
        individual.setTotalSavingsAmount(currency(offerProductDTO.savingsAmount()));
        individual.setPlaceOfBirth(offerProductDTO.getBirthCity());
        if (AccountType.CA.toString().equalsIgnoreCase(offerProductDTO.getAccType())
                && offerProductDTO.getThreatMatrixDTO() != null) {
            final CustomerDeviceDetails aCustomerDeviceDetails = customerDeviceDetails(offerProductDTO.getThreatMatrixDTO());
            aCustomerDeviceDetails.setWorkFlowName(getSIRAWorkflowName(offerProductDTO));
            individual.setCustomerDeviceDetails(aCustomerDeviceDetails);
        }
        return individual;
    }

    private Individual relatedPlayedBy(final RelatedInvolvedParty relatedInvolvedParty) {
        final Individual individual = new Individual();

        individual.setAnticipateDateOfGraduation(relatedInvolvedParty.getAnticipateDateOfGraduation());
        individual.setCurrentYearOfStudy(relatedInvolvedParty.getCurrentYearOfStudy());

        individual.setIndividualName(new IndividualName[] { individualName(relatedInvolvedParty) });//
        individual.setBirthDate(calendar(relatedInvolvedParty.getDob()));
        individual.setGender(relatedInvolvedParty.getGender());
        individual.setMaritalStatus(relatedInvolvedParty.getMaritalStatus());
        individual.setResidentialStatus(relatedInvolvedParty.getResidentialStatus());
        BigInteger numberOfDependents = null;
        if (relatedInvolvedParty.getNumberOfDependents() != null) {
            numberOfDependents = new BigInteger(relatedInvolvedParty.getNumberOfDependents().toString());
        }
        individual.setNumberOfDependents(numberOfDependents);//

        final TinDetails tinDetails = relatedInvolvedParty.getTinDetails();
        if (tinDetails != null) {
            individual.setCountryOfBirth(getBirthCountryName(tinDetails.getBirthCountry()));
            final Set<String> nationalities = tinDetails.getNationalities();
            if (!CollectionUtils.isEmpty(nationalities)) {
                populateCurrentAndPreviousNationalities(nationalities, individual);
            }
        }

        individual.setUKResidenceStartDate(relatedInvolvedParty.getUkArrivalDate() == null ? null
                : calendar(relatedInvolvedParty.getUkArrivalDate()));
        individual.setVisaExpiryDate(relatedInvolvedParty.getVisaExpiryDate() == null ? null
                : calendar(relatedInvolvedParty.getVisaExpiryDate()));
        final BigDecimal monthlyIncome = new BigDecimal(commonUtils.defaultZero(relatedInvolvedParty.getIncome()));
        individual.setNetMonthlyIncome(currency(monthlyIncome));//
        individual.setEmploymentStatus(relatedInvolvedParty.getEmploymentStatus());
        individual.setOccupation(relatedInvolvedParty.getOccupnType());
        // Not required for relatedParty
        // employer(individual, offerProductDTO);

        final Integer monthlyMortgageAmount = relatedInvolvedParty.getRentMortgCost();
        BigDecimal monMortgageAmount = null;
        if (monthlyMortgageAmount != null) {
            monMortgageAmount = new BigDecimal(monthlyMortgageAmount);
            individual.setMonthlyMortgageAmount(currency(monMortgageAmount));
        }

        final Integer maintnCost = relatedInvolvedParty.getMaintnCost();
        BigDecimal maintnCostAmount = null;
        if (maintnCost != null) {
            maintnCostAmount = new BigDecimal(maintnCost);
            individual.setMonthlyLoanRepaymentAmount(currency(maintnCostAmount));
        }

        final Integer savingAmnt = relatedInvolvedParty.getSavingsAmount();
        BigDecimal savingAmount = null;
        if (savingAmnt != null) {
            savingAmount = new BigDecimal(savingAmnt);
            individual.setTotalSavingsAmount(currency(savingAmount));
        }

        individual.setPlaceOfBirth(relatedInvolvedParty.getBirthCity());
        /*
         * if (AccountType.CA.toString().equalsIgnoreCase(offerProductDTO.
         * getAccType()) && offerProductDTO.getThreatMatrixDTO() != null) {
         * CustomerDeviceDetails aCustomerDeviceDetails =
         * customerDeviceDetails(offerProductDTO.getThreatMatrixDTO());
         * aCustomerDeviceDetails.setWorkFlowName(getSIRAWorkflowName(
         * offerProductDTO));
         * individual.setCustomerDeviceDetails(aCustomerDeviceDetails); }
         */
        return individual;
    }

    private String getBirthCountryName(final String countryCode) {
        final String birthCountryName = referenceDataService.getCountryName(countryCode);
        if (birthCountryName != null) {
            return birthCountryName;
        } else {
            return countryCode;
        }

    }

    /**
     * Method to populate current & previous nationalities
     *
     * @param set
     * @param individual
     */
    private void populateCurrentAndPreviousNationalities(final Set<String> set, final Individual individual) {
        String nationality = null;
        final List<String> nationalities = new ArrayList<String>();
        int counter = 0;
        for (final Iterator<String> it = set.iterator(); it.hasNext();) {
            nationality = it.next();
            counter++;
            if (counter == 1) {
                individual.setNationality(nationality);
            } else {
                nationalities.add(nationality);
            }
        }
        if (!CollectionUtils.isEmpty(nationalities)) {
            individual.setPreviousNationalities(nationalities.toArray(new String[nationalities.size()]));
        }
    }

    private TelephoneNumber[] telephones(final OfferProductDTO offerProductDTO) {
        final List<TelephoneNumber> numbers = new ArrayList<TelephoneNumber>();
        return telephones(
                asList(offerProductDTO.mobilePhone(), offerProductDTO.homePhone(), offerProductDTO.workPhone()),
                numbers);
    }

    private TelephoneNumber[] telephones(final List<PhoneDTO> phoneNumbers, final List<TelephoneNumber> numbers) {
        for (final PhoneDTO phone : phoneNumbers) {
            if (phone == null) {
                continue;
            }
            final TelephoneNumber telephoneNumber = new TelephoneNumber();
            telephoneNumber.setCountryPhoneCode(phone.countryCode());
            if (notNullAndNotEmpty(phone.telephoneAreaCode())) {
                telephoneNumber.setAreaCode(phone.telephoneAreaCode());
            }
            telephoneNumber.setPhoneNumber(phone.phoneNumber());
            if (notNullAndNotEmpty(phone.getExtNumber())) {
                telephoneNumber.setExtensionNumber(phone.getExtNumber());
            }
            telephoneNumber.setDeviceType(phone.type());
            telephoneNumber.setTelephoneType(PHONE_DEVICE_TYPE_MAP.get(phone.type()));
            numbers.add(telephoneNumber);
        }
        return numbers.toArray(new TelephoneNumber[numbers.size()]);

    }

    private String resolveDuration(final OfferProductDTO offerProductDTO) {
        String year = offerProductDTO.employment().details().currentEmploymentYear();
        String month = offerProductDTO.employment().details().currentEmploymentMonth();
        final StringBuffer duration = new StringBuffer();

        if (year != null) {
            year = year.length() == 1 ? "0" + year : year;
            duration.append(year);
        }
        if (month != null) {
            month = month.length() == 1 ? "0" + month : month;
            duration.append(month);
        }

        return duration.toString();
    }

    private void employer(final Individual individual, final OfferProductDTO offerProductDTO) {
        final EmploymentDTO employment = offerProductDTO.employment();
        String employmentDuration = "0000";
        if (employment.details() != null) {
            final Employer employer = new Employer();
            employer.setName(employment.details().employerName());
            final PostalAddress postalAddress = new PostalAddress();
            final UnstructuredAddress unstructuredAddress = new UnstructuredAddress();
            unstructuredAddress.setAddressLine1(employment.details().employerAddressLine1());
            unstructuredAddress.setAddressLine2(employment.details().employerAddressLine2());
            unstructuredAddress.setPostCode(employment.details().employerPostcode());
            postalAddress.setUnstructuredAddress(unstructuredAddress);
            postalAddress.setIsBFPOAddress(false);
            postalAddress.setIsPAFFormat(false);
            employer.setHasPostalAddress(new PostalAddress[] { postalAddress });
            individual.setCurrentEmployer(employer);
            employmentDuration = resolveDuration(offerProductDTO);
        }
        individual.setCurrentEmploymentDuration(employmentDuration);
    }

    private CurrencyAmount currency(final BigDecimal currency) {
        final CurrencyAmount currencyAmount = new CurrencyAmount();
        currencyAmount.setAmount(currency);
        return currencyAmount;
    }

    private IndividualName individualName(final OfferProductDTO offerProductDTO) {
        final IndividualName name = new IndividualName();
        name.setPrefixTitle(offerProductDTO.prefixTitleName());//
        name.setFirstName(offerProductDTO.firstName());//
        name.setLastName(offerProductDTO.lastName());//
        if (notNullAndNotEmpty(offerProductDTO.middleName())) {
            name.setMiddleNames(new String[] { offerProductDTO.middleName() });
        } // optional
          // in
          // the
          // request
        return name;
    }

    private IndividualName individualName(final RelatedInvolvedParty relatedInvolvedParty) {
        final IndividualName name = new IndividualName();
        name.setPrefixTitle(relatedInvolvedParty.getTitle());//
        name.setFirstName(relatedInvolvedParty.getFirstName());//
        name.setLastName(relatedInvolvedParty.getLastName());//
        if (notNullAndNotEmpty(relatedInvolvedParty.getMiddleName())) {
            name.setMiddleNames(new String[] { relatedInvolvedParty.getMiddleName() });
        } // optional
          // in
          // the
          // request
        return name;
    }

    private Calendar calendar(final Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    private boolean notNullAndNotEmpty(final String value) {
        return value != null && !EMPTY_STRING.equals(value.replace(" ", ""));
    }

    private CustomerDeviceDetails customerDeviceDetails(final ThreatMatrixDTO threatMatrixDTO) {
        if (threatMatrixDTO != null) {
            final CustomerDeviceDetails aCustomerDeviceDetails = new CustomerDeviceDetails();
            aCustomerDeviceDetails.setAccountLogin(threatMatrixDTO.getAccountLogin());
            aCustomerDeviceDetails.setBrowserLanguage(threatMatrixDTO.getBrowserLanguage());
            aCustomerDeviceDetails.setDeviceFirstSeen(threatMatrixDTO.getDeviceFirstSeen());
            aCustomerDeviceDetails.setDeviceLastEvent(threatMatrixDTO.getDevicelastSeen());
            aCustomerDeviceDetails.setDnsIPGeo(threatMatrixDTO.getDnsIpGeo());
            aCustomerDeviceDetails.setExactDeviceId(threatMatrixDTO.getDeviceId());
            aCustomerDeviceDetails.setProxyIpGeo(threatMatrixDTO.getProxyIpGeo());
            aCustomerDeviceDetails.setSmartDeviceId(threatMatrixDTO.getSmartDeviceId());
            aCustomerDeviceDetails.setSmartDeviceIdConfidence(threatMatrixDTO.getSmartDeviceIdConfidence());
            aCustomerDeviceDetails.setTmxPolicyScore(threatMatrixDTO.getPolicyScore());
            aCustomerDeviceDetails.setTmxReasonCode(threatMatrixDTO.getReasonCode());
            aCustomerDeviceDetails.setTmxReviewStatus(threatMatrixDTO.getReviewStatus());
            aCustomerDeviceDetails.setTmxRiskRating(threatMatrixDTO.getRiskRating());
            aCustomerDeviceDetails.setTmxSummaryReasonCode(threatMatrixDTO.getSummaryReasonCode());
            aCustomerDeviceDetails.setTmxSummaryRiskScore(threatMatrixDTO.getSummartRiskScore());
            aCustomerDeviceDetails.setTrueIp(threatMatrixDTO.getTrueIp());
            aCustomerDeviceDetails.setTrueIpGeo(threatMatrixDTO.getTrueIpGeo());
            aCustomerDeviceDetails.setTrueIpIsp(threatMatrixDTO.getTrueIpIsp());
            aCustomerDeviceDetails.setTrueIpOrganization(threatMatrixDTO.getTrueIpOrgnaisation());
            return aCustomerDeviceDetails;
        }

        return null;
    }

    public String getSIRAWorkflowName(final OfferProductDTO offerProductDTO) {
        String workFlowName = null;
        final DAOResponse<ChannelBrandDTO> channel = channelBrandingService.getChannelBrand();
        final String brand = channel.getResult().getBrand();
        if (brand != null && (BrandValue.LLOYDS.getBrand()).equalsIgnoreCase(brand)) {
            workFlowName = SIRA_WORKFLOW_NAME_LTB;
        } else {
            workFlowName = SIRA_WORKFLOW_NAME_HBOS;
        }
        offerProductDTO.setSiraWorkFlowName(workFlowName);
        return workFlowName;
    }
}
