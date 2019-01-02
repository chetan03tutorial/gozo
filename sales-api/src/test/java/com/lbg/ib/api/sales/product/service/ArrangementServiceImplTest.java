/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.product.domain.arrangement.ASMScoreType.findASMTypeFromCode;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.lbg.ib.api.sales.common.domain.BankHolidaysHolder;
import com.lbg.ib.api.sales.common.service.BankHolidayService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.address.service.AddressService;
import com.lbg.ib.api.sales.common.auditing.FraudAuditor;
import com.lbg.ib.api.sales.common.auditing.ThreatAuditor;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
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
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;
import com.lbg.ib.api.sales.dto.product.offer.ASMScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.EIDVScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.RelatedApplicationDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.dto.product.offer.TaxResidencyDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.TinDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.StructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.UnstructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreference;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.product.domain.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.ProductArrangementCondition;
import com.lbg.ib.api.sales.product.domain.arrangement.RelatedApplication;
import com.lbg.ib.api.sales.product.domain.arrangement.RelatedInvolvedParty;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lloydstsb.ea.application.switching.GlobalApplicationSwitches;
import com.lloydstsb.ea.channel.ChannelService;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;

public class ArrangementServiceImplTest {

    public static final PhoneDTO                  MOBILE_PHONE                              = new PhoneDTO("1", "2",
            "3", "4", "Mobile");

    public static final PhoneDTO                  HOME_PHONE                                = new PhoneDTO("1", "2",
            "3", "4", "Fixed");

    public static final PhoneDTO                  WORK_PHONE                                = new PhoneDTO("1", "2",
            "3", "4", "Work");

    public static final PostalAddressComponentDTO CURRENT_ADDRESS                           = new PostalAddressComponentDTO(
            new StructuredPostalAddressDTO("district", "town", "county", "organisation", "subBuilding", "building",
                    "buildingNumber", asList("addressLines")),
            "1010", "CURRENT", "suffix", "E201BF", false, false);

    public static final PostalAddressComponentDTO PREV_ADDRESS                              = new PostalAddressComponentDTO(
            new UnstructuredPostalAddressDTO("addressLine1", "addressLine2", "addressLine3", "addressLine4",
                    "addressLine5", "addressLine6", "addressLine7", "addressLine8"),
            "1010", "PREVIOUS", "deliveryPointSuffix", "E201BF", false, false);

    private static final Date                     BIRTH_DATE                                = new Date(0);

    private static final Date                     UK_RES_DATE                               = new Date(1);

    private static final Date                     UK_VISA_DATE                              = new Date(2);

    public static final String                    EMPLOYMENT_STATUS_RETIRED                 = "006";

    public static final EmployerDetails           EMPLOYER                                  = new EmployerDetails(
            "employerName", null, null, null, 0, 0);

    private static final EmploymentDTO            EMPLOYMENT                                = new EmploymentDTO(
            "occupation", "employmentStatus", "employerName", null, null, null, "0", "0");

    private static final ChannelBrandDTO          CHANNEL                                   = new ChannelBrandDTO("ch",
            "br", "cid");

    public static final ResponseError             RESPONSE_ERROR                            = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");

    public static final RuntimeException          RUNTIME_EXCEPTION                         = new RuntimeException();

    private final AddressService                        addressService                            = mock(
            AddressService.class);

    private final GalaxyErrorCodeResolver         resolver                                  = mock(
            GalaxyErrorCodeResolver.class);

    private final OfferProductDAO                       offerProductDAO                           = mock(
            OfferProductDAO.class);

    private final EligibiltyDAO                         customerEligibiltyDAO                     = mock(EligibiltyDAO.class);

    private final OverdraftDAO                          overdraftDAO                              = mock(OverdraftDAO.class);

    private final LoggerDAO                             logger                                    = mock(LoggerDAO.class);

    private final ChannelBrandingDAO                    channelService                            = mock(
            ChannelBrandingDAO.class);

    private LoggingServiceFactory                 aLoggingServiceFactory;

    private final ConfigurationDAO                      configurationService                      = mock(
            ConfigurationDAO.class);

    private final BankHolidayService bankHolidayService                   = mock(
            BankHolidayService.class);

    private final BankHolidaysHolder bankHolidaysHolder                   = mock(
            BankHolidaysHolder.class);

    private final CrossSellEligibilityDAO               crossSellEligibilityDAO                   = mock(
            CrossSellEligibilityDAO.class);

    private final PartyRegistrationDAO                  partyDAO                                  = mock(
            PartyRegistrationDAO.class);

    private final SessionManagementDAO                  session                                   = mock(
            SessionManagementDAO.class);

    private final ThreatAuditor                         auditor                                   = mock(ThreatAuditor.class);

    private final FraudAuditor                          fraudAuditor                              = mock(FraudAuditor.class);

    private final ThreatMatrixDAO                       threatMatrixDAO                           = mock(
            ThreatMatrixDAO.class);

    private static ChannelService                 CHANNEL_SERVICE                           = mock(
            ChannelService.class);

    private static ThreatMatrixDTO                threatMatrixDTO                           = mock(
            ThreatMatrixDTO.class);

    private final SIRAScoreDTO                          siraScoreDTO                              = mock(SIRAScoreDTO.class);

    private final UserContext                           userContext                               = mock(UserContext.class);

    private final SwitchesManagementDAO                 switchesDAO                               = mock(
            SwitchesManagementDAO.class);

    private final DAOResponse<Map<String, String>>      tmResults                                 = mock(DAOResponse.class);

    private final DAOResponse<HashMap<String, Boolean>> switchResult                              = mock(DAOResponse.class);

    @InjectMocks
    private GlobalApplicationSwitches             aGlobalApplicationSwitches;

    private static ExternalProductIdentifier[]    externalProductIdentifiers                = new ExternalProductIdentifier[] {
            new ExternalProductIdentifier("epic", "epii") };

    private static PostalAddressComponent         currentAddress                            = new PostalAddressComponent(
            "1010", false, false, new PostalAddress("district", "town", "county", "organisation", "subBuilding",
                    "building", "buildingNumber", asList("addressLines"), "E201BF", "suffix"));

    private static PostalAddressComponent         previousAddress                           = new PostalAddressComponent(
            "1010", false, false,
            new UnstructuredPostalAddress("addressLine1", "addressLine2", "addressLine3", "addressLine4",
                    "addressLine5", "addressLine6", "addressLine7", "addressLine8", "E201BF", "deliveryPointSuffix"));

    private static ContactNumber                  mobileNumber                              = new ContactNumber("1",
            "2", "3", "4");

    private static ContactNumber                  workNumber                                = new ContactNumber("1",
            "2", "3", "4");

    private static ContactNumber                  homeNumber                                = new ContactNumber("1",
            "2", "3", "4");

    private static final ProductArrangement       productArrangement                        = new ProductArrangement(
            "pname", "mnemonic", asList(new ProductArrangementCondition("cname", 1, "cvalue")));

    private final PrimaryInvolvedParty                  primaryInvolvedParty                      = primaryInvolvedParty(
            SwitchOptions.No, false);

    private final PrimaryInvolvedParty                  primaryInvolvedPartyYesSwitchYesOverdraft = primaryInvolvedParty(
            SwitchOptions.Yes, true);

    private final PrimaryInvolvedParty                  primaryInvolvedPartyYesSwitch             = primaryInvolvedParty(
            SwitchOptions.Yes, false);

    private final PrimaryInvolvedParty                  primaryInvolvedPartyYesOverdraft          = primaryInvolvedParty(
            SwitchOptions.No, true);

    private PrimaryInvolvedParty primaryInvolvedParty(final SwitchOptions option, final boolean intendOverDraft) {
        return new PrimaryInvolvedParty(option, intendOverDraft, 200, "prefixTitleName", "firstName", "middleName",
                "lastName", "emailAddress", BIRTH_DATE, "gender", currentAddress, previousAddress, "maritalStatus",
                "residentialStatus", 1, "fundingSource", "accountPurpose", mobileNumber, workNumber, homeNumber, false,
                true, false, true, UK_RES_DATE, UK_VISA_DATE, false, 2, "employmentStatus", "occupation", EMPLOYER, 3,
                1, 4, 0, 0, null,null);
    }

    private final Arrangement                  arrangementRequested                      = new Arrangement(
            primaryInvolvedParty, productArrangement, null);

    private final Arrangement                  arrangementRequestedYesOverdraft          = new Arrangement(
            primaryInvolvedPartyYesOverdraft, productArrangement, null);

    private final Arrangement                  arrangementRequestedYesSwitchYesOverdraft = new Arrangement(
            primaryInvolvedPartyYesSwitchYesOverdraft, productArrangement, null);

    private final Arrangement                  arrangementRequestedYesSwitch             = new Arrangement(
            primaryInvolvedPartyYesSwitch, productArrangement, null);

    public static final String           PID                                       = "pid";

    private static final OfferProductDTO offerProductDtoYesIntendToSwitch          = offerProductDTO(SwitchOptions.Yes,
            false, null);

    private static final OfferProductDTO offerProductDtoNoIntendToSwitch           = offerProductDTO(SwitchOptions.No,
            false, null);

    private static final OfferProductDTO offerProductDtoForSwitchLater             = offerProductDTO(
            SwitchOptions.Later, false, null);

    private static final OfferProductDTO offerProductDtoForRelatedProduct          = offerProductDTO(SwitchOptions.No,
            false, relatedApp());

    private static final ResponseError   CUSTOM_IB_ERROR                           = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Customer Msg");

    private static RelatedApplicationDTO relatedApp() {
        final RelatedApplicationDTO relatedApp = new RelatedApplicationDTO();
        relatedApp.setApplicationId("relAppId");
        relatedApp.setApplicationStatus("relAppStatus");
        return relatedApp;
    }

    private static OfferProductDTO offerProductDTO(final SwitchOptions option, final boolean intendOverDraft,
            final RelatedApplicationDTO relatedApplication) {
    return new OfferProductDTO(
        option,
        intendOverDraft,
        200,
        "prefixTitleName",
        "firstName",
        "middleName",
        "lastName",
        "emailAddress",
        MOBILE_PHONE,
        HOME_PHONE,
        WORK_PHONE,
        CURRENT_ADDRESS,
        PREV_ADDRESS,
        "gender",
        BIRTH_DATE,
        "maritalStatus",
        new BigInteger("1"),
        EMPLOYMENT,
        new BigDecimal("2"),
        "residentialStatus",
        UK_RES_DATE,
        UK_VISA_DATE,
        true,
        true,
        false,
        false,
        new BigDecimal("3"),
        new BigDecimal("1"),
        new BigDecimal("4"),
        "fundingSource",
        "accountPurpose",
        PID,
        new HashMap<String, String>() {
            {
            put("cname", "cvalue");
            }
        },
        new HashMap<String, String>() {
            {
            put("epic", "epii");
            }
        },
        "pname",
        "mnemonic",
        relatedApplication,
        new ThreatMatrixDTO(),
        Collections.<MarketingPreferenceDTO>emptyList(),"100");
    }

    private EIDVScoreDTO                  eidvScoreDTO                                      = new EIDVScoreDTO("Accept",
            "assessmentType", "evidenceIdentifier", "identityStrength", "code", "description");

    private ASMScoreDTO                   asmScoreDTO                                       = new ASMScoreDTO("1",
            "assessmentType", "evidenceIdentifier", "identityStrength", "601", "description");

    private ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "otype",
        new HashMap<String, String>(),
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        "1001776000",
        null,
        "pdtFamilyId",
        null,
        siraScoreDTO,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());

    private static final Arranged arranged = new Arranged("pname",
        "mnemonic",
        "appStatus",
        "appSubStatus",
        "arrangementId",
        "offerType",
        asList(new ProductAttributes("n", "v")),
        asList(new Condition("n", 1, "v")),
        Collections.<MarketingPreference>emptyList());

    private SelectedProduct               selectedProduct                                   = new SelectedProduct("n",
            PID, "pdtFamilyId", externalProductIdentifiers, "P_CLASSIC", null, null);

    private final ArrangementServiceImpl        classUnderTest                                    = new ArrangementServiceImpl(
            offerProductDAO, session, resolver, logger, customerEligibiltyDAO, overdraftDAO, channelService,
            configurationService, crossSellEligibilityDAO, partyDAO, auditor, fraudAuditor,
            threatMatrixDAO, switchesDAO, new CommonUtils(session, addressService), addressService, bankHolidayService);

    private Arrangement                   arrangement;

    private final Set<String>           holidayList                                       = new HashSet<String>();

    private final PartyResponseDTO              partyResponseDTO                                  = new PartyResponseDTO(
            "pwdState", asList(new ChannelDTO("channelId", "channleDescription")), true);

    private final DAOResponse<PartyResponseDTO> partyResponseDTOIfCustomerHasNoEligibleAccounts   = DAOResponse
            .withError(errorMappindForMandate("131091", "Customer has no eligible accounts"));

    private final DAOResponse<PartyResponseDTO> partyResponseDTOWithMandateIfCustomerHasMandate   = DAOResponse
            .withError(errorMappindForMandate("131089", "Customer already has mandate for channel"));

    private final DAOResponse<PartyResponseDTO> partyResponseDTOWithMandateIfCustomerHasNoMacthes = DAOResponse
            .withError(errorMappindForMandate("131085", "Customer already has no matches"));

    private final DAOResponse<PartyResponseDTO> partyResponseDTOWithMandateIfCustomerHasNoParties = DAOResponse
            .withError(errorMappindForMandate("131084", "Customer already has no parties"));

    private ArrangeToActivateParameters   params;

    @Before
    public void setUp() throws Exception {
        holidayList.add("01-JAN-15 00.00.00.000000000");
        arrangementRequested.setAccountType(AccountType.CA);

        params = new ArrangeToActivateParameters();
        params.setArrangementType("arrangementType");
        params.setApplicationType("applicationType");
        params.setOcisId("ocis");
        params.setPartyId("party");
        params.setProductName("pname");
        params.setProductMnemonic("mnemonic");
        params.setProductId("1001776000");
        params.setProductFamilyIdentifier("pdtFamilyId");
        params.setAppStatus("appStatus");
        params.setEidvStatus("Accept");
        params.setArrangementId("arrangementId");
        params.setAmtOverdraft(new BigDecimal("200"));
        params.setOfferedProductId("1001776000");
        params.setOfferedProductName("pname");
        params.setOfferedProductMnemonic("mnemonic");


        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(tmResults.getResult()).thenReturn(prepareDaoResponseTest());
        when(threatMatrixDAO.retrieveThreadMatrixResults("", ThreatAuditor.CURRENT_ACCOUNT_APPLICATION_TYPE, ""))
                .thenReturn(tmResults);
        when(bankHolidayService.getBankHolidaysList()).thenReturn(bankHolidaysHolder);
    }

    @Test
    public void shouldReturnArrangementInformationWhenDAOReturnsWithArrangementIdAndSetParametersInSession()
            throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        validate(testResult);
    }

    @Test(expected = ServiceException.class)
    public void testArrangeWhenselectedNoProductInSession() throws ServiceException {
        when(session.getSelectedProduct()).thenReturn(null);
        when(resolver.resolve("9210002")).thenReturn(RESPONSE_ERROR);
        // this shoud throw a service exception
        classUnderTest.arrange(arrangementRequested);
    }

    @Test
    public void testArrangeForSavingAccountType() throws Exception {
        arrangementRequested.setAccountType(AccountType.SA);
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        validate(testResult);
    }

    @Test
    public void testArrangeForCurrentAccoutForSiraSwitchStatusAsTrue() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoYesIntendToSwitch);
        when(switchesDAO.getSwitches("cid")).thenReturn(switchResult);
        final HashMap<String, Boolean> globalApplicationSwitches = new HashMap<String, Boolean>();
        globalApplicationSwitches.put("SW_EnSIRAFrdChk", true);
        when(switchResult.getResult()).thenReturn(globalApplicationSwitches);
        // offerProductDtoYesIntendToSwitch.setSiraWorkFlowName("ABC");
        Mockito.doNothing().when(siraScoreDTO).setSiraWorkFlowName(Matchers.anyString());
        Mockito.doNothing().when(fraudAuditor).audit(productOfferedDTO);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        validate(testResult);
    }

    @Test
    public void testArrangeForApprovedApplicationStatusWithVantageEligibility() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        productOfferedDTO.setApplicationStatus("1002");
        params.setAppStatus("1002");
        final Map<String, String> productOptions = new HashMap<String, String>();
        productOptions.put("Related Vantage Mnemonic", "Related Vantage product");
        productOfferedDTO.setProductOptions(productOptions);
        final HashMap<String, String> result = new HashMap();
        result.put("Related Vantage product", "true");
        when(customerEligibiltyDAO.determineEligibleCustomerInstructions(Matchers.any(EligibilityRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(result));
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertThat(testResult.getApplicationStatus(), is("1002"));

    }

    @Test
    public void testArrangeForApprovedApplicationStatusWithOverDraft() throws Exception {
        primaryInvolvedPartyYesOverdraft.setCurrentYearOfStudy(new BigInteger("4"));
        final Arrangement arrangementReqYesOverdraft = new Arrangement(primaryInvolvedPartyYesOverdraft, productArrangement,
                null);
        setBirthCityCountryNationalityAndTinDetails(arrangementReqYesOverdraft, offerProductDtoYesIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        productOfferedDTO.setApplicationStatus("1002");
        productOfferedDTO.setMnemonic("P_STUDENT");
        params.setAppStatus("1002");
        params.setProductMnemonic("P_STUDENT");
        final Map<String, String> productOptions = new HashMap<String, String>();
        productOptions.put("Overdraft Offer Flag", "Y");
        productOptions.put("tariffKey", "tariffValue");
        productOfferedDTO.setProductOptions(productOptions);
        when(session.getUserContext()).thenReturn(userContext);
        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID", "cid"))
                .thenReturn("contactpoitnId");
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.withResult(responseDTO()));
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementReqYesOverdraft);
        assertThat(testResult.getApplicationStatus(), is("1002"));
        verify(session, times(2)).setArrangeToActivateParameters(Matchers.any(ArrangeToActivateParameters.class));
    }

    @Test
    public void testArrangeForApprovedApplicationStatusWithOverDraftWithError() throws Exception {
        primaryInvolvedPartyYesOverdraft.setCurrentYearOfStudy(new BigInteger("4"));
        final Arrangement arrangementReqYesOverdraft = new Arrangement(primaryInvolvedPartyYesOverdraft, productArrangement,
                null);
        setBirthCityCountryNationalityAndTinDetails(arrangementReqYesOverdraft, offerProductDtoYesIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        productOfferedDTO.setApplicationStatus("1002");
        productOfferedDTO.setMnemonic("P_STUDENT");
        params.setAppStatus("1002");
        params.setProductMnemonic("P_STUDENT");
        final Map<String, String> productOptions = new HashMap<String, String>();
        productOptions.put("Overdraft Offer Flag", "Y");
        productOptions.put("tariffKey", "tariffValue");
        productOfferedDTO.setProductOptions(productOptions);
        when(session.getUserContext()).thenReturn(userContext);
        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID", "cid"))
                .thenReturn("contactpoitnId");
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.<OverdraftResponseDTO> withError(new DAOResponse.DAOError("code", null)));
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementReqYesOverdraft);
        assertThat(testResult.getApplicationStatus(), is("1002"));

    }

    @Test
    public void shouldReturnArrangementWithMandateFalseForNewCustomer() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(configurationService.getConfigurationStringValue("131091", "IB_ERROR_CODE")).thenReturn("1100023");
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(partyResponseDTOIfCustomerHasNoEligibleAccounts);
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertFalse(testResult.getIsIBMandate());
        validate(testResult);
    }

    @Test
    public void shouldReturnArrangementWithMandateFalseIfNoMatches() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(configurationService.getConfigurationStringValue("131085", "IB_ERROR_CODE")).thenReturn("1100023");
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(partyResponseDTOWithMandateIfCustomerHasNoMacthes);
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertFalse(testResult.getIsIBMandate());

        //Reset The values
        params.setAsmScore(testResult.getAsmScore());
        params.setCbsProductNumberTrimmed("901");
        params.setProductOptions(new HashMap<String,String>());
        //End of Reset of the Values
        verify(session).setArrangeToActivateParameters(params);

        //Reset the same values
        params.setAsmScore(null);
        params.setCbsProductNumberTrimmed(null);
        params.setCurrentYearOfStudy(null);
        params.setProductOptions(null);
        //End of Reset of the same values
    }

    @Test
    public void shouldReturnArrangementWithMandateFalseIfNoPartiesFound() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(configurationService.getConfigurationStringValue("131084", "IB_ERROR_CODE")).thenReturn("1100023");
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(partyResponseDTOWithMandateIfCustomerHasNoParties);
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertFalse(testResult.getIsIBMandate());
        //verify(session).setArrangeToActivateParameters(params);
        validate(testResult);
    }


    @Test
    public void shouldReturnArrangementWithMandateFalseIfNoPartiesFoundInAuthJourney() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(session.getUserInfo()).thenReturn(new com.lbg.ib.api.sso.domain.user.Arrangement());
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(configurationService.getConfigurationStringValue("131084", "IB_ERROR_CODE")).thenReturn("1100023");
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(partyResponseDTOWithMandateIfCustomerHasNoParties);
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertFalse(testResult.getIsIBMandate());
        validate(testResult);
    }

    public void validate(Arranged arranged){
        //Reset The values
        params.setAsmScore(arranged.getAsmScore());
        params.setCbsProductNumberTrimmed("901");
        params.setProductOptions(new HashMap<String,String>());
        //End of Reset of the Values
        verify(session).setArrangeToActivateParameters(params);

        //Reset the same values
        params.setAsmScore(null);
        params.setCbsProductNumberTrimmed(null);
        params.setCurrentYearOfStudy(null);
        params.setProductOptions(null);
        //End of Reset of the same values
    }
    @Test
    public void shouldReturnArrangementWithMandateTrueIfCustomerHasAMandate() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(configurationService.getConfigurationStringValue("131089", "IB_ERROR_CODE")).thenReturn("1100021");
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(partyResponseDTOWithMandateIfCustomerHasMandate);
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertTrue(testResult.getIsIBMandate());
        validate(testResult);
    }

    @Test
    public void shouldReturnArrangementWithMandateTrueForExistingCustomer() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertTrue(testResult.getIsIBMandate());
        validate(testResult);
    }

    @Test
    public void shouldThrowServiceExceptionWhenSelectedProductNotFound() throws Exception {
        try {
            setBirthCityCountryNationalityAndTinDetails(arrangementRequested, null);
            Arranged response = classUnderTest.arrange(arrangementRequested);
            validate(response);

        } catch (final ServiceException e) {
            assertThat(e.getResponseError(),
                    is(resolver.resolve(ResponseErrorConstants.BAD_REQUEST_PRODUCT_NOT_SELECTED)));
        }
    }

    @Test
    public void shouldResolveDaoErrorAndThrowServiceExceptionWhenOfferProductDAOReturnsWithAnErrorResultWithNullMsg()
            throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.<ProductOfferedDTO> withError(new DAOResponse.DAOError("code", null)));
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(resolver.customResolve("code", "message")).thenReturn(CUSTOM_IB_ERROR);
        try {
            classUnderTest.arrange(arrangementRequested);
            fail();
        } catch (final ServiceException e) {
            assertThat(e.getResponseError().getMessage(), is("Service Unavailable"));
        }
    }

    @Test
    public void shouldResolveDaoErrorAndThrowServiceExceptionWhenOfferProductDAOReturnsWithAnErrorResult()
            throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.<ProductOfferedDTO> withError(new DAOResponse.DAOError("code", "message")));
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(resolver.customResolve("code", "message")).thenReturn(CUSTOM_IB_ERROR);
        try {
            classUnderTest.arrange(arrangementRequested);
            fail();
        } catch (final ServiceException e) {
            assertThat(e.getResponseError(), is(CUSTOM_IB_ERROR));
        }
    }

    @Test
    public void shouldLogAndWrapUnhandledDaoExceptionWithServiceException() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenThrow(RUNTIME_EXCEPTION);
        try {
            classUnderTest.arrange(arrangementRequested);
            fail();
        } catch (final ServiceException e) {
            assertThat(e.getResponseError(), is(resolver.resolve(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR)));
            verify(logger).logException(ArrangementServiceImpl.class, RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void shouldThrowServiceExceptionWhenDAOReturnsNull() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(offerProductDtoNoIntendToSwitch)).thenReturn(null);
        try {
            classUnderTest.arrange(arrangementRequested);
            fail();
        } catch (final ServiceException e) {
            assertThat(e.getResponseError(),
                    is(resolver.resolve(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR_OFFER_DOES_NOT_EXIST)));
        }
    }

    @Test
    public void shouldReturnArrangementDTOWithIntendSwitchAndOverdraft() throws Exception {
    final OfferProductDTO offerProductDto = new OfferProductDTO(
        SwitchOptions.Yes,
        true,
        200,
        "prefixTitleName",
        "firstName",
        "middleName",
        "lastName",
        "emailAddress",
        MOBILE_PHONE,
        HOME_PHONE,
        WORK_PHONE,
        CURRENT_ADDRESS,
        PREV_ADDRESS,
        "gender",
        BIRTH_DATE,
        "maritalStatus",
        new BigInteger("1"),
        EMPLOYMENT,
        new BigDecimal("2"),
        "residentialStatus",
        UK_RES_DATE,
        UK_VISA_DATE,
        true,
        true,
        false,
        false,
        new BigDecimal("3"),
        new BigDecimal("1"),
        new BigDecimal("4"),
        "fundingSource",
        "accountPurpose",
        PID,
        new HashMap<String, String>() {
            {
            put("cname", "cvalue");
            }
        },
        new HashMap<String, String>() {
            {
            put("epic", "epii");
            }
        },
        "pname",
        "mnemonic",
        null,
        new ThreatMatrixDTO(),
        Collections.<MarketingPreferenceDTO>emptyList(),"100");
        setBirthCityCountryNationalityAndTinDetails(arrangementRequestedYesSwitchYesOverdraft, offerProductDto);

        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));

        final Arranged testResult = classUnderTest.arrange(arrangementRequestedYesSwitchYesOverdraft);

        assertThat(testResult.getArrangementId(), is(arranged.getArrangementId()));
        validate(testResult);
    }

    @Test
    public void shouldReturnArrangementDTOWithIntendSwitchAndOverdraftWith() throws Exception {
        final PartyResponseDTO partyResponseDTO = new PartyResponseDTO("pwdState", asList(new ChannelDTO("STL", "STL")),
                true);

    final OfferProductDTO offerProductDto = new OfferProductDTO(
        SwitchOptions.Yes,
        true,
        200,
        "prefixTitleName",
        "firstName",
        "middleName",
        "lastName",
        "emailAddress",
        MOBILE_PHONE,
        HOME_PHONE,
        WORK_PHONE,
        CURRENT_ADDRESS,
        PREV_ADDRESS,
        "gender",
        BIRTH_DATE,
        "maritalStatus",
        new BigInteger("1"),
        EMPLOYMENT,
        new BigDecimal("2"),
        "residentialStatus",
        UK_RES_DATE,
        UK_VISA_DATE,
        true,
        true,
        false,
        false,
        new BigDecimal("3"),
        new BigDecimal("1"),
        new BigDecimal("4"),
        "fundingSource",
        "accountPurpose",
        PID,
        new HashMap<String, String>() {
            {
            put("cname", "cvalue");
            }
        },
        new HashMap<String, String>() {
            {
            put("epic", "epii");
            }
        },
        "pname",
        "mnemonic",
        null,
        new ThreatMatrixDTO(),
        Collections.<MarketingPreferenceDTO>emptyList(),"100");
        setBirthCityCountryNationalityAndTinDetails(arrangementRequestedYesSwitchYesOverdraft, offerProductDto);

        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));

        final Arranged testResult = classUnderTest.arrange(arrangementRequestedYesSwitchYesOverdraft);

        assertThat(testResult.getArrangementId(), is(arranged.getArrangementId()));
        validate(testResult);
    }

    @Test
    public void testMapMnemonicIntoActivateSession() {
        final TreeMap<String, String> map = new TreeMap<String, String>();
        final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic = withResult(map);
        final ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        classUnderTest.mapMnemonicIntoActivateSession(arrangeToActivateParameters, promotionalInstructionMnemonic);
    }

    @Test
    public void testMapMnemonicIntoActivateSessionWithPromotionalAsNull() {
        final TreeMap<String, String> map = null;
        final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic = withResult(map);
        final ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        classUnderTest.mapMnemonicIntoActivateSession(arrangeToActivateParameters, promotionalInstructionMnemonic);
    }

    @Test
    public void testMapMnemonicIntoActivateSessionWithPromotionalWithError() {
        final TreeMap<String, String> map = new TreeMap<String, String>();
        final DAOResponse<TreeMap<String, String>> promotionalInstructionMnemonic = DAOResponse
                .withError(errorMappindForMandate("131091", "Customer has no eligible accounts"));
        final ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        classUnderTest.mapMnemonicIntoActivateSession(arrangeToActivateParameters, promotionalInstructionMnemonic);
    }

    @Test
    public void testIsApplnStatusDeclinedOrRejectedOrDownSell() {
        final ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        assertFalse(classUnderTest.isApplnStatusDeclinedOrRejectedOrDownSell(productOfferedDTO));
    }

    @Test
    public void testIsApplnStatusDeclinedOrRejectedOrDownSellDecline() {
        final ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        productOfferedDTO.setApplicationStatus("1004");
        assertTrue(classUnderTest.isApplnStatusDeclinedOrRejectedOrDownSell(productOfferedDTO));
        productOfferedDTO.setApplicationStatus("1003");
        assertTrue(classUnderTest.isApplnStatusDeclinedOrRejectedOrDownSell(productOfferedDTO));
        productOfferedDTO.setApplicationStatus("1006");
        productOfferedDTO.setOfferType("DOWNSELL");
        assertTrue(classUnderTest.isApplnStatusDeclinedOrRejectedOrDownSell(productOfferedDTO));
    }

    @Test
    public void testIsApplnStatusApproveOrRefer() {
        final ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        productOfferedDTO.setApplicationStatus("1003");
        assertTrue(classUnderTest.isApplnStatusApproveOrRefer(productOfferedDTO));
    }

    @Test
    public void testToIntWithException() {
        final ConditionDTO condition = new ConditionDTO(null, null, null);
        assertTrue(classUnderTest.toInt(condition) == null);
    }

    @Test
    public void testPopulateAuthUnstructuredAddressDTO() {
        final UnstructuredPostalAddress address = new UnstructuredPostalAddress();
        assertTrue(classUnderTest.getCommonUtils().populateAuthUnstructuredAddressDTO(address) != null);
    }

    @Test
    public void testPopulateUnstructuredAddressDTO() {
        UnstructuredPostalAddress address = new UnstructuredPostalAddress();
        address.setAddressLine7("dummy");
        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

        address.setAddressLine5("dummy");
        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

        address.setAddressLine2("dummy");
        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

        address = new UnstructuredPostalAddress();
        address.setAddressLine2("dummy");
        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

        address = new UnstructuredPostalAddress();
        address.setAddressLine3("dummy");
        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

        address = new UnstructuredPostalAddress();
        address.setAddressLine2("dummy");
        address.setAddressLine7(null);
        address.setAddressLine5("dummy");

        assertTrue(classUnderTest.getCommonUtils().populateUnstructuredAddressDTO(address) != null);

    }

    @Test
    public void testPhoneWithNull() {
        assertTrue(classUnderTest.getCommonUtils().phone(null, null) == null);
    }

    @Test
    public void testGetCustomerDetailsWithArrangementAsNull() {
        assertTrue(classUnderTest.getCustomerDetails(null) == null);
    }

    @Test
    public void testGetCustomerDetailsWithPrimaryInvolvedPartyAsNull() {
        final Arrangement arrangement = new Arrangement();
        assertTrue(classUnderTest.getCustomerDetails(arrangement) == null);
    }

    @Test
    public void testCheckPwdStateChannelNull() {
        final PartyResponseDTO responseDTO = new PartyResponseDTO(null, null, null);
        assertFalse(classUnderTest.checkPwdState(responseDTO));
    }

    @Test
    public void testPopulateDeclineReason() {
        assertTrue(classUnderTest.populateDeclineReason(null) == null);
    }

    @Test
    public void testPopulateTmxDTO() {
        assertTrue(classUnderTest.populateTmxDTO(null) != null);
    }

    @Test
    public void shouldReturnArrangementDTOWithIntendSwitch() throws Exception {
        final OfferProductDTO offerProductDto = getOfferProductDTO(SwitchOptions.Yes, false);
        setBirthCityCountryNationalityAndTinDetails(arrangementRequestedYesSwitch, offerProductDto);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequestedYesSwitch);

        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        validate(testResult);

    }

    @Test
    public void shouldReturnArrangementDTOWithIntendOverdraft() throws Exception {
        final OfferProductDTO offerProductDto = getOfferProductDTO(SwitchOptions.No, true);
        setBirthCityCountryNationalityAndTinDetails(arrangementRequestedYesOverdraft, offerProductDto);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequestedYesOverdraft);

        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        validate(testResult);

    }

    @Test
    public void shouldThrowServiceExceptionWhenDAOReturnsNullForDtoWithIntendSwitch() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(offerProductDtoNoIntendToSwitch)).thenReturn(null);
        try {
            classUnderTest.arrange(arrangementRequested);
            fail();
        } catch (final ServiceException e) {
            assertThat(e.getResponseError(),
                    is(resolver.resolve(ResponseErrorConstants.PRODUCT_OFFER_SERVICE_ERROR_OFFER_DOES_NOT_EXIST)));
        }
    }

    @Test
    public void shouldReturnOverdraftInformationWhenDAOReturnsWithOverdraftAttrinutesAndSetParametersInSession()
            throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        final StHeader stHead = setheaders("IBL");
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(overdraftDAO.fetchOverdraftDetails((requestDTO(stHead))))
                .thenReturn(DAOResponse.withResult(responseDTO()));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertThat(testResult.getOverdraft(), is(arranged.getOverdraft()));
    }

    @Test
    public void shouldReturnCrossSellInformationWhenDAOReturnsWithCrossSellProducts() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final TreeMap<String, String> promotionalResponseMap = new TreeMap<String, String>();
        promotionalResponseMap.put("2", "P_EXC_SVR");
        when(crossSellEligibilityDAO
                .determineCrossSellEligibilityForCustomer(promotionalCustomerInstructionsTestRequest()))
                        .thenReturn(DAOResponse.withResult(promotionalResponseMap));
        Arranged arranged = classUnderTest.arrange(arrangementRequested);
        final ArrangeToActivateParameters activateToArrangement = params;
        validate(arranged);

    }

    @Test
    public void shouldReturnBankHolidaysWhenIntendToSwitch() throws Exception {
        setArrangementForHolidayList(SwitchOptions.Yes);
        setBirthCityCountryNationalityAndTinDetails(arrangement, offerProductDtoYesIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        when(bankHolidaysHolder.getBankHolidays()).thenReturn(holidayList);

        final Arranged testResult = classUnderTest.arrange(arrangement);
        assertEquals(holidayList, testResult.getBankHolidays());

    }

    @Test
    public void shouldReturnBankHolidaysAsNullWhenNotIntendToSwitch() throws Exception {
        setArrangementForHolidayList(SwitchOptions.Later);
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        when(bankHolidaysHolder.getBankHolidays()).thenReturn(holidayList);

        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertEquals(testResult.getBankHolidays(), null);

    }

    @Test
    public void shouldReturnBankHolidaysWhenIntendToSwitchLater() throws Exception {
        setArrangementForHolidayList(SwitchOptions.Later);
        setBirthCityCountryNationalityAndTinDetails(arrangement, offerProductDtoForSwitchLater);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        when(bankHolidaysHolder.getBankHolidays()).thenReturn(holidayList);

        final Arranged testResult = classUnderTest.arrange(arrangement);
        assertEquals(holidayList, testResult.getBankHolidays());

    }

    @Test
    public void shouldReturnProductAttributeListForDownsell() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
    final ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "DOWNSELL",
        new HashMap<String, String>() {
            {
            put("n", "v");
            }
        },
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        null,
        null,
        "pdtFamilyId",
        null,
        null,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        assertThat(classUnderTest.arrange(arrangementRequested).getProductAttributes(),
                is(arranged.getProductAttributes()));
    }

    @Test
    public void shouldReturnsNullForNormalOfferType() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        assertEquals(classUnderTest.arrange(arrangementRequested).getProductAttributes(), null);
    }

    @Test
    public void testToVerifyWhetherSessionIsClearedIfOfferTypeIsDownsell() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
    final ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "DOWNSELL",
        new HashMap<String, String>() {
            {
            put("n", "v");
            }
        },
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        null,
        null,
        "pdtFamilyId",
        null,
        null,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        classUnderTest.arrange(arrangementRequested);
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    private void setArrangementForHolidayList(final SwitchOptions option) {

        final PrimaryInvolvedParty primaryInvolvedParty = primaryInvolvedParty(option, false);

        arrangement = new Arrangement(primaryInvolvedParty, productArrangement, null);
        arrangement.setAccountType(AccountType.CA);
    }

    private void setBirthCityCountryNationalityAndTinDetails(final Arrangement arrangementRequested,
            final OfferProductDTO offerProductDTO) {

        if (arrangementRequested != null && arrangementRequested.getPrimaryInvolvedParty() != null) {
            arrangementRequested.getPrimaryInvolvedParty().setBirthCity("leeds");
            final TinDetails tinDetails = new TinDetails();
            tinDetails.setBirthCountry("United Kingdom");
            final LinkedHashSet<String> nationalities = new LinkedHashSet<String>();
            nationalities.add("GBR");
            nationalities.add("IND");
            tinDetails.setNationalities(nationalities);
            final LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();
            final TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
            taxResidencyDetails.setTaxResidency("GBR");
            taxResidencyDetails.setTaxResidency("IND");
            taxResidencies.add(taxResidencyDetails);
            tinDetails.setTaxResidencies(taxResidencies);
            arrangementRequested.getPrimaryInvolvedParty().setTinDetails(tinDetails);
            arrangementRequested.setAccountType(AccountType.CA);
        }

        if (offerProductDTO != null) {
            offerProductDTO.setBirthCity("leeds");
            final TinDetailsDTO tinDetails = new TinDetailsDTO();
            tinDetails.setBirthCountry("United Kingdom");
            final LinkedHashSet<String> nationalities = new LinkedHashSet<String>();
            nationalities.add("GBR");
            nationalities.add("IND");
            tinDetails.setNationalities(nationalities);
            final LinkedHashSet<TaxResidencyDetailsDTO> taxResidencies = new LinkedHashSet<TaxResidencyDetailsDTO>();
            final TaxResidencyDetailsDTO taxResidencyDetails = new TaxResidencyDetailsDTO();
            taxResidencyDetails.setTaxResidency("GBR");
            taxResidencyDetails.setTaxResidency("IND");
            taxResidencies.add(taxResidencyDetails);
            tinDetails.setTaxResidencies(taxResidencies);
            offerProductDTO.setTinDetails(tinDetails);
            offerProductDTO.setAccType("CA");
        }
    }

    private OfferProductDTO getOfferProductDTO(final SwitchOptions intendSwitch, final boolean intendOverdraft) {
        return new OfferProductDTO(intendSwitch, intendOverdraft, 200, "prefixTitleName", "firstName", "middleName",
                "lastName", "emailAddress", MOBILE_PHONE, HOME_PHONE, WORK_PHONE, CURRENT_ADDRESS, PREV_ADDRESS,
                "gender", BIRTH_DATE, "maritalStatus", new BigInteger("1"), EMPLOYMENT, new BigDecimal("2"),
                "residentialStatus", UK_RES_DATE, UK_VISA_DATE, true, true, false, false, new BigDecimal("3"),
                new BigDecimal("1"), new BigDecimal("4"), "fundingSource", "accountPurpose", PID,
                new HashMap<String, String>() {
                    {
                        put("cname", "cvalue");
                    }
                }, new HashMap<String, String>() {
                    {
                        put("epic", "epii");
                    }
                }, "pname", "mnemonic", null, new ThreatMatrixDTO(), null,null);
    }

    private StHeader setheaders(final String chanId) {
        final StHeader stHeader = new StHeader();
        stHeader.setChanid(chanId);
        stHeader.setUseridAuthor("UNAUTHSALE");
        return stHeader;
    }

    private OverdraftRequestDTO requestDTO(final StHeader stHeaders) {

        final OverdraftRequestDTO odReq = new OverdraftRequestDTO();
        odReq.setStheader(stHeaders);
        odReq.setSortcode("777505");
        odReq.setAmtOverdraft(new BigDecimal(200));
        odReq.setCbsprodnum(new BigInteger("0071"));
        odReq.setCbstariff("1");
        return odReq;
    }

    private OverdraftResponseDTO responseDTO() {

        final OverdraftResponseDTO odResponse = new OverdraftResponseDTO();
        odResponse.setAmtIntFreeOverdraft(new BigDecimal("25"));
        odResponse.setAmtExcessFee(new BigDecimal("30"));
        odResponse.setAmtExcessFeeBalIncr(new BigDecimal("10"));
        odResponse.setAmtTotalCreditCost(new BigDecimal("114"));
        odResponse.setIntrateBase("0");
        odResponse.setIntrateMarginOBR("18.3");
        odResponse.setIntrateUnauthEAR("19.94");
        odResponse.setIntrateUnauthMnthly("1.53");
        odResponse.setNExcessFeeCap(new BigInteger("3"));
        return odResponse;
    }

    @Test
    public void shouldReturnEidvScoreWhenOfferResponseHasEidvScore() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertEquals(productOfferedDTO.eidvScoreDTO().getScoreResult(), testResult.getEidvScore());

    }

    @Test
    public void shouldReturnAsmScoreWhenOfferResponseHasAsmScore() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));

        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        String asmScore = null;
        if (productOfferedDTO.asmScoreDTO() != null && productOfferedDTO.asmScoreDTO().getScoreResult() != null)
            asmScore = findASMTypeFromCode(productOfferedDTO.asmScoreDTO().getScoreResult()).toString();
        assertEquals(asmScore, testResult.getAsmScore());

    }

    @Test
    public void shouldReturnEidvScoreAsNullWhenOfferResponseHasNoEidvScore() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        eidvScoreDTO = new EIDVScoreDTO(null, "assessmentType", "evidenceIdentifier", "identityStrength", "code",
                "description");
    productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "otype",
        new HashMap<String, String>(),
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        null,
        null,
        "pdtFamilyId",
        null,
        null,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertEquals(testResult.getEidvScore(), null);

    }


    @Test
    public void testArrangeForApprovedApplicationStatusWithOverDraftWithAddPartyWithNoOverDraft() throws Exception {
        primaryInvolvedPartyYesOverdraft.setCurrentYearOfStudy(new BigInteger("4"));
        final Arrangement arrangementReqYesOverdraft = new Arrangement(primaryInvolvedPartyYesOverdraft, productArrangement,
                null);
        final RelatedInvolvedParty relatedInvolvedParty = new RelatedInvolvedParty();
        //relatedInvolvedParty.setOverDraftLimit("1");
        arrangementReqYesOverdraft.setRelatedInvolvedParty(relatedInvolvedParty);
        setBirthCityCountryNationalityAndTinDetails(arrangementReqYesOverdraft, offerProductDtoYesIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        productOfferedDTO.setApplicationStatus("1002");
        productOfferedDTO.setMnemonic("P_STUDENT");
        params.setAppStatus("1002");
        params.setProductMnemonic("P_STUDENT");
        final Map<String, String> productOptions = new HashMap<String, String>();
        productOptions.put("Overdraft Offer Flag", "Y");
        productOptions.put("tariffKey", "tariffValue");
        productOfferedDTO.setProductOptions(productOptions);
        when(session.getUserContext()).thenReturn(userContext);
        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID", "cid"))
                .thenReturn("contactpoitnId");
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.withResult(responseDTO()));
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementReqYesOverdraft);
        assertThat(testResult.getApplicationStatus(), is("1002"));
        verify(session, times(2)).setArrangeToActivateParameters(Matchers.any(ArrangeToActivateParameters.class));
    }


    @Test
    public void testArrangeForApprovedApplicationStatusWithOverDraftWithAddPartyWithOverDraft() throws Exception {
        primaryInvolvedPartyYesOverdraft.setCurrentYearOfStudy(new BigInteger("4"));
        final Arrangement arrangementReqYesOverdraft = new Arrangement(primaryInvolvedPartyYesOverdraft, productArrangement,
                null);
        final RelatedInvolvedParty relatedInvolvedParty = new RelatedInvolvedParty();
        relatedInvolvedParty.setOverDraftLimit("1");
        arrangementReqYesOverdraft.setRelatedInvolvedParty(relatedInvolvedParty);
        setBirthCityCountryNationalityAndTinDetails(arrangementReqYesOverdraft, offerProductDtoYesIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        productOfferedDTO.setApplicationStatus("1002");
        productOfferedDTO.setMnemonic("P_STUDENT");
        params.setAppStatus("1002");
        params.setProductMnemonic("P_STUDENT");
        final Map<String, String> productOptions = new HashMap<String, String>();
        productOptions.put("Overdraft Offer Flag", "Y");
        productOptions.put("tariffKey", "tariffValue");
        productOfferedDTO.setProductOptions(productOptions);
        when(session.getUserContext()).thenReturn(userContext);
        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID", "cid"))
                .thenReturn("contactpoitnId");
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.withResult(responseDTO()));
        when(offerProductDAO.offer(Matchers.any(OfferProductDTO.class)))
                .thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementReqYesOverdraft);
        assertThat(testResult.getApplicationStatus(), is("1002"));
        verify(session, times(2)).setArrangeToActivateParameters(Matchers.any(ArrangeToActivateParameters.class));
    }


    @Test
    public void shouldReturnAsmScoreAsNullWhenOfferResponseHasNoAsmScore() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        asmScoreDTO = new ASMScoreDTO(null, "assessmentType", "evidenceIdentifier", "identityStrength", "code",
                "description");
    productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "otype",
        new HashMap<String, String>(),
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        null,
        null,
        "pdtFamilyId",
        null,
        null,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        String asmScore = null;
        if (productOfferedDTO.asmScoreDTO() != null && productOfferedDTO.asmScoreDTO().getScoreResult() != null)
            asmScore = findASMTypeFromCode(productOfferedDTO.asmScoreDTO().getScoreResult()).toString();
        assertEquals(asmScore, null);
        assertEquals(testResult.getAsmScore(), null);

    }

    @Test
    public void shouldSetEmploymentDetailsAsNullWhenEmploymentStatusIsRetired() throws Exception {
        final EmploymentDTO employment = new EmploymentDTO("janitor", EMPLOYMENT_STATUS_RETIRED);
        final OfferProductDTO offerProductDto = new OfferProductDTO(SwitchOptions.Yes, true, 200, "prefixTitleName",
                "firstName", "middleName", "lastName", "emailAddress", MOBILE_PHONE, HOME_PHONE, WORK_PHONE,
                CURRENT_ADDRESS, PREV_ADDRESS, "gender", BIRTH_DATE, "maritalStatus", new BigInteger("1"), employment,
                new BigDecimal("2"), "residentialStatus", UK_RES_DATE, UK_VISA_DATE, true, true, false, false,
                new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("4"), "fundingSource", "accountPurpose", PID,
                new HashMap<String, String>() {
                    {
                        put("cname", "cvalue");
                    }
                }, new HashMap<String, String>() {
                    {
                        put("epic", "epii");
                    }
                }, "pname", "mnemonic", null, new ThreatMatrixDTO(), null,null);
        offerProductDto.setAccType("CA");
        final PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty(SwitchOptions.Yes, true, 200,
                "prefixTitleName", "firstName", "middleName", "lastName", "emailAddress", BIRTH_DATE, "gender",
                currentAddress, previousAddress, "maritalStatus", "residentialStatus", 1, "fundingSource",
                "accountPurpose", mobileNumber, workNumber, homeNumber, false, true, false, true, UK_RES_DATE,
                UK_VISA_DATE, false, 2, EMPLOYMENT_STATUS_RETIRED, "janitor", null, 3, 1, 4, 0, 0, null,null);

        arrangement = new Arrangement(primaryInvolvedParty, productArrangement, null);
        arrangement.setAccountType(AccountType.CA);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangement);
        assertEquals(testResult.getArrangementId(), productOfferedDTO.arrangementId());
    }

    @Test
    public void shouldSetEmployerWhenEmploymentStatusIsNotRetired() throws Exception {
        setBirthCityCountryNationalityAndTinDetails(arrangementRequested, offerProductDtoNoIntendToSwitch);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        final Arranged testResult = classUnderTest.arrange(arrangementRequested);
        assertEquals(testResult.getArrangementId(), productOfferedDTO.arrangementId());

    }

    private EligibilityRequestDTO promotionalCustomerInstructionsTestRequest() {

        final String[] cnadidateInstruction = { "P_CLUB" };
        final Date birthDate = new Date();

        return new EligibilityRequestDTO(null, birthDate, "678375072", null, cnadidateInstruction, "1001776000",
                "777505");
    }

    @Test
    public void shouldBlockCallsForCrossSellRequest() throws Exception {
        final RelatedApplication relatedApplication = new RelatedApplication();
        relatedApplication.setApplicationId("relAppId");
        relatedApplication.setApplicationStatus("relAppStatus");

        // Related application will be present for a cross-sell request
        final Arrangement arrangementWithRelatedApplication = new Arrangement(primaryInvolvedParty, productArrangement,
                relatedApplication);
        arrangementWithRelatedApplication.setAccountType(AccountType.SA);

        setBirthCityCountryNationalityAndTinDetails(arrangementWithRelatedApplication,
                offerProductDtoForRelatedProduct);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));

        final Arranged testResult = classUnderTest.arrange(arrangementWithRelatedApplication);

        assertThat(testResult.getApplicationStatus(), is(arranged.getApplicationStatus()));
        // Verifying whether the B801 call has been blocked
        verify(partyDAO, times(0)).retrievePartyMandate(Matchers.any(PartyRequestDTO.class));

        // Verifying whether the vantage eligibility call has been blocked
        verify(customerEligibiltyDAO, times(0))
                .determineEligibleCustomerInstructions(Matchers.any(EligibilityRequestDTO.class));

        // Verifying whether the overdraft call has been blocked
        verify(overdraftDAO, times(0)).fetchOverdraftDetails(Matchers.any(OverdraftRequestDTO.class));
    }

    @Test
    public void shouldReturnDeclineReasonForDeclineScenario() throws Exception {
        setArrangementForHolidayList(SwitchOptions.Yes);
        setBirthCityCountryNationalityAndTinDetails(arrangement, offerProductDtoYesIntendToSwitch);
        asmScoreDTO = new ASMScoreDTO("3", "assessmentType", "evidenceIdentifier", "identityStrength", "code",
                "description");
    productOfferedDTO = new ProductOfferedDTO(
        "arrangementId",
        "arrangementType",
        "applicationType",
        eidvScoreDTO,
        asmScoreDTO,
        "ocis",
        "party",
        "cn",
        "ii",
        "pname",
        "otype",
        new HashMap<String, String>(),
        asList(new ConditionDTO("n", "1", "v")),
        "appStatus",
        "appSubStatus",
        "mnemonic",
        "901",
        null,
        new ExistingProductArrangementDTO(null),
        true,
        new BigDecimal("200"),
        null,
        null,
        "pdtFamilyId",
        new ConditionDTO("code", null, "description"),
        null,
        null,
        Collections.<MarketingPreferenceDTO>emptyList());
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        when(offerProductDAO.offer(any(OfferProductDTO.class))).thenReturn(DAOResponse.withResult(productOfferedDTO));
        when(partyDAO.retrievePartyMandate(Matchers.any(PartyRequestDTO.class)))
                .thenReturn(DAOResponse.withResult(partyResponseDTO));
        when(bankHolidaysHolder.getBankHolidays()).thenReturn(holidayList);

        final Arranged testResult = classUnderTest.arrange(arrangement);
        assertEquals(testResult.getDeclineReason().getName(), productOfferedDTO.getDeclineReason().getName());
        assertEquals(testResult.getDeclineReason().getValue(), productOfferedDTO.getDeclineReason().getValue());

    }

    private DAOError errorMappindForMandate(final String errorCode, final String erroMessage) {
        return new DAOError(errorCode, erroMessage);
    }

    private Map<String, String> prepareDaoResponseTest() {
        final Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("device_id", null);
        responseMap.put("smartDeviceId", null);
        responseMap.put("smartDeviceIdConfidence", null);
        responseMap.put("trueIp", null);
        responseMap.put("accountLogin", null);
        responseMap.put("summartRiskScore", null);
        responseMap.put("summaryReasonCode", null);
        responseMap.put("reasonCode", null);
        responseMap.put("riskRating", null);
        responseMap.put("reviewStatus", null);
        responseMap.put("deviceFirstSeen", null);
        responseMap.put("devicelastSeen", null);
        responseMap.put("trueIpGeo", null);
        responseMap.put("trueIpIsp", null);
        responseMap.put("trueIpOrgnaisation", null);
        responseMap.put("proxyIpGeo", null);
        responseMap.put("dnsIpGeo", null);
        responseMap.put("browserLanguage", null);
        return responseMap;
    }

}
