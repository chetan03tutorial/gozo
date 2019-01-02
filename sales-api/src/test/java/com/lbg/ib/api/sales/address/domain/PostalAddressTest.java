package com.lbg.ib.api.sales.address.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.OPTIONAL_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS_BRACKET;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import org.junit.Test;

public class PostalAddressTest {
    private static Pattern regex = compile(OPTIONAL_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS_BRACKET);

    @Test
    public void shouldParseAllowedSpecialCharacters() throws Exception {
        PostalAddress address = new PostalAddress("district", null, "county,", "organisationName", null,
                "buildingName 90()", "buildingNumber", null, "postcode", "deliveryPointSuffix'");
        String strAddress = address.getBuildingName();
        assertThat(regex.matcher(strAddress).find(), is(true));
        strAddress = address.getBuildingNumber();
        assertThat(regex.matcher(strAddress).find(), is(true));
        strAddress = address.getCounty();
        assertThat(regex.matcher(strAddress).find(), is(true));
        strAddress = address.getDeliveryPointSuffix();

    }

    @Test
    public void shouldNotParseNotAllowedSpecialCharacters() throws Exception {
        PostalAddress address = new PostalAddress(null, null, "county;", null, null, "buildingName 90*",
                "buildingNumber$", null, null, null);
        String strAddress = address.getBuildingName();
        assertThat(regex.matcher(strAddress).find(), is(false));
        strAddress = address.getBuildingNumber();
        assertThat(regex.matcher(strAddress).find(), is(false));
        strAddress = address.getCounty();
        assertThat(regex.matcher(strAddress).find(), is(false));

    }

}