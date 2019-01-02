package com.lbg.ib.api.sales.product.domain.arrangement;

public class MarketingPreferenceBuilder {
	
	private String entitlementId;
	private Boolean consentOption;
	
	public MarketingPreferenceBuilder withEntitlementId(String entitlementId) {
		this.entitlementId = entitlementId;
		return this;
	}

	public MarketingPreferenceBuilder withConsentOption(Boolean consentOption) {
		this.consentOption = consentOption;
		return this;
	}
	
	public MarketingPreference build() {
		MarketingPreference marketingPreference = new MarketingPreference();
		marketingPreference.setEntitlementId(entitlementId);
		marketingPreference.setConsentOption(consentOption);
		
		return marketingPreference;		
	}

}
