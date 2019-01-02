package com.lbg.ib.api.sales.dto.product.offer;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ASMScoreDTO {
    private final String scoreResult;
    private final String assessmentType;
    private final String decisionCode;
    private final String decisionText;
    private final String code;
    private final String description;

    public String getScoreResult() {
        return scoreResult;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getDecisionCode() {
        return decisionCode;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public ASMScoreDTO(String scoreResult, String assessmentType, String decisionCode, String decisionText, String code,
            String description) {
        this.scoreResult = scoreResult;
        this.assessmentType = assessmentType;
        this.decisionCode = decisionCode;
        this.decisionText = decisionText;
        this.code = code;
        this.description = description;
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
