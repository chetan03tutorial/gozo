package com.lbg.ib.api.secondActivateProduct;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/secondActivateProductFeature", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/secondActivateProductFeature.json" }, tags = { "~@ignore" })
public class SecondActivateProductFeatureTest {
}
