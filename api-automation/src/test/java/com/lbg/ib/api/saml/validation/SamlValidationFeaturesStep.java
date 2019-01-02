 package com.lbg.ib.api.saml.validation;

import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class SamlValidationFeaturesStep extends BaseEnvSetUp {

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
	public SamlValidationFeaturesStep() throws Exception {	
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

	@When("^I should see http statuscode same as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_same_as(String httpStatus)
			throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		String httpStatusActual = getTokenResponse.get("StatusCode")
				.replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}
	
	@When("^I should see message \"([^\"]*)\"validated\"([^\"]*)\"$")
	public void i_should_see_message_validated(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtils.parseJson(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}
	
	@When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
	public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtils.parseJsonRecursive(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}	
	/*
	@When("^I try to post \"([^\"]*)\" with invalid Token$")
	public void i_try_to_post_with_invalid_Token(String arg1) throws Throwable {
		String tokenRequest = envProperty.getProperty(arg1);
		validateTokenResponse = samlValidationFeaturesImpl.getResponseFromAPI(tokenRequest, validateTokenHost);
	}
	
	@Then("^I should see error response with response code \"([^\"]*)\"$")
	public void i_should_see_error_response_with_response_code(String responseCode) throws Throwable {
		
		String messageBody = validateTokenResponse.get("StatusMessage");
		String responseCodeActual = JsonUtil.getCodeFromError(messageBody, "code").replaceAll("\"", "");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));
	}
	
	@When("^I should see error response having response code \"([^\"]*)\"$")
	public void i_should_see_error_response_having_response_code(String responseCode) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String responseCodeActual = JsonUtil.getCodeFromError(messageBody, "code").replaceAll("\"", "");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));
	}

	@Then("^I should see response code as \"([^\"]*)\"$")
	public void i_should_see_response_code_as(String httpStatus) throws Throwable {
		String httpStatusActual = validateTokenResponse.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}
	
	@When("^I Get the response from userInfo API$")
	public void i_Get_the_response_from_userInfo_API() throws Throwable {
		 String apiURL = envProperty.getProperty("test.userInfo");
         String sessionId = validateTokenResponse.get("sessionId");
         getUserInfoResponse = samlValidationFeaturesImpl.getUserInfoFromAPI(sessionId, apiURL);
	}
	
	@Then("^I should see request message \"([^\"]*)\"$")
	public void i_should_see_request_message(String attribute) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtil.getCodeFromError(messageBody, "message").replaceAll("\"", "");
		assertTrue(attribute.equalsIgnoreCase(attributeValue));
	}
	
	@When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
	public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtil.parseJson(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}
	
	
	
	@Then("^I should see response with following user details$")
	public void i_should_see_response_with_following_user_details(DataTable dataTable) throws Throwable {		
		List<String> attibuteNames = dataTable.asList(String.class);	
		String messageBody = getUserInfoResponse.get("StatusMessage");
        for(String item : attibuteNames){
        	if(!messageBody.contains(item)){
        		assertFalse(true);
        	}
        }
        assertTrue(true);
	}
	
	@When("^I should see error message \"([^\"]*)\"$")
	public void i_should_see_error_message(String arg1) throws Throwable {
		String messageBody = getUserInfoResponse.get("StatusMessage");
		assertTrue(messageBody.toUpperCase().contains(arg1.toUpperCase()));
	}
	*/
}