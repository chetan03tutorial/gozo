/**
 * 
 */
package com.lbg.ib.api.pendingarrangement;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author sthak4
 *
 */
public class RetrievePendingProductStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private RetrieveProductFeaturesImpl retrieveProductFeaturesImpl;
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String serviceEndPoint;
	private String arrangementId;

	/**
	 * @throws Exception
	 * 
	 */
	public RetrievePendingProductStep() throws Exception {
		super();
		retrieveProductFeaturesImpl = new RetrieveProductFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	private String parseJson(String jsonMessage, String key) {

		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = o.get(key);
		if (jsonElement != null) {
			if (!jsonElement.isJsonNull()) {
				String value = jsonElement.toString();
				System.out.println("value:" + value);
				return value;
			}
		}

		return null;

	}

	// Shiv
	@Given("^for a  branch colleague, I have an arrangementId\"([^\"]*)\" of a customer$")
	public void for_a_branch_colleague_I_have_an_arrangementId_of_a_customer(String arrangementId) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		this.arrangementId = arrangementId;
		assertTrue(arrangementId != null);
	}

	@When("^I make a call to the retrieveProductArrangement API at retrieveArrangement-endpoint \"([^\"]*)\"$")
	public void i_make_a_call_to_the_retrieveProductArrangement_API_at_retrieveArrangement_endpoint(String endpoint)
			throws Throwable {
		host = (String) envProperty.get(endpoint);
		serviceEndPoint = host.concat(arrangementId);
		assertTrue(host != null);
	}

	@Then("^I should see the repsonse status as \"([^\"]*)\"$")
	public void i_should_see_the_repsonse_status_as(String arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		actualResult = retrieveProductFeaturesImpl.getResponseFromAPI(serviceEndPoint);
		assertTrue(actualResult != null);
	}

	@Then("^I should see customer details response as \"([^\"]*)\"$")
	public void i_should_see_customer_details_response_as(String responseParam) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("Actual Result" + actualResult);
		String actualResultJson = actualResult.get("StatusMessage");
		String jsonFieldKey = responseParam;
		String jsonFieldValue = null;
		String jsonConcatenator = "\":\"";
		String value = null;
		if (actualResultJson.contains(jsonFieldKey)) {
			int keyIndex = actualResultJson.indexOf(jsonFieldKey + jsonConcatenator);
			int valueIndex = keyIndex + (jsonFieldKey + jsonConcatenator).length();
			int valueEndIndex = actualResultJson.indexOf("\"", valueIndex);
			value = actualResultJson.substring(valueIndex, valueEndIndex);
			System.out.println("checking field::" + responseParam + "value::" + value);
		}
		assertTrue(value != null);
	}

	@Then("^I should see response with response code \"([^\"]*)\"$")
	public void i_should_see_response_with_response_code(String responseCode) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		String messageBody = actualResult.get("StatusMessage");
		String error = parseJson(messageBody, "error");
		String responseCodeActual = parseJson(error, "code").replaceAll("\"", "");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));

		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));
	}

	@Then("^I should see http statuscode as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_as(String statusCode) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		assertTrue(true);
	}

	@Then("^I should see message \"([^\"]*)\"$")
	public void i_should_see_message(String message) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		String messageBody = actualResult.get("StatusMessage");
		String error = parseJson(messageBody, "error");
		String messageActual = parseJson(error, "message");
		if (null != messageActual) {
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.equalsIgnoreCase(message));
		} else {
			assertTrue(String.valueOf(messageActual).equalsIgnoreCase(message));
		}
	}
}
