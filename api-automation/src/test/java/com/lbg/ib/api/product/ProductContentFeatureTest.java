package com.lbg.ib.api.product;

import java.util.Map;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.test.services.ProductContentFeatureImpl;

import cucumber.api.java.en.Then;

/**
 * @author ssama1
 *
 */
public class ProductContentFeatureTest extends BaseEnvSetUp {

	public static final String ENV = "env";
	private ProductContentFeatureImpl service;
	public Map<String, String> actualResult;
	/*private String sessionId;*/
	
	
	public ProductContentFeatureTest() throws Exception {
		super();
		service = new ProductContentFeatureImpl();
	}
	
	@Then("^I should see  field \"([^\"]*)\" as \"([^\"]*)\"$")
	public void i_should_see_field_as(String productMnemonic, String mnemonic) throws Throwable {
	}
	
}
