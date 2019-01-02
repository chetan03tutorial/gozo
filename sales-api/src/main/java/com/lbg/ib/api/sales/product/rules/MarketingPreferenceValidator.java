package com.lbg.ib.api.sales.product.rules;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreference;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;

/**
 * Validate that the Marketing preferences values for GDPR
 */
@Component
public class MarketingPreferenceValidator {

	static final String INVALID_MARKETING_PREFERENCE_ERROR_MSG = "Invalid marketing preference";
	static final String SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG = "Both marketing preferences can not be specified";

	/**
	 * Validate that the system does not accept the old values and the new values
	 * for MarketingPreference at the same time Validate that the system receive the
	 * old values OR the new values for MarketingPreference Validate that values for
	 * Marketing preferences properties are correct
	 */
	public ValidationError validateRules(Arrangement arrangement) throws ServiceException {
		PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();

		if (primaryInvolvedParty.getMarketingPreferences() != null && (primaryInvolvedParty.getMarketPrefEmail() != null
				|| primaryInvolvedParty.getMarketPrefPhone() != null || primaryInvolvedParty.getMarketPrefPost() != null
				|| primaryInvolvedParty.getMarketPrefText() != null)) {
			return new ValidationError(SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG);
		}

		List<MarketingPreference> marketingPreferenceList = primaryInvolvedParty.getMarketingPreferences();
		if (marketingPreferenceList != null) {
			for (MarketingPreference mp : marketingPreferenceList) {
				if (mp == null || StringUtils.isBlank(mp.getEntitlementId()) || mp.isConsentOption() == null) {
					return new ValidationError(INVALID_MARKETING_PREFERENCE_ERROR_MSG);
				}
			}
		}
		return null;
	}
}