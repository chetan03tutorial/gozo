/**
 * 8711247 T Senthil Kumar
 */

package com.lbg.ib.api.sales.dao.product.eligibility;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.mapper.EligibilityRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EligibilityRequestMapperTest {

    @Mock
    private GBOHeaderUtility         gboHeaderUtility;

    @Mock
    private SessionManagementDAO     session;
    @InjectMocks
    private EligibilityRequestMapper mapper;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPopulateRequestWithStructuredAddress() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedParty();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", party, new String[] { "P_REWARD" }, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.populateRequest(dto);
        Assert.assertNotNull(request);
    }

    @Test
    public void testPopulateRequestWithPostalCode5() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedPartyWithPostalCode5();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", party, new String[] { "P_REWARD" }, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.populateRequest(dto);
        Assert.assertNotNull(request);
    }

    @Test
    public void testPopulateRequestWithPostalCode7() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedPartyWithPostalCode7();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", party, new String[] { "P_REWARD" }, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.populateRequest(dto);
        Assert.assertNotNull(request);
    }

    @Test
    public void testPopulateRequestWithPostalCode() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedPartyWithPostalCode();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", party, new String[] { "P_REWARD" }, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.populateRequest(dto);
        Assert.assertNotNull(request);
    }

    @Test
    public void testMapRequest() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedParty();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", new GregorianCalendar().getTime(), "P_REWARD", null,
                null, null, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.mapRequest(dto);
        Assert.assertNotNull(request);
    }

    @Test
    public void testPopulateRequestwithUnstrucutruedAddress() throws Exception {

        PrimaryInvolvedParty party = setPrimaryInvolvedPartyWithUnstructuredAddress();
        EligibilityRequestDTO dto = new EligibilityRequestDTO("CA", party, new String[] { "P_REWARD" }, null);
        List<SOAPHeader> list = new ArrayList<SOAPHeader>();
        when(gboHeaderUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "determineEligibleCustomerInstructions")).thenReturn(list);
        when(session.getSessionId()).thenReturn("abcz");
        DetermineEligibleCustomerInstructionsRequest request = mapper.populateRequest(dto);
        Assert.assertNotNull(request);
    }

    private PrimaryInvolvedParty setPrimaryInvolvedParty() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        postalAddress.setPostcode("E201BF");

        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setMiddleName("sira, kira");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        pip.setIncome(23);
        pip.setRentMortgCost(23);
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);

        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        LinkedHashSet<TaxResidencyDetails> taxResisencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails txDetails = new TaxResidencyDetails();
        txDetails.setTinNumber("23");
        txDetails.setTaxResidencyType("residencyType");
        txDetails.setTaxResidency("2323");
        set.add("GBR");

        taxResisencies.add(txDetails);
        tDetails.setTaxResidencies(taxResisencies);
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private PrimaryInvolvedParty setPrimaryInvolvedPartyWithPostalCode5() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        postalAddress.setPostcode("E16DU");

        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setMiddleName("sira, kira");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        pip.setIncome(23);
        pip.setRentMortgCost(23);
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);

        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        LinkedHashSet<TaxResidencyDetails> taxResisencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails txDetails = new TaxResidencyDetails();
        txDetails.setTinNumber("23");
        txDetails.setTaxResidencyType("residencyType");
        txDetails.setTaxResidency("2323");
        set.add("GBR");

        taxResisencies.add(txDetails);
        tDetails.setTaxResidencies(taxResisencies);
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private PrimaryInvolvedParty setPrimaryInvolvedPartyWithPostalCode() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        postalAddress.setPostcode("E1 6DU");

        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setMiddleName("sira, kira");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        pip.setIncome(23);
        pip.setRentMortgCost(23);
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);

        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        LinkedHashSet<TaxResidencyDetails> taxResisencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails txDetails = new TaxResidencyDetails();
        txDetails.setTinNumber("23");
        txDetails.setTaxResidencyType("residencyType");
        txDetails.setTaxResidency("2323");
        set.add("GBR");

        taxResisencies.add(txDetails);
        tDetails.setTaxResidencies(taxResisencies);
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private PrimaryInvolvedParty setPrimaryInvolvedPartyWithPostalCode7() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        postalAddress.setPostcode("E16DUXX");

        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setMiddleName("sira, kira");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        pip.setIncome(23);
        pip.setRentMortgCost(23);
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);

        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        LinkedHashSet<TaxResidencyDetails> taxResisencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails txDetails = new TaxResidencyDetails();
        txDetails.setTinNumber("23");
        txDetails.setTaxResidencyType("residencyType");
        txDetails.setTaxResidency("2323");
        set.add("GBR");

        taxResisencies.add(txDetails);
        tDetails.setTaxResidencies(taxResisencies);
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private PrimaryInvolvedParty setPrimaryInvolvedPartyWithUnstructuredAddress() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        postalAddress.setPostcode("E201BF");
        postalAddress.setSubBuildingName("23");
        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        // component.setStructuredAddress(postalAddress);

        UnstructuredPostalAddress unAddress = new UnstructuredPostalAddress("ss", "ss", "sdsd", "sds", "sdsd", "sdsd",
                "sdsd", "sdsd", "E201BF", "2T");

        component.setUnstructuredAddress(unAddress);

        pip.setCurrentAddress(component);
        pip.setPreviousAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        pip.setIncome(23);
        pip.setRentMortgCost(23);
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);
        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        LinkedHashSet<TaxResidencyDetails> taxResisencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails txDetails = new TaxResidencyDetails();
        txDetails.setTinNumber("23");
        txDetails.setTaxResidencyType("residencyType");
        txDetails.setTaxResidency("2323");
        set.add("GBR");

        taxResisencies.add(txDetails);
        tDetails.setTaxResidencies(taxResisencies);
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

}
