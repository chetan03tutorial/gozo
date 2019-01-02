/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.offer;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.party.domain.TaxResidencyType.findTaxResidencyTypeFromCode;
import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sso.domain.user.UserContext;
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
import com.lbg.ib.api.sales.dto.product.offer.RelatedApplicationDTO;
import com.lbg.ib.api.sales.dto.product.offer.TaxResidencyDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.TinDetailsDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.StructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.UnstructuredPostalAddressDTO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.RelatedInvolvedParty;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IdentificationDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.MarketingPreference;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.offerproduct.reqrsp.OfferProductArrangementRequest;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sales.utils.CommonUtils;

public class OfferProductArrangementRequestBuilderTest {

    public static final String                    ACCOUNT_PURPOSE            = "BENPA";

    public static final String                    MALE                       = "001";

    public static final Date                      DOB                        = new Date(0);

    public static final String                    UNITED_KINGDOM             = "United Kingdom";

    public static final BigDecimal                INCOME                     = new BigDecimal("10.0");

    public static final String[]                  TAX_RESIDENCIES            = new String[] { "ALA", "GBR", "USA" };

    public SessionManagementDAO                   session                    = mock(SessionManagementDAO.class);

    public ConfigurationDAO                       configurationDAO           = mock(ConfigurationDAO.class);

    public Arrangement                            arrangement                = mock(Arrangement.class);

    private final ChannelBrandingDAO                    channelBrandingDao         = mock(ChannelBrandingDAO.class);

    private final GBOHeaderUtility                      gboHeaderUtility           = mock(GBOHeaderUtility.class);

    private final ReferenceDataServiceDAO               referenceDataService       = mock(ReferenceDataServiceDAO.class);

    private final MCAHeaderUtility                      mcaHeaderUtility           = mock(MCAHeaderUtility.class);

    private final OfferProductArrangementRequestBuilder builder                    = new OfferProductArrangementRequestBuilder(
            session, gboHeaderUtility, referenceDataService, channelBrandingDao, mcaHeaderUtility);

    private final OfferProductArrangementRequestBuilder builder2                   = new OfferProductArrangementRequestBuilder(
            session, gboHeaderUtility, referenceDataService, channelBrandingDao, mcaHeaderUtility, configurationDAO);

    private final HashMap<String, String>               productExternalIdentifiers = new HashMap<String, String>() {
                                                                                 {
                                                                                     put("extCode", "extValue");
                                                                                 }
                                                                             };

    private final HashMap<String, String>               productOptions             = new HashMap<String, String>() {
                                                                                 {
                                                                                     put("option", "value");
                                                                                 }
                                                                             };

    private final Date                                  RESIDENCE_START_DATE       = new Date(0);

    private final Date                                  VISA_EXPIRY_DATE           = new Date(1);

    private final PostalAddressComponentDTO       currentAddress             = new PostalAddressComponentDTO(
            new StructuredPostalAddressDTO("Shoreditch", "London", "London", "A CORP", "SUB", "BUILDING", "5",
                    asList("Polo")),
            "1010", "CURRENT", "P1", "E2 7KK", true, false);

    private final PostalAddressComponentDTO       previousAddress            = new PostalAddressComponentDTO(
            new UnstructuredPostalAddressDTO("3 Portland Street", "London", "ext1"), "3030", "PREVIOUS", "A1", "WC11CZ",
            false, true);

    private static PostalAddressComponent         cAddress                   = new PostalAddressComponent("1010", false,
            false, new com.lbg.ib.api.sso.domain.address.PostalAddress("district", "town", "county", "organisation",
                    "subBuilding", "building", "buildingNumber", asList("addressLines"), "postcode", "suffix"));

    private static PostalAddressComponent         pAddress                   = new PostalAddressComponent("1010", false,
            false, new UnstructuredPostalAddress("addressLine1", "addressLine2", "addressLine3", "addressLine4",
                    "addressLine5", "addressLine6", "addressLine7", "addressLine8", "postcode", "deliveryPointSuffix"));

    private final EmploymentDTO                   EMPLOYMENT                 = new EmploymentDTO("banker", "001",
            "lloyds", "48 Chiswell St", "Moorgate", "WC14XX", "20", "20");

    private final EmploymentDTO                   EMPLOYMENT_HFAX            = new EmploymentDTO("banker", "001",
            "halifax", "48 Chiswell St", "Moorgate", "WC14XX", "20", "20");

    private final EmploymentDTO                   EMPLOYMENT_NO_DETAILS      = new EmploymentDTO("retired", "123");

    private final PhoneDTO                        mobilePhone                = new PhoneDTO("44", null, "7795554466",
            "", "Mobile");

    private final String                                BIRTH_CITY                 = "Leeds";

    private final TinDetailsDTO                         tinDetails                 = new TinDetailsDTO();

    private final LinkedHashSet<String>                 nationalities              = new LinkedHashSet<String>();

    private final LinkedHashSet<TaxResidencyDetailsDTO> taxResidencies             = new LinkedHashSet<TaxResidencyDetailsDTO>();

    private static final Date                     BIRTH_DATE                 = new Date(0);

    private static final Date                     UK_RES_DATE                = new Date(1);

    private static final Date                     UK_VISA_DATE               = new Date(2);

    public static final PostalAddressComponentDTO CURRENT_ADDRESS            = new PostalAddressComponentDTO(
            new StructuredPostalAddressDTO("district", "town", "county", "organisation", "subBuilding", "building",
                    "buildingNumber", asList("addressLines")),
            "1010", "CURRENT", "suffix", "postcode", false, false);

    public static final PostalAddressComponentDTO PREV_ADDRESS               = new PostalAddressComponentDTO(
            new UnstructuredPostalAddressDTO("addressLine1", "addressLine2", "addressLine3", "addressLine4",
                    "addressLine5", "addressLine6", "addressLine7", "addressLine8"),
            "1010", "PREVIOUS", "deliveryPointSuffix", "postcode", false, false);

    public static ContactNumber                   mobileNumber               = new ContactNumber("1", "2", "3", "4");

    public static ContactNumber                   workNumber                 = new ContactNumber("1", "2", "3", "4");

    public static ContactNumber                   homeNumber                 = new ContactNumber("1", "2", "3", "4");

    public static final EmployerDetails           EMPLOYER                   = new EmployerDetails("employerName", null,
            null, null, 0, 0);

    private final RelatedInvolvedParty                  relatedInvolvedParty       = relatedInvolvedParty(SwitchOptions.No,
            false);

    private static final MarketingPreferenceDTO MARKETING_PREFERENCE_DTO = new MarketingPreferenceDTO("email", true);

    @Before
    public void setUp() throws Exception {
        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("LTSBRetail", "LLOYDS", "IBL")));
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestBasedOnDto() throws Exception {
        final OfferProductArrangementRequest request = builder.build(exampleOfferProductDTO(EMPLOYMENT));

        final Individual individual = request.getProductArrangement().getPrimaryInvolvedParty().getIsPlayedBy();
        assertCustomer(request.getProductArrangement().getPrimaryInvolvedParty());
        assertIndividual(individual);
        assertEmploymentWithDetails(individual, "banker", "001");
        assertArrangement(request.getProductArrangement());
    }

    @Test
    public void testGetSIRAWorkflowNameWithNullBrandName() {

        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("LTSBRetail", null, "IBL")));

        final OfferProductArrangementRequestBuilder builderx = new OfferProductArrangementRequestBuilder(session,
                gboHeaderUtility, referenceDataService, channelBrandingDao, mcaHeaderUtility, configurationDAO);

        assertTrue(builderx.getSIRAWorkflowName(new OfferProductDTO()) != null);
    }

    @Test
    public void shouldBuildExpectedProductArrangementforNonBranch() throws Exception {
        final OfferProductDTO dto = exampleOfferProductDTO(EMPLOYMENT);
        final RelatedApplicationDTO rDto = new RelatedApplicationDTO();
        rDto.setApplicationId("id");
        rDto.setApplicationStatus("Status");
        dto.setRelatedApplication(rDto);
        dto.setAccType("CA");
        dto.setOcisId("ocis");
        final BranchContext context = new BranchContext("colleagueId", "22222222", "PCA", "Lloyds", "PCA");
        when(session.getBranchContext()).thenReturn(context);
        final UserContext ucontext = new UserContext("userId", "ipAddress", "sessionId", "partyId", "partyId", "partyId",
                "partyId", "partyId", "partyId", "partyId", "partyId");
        when(session.getUserContext()).thenReturn(ucontext);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("partyId", "partyid");

        when(configurationDAO.getConfigurationItems("ChannelIdMapping")).thenReturn(map);
        // builder.build(offerProductDTO)

        final OfferProductArrangementRequest request = builder2.build(dto);

        final Individual individual = request.getProductArrangement().getPrimaryInvolvedParty().getIsPlayedBy();
        assertCustomer(request.getProductArrangement().getPrimaryInvolvedParty());
        assertIndividual(individual);
        assertEmploymentWithDetails(individual, "banker", "001");
        assertArrangement(request.getProductArrangement());
    }

    @Test
    public void shouldBuildExpectedProductArrangementforNonBranchforHFax() throws Exception {
        final OfferProductDTO dto = exampleOfferProductDTO(EMPLOYMENT_HFAX);
        final RelatedApplicationDTO rDto = new RelatedApplicationDTO();
        when(channelBrandingDao.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("HFAXRetail", "HALIFAX", "IBH")));
        rDto.setApplicationId("id");
        rDto.setApplicationStatus("Status");
        dto.setRelatedApplication(rDto);
        dto.setAccType("CA");
        dto.setOcisId("ocis");
        final BranchContext context = new BranchContext("colleagueId", "22222222", "PCA", "Lloyds", "PCA");
        when(session.getBranchContext()).thenReturn(context);
        final UserContext ucontext = new UserContext("userId", "ipAddress", "sessionId", "partyId", "partyId", "partyId",
                "partyId", "partyId", "partyId", "partyId", "partyId");
        when(session.getUserInfo()).thenReturn(arrangement);
        when(session.getUserContext()).thenReturn(ucontext);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("partyId", "partyid");

        when(configurationDAO.getConfigurationItems("ChannelIdMapping")).thenReturn(map);
        // builder.build(offerProductDTO)

        final OfferProductArrangementRequest request = builder2.build(dto);

        final Individual individual = request.getProductArrangement().getPrimaryInvolvedParty().getIsPlayedBy();
        // assertCustomer(request.getProductArrangement().getPrimaryInvolvedParty());
        assertIndividual(individual);
        // assertEmploymentWithDetails(individual, "banker", "001");
        assertArrangement(request.getProductArrangement());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForFATCA() throws Exception {
        final String tinNumber = "ABC";
        final String[] taxResidency = new String[] { "USA" };
        final HashMap<String, String> tinDetailsMap = new HashMap<String, String>();
        tinDetailsMap.put("USA", "ABC");
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "FATCA");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxPayerIdNumber(), is(tinNumber));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForFATCAWithTinNumberNull() throws Exception {
        final String tinNumber = null;
        final String[] taxResidency = new String[] { "USA" };
        final HashMap<String, String> tinDetailsMap = new HashMap<String, String>();
        tinDetailsMap.put("USA", null);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "FATCA");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxPayerIdNumber(), is(tinNumber));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidenciesWithTelephoneNumber()
            throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", "ABC");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        offerProductDTO.setMiddleName(null);
        final PhoneDTO phoneDTO = new PhoneDTO("dummy", "dummy", "dummy", "dummy", "dummy");
        offerProductDTO.setMobilePhone(phoneDTO);
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithNullTaxResidencies() throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", "ABC");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        offerProductDTO.setMiddleName(null);
        offerProductDTO.getTinDetails().setTaxResidencies(null);
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertTrue(request != null);

    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidencies() throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", "ABC");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        offerProductDTO.setMiddleName(null);
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidenciesWithNoRuleConditions()
            throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", "ABC");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        offerProductDTO.setIntendSwitch(null);
        offerProductDTO.setIsIntendOverDraft(null);
        offerProductDTO.setIntendOdAmount(null);
        offerProductDTO.setMiddleName(null);
        offerProductDTO.setExptdMntlyDepAmt(1.0);
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidenciesWithNullMiddleName()
            throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", "ABC");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidencyAndTinNumberEmpty()
            throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final HashMap<String, String> tinDetailsMap = new HashMap<String, String>();
        tinDetailsMap.put("GBR", "");
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithSingleTaxResidencyAndTinNumberNull()
            throws Exception {
        final String[] taxResidency = new String[] { "GBR" };
        final HashMap<String, String> tinDetailsMap = new HashMap<String, String>();
        tinDetailsMap.put("GBR", null);
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestForCRSWithMultipleTaxResidenciesAndTinNumberNull()
            throws Exception {
        final Map<String, String> tinDetailsMap = new LinkedHashMap<String, String>();
        tinDetailsMap.put("GBR", null);
        tinDetailsMap.put("USA", null);
        final String[] taxResidency = new String[] { "GBR", "USA" };
        final List<IdentificationDetails> identificationDetailsList = populateIdentificationDetails(tinDetailsMap);
        final OfferProductDTO offerProductDTO = populateOfferProductDTOWithTinDetails(tinDetailsMap, "CRS");
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        final OfferProductArrangementRequest request = builder.build(offerProductDTO);

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getTaxResidencyDetails()
                .getTaxResidencyCountries(), is(taxResidency));

        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails().length,
                is(identificationDetailsList.size()));
        assertThat(
                request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getCountryCode(),
                is(identificationDetailsList.get(0).getCountryCode()));
        assertThat(request.getProductArrangement().getPrimaryInvolvedParty().getIdentificationDetails(0).getValue(),
                is(identificationDetailsList.get(0).getValue()));

    assertMarketingPreferences(request.getProductArrangement().getMarketingPreferences());
    }

    @Test
    public void shouldBuildExpectedProductArrangementRequestWithoutEmploymentDetails() throws Exception {
        final OfferProductArrangementRequest request = builder.build(exampleOfferProductDTO(EMPLOYMENT_NO_DETAILS));

        final Individual individual = request.getProductArrangement().getPrimaryInvolvedParty().getIsPlayedBy();
        assertCustomer(request.getProductArrangement().getPrimaryInvolvedParty());
        assertIndividual(individual);
        assertEmploymentNoDetails(individual, "retired", "123");
        assertArrangement(request.getProductArrangement());
    }

    @Test
    public void testRelatedInvolvedPartyWhenBranchContext() throws Exception {
        final CommonUtils commonUtils = new CommonUtils(session);
        builder.setCommonUtils(commonUtils);
        when(session.getBranchContext())
                .thenReturn(new BranchContext("colleagueId", "originatingSortCode", "tellerId", "domain"));
        final Customer customer = builder.relatedInvolvedParty(relatedInvolvedParty(SwitchOptions.No, false));
        assertThat(customer.getEmailAddress(), is("emailAddress"));
        assertThat(customer.getIsPlayedBy().getEmploymentStatus(), is("employmentStatus"));

    }

    @Test
    public void testRelatedInvolvedPartyWhenNoBranchContext() throws Exception {
        final CommonUtils commonUtils = new CommonUtils(session);
        builder.setCommonUtils(commonUtils);
        final Customer customer = builder.relatedInvolvedParty(relatedInvolvedParty(SwitchOptions.No, false));
        assertThat(customer.getEmailAddress(), is("emailAddress"));
        assertThat(customer.getIsPlayedBy().getEmploymentStatus(), is("employmentStatus"));

    }

    private OfferProductDTO exampleOfferProductDTO(final EmploymentDTO employment) {
    final OfferProductDTO offerProductDTO = new OfferProductDTO(
        SwitchOptions.Yes,
        true,
        200,
        "mr",
        "jack",
        "j",
        "bush",
        "jjb@google.com",
        mobilePhone,
        null,
        null,
        currentAddress,
        previousAddress,
        MALE,
        DOB,
        "003",
        new BigInteger("1"),
        employment,
        INCOME,
        "002",
        RESIDENCE_START_DATE,
        VISA_EXPIRY_DATE,
        false,
        true,
        false,
        true,
        new BigDecimal("200"),
        new BigDecimal("100"),
        new BigDecimal("300.0"),
        "1",
        ACCOUNT_PURPOSE,
        "pid",
        productOptions,
        productExternalIdentifiers,
        "Classic Account",
        "mnemonic",
        null,
        null,
        Collections.singletonList(MARKETING_PREFERENCE_DTO),"100");
        offerProductDTO.setBirthCity(BIRTH_CITY);
        tinDetails.setBirthCountry(UNITED_KINGDOM);
        nationalities.add("GBR");
        nationalities.add("DZA");
        nationalities.add("ALA");
        tinDetails.setNationalities(nationalities);
        final TaxResidencyDetailsDTO taxResidencyDetails = new TaxResidencyDetailsDTO();
        taxResidencyDetails.setTaxResidency("ALA");
        final TaxResidencyDetailsDTO taxResidencyDetails1 = new TaxResidencyDetailsDTO();
        taxResidencyDetails1.setTaxResidency("GBR");
        taxResidencyDetails1.setTinNumber("ABC");
        final TaxResidencyDetailsDTO taxResidencyDetails2 = new TaxResidencyDetailsDTO();
        taxResidencyDetails2.setTaxResidency("USA");
        taxResidencyDetails2.setTinNumber("1234");
        taxResidencyDetails2.setTaxResidencyType(findTaxResidencyTypeFromCode("1").toString());
        taxResidencies.add(taxResidencyDetails);
        taxResidencies.add(taxResidencyDetails1);
        taxResidencies.add(taxResidencyDetails2);
        tinDetails.setTaxResidencies(taxResidencies);
        offerProductDTO.setTinDetails(tinDetails);
        populateOfferProductDTOWithTMXDetails(offerProductDTO);
        return offerProductDTO;

    }

    private OfferProductDTO exampleOfferProductDTOForTaxResidencies(final EmploymentDTO employment) {
    final OfferProductDTO offerProductDTO = new OfferProductDTO(
        SwitchOptions.Yes,
        true,
        200,
        "mr",
        "jack",
        "j",
        "bush",
        "jjb@google.com",
        mobilePhone,
        null,
        null,
        currentAddress,
        previousAddress,
        MALE,
        DOB,
        "003",
        new BigInteger("1"),
        employment,
        INCOME,
        "002",
        RESIDENCE_START_DATE,
        VISA_EXPIRY_DATE,
        false,
        true,
        false,
        true,
        new BigDecimal("200"),
        new BigDecimal("100"),
        new BigDecimal("300.0"),
        "1",
        ACCOUNT_PURPOSE,
        "pid",
        productOptions,
        productExternalIdentifiers,
        "Classic Account",
        "mnemonic",
        null,
        null,
        Collections.singletonList(MARKETING_PREFERENCE_DTO),"100");
        offerProductDTO.setBirthCity(BIRTH_CITY);
        tinDetails.setBirthCountry(UNITED_KINGDOM);
        nationalities.add("GBR");

        return offerProductDTO;

    }

    private void assertArrangement(final ProductArrangement arrangement) {
        assertFalse(arrangement.getMarketingPreferenceByEmail());
        assertTrue(arrangement.getMarketingPreferenceByMail());
        assertFalse(arrangement.getMarketingPreferenceByPhone());
        assertTrue(arrangement.getMarketingPreferenceBySMS());
        assertThat(arrangement.getFundingSource(), is("1"));
        assertThat(arrangement.getAccountPurpose(), is("BENPA"));
        assertProduct(arrangement.getAssociatedProduct());
    assertMarketingPreferences(arrangement.getMarketingPreferences());
    }

    private void assertIndividual(final Individual individual) {
        assertName(individual);
        assertThat(individual.getNationality(), is("GBR"));
        assertThat(individual.getGender(), is(MALE));
        assertThat(individual.getBirthDate().getTime(), is(DOB));
        assertThat(individual.getCountryOfBirth(), is(UNITED_KINGDOM));
        assertThat(individual.getMaritalStatus(), is("003"));
        assertThat(individual.getNumberOfDependents(), is(new BigInteger("1")));
        assertThat(individual.getNetMonthlyIncome().getAmount(), is(INCOME));
        assertThat(individual.getResidentialStatus(), is("002"));
        assertThat(individual.getUKResidenceStartDate().getTime(), is(RESIDENCE_START_DATE));
        assertThat(individual.getVisaExpiryDate().getTime(), is(VISA_EXPIRY_DATE));
        assertThat(individual.getMonthlyMortgageAmount().getAmount(), is(new BigDecimal("200")));
        assertThat(individual.getTotalSavingsAmount().getAmount(), is(new BigDecimal("300.0")));

        // Check for previous nationalities
        final String[] previousNationalities = new String[2];
        previousNationalities[0] = "DZA";
        previousNationalities[1] = "ALA";
        assertThat(individual.getPreviousNationalities(), is(previousNationalities));

        // Check for birthCity
        assertThat(individual.getPlaceOfBirth(), is(BIRTH_CITY));
    }

    private void assertCustomer(final Customer customer) {
        assertThat(customer.getEmailAddress(), is("jjb@google.com"));
        assertPhone(customer);
        assertThat(customer.getTaxResidencyDetails().getTaxResidencyCountries(), is(TAX_RESIDENCIES));

        // Check for CRS Tax Residencies population
        final IdentificationDetails[] identificationDetails = new IdentificationDetails[3];
        final IdentificationDetails identificationDtls = new IdentificationDetails();
        identificationDtls.setCountryCode("ALA");
        identificationDtls.setType("001");
        identificationDetails[0] = identificationDtls;
        final IdentificationDetails identificationDtls1 = new IdentificationDetails();
        identificationDtls1.setCountryCode("USA");
        identificationDtls1.setType("001");
        identificationDtls1.setValue("ABC");
        identificationDetails[1] = identificationDtls1;
        final IdentificationDetails identificationDtls2 = new IdentificationDetails();
        identificationDtls1.setCountryCode("GBR");
        identificationDtls1.setType("001");
        identificationDtls1.setValue("ABC");
        identificationDetails[2] = identificationDtls2;
        assertThat(customer.getIdentificationDetails().length, is(identificationDetails.length));
        assertThat(customer.getIdentificationDetails(0).getCountryCode(),
                is(identificationDetails[0].getCountryCode()));
        assertThat(customer.getIdentificationDetails(1).getCountryCode(),
                is(identificationDetails[1].getCountryCode()));
        assertThat(customer.getIdentificationDetails(1).getValue(), is(identificationDetails[1].getValue()));

        assertAddress(customer);
    }

    private void assertProduct(final Product associatedProduct) {
        assertThat(associatedProduct.getProductIdentifier(), is("pid"));
        final ProductOptions[] productoptions = associatedProduct.getProductoptions();
        assertThat(productoptions.length, is(1));
        assertThat(productoptions[0].getOptionsCode(), is("option"));
        assertThat(productoptions[0].getOptionsValue(), is("value"));
        final ExtSysProdIdentifier[] externalSystemProductIdentifier = associatedProduct.getExternalSystemProductIdentifier();
        assertThat(externalSystemProductIdentifier.length, is(1));
        assertThat(externalSystemProductIdentifier[0].getSystemCode(), is("extCode"));
        assertThat(externalSystemProductIdentifier[0].getProductIdentifier(), is("extValue"));
        assertThat(associatedProduct.getProductName(), is("Classic Account"));
        assertThat(associatedProduct.getInstructionDetails().getInstructionMnemonic(), is("mnemonic"));
    }

    private static void assertMarketingPreferences(final MarketingPreference[] marketingPreferences) {
    assertThat(marketingPreferences.length, is(1));
    assertThat(marketingPreferences[0].getEntitlementId(), is(MARKETING_PREFERENCE_DTO.getEntitlementId()));
    assertThat(marketingPreferences[0].getConsentOption(), is(MARKETING_PREFERENCE_DTO.getConsentOption()));
    }

    private void assertEmploymentWithDetails(final Individual individual, final String occupation, final String status) {
        assertThat(individual.getOccupation(), is(occupation));
        assertThat(individual.getCurrentEmployer().getName(), is("lloyds"));
        assertThat(individual.getCurrentEmployer().getHasPostalAddress(0).getUnstructuredAddress().getAddressLine1(),
                is("48 Chiswell St"));
        assertThat(individual.getCurrentEmployer().getHasPostalAddress(0).getUnstructuredAddress().getAddressLine2(),
                is("Moorgate"));
        assertThat(individual.getCurrentEmployer().getHasPostalAddress(0).getUnstructuredAddress().getPostCode(),
                is("WC14XX"));
        assertThat(individual.getEmploymentStatus(), is(status));
        assertThat(individual.getCurrentEmploymentDuration(), is("2020"));
    }

    private void assertEmploymentNoDetails(final Individual individual, final String occupation, final String status) {
        assertThat(individual.getOccupation(), is(occupation));
        assertThat(individual.getEmploymentStatus(), is(status));
    }

    private void assertAddress(final Customer customer) {
        final PostalAddress[] postalAddress = customer.getPostalAddress();
        assertThat(postalAddress.length, is(2));
        assertThat(postalAddress[0].getStructuredAddress().getDistrict(), is("Shoreditch"));
        assertThat(postalAddress[0].getStructuredAddress().getPostTown(), is("London"));
        assertThat(postalAddress[0].getStructuredAddress().getCounty(), is("London"));
        assertThat(postalAddress[0].getStructuredAddress().getSubBuilding(), is("SUB"));
        assertThat(postalAddress[0].getStructuredAddress().getBuilding(), is("BUILDING"));
        assertThat(postalAddress[0].getStructuredAddress().getBuildingNumber(), is("5"));
        assertThat(postalAddress[0].getStructuredAddress().getAddressLinePAFData(0), is("Polo"));
        assertThat(postalAddress[0].getStructuredAddress().getPointSuffix(), is("P1"));
        assertThat(postalAddress[0].getStructuredAddress().getPostCodeOut(), is("E2"));
        assertThat(postalAddress[0].getStructuredAddress().getPostCodeIn(), is("7KK"));
        assertThat(postalAddress[0].getStatusCode(), is("CURRENT"));
        assertThat(postalAddress[0].getDurationofStay(), is("1010"));
        assertThat(postalAddress[0].getIsPAFFormat(), is(true));
        assertThat(postalAddress[0].getIsBFPOAddress(), is(false));

        assertThat(postalAddress[1].getUnstructuredAddress().getPointSuffix(), is("A1"));
        assertThat(postalAddress[1].getUnstructuredAddress().getAddressLine1(), is("3 Portland Street"));
        assertThat(postalAddress[1].getUnstructuredAddress().getAddressLine2(), is("London"));
        assertThat(postalAddress[1].getUnstructuredAddress().getAddressLine3(), is("ext1"));
        // assertThat(postalAddress[1].getUnstructuredAddress().getAddressLine4(),
        // is("ext2"));
        assertThat(postalAddress[1].getUnstructuredAddress().getPostCode(), is("WC11CZ"));
        assertThat(postalAddress[1].getStatusCode(), is("PREVIOUS"));
        assertThat(postalAddress[1].getDurationofStay(), is("3030"));
        assertThat(postalAddress[1].getIsPAFFormat(), is(false));
        assertThat(postalAddress[1].getIsBFPOAddress(), is(true));
    }

    private void assertPhone(final Customer customer) {
        final TelephoneNumber[] telephoneNumber = customer.getTelephoneNumber();
        assertThat(telephoneNumber.length, is(1));
        assertThat(telephoneNumber[0].getCountryPhoneCode(), is("44"));
        assertThat(telephoneNumber[0].getAreaCode(), nullValue());
        assertThat(telephoneNumber[0].getPhoneNumber(), is("7795554466"));
        assertThat(telephoneNumber[0].getExtensionNumber(), nullValue());
        assertThat(telephoneNumber[0].getDeviceType(), is("Mobile"));
        assertThat(telephoneNumber[0].getTelephoneType(), is("7"));

    }

    private void assertName(final Individual individual) {
        assertThat(individual.getIndividualName(0).getPrefixTitle(), is("mr"));
        assertThat(individual.getIndividualName(0).getFirstName(), is("jack"));
        assertThat(individual.getIndividualName(0).getMiddleNames(0), is("j"));
        assertThat(individual.getIndividualName(0).getLastName(), is("bush"));
    }

    private OfferProductDTO populateOfferProductDTOWithTinDetails(final Map<String, String> tinDetailsMap,
            final String taxResidencyType) {
        final OfferProductDTO offerProductDTO = exampleOfferProductDTOForTaxResidencies(EMPLOYMENT);
        final TinDetailsDTO tinDetails = new TinDetailsDTO();

        for (final Map.Entry<String, String> entry : tinDetailsMap.entrySet()) {
            final TaxResidencyDetailsDTO taxResidencyDetails = new TaxResidencyDetailsDTO();
            taxResidencyDetails.setTaxResidency(entry.getKey());
            taxResidencyDetails.setTinNumber(entry.getValue());
            taxResidencyDetails.setTaxResidencyType(taxResidencyType);
            taxResidencies.add(taxResidencyDetails);
        }
        tinDetails.setNationalities(nationalities);
        tinDetails.setTaxResidencies(taxResidencies);
        offerProductDTO.setTinDetails(tinDetails);
        return offerProductDTO;
    }

    private List<IdentificationDetails> populateIdentificationDetails(final Map<String, String> tinDetailsMap) {
        final List<IdentificationDetails> identificationDetailsList = new ArrayList<IdentificationDetails>();
        for (final Map.Entry<String, String> entry : tinDetailsMap.entrySet()) {
            final IdentificationDetails identificationDetails = new IdentificationDetails();
            identificationDetails.setCountryCode(entry.getKey());
            identificationDetails.setType("001");
            identificationDetails.setValue(entry.getValue());
            identificationDetailsList.add(identificationDetails);
        }

        return identificationDetailsList;
    }

    private void populateOfferProductDTOWithTMXDetails(final OfferProductDTO offerProductDTO) {
        final ThreatMatrixDTO threatMatrixDTO = new ThreatMatrixDTO("deviceId", "smartDeviceId", "smartDeviceIdConfidence",
                "trueIp", "accountLogin", "summartRiskScore", "summaryReasonCode", "policyScore", "reasonCode",
                "riskRating", "reviewStatus", "deviceFirstSeen", "devicelastSeen", "trueIpGeo", "trueIpIsp",
                "trueIpOrgnaisation", "proxyIpGeo", "dnsIpGeo", "browserLanguage");
        offerProductDTO.setThreatMatrixDTO(threatMatrixDTO);
    }

    private RelatedInvolvedParty relatedInvolvedParty(final SwitchOptions option, final boolean intendOverDraft) {
        final RelatedInvolvedParty relatedInvolvedParty = new RelatedInvolvedParty();
        relatedInvolvedParty.setIntendSwitch(option);
        relatedInvolvedParty.setIntendOverDraft(intendOverDraft);
        relatedInvolvedParty.setIntendOdAmount(200);
        relatedInvolvedParty.setTitle("prefixTitleName");
        relatedInvolvedParty.setFirstName("firstName");
        relatedInvolvedParty.setMiddleName("middleName");
        relatedInvolvedParty.setLastName("lastName");
        relatedInvolvedParty.setEmail("emailAddress");
        relatedInvolvedParty.setDob(BIRTH_DATE);
        relatedInvolvedParty.setGender("gender");
        relatedInvolvedParty.setCurrentAddress(cAddress);
        relatedInvolvedParty.setPreviousAddress(pAddress);
        relatedInvolvedParty.setMaritalStatus("maritalStatus");
        relatedInvolvedParty.setResidentialStatus("residentialStatus");
        relatedInvolvedParty.setNumberOfDependents(1);
        relatedInvolvedParty.setFundSource("fundingSource");
        relatedInvolvedParty.setPurpose("accountPurpose");
        relatedInvolvedParty.setMobileNumber(mobileNumber);
        relatedInvolvedParty.setWorkPhone(workNumber);
        relatedInvolvedParty.setHomePhone(homeNumber);
        relatedInvolvedParty.setMarketPrefPhone(false);
        relatedInvolvedParty.setMarketPrefEmail(true);
        relatedInvolvedParty.setMarketPrefText(false);
        relatedInvolvedParty.setMarketPrefPost(true);
        relatedInvolvedParty.setUkArrivalDate(UK_RES_DATE);
        relatedInvolvedParty.setVisaExpiryDate(UK_VISA_DATE);
        relatedInvolvedParty.setIlrStatus(false);
        relatedInvolvedParty.setIncome(2);
        relatedInvolvedParty.setEmploymentStatus("employmentStatus");
        relatedInvolvedParty.setOccupnType("occupation");
        relatedInvolvedParty.setEmployer(EMPLOYER);
        relatedInvolvedParty.setRentMortgCost(3);
        relatedInvolvedParty.setMaintnCost(1);
        relatedInvolvedParty.setSavingsAmount(4);
        relatedInvolvedParty.setPrevBankYear(0);
        relatedInvolvedParty.setPrevBankMonth(0);
        return relatedInvolvedParty;

    }

}
