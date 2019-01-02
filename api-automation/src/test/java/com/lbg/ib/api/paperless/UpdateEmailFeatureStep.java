/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.paperless;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.PaperlessFeatureImpl;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Feature Step for update email address API.
 * @author tkhann
 */
public class UpdateEmailFeatureStep extends BaseEnvSetUp {

    public static final String ENV = "env";
    private PaperlessFeatureImpl paperlessFeatureImpl;
    public Map<String, String> getUpdateEmailResponse;
    private Properties envProperty;

    public UpdateEmailFeatureStep() throws Exception {
        super();
        paperlessFeatureImpl = new PaperlessFeatureImpl();
        try {
            envProperty = this.getEnvProperty();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @When("^I hit the updateEmail API \"([^\"]*)\" having values \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_get_the_response_from_updateEmail(String apiURL, String emailAddress, String ocisId, String partyId) {
        apiURL = envProperty.getProperty(apiURL);
        StringBuffer jsonRequest = new StringBuffer();
        jsonRequest.append("{");
        jsonRequest.append("\"").append("emailAddress").append("\"").append(":").append("\"").append(emailAddress).append("\"");
        jsonRequest.append("}");
        getUpdateEmailResponse = paperlessFeatureImpl.postRequestAPI(jsonRequest.toString(), apiURL);
        assertTrue(getUpdateEmailResponse.size() > 0);
    }

    @Then("^I should see message with \"([^\"]*)\" having value \"([^\"]*)\"$")
    public void i_should_see_Succ_response_(String messageAttr, String msgValue) {
        String messageBody = getUpdateEmailResponse.get("StatusMessage");
        String attributeValue = JsonUtils.parseJsonRecursive(messageBody, messageAttr).replaceAll("\"", "");
        assertTrue(msgValue.equalsIgnoreCase(attributeValue));
    }

}
