package com.lbg.ib.api.services.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/upfrontOverdraftFeature", glue = {"com.lbg.ib.api.services"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-UpfrontOverdraftFeaturesTest.json" }, tags = { "~@ignore" })
public class UpfrontOverdraftFeaturesTest {

}
