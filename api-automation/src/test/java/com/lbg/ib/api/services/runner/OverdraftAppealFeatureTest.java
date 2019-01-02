package com.lbg.ib.api.services.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/odAppeal", glue = {"com.lbg.ib.api.services"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-overdraftAppeal.json" }, tags = { "~@ignore" })
public class OverdraftAppealFeatureTest {

}
