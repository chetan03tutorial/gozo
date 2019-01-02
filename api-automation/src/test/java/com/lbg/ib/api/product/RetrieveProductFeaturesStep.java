/**
 * 
 */
package com.lbg.ib.api.product;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author ssama1
 *
 */
public class RetrieveProductFeaturesStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private RetrieveProductFeaturesImpl service;
	public Map<String, String> actualResult;
	/*private String sessionId;*/
	
	
	public RetrieveProductFeaturesStep() throws Exception {
		super();
		service = new RetrieveProductFeaturesImpl();
	}
	
	@Given("^product retrieve service is up and running \"([^\"]*)\" \"([^\"]*)\"$")
	public void retrieveProduct(String endpoint, String urlIdentifier) throws Throwable {
		String host = (String)this.getEnvProperty().get(endpoint);
		host = host.trim();
		String serviceEndPoint = new StringBuilder(host).append(urlIdentifier).toString();
		actualResult = service.getResponseFromAPI(serviceEndPoint);
		setSessionId(actualResult.get("sessionId"));
		setCookieMap(service.getCookieMap());
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


}
