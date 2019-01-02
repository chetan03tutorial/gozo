package com.lbg.ib.api.q226;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/q226", glue = {
		"com.lbg.ib.api.automation", "com.lbg.ib.api.q226" }, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-SearchPartyFeaturesTest.json" }, tags = { "~@ignore" })
public class Q226FeaturesTest {

}
