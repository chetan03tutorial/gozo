/**
 * 
 */
package com.lbg.ib.api.productParty;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * 
 *
 * @author  Amit
 * @version 1.0
 * @since   14thFeb2016
 ***********************************************************************/
public class ProductPartySegment_206FeaturesStep extends BaseEnvSetUp {
	
	public static final String ENV = "env";
	private RetrieveProductFeaturesImpl retrieveProductFeaturesImpl;
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String serviceEndPoint;

	/**
	 * @throws Exception
	 * 
	 */

	public ProductPartySegment_206FeaturesStep() throws Exception {
		super();
		retrieveProductFeaturesImpl = new RetrieveProductFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Given("^I have a service running on \"([^\"]*)\" for a brand$")
	public void i_have_a_service_running_on_for_a_brand(String envHost) throws Throwable {
		System.out.println("envHost" + envHost);
		//String currentEnv = System.getProperty(ENV);
		String currentEnv ="test";
		envHost = envHost.replaceAll("env", currentEnv);
		System.out.println("current env host" + envHost);
		host = envProperty.getProperty(envHost);
		System.out.println("hostName:" + host);
		assertTrue(host != null);
	}

	@Given("^urlidentifier as \"([^\"]*)\"$")
	public void urlidentifier_as(String urlIdentifier) throws Throwable {
		serviceEndPoint = new StringBuilder(host).append(urlIdentifier).toString();
		System.out.println("service Endpoint:" + serviceEndPoint);
		assertTrue(serviceEndPoint != null);
	}

	@When("^I try to get response from the API$")
	public void i_try_to_get_response_from_the_API() throws Throwable {
		actualResult = retrieveProductFeaturesImpl.getResponseFromAPI(serviceEndPoint);
		assertTrue(actualResult != null);
	}

	@When("^I should see the response details with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_and(String productName, String mnemonic) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String productJson = JsonUtils.parseJson(messageBody, "product");
		System.out.println("product json:" + productJson);
		String actualProductName = JsonUtils.parseJson(productJson, "name").replaceAll("\"", "");
		String actualMnemonic = JsonUtils.parseJson(productJson, "mnemonic").replaceAll("\"", "");
		assertTrue(actualProductName.equalsIgnoreCase(productName));
		assertTrue(actualMnemonic.equalsIgnoreCase(mnemonic));

	}

	@Then("^I should see response with response code \"([^\"]*)\"$")
	public void i_should_see_response_with_response_code(String responseCode) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String responseCodeActual = JsonUtils.parseJson(messageBody, "code").replaceAll("\"", "");
		assertTrue(responseCodeActual.equalsIgnoreCase(responseCode));
	}
	
	@Then("^I should see http statuscode as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_as(String httpStatus) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));

	}

	@Then("^I should see message \"([^\"]*)\"$")
	public void i_should_see_message(String message) throws Throwable {
		String messageBody = actualResult.get("StatusMessage");
		String messageActual = JsonUtils.parseJson(messageBody, "message");
		if(null!=messageActual){
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.equalsIgnoreCase(message));
		}else{
			assertTrue(String.valueOf(messageActual).equalsIgnoreCase(message));
		}
		
	}
	
	@When("^I should see asmcode as \"([^\"]*)\"$")
	public void i_should_see_asmcode_as(String asmCode) throws Throwable {
		generic_i_should_see(asmCode,"asmCode");
	}
	
	@When("^I should see user first name as \"([^\"]*)\"$")
	public void i_should_see_first_name_as(String firstName) throws Throwable {
		generic_i_should_see(firstName,"firstName");
	}

	
	
	@When("^I should see user error msg as \"([^\"]*)\"$")
	public void i_should_see_error_msg_as(String errorMsg) throws Throwable {
		generic_i_should_see(errorMsg,"msg");
	}

	
	public void generic_i_should_see(String assertContent,String field){
		String messageBody = actualResult.get("StatusMessage");
		System.out.println(messageBody);
		String messageActual = JsonUtils.parseJsonRecursive(messageBody, field );
		if(null!=messageActual){
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.contains(assertContent));
		}else{
			assertTrue(String.valueOf(messageActual).contains(assertContent));
		}
	}

}
