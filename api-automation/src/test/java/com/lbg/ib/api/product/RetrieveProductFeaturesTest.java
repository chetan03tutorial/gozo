package com.lbg.ib.api.product;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author ssama1
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/retrieveProductFeatures", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-RetrieveProductFeaturesTest.json" }, tags = { "~@ignore" })
public class RetrieveProductFeaturesTest {

}
