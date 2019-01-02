/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.rules;

import static java.util.Calendar.JANUARY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.ValidationService;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;

@RunWith(MockitoJUnitRunner.class)
public class ArrangementRuleValidatorTest {
    private PrimaryInvolvedParty     primaryInvolvedParty;

    @InjectMocks
    private ArrangementRuleValidator arrangementRuleValidator = new ArrangementRuleValidator();

    @Mock
    private EmployerDetails          employer                 = new EmployerDetails("Company Name", "addressLine1",
            "addressLine2", "postcode", 20, 10);

    @Mock
    private PrimaryInvolvedParty     mockPrimaryInvolvedParty;

    @Mock
    ValidationService                validationService;

    @Test
    public void testValidateRulesForSuccess() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setOccupnType("abc");
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error, null);
    }

    @Test
    public void testValidateRulesWhenValidationServiceReturnTrue() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(true);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error, null);
    }

    @Test
    public void testValidateRulesForEmploymentType() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        primaryInvolvedParty.setEmploymentStatus("004");
        primaryInvolvedParty.setOccupnType("abc");
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error.getMessage(), "If you are not in job, then Employer Field should be null");
    }

    @Test
    public void testValidateRulesForOverDraft() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setOccupnType("abc");
        primaryInvolvedParty.setIntendOverDraft(true);
        primaryInvolvedParty.setNumberOfDependents(null);
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error.getMessage(),
                "If overdraft preference is selected then number of dependents must be specified");
    }

    @Test
    public void testValidateRulesForOccupationType() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setOccupnType("");
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error.getMessage(), "You must specify an occupation type");
    }

    @Test
    public void testValidateRulesForPhoneNumber() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("buildingName", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        primaryInvolvedParty.setHomePhone(null);
        primaryInvolvedParty.setMobileNumber(null);

        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error.getMessage(), "Either 'homePhone' or 'mobileNumber' must be specified");
    }

    @Test
    public void testValidateRulesForBuildingName() throws Exception {
        Arrangement arrangement = new Arrangement();
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        UnstructuredPostalAddress unStructuredAddress = withUnstructuredPostalAddress("", null);
        PostalAddressComponent currentAddress = new PostalAddressComponent();
        currentAddress.setUnstructuredAddress(unStructuredAddress);
        primaryInvolvedParty.setCurrentAddress(currentAddress);
        arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        when(validationService.isOptional()).thenReturn(false);
        ValidationError error = arrangementRuleValidator.validateRules(arrangement);
        assertEquals(error.getMessage(), "Either 'buildingName' or 'buildingNumber' must be specified");
    }

    @Test
    public void eitherBuildingNameOrBuildingNumberIsProvided() throws Exception {
        primaryInvolvedParty = pipWithCurrentUnstructuredAddress(withUnstructuredPostalAddress("buildingName", null));
        assertThat(arrangementRuleValidator.validateBuildingNameNumberRule(primaryInvolvedParty), is(nullValue()));
        PrimaryInvolvedParty buildingNumber = pipWithCurrentUnstructuredAddress(
                withUnstructuredPostalAddress(null, "buildingNumber"));
        assertThat(arrangementRuleValidator.validateBuildingNameNumberRule(buildingNumber), is(nullValue()));
        PrimaryInvolvedParty both = pipWithCurrentUnstructuredAddress(
                withUnstructuredPostalAddress("buildingName", "buildingNumber"));
        assertThat(arrangementRuleValidator.validateBuildingNameNumberRule(both), is(nullValue()));
        PrimaryInvolvedParty none = pipWithCurrentUnstructuredAddress(withUnstructuredPostalAddress("", ""));
        assertThat(arrangementRuleValidator.validateBuildingNameNumberRule(none),
                is(new ValidationError("Either 'buildingName' or 'buildingNumber' must be specified")));
        assertEquals(arrangementRuleValidator.validateBuildingNameNumberRule(pipWithCurrentAddress(null)), null);
    }

    @Test
    public void eitherHomePhoneOrMobilePhoneIsProvided() throws Exception {
        primaryInvolvedParty = pipWithPhone(new ContactNumber(), null);
        assertThat(arrangementRuleValidator.validatePhoneNumbersRule(primaryInvolvedParty), is(nullValue()));
        PrimaryInvolvedParty mobileNumber = pipWithPhone(null, new ContactNumber());
        assertThat(arrangementRuleValidator.validatePhoneNumbersRule(mobileNumber), is(nullValue()));
        PrimaryInvolvedParty both = pipWithPhone(new ContactNumber(), new ContactNumber());
        assertThat(arrangementRuleValidator.validatePhoneNumbersRule(both), is(nullValue()));
        PrimaryInvolvedParty none = pipWithPhone(null, null);
        assertThat(arrangementRuleValidator.validatePhoneNumbersRule(none),
                is(new ValidationError("Either 'homePhone' or 'mobileNumber' must be specified")));
    }

    @Test
    public void validateAge() throws Exception {
        Calendar instance = Calendar.getInstance();
        instance.set(1982, JANUARY, 1);// 18
        Date dob = instance.getTime();
        primaryInvolvedParty = pipWithDOB(dob);
        instance.set(2000, JANUARY, 2);
        assertThat(arrangementRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()), is(nullValue()));
        instance.set(1999, JANUARY, 1);
        assertThat(arrangementRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()),
                is(new ValidationError("The age must be between 18-100")));
        instance.set(2082, JANUARY, 2);
        assertThat(arrangementRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()),
                is(new ValidationError("The age must be between 18-100")));

    }

    @Test
    public void ShouldReturnErrorWhenEmploymentStatusIsUnEmployedAndOccupationTypeNotNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("000", "004");
        assertThat(arrangementRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are not in a job, then Occupation Type should be null")));
    }

    @Test
    public void ShouldReturnErrorWhenEmploymentStatusIsRetiredAndOccupationTypeNotNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("006", "001");
        assertThat(arrangementRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are not in a job, then Occupation Type should be null")));
    }

    @Test
    public void ShouldReturnErrorWhenOccupationTypeIsNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("004", null);
        assertThat(arrangementRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are in a job, then Occupation Type should be provided")));
    }

    @Test
    public void ShouldReturnErrorWhenEmployerIsNullForFullTimePartTimeSelfEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("001", "004", null);
        assertThat(arrangementRuleValidator.validateEmploymentStatus(primaryInvolvedParty),
                is(new ValidationError("If you are in a job, then Employer Field should not be null")));
    }

    @Test
    public void ShouldReturnErrorWhenEmployerIsNotNullForNonEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("006", "004", employer);
        assertThat(arrangementRuleValidator.validateEmploymentStatus(primaryInvolvedParty),
                is(new ValidationError("If you are not in job, then Employer Field should be null")));
    }

    @Test
    public void ShouldReturnNullWhenEmployerExistForFullTimePartTimeSelfEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("001", "004", employer);
        assertEquals(arrangementRuleValidator.validateEmploymentStatus(primaryInvolvedParty), null);
    }

    @Test
    public void shouldReturnErrorWhenIntendToOverdraftButNumberOfDependentsNull() throws Exception {
        pipWithOverdraft(true, 200, null);
        assertThat(arrangementRuleValidator.validateOverDratftAttributes(mockPrimaryInvolvedParty),
                is(new ValidationError(
                        "If overdraft preference is selected then number of dependents must be specified")));
    }

    @Test
    public void shouldReturnNullWhenIntendToOverdraftWithNumberOfDependents() throws Exception {
        pipWithOverdraft(true, 200, 2);
        assertEquals(arrangementRuleValidator.validateOverDratftAttributes(mockPrimaryInvolvedParty), null);
    }

    @Test
    public void shouldNotContainDobInToString() {
        Calendar instance = Calendar.getInstance();
        instance.set(1982, JANUARY, 1);// 18
        Date dob = instance.getTime();
        primaryInvolvedParty = pipWithDOB(dob);
        assertFalse(primaryInvolvedParty.toString().contains("dob"));
    }

    private UnstructuredPostalAddress withUnstructuredPostalAddress(String buildingName, String buildingNumber) {
        return new UnstructuredPostalAddress(buildingName, buildingNumber, null, null, null, null, null, null, null,
                null);
    }

    private PrimaryInvolvedParty pipWithCurrentAddress(PostalAddress address) {
        return new PrimaryInvolvedParty(SwitchOptions.No, false, 200, null, null, null, null, null, null, null,
                new PostalAddressComponent(null, null, null, address), null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null);
    }

    private PrimaryInvolvedParty pipWithCurrentUnstructuredAddress(UnstructuredPostalAddress address) {
        return new PrimaryInvolvedParty(SwitchOptions.No, false, 200, null, null, null, null, null, null, null,
                new PostalAddressComponent(null, null, null, address), null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null);
    }

    private PrimaryInvolvedParty pipWithDOB(Date dob) {
        return new PrimaryInvolvedParty(SwitchOptions.No, false, 200, null, null, null, null, null, dob, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null,null);
    }

    private PrimaryInvolvedParty pipWithPhone(ContactNumber homePhone, ContactNumber mobilePhone) {
        return new PrimaryInvolvedParty(SwitchOptions.No, false, 200, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, mobilePhone, null, homePhone, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null,null);
    }

    private PrimaryInvolvedParty pipWithEmploymentStatus(String empStatus, String occuType) {
        return new PrimaryInvolvedParty(SwitchOptions.No, true, 200, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                empStatus, occuType, null, null, null, null, null, null, null,null);
    }

    private PrimaryInvolvedParty pipWithEmploymentStatusAndEmployer(String empStatus, String occuType,
            EmployerDetails employer) {
        return new PrimaryInvolvedParty(SwitchOptions.No, true, 200, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                empStatus, occuType, employer, null, null, null, null, null, null,null);
    }

    private void pipWithOverdraft(boolean isIntendOverdraft, Integer overdraftAmount, Integer numberOfDependents) {
        when(mockPrimaryInvolvedParty.getIntendOverDraft()).thenReturn(isIntendOverdraft);
        when(mockPrimaryInvolvedParty.getIntendOdAmount()).thenReturn(overdraftAmount);
        when(mockPrimaryInvolvedParty.getNumberOfDependents()).thenReturn(numberOfDependents);
    }
}