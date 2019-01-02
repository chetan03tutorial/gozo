package com.lbg.ib.api.sales.common.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import com.lbg.ib.api.sales.user.domain.AddParty;
import com.lbg.ib.api.shared.validation.OptionalFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceImplTest {

    @Mock
    private SessionManagementDAO  session;

    @InjectMocks
    private ValidationServiceImpl ValidationServiceImpl;

    @Test
    public void testIsOptionalWhenUserInfoIsNotInSession() {
        when(session.getUserInfo()).thenReturn(null);
        boolean isOptional = ValidationServiceImpl.isOptional();
        assertFalse(isOptional);
    }

    @Test
    public void testIsOptionalWhenUserInfoIsInSession() {
        Arrangement arrangement = new Arrangement();
        when(session.getUserInfo()).thenReturn(arrangement);
        boolean isOptional = ValidationServiceImpl.isOptional();
        assertTrue(isOptional);
    }

    @Test
    public void testIsOptionalForFeildForBranchAuth() {
        SubClassForOptionalValidated subClass = new SubClassForOptionalValidated();
        Field[] fields = subClass.getClass().getDeclaredFields();
        Arrangement arrangement = new Arrangement();
        when(session.getUserInfo()).thenReturn(arrangement);
        BranchContext context = new BranchContext();
        when(session.getBranchContext()).thenReturn(context);
        boolean isOptional = ValidationServiceImpl.isOptional(fields[0]);
        assertTrue(isOptional);
    }

    @Test
    public void testIsOptionalForFeildForAuth() {
        SubClassForOptionalValidated subClass = new SubClassForOptionalValidated();
        Field[] fields = subClass.getClass().getDeclaredFields();
        Arrangement arrangement = new Arrangement();
        when(session.getUserInfo()).thenReturn(arrangement);
        when(session.getBranchContext()).thenReturn(null);
        boolean isOptional = ValidationServiceImpl.isOptional(fields[0]);
        assertTrue(isOptional);
    }

    @Test
    public void testIsOptionalForFeildForBranch() {
        SubClassForOptionalValidated subClass = new SubClassForOptionalValidated();
        Field[] fields = subClass.getClass().getDeclaredFields();

        when(session.getUserInfo()).thenReturn(null);
        BranchContext context = new BranchContext();
        when(session.getBranchContext()).thenReturn(context);
        AddParty addParty = new AddParty();
        addParty.setNewParty(true);
        when(session.getAddPartyContext()).thenReturn(addParty);
        boolean isOptional = ValidationServiceImpl.isOptional(fields[0]);
        assertTrue(isOptional);
    }

    @Test
    public void testIsOptionalForFeildForUnAuth() {
        SubClassForOptionalValidated subClass = new SubClassForOptionalValidated();
        Field[] fields = subClass.getClass().getDeclaredFields();
        when(session.getUserInfo()).thenReturn(null);
        when(session.getBranchContext()).thenReturn(null);
        AddParty addParty = new AddParty();
        addParty.setExistingParty(true);
        when(session.getAddPartyContext()).thenReturn(addParty);
        boolean isOptional = ValidationServiceImpl.isOptional(fields[0]);
        assertTrue(isOptional);
    }

    @Test
    public void testIsOptionalForFeildForMandatory() {
        SubClassForMandatoryValidate subClass = new SubClassForMandatoryValidate();
        Field[] fields = subClass.getClass().getDeclaredFields();
        boolean isOptional = ValidationServiceImpl.isOptional(fields[0]);
        assertFalse(isOptional);
    }

    @Validate
    private static class SubClassForOptionalValidated {
        @OptionalFieldValidation(values = { "auth", "branch", "unAuth" })
        private String subField;
    }

    private static class SubClassForMandatoryValidate {
        private String subField;
    }

}
