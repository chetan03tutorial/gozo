package com.lbg.ib.api.sales.address.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.POSTCODE;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

import com.lbg.ib.api.shared.exception.InvalidFormatException;

public class Postcode {

    private String         outPostcode;
    private String         inPostcode;
    private static Pattern regex = compile(POSTCODE);

    public Postcode(String rawPostcode) throws InvalidFormatException {
        String validated = validate(rawPostcode);
        this.outPostcode = validated.substring(0, validated.length() - 3);
        this.inPostcode = validated.substring(validated.length() - 3);
    }

    private String validate(String raw) throws InvalidFormatException {
        String formatted = raw.replace(" ", "").toUpperCase();
        if (regex.matcher(formatted).find()) {
            return formatted;
        }
        throw new InvalidFormatException(format("'%s' cannot be parsed as postcode", raw));
    }

    public String inPostcode() {
        return inPostcode;
    }

    public String outPostcode() {
        return outPostcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Postcode postcode = (Postcode) o;

        if (outPostcode != null ? !outPostcode.equals(postcode.outPostcode) : postcode.outPostcode != null) {
            return false;
        }
        return !(inPostcode != null ? !inPostcode.equals(postcode.inPostcode) : postcode.inPostcode != null);

    }

    @Override
    public int hashCode() {
        int result = outPostcode != null ? outPostcode.hashCode() : 0;
        result = 31 * result + (inPostcode != null ? inPostcode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Postcode{" + "outPostcode='" + outPostcode + '\'' + ", inPostcode='" + inPostcode + '\'' + '}';
    }
}
