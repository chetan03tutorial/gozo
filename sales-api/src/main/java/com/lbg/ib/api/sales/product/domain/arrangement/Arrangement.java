/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class Arrangement {

    @RequiredFieldValidation
    private PrimaryInvolvedParty primaryInvolvedParty;

    @RequiredFieldValidation
    private ProductArrangement   productArrangement;

    private RelatedApplication   relatedApplication;

    @JsonIgnore
    private AccountType          accountType;

    private String               originatingSortCode;

    private String               colleagueId;

    private String               domain;

    private RelatedInvolvedParty relatedInvolvedParty;

    private String               existingPartyID;
    
    private boolean skipOverdraft;

    public Arrangement() {
        // default comments for Sonar violations avoidance.
    }

    public Arrangement(PrimaryInvolvedParty primaryInvolvedParty, ProductArrangement productArrangement,
            RelatedApplication relatedApplication) {
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.productArrangement = productArrangement;
        this.relatedApplication = relatedApplication;
    }

    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public ProductArrangement getProductArrangement() {
        return productArrangement;
    }

    public RelatedApplication getRelatedApplication() {
        return relatedApplication;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public RelatedInvolvedParty getRelatedInvolvedParty() {
        return relatedInvolvedParty;
    }

    public void setRelatedInvolvedParty(RelatedInvolvedParty relatedInvolvedParty) {
        this.relatedInvolvedParty = relatedInvolvedParty;
    }

    public String getExistingPartyID() {
        return existingPartyID;
    }

    public void setExistingPartyID(String existingPartyID) {
        this.existingPartyID = existingPartyID;
    }
    


    public boolean isSkipOverdraft() {
        return skipOverdraft;
    }

    public void setSkipOverdraft(boolean skipOverdraft) {
        this.skipOverdraft = skipOverdraft;
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
