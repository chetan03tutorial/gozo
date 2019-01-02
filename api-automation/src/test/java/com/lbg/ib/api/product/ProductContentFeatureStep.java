package com.lbg.ib.api.product;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/productContent", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-ProductContentFeatureTest.json" }, tags = { "~@ignore" })
public class ProductContentFeatureStep {

}
