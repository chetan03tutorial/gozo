/**
 * 
 */
package com.lbg.ib.api.BranchContextService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.response.Cookie;
import com.jayway.restassured.response.Cookies;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.lbg.api.uploadDocumentation.UploadDocumentStepFeaturesStep;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.pendingarrangement.RetrievePendingProductStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.BranchContextImpl;
import com.lbg.ib.api.test.services.DocUploadServiceImpl;
import com.lbg.ib.api.test.services.OfferProductFeaturesImpl;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class BranchContextServiceStepFeaturesStep extends BaseEnvSetUp {

	private OfferProductFeaturesImpl offerProductFeaturesService;
	public Map<String, String> actualResult;
	public Map<String, String> actualResultOffer;
	private BranchContextImpl branchContextImpl;
	private RetrieveProductFeaturesImpl retrieveProductFeaturesImpl;
	private RetrievePendingProductStep retrievePendingProductStep;
	private UploadDocumentStepFeaturesStep uploadDocFeatureStep;
	private DocUploadServiceImpl docUploadService;
	public static ThreadLocal<String> arrangementId=new ThreadLocal<String>();
	
	/* // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> arrangementId =
        new ThreadLocal<Integer>() {
            @Override protected Integer initialValue() {
                return nextId.getAndIncrement();
        }
    };*/


	public BranchContextServiceStepFeaturesStep() throws Exception {
		super();
		offerProductFeaturesService = new OfferProductFeaturesImpl();
		branchContextImpl = new BranchContextImpl();
		retrieveProductFeaturesImpl = new RetrieveProductFeaturesImpl();
		retrievePendingProductStep = new RetrievePendingProductStep();
		uploadDocFeatureStep = new UploadDocumentStepFeaturesStep();
		docUploadService = new DocUploadServiceImpl();
	}

	@Given("Perform the branch context setting operation endpoint \"([^\"]*)\" with colleagueId \"([^\"]*)\" and domain \"([^\"]*)\"$")
	public void setBranchContextOperation(String endpoint, String colleagueId, String domain) throws IOException {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = branchContextImpl.getResponseFromAPI(url + "/" + colleagueId + "/" + domain);
		setSessionId(actualResult.get("sessionId"));
		setCookieMap(branchContextImpl.getCookieMap());
		assertTrue(actualResult != null);
	}

	@Then("Check the Branch Context Content using endpoint \"([^\"]*)\"$")
	public void checkBranchContext(String endpoint) throws IOException {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = branchContextImpl.getResponseFromAPIUsingSessionId(getSessionId(), url);
		assertTrue(actualResult != null);
	}

	@Then("^Perform the branchContext operation using enpoint and Request \"([^\"]*)\" \"([^\"]*)\"$")
	public void branchContextOperation(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = offerProductFeaturesService.getResponseFromAPIWithCookieMap(url, readRequest(requestData),
				getCookieMap());
		// setSessionId(actualResult.get("sessionId"));
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

	@Given("^product retrieve service is up and running \"([^\"]*)\" \"([^\"]*)\"$")
	public void retrieveProduct(String endpoint, String urlIdentifier) throws Throwable {
		String host = (String) this.getEnvProperty().get(endpoint);
		host = host.trim();
		String serviceEndPoint = new StringBuilder(host).append(urlIdentifier).toString();
		actualResult = branchContextImpl.getResponseFromAPIUsingSessionId(getSessionId(), serviceEndPoint);
		setSessionId(actualResult.get("sessionId"));
		setCookieMap(branchContextImpl.getCookieMap());
	}

	@Then("^verify product name \"([^\"]*)\"$")
	public void verifyProductName(String productName) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String productJson = JsonUtils.parseJson(messageBody, "product");
		String prdName = JsonUtils.parseJson(productJson, "name").replaceAll("\"", "");
		assertTrue(productName.equalsIgnoreCase(prdName));

	}

	@Then("^verify product mnemonic \"([^\"]*)\"$")
	public void verifyProductMnemonic(String mnemonic) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String productJson = JsonUtils.parseJson(messageBody, "product");
		String prdMnemonic = JsonUtils.parseJson(productJson, "mnemonic").replaceAll("\"", "");
		assertTrue(mnemonic.equalsIgnoreCase(prdMnemonic));
	}

	@Then("^verify service status \"([^\"]*)\"$")
	public void verifyServiceStatus(String serviceStatus) throws Throwable {
		String httpStatus = actualResult.get("StatusCode").replaceAll("\"", "");
		assertTrue(serviceStatus.equalsIgnoreCase(httpStatus));
	}

	@Then("^verify response code \"([^\"]*)\"$")
	public void verifyResponseCode(String responseCode) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String errorCode = JsonUtils.parseJson(messageBody).getAsJsonObject("error").get("code").getAsString();
		assertTrue(responseCode.equalsIgnoreCase(errorCode));
	}

	@Given("^we send a request for offer service \"([^\"]*)\" \"([^\"]*)\"$")
	public void offerProduct(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = offerProductFeaturesService.getResponseFromAPIWithCookieMap(url, readOfferRequest(requestData),
				getCookieMap());
		actualResultOffer=actualResult;
		assertTrue(actualResult != null);
		// setSessionId(actualResult.get("sessionId"));
		// setCookieMap(branchContextImpl.getCookieMap());
	}

	@Then("^verify product mnemonic of offer \"([^\"]*)\"$")
	public void validateOfferedProductMnemonic(String mnemonic) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String productMnemonic = JsonUtils.parseJsonRecursive(messageBody, "mnemonic");
		assertTrue(productMnemonic.equalsIgnoreCase(mnemonic));
	}

	@Then("^verify eidv score \"([^\"]*)\"$")
	public void validateEidvScore(String eidvScore) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String eidv = JsonUtils.parseJsonRecursive(messageBody, "eidvScore");
		assertTrue(eidvScore.equalsIgnoreCase(eidv));
	}

	@Then("^verify asm score \"([^\"]*)\"$")
	public void validateAsmScore(String asmScore) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String asm = JsonUtils.parseJsonRecursive(messageBody, "asmScore");
		assertTrue(asmScore.equalsIgnoreCase(asm));
	}

	@Then("^we send a request for activate service \"([^\"]*)\" \"([^\"]*)\"$")
	public void we_send_a_request_for_activate_service(String activateEndpoint, String activateRequest)
			throws Throwable {
		arrangementId.set( offerProductFeaturesService.getArrangementId(actualResult));
		String activateServiceEndPoint = this.getEnvProperty().getProperty(activateEndpoint)
				+ offerProductFeaturesService.getArrangementId(actualResult);
		actualResult = offerProductFeaturesService.getResponseFromAPIWithCookieMap(activateServiceEndPoint,
				readActivateRequest(activateRequest), getCookieMap());
	}


	public Map<String, String> getResponseFromAPIWithCookieMap(String apiURL, Map<String, String> cookieMap) {
		Map<String, String> responseMap = new HashMap<String, String>();
		System.out.println("=================api url:" + apiURL);
		List<Cookie> cookies = new ArrayList<Cookie>();
		for(Map.Entry<String, String> entry : cookieMap.entrySet()){
			cookies.add(new Cookie.Builder(entry.getKey(),entry.getValue()).build());
		}
		
		Response response = given().cookies(new Cookies(cookies)).get(apiURL).thenReturn();

		ResponseBody responseBody = response.getBody();
		String msg = responseBody.asString();
		System.out.println("message:" + msg);
		String resCode = String.valueOf(response.getStatusCode());
		System.out.println("resCode:" + resCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", resCode);
		System.out.println("session Id for offer product:"+response.getSessionId());
		//responseMap.put("sessionId", sessionId);//response.getSessionId());
		
		//this.cookieMap = response.getCookies();
		return responseMap;
	}
	

	@Then("^validate the response of activate service \"([^\"]*)\"$")
	public void validate_the_response_of_activate_service(String applicationStatus) throws Throwable {
		String statusMessage = actualResult.get("StatusMessage");
		String activateAppStatus = JsonUtils.parseJson(statusMessage, "applicationStatus").replaceAll("\"", "");
		assertTrue(applicationStatus.trim().equalsIgnoreCase(activateAppStatus.trim()));
	}

	@Given("^for a  branch colleague, I have an arrangementId\"([^\"]*)\" of a customer$")
	public void for_a_branch_colleague_I_have_an_arrangementId_of_a_customer(String arrangementId) throws Throwable {
		retrievePendingProductStep.for_a_branch_colleague_I_have_an_arrangementId_of_a_customer(arrangementId);
	}

	@When("^I make a call to the retrieveProductArrangement API at retrieveArrangement-endpoint \"([^\"]*)\"$")
	public void i_make_a_call_to_the_retrieveProductArrangement_API_at_retrieveArrangement_endpoint(String endpoint)
			throws Throwable {
		retrievePendingProductStep
				.i_make_a_call_to_the_retrieveProductArrangement_API_at_retrieveArrangement_endpoint(endpoint);
	}

	@Then("^I should see the repsonse status as \"([^\"]*)\"$")
	public void i_should_see_the_repsonse_status_as(String arg1) throws Throwable {
		retrievePendingProductStep.i_should_see_the_repsonse_status_as(arg1);
	}

	@Then("^I should see customer details response as \"([^\"]*)\"$")
	public void i_should_see_customer_details_response_as(String responseParam) throws Throwable {
		retrievePendingProductStep.i_should_see_customer_details_response_as(responseParam);
	}

	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/branchContext", fileName);
	}
	
	public String readRequestDocUp(String fileName) {
		return readRequestData("jsonRequestFiles/docUpload", fileName);
	}

	public String readOfferRequest(String fileName) {
		return readRequestData("jsonRequestFiles/offer", fileName);
	}

	public String readActivateRequest(String fileName) {
		return readRequestData("jsonRequestFiles/activate", fileName);
	}
	
	@When("^we send a request for createCase service \"([^\"]*)\" with request \"([^\"]*)\" and failFlag \"([^\"]*)\"$")
	public void we_send_a_request_for_createCase_service_with_request_and_failFlag(String endpoint, String requestData, String failFlag) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String arrangementId = offerProductFeaturesService.getArrangementId(actualResultOffer);
//		Threadl
		uploadDocFeatureStep.we_send_a_request_for_createCase_service_with_request(endpoint, requestData, failFlag);
	}

	@Then("^validate the \"([^\"]*)\" response of Create Case Service for Success$")
	public void validate_the_response_of_Create_Case_Service_for_Success(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		uploadDocFeatureStep.validate_the_response_of_Create_Case_Service_for_Success(arg1);
		
	}

	@Then("^Update \"([^\"]*)\"$")
	public void update(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		System.out.println("Update done");
	}

	@Then("^we send a request for Document Upload service \"([^\"]*)\" with request \"([^\"]*)\"$")
	public void we_send_a_request_for_Document_Upload_service_with_request(String endpoint, String requestData) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		String url = (String) this.getEnvProperty().get(endpoint);
		/*actualResult = offerService.getResponseFromAPIWithCookieMap(url, readRequest(requestData),getCookieMap());
		assertTrue(actualResult != null);*/
		
//		setCookieMap(service.getCookieMap());
//		Map<String, String> cookieMap = SSLUtil.getCookies(url);
		String incomingRequest=readRequestDocUp(requestData);
		incomingRequest=uploadDocFeatureStep.prepareForUploadDocument(incomingRequest);
//		 JsonManager.update(incomingRequest, "$.partyDetails[0].casePartyID", casePartyId).
		actualResult = docUploadService.getMultipartResponse(url, incomingRequest,getCookieMap());
		assertTrue(actualResult != null);
		
//		uploadDocFeatureStep.we_send_a_request_for_Document_Upload_service_with_request(arg1, arg2);
	}

	@Then("^validate the \"([^\"]*)\" response of Document Upload$")
	public void validate_the_response_of_Document_Upload(String success) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		uploadDocFeatureStep.validate_the_response_of_Document_Upload(success);
		String messageBody = actualResult.get("StatusMessage");
		String successCode = JsonUtils.parseJsonRecursive(messageBody, "success");
		assertTrue(successCode.equalsIgnoreCase(success));
	}
	
	
	@Then("^we send a request for second activate service \"([^\"]*)\" \"([^\"]*)\"$")
	public void activateProduct(String activateEndpoint, String activateRequest) throws Throwable {
		String activateServiceEndPoint = this.getEnvProperty().getProperty(activateEndpoint)
				+ offerProductFeaturesService.getArrangementId(actualResultOffer);
		String incomingReq = readActivateRequest(activateRequest);
		int indexofCaseReferenceNo= incomingReq.indexOf("\"documentReferenceIndex\":");
		String caseReferenceNo = incomingReq.substring(indexofCaseReferenceNo+"\"documentReferenceIndex\":".length(), incomingReq.indexOf(",", indexofCaseReferenceNo));
		String trimmedCaseRefNo = caseReferenceNo.trim();
		trimmedCaseRefNo=trimmedCaseRefNo.replaceAll("\"", "");
		String docRefNo = getDocRefNo();
		incomingReq=incomingReq.replace(trimmedCaseRefNo, docRefNo);
		actualResult = offerProductFeaturesService.getResponseFromAPIWithCookieMap(activateServiceEndPoint,
				incomingReq, getCookieMap());
	}
	
	@Then("^verify error code is \"([^\"]*)\"$")
	public void verifyErrorMessage(String msg) {
		String messageBody = actualResult.get("StatusMessage");
		String errorMessage = JsonUtils.parseJsonRecursive(messageBody, "code");
		assertTrue(msg.equalsIgnoreCase(errorMessage));
	}

	
	private String getDocRefNo(){
		String messageBody = actualResult.get("StatusMessage");
		JsonElement jelement = new JsonParser().parse(messageBody);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonElement jelement1 = jobject.get("result");
		if(jelement1!=null && jelement1!=JsonNull.INSTANCE && jelement1.getAsJsonObject().get("partyDetails")!=null){
			JsonArray partyDetailsArrObj=jelement1.getAsJsonObject().get("partyDetails").getAsJsonArray();
			if(partyDetailsArrObj.get(0).getAsJsonObject().get("evidences")!=null){
				JsonArray evidencesArrayObj=partyDetailsArrObj.get(0).getAsJsonObject().get("evidences").getAsJsonArray();
				if(evidencesArrayObj.get(0).getAsJsonObject().get("documents")!=null){
					JsonArray documentsArrayObj=evidencesArrayObj.get(0).getAsJsonObject().get("documents").getAsJsonArray();
					if(documentsArrayObj.get(0).getAsJsonObject().get("uploadSequenceNo")!=null){
						String uploadSequenceNumber = documentsArrayObj.get(0).getAsJsonObject().get("tmpSysFileRefNum").getAsString();
						return uploadSequenceNumber;
					}
				}
			}
		}

		return null;
	}
}
