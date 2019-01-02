package com.lbg.ib.api.sales.product.domain.activate;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by dbhatt on 9/23/2016.
 */
public class AssessmentEvidence implements Serializable {
    @RequiredFieldValidation
    private String evidenceIdentifier;
    @RequiredFieldValidation
    private String addressStrength;
    @RequiredFieldValidation
    private String identityStrength;

    public AssessmentEvidence() {
        // default comments for Sonar violations avoidance.
    }

    public String getEvidenceIdentifier() {
        return this.evidenceIdentifier;
    }

    public void setEvidenceIdentifier(String evidenceIdentifier) {
        this.evidenceIdentifier = evidenceIdentifier;
    }

    public String getAddressStrength() {
        return this.addressStrength;
    }

    public void setAddressStrength(String addressStrength) {
        this.addressStrength = addressStrength;
    }

    public String getIdentityStrength() {
        return this.identityStrength;
    }

    public void setIdentityStrength(String identityStrength) {
        this.identityStrength = identityStrength;
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
