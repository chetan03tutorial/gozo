/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.activate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.common.validation.ValidationService;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.product.rules.ActivationRuleValidator;

@RunWith(MockitoJUnitRunner.class)
public class ActivationTest {
    private ActivationRuleValidator     ruleValidator     = new ActivationRuleValidator();
    private BigDecimal                  amount            = null;
    private Overdraft                   overdraftFeatures = new Overdraft();
    private String                      sortCode          = "402715";
    private String                      accNo             = "12345678";
    private String                      accName           = "abc";
    private Boolean                     hasDebitCard      = false;
    private Boolean                     canBeOverDrawn    = false;
    private String                      cardNumber        = null;
    private String                      cardExpiryDate    = null;
    private Boolean                     textAlert         = false;
    private String                      mobileNo          = null;
    private Date                        switchDate        = null;
    private Boolean                     consent           = false;
    private ArrangeToActivateParameters params            = null;
    @Mock
    private LoggerDAO                   logger;

    @InjectMocks
    private FieldValidator              fieldValidator    = new FieldValidator();

    @Mock
    private ValidationService           validationService = null;

    @Test
    public void shouldBeValidActivationRequest() throws Exception {
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1wor"), null, null, null, null,
                false, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"), new Condition[0], null, null,
                null, false, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, null, null, null, false, "productId", null, null,
                null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        overdraftFeatures.setAmount(amount);
        mobileNo = "1234567890";
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldFailInUserNameValidation() throws Exception {
        Activation instance = new Activation(new InvolvedPartyRole("userNa", "pass1wor"), null, null, null, null, false,
                "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance).getMessage(), containsString("userName"));
        instance = new Activation(new InvolvedPartyRole("userNa12111adasdasdasdasdasdasdas1", "pass1wor"), null, null,
                null, null, false, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance).getMessage(), containsString("userName"));
    }

    @Test
    public void shouldFailForCondition() throws Exception {
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1wor"),
                new Condition[] { new Condition("123", 5, "val") }, null, null, null, false, "productId", null, null,
                null, null);
        assertThat(fieldValidator.validateInstanceFields(instance).getMessage(), containsString("name"));
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1wor"),
                new Condition[] { new Condition("asd", 5, "111") }, null, null, null, false, "productId", null, null,
                null, null);
        //assertThat(fieldValidator.validateInstanceFields(instance).getMessage(), containsString("value"));
    }

    @Test
    public void shouldBeValidWhenUserNameAndPasswordAreNotSpecified() throws Exception {
        Activation instance = new Activation(new InvolvedPartyRole("", ""),
                new Condition[] { new Condition("asd", 5, "val") }, null, null, null, false, "productId", null, null,
                null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        instance = new Activation(new InvolvedPartyRole(null, null), new Condition[] { new Condition("asd", 5, "val") },
                null, null, null, false, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldFailInOverDrawnDtlsForAccSwitchValidation() throws Exception {
        amount = new BigDecimal("200");
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(ruleValidator.validateOverDrawnDtlsForAccSwitch(instance).getMessage(),
                containsString("The overdraft amount should be null if you dont want to be overdrawn"));

        amount = null;
        canBeOverDrawn = true;
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);

        assertTrue(ruleValidator.validateOverDrawnDtlsForAccSwitch(instance) == null);
    }

    @Test
    public void shouldFailInDebitCardDtlsForAccSwitchValidation() throws Exception {
        cardNumber = "1234567890123456";
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"),
                overdraftFeatures, populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, null,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(ruleValidator.validateDebitCardDtlsForAccSwitch(instance).getMessage(),
                containsString("If you entered Card number then card expiry date is mandatory"));

        cardExpiryDate = "1218";
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"),
                overdraftFeatures, populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, null, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(ruleValidator.validateDebitCardDtlsForAccSwitch(instance).getMessage(),
                containsString("If you entered Card expiry date then card number is mandatory"));
    }

    @Test
    public void shouldPassInDebitCardDtlsForAccSwitchValidation() throws Exception {
        cardNumber = "1234567890123456";
        cardExpiryDate = "1218";
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertEquals(ruleValidator.validateDebitCardDtlsForAccSwitch(instance), null);

        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"),
                overdraftFeatures, populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, null, null,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertEquals(ruleValidator.validateDebitCardDtlsForAccSwitch(instance), null);
    }

    @Test
    public void shouldFailInODAmtOptedValidation() throws Exception {
        amount = new BigDecimal("200");
        overdraftFeatures.setAmount(amount);
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        ArrangeToActivateParameters session = new ArrangeToActivateParameters();
        OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();
        BigDecimal amtOverdraft = new BigDecimal("100");
        overdraftIntrestRates.setAmtOverdraft(amtOverdraft);
        session.setOverdraftIntrestRates(overdraftIntrestRates);
        assertThat(ruleValidator.validateODAmtOpted(instance, session).getMessage(),
                containsString("OverDraft amount requested cannot be greater than the overdraft amount offered"));

        session = null;
        assertThat(ruleValidator.validateODAmtOpted(instance, session).getMessage(), containsString(
                "Cannot request overdraft amount as session or overdraft amount from offer response is not available"));
    }

    @Test
    public void shouldFailInTextAlertDtlsValidation() throws Exception {
        mobileNo = "1234567890";
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(ruleValidator.validateTextAlertDtlsForAccSwitch(instance).getMessage(),
                containsString("Mobile number should be null if you dont wish to have text alerts"));

        textAlert = true;
        mobileNo = null;
        instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                false, "productId", null, null, null, null);
        assertThat(ruleValidator.validateTextAlertDtlsForAccSwitch(instance).getMessage(),
                containsString("Mobile number cannot be null if you  wish to have text alerts"));
    }

    @Test
    public void shouldFailInVantageOptedValidation() throws Exception {
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                true, "productId", null, null, null, null);
        ArrangeToActivateParameters params = null;
        assertThat(ruleValidator.validateVantageOpted(instance, params).getMessage(),
                containsString("Cannot opt for vantage as no product has been selected"));

        params = new ArrangeToActivateParameters();
        params.setVantagePrdIdentifier(null);
        assertThat(ruleValidator.validateVantageOpted(instance, params).getMessage(),
                containsString("Cannot opt for Vantage as the product offered does not have vantage eligibility"));

    }

    @Test
    public void shouldBeValidActivationRequestWithNewParamsAdded() throws Exception {

        amount = new BigDecimal("200");
        overdraftFeatures.setAmount(amount);
        sortCode = "123456";
        accNo = "12345678";
        accName = "abc";
        hasDebitCard = true;
        cardNumber = "1234567890123456";
        cardExpiryDate = "09/16";
        canBeOverDrawn = true;
        textAlert = true;
        mobileNo = "1234567890";
        switchDate = new Date();
        consent = true;
        OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();
        overdraftIntrestRates.setAmtOverdraft(amount);
        params = new ArrangeToActivateParameters();
        params.setArrangementType("CA");
        params.setApplicationType("abc");
        params.setOcisId("1234");
        params.setPartyId("0123456");
        params.setOverdraftIntrestRates(overdraftIntrestRates);
        params.setVantagePrdIdentifier("1234");
        Activation instance = new Activation(new InvolvedPartyRole("userNameOfMe", "pass1word12"),
                new Condition[] { new Condition("n_m", 5, "val") }, new Location("10", "20"), overdraftFeatures,
                populateAccSwitchDtls(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate,
                        canBeOverDrawn, amount, textAlert, mobileNo, switchDate, consent),
                true, "productId", null, null, null, null);
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
        //assertThat(ruleValidator.validateRules(instance, params), is(nullValue()));
    }

    private AccountSwitching populateAccSwitchDtls(String sortCode, String accNo, String accName, Boolean hasDebitCard,
            String cardNumber, String cardExpiryDate, Boolean canBeOverDrawn, BigDecimal amount, Boolean textAlert,
            String mobileNo, Date switchDate, Boolean consent) {
        return new AccountSwitching(sortCode, accNo, accName, hasDebitCard, cardNumber, cardExpiryDate, canBeOverDrawn,
                amount, textAlert, mobileNo, switchDate, consent);
    }
}