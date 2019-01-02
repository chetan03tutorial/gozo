/**
 * 
 */
package com.lbg.ib.api.RecordArrangementQue;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.OfferService.OfferServiceStepFeaturesStep;
import com.lbg.ib.api.product.RetrieveProductFeaturesStep;
import com.lbg.ib.api.services.beans.ProductOptions;
import com.lbg.ib.api.services.util.FileUtil;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.ActivateProductImpl;
import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RecordArrangementQuestionnaireFeaturesStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private RecordProductArrangementQuestionnireImpl stepDefImpl;
	
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String serviceEndPoint;
	private RetrieveProductFeaturesStep retrieveService;
	private OfferServiceStepFeaturesStep offerService;
	
	public RecordArrangementQuestionnaireFeaturesStep() throws Exception {	
		super();
			offerService = new OfferServiceStepFeaturesStep();
			retrieveService = new RetrieveProductFeaturesStep();
		stepDefImpl = new RecordProductArrangementQuestionnireImpl();
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
		
	
	@Given("^we have a record questionnarie service running on \"([^\"]*)\" for a brand$")
	public void i_have_a_service_running_on_for_a_brand(String envHost) throws Throwable {
		String value = (String)envProperty.get("test.recordque.halifax");
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

	@When("^we try to get response from the API \"([^\"]*)\"$")
	public void i_try_to_get_response_from_the_API(String JsonFileName) throws Throwable {
		String jsonRequest = readRequestData(JsonFileName);
		String arrangementId = offerService.getArrangementId();
		//replace the arrangementId with the one we got from offer
		String jsonRequestWithArrangementId = jsonRequest.replaceAll("\\\"arrangementId\\\" : (\\\"\\d+\\\"),+", "\"arrangementId\" : \"" + arrangementId + "\",");
		actualResult = stepDefImpl.getResponseFromAPI(host,jsonRequestWithArrangementId);
		assertTrue(actualResult != null);
	}
	
	@When("^we should see http statuscode as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_as(String httpStatus) throws Throwable {
		System.out.println(httpStatus);
		String httpStatusActual = actualResult.get("StatusCode");
		System.out.println(httpStatusActual);
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}
	
	@When("^we should see the response code \"([^\"]*)\"$")
    public void i_should_see_response_code(String reasonCode) throws Throwable {
    	String jsonMessage = actualResult.get("StatusMessage").replaceAll("\"", "");
        JsonObject jsonObject = JsonUtils.parseJson(jsonMessage);
        String resultedReasonCode = jsonObject.get("reasonCode").toString();
        assertTrue(resultedReasonCode.equalsIgnoreCase(reasonCode));
	}
	
	@When("^we should see the error code \"([^\"]*)\"$")
	public void i_should_see_the_error_code(String errorCode) throws Throwable {
		String jsonMessage = actualResult.get("StatusMessage");
        JsonObject jsonObject = JsonUtils.parseJson(jsonMessage);
        JsonObject error = jsonObject.getAsJsonObject("error");
        String actualErrorCode = error.get("code").toString();
        actualErrorCode=actualErrorCode.replaceAll("\"", "");
        assertTrue(actualErrorCode.equalsIgnoreCase(errorCode));
	}


	
/*	@When("^I should see the response code \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_code(String reasonCode) throws Throwable {
		
        System.out.println("----------------test123----------------");
        String messageBody = actualResult.get("StatusMessage");
        List<ProductOptions> options = JsonUtils.parseJson(messageBody, reasonCode);
        for (ProductOptions prodOption : options) {
        	System.out.println(prodOption);
                       if (prodOption.getDesc().equals(reasonCode))
                    	   assertTrue(prodOption.getDesc().equals(reasonCode));
        }

	}
       */                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	
	
	
	
	/*@When("^I should see response message \"([^\"]*)\"$")
	public void i_should_see_response_message(String message) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		System.out.println(messageBody);
		String messageActual = JsonUtils.parseJson(messageBody, "message");
		if (null != messageActual) {
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.contains(message));
		} else {
			assertTrue(String.valueOf(messageActual).contains(message));
		}
	}*/
	
	
	/*@When("^I try to post valid request with valid inputs$")
	public void i_try_to_post_valid_request_with_valid_inputs() throws Throwable {
	    
	}*/


	
}


