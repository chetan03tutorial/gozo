/**
 * 
 */
package com.lbg.ib.api.UserIdValidation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import com.google.gson.JsonObject;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.OfferService.OfferServiceStepFeaturesStep;
import com.lbg.ib.api.product.RetrieveProductFeaturesStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.UserIdValidationService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class UserIdValidationFeaturesStep extends BaseEnvSetUp {

    public static final String ENV = "env";
    private UserIdValidationService stepDefImpl;
    
    public Map<String, String> actualResult;
    private String host;
    private Properties envProperty;
    private RetrieveProductFeaturesStep retrieveService;
    private OfferServiceStepFeaturesStep offerService;
    
    public UserIdValidationFeaturesStep() throws Exception {    
        super();
            offerService = new OfferServiceStepFeaturesStep();
            retrieveService = new RetrieveProductFeaturesStep();
        stepDefImpl = new UserIdValidationService();
        try {
            envProperty = this.getEnvProperty();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
        
    
    @Given("^we have a userid validation service running on \"([^\"]*)\" for a brand$")
    public void i_have_a_service_running_on_for_a_brand(String envHost) throws Throwable {
        String value = (String)envProperty.get(envHost);
        System.out.println(value);
        host = value;
        System.out.println(host);
        
        
    }
    
    public String readRequestData(String fileName)
    {
        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        InputStream in = contextClassLoader.getResourceAsStream("jsonRequestFiles/"+fileName+".json");
        String theString = null;
        try {
             theString = IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return theString;
    }
    
    @Given("^product retrieve service is up and running for product \"([^\"]*)\" \"([^\"]*)\"$")
    public void retrieveProduct(String retrieveEndpoint, String urlIdentifier) throws Throwable {
        retrieveService.retrieveProduct(retrieveEndpoint, urlIdentifier);
    }

    @Then("^validate the response of retrieve service \"([^\"]*)\"$")
    public void validateProductResponse(String productMnemonic) throws Throwable {
        retrieveService.verifyProductMnemonic(productMnemonic);
    }

    @Then("^we send a request for offer service \"([^\"]*)\" \"([^\"]*)\"$")
    public void offerProduct(String offerEndpoint, String offerRequest) throws Throwable {
        offerService.offerProduct(offerEndpoint, offerRequest);
    }

    @Then("^validate the response of offer service \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateOfferResponse(String mnemonic, String eidvScore, String asmScore) throws Throwable {
        offerService.validateOfferedProductMnemonic(mnemonic);
        offerService.validateEidvScore(eidvScore);
        offerService.validateAsmScore(asmScore);
    }

    @When("^we try to get response from the API \"([^\"]*)\" for \"([^\"]*)\"$")
    public void i_try_to_get_response_from_the_API(String UserIdValidationRequest, String availability) throws Throwable {
        String jsonRequest = readRequestData(UserIdValidationRequest);
        String arrangementId = offerService.getArrangementId();
        String jsonRequestWithUsername = jsonRequest;
        if(availability!=null && availability.equals("true")){
            String newUserName = UUID.randomUUID().toString();
            jsonRequestWithUsername = jsonRequest.replaceAll("\\\"username\\\":(\\\"\\D+\\\")+", "\"username\":\"" + newUserName + "\"");
        }
        //replace the arrangementId with the one we got from offer
        String jsonRequestWithArrangementId = jsonRequestWithUsername.replaceAll("\\\"arrangementID\\\":(\\\"\\d+\\\"),+", "\"arrangementID\" : \"" + arrangementId + "\",");
        actualResult = stepDefImpl.getResponseFromAPI(host,jsonRequestWithArrangementId, getCookieMap());
        assertTrue(actualResult != null);
    }
    
    @When("^we should see http statuscode as \"([^\"]*)\"$")
    public void i_should_see_http_statuscode_as(String httpStatus) throws Throwable {
        System.out.println(httpStatus);
        String httpStatusActual = actualResult.get("StatusCode");
        System.out.println(httpStatusActual);
        assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
    }
    
    @When("^we should check the availability as \"([^\"]*)\"$")
    public void i_should_see_response_code(String available) throws Throwable {
        String jsonMessage = actualResult.get("StatusMessage").replaceAll("\"", "");
        JsonObject jsonObject = JsonUtils.parseJson(jsonMessage);
        String resultedAvailability = jsonObject.get("available").toString();
        assertTrue(resultedAvailability.equalsIgnoreCase(available));
    }
    
    @When("^we should see the error code \"([^\"]*)\"$")
    public void i_should_see_the_error_code(String errorCode) throws Throwable {
        String jsonMessage = actualResult.get("StatusMessage");
        String  actualErrorCode = JsonUtils.parseJsonRecursive(jsonMessage,"code");
        assertTrue(actualErrorCode.equalsIgnoreCase(errorCode));
    }
    
}


