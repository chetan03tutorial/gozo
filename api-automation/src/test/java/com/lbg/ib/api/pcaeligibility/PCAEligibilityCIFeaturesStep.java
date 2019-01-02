/**
 * 
 */
package com.lbg.ib.api.pcaeligibility;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.beans.DetermineEligibility;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.DeciFeaturesImpl;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PCAEligibilityCIFeaturesStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private SamlValidationFeaturesImpl samlValidationFeaturesImpl;
	private SamlTokenFeaturesImpl samlTokenFeaturesImpl;
	public Map<String, String> getTokenResponse;
	public Map<String, String> validateTokenResponse;
	public Map<String, String> getUserInfoResponse;
	private String samlAssertionHost;
	private String samlToken;
	private Properties envProperty;
	private DeciFeaturesImpl deciService;
	private Map<String, String> deciResultMap = null;




	public PCAEligibilityCIFeaturesStep() throws Exception {	
		super();
		samlValidationFeaturesImpl = new SamlValidationFeaturesImpl();
		samlTokenFeaturesImpl= new SamlTokenFeaturesImpl();
		deciService = new DeciFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	@Given("^I have a service running on \"([^\"]*)\"$")
	public void i_have_a_service_running_on(String envHost) throws Throwable {
		samlAssertionHost = envProperty.getProperty(envHost);
		assertTrue(samlAssertionHost != null);
	}
	
	@When("^I try to get response from the getToken for user \"([^\"]*)\" API$")
	public void i_try_to_get_response_from_the_getToken_API(String user) throws Throwable {
		getTokenResponse = samlTokenFeaturesImpl.getResponseFromAPI(samlAssertionHost+user);
		assertTrue(getTokenResponse != null);
	}
	
	@When("^I should see the response details with SAML token$")
	public void i_should_see_the_response_details_with_SAML_token() throws Throwable {
		String messageBody = getTokenResponse.get("StatusMessage");
		samlToken = JsonUtils.parseJsonRecursive(messageBody, "tokenID").replaceAll("\"", "");
		assertTrue(null != samlToken);
	}
	
	@When("^I Post the SAML token to Token Validate API as \"([^\"]*)\"$")
	public void i_Post_the_SAML_token_to_Token_Validate_API(String validateTokenHost) throws Throwable {
		validateTokenHost = envProperty.getProperty(validateTokenHost);
		StringBuffer jsonRequest =  new StringBuffer();
		jsonRequest.append("{");
		jsonRequest.append("\"").append("tokenID").append("\"").append(":").append("\"").append(samlToken).append("\"");
		jsonRequest.append("}");
		validateTokenResponse=samlValidationFeaturesImpl.getResponseFromAPI(jsonRequest.toString(), validateTokenHost);
		assertTrue(null != validateTokenResponse);
	}	
	
	@When("^I should see http statuscode same as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_same_as(String httpStatus)
			throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		String httpStatusActual = getTokenResponse.get("StatusCode")
				.replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}
	
	@When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
	public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtils.parseJsonRecursive(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}	
	
	@When("^I Get the response from userInfo from API \"([^\"]*)\"$")
	public void i_get_the_response_from_userinfo_api(String apiURL){
		apiURL = envProperty.getProperty(apiURL);
        String sessionId = validateTokenResponse.get("sessionId");
        setSessionId(sessionId);
        getUserInfoResponse = samlValidationFeaturesImpl.getUserInfoFromAPI(getSessionId(), apiURL);
        assertTrue(getUserInfoResponse.size()>0);
	}
	
	@When("^I Get the response from ocisId from API$")
	public void i_get_the_response_from_ocisId_from_API() throws Throwable {
		String messageBody = getUserInfoResponse.get("StatusMessage");
		assertTrue(JsonUtils.checkJsonRecursiveExists(messageBody, "ocisId"));
	}
	
	
	@Then("^we send a request for offer service \"([^\"]*)\" \"([^\"]*)\"$")
	public void deciRequest(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		String inputRequest = readRequest(requestData);
		deciResultMap = deciService.getResponseFromAPI(url, inputRequest ,getSessionId());
		setSessionId(null);
		assertTrue(deciResultMap != null);
	}
	
	@When("^I should see \"([^\"]*)\" as \"([^\"]*)\"$")
	public void i_should_see_msg_as(String key ,String msgValue){
		String messageBody = deciResultMap.get("StatusMessage");
		String value = JsonUtils.parseJsonRecursive(messageBody,key);
		assertTrue(msgValue.equals(value));
	}
	
	@When("^I should see the response details with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_should_see_the_response_with_prodMnemonic_and_eligibilityValue(String prodMnemonic, String eligibilityValue) throws Exception{
		
		String messageBody = deciResultMap.get("StatusMessage");
        List<DetermineEligibility> eligible = JsonUtils.DECIProductSelectorParseJson(messageBody);
        for (DetermineEligibility eligiblity : eligible) {
        	if (eligiblity.getmnenomic().equals(prodMnemonic)){
            	   assertTrue(eligiblity.getmnenomic().equals(prodMnemonic));
            	   assertTrue(eligiblity.getisEligible().toString().equals(eligibilityValue));
            	   return;
            }
        }
        
        assertTrue(false);

	}
	
	
	@When("^I should see the response pca details with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_should_see_the_response_pca_with_prodMnemonic_and_eligibilityValue(String prodMnemonic, String eligibilityValue) throws Exception{
		
		String messageBody = deciResultMap.get("StatusMessage");
		List<Map<String,Object>>  eligibleProducts = JsonUtils.genericProductSelectorParseJsonResponse(messageBody,"products");
		for(Map<String,Object> map : eligibleProducts){
			if(map.get("mnemonic").equals(prodMnemonic)){ 
				assertTrue(map.get("mnemonic").equals(prodMnemonic));
				assertTrue(map.get("isEligible").toString().equals(eligibilityValue));
				return;
			}
		}
    }
	
	
	@When("^I should have the following suitable products \"([^\"]*)\"$")
	public void i_should_have_the_following_suitable_products(String suitableProductListFile) throws Exception{
		
		String inputRequest = readRequest(suitableProductListFile);
		String[] strArr = inputRequest.split(","); 
		
		String messageBody = deciResultMap.get("StatusMessage");
		List<Object>  eligibleProducts = JsonUtils.genericProductSelectorParseJsonResponseList(messageBody,"suitableProducts");
		for(int i=0;i<strArr.length;i++){
			eligibleProducts.remove(strArr[i]);
		}
		
		assertTrue(eligibleProducts.size()==0);
    }
	
	//String inputRequest = readRequest(requestData);
	
	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/deci", fileName);
	}
	
}
