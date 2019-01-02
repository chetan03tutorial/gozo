package com.lbg.ib.api.services;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class UserInfoFeatureStep extends BaseFeatures  {

	@Given("^user info service is up and running \"([^\"]*)\"$")
	@RestInvoker(service = "userInfo", method = HttpMethod.GET)
	public void userInfoService(@Brand String brand) throws Throwable {

	}
}
