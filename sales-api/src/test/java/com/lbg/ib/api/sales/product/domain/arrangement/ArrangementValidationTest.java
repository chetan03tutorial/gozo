/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.arrangement;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.LinkedHashSet;

import com.lbg.ib.api.sales.common.validation.ValidationService;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.validation.CountryFieldValidation;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;

@RunWith(MockitoJUnitRunner.class)
public class ArrangementValidationTest {
    @Mock
    private LoggerDAO              logger;
    @Mock
    private CountryFieldValidation countryFieldValidation;

    @Mock
    private ValidationService      validationService = null;

    @InjectMocks
    private FieldValidator         fieldValidator    = new FieldValidator();

    @Test
    public void shouldPassForValidArrangement() throws Exception {
        Arrangement instance = happyArrangement();
        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        if (validationError != null)
            throw new IllegalStateException(validationError.getMessage());
    }

    @Test
    public void shouldThrowInvalidFormatExceptionWhenEmailValidationFails() throws Exception {
        Arrangement instance = invalidEmailArrangement();
        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        assertThat(validationError.getMessage(), containsString("email"));
    }

    @Test
    public void shouldThrowInvalidFormatExceptionWhenPostalAddressComponentRelatedValidation() throws Exception {
        Arrangement instance = invalidDurationOfStayArrangement();
        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        assertThat(validationError.getMessage(), containsString("durationOfStay"));
    }

    @Test
    public void shouldThrowInvalidFormatExceptionWhenPostalAddressComponentPostcodeRelatedValidation()
            throws Exception {
        Arrangement instance = invalidPostalAddress();
        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        assertThat(validationError.getMessage(), containsString("postcode"));
    }

    @Test
    public void shouldPassForValidCrossSellSavingsArrangement() throws Exception {
        Arrangement instance = validArrangementForCrossSellSavingsRequest();
        ValidationError validationError = fieldValidator.validateInstanceFields(instance);
        if (validationError != null)
            throw new IllegalStateException(validationError.getMessage());
    }

    private Arrangement happyArrangement() {
        return getArrangement("paulrm6@gmail.com", "0401", "E27PF");
    }

    private Arrangement invalidPostalAddress() {
        return getArrangement("paulrm6@gmail.com", "0401", "EPF");
    }

    private Arrangement invalidDurationOfStayArrangement() {
        return getArrangement("paulrm6@gmail.com", "041", "E27PF");
    }

    private Arrangement invalidEmailArrangement() {
        return getArrangement("paulrm6gmail.com", "0401", "E27PF");
    }

    private Arrangement validArrangementForCrossSellSavingsRequest() {
        return getArrangementForCrossSellSavings("paulrm6@gmail.com", "0401", "E27PF");
    }

    private Arrangement getArrangement(String email, String durationOfStay, String postcode) {
        PostalAddress address1 = new PostalAddress("BETHNAL GREEN", "LONDON", "SOMEWHERE", "org", "sub", "", "8",
                asList("PELTER STREET"), postcode, "2D");
        PostalAddressComponent current = new PostalAddressComponent(durationOfStay, true, false, address1);
        LinkedHashSet<String> country = new LinkedHashSet<String>();
        country.add("GBR");
        country.add("IND");
        LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("GBR");
        taxResidencyDetails.setTaxResidency("IND");
        taxResidencies.add(taxResidencyDetails);
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty(SwitchOptions.No, false, 200, "Mr", "Paul", "Robert",
                "MacDonald", email, new Date(), "001", current, null, "001", "004", 0, "1", "SPORI",
                new ContactNumber("44", null, "234234234", null), new ContactNumber("44", "234234", "234234", "2342"),
                new ContactNumber("44", "234234", "234234234234", null), true, true, true, true, null, null, false,
                3200, "003", "007", new EmployerDetails("Lloyds", "Chiswell", "London", "E61AA", 4, 9), 300, 10, 12500,
                3, 4, null,null);
        pip.setBirthCity("leeds");
        TinDetails tinDetails = new TinDetails();
        tinDetails.setBirthCountry("United Kingdom");
        tinDetails.setNationalities(country);
        tinDetails.setTaxResidencies(taxResidencies);
        pip.setTinDetails(tinDetails);
        return new Arrangement(pip, new ProductArrangement("Classic Account", "P_CLASSIC",
                asList(new ProductArrangementCondition("Condition name", 5, "Value"))), null);
    }

    private Arrangement getArrangementForCrossSellSavings(String email, String durationOfStay, String postcode) {
        PostalAddress address1 = new PostalAddress("BETHNAL GREEN", "LONDON", "SOMEWHERE", "org", "sub", "", "8",
                asList("PELTER STREET"), postcode, "2D");
        PostalAddressComponent current = new PostalAddressComponent(durationOfStay, true, false, address1);
        LinkedHashSet<String> country = new LinkedHashSet<String>();
        country.add("GBR");
        country.add("IND");
        LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("GBR");
        taxResidencyDetails.setTaxResidency("IND");
        taxResidencies.add(taxResidencyDetails);
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty(SwitchOptions.No, false, 200, "Mr", "Paul", "Robert",
                "MacDonald", email, new Date(), "001", current, null, "001", "004", 0, "1", "SPORI",
                new ContactNumber("44", null, "234234234", null), new ContactNumber("44", "234234", "234234", "2342"),
                new ContactNumber("44", "234234", "234234234234", null), true, true, true, true, null, null, false,
                3200, "003", "007", new EmployerDetails("Lloyds", "Chiswell", "London", "E61AA", 4, 9), 300, 10, 12500,
                3, 4, null,null);
        pip.setBirthCity("leeds");
        TinDetails tinDetails = new TinDetails();
        tinDetails.setBirthCountry("United Kingdom");
        tinDetails.setNationalities(country);
        tinDetails.setTaxResidencies(taxResidencies);
        pip.setTinDetails(tinDetails);
        RelatedApplication relApplication = new RelatedApplication();
        relApplication.setApplicationId("relAppId");
        relApplication.setApplicationStatus("relAppStatus");
        return new Arrangement(pip, new ProductArrangement("Classic Account", "P_CLASSIC",
                asList(new ProductArrangementCondition("Condition name", 5, "Value"))), relApplication);
    }

}