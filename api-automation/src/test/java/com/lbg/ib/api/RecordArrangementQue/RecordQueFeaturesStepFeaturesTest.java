package com.lbg.ib.api.RecordArrangementQue;


import org.junit.runner.RunWith;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/RecordArrangementQue", format = { "pretty",
		"html:target/site/cucumber-pretty","json:target/reports/cucumber-RecordQueFeaturesStepFeaturesTest.json" }, tags = { "~@ignore" })
public class RecordQueFeaturesStepFeaturesTest {

}
