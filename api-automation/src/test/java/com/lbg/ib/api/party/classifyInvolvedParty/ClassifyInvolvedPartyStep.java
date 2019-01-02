package com.lbg.ib.api.party.classifyInvolvedParty;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.OfferService.OfferServiceStepFeaturesStep;
import com.lbg.ib.api.test.services.ClassifyInvolvedPartyImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ClassifyInvolvedPartyStep extends BaseEnvSetUp {

	public static final String ENV = "env";

	private OfferServiceStepFeaturesStep offerService;
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String countryOfBirth;
	private String nationality;
	private String taxCountry;
	private String statusCode;
	Map<String,String> responseMap;
	/**
	 * @throws Exception
	 * 
	 */
	public ClassifyInvolvedPartyStep() throws Exception {
		super();
		offerService = new OfferServiceStepFeaturesStep();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Given("^I have a \"([^\"]*)\" for Nationality \"([^\"]*)\"$")
	public void i_have_a_for_Nationality(String endPoint, String nationality) throws Throwable {

		this.host = envProperty.getProperty(endPoint.trim());
		assertTrue(host != null);
		this.nationality = nationality;
		assertTrue(host != null && nationality != null);
	}

	@Given("^country of Birth \"([^\"]*)\" Tax Country \"([^\"]*)\"$")
	public void country_of_Birth_Tax_Country(String cob, String taxCountry) throws Throwable {
		this.countryOfBirth = cob;
		this.taxCountry = taxCountry;
		assertTrue(cob != null && taxCountry != null);
	}

	@When("^I send a request$")
	public void i_send_a_request() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		RequestSpecBuilder builder = new RequestSpecBuilder();
		String jsonRequest = createJSONRequest();
		builder.setBody(jsonRequest);
		builder.setContentType(ContentType.JSON);
		System.out.println("JSON Request:" + jsonRequest);
		responseMap = offerService.offerProductAndReturnReqWithFullText(host, jsonRequest);
		System.out.println(responseMap);
		statusCode = responseMap.get("StatusCode");
		
		
	}

	@Then("^I should see http status code as \"([^\"]*)\"$")
	public void i_should_see_http_status_code_as(String responseCode) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
	    assertTrue(statusCode.equalsIgnoreCase(responseCode));
	}

	@Then("^I should see the response for asking TIN number$")
	public void i_should_see_the_response_for_asking_TIN_number() throws Throwable {
	    String responseMsg = responseMap.get("StatusMessage");
	    assertTrue(responseMsg.contains("tinRequired"));
	    assertTrue(responseMsg.contains("taxResidencyType"));
	}

	

	private String createJSONRequest() {

		String jsonString = "{" + "\"birthCountry\":\"" + countryOfBirth + "\"" + "," + "\"nationalities\"" + ":[\""
				+ nationality + "\"" + "]," + "\"taxResidencies\"" + ":[" + "{\"taxResidency\"" + ":\"" + taxCountry
				+ "\"}" + "]" + "}";
		return jsonString;

	}
}
