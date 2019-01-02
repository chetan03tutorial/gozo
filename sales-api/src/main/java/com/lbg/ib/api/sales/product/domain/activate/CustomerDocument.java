package com.lbg.ib.api.sales.product.domain.activate;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.OPTIONAL_ALPHA;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_3_DIGITS;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.io.Serializable;

/**
 * Created by dbhatt on 9/23/2016.
 */
import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class CustomerDocument implements Serializable {
    @StringFieldValidation(maxLength = ValidationConstants.NUMBER_THIRTY)
    private String documentPurpose;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_3_DIGITS, maxLength = ValidationConstants.NUMBER_THREE)
    private String documentType;

    @StringFieldValidation(maxLength = ValidationConstants.NUMBER_HUNDRED)
    private String documentReferenceText;

    @RequiredFieldValidation
    @StringFieldValidation(maxLength = ValidationConstants.NUMBER_FORTY)
    private String documentReferenceIndex;

    @StringFieldValidation(pattern = OPTIONAL_ALPHA, maxLength = ValidationConstants.NUMBER_THREE)
    private String documentCountryOfIssue;
    @StringFieldValidation(pattern = OPTIONAL_ALPHA, maxLength = ValidationConstants.NUMBER_HUNDRED)
    private String documentAdditionalInfo;

    public CustomerDocument() {
        // default comments for Sonar violations avoidance.
    }

    public String getDocumentPurpose() {
        return this.documentPurpose;
    }

    public void setDocumentPurpose(String documentPurpose) {
        this.documentPurpose = documentPurpose;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentReferenceText() {
        return this.documentReferenceText;
    }

    public void setDocumentReferenceText(String documentReferenceText) {
        this.documentReferenceText = documentReferenceText;
    }

    public String getDocumentReferenceIndex() {
        return this.documentReferenceIndex;
    }

    public void setDocumentReferenceIndex(String documentReferenceIndex) {
        this.documentReferenceIndex = documentReferenceIndex;
    }

    public String getDocumentCountryOfIssue() {
        return this.documentCountryOfIssue;
    }

    public void setDocumentCountryOfIssue(String documentCountryOfIssue) {
        this.documentCountryOfIssue = documentCountryOfIssue;
    }

    public String getDocumentAdditionalInfo() {
        return this.documentAdditionalInfo;
    }

    public void setDocumentAdditionalInfo(String documentAdditionalInfo) {
        this.documentAdditionalInfo = documentAdditionalInfo;
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
