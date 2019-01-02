/**
 * 
 */
package com.lb.ib.api.authjourney;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.BranchContextService.BranchContextServiceStepFeaturesStep;
import com.lbg.ib.api.OfferService.OfferServiceStepFeaturesStep;
import com.lbg.ib.api.product.RetrieveProductFeaturesStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.ActivateProductImpl;
import com.lbg.ib.api.test.services.OfferProductFeaturesImpl;
import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AuthJourneyProductFeatureStep extends BaseEnvSetUp {

	private RetrieveProductFeaturesStep retrieveService;
	private OfferServiceStepFeaturesStep offerService;
	private ActivateProductImpl activateService;
	private RecordProductArrangementQuestionnireImpl recordService;
	private BranchContextServiceStepFeaturesStep branchContextServiceStepFeaturesStep;
	private OfferProductFeaturesImpl offerProductFeaturesImpl;
	
	private Map<String, String> recordResponseMap;
	private Map<String, String> responseMap = new HashMap<String,String>();
	private Map<String,String> offerResponse = new HashMap<String,String>();
	private String offRequestString = null;
	public Map<String, String> activateResponseMap;
	public Map<String,String> samlResponseMap = new HashMap<String,String>();
	public Map<String, String> actualResult = new HashMap<String,String>();	
	
	
	
	public AuthJourneyProductFeatureStep() throws Exception {
		super();
		activateService = new ActivateProductImpl();
		offerService = new OfferServiceStepFeaturesStep();
		retrieveService = new RetrieveProductFeaturesStep();
		branchContextServiceStepFeaturesStep = new BranchContextServiceStepFeaturesStep();
		recordService = new RecordProductArrangementQuestionnireImpl();
		offerProductFeaturesImpl = new OfferProductFeaturesImpl();
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
		try{
			Thread.sleep(1000*1);
		}catch(Exception e){
			
		}
		offRequestString = offerService.offerProductAndReturnReq(offerEndpoint, offerRequest);
	}

	@Then("^validate the response of offer service \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	public void validateOfferResponse(String mnemonic, String eidvScore, String asmScore) throws Throwable {
		offerService.validateOfferedProductMnemonic(mnemonic);
		offerService.validateEidvScore(eidvScore);
		offerService.validateAsmScore(asmScore);
	}

	@Then("^validate the ocisid of offer service$")
	public void validate_the_ocisid_of_offer_service() throws Throwable {
		offerService.validateOcis(offerResponse.get("ocisId"));
	}
	
	@Then("^fetch product value for from Offer Response \"([^\"]*)\"$")
	public void fetchOfferedProductFromOfferResponse(String argument) throws Throwable {
		String value = offerService.fetchOfferedProductFromOfferResponse(argument);
		offerResponse.put(argument, value);
	}
	
	@Then("^fetch product value for from Offer Request \"([^\"]*)\"$")
	public void fetchOfferedProductFromOfferResquest(String argument) throws Throwable {
		String value = JsonUtils.parseJsonRecursive(offRequestString, argument);
		offerResponse.put(argument, value);
	}
	
	@Then("^we send a request for activate service \"([^\"]*)\" \"([^\"]*)\"$")
	public void activateProduct(String activateEndpoint, String activateRequest) throws Throwable {
		try{
			Thread.sleep(1000*1);
		}catch(Exception e){
			
		}
		String activateServiceEndPoint = this.getEnvProperty().getProperty(activateEndpoint)
				+ offerService.getArrangementId();
		activateResponseMap = activateService.getResponseFromAPI(activateServiceEndPoint, readRequest(activateRequest),
				getCookieMap());
	}
	
	@Then("^validate the response of activate service \"([^\"]*)\"$")
	public void validate_the_response_of_activate_service(String applicationStatus)
			throws Throwable {
		String statusMessage = activateResponseMap.get("StatusMessage");
		String activateAppStatus = JsonUtils.parseJson(statusMessage, "applicationStatus").replaceAll("\"", "");
		assertTrue(applicationStatus.trim().equalsIgnoreCase(activateAppStatus.trim()));
	}

	@Then("^validate the response of record service \"([^\"]*)\"$")
	public void validateTheResponseOfRecordService(String reasonCode) throws Throwable {
		String statusMessage = recordResponseMap.get("StatusMessage");
		String responseReasonCode= JsonUtils.parseJson(statusMessage, "reasonCode").replaceAll("\"", "");
		assertTrue(reasonCode.trim().equalsIgnoreCase(responseReasonCode.trim()));
	}

	@And("^we send a request for record questionnarie service \"([^\"]*)\" \"([^\"]*)\"$")
	public void recordQuestionnarieService(String recordRequest, String recordEndpoint) throws Throwable {
		String recordURL = this.getEnvProperty().getProperty(recordEndpoint);
		String jsonRequest = readRequestData("jsonRequestFiles", recordRequest);
		String arrangementId = offerService.getArrangementId();
		//replace the arrangementId with the one we got from offer
		String jsonRequestWithArrangementId = jsonRequest.replaceAll("\\\"arrangementId\\\" : (\\\"\\d+\\\"),+", "\"arrangementId\" : \"" + arrangementId + "\",");
		recordResponseMap = recordService.getResponseFromAPI(recordURL, jsonRequestWithArrangementId);
	}	
	
	@Then("^Generate the Saml Asssertion \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	public void generateSaml(String samlAssertionUrl, String samlRequest,String brand) throws Throwable {
		String actualRequest = readRequest("jsonRequestFiles/saml",samlRequest);
		for(Map.Entry<String, String> entry : offerResponse.entrySet()){
			actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest,entry.getKey(),entry.getValue());
		}
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest,"countryPhoneCode",offerResponse.get("countryCode"));
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest,"fullNumber",offerResponse.get("number"));
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest,"brand",brand);
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest, "internetBankingSessionIdentifier","0000c8tD7C2gk1eSXZGwUMUyHeY:19kne7e5n");
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest, "cisRefNumber","00346567183");
		actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest, "isBranchContext","true");
		
		samlAssertionUrl = (String) this.getEnvProperty().get(samlAssertionUrl);
		samlResponseMap = offerProductFeaturesImpl.getResponseFromAPI(samlAssertionUrl, actualRequest);
	}

	
	@Given("Perform the branch context setting operation endpoint \"([^\"]*)\" with colleagueId \"([^\"]*)\" and domain \"([^\"]*)\"$")
	public void setBranchContextOperation(String endpoint,String colleagueId,String domain) throws IOException{
		branchContextServiceStepFeaturesStep.setBranchContextOperation(endpoint, colleagueId, domain);
		setSessionId(branchContextServiceStepFeaturesStep.getSessionId());
		setCookieMap(branchContextServiceStepFeaturesStep.getCookieMap());
	}
	
	@Then("^Perform the branchContext operation using enpoint and Request \"([^\"]*)\" \"([^\"]*)\"$")
	public void branchContextOperation(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = offerProductFeaturesImpl.getResponseFromAPIWithCookieMap(url, readRequestBranchContext(requestData),getCookieMap());
		//setSessionId(actualResult.get("sessionId"));
		assertTrue(actualResult != null);
	}	
	
	@Then("^verify colleagueId equals \"([^\"]*)\"$")
	public void validateColleagueId(String colleagueId) {
		String messageBody = actualResult.get("StatusMessage");
		String fetchedColleagueId = JsonUtils.parseJsonRecursive(messageBody, "colleagueId").replaceAll("\"", "");
		assertTrue(colleagueId.equals(fetchedColleagueId));
	}
	
	@Then("^verify authorized equals \"([^\"]*)\"$")
	public void validateAuthorized(String authorized) {
		String messageBody = actualResult.get("StatusMessage");
		String fetchedAuthorisation = JsonUtils.parseJsonRecursive(messageBody, "authorized").replaceAll("\"", "");
		assertTrue(authorized.equals(fetchedAuthorisation));
	}
	
	
	@When("^I Post the SAML token to Token Validate API as \"([^\"]*)\"$")
	public void i_Post_the_SAML_token_to_Token_Validate_API(String validateTokenHost) throws Throwable {
		validateTokenHost = (String) this.getEnvProperty().get(validateTokenHost);
		responseMap = offerProductFeaturesImpl.getResponseFromAPIWithCookieMap(validateTokenHost, samlResponseMap.get("StatusMessage"),getCookieMap());
		System.out.println(responseMap);
	}
	
	@When("^I should see http statuscode same as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_same_as(String httpStatus)
			throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		String httpStatusActual = responseMap.get("StatusCode")
				.replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}	

	@When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
	public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
		String messageBody = responseMap.get("StatusMessage");
		String attributeValue = JsonUtils.parseJsonRecursive(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}
	
	@When("^I Get the response from userInfo from API \"([^\"]*)\"$")
	public void i_get_the_response_from_userinfo_api(String apiURL) throws IOException{
		apiURL = (String)this.getEnvProperty().get(apiURL);
		responseMap = branchContextServiceStepFeaturesStep.getResponseFromAPIWithCookieMap(apiURL,getCookieMap());
        assertTrue(responseMap.size()>0);
	}	
	
	@Then("^I should see response with following user details$")
	public void i_should_see_response_with_following_user_details(DataTable dataTable){
		String messageBody = responseMap.get("StatusMessage");
		List<String> attibuteNames = dataTable.asList(String.class);
		for(int i=0;i<attibuteNames.size();i++){
			System.out.println(attibuteNames.get(i));
			assertTrue(JsonUtils.checkJsonRecursiveExists(messageBody, attibuteNames.get(i)));
		}
	}
	
	
	@Then("^Product retrieve service is up and running for product with session \"([^\"]*)\" \"([^\"]*)\"$")
	public void i_get_the_retrieve_product_features_session(String endpoint,String urlIdentifier) throws IOException{
		String host = (String)this.getEnvProperty().get(endpoint);
		host = host.trim();
		String serviceEndPoint = new StringBuilder(host).append(urlIdentifier).toString();
		responseMap = branchContextServiceStepFeaturesStep.getResponseFromAPIWithCookieMap(serviceEndPoint,getCookieMap());
		assertTrue(responseMap.size()>0);
		retrieveService.actualResult = responseMap;
	}
	
	@Then("^we send a request for offer service using auth journey \"([^\"]*)\" \"([^\"]*)\"$")
	public void offerProductAuth(String offerEndpoint, String offerRequest) throws Throwable {
		try{
			Thread.sleep(1000*1);
		}catch(Exception e){
			
		}
		String actualRequest = readRequest("jsonRequestFiles/offerAuth",offerRequest);
		offerResponse.remove("dob");
		for(Map.Entry<String, String> entry : offerResponse.entrySet()){
			actualRequest = JsonUtils.replaceJsonRecursiveExists(actualRequest,entry.getKey(),entry.getValue());
		}
		
		offerService.offerProductAndReturnReqFullTextBody(offerEndpoint, actualRequest);
	}


	@Then("^we send a request for auth deci request service using auth journey \"([^\"]*)\" \"([^\"]*)\"$")
	public void authDeciRequest(String offerEndpoint, String offerRequest) throws Throwable {
		String actualRequest = readRequest("jsonRequestFiles/deci",offerRequest);
		offerService.offerProductAndReturnReqFullTextBody(offerEndpoint, actualRequest);
		String msg = offerService.getMessageBody();
		assertTrue(msg.contains("Successfully"));
	}

	public String readRequest(String directory,String fileName) {
		return readRequestData(directory, fileName);
	}
	
	public String readRequestBranchContext(String fileName) {
		return readRequestData("jsonRequestFiles/branchContext", fileName);
	}
	
	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/activate", fileName);
	}
	
	
	public String readRequestData(String fileName){
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


}
