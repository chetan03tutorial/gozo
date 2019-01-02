/**
 * 
 */
package com.lbg.ib.api.DetermineEligibilityCI;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.beans.DetermineEligibility;
import com.lbg.ib.api.services.beans.ProductOptions;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.DetermineEligibilityCIImpl;
import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;
import com.lbg.ib.api.test.services.RetrieveProductArrangementQuestionnireImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


public class DetermineEligibilityCIFeaturesStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private DetermineEligibilityCIImpl stepDefImpl;
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String serviceEndPoint;



	/**
	 * @throws Exception
	 * 
	 */
	public DetermineEligibilityCIFeaturesStep() throws Exception {	
		super();
		stepDefImpl = new DetermineEligibilityCIImpl();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

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
/*	
	@Given("^I have a \"([^\"]*)\" for the valid \"([^\"]*)\"$")
	public void i_have_a_service_running_on_for_a_brand(String envHost, String productIdentifier) throws Throwable {
		String value = (String)envProperty.get("test.deci.halifax");
		serviceEndPoint = new StringBuilder(value).append(productIdentifier).toString();
		assertTrue(host == null);
	}*/
	
	
	@Given("^I have a service running on \"([^\"]*)\" for a brand$")
	public void i_have_a_service_running_on_for_a_brand(String envHost) throws Throwable {
		String value = (String)envProperty.get("test.deci.halifax");
		System.out.println(value);
		host = value;
		System.out.println(host);
		
		
	}
	
	
	@When("^I try to get response from the API \"([^\"]*)\"$")
	public void i_try_to_get_response_from_the_API(String JsonFileName) throws Throwable {
		actualResult = stepDefImpl.getResponseFromAPI(host,readRequestData(JsonFileName));
		assertTrue(actualResult != null);
	}

	
	
	@When("^I should see the mnemonic as \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_mnemonic(String mnemonics) throws Throwable {
		
        System.out.println("----------------test123----------------");
        String messageBody = actualResult.get("StatusMessage");
        List<DetermineEligibility> eligible = JsonUtils.DECIparseJson(messageBody);
        for (DetermineEligibility eligiblity : eligible) {
        	System.out.println(eligible);
                       if (eligiblity.getmnenomic().equals(mnemonics));
                    	   assertTrue(eligiblity.getmnenomic().equals(mnemonics));
        }
        
	}

	@When("^I should see the eligiblity criteria is \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_isEligible(String isEligible) throws Throwable {

        System.out.println("----------------test123----------------");
        String messageBody = actualResult.get("StatusMessage");
        List<DetermineEligibility> eligible = JsonUtils.DECIparseJson(messageBody);
        for (DetermineEligibility eligiblity : eligible) {
        	System.out.println(eligible);
                       if (eligiblity.getisEligible().equalsIgnoreCase(isEligible));
                    	   assertTrue(eligiblity.getisEligible().equals(isEligible));
        }
        
	}
	
	@When("^I should see the description as \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_description(String desc) throws Throwable {
		
		System.out.println("----------------test123----------------");
        String messageBody = actualResult.get("StatusMessage");
        List<DetermineEligibility> eligible = JsonUtils.DECIparseJson(messageBody);
        for (DetermineEligibility eligiblity : eligible) {
        	System.out.println(eligible);
                       if (eligiblity.getDesc().equalsIgnoreCase(desc));
                    	   assertTrue(eligiblity.getDesc().equals(desc));
        }

	}
	
	@When("^I should see the code as \"([^\"]*)\"$")
	public void i_should_see_the_response_details_with_code(String code) throws Throwable {
		
        System.out.println("----------------test123----------------");
        String messageBody = actualResult.get("StatusMessage");
        List<DetermineEligibility> eligible = JsonUtils.DECIparseJson(messageBody);
        for (DetermineEligibility eligiblity : eligible) {
        	System.out.println(eligible);
                       if (eligiblity.getcode().equalsIgnoreCase(code));
                    	   assertTrue(eligiblity.getcode().equals(code));
        }

	}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
	
	@When("^I should see http statuscode as \"([^\"]*)\"$")
	public void i_should_see_http_statuscode_as(String httpStatus) throws Throwable {
		System.out.println(httpStatus);
		String httpStatusActual = actualResult.get("StatusCode");
		System.out.println(httpStatusActual);
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}

@When("^I should see the message as \"([^\"]*)\"$")
public void i_should_see_response_message(String message) throws Throwable {
	String messageBody = actualResult.get("StatusMessage");
	System.out.println(messageBody);
	String messageActual = JsonUtils.parseJson(messageBody, "msg");
	if(null!=messageActual){
		messageActual = messageActual.replaceAll("\"", "");
		assertTrue(messageActual.contains(message));
	}else{
		assertTrue(String.valueOf(messageActual).contains(message));
	}
}
	
	
}
