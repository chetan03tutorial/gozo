package com.lbg.ib.api.sales.product.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;

public class ArrangeToActivateParameters {
    private String                  arrangementType;
    private String                  applicationType;
    private String                  ocisId;
    private String                  partyId;
    private OverdraftIntrestRates   overdraftIntrestRates;
    private TreeMap<String, String> crossSellMnemonicMap;
    private String                  sortcode;
    private String                  productName;
    private String                  productMnemonic;
    private String                  productId;
    private String                  productFamilyIdentifier;
    private String                  vantagePrdIdentifier;
    private String                  appStatus;
    private String                  alternateVantagePrdIdentifier;
    private String                  alternateVantageMnemonic;
    private String                  eidvStatus;
    private OverdraftIntrestRates   addPartyOverdraftIntrestRates;
    private String                  arrangementId;
    private BigDecimal              amtOverdraft;
    private Overdraft               overdraft;
    private String                  offeredProductId;
    private String                  offeredProductName;
    private String                  offeredProductMnemonic;
    private String                  asmScore;
    private String                  cbsProductNumberTrimmed;
    private String                  cbsProductNumber;
    private BigInteger              currentYearOfStudy;
    private Map<String, String>     productOptions;
    private String                  activationStatus;

    public OverdraftIntrestRates getAddPartyOverdraftIntrestRates() {
        return addPartyOverdraftIntrestRates;
    }

    public void setAddPartyOverdraftIntrestRates(OverdraftIntrestRates addPartyOverdraftIntrestRates) {
        this.addPartyOverdraftIntrestRates = addPartyOverdraftIntrestRates;
    }

    public String getArrangementType() {
        return arrangementType;
    }
    
    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }
    
    public String getApplicationType() {
        return applicationType;
    }
    
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }
    
    public String getOcisId() {
        return ocisId;
    }
    
    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }
    
    public String getPartyId() {
        return partyId;
    }
    
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
    
    public OverdraftIntrestRates getOverdraftIntrestRates() {
        return overdraftIntrestRates;
    }
    
    public void setOverdraftIntrestRates(OverdraftIntrestRates overdraftIntrestRates) {
        this.overdraftIntrestRates = overdraftIntrestRates;
    }
    
    public TreeMap<String, String> getCrossSellMnemonicMap() {
        return crossSellMnemonicMap;
    }
    
    public void setCrossSellMnemonicMap(TreeMap<String, String> crossSellMnemonicMap) {
        this.crossSellMnemonicMap = crossSellMnemonicMap;
    }
    
    public String getSortcode() {
        return sortcode;
    }
    
    public void setSortcode(String sortcode) {
        this.sortcode = sortcode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductMnemonic() {
        return productMnemonic;
    }
    
    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getProductFamilyIdentifier() {
        return productFamilyIdentifier;
    }
    
    public void setProductFamilyIdentifier(String productFamilyIdentifier) {
        this.productFamilyIdentifier = productFamilyIdentifier;
    }
    
    public String getVantagePrdIdentifier() {
        return vantagePrdIdentifier;
    }
    
    public void setVantagePrdIdentifier(String vantagePrdIdentifier) {
        this.vantagePrdIdentifier = vantagePrdIdentifier;
    }
    
    public String getAppStatus() {
        return appStatus;
    }
    
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
    
    public String getEidvStatus() {
        return eidvStatus;
    }
    
    public void setEidvStatus(String eidvStatus) {
        this.eidvStatus = eidvStatus;
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
    
    public String getAlternateVantagePrdIdentifier() {
        return alternateVantagePrdIdentifier;
    }
    
    public void setAlternateVantagePrdIdentifier(String alternateVantagePrdIdentifier) {
        this.alternateVantagePrdIdentifier = alternateVantagePrdIdentifier;
    }
    
    public String getAlternateVantageMnemonic() {
        return alternateVantageMnemonic;
    }
    
    public void setAlternateVantageMnemonic(String alternateVantageMnemonic) {
        this.alternateVantageMnemonic = alternateVantageMnemonic;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public BigDecimal getAmtOverdraft() {
        return amtOverdraft;
    }

    public void setAmtOverdraft(BigDecimal amtOverdraft) {
        this.amtOverdraft = amtOverdraft;
    }

    public String getOfferedProductId() {
        return offeredProductId;
    }

    public void setOfferedProductId(String offeredProductId) {
        this.offeredProductId = offeredProductId;
    }

    public String getOfferedProductName() {
        return offeredProductName;
    }

    public void setOfferedProductName(String offeredProductName) {
        this.offeredProductName = offeredProductName;
    }

    public String getOfferedProductMnemonic() {
        return offeredProductMnemonic;
    }

    public void setOfferedProductMnemonic(String offeredProductMnemonic) {
        this.offeredProductMnemonic = offeredProductMnemonic;
    }

    public String getAsmScore() {
        return asmScore;
    }

    public void setAsmScore(String asmScore) {
        this.asmScore = asmScore;
    }

    public String getCbsProductNumberTrimmed() {
        return cbsProductNumberTrimmed;
    }

    public void setCbsProductNumberTrimmed(String cbsProductNumberTrimmed) {
        this.cbsProductNumberTrimmed = cbsProductNumberTrimmed;
    }

    public String getCbsProductNumber() {
        return cbsProductNumber;
    }

    public void setCbsProductNumber(String cbsProductNumber) {
        this.cbsProductNumber = cbsProductNumber;
    }

    public BigInteger getCurrentYearOfStudy() {
        return this.currentYearOfStudy;
    }

    public void setCurrentYearOfStudy(BigInteger currentYearOfStudy) {
        this.currentYearOfStudy = currentYearOfStudy;
    }

    public Map<String, String> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(Map<String, String> productOptions) {
        this.productOptions = productOptions;
    }


    public Overdraft getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(Overdraft overdraft) {
        this.overdraft = overdraft;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }
}
