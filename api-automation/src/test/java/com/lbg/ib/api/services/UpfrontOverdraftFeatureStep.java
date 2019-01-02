package com.lbg.ib.api.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class UpfrontOverdraftFeatureStep extends BaseFeatures {

	@Given("^overdraft upfront service is up and running \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	@RestInvoker(service = "upfrontOverdraft", method = HttpMethod.POST)
	public void upfrontOverdraftLimit(@Brand String brand, String accountNo,
			String sortCode) throws Throwable {
		UFERequest ufeRequest = new UFERequest(accountNo, sortCode);
		request.setRequestBody(convertToJson(ufeRequest));
	}
	
	@Then("^verify the upfront overdraft limit \"([^\"]*)\" \"([^\"]*)\"$")
	public void verify_the_upfront_overdraft_limit(@Brand String brand, String maxOverdraft) throws Throwable {
		String maxOdLimit = (String) response.getPropValue("maxOverDraftLimit");
		assertEquals(maxOverdraft,maxOdLimit);
	}

	private String convertToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return requestBody;
	}

	public class UFERequest {

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
