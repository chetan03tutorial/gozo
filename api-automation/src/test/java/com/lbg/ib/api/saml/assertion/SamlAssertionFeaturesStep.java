package com.lbg.ib.api.saml.assertion;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SamlAssertionFeaturesStep extends BaseEnvSetUp {

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
	public SamlAssertionFeaturesStep() throws Exception {
		super();
		samlTokenFeaturesImpl = new SamlTokenFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}






	@When("^I should see http statuscode as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_as(String httpStatus)
			throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		String httpStatusActual = getTokenResponse.get("StatusCode")
				.replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}


	@When("^I should see message \"([^\"]*)\"$")
	public void i_should_see_message(String arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		String messageBody = getTokenResponse.get("StatusMessage");
		assertTrue(messageBody.contains(arg1));
	}



	@When("^I should see the response details with error \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_error(String responseCode) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions		
	    //throw new PendingException();
		String messageBody = getTokenResponse.get("StatusMessage");
		String responseCodeActual = JsonUtils.parseJsonRecursive(messageBody, "code");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));		
	}	
	
	


}