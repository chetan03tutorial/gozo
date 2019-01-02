package com.lbg.ib.api.Addresslookup;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/AddressLookUp", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-AddressLookupFeaturesTest.json" }, tags = { "~@ignore" })
public class AddressLookupFeaturesTest {

}
