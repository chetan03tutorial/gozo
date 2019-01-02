/**
 *
 */
package com.lbg.ib.api.C078;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.test.services.generic.GenericImpl;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class C078FeaturesStep extends BaseEnvSetUp {

	private static final String ENV = "env";
	private GenericImpl genericImpl;
	Map<String, String> workingDaysMap;
	private String serviceEndPoint;
	String jsonRequest = null;
	Map<String, String> actualResult;

	public C078FeaturesStep() throws Exception {
		super();
		genericImpl = new GenericImpl();
	}

	@Given("^I have a \"([^\"]*)\" for valid \"([^\"]*)\" and  \"([^\"]*)\"$")
	public void i_have_a_for_valid_and(String envHost, String applicationType, String applicationId) throws Throwable {
		jsonRequest = readRequestDataFromDir("C078",applicationType+applicationId);
		serviceEndPoint = new StringBuilder(this.getEnvProperty().getProperty(envHost)).toString();
		assertTrue(serviceEndPoint != null);
	}

	@When("^I try to get the response from the API$")
	public void i_try_to_get_the_response_from_the_API() throws Throwable {
		actualResult = genericImpl.getResponseFromAPI(serviceEndPoint,jsonRequest);
		assertTrue(actualResult != null);
	}

	@Then("^I should see http status code as \"([^\"]*)\"$")
	public void i_should_see_http_status_code_as(String httpStatus) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}

	@Then("^I should see http status code as \"([^\"]*)\" and should get error message \"([^\"]*)\"$")
	public void i_should_see_http_status_code_as_and_should_get_error_message(String httpStatus, String errorMessage) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));

		String body = actualResult.get("StatusMessage");
		assertTrue(body.contains(errorMessage));
	}

}
