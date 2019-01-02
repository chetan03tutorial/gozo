package com.lbg.ib.api.paperless;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UserMandateInfoFeatureStep extends BaseEnvSetUp {

    public static final String ENV = "env";
    private SamlValidationFeaturesImpl samlValidationFeaturesImpl;
    public Map<String, String> getTokenResponse;
    public Map<String, String> validateTokenResponse;
    public Map<String, String> getUserInfoResponse;
    private Properties envProperty;

    public UserMandateInfoFeatureStep() throws Exception {
        super();
        samlValidationFeaturesImpl = new SamlValidationFeaturesImpl();
        try {
            envProperty = this.getEnvProperty();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Given("^I Post the SAML token to Token Validate API as \"([^\"]*)\" having value \"([^\"]*)\"$")
    public void i_have_a_service_running_on(String apiURL, String tokenValue) throws Throwable {
        apiURL = envProperty.getProperty(apiURL);
        StringBuffer jsonRequest =  new StringBuffer();
        jsonRequest.append("{");
        jsonRequest.append("\"").append("tokenID").append("\"").append(":").append("\"").append(tokenValue).append("\"");
        jsonRequest.append("}");
        validateTokenResponse = samlValidationFeaturesImpl.getResponseFromAPI(jsonRequest.toString(), apiURL);
        assertTrue(null != validateTokenResponse);
    }

    @Then("^I should see http statuscode same as \"([^\"]*)\"$")
    public void i_should_see_http_statuscode_same_as(String httpStatus)
            throws Throwable {
        String httpStatusActual = validateTokenResponse.get("StatusCode")
                .replaceAll("\"", "");
        assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
    }

    @When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
    public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
        String messageBody = validateTokenResponse.get("StatusMessage");
        String attributeValue = JsonUtils.parseJsonRecursive(messageBody, attribute).replaceAll("\"", "");
        assertTrue("true".equalsIgnoreCase(attributeValue));
    }

    @When("^I hit the userMandateInfo API \"([^\"]*)\"$")
    public void i_get_the_response_from_userinfo_api(String apiURL) {
        apiURL = envProperty.getProperty(apiURL);
        String sessionId = validateTokenResponse.get("sessionId");
        getUserInfoResponse = samlValidationFeaturesImpl.getUserInfoFromAPI(sessionId, apiURL);
        assertTrue(getUserInfoResponse.size() > 0);
    }

    @Then("^I should see response with following user Mandate details$")
    public void i_should_see_response_with_following_user_details(DataTable dataTable) {
        String messageBody = getUserInfoResponse.get("StatusMessage");
        List<String> attibuteNames = dataTable.asList(String.class);
        for (int i = 0; i < attibuteNames.size(); i++) {
            System.out.println(attibuteNames.get(i));
            assertTrue(JsonUtils.checkJsonRecursiveExists(messageBody, attibuteNames.get(i)));
        }
    }

}
