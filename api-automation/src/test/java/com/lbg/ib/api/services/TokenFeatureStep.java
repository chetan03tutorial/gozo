package com.lbg.ib.api.services;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class TokenFeatureStep extends BaseFeatures {

	@Given("^token service is up and running \"([^\"]*)\" \"([^\"]*)\"$")
	@RestInvoker(service = "token", method = HttpMethod.POST)
	public void tokenService(@Brand String brand,
			@RequestBody(file = true) String tokenBody) throws Throwable {

	}
}
