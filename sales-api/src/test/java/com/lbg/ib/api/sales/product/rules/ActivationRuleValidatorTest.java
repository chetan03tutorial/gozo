package com.lbg.ib.api.sales.product.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@RunWith(MockitoJUnitRunner.class)
public class ActivationRuleValidatorTest {

    @InjectMocks
    private ActivationRuleValidator activationRuleValidator = new ActivationRuleValidator();

    @Test
    public void testValidateAccountDetailsForAddParty_null_details() {
        Activation activation = mock(Activation.class);
        when(activation.getAccountNumber()).thenReturn(null);

        Arrangement arrangement = mock(Arrangement.class);
        ValidationError validationError = activationRuleValidator.validateAccountDetailsForAddParty(activation, arrangement);

        assertNotNull(validationError);
        assertEquals("Any of AccountNumber, CbsProductId, SortCode, SellerLegalEntity is missing ", validationError.getMessage());
    }

    @Test
    public void testValidateAccountDetailsForAddParty_empty_account_details() {
        Activation activation = mock(Activation.class);
        when(activation.getAccountNumber()).thenReturn("12345");
        when(activation.getSortCode()).thenReturn("121212");
        when(activation.getCbsProductId()).thenReturn("1234567890");
        when(activation.getSellerLegalEntity()).thenReturn("LTB");

        Arrangement arrangement = mock(Arrangement.class);
        List<Account> accounts = new ArrayList<Account>();
        when(arrangement.getAccounts()).thenReturn(accounts);
        ValidationError validationError = activationRuleValidator.validateAccountDetailsForAddParty(activation, arrangement);

        assertNotNull(validationError);
    }

    @Test
    public void testValidateAccountDetailsForAddParty_valid_account_details() {
        Activation activation = mock(Activation.class);
        when(activation.getAccountNumber()).thenReturn("12345");
        when(activation.getSortCode()).thenReturn("121212");
        when(activation.getCbsProductId()).thenReturn("1234567890");
        when(activation.getSellerLegalEntity()).thenReturn("LTB");

        Arrangement arrangement = mock(Arrangement.class);
        List<Account> accounts = new ArrayList<Account>();
        Account account1 = new Account();
        account1.setAccountNumber("12345");
        account1.setAccountType("T1234567890");
        account1.setSortCode("121212");
        accounts.add(account1);
        when(arrangement.getAccounts()).thenReturn(accounts);
        ValidationError validationError = activationRuleValidator.validateAccountDetailsForAddParty(activation, arrangement);

        assertNull(validationError);
    }

    @Test
    public void testValidateAccountDetailsForAddParty_valid_account_details_multi() {
        Activation activation = mock(Activation.class);
        when(activation.getAccountNumber()).thenReturn("123456");
        when(activation.getSortCode()).thenReturn("121213");
        when(activation.getCbsProductId()).thenReturn("1234567899");
        when(activation.getSellerLegalEntity()).thenReturn("LTB");

        Arrangement arrangement = mock(Arrangement.class);
        List<Account> accounts = new ArrayList<Account>();
        Account account1 = new Account();
        account1.setAccountNumber("12345");
        account1.setAccountType("T1234567890");
        account1.setSortCode("121212");

        Account account2 = new Account();
        account2.setAccountNumber("123456");
        account2.setAccountType("1234567899");
        account2.setSortCode("121213");


        accounts.add(account1);
        accounts.add(account2);
        when(arrangement.getAccounts()).thenReturn(accounts);
        ValidationError validationError = activationRuleValidator.validateAccountDetailsForAddParty(activation, arrangement);

        assertNull(validationError);
    }

    @Test
    public void testValidateAccountDetailsForAddParty_valid_account_details_multi_sortcode() {
        Activation activation = mock(Activation.class);
        when(activation.getAccountNumber()).thenReturn("123456");
        when(activation.getSortCode()).thenReturn("121213");
        when(activation.getCbsProductId()).thenReturn("1234567899");
        when(activation.getSellerLegalEntity()).thenReturn("LTB");

        Arrangement arrangement = mock(Arrangement.class);
        List<Account> accounts = new ArrayList<Account>();
        Account account1 = new Account();
        account1.setAccountNumber("12345");
        account1.setAccountType("T1234567890");
        account1.setSortCode("121212");

        Account account2 = new Account();
        account2.setAccountNumber("123456");
        account2.setAccountType("T1234567899");
        account2.setSortCode("121214");


        accounts.add(account1);
        accounts.add(account2);
        when(arrangement.getAccounts()).thenReturn(accounts);
        ValidationError validationError = activationRuleValidator.validateAccountDetailsForAddParty(activation, arrangement);

        assertNotNull(validationError);
    }

}
