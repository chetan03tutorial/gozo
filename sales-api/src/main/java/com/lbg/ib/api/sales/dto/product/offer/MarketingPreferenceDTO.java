package com.lbg.ib.api.sales.dto.product.offer;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreference;

/**
 * Data transfer object MarketingPreferenceDTO for {@link MarketingPreference}
 */
public class MarketingPreferenceDTO {

    private final String entitlementId;

    private final Boolean consentOption;

    public MarketingPreferenceDTO(final String entitlementId, final Boolean consentOption) {
        this.entitlementId = Objects.requireNonNull(StringUtils.trimToNull(entitlementId), "Invalid blank entitlementId");
        this.consentOption = Objects.requireNonNull(consentOption, "Invalid null consentOption");
    }

    public String getEntitlementId() {
        return entitlementId;
    }

    public Boolean getConsentOption() {
        return consentOption;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entitlementId, consentOption);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MarketingPreferenceDTO other = (MarketingPreferenceDTO) obj;
        return Objects.equals(this.entitlementId, other.entitlementId)
                && Objects.equals(this.consentOption, other.consentOption);
    }

    @Override
    public String toString() {
        return String.format("MarketingPreferenceDTO [entitlementId=%s, consentOption=%s]", entitlementId, consentOption);
    }
}
