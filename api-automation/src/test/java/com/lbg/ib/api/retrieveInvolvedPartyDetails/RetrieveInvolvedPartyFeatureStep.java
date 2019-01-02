/**
 * 
 */
package com.lbg.ib.api.retrieveInvolvedPartyDetails;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.PathParameter;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import static org.junit.Assert.*;

public class RetrieveInvolvedPartyFeatureStep extends BaseFeatures {

    @Given("^the user has selected a particular product \"([^\"]*)\" of a particular brand \"([^\"]*)\"$")
    @RestInvoker(service = "retrieveProduct", method = HttpMethod.GET)
    public void the_user_has_selected_a_particular_product_of_a_particular_brand(@PathParameter(param = "urlIdentifier") String product, @Brand String brand) {

    }

    @Given("^the user has been offered \"([^\"]*)\" a product of that particular brand \"([^\"]*)\"$")
    @RestInvoker(service = "offerProduct", method = HttpMethod.POST)
    public void the_user_has_been_offered_a_product_of_that_particular_brand(@RequestBody(file = true) String offerRequest, @Brand String brand) {

    }

    @Then("^retrieve the involved party information for this user \"([^\"]*)\"$")
    @RestInvoker(service = "retrieveParty", method = HttpMethod.GET)
    public void retrieve_the_involved_party_information(@Brand String brand) {

    }

    @Then("^verify the email \"([^\"]*)\" of the party$")
    public void verify_the_of_the_party(String email) {
        assertTrue("Unexpected value of Email", email.equalsIgnoreCase((String)response.getPropValue("email")));
    }

}
