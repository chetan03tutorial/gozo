package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class CustomerScore {
    private String scoreResult;
    private String decisionCode;
    private String decisionText;

    public CustomerScore() {
        /* jackson */}

    public CustomerScore(String scoreResult, String decisionCode, String decisionText) {
        this.scoreResult = scoreResult;
        this.decisionCode = decisionCode;
        this.decisionText = decisionText;
    }

    public String getScoreResult() {
        return scoreResult;
    }

    public String getDecisionCode() {
        return decisionCode;
    }

    public String getDecisionText() {
        return decisionText;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
