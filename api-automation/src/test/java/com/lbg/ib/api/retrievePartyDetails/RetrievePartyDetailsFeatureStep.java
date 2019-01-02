package com.lbg.ib.api.retrievePartyDetails;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class RetrievePartyDetailsFeatureStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private RetrieveProductFeaturesImpl retrieveProductFeaturesImpl;
	public  Map<String, String>         actualResult;
	private Properties                  envProperty;
	private String                      serviceEndPoint;
	private String                      ocisId;

	public RetrievePartyDetailsFeatureStep() throws Exception {
		super();
		retrieveProductFeaturesImpl = new RetrieveProductFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Given("^for a  branch colleague, I have an ocisId \"([^\"]*)\" of a customer$")
	public void or_a_branch_colleague_I_have_an_ocisId_of_a_customer(String ocisId) throws Throwable {
		this.ocisId = ocisId;
		assertTrue(ocisId != null);
	}

	@When("^I make a call to the RetrievePartyDetails API at RetrievePartyDetails-endpoint \"([^\"]*)\"$")
	public void i_make_a_call_to_the_RetrievePartyDetails_API_at_RetrievePartyDetails_endpoint(String endpoint)
			throws Throwable {
		String endPoint = (String) envProperty.get(endpoint);
		serviceEndPoint = endPoint.replaceFirst("\\{ocisId\\}", this.ocisId);
		assertTrue(serviceEndPoint != null);

	}

	@Then("^I should see the response details with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void evaluateResult(String expectedFirstName, String expectedLastName) throws Throwable {
		actualResult = retrieveProductFeaturesImpl.getResponseFromAPI(serviceEndPoint);
		String responseBody = actualResult.get("StatusMessage");
		String firstNameKey = "firstName";
		String lastNameKey = "lastName";

		String jsonConcatenator = "\":";
		String firstNameValue = null;
		if (responseBody.contains(firstNameKey)) {
			int keyIndex = responseBody.indexOf(firstNameKey + jsonConcatenator);
			int valueIndex = keyIndex + (firstNameKey + jsonConcatenator).length();
			int valueEndIndex = responseBody.indexOf(",", valueIndex);
			firstNameValue = responseBody.substring(valueIndex, valueEndIndex).replace("\"", "");
		}

		String lastNameValue = null;
		if (responseBody.contains(lastNameKey)) {
			int keyIndex = responseBody.indexOf(lastNameKey + jsonConcatenator);
			int valueIndex = keyIndex + (lastNameKey + jsonConcatenator).length();
			int valueEndIndex = responseBody.indexOf(",", valueIndex);
			lastNameValue = responseBody.substring(valueIndex, valueEndIndex).replace("\"", "");
		}

		assertTrue(expectedFirstName.equalsIgnoreCase(firstNameValue));
		assertTrue(expectedLastName.equalsIgnoreCase(lastNameValue));
	}

	@Then("^I should see the response status as \"([^\"]*)\"$")
	public void i_should_see_the_response_status_as(String arg1) throws Throwable {
		String responseStatus = actualResult.get("StatusCode");
		assertTrue(responseStatus != null);
	}

}
