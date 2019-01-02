package com.lbg.ib.api.sales.address.domain;

import com.lbg.ib.api.shared.exception.InvalidFormatException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PostcodeTest {
    @Test
    public void shouldParseAllPostcodeFormatsCorrectly() throws Exception {
        Postcode postcode = new Postcode("e1 8ep");
        assertThat(postcode.outPostcode(), is("E1"));
        assertThat(postcode.inPostcode(), is("8EP"));
        assertThat(postcode, is(new Postcode("e18EP")));

        postcode = new Postcode("w1a 5xx");
        assertThat(postcode.outPostcode(), is("W1A"));
        assertThat(postcode.inPostcode(), is("5XX"));
        assertThat(postcode, is(new Postcode("w1A5xx ")));

        postcode = new Postcode("RH10 0ny");
        assertThat(postcode.outPostcode(), is("RH10"));
        assertThat(postcode.inPostcode(), is("0NY"));
        assertThat(postcode, is(new Postcode("rh100ny")));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowInvalidFormatExceptionWhenPostcodeIsShorterThan5Characters() throws Exception {
        new Postcode("N1 8e");
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowInvalidFormatExceptionWhenPostcodeIsLongerThan7Characters() throws Exception {
        new Postcode("RH108epx");
    }

}