package com.lbg.ib.api.bankwizard;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/bankwizard", glue = {"com.lbg.ib.api.automation", "com.lbg.ib.api.bankwizard"}, format = {"pretty",
        "html:target/site/cucumber-pretty",
        "json:target/reports/cucumber-ValidateBankAccountTest.json"}, tags = {"~@ignore"})
public class ValidateBankAccountTest {

}
