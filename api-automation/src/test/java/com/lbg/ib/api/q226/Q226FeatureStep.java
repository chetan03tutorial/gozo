package com.lbg.ib.api.q226;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.automation.http.RequestWrapper;
import com.lbg.ib.api.automation.http.ResponseWrapper;
import com.lbg.ib.api.services.util.JsonUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class Q226FeatureStep extends BaseEnvSetUp {

	@Autowired
	private RequestWrapper requestWrapper;

	@Autowired
	private ResponseWrapper responseWrapper;
	
	
	@Given("^requesting the customer details for the agreement identifier \"([^\"]*)\" and type \"([^\"]*)\"$")
	public void requestPartyDetail(String agreementIdentifier, String type) throws Throwable {
		String endPoint = requestWrapper.getEndPoint();
		endPoint = new StringBuilder(endPoint).append(agreementIdentifier).append("?type=").append(type).toString();
		requestWrapper.setEndPoint(endPoint);
	}

	@Then("^I should see the response details with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void evaluateResult(String expectedFirstName, String expectedLastName) throws Throwable {
		String responseBody = responseWrapper.getResponseBody();
		String firstName = JsonUtils.parseJsonArray(responseBody, "firstName").replaceAll("\"", "");
		String lastName = JsonUtils.parseJsonArray(responseBody, "lastName").replaceAll("\"", "");
		assertTrue(expectedFirstName.equalsIgnoreCase(firstName));
		assertTrue(expectedLastName.equalsIgnoreCase(lastName));
	}
	
	@Then("^result count should be \"([^\"]*)\"$")
	public void evaluateResult(int expectedCount) throws Throwable {
		String responseBody = responseWrapper.getResponseBody();
		int actualCount = JsonUtils.objectCount(responseBody);
		assertEquals(actualCount,expectedCount);
	}

}
