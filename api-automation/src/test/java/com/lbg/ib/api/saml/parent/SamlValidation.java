package com.lbg.ib.api.saml.parent;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.apache.commons.lang3.StringUtils;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SamlValidation  extends BaseEnvSetUp{

	private SamlValidationFeaturesImpl samlValidationFeaturesImpl;
	private SamlTokenFeaturesImpl samlTokenFeaturesImpl;
	private String samlAssertionHost;
	private Properties envProperty;
	public Map<String, String> getTokenResponse;	
	private String samlToken;	
	
	@Given("^I have a service running on \"([^\"]*)\"$")
	public void i_have_a_service_running_on(String envHost) throws Throwable {
	samlAssertionHost = envProperty.getProperty(envHost);
		assertTrue(samlAssertionHost != null);
	}

	@When("^I try to get response from the getToken API for an existing user with userId \"([^\"]*)\"$")
	public void i_try_to_get_response_from_the_getToken_API_for_an_existing_user_with_userId(
			String userId) throws Throwable {
		
		String tokenHostEndPoint = samlAssertionHost+userId;
		getTokenResponse = samlTokenFeaturesImpl
				.getResponseFromAPI(tokenHostEndPoint);
		assertTrue(getTokenResponse != null);
	}
	
	@When("^I should see the response details with SAML token$")
	public void i_should_see_the_response_details_with_SAML_token()
			throws Throwable {
		
		String messageBody = getTokenResponse.get("StatusMessage");
		samlToken = JsonUtils.parseJsonRecursive(messageBody, "tokenID")
				.replaceAll("\"", "");
		assertTrue(null != samlToken && StringUtils.isNotBlank(samlToken));
	}
}
