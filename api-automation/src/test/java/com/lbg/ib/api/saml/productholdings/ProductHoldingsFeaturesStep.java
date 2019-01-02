package com.lbg.ib.api.saml.productholdings;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jayway.restassured.path.json.JsonPath;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.SamlTokenFeaturesImpl;
import com.lbg.ib.api.test.services.SamlValidationFeaturesImpl;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ProductHoldingsFeaturesStep  extends BaseEnvSetUp {
	public static final String ENV = "env";
	private SamlValidationFeaturesImpl samlValidationFeaturesImpl;
	private SamlTokenFeaturesImpl samlTokenFeaturesImpl;
	public Map<String, String> getTokenResponse;
	public Map<String, String> validateTokenResponse;
	public Map<String, String> getUserInfoResponse;
	private String samlToken;
	private Properties envProperty;
	private String validateTokenHost;	
	private List<Map<String,String>> accounts;

	/**
	 * @throws Exception
	 * 
	 */
	public ProductHoldingsFeaturesStep() throws Exception {	
		super();
		samlValidationFeaturesImpl = new SamlValidationFeaturesImpl();
		samlTokenFeaturesImpl= new SamlTokenFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Given("^I have a service running on \"([^\"]*)\"$")
	public void i_have_a_service_running_on(String envHost) throws Throwable {
		validateTokenHost = envProperty.getProperty(envHost);
		assertTrue(validateTokenHost != null);
	}	
	
	@When("^I try to get response from the getToken API \"([^\"]*)\" the user \"([^\"]*)\"$")
	public void i_try_to_get_response_from_the_getToken_API(String envHost, String user) throws Throwable {
		String getTokenHost = envProperty.getProperty(envHost);
		getTokenResponse = samlTokenFeaturesImpl.getResponseFromAPI(getTokenHost+user);
	}
	
	@When("^I should see the response details with SAML token$")
	public void i_should_see_the_response_details_with_SAML_token() throws Throwable {
		String messageBody = getTokenResponse.get("StatusMessage");
		samlToken = JsonUtils.parseJsonRecursive(messageBody, "tokenID").replaceAll("\"", "");
		assertTrue(null != samlToken);
	}
	

	@When("^I Post the SAML token to Token Validate API$")
	public void i_Post_the_SAML_token_to_Token_Validate_API() throws Throwable {
		StringBuffer jsonRequest =  new StringBuffer();
		jsonRequest.append("{");
		jsonRequest.append("\"").append("tokenID").append("\"").append(":").append("\"").append(samlToken).append("\"");
		jsonRequest.append("}");
		validateTokenResponse=samlValidationFeaturesImpl.getResponseFromAPI(jsonRequest.toString(), validateTokenHost);
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
	
	@When("^I should see request message with \"([^\"]*)\" having value \"([^\"]*)\"$")
	public void i_should_see_request_message_with_having_value(String attribute, String value) throws Throwable {
		String messageBody = validateTokenResponse.get("StatusMessage");
		String attributeValue = JsonUtils.parseJsonRecursive(messageBody, attribute).replaceAll("\"", "");
		assertTrue("true".equalsIgnoreCase(attributeValue));
	}	
	
	@When("^I Get the response from userInfo API for the customer having eligible current,savings,Monthly saving and ISA Accounts from API \"([^\"]*)\"$")
	public void i_Get_the_response_from_userInfo_API_for_the_customer_having_eligible_current_savings_Monthly_saving_and_ISA_Accounts(String apiURL) throws Throwable {
		apiURL = envProperty.getProperty(apiURL);
        String sessionId = validateTokenResponse.get("sessionId");
        getUserInfoResponse = samlValidationFeaturesImpl.getUserInfoFromAPI(sessionId, apiURL);
        assertTrue(getUserInfoResponse.size()>0);
	}
	
	@Then("^I should see response with following user details$")
	public void i_should_see_response_with_following_user_details(DataTable dataTable){
		String messageBody = getUserInfoResponse.get("StatusMessage");
		List<String> attibuteNames = dataTable.asList(String.class);
		for(int i=0;i<attibuteNames.size();i++){
			System.out.println(attibuteNames.get(i));
			assertTrue(JsonUtils.checkJsonRecursiveExists(messageBody, attibuteNames.get(i)));
			if(attibuteNames.get(i).contains("lastLoggedInTime")){
				assertTrue(JsonUtils.parseJsonRecursive(messageBody, attibuteNames.get(i))!=null);
			}
		}
	}
	

	@Then("^I should see JSON response with following products as productType in accounts section$")
	public void i_should_see_JSON_response_with_following_products_as_productType_in_accounts_section(DataTable dataTable) throws Throwable {
		List<String> attibuteNames = dataTable.asList(String.class);	
		String messageBody = getUserInfoResponse.get("StatusMessage");
		JsonPath jsonPath = new JsonPath(messageBody);
		accounts = jsonPath.getList("accounts");
		boolean flag = false;
		for(String item : attibuteNames){
			for(Map<String,String> account : accounts) {
				String productType = account.get("productType"); 
					if(item.equals(productType)) {
						flag= true;
						break;
					} else {
						flag=false;
					}		
			}
		}	
		assertTrue(flag);
	}
	
	@Then("^I should see following fileds for current,savings,Monthly saving and ISA Accounts in accounts section$")
	public void i_should_see_following_fileds_for_current_savings_Monthly_saving_and_ISA_Accounts_in_accounts_section(DataTable dataTable) throws Throwable {
		List<String> attibuteNames = dataTable.asList(String.class);
		boolean flag = false;
		for(Map<String,String> account : accounts) {
			for(String item : attibuteNames){
				if(null != account.get(item)) {
					flag= true;
					continue;
				} else {
					flag=false;
					break;
				}		
		    }
	    }	
		assertTrue(flag);
	}	
	
	@Then("^I should not see following fileds for current,savings,Monthly saving and ISA Accounts in accounts section$")
	public void i_should_not_see_following_fileds_for_current_savings_Monthly_saving_and_ISA_Accounts_in_accounts_section(DataTable dataTable) throws Throwable {
		String messageBody = getUserInfoResponse.get("StatusMessage");
		List<String> attibuteNames = dataTable.asList(String.class);
		for(int i=0;i<attibuteNames.size();i++){
			System.out.println(attibuteNames.get(i));
			assertTrue(!JsonUtils.checkJsonRecursiveExists(messageBody, attibuteNames.get(i)));
		}

	}
	
	@Then("^I should see accountsFetched field as true$")
	public void i_should_see_accountsFetched_field_as_true() throws Throwable {
		String messageBody = getUserInfoResponse.get("StatusMessage");
		JsonPath jsonPath = new JsonPath(messageBody);
		boolean accountsFetchedStatus = jsonPath.getBoolean("accountsFetchedStatus");
		assertTrue(accountsFetchedStatus);
	}	

	@When("^I Get the response from userInfo API for the customer having no eligible current,savings,Monthly saving and ISA Accounts from API \"([^\"]*)\"$")
	public void i_Get_the_response_from_userInfo_API_for_the_customer_having_no_eligible_current_savings_Monthly_saving_and_ISA_Accounts_from_API(String apiURL) throws Throwable {
		apiURL = envProperty.getProperty(apiURL);
        String sessionId = validateTokenResponse.get("sessionId");
        getUserInfoResponse = samlValidationFeaturesImpl.getUserInfoFromAPI(sessionId, apiURL);
        assertTrue(getUserInfoResponse.size()>0);
	}

	@Then("^I should not see JSON response with following products as productType in accounts section$")
	public void i_should_not_see_JSON_response_with_following_products_as_productType_in_accounts_section(DataTable dataTable) throws Throwable {
		String messageBody = getUserInfoResponse.get("StatusMessage");
		List<String> attibuteNames = dataTable.asList(String.class);
		for(int i=0;i<attibuteNames.size();i++){
			System.out.println(attibuteNames.get(i));
			assertTrue(!JsonUtils.checkJsonRecursiveExists(messageBody, attibuteNames.get(i)));
		}
	}	
}
