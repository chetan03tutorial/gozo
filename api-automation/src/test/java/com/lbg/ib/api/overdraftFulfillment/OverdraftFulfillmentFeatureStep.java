package com.lbg.ib.api.overdraftFulfillment;

import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class OverdraftFulfillmentFeatureStep extends BaseFeatures {

	
	
	@Given("^token service is up and running for the user \"([^\"]*)\" for brand \"([^\"]*)\"$")
	@RestInvoker(service = "token", method = HttpMethod.POST)
	public void token_service_is_up_and_running_for_the_user_having_token_for_brand(
			@RequestBody(file = true) String request, @Brand String brand) throws Throwable {

	}
	
	@Given("^user info service is also up and running \"([^\"]*)\"$")
	@RestInvoker(service = "userInfo", method = HttpMethod.GET)
	public void user_info_service_is_also_up_and_running(@Brand String brand) throws Throwable {

	}

	@Given("^the overdraft upfront service is up and running for brand \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	@RestInvoker(service = "upfrontOverdraft", method = HttpMethod.POST)
	public void the_overdraft_upfront_service_is_up_and_running_for_brand(@Brand String brand, String accountNo, String sortCode) throws Throwable {
		UFERequest ufeRequest = new UFERequest(accountNo, sortCode);
		request.setRequestBody(convertToJson(ufeRequest));
	}
	
	@Given("^user request the overdraft amount \"([^\"]*)\" for brand \"([^\"]*)\"$")
	@RestInvoker(service = "fulfillment", method = HttpMethod.POST)
	public void then_hit_the_overdraft_fulfillment_for_brand(String demandedOverdraftAmount,
			@Brand String brand) throws Throwable {
		
		OdFulfillmentRequest fulfillmentRequest = new OdFulfillmentRequest(demandedOverdraftAmount);
		request.setRequestBody(convertToJson(fulfillmentRequest));
	}
	
	
	@Then("^expect an error$")
	public void expect_an_error() throws Throwable {
		response.getServiceStatus();
	}
	
	@Then("^amend the overdraft amount to \"([^\"]*)\"$")
	public void amend_the_overdraft_amount_to(String arg1) throws Throwable {
	    
	}
	
	@Then("^remove the overdraft amount to \"([^\"]*)\"$")
	public void remove_the_overdraft_amount_to(String arg1) throws Throwable {
	    
	}

	
	@Then("^create a new overdraft of amount \"([^\"]*)\"$")
	public void create_a_new_overdraft_of_amount(String arg1) throws Throwable {
	    
	}
	
	private String convertToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestBody;
	}
	public class OdFulfillmentRequest{
		
		public OdFulfillmentRequest(String amount) {
			this.demandedOd = amount;
		}
		private String demandedOd;

		public String getDemandedOd() {
			return demandedOd;
		}

		public void setDemandedOd(String demandedOd) {
			this.demandedOd = demandedOd;
		}
	}

	public class UFERequest{
		
		public UFERequest(String account, String sortCode) {
			this.accountNumber = account;
			this.sortCode = sortCode;
		}
		
		private String accountNumber;
		private String sortCode;
		
		public String getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}
		public String getSortCode() {
			return sortCode;
		}
		public void setSortCode(String sortCode) {
			this.sortCode = sortCode;
		}

		
	}
	
	

}
