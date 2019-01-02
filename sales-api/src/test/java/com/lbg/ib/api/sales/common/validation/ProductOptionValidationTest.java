package com.lbg.ib.api.sales.common.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductOptionValidationTest {

    @Test
    public void testDoesFailForInvalidObject() throws IOException {
        ProductOptionValidation productOptionValidation = new ProductOptionValidation();
        boolean status = productOptionValidation.doesFail("abc");
        assertTrue(status);
    }

    @Test
    public void testDoesFailForValidObject() throws IOException {
        ProductOptionValidation productOptionValidation = new ProductOptionValidation();
        boolean status = productOptionValidation.doesFail("MOPI");
        assertFalse(status);
    }

    @Test
    public void testDoesFailForNullObject() throws IOException {
        ProductOptionValidation productOptionValidation = new ProductOptionValidation();
        boolean status = productOptionValidation.doesFail(null);
        assertFalse(status);
    }

    @Test
    public void testMessage() throws IOException {
        ProductOptionValidation productOptionValidation = new ProductOptionValidation();
        String message = productOptionValidation.message();
        assertEquals(message, "Invalid Question Code");
    }

}
