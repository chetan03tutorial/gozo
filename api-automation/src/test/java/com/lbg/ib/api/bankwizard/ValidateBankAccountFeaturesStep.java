package com.lbg.ib.api.bankwizard;

import static com.jayway.restassured.RestAssured.given;
import static net.sf.json.test.JSONAssert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.Assert;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ValidateBankAccountFeaturesStep extends BaseEnvSetUp {

    private Properties envProperty;
    private String endPoint;
    private String requestBody;
    private String responseCode;
    private String responseBody;
    
    
	public ValidateBankAccountFeaturesStep() throws Exception {	
		super();
			
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	    
    @Given("^I have  service available for feature \"([^\"]*)\" and brand \"([^\"]*)\"$")
    public void givenIhaveServiceAvailableForFeatureAndBrand(String feature, String brand) {
        String featureEndPoint = (String)envProperty.get(brand);
        assertTrue(StringUtils.isNotEmpty(featureEndPoint));
        endPoint = featureEndPoint;
    }
    
    @When("^I send a POST \"([^\"]*)\"$")
    public void i_send_a_POST_and_I_get(String fileName) throws Throwable {
    	String filePath = "bankwizard/"+ fileName;
    	String jsonBody = readRequestData("jsonRequestFiles" , filePath);
    	requestBody= jsonBody;
        Response apiResponse = given().contentType("application/json").body(requestBody).post(endPoint).then().contentType(ContentType.JSON).extract().response();
        if (null == apiResponse) {
            throw new RuntimeException("Unable to get any response");
        }
        responseCode = String.valueOf(apiResponse.getStatusCode());
        responseBody =  apiResponse.getBody().asString();
        assertTrue(null != responseBody);
    }
    
	   
    @Then("^I should see http statuscode as \"([^\"]*)\"$")
    public void thenIshouldGetResponseFromTheService(String resCode) {
     assertTrue(resCode.equalsIgnoreCase(responseCode));
     }

    @When("^I should see the response value as \"([^\"]*)\"$")
    public void i_should_see_the_response_value_as(String responseText) throws Throwable {
    	JsonObject response = JsonUtils.parseJson(responseBody);
    	String valid = response.get("isValidIndicator").toString();
        assertEquals(valid, responseText);
    }

    @When("^I should see the isIntraBrandSwitching value as \"([^\"]*)\"$")
    public void i_should_see_the_isIntraBrandSwitching_value_as(String responseText) throws Throwable {
        JsonObject response = JsonUtils.parseJson(responseBody);
        String valid = response.get("isIntraBrandSwitching").toString();
        assertEquals(valid, responseText);
    }


    @When("^I should see the bankname value as \"([^\"]*)\"$")
    public void i_should_see_the_bankname_value_as(String responseText) throws Throwable {
        JsonObject response = JsonUtils.parseJson(responseBody);
        if (responseText.contains("null"))
        {
            String valid = response.get("bankName").toString();
            assertEquals(valid, responseText);
        }
        else
        {
            JsonElement valid = response.get("bankName");
            assertThat(valid.getAsString(), equalToIgnoringCase(responseText));
        }

    }

    @When("^I should see the bankInCASS value as \"([^\"]*)\"$")
    public void i_should_see_the_bankInCASS_as(String responseText) throws Throwable {
        JsonObject response = JsonUtils.parseJson(responseBody);
        JsonElement Cass = response.get("bankInCASS").getAsJsonObject().get("isBankInCASS");
         assertThat(Cass.getAsString(), equalToIgnoringCase(responseText));

    }

    
}
