package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Class that represent the Marketing Preference object to store the consent for
 * marketing preferences
 */
public class MarketingPreference {

	/**
	 * Indentify the type of marketing preference
	 */
	private String entitlementId;

	/**
	 * Will be true if the customer consents the expecific marketing preference
	 */
	private Boolean consentOption;

	public String getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(String entitlementId) {
		this.entitlementId = entitlementId;
	}

	public Boolean isConsentOption() {
		return consentOption;
	}

	public void setConsentOption(Boolean consentOption) {
		this.consentOption = consentOption;
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
