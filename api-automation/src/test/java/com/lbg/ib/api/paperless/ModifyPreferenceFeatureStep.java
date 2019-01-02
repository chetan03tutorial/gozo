/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.paperless;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.PaperlessFeatureImpl;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Feature Step for update preferences API.
 * @author tkhann
 */
public class ModifyPreferenceFeatureStep extends BaseEnvSetUp {

    public static final String ENV = "env";
    private PaperlessFeatureImpl paperlessFeatureImpl;
    public Map<String, String> getModifyPreference;
    private Properties envProperty;

    public String readRequest(String fileName) {
        return readRequestData("jsonRequestFiles/paperless", fileName);
    }

    public ModifyPreferenceFeatureStep() throws Exception {
        super();
        paperlessFeatureImpl = new PaperlessFeatureImpl();
        try {
            envProperty = this.getEnvProperty();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @When("^I hit the updatePreference API \"([^\"]*)\" with request \"([^\"]*)\"$")
    public void i_get_the_response_from_updateEmail(String apiURL, String request) {
        apiURL = envProperty.getProperty(apiURL);
        String incomingRequest = readRequest(request);
        getModifyPreference = paperlessFeatureImpl.postRequestAPI(incomingRequest, apiURL);
        assertTrue(getModifyPreference.size() > 0);
    }

    @Then("^I should see message for ModifyResponse with \"([^\"]*)\" having value \"([^\"]*)\"$")
    public void i_should_see_Succ_response_(String messageAttr, String msgValue) {
        String messageBody = getModifyPreference.get("StatusMessage");
        String attributeValue = JsonUtils.parseJsonRecursive(messageBody, messageAttr).replaceAll("\"", "");
        assertTrue(msgValue.equalsIgnoreCase(attributeValue));
    }

}
