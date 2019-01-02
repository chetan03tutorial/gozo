/**
 * 
 */
package com.lbg.ib.api.DocUpload;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.BranchContextService.BranchContextServiceStepFeaturesStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.services.util.SSLUtil;
import com.lbg.ib.api.test.services.DocUploadServiceImpl;
import com.lbg.ib.api.test.services.OfferProductFeaturesImpl;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class DocUploadStepFeaturesStep extends BaseEnvSetUp {

	
	private DocUploadServiceImpl offerService;
	public Map<String, String> actualResult;
	private String caseReferenceNumber;


	public DocUploadStepFeaturesStep() throws Exception {
		super();
		offerService = new DocUploadServiceImpl();	
	}

	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/docUpload", fileName);
	}
	
	public static void main(String[] args) {
		try {
			DocUploadStepFeaturesStep docUploadStepFeaturesStep = new DocUploadStepFeaturesStep();
			docUploadStepFeaturesStep.we_send_a_request_for_createCase_service_with_request("env.createCase.lloyds", "successCreateCaseRequest",null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Given("^we send a request for createCase service \"([^\"]*)\" with request \"([^\"]*)\" and failFlag \"([^\"]*)\"$")
	public void we_send_a_request_for_createCase_service_with_request(String endpoint, String requestData, String failFlag) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		//		setCookieMap(service.getCookieMap());
		//		Map<String, String> cookieMap = SSLUtil.getCookies(url);
		String incomingRequest = readRequest(requestData);
		if(getCookieMap()!=null){
			if(failFlag!=null && failFlag.equalsIgnoreCase("false")){
				incomingRequest = incrementCaseRefNo(incomingRequest, false);
			}
			actualResult = offerService.getResponseFromAPIWithCookieMap(url, incomingRequest,getCookieMap());
		}else{
			if(failFlag!=null && failFlag.equalsIgnoreCase("true")){
				incomingRequest = incrementCaseRefNo(incomingRequest, true);
			}
			actualResult = offerService.getResponseFromAPI(url, incomingRequest);
			setSessionId(actualResult.get("sessionId"));
			setCookieMap(offerService.getCookieMap());
		}
		assertTrue(actualResult != null);
		//		assertTrue(getCasepartyID()!=null);

	}

/*	@Given("^we send a request for createCase service \"([^\"]*)\" with request \"([^\"]*)\"$")
	public void we_send_a_request_for_createCase_service_with_request(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
//		setCookieMap(service.getCookieMap());
//		Map<String, String> cookieMap = SSLUtil.getCookies(url);
		actualResult = offerService.getMultipartResponse(url, readRequest(requestData));
		assertTrue(actualResult != null);
		assertTrue(getCasepartyID()!=null);
		
	}*/
	
	private String incrementCaseRefNo(String incomingReq, boolean isUniqueRequired){
		if(incomingReq.contains("\"caseReferenceNo\"")){
			int indexofCaseReferenceNo= incomingReq.indexOf("\"caseReferenceNo\":");
			String caseReferenceNo = incomingReq.substring(indexofCaseReferenceNo+"\"caseReferenceNo\":".length(), incomingReq.indexOf(",", indexofCaseReferenceNo));
			String trimmedCaseRefNo = caseReferenceNo.trim();
			trimmedCaseRefNo=trimmedCaseRefNo.replaceAll("\"", "");
			String uniqueRandomNumber =null;
			if(isUniqueRequired){
				uniqueRandomNumber = generateUniqueRandomNo();
			}else{
				uniqueRandomNumber = BranchContextServiceStepFeaturesStep.arrangementId.get();
			}
			System.out.println("Number"+uniqueRandomNumber);
			caseReferenceNumber = uniqueRandomNumber;
			incomingReq=incomingReq.replace(trimmedCaseRefNo, uniqueRandomNumber);

		}
		System.out.println();
		return incomingReq;
	}
	
	private String generateUniqueRandomNo(){
	        ArrayList<Integer> list = new ArrayList<Integer>();
	        for (int i=1; i<100; i++) {
	            list.add(new Integer(i));
	        }
	        Collections.shuffle(list);
	        StringBuffer strBuffer = new StringBuffer();
	        for (int i=0; i<3; i++) {
	        	strBuffer.append(String.valueOf(list.get(i)));
	    }
	        return strBuffer.toString();
	}
	
	public String getCaseReferenceNumber(){
		return caseReferenceNumber;
	}
	
	public String getCasepartyID(){
		String messageBody = actualResult.get("StatusMessage");
		JsonElement jelement = new JsonParser().parse(messageBody);
		if(jelement!=null){
			JsonObject jobject = jelement.getAsJsonObject();
			JsonElement jelement1 = jobject.get("result");
			if(null!=jelement1 && jelement1!=JsonNull.INSTANCE && null!=jelement1.getAsJsonObject().get("partyDetails")){
				JsonArray partyDetailsArrObj=jelement1.getAsJsonObject().get("partyDetails").getAsJsonArray();
				JsonElement jEl=partyDetailsArrObj.get(0).getAsJsonObject().get("casePartyID");
				return jEl.getAsString();
			}
		}
		return null;
	}
	
	public String getUploadSequenceNo(){
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
						String uploadSequenceNumber = documentsArrayObj.get(0).getAsJsonObject().get("uploadSequenceNo").getAsString();
						return uploadSequenceNumber;
					}
				}
			}
		}

		return null;
	}
	
	public String getCaseReferenceNo(){
		String messageBody = actualResult.get("StatusMessage");
		JsonElement jelement = new JsonParser().parse(messageBody);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonElement jelement1 = jobject.get("result");
		if(jelement1!=null && jelement1.getAsJsonObject().get("caseReferenceNo")!=null){
			JsonElement caseRefNoEl=jelement1.getAsJsonObject().get("caseReferenceNo");
			return caseRefNoEl.getAsString();
		}
		return null;
	}
	
	@Then("^validate the \"([^\"]*)\" response of Create Case Service for Success$")
	public void validate_the_response_of_Create_Case_Service_Success(String success) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String sucessMessage = JsonUtils.parseJsonRecursive(messageBody, "success");
		System.out.println("sucessMessage::"+sucessMessage+"\tsuccess::"+success);
		assertTrue(sucessMessage.equalsIgnoreCase(success));
	}
	
	@Then("^validate the \"([^\"]*)\" response of Create Case Service for message$")
	public void validate_the_response_of_Create_Case_Service_StatusMessage(String message) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String messageResponse = JsonUtils.parseJsonRecursive(messageBody, "message");
		assertTrue(messageResponse.equalsIgnoreCase(message));
	}

	@Then("^validate the \"([^\"]*)\" response of Create Case Service for error$")
	public void validateErrorMessage(String error) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String errorMessage = JsonUtils.parseJsonRecursive(messageBody, "error");
		assertTrue(errorMessage.equalsIgnoreCase(error));
	}
	
/*	@Then("^And validate the \"([^\"]*)\" response of status in error section$")
	public void validateErrorStatusResponse(String status) throws Throwable {
		String messageBody = actualResult.get("Status");
		String StatusResponse = JsonUtils.parseJsonRecursive(messageBody, "status");
		assertTrue(StatusResponse.equalsIgnoreCase(status));
	}*/
	
	@Then("^validate the \"([^\"]*)\" response of Create Case Service for messageType$")
	public void validateMessageTypeResponse(String messageType) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String messageTypeResponse = JsonUtils.parseJsonRecursive(messageBody, "messageType");
		assertTrue(messageTypeResponse.equalsIgnoreCase(messageType ));
	}
	

	
	@Then("^validate the \"([^\"]*)\" response of code in error section$")
	public void validate_the_response_of_status_in_error_section(String code) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		JsonElement jelement = new JsonParser().parse(messageBody);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonElement jelement1 = jobject.get("error");
		String errorCode = jelement1.getAsJsonObject().get("code").getAsString();
		assertTrue(errorCode.equalsIgnoreCase(code));
	}
	
	@Then("^validate the \"([^\"]*)\" response of status in error section$")
	
		
	public void validate_the_response_of_code_in_error_section(String status) throws Throwable {
        String messageBody = actualResult.get("StatusMessage");
       	JsonElement jelement = new JsonParser().parse(messageBody);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonElement jelement1 = jobject.get("error");
		String errorCode = jelement1.getAsJsonObject().get("status").getAsString();
		System.out.println("StatusRes"+errorCode+"\tStatus::"+status);
		assertTrue(errorCode.equalsIgnoreCase(status));
	}
	
	
	}
