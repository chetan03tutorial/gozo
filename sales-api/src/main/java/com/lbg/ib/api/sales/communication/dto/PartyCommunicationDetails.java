package com.lbg.ib.api.sales.communication.dto;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;

import java.util.Map;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.EMAIL;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by rabaja on 16/11/2016.
 */
public class PartyCommunicationDetails {

    @RequiredFieldValidation
    private String templateId;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = EMAIL, maxLength = 80)
    private String[] recipientEmails;

    @RequiredFieldValidation
    private Map<String, String> tokenMap;

    @RequiredFieldValidation
    private String emailCommunicationType;

    public String getEmailCommunicationType() {
        return emailCommunicationType;
    }

    public void setEmailCommunicationType(String emailCommunicationType) {
        this.emailCommunicationType = emailCommunicationType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Map<String, String> getTokenMap() {
        return tokenMap;
    }

    public void setTokenMap(Map<String, String> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String[] getRecipientEmail() {
        return recipientEmails;
    }

    public void setRecipientEmail(String[] recipientEmails) {
        this.recipientEmails = recipientEmails;
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
