package com.lbg.ib.api.BranchContextService;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/branchContext/BranchContextNoEmail.feature", format = { "pretty",
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-BranchContextServiceStepFeaturesTest.json" }, tags = { "~@ignore" })
public class BranchContextServiceStepFeaturesTest {
}
