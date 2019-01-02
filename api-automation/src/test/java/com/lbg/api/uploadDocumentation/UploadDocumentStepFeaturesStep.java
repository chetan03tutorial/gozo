/**
 * 
 */
package com.lbg.api.uploadDocumentation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.DocUpload.DocUploadStepFeaturesStep;
import com.lbg.ib.api.services.util.JsonManager;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.services.util.SSLUtil;
import com.lbg.ib.api.test.services.DocUploadServiceImpl;
import com.lbg.ib.api.test.services.OfferProductFeaturesImpl;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class UploadDocumentStepFeaturesStep extends BaseEnvSetUp {

	
	private DocUploadServiceImpl offerService;
	private DocUploadStepFeaturesStep docUploadFeatureStep;
	public Map<String, String> actualResult;
	private String casePartyId;
	private String uploadSequenceNumber;
	private String caseReferenceNumber;


	public UploadDocumentStepFeaturesStep() throws Exception {
		super();
		offerService = new DocUploadServiceImpl();
		docUploadFeatureStep = new DocUploadStepFeaturesStep();
	}

	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/docUpload", fileName);
	}


	@Given("^we send a request for createCase service \"([^\"]*)\" with request \"([^\"]*)\" and failFlag \"([^\"]*)\"$")
	public void we_send_a_request_for_createCase_service_with_request(String endpoint, String requestData, String failFlag) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		docUploadFeatureStep.we_send_a_request_for_createCase_service_with_request(endpoint, requestData,failFlag);
	}

	@Then("^validate the \"([^\"]*)\" response of Create Case Service for Success$")
	public void validate_the_response_of_Create_Case_Service_for_Success(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		casePartyId= docUploadFeatureStep.getCasepartyID();
		uploadSequenceNumber = docUploadFeatureStep.getUploadSequenceNo();
		caseReferenceNumber= docUploadFeatureStep.getCaseReferenceNumber();
		assertTrue(docUploadFeatureStep.getCasepartyID()!=null);
	}

	/*@Then("^Update \"([^\"]*)\"$")
	public void update(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    assertTrue(1==1);
	}*/

	@Then("^we send a request for Document Upload service \"([^\"]*)\" with request \"([^\"]*)\"$")
	public void we_send_a_request_for_Document_Upload_service_with_request(String endpoint, String requestData) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String url = (String) this.getEnvProperty().get(endpoint);
		/*actualResult = offerService.getResponseFromAPIWithCookieMap(url, readRequest(requestData),getCookieMap());
		assertTrue(actualResult != null);*/
		
//		setCookieMap(service.getCookieMap());
//		Map<String, String> cookieMap = SSLUtil.getCookies(url);
		String incomingRequest=readRequest(requestData);
		incomingRequest=prepareForUploadDocument(incomingRequest);
//		 JsonManager.update(incomingRequest, "$.partyDetails[0].casePartyID", casePartyId).
		actualResult = offerService.getMultipartResponse(url, incomingRequest,getCookieMap());
		assertTrue(actualResult != null);
	}
	
	public String prepareForUploadDocument(String incomingReq){
		if(incomingReq.contains("\"caseReferenceNo\"")){
			int indexofCaseReferenceNo= incomingReq.indexOf("\"caseReferenceNo\":");
			String casePartyIdVal = incomingReq.substring(indexofCaseReferenceNo+"\"caseReferenceNo\":".length(), incomingReq.indexOf(",", indexofCaseReferenceNo));
			incomingReq=incomingReq.replace(casePartyIdVal, caseReferenceNumber);
		}
		if(incomingReq.contains("\"casePartyID\":")){
			int indexofCasepartyId= incomingReq.indexOf("\"casePartyID\":");
			String casePartyIdVal = incomingReq.substring(indexofCasepartyId+"\"casePartyID\":".length(), incomingReq.indexOf(",", indexofCasepartyId));
			incomingReq=incomingReq.replace(casePartyIdVal, casePartyId);
			
		}
		if(incomingReq.contains("\"uploadSequenceNo\":")){
			int indexofUploadSequenceNo= incomingReq.indexOf("\"uploadSequenceNo\":");
			String uploadSequenceNoVal = incomingReq.substring(indexofUploadSequenceNo+"\"uploadSequenceNo\":".length(), incomingReq.indexOf(",", indexofUploadSequenceNo));
			incomingReq=incomingReq.replace(uploadSequenceNoVal, uploadSequenceNumber);
		}
		
		return incomingReq;
	}

	@Then("^validate the \"([^\"]*)\" response of Document Upload$")
	public void validate_the_response_of_Document_Upload(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	}

	
	public String getArrangementId() {
		String statusMessage = actualResult.get("StatusMessage");
		String arrangementId = JsonUtils.parseJson(statusMessage, "arrangementId").replaceAll("\"", "");
		assertNotNull(arrangementId);
		return arrangementId;
	}
	
	
	public String getMessageBody(){
		String messageBody = actualResult.get("StatusMessage");
		return messageBody;
	}

	
	
	
	}
