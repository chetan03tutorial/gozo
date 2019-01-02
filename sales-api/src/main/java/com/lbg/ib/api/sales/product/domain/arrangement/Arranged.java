/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.List;
import java.util.Set;

import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;

public class Arranged {

    private String                  name;

    private String                  mnemonic;

    private String                  applicationStatus;

    private String                  applicationSubStatus;

    private String                  arrangementId;

    private String                  offerType;                // Normal-2001,
                                                              // Upsell-2002,
                                                              // Typical-2004

    private List<ProductAttributes> productAttributes;        // This will be
                                                              // populated
                                                              // only for a
                                                              // downsell
                                                              // product

    private Boolean                 isIBMandate       = false;

    private Boolean                 isIA              = false;

    private Boolean                 isPwdActive       = false;

    private List<Condition>         conditions;

    private Boolean                 isVantageEligible = false;

    private Overdraft               overdraft;

    private Set<String>             bankHolidays;             // contains
                                                              // holiday list
                                                              // helps to
                                                              // select
                                                              // the switch
                                                              // date

    private String                  eidvScore;

    private String                  asmScore;

    private String                  ocisId;

    private String                  partyId;

    private Condition               declineReason;

    private List<MarketingPreference> marketingPreferences;

    private SIRAScoreDTO siraScoreDTO;

    private String currentDate;

    public Arranged(final String name, final String mnemonic, final String applicationStatus, final String applicationSubStatus,
            final String arrangementId, final String offerType, final List<ProductAttributes> productAttributes,
        final List<Condition> conditions, final List<MarketingPreference> marketingPreferences) {
        this.setName(name);
        this.setMnemonic(mnemonic);
        this.applicationStatus = applicationStatus;
        this.applicationSubStatus = applicationSubStatus;
        this.arrangementId = arrangementId;
        this.offerType = offerType;
        this.productAttributes = productAttributes;
        this.conditions = conditions;
        this.marketingPreferences = marketingPreferences;
    }

    public Arranged() {/* jackson */
    }

    public String getName() {
        return name;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getApplicationSubStatus() {
        return applicationSubStatus;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public String getOfferType() {
        return offerType;
    }

    public List<ProductAttributes> getProductAttributes() {
        return productAttributes;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Boolean getIsIBMandate() {
        return isIBMandate;
    }

    public void setIsIBMandate(final Boolean isIBMandate) {
        this.isIBMandate = isIBMandate;
    }

    public Boolean getIsIA() {
        return isIA;
    }

    public void setIsIA(final Boolean isIA) {
        this.isIA = isIA;
    }

    public Boolean getIsPwdActive() {
        return isPwdActive;
    }

    public void setIsPwdActive(final Boolean isPwdActive) {
        this.isPwdActive = isPwdActive;
    }

    public Boolean getIsVantageEligible() {
        return isVantageEligible;
    }

    public void setIsVantageEligible(final Boolean isVantageEligible) {
        this.isVantageEligible = isVantageEligible;
    }

    public Overdraft getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(final Overdraft overdraft) {
        this.overdraft = overdraft;
    }

    public Set<String> getBankHolidays() {
        return bankHolidays;
    }

    public void setBankHolidays(final Set<String> bankHolidays) {
        this.bankHolidays = bankHolidays;
    }

    public String getEidvScore() {
        return eidvScore;
    }

    public void setEidvScore(final String eidvScore) {
        this.eidvScore = eidvScore;
    }

    public String getAsmScore() {
        return asmScore;
    }

    public void setAsmScore(final String asmScore) {
        this.asmScore = asmScore;
    }

    public Condition getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(final Condition declineReason) {
        this.declineReason = declineReason;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(final String ocisId) {
        this.ocisId = ocisId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(final String partyId) {
        this.partyId = partyId;
    }

    public List<MarketingPreference> getMarketingPreferences() {
    return marketingPreferences;
    }

    public void setMarketingPreferences(final List<MarketingPreference> marketingPreferences) {
    this.marketingPreferences = marketingPreferences;
    }

    public SIRAScoreDTO getSiraScoreDTO() {
        return siraScoreDTO;
    }

    public void setSiraScoreDTO(SIRAScoreDTO siraScoreDTO) {
        this.siraScoreDTO = siraScoreDTO;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public boolean equals(final Object o) {
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
