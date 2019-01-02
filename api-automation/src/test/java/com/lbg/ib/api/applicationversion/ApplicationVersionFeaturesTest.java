package com.lbg.ib.api.applicationversion;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/applicationVersion", format = { "pretty",
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-ApplicationVersionStepFeaturesTest.json" }, tags = { "~@ignore" })
public class ApplicationVersionFeaturesTest {
}
