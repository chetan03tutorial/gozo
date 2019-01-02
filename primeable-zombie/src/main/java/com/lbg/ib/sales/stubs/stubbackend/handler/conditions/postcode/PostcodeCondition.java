package com.lbg.ib.sales.stubs.stubbackend.handler.conditions.postcode;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.lbg.ib.sales.stubs.stubbackend.handler.conditions.xpath.XmlEntityXpathMatchesCondition.xpath;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class PostcodeCondition implements Condition {

    private String in;
    private String out;

    public PostcodeCondition(String out, String in) {
        this.in = in;
        this.out = out;
    }

    public boolean matches(HttpExchange httpExchange) {
        return xpath("//addressComponents[type/text()='In Postal Code']/value").
                valueIs(in).matches(httpExchange) &&
                xpath("//addressComponents[type/text()='Out Postal Code']/value").
                        valueIs(out).matches(httpExchange);
    }

    public static Condition matchesWithPostcode(String out, String in) {
        return new PostcodeCondition(out, in);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PostcodeCondition that = (PostcodeCondition) o;

        return new EqualsBuilder()
                .append(in, that.in)
                .append(out, that.out)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(in)
                .append(out)
                .toHashCode();
    }
}
