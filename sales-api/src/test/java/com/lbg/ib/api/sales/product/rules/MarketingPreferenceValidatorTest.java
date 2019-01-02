package com.lbg.ib.api.sales.product.rules;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreference;
import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreferenceBuilder;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedPartyBuilder;


/**
 * Test class to validate the new list of MarketingPreference values for GDPR
 */
@RunWith(Parameterized.class)
public class MarketingPreferenceValidatorTest {
	
	private MarketingPreference marketingPreference;

	private MarketingPreferenceValidator marketingPreferenceValidator = new MarketingPreferenceValidator();

    @Parameter(value = 0)
	public Arrangement arrangement;
    
    @Parameter(value = 1)
	public ValidationError validationError;

    @Before
    public void setUp(){
    	marketingPreference = new MarketingPreference();
    }
	
	@Parameters (name = "{index}: validateMarketingPreferences ({1} {0})")
	public static Collection<Object[]> dataPoints() {
	
		return Arrays.asList  ( new Object [][] {
				// Test that marketing preferences have value and one of the old marketing preferences have value also (marketPrefPhone)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
					.withMarketPrefPhone(Boolean.TRUE)
					.withMarketingPreferences(new MarketingPreferenceBuilder()
							.withEntitlementId("1111")
							.withConsentOption(Boolean.TRUE)
							.build())
					.build(), null, null), 
					new ValidationError(MarketingPreferenceValidator.SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that marketing preferences have value and one of the old marketing preferences have value also (marketPrefEmail)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
					.withMarketPrefEmail(Boolean.TRUE)
					.withMarketingPreferences(new MarketingPreferenceBuilder()
							.withEntitlementId("1111")
							.withConsentOption(Boolean.TRUE)
							.build())
					.build(), null, null), 
					new ValidationError(MarketingPreferenceValidator.SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that marketing preferences have value and one of the old marketing preferences have value also (marketPrefPost)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefPost(Boolean.TRUE)
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("1111")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null),
						new ValidationError(MarketingPreferenceValidator.SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that marketing preferences have value and one of the old marketing preferences have value also (marketPrefText)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefText(Boolean.TRUE)
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("1111")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that marketing preferences is null and one of the old marketing preferences have value (marketPrefPhone)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefPhone(Boolean.TRUE)					
						.build(), null, null), 
						null}, //no validation error is thrown	
						
				// Test that marketing preferences is null and one of the old marketing preferences have value (marketPrefEmail)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefEmail(Boolean.TRUE)					
						.build(), null, null), 
						null}, //no validation error is thrown	
				
				// Test that marketing preferences is null and one of the old marketing preferences have value (marketPrefPost)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefPhone(Boolean.TRUE)					
						.build(), null, null), 
						null}, //no validation error is thrown	
				
				// Test that marketing preferences is null and one of the old marketing preferences have value (marketPrefText)
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefText(Boolean.TRUE)					
						.build(), null, null), 
						null}, //no validation error is thrown	
			
				// Test that marketing preferences have value and all of the old marketing preferences have value also
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketPrefText(Boolean.TRUE)
						.withMarketPrefEmail(Boolean.FALSE)
						.withMarketPrefPost(Boolean.FALSE)
						.withMarketPrefPhone(Boolean.TRUE)
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("1111")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.SPECIFIED_BOTH_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that new marketing preferences is null and all of the old marketing preferences are null also
				{new Arrangement(new PrimaryInvolvedPartyBuilder()				
						.build(), null, null), 
						null}, //no validation error is thrown	
				
				// Test that marketing preferences is an empty list and old marketing preferences are empty 
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withEmptyMarketingPreferences()
						.build(), null, null), 
						null}, //no validation error is thrown	
				
				// Test that MarketingPreference entitlementId is empty
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.INVALID_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that MarketingPreference entitlementId can not be a space value
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId(" ")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.INVALID_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that MarketingPreference consentOption can not have a null value
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("1001")
								.withConsentOption(null)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.INVALID_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that MarketingPreference list can not have null values
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId(null)
								.withConsentOption(null)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.INVALID_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that marketing preferences entitlementId is blank and consentOption has no value
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("")
								.withConsentOption(null)
								.build())
						.build(), null, null), 
						new ValidationError(MarketingPreferenceValidator.INVALID_MARKETING_PREFERENCE_ERROR_MSG)}, //expected result
				
				// Test that MarketingPreference properties are correct, entlimentId is not empty and consentOption is not null
				{new Arrangement(new PrimaryInvolvedPartyBuilder()
						.withMarketingPreferences(new MarketingPreferenceBuilder()
								.withEntitlementId("1001")
								.withConsentOption(Boolean.TRUE)
								.build())
						.build(), null, null), 
						null} //no validation error is thrown							
		});
	}
	
	@Test
	public void testValidationMarketingPreferences() {
		// Given

		// When
		ValidationError error = marketingPreferenceValidator.validateRules(arrangement);

		// Then
		assertEquals(validationError, error);
	}		

}