package com.lbg.ib.api.automation.impl;

import com.lbg.ib.api.automation.EnvironmentConfig;
import com.lbg.ib.api.automation.GenericStepDefs;
import com.lbg.ib.api.automation.http.APIRequestManager;
import com.lbg.ib.api.automation.http.RequestWrapper;
import com.lbg.ib.api.automation.http.ResponseWrapper;
import com.lbg.ib.api.services.util.JsonUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class GenericStepDefsImpl implements GenericStepDefs {

	@Autowired
	private EnvironmentConfig environmentConfig;

	@Autowired
	private RequestWrapper requestWrapper;

	@Autowired
	private ResponseWrapper responseWrapper;

	@Autowired
	private APIRequestManager httpRequestManager;

	@Override
	@Given("^I have  service available for feature \"([^\"]*)\" and brand \"([^\"]*)\"$")
	public void givenIhaveServiceAvailableForFeatureAndBrand(String feature, String brand) {
		
		String featureEndPoint = environmentConfig.getProperty(feature);
		System.out.println("feature end point:" + featureEndPoint);

		requestWrapper.setEndPoint(featureEndPoint);

	}

	@Override
	@When("^I try to get response from the API for the request$")
	public void whenITryToInvokeTheService() {

		httpRequestManager.callAPI();
	}

	@Override
	@Then("^I should see http statuscode as \"([^\"]*)\"$")
	public void thenIshouldGetResponseFromTheService(String responseCode) {
		assertTrue(responseCode.equalsIgnoreCase(responseWrapper.getResponseCode()));
	}

	@Then("^I should see response with response code \"([^\"]*)\"$")
	public void iShouldSeeResponseWithResponseCode(String responseCode) throws Throwable {
		String messageBody = responseWrapper.getResponseBody();
		String responseCodeActual = JsonUtils.parseJson(messageBody, "code").replaceAll("\"", "");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));

	}

	@Then("^I should see message \"([^\"]*)\"$")
	public void iShouldSeeMessage(String message) throws Throwable {
		String messageBody = responseWrapper.getResponseBody();
		String messageActual = JsonUtils.parseJson(messageBody, "message");
		if (null != messageActual) {
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.equalsIgnoreCase(message));
		} else {
			assertTrue(String.valueOf(messageActual).equalsIgnoreCase(message));
		}
	}

	@When("^I send a POST request with with data \"([^\"]*)\"$")
	public void i_send_a_POST_request_with_with_data(String datatype) throws Throwable {
		String jsonBody = environmentConfig.getProperty(datatype);
		requestWrapper.setRequestBody(jsonBody);
		httpRequestManager.callAPI();

	}

	public RequestWrapper getRequestWrapper() {
		return requestWrapper;
	}

	public ResponseWrapper getResponseWrapper() {
		return responseWrapper;
	}

}
