/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.product.offer;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.ExistingProductArrangementDTO;

public class ProductOfferedDTO {
    private String                        arrangementId;

    private String                        arrangementType;

    private String                        applicationType;

    private EIDVScoreDTO                  eidvScoreDTO;

    private ASMScoreDTO                   asmScoreDTO;

    private String                        customerIdentifier;

    private String                        cidPersID;

    private String                        customerNumber;

    private String                        individualIdentifier;

    private String                        productName;

    private String                        offerType;                  // 2001-Normal,
                                                                      // 2002-Upsell,
                                                                      // 2003-Downsell,
                                                                      // 2004-Typical

    private List<ConditionDTO>            conditions;

    private String                        applicationSubStatus;

    private String                        cbsProductNumberTrimmed;

    private String                        cbsProductNumber;

    private ExistingProductArrangementDTO existingProductArrangements;

    private boolean                       isOverdraftRequired;

    private BigDecimal                    overdraftAmount;

    private String                        productIdentifier;

    private String                        existingProductSortCode;

    private String                        productFamilyIdentifier;

    private ConditionDTO                  declineReason;

    private BigInteger                    currentYearOfStudy;

    private SIRAScoreDTO                  siraScoreDTO;

    private ThreatMatrixDTO               threatMatrixDTO;

    private String                        applicationStatus;

    private Map<String, String>           productOptions;

    private String                        mnemonic;

    private List<MarketingPreferenceDTO> marketingPreferences;

    public ProductOfferedDTO() {
        // Sonar avoidance comments
    }

    public ProductOfferedDTO(final String arrangementId, final String arrangementType, final String applicationType,
            final EIDVScoreDTO eidvScoreDTO, final ASMScoreDTO asmScoreDTO, final String customerIdentifier, final String cidPersID,
            final String customerNumber, final String individualIdentifier, final String productName, final String offerType,
            final Map<String, String> productOptions, final List<ConditionDTO> conditions, final String applicationStatus,
            final String applicationSubStatus, final String mnemonic, final String cbsProductNumberTrimmed, final String cbsProductNumber,
            final ExistingProductArrangementDTO existingProductArrangements, final boolean isOverdraftRequired,
            final BigDecimal overdraftAmount, final String productIdentifier, final String existingProductSortCode,
            final String productFamilyIdentifier, final ConditionDTO declineReason, final SIRAScoreDTO siraScoreDTO,
            final ThreatMatrixDTO threatMatrixDTO, final List<MarketingPreferenceDTO> marketingPreferences) {
        this.arrangementId = arrangementId;
        this.arrangementType = arrangementType;
        this.applicationType = applicationType;
        this.eidvScoreDTO = eidvScoreDTO;
        this.asmScoreDTO = asmScoreDTO;
        this.customerIdentifier = customerIdentifier;
        this.cidPersID = cidPersID;
        this.customerNumber = customerNumber;
        this.individualIdentifier = individualIdentifier;
        this.productName = productName;
        this.offerType = offerType;
        this.productOptions = productOptions;
        this.conditions = conditions;
        this.applicationStatus = applicationStatus;
        this.applicationSubStatus = applicationSubStatus;
        this.mnemonic = mnemonic;
        this.cbsProductNumberTrimmed = cbsProductNumberTrimmed;
        this.cbsProductNumber = cbsProductNumber;
        this.existingProductArrangements = existingProductArrangements;
        this.isOverdraftRequired = isOverdraftRequired;
        this.overdraftAmount = overdraftAmount;
        this.productIdentifier = productIdentifier;
        this.existingProductSortCode = existingProductSortCode;
        this.productFamilyIdentifier = productFamilyIdentifier;
        this.declineReason = declineReason;
        this.siraScoreDTO = siraScoreDTO;
        this.threatMatrixDTO = threatMatrixDTO;
    this.marketingPreferences = marketingPreferences;
    }

    public String getCbsProductNumber() {
        return cbsProductNumber;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public String getcbsProductNumberTrimmed() {
        return cbsProductNumberTrimmed;
    }

    public String getOfferType() {
        return offerType;
    }

    public Map<String, String> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(final Map<String, String> productOptions) {
        this.productOptions = productOptions;
    }

    public String arrangementId() {
        return arrangementId;
    }

    public EIDVScoreDTO eidvScoreDTO() {
        return eidvScoreDTO;
    }

    public ASMScoreDTO asmScoreDTO() {
        return asmScoreDTO;
    }

    public String customerIdentifier() {
        return customerIdentifier;
    }

    public String cidPersID() {
        return cidPersID;
    }

    public String customerNumber() {
        return customerNumber;
    }

    public String individualIdentifier() {
        return individualIdentifier;
    }

    public String productName() {
        return productName;
    }

    public List<ConditionDTO> conditions() {
        return conditions;
    }

    public String applicationSubStatus() {
        return applicationSubStatus;
    }

    public String applicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String mnemonic() {
        return mnemonic;
    }

    public void setMnemonic(final String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getArrangementType() {
        return arrangementType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public ExistingProductArrangementDTO existingProductArrangements() {
        return existingProductArrangements;
    }

    public boolean isOverdraftRequired() {
        return isOverdraftRequired;
    }

    public BigDecimal getOverdraftAmount() {
        return overdraftAmount;
    }

    public String getExistingProductSortCode() {
        return existingProductSortCode;
    }

    public String getProductFamilyIdentifier() {
        return productFamilyIdentifier;
    }

    public ConditionDTO getDeclineReason() {
        return declineReason;
    }

    public BigInteger getCurrentYearOfStudy() {
        return currentYearOfStudy;
    }

    public void setCurrentYearOfStudy(final BigInteger currentYearOfStudy) {
        this.currentYearOfStudy = currentYearOfStudy;
    }

    public SIRAScoreDTO getSiraScoreDTO() {
        return siraScoreDTO;
    }

    public void setSiraScoreDTO(final SIRAScoreDTO siraScoreDTO) {
        this.siraScoreDTO = siraScoreDTO;
    }

    public ThreatMatrixDTO getThreatMatrixDTO() {
        return threatMatrixDTO;
    }

    public void setOfferType(final String offerType) {
        this.offerType = offerType;
    }

    public List<MarketingPreferenceDTO> getMarketingPreferences() {
    return marketingPreferences;
    }

    public void setMarketingPreferences(final List<MarketingPreferenceDTO> marketingPreferences) {
    this.marketingPreferences = marketingPreferences;
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
