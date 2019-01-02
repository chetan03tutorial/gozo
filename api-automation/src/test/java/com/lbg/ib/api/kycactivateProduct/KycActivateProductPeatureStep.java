/**
 * 
 */
package com.lbg.ib.api.kycactivateProduct;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.OfferService.OfferServiceStepFeaturesStep;
import com.lbg.ib.api.product.RetrieveProductFeaturesStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.ActivateProductImpl;
import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class KycActivateProductPeatureStep extends BaseEnvSetUp {

	private RetrieveProductFeaturesStep retrieveService;
	private OfferServiceStepFeaturesStep offerService;
	private ActivateProductImpl activateService;
	public Map<String, String> activateResponseMap;
	private RecordProductArrangementQuestionnireImpl recordService;
	private Map<String, String> recordResponseMap;

	public KycActivateProductPeatureStep() throws Exception {
		super();
		activateService = new ActivateProductImpl();
		offerService = new OfferServiceStepFeaturesStep();
		retrieveService = new RetrieveProductFeaturesStep();
		recordService = new RecordProductArrangementQuestionnireImpl();
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

	@Then("^we send a request for activate service \"([^\"]*)\" \"([^\"]*)\"$")
	public void activateProduct(String activateEndpoint, String activateRequest) throws Throwable {
		String activateServiceEndPoint = this.getEnvProperty().getProperty(activateEndpoint)
				+ offerService.getArrangementId();
		activateResponseMap = activateService.getResponseFromAPI(activateServiceEndPoint, readRequest(activateRequest),
				getCookieMap());
	}

	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/activate", fileName);
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
}
