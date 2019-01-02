package com.lbg.ib.api.sales.dto.product.offer;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class EIDVScoreDTO {
    private final String scoreResult;
    private final String assessmentType;
    private final String evidenceIdentifier;
    private final String identityStrength;
    private final String code;
    private final String description;

    public String getScoreResult() {
        return scoreResult;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getEvidenceIdentifier() {
        return evidenceIdentifier;
    }

    public String getIdentityStrength() {
        return identityStrength;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public EIDVScoreDTO(String scoreResult, String assessmentType, String evidenceIdentifier, String identityStrength,
            String code, String description) {
        this.scoreResult = scoreResult;
        this.assessmentType = assessmentType;
        this.evidenceIdentifier = evidenceIdentifier;
        this.identityStrength = identityStrength;
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
