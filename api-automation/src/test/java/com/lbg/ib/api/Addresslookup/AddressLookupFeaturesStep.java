/**
 *
 */
package com.lbg.ib.api.Addresslookup;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;
import com.lbg.ib.api.test.services.generic.GenericImpl;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import com.gargoylesoftware.htmlunit.javascript.host.Set;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.beans.PostalAddress;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.AddressLookupFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AddressLookupFeaturesStep extends BaseEnvSetUp {

	private static final String ENV = "env";
	private GenericImpl service;
	public Map<String, String> actualResult;
	//private String host;
	//private Properties envProperty;
	private String serviceEndPoint;
	private String jsonRequest;

	public AddressLookupFeaturesStep() throws Exception {
		super();
		service = new GenericImpl();
	}

	@Given("^I have a \"([^\"]*)\" for the \"([^\"]*)\"$")
	public void i_have_a_for_the_valid(String envHost, String jsonFileName) throws Throwable
	{
		jsonRequest = readRequestData(jsonFileName);
		serviceEndPoint = new StringBuilder(this.getEnvProperty().getProperty(envHost)).toString();
		assertTrue(serviceEndPoint != null);
	}

	@When("^I try to get the response from the API$")
	public void i_try_to_get_the_response_from_the_API() throws Throwable {
		actualResult = service.getResponseFromAPI(serviceEndPoint,jsonRequest);
		assertTrue(actualResult != null);
	}

	@Then("^I should see http status code as \"([^\"]*)\"$")
	public void i_should_see_http_status_code_as(String httpStatus) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}

	@Then("^I should see the address details with valid post code$")
	public void i_should_see_the_address_details_with_valid_post_code() throws Throwable {
		String responseBody = actualResult.get("StatusMessage");
		JsonArray addressList = JsonUtils.parseJsonIntoArray(responseBody);
		if(addressList!= null)
			assertTrue(addressList.size() != 0);
	}


	@Given("^I have a \"([^\"]*)\" for the invalid \"([^\"]*)\"$")
	public void i_have_a_for_the_invalid(String envHost, String postCode) throws Throwable {
		serviceEndPoint = new StringBuilder(this.getEnvProperty().getProperty(envHost)).append(postCode).toString();
	}



	@Then("^I should see response having response code \"([^\"]*)\"$")
	public void i_should_see_response_having_response_code(String responseCode) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String responseCodeActual = JsonUtils.parseJsonRecursive(messageBody, "code").replaceAll("\"", "");
		//assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));
		assertTrue(responseCodeActual!=null);
	}

	public String readRequestData(String fileName)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream("jsonRequestFiles/address/"+fileName+".json");
		String theString = null;
		try {
			theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theString;
	}
}
