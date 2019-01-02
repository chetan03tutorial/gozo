package com.lbg.ib.api.workingDays;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/workingDays", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-WorkingDaysFeaturesTest.json" }, tags = { "~@ignore" })
public class WorkingDaysFeaturesTest {
}
