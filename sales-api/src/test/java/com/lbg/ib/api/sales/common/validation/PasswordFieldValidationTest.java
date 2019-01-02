package com.lbg.ib.api.sales.common.validation;

import org.junit.Test;

import static org.junit.Assert.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class PasswordFieldValidationTest {
    @Test
    public void shouldFail() throws Exception {
        PasswordFieldValidation validation = new PasswordFieldValidation();
        assertTrue(validation.doesFail(null));
        assertTrue(validation.doesFail(""));
        assertTrue(validation.doesFail("aaaa123-"));
        assertTrue(validation.doesFail("a1231aa s"));
        assertTrue(validation.doesFail("a1231aasssssssssssss"));
        assertTrue(validation.doesFail("a1ab"));
    }

    @Test
    public void shouldPass() throws Exception {
        PasswordFieldValidation validation = new PasswordFieldValidation();
        assertFalse(validation.doesFail("as1dA11"));

    }

}