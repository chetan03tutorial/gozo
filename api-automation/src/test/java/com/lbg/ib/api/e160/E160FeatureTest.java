package com.lbg.ib.api.e160;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/e160", glue = {"com.lbg.ib.api.e160"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-e160.json" }, tags = { "~@ignore" })
public class E160FeatureTest {

}
