package com.lbg.ib.api.sales.product.domain.arrangement;

import java.util.Arrays;
import java.util.List;

public class PrimaryInvolvedPartyBuilder {
	
	private Boolean marketPrefEmail;
	private Boolean marketPrefPhone;
	private Boolean marketPrefPost;
	private Boolean marketPrefText;
	
	private List<MarketingPreference> marketingPreferences;
	
		
	public PrimaryInvolvedPartyBuilder withMarketingPreferences(MarketingPreference ... marketingPreferences) {
		this.marketingPreferences = Arrays.asList(marketingPreferences);
		return this;
	}
	
	public PrimaryInvolvedPartyBuilder withEmptyMarketingPreferences() {
		marketingPreferences = Arrays.asList();
		return this;
	}
	
	public PrimaryInvolvedPartyBuilder withMarketPrefEmail(Boolean marketPrefEmail) {
		this.marketPrefEmail = marketPrefEmail;
		return this;
	}

	public PrimaryInvolvedPartyBuilder withMarketPrefPhone(Boolean marketPrefPhone) {
		this.marketPrefPhone = marketPrefPhone;
		return this;
	}

	public PrimaryInvolvedPartyBuilder withMarketPrefPost(Boolean marketPrefPost) {
		this.marketPrefPost = marketPrefPost;
		return this;
	}
	
	public PrimaryInvolvedPartyBuilder withMarketPrefText(Boolean marketPrefText) {
		this.marketPrefText = marketPrefText;
		return this;
	}
	
	public PrimaryInvolvedParty build() {
		PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
		primaryInvolvedParty.setMarketPrefEmail(marketPrefEmail);
		primaryInvolvedParty.setMarketPrefPhone(marketPrefPhone);
		primaryInvolvedParty.setMarketPrefPost(marketPrefPost);
		primaryInvolvedParty.setMarketPrefText(marketPrefText);
		primaryInvolvedParty.setMarketingPreferences(marketingPreferences);
		
		return primaryInvolvedParty;
	}
}