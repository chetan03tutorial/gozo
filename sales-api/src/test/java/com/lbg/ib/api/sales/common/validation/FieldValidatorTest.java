/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.common.validation;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import com.lbg.ib.api.shared.validation.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.product.domain.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.ProductArrangementCondition;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

@RunWith(MockitoJUnitRunner.class)
public class FieldValidatorTest {

    @Mock
    private CountryFieldValidation countryFieldValidation;

    @Mock
    private ValidationService      validationService = null;

    @Mock
    private LoggerDAO              logger;

    @InjectMocks
    private FieldValidator         fieldValidator    = new FieldValidator();

    @Test
    public void shouldPassValidation() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldFailForRegexValidation() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali1");
        instance.annotatedOverrideMessage = "ali1";
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'ali1' is not matching with format in field 'annotated'")));
    }

    @Test
    public void shouldFailForRegexValidationWithCustomError() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        instance.annotatedOverrideMessage = "ali1";
        assertThat(fieldValidator.validateInstanceFields(instance), is(new ValidationError("Hey watch out!")));
    }

    @Test
    public void shouldFailForMaxLengthValidation() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("aliiiiiiiiiiii");
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'aliiiiiiiiiiii' is longer than '5' characters in field 'annotated'")));
    }

    @Test
    public void shouldFailForMinLengthValidation() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("a");
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'a' is shorter than '3' characters in field 'annotated'")));
    }

    @Test
    public void shouldValidateSubClasses() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        SubClassToBeValidated subClassToBeValidated = new SubClassToBeValidated();
        subClassToBeValidated.subField = "ali1";
        instance.subClassToBeValidated = subClassToBeValidated;
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'ali1' is not matching with format in field 'subField'")));
    }

    @Test
    public void shouldNotThrowStackOverflowExceptionWHenThereIsAFieldWhichIsTheSameTypeWIthClass() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        fieldValidator.validateInstanceFields(instance);
    }

    @Test
    public void shouldAvoidStringValidationWhenStringIsEmptyAndNotRequired() throws Exception {
        SubClassToBeValidated instance = new SubClassToBeValidated();
        instance.subField = "";
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldReturnValidationErrorWhenTheFieldIsRequiredAndItSetAsNull() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated(null);
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'annotated' required to be set but it was 'null'")));
    }

    @Test
    public void shouldReturnValidationErrorWhenTheFieldIsRequiredAndItSetAsEmptyString() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("");
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'annotated' required to be set but it was ''")));
    }

    @Test
    public void shouldReturnValidationErrorWhenTheBoolFieldIsRequiredAndItSetAsNull() throws Exception {
        SubClassHasRequiredField instance = new SubClassHasRequiredField();
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'bool' required to be set but it was 'null'")));
    }

    @Test
    public void shouldReturnErrorForRequiredEmptyIterablesAndWhenTheSizeIsMoreThanRequired() throws Exception {
        ClassEmptyIterables instance = new ClassEmptyIterables();
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'list' required to be set but it was 'empty'")));
        instance.list.add("a");
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'array' required to be set but it was 'empty'")));
        instance.array = new String[] { "b" };
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        instance.list.add("aa");
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'list' size can be maximum '1' it is 2")));
        instance.array = new String[] { "b", "bb" };
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'list' size can be maximum '1' it is 2")));
    }

    @Test
    public void shouldValidateMinimumMaximumForNumbers() throws Exception {
        NumberRequiredFieldClass instance = new NumberRequiredFieldClass();
        instance.number = 3;
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'3' is lower than minimum limit '5' in field 'number'")));
        instance.number = 9;
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'9' is greater than maximum limit '7' in field 'number'")));
        instance.number = 6;
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldValidateValidatableListElements() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        SubClassToBeValidated sub = new SubClassToBeValidated();
        SubClassToBeValidated sub2 = new SubClassToBeValidated();
        sub.subField = "ali";
        sub2.subField = "ali1";
        instance.array = new SubClassToBeValidated[] { sub, sub2 };
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("'ali1' is not matching with format in field 'subField'")));
    }

    @Test
    public void shouldSupportCustomValidation() throws Exception {
        ClassToBeValidated instance = new ClassToBeValidated("ali");
        instance.password = "as1";
        assertThat(fieldValidator.validateInstanceFields(instance),
                is(new ValidationError("Field validation for 'password' fails: Password is not in correct format")));
    }

    @Test
    public void ValidateCountryComboSuccess() throws ReferenceDataException {
        LinkedHashSet<String> country = new LinkedHashSet<String>();
        country.add("GBR");
        country.add("IND");
        Arrangement arrangement = arrangementItems(country);
        when(countryFieldValidation.doesFail(anyString())).thenReturn(false);
        assertThat(fieldValidator.validateInstanceFields(arrangement), is(nullValue()));
    }

    @Test
    public void ValidateCountryComboFailure() throws ReferenceDataException {
        LinkedHashSet<String> country = new LinkedHashSet<String>();
        country.add("GBR");
        country.add("ZZZ");
        Arrangement arrangement = arrangementItems(country);
        when(countryFieldValidation.doesFail(anyString())).thenReturn(true);
        when(countryFieldValidation.message()).thenReturn("Specified Country provided is not in the default List");
        assertThat(fieldValidator.validateInstanceFields(arrangement), is(new ValidationError(
                "Field validation for 'nationalities' fails: Specified Country provided is not in the default List")));
    }

    @Validate
    private static class ClassToBeValidated {
        @RequiredFieldValidation
        @StringFieldValidation(pattern = "[a-z]*", maxLength = 5, minLength = 3)
        private String                          annotated;

        @StringFieldValidation(pattern = "[a-z]*", maxLength = 5, minLength = 3, message = "Hey watch out!")
        private String                          annotatedOverrideMessage;

        SubClassToBeValidated[]                 array;

        private SubClassToBeValidated           subClassToBeValidated;

        private String                          unannotated;

        @CustomFieldValidation(validation = PasswordFieldValidation.class)
        private String                          password;

        private static ClassToBeValidated       itself       = new ClassToBeValidated("ali");

        private static ClassToBeValidated       itself2      = new ClassToBeValidated("ali");

        private static SubClassNotToBeValidated noValidation = new SubClassNotToBeValidated();

        public ClassToBeValidated(String value) {
            annotated = value;
        }
    }

    @Validate
    private static class SubClassToBeValidated {
        @StringFieldValidation(pattern = "[a-z]*", maxLength = 5, minLength = 3)
        private String subField;
    }

    private static class SubClassNotToBeValidated {
        @RequiredFieldValidation
        private String subField;
    }

    @Validate
    private static class SubClassHasRequiredField {
        @RequiredFieldValidation
        private Boolean bool;
    }

    @Validate
    private static class NumberRequiredFieldClass {
        @IntegerFieldValidation(min = 5, max = 7)
        private Number number;
    }

    @Validate
    private static class ClassEmptyIterables {
        @RequiredFieldValidation(max = 1)
        private List<String> list  = new ArrayList<String>();

        @RequiredFieldValidation(max = 1)
        private String[]     array = new String[0];
    }

    @Validate
    private static class SubClasstoBeValidatedAgainstAccType {
        @RequiredFieldValidation
        @AccountTypeValidation(value = AccountType.CA)
        private String mandatoryFieldForPCA;

        @RequiredFieldValidation
        @AccountTypeValidation(value = AccountType.SA)
        private String mandatoryFieldForSavingsAccount;

        @RequiredFieldValidation
        @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
        private String mandatoryFieldForBoth;

        @RequiredFieldValidation
        private String mandatoryField;
    }

    @Test
    public void shouldValidateMandatoryFields() {
        SubClasstoBeValidatedAgainstAccType instance = new SubClasstoBeValidatedAgainstAccType();
        instance.mandatoryFieldForPCA = "value";
        instance.mandatoryFieldForBoth = "value2";
        // Not setting any value for mandatoryField
        ValidationError err = null;
        try {
            fieldValidator.validateInstanceFields(instance, AccountType.CA);
        } catch (ValidationException e) {
            err = e.getValidationError();
        }
        assertThat(err, is(new ValidationError("'mandatoryField' required to be set but it was 'null'")));
    }

    @Test
    public void shouldValidateMandatoryFieldsForPcaOnly() {
        SubClasstoBeValidatedAgainstAccType instance = new SubClasstoBeValidatedAgainstAccType();
        // Not setting any value for mandatoryFieldForPCA
        instance.mandatoryFieldForBoth = "value";
        instance.mandatoryField = "value2";
        boolean validateInstanceFields;
        ValidationError err = null;
        try {
            fieldValidator.validateInstanceFields(instance, AccountType.CA);
        } catch (ValidationException e) {
            err = e.getValidationError();
        }
        assertThat(err, is(new ValidationError("'mandatoryFieldForPCA' required to be set but it was 'null'")));
    }

    @Test
    public void shouldNotValidateSavingsMandatoryFieldsWhenPCARequest() throws ValidationException {
        SubClasstoBeValidatedAgainstAccType instance = new SubClasstoBeValidatedAgainstAccType();
        instance.mandatoryFieldForPCA = "value";
        instance.mandatoryFieldForBoth = "value1";
        instance.mandatoryField = "value2";
        // Not setting any value for mandatoryFieldForSavingsAccount, as this is
        // a PCA request

        assertTrue(fieldValidator.validateInstanceFields(instance, AccountType.CA));
    }

    @Test
    public void shouldValidateMandatoryFieldsForSavingsOnly() {
        SubClasstoBeValidatedAgainstAccType instance = new SubClasstoBeValidatedAgainstAccType();
        instance.mandatoryFieldForBoth = "value";
        instance.mandatoryField = "value2";
        // Not setting any value for mandatoryFieldForSavingsAccount
        ValidationError err = null;
        try {
            fieldValidator.validateInstanceFields(instance, AccountType.SA);
        } catch (ValidationException e) {
            err = e.getValidationError();
        }
        assertThat(err,
                is(new ValidationError("'mandatoryFieldForSavingsAccount' required to be set but it was 'null'")));
    }

    private PrimaryInvolvedParty primaryInvolvedPartyItems(LinkedHashSet<String> country) {
        LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("GBR");
        taxResidencyDetails.setTaxResidency("IND");
        taxResidencies.add(taxResidencyDetails);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty(SwitchOptions.No, true, 200, "Mr", "first",
                "middle", "last", "sk996@gmail.com", new Date(1935 - 03 - 05), "001",
                new PostalAddressComponent("0505", true, false, new PostalAddress()),
                new PostalAddressComponent("0505", true, false, new PostalAddress()), "001", "002", 2, "3", "SPORI",
                new ContactNumber("44", null, "98797987", null), new ContactNumber("44", null, "98797987", null),
                new ContactNumber("44", null, "98797987", null), true, true, true, true, null, null, null, null, "003",
                "016", null, null, null, 17500, null, null, null,null);
        primaryInvolvedParty.setBirthCity("leeds");
        TinDetails tinDetails = new TinDetails();
        tinDetails.setBirthCountry("United Kingdom");
        tinDetails.setNationalities(country);
        tinDetails.setTaxResidencies(taxResidencies);
        primaryInvolvedParty.setTinDetails(tinDetails);
        return primaryInvolvedParty;
    }

    private Arrangement arrangementItems(LinkedHashSet<String> country) throws ReferenceDataException {

        PrimaryInvolvedParty primaryInvolvedParty = primaryInvolvedPartyItems(country);
        ProductArrangement productArrangement = productArrangementItems("Current Account", "P_CLASSIC", null);
        return new Arrangement(primaryInvolvedParty, productArrangement, null);

    }

    private ProductArrangement productArrangementItems(String name, String mnemonic,
            List<ProductArrangementCondition> conditions) {
        return new ProductArrangement("Current Account", "P_CLASSIC", null);
    }

}