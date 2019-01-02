package com.lbg.ib.api.saml.assertion.performance;

import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class PerformanceValidationFeaturesStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private SamlValidationFeaturesImpl samlValidationFeaturesImpl;
	private SamlTokenFeaturesImpl samlTokenFeaturesImpl;
	public Map<String, String> getTokenResponse;
	public Map<String, String> validateTokenResponse;
	public Map<String, String> getUserInfoResponse;
	private String samlAssertionHost;
	private String samlToken;
	private Properties envProperty;

	/**
	 * @throws Exception
	 * 
	 */
	public PerformanceValidationFeaturesStep() throws Exception {	
		super();
		samlValidationFeaturesImpl = new SamlValidationFeaturesImpl();
		samlTokenFeaturesImpl= new SamlTokenFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	@Given("^I have a service running on \"([^\"]*)\"$")
	public void i_have_a_service_running_on(String envHost) throws Throwable {
		samlAssertionHost = envProperty.getProperty(envHost);
		assertTrue(samlAssertionHost != null);
	    
	}

	@When("^I try to get response from the getToken for user \"([^\"]*)\" API$")
	public void i_try_to_get_response_from_the_getToken_API(String user) throws Throwable {
		getTokenResponse = samlTokenFeaturesImpl.getResponseFromAPI(samlAssertionHost+user);
		assertTrue(getTokenResponse != null);
	}

	
	@When("^I should see the response details with SAML token$")
	public void i_should_see_the_response_details_with_SAML_token() throws Throwable {
		String messageBody = getTokenResponse.get("StatusMessage");
		samlToken = JsonUtils.parseJsonRecursive(messageBody, "tokenID").replaceAll("\"", "");
		assertTrue(null != samlToken);
	}
	
	
	@When("^I Post the SAML token to Token Validate API as \"([^\"]*)\"$")
	public void i_Post_the_SAML_token_to_Token_Validate_API(String validateTokenHost) throws Throwable {
		validateTokenHost = envProperty.getProperty(validateTokenHost);
		StringBuffer jsonRequest =  new StringBuffer();
		jsonRequest.append("{");
		jsonRequest.append("\"").append("tokenID").append("\"").append(":").append("\"").append(samlToken).append("\"");
		jsonRequest.append("}");
		validateTokenResponse=samlValidationFeaturesImpl.getResponseFromAPI(jsonRequest.toString(), validateTokenHost);
		assertTrue(null != validateTokenResponse);
	}

	
	@When("^I should see message \"([^\"]*)\"validated\"([^\"]*)\"$")
	public void i_should_see_message_validated(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtils.parseJson(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}

}