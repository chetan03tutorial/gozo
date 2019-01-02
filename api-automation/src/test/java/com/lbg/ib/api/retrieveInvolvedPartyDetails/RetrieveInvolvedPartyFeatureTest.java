package com.lbg.ib.api.retrieveInvolvedPartyDetails;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, 
    features = {"src/test/resources/retrieveInvolvedPartyDetails"}, 
    glue = {"com.lbg.ib.api.retrieveInvolvedPartyDetails"}, format = {"pretty", "html:target/site/cucumber-pretty", "json:target/reports/cucumber-retrieveInvolvedPartyDetails.json"}, tags = {"@ignore"})
public class RetrieveInvolvedPartyFeatureTest {

}
