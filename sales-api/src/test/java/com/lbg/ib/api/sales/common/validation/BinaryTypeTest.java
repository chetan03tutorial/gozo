package com.lbg.ib.api.sales.common.validation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class BinaryTypeTest {

    @Test
    public void testValidBinaryStringY() {
        boolean rtnValue = BinaryType.validateBinaryString("Y");
        assertEquals(true, rtnValue);
    }

    @Test
    public void testValidBinaryStringN() {
        boolean rtnValue = BinaryType.validateBinaryString("N");
        assertEquals(true, rtnValue);
    }

    @Test
    public void testValidBinaryStringYES() {
        boolean rtnValue = BinaryType.validateBinaryString("YES");
        assertEquals(true, rtnValue);
    }

    @Test
    public void testValidBinaryStringNO() {
        boolean rtnValue = BinaryType.validateBinaryString("NO");
        assertEquals(true, rtnValue);
    }

    @Test
    public void testValidBinaryStringDONTKNOW() {
        boolean rtnValue = BinaryType.validateBinaryString("DONTKNOW");
        assertEquals(true, rtnValue);
    }

    @Test
    public void testInvalidBinaryString() {
        boolean rtnValue = BinaryType.validateBinaryString("YO");
        assertEquals(false, rtnValue);
    }

    @Test
    public void testEmptyBinaryString() {
        boolean rtnValue = BinaryType.validateBinaryString(" ");
        assertEquals(false, rtnValue);
    }
}
