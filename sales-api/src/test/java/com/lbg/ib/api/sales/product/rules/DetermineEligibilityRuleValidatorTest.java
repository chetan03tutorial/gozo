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
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;

@RunWith(MockitoJUnitRunner.class)
public class DetermineEligibilityRuleValidatorTest {
    private PrimaryInvolvedParty              primaryInvolvedParty;

    @InjectMocks
    private DetermineEligibilityRuleValidator determineEligibilityRuleValidator = new DetermineEligibilityRuleValidator();

    @Mock
    private EmployerDetails                   employer                          = new EmployerDetails("Company Name",
            "addressLine1", "addressLine2", "postcode", 20, 10);

    @Mock
    private PrimaryInvolvedParty              mockPrimaryInvolvedParty;

    @Test
    public void testValidateRulesForSuccess() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse("01/29/87");
        primaryInvolvedParty.setDob(date);
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setOccupnType("abc");
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError, null);
    }

    @Test
    public void testValidateRulesForEmploymentStatus() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse("01/29/87");
        primaryInvolvedParty.setDob(date);
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        primaryInvolvedParty.setEmploymentStatus("004");
        primaryInvolvedParty.setOccupnType("abc");
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError.getMessage(), "If you are not in job, then Employer Field should be null");
    }

    @Test
    public void testValidateRulesForOccupationType() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse("01/29/87");
        primaryInvolvedParty.setDob(date);
        primaryInvolvedParty.setIntendOverDraft(false);
        primaryInvolvedParty.setNumberOfDependents(5);
        primaryInvolvedParty.setEmploymentStatus("003");
        primaryInvolvedParty.setOccupnType(null);
        EmployerDetails employer = new EmployerDetails();
        primaryInvolvedParty.setEmployer(employer);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError.getMessage(), "You must specify an occupation type");
    }

    @Test
    public void testValidateRulesForOverDraft() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse("01/29/87");
        primaryInvolvedParty.setDob(date);
        primaryInvolvedParty.setIntendOverDraft(true);
        primaryInvolvedParty.setNumberOfDependents(null);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError.getMessage(),
                "If overdraft preference is selected then number of dependents must be specified");
    }

    @Test
    public void testValidateRulesForDoB() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        ContactNumber homePhone = new ContactNumber("44", "", "123456", "");
        ContactNumber mobileNumber = new ContactNumber("44", "", "654321", "");
        primaryInvolvedParty.setHomePhone(homePhone);
        primaryInvolvedParty.setMobileNumber(mobileNumber);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse("01/29/03");
        primaryInvolvedParty.setDob(date);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError.getMessage(), "The age must be between 18-100");
    }

    @Test
    public void testValidateRulesForPhoneNumber() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setHomePhone(null);
        primaryInvolvedParty.setMobileNumber(null);
        ValidationError employmentStatusError = determineEligibilityRuleValidator.validateRules(primaryInvolvedParty);
        assertEquals(employmentStatusError.getMessage(), "Either 'homePhone' or 'mobileNumber' must be specified");
    }

    @Test
    public void eitherHomePhoneOrMobilePhoneIsProvided() throws Exception {
        primaryInvolvedParty = pipWithPhone(new ContactNumber(), null);
        assertThat(determineEligibilityRuleValidator.validatePhoneNumbersRule(primaryInvolvedParty), is(nullValue()));
        PrimaryInvolvedParty mobileNumber = pipWithPhone(null, new ContactNumber());
        assertThat(determineEligibilityRuleValidator.validatePhoneNumbersRule(mobileNumber), is(nullValue()));
        PrimaryInvolvedParty both = pipWithPhone(new ContactNumber(), new ContactNumber());
        assertThat(determineEligibilityRuleValidator.validatePhoneNumbersRule(both), is(nullValue()));
        PrimaryInvolvedParty none = pipWithPhone(null, null);
        assertThat(determineEligibilityRuleValidator.validatePhoneNumbersRule(none),
                is(new ValidationError("Either 'homePhone' or 'mobileNumber' must be specified")));
    }

    @Test
    public void validateAge() throws Exception {
        Calendar instance = Calendar.getInstance();
        instance.set(1982, JANUARY, 1);// 18
        Date dob = instance.getTime();
        primaryInvolvedParty = pipWithDOB(dob);
        instance.set(2000, JANUARY, 2);
        assertThat(determineEligibilityRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()),
                is(nullValue()));
        instance.set(1999, JANUARY, 1);
        assertThat(determineEligibilityRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()),
                is(new ValidationError("The age must be between 18-100")));
        instance.set(2082, JANUARY, 2);
        assertThat(determineEligibilityRuleValidator.validateDob(primaryInvolvedParty, instance.getTime()),
                is(new ValidationError("The age must be between 18-100")));

    }

    @Test
    public void ShouldReturnErrorWhenEmploymentStatusIsUnEmployedAndOccupationTypeNotNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("000", "004");
        assertThat(determineEligibilityRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are not in a job, then Occupation Type should be null")));
    }

    @Test
    public void ShouldReturnErrorWhenEmploymentStatusIsRetiredAndOccupationTypeNotNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("006", "001");
        assertThat(determineEligibilityRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are not in a job, then Occupation Type should be null")));
    }

    @Test
    public void ShouldReturnErrorWhenOccupationTypeIsNull() throws Exception {
        primaryInvolvedParty = pipWithEmploymentStatus("004", null);
        assertThat(determineEligibilityRuleValidator.validateOccupationType(primaryInvolvedParty),
                is(new ValidationError("If you are in a job, then Occupation Type should be provided")));
    }

    @Test
    public void ShouldReturnErrorWhenEmployerIsNullForFullTimePartTimeSelfEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("001", "004", null);
        assertThat(determineEligibilityRuleValidator.validateEmploymentStatus(primaryInvolvedParty),
                is(new ValidationError("If you are in a job, then Employer Field should not be null")));
    }

    @Test
    public void ShouldReturnErrorWhenEmployerIsNotNullForNonEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("006", "004", employer);
        assertThat(determineEligibilityRuleValidator.validateEmploymentStatus(primaryInvolvedParty),
                is(new ValidationError("If you are not in job, then Employer Field should be null")));
    }

    @Test
    public void ShouldReturnNullWhenEmployerExistForFullTimePartTimeSelfEmployed() {
        primaryInvolvedParty = pipWithEmploymentStatusAndEmployer("001", "004", employer);
        assertEquals(determineEligibilityRuleValidator.validateEmploymentStatus(primaryInvolvedParty), null);
    }

    @Test
    public void shouldReturnErrorWhenIntendToOverdraftButNumberOfDependentsNull() throws Exception {
        pipWithOverdraft(true, 200, null);
        assertThat(determineEligibilityRuleValidator.validateOverDratftAttributes(mockPrimaryInvolvedParty),
                is(new ValidationError(
                        "If overdraft preference is selected then number of dependents must be specified")));
    }

    @Test
    public void shouldReturnNullWhenIntendToOverdraftWithNumberOfDependents() throws Exception {
        pipWithOverdraft(true, 200, 2);
        assertEquals(determineEligibilityRuleValidator.validateOverDratftAttributes(mockPrimaryInvolvedParty), null);
    }

    private PostalAddress with(String buildingName, String buildingNumber) {
        return new PostalAddress(null, null, null, null, null, buildingName, buildingNumber, null, null, null);
    }

    private PrimaryInvolvedParty pipWithCurrentAddress(PostalAddress address) {
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