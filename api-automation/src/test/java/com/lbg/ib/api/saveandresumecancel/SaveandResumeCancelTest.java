package com.lbg.ib.api.saveandresumecancel;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false,features = "src/test/resources/snr/SaveandResumeCancel.feature", format = { "pretty",
        "html:target/site/cucumber-pretty", "json:target/reports/cucumber-SaveandResumeCancel.json" }, tags = { "~@ignore" })
public class SaveandResumeCancelTest{
}
